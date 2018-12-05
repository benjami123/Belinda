package TO_PA_SC;

import AST_P.*;
import TAM.*;

import java.awt.event.MouseAdapter;
import java.io.*;
import java.util.ArrayList;

public class Encoder implements Visitor {

    private int nextAdr = Machine.CB;
    private int currentLevel = 0;
    private int programEnd;
    private int stackAddress = 0;

    private void emit( int op, int n, int r, int d )
    {
        if( n > 255 ) {
            System.out.println( "Operand too long" );
            n = 255;
        }

        Instruction instr = new Instruction();
        instr.op = op;
        instr.n = n;
        instr.r = r;
        instr.d = d;

        if( nextAdr >= Machine.PB )
            System.out.println( "Program too large" );
        else
            Machine.code[nextAdr++] = instr;
        System.out.println(nextAdr + " : " + instr );
    }

    private void patch( int adr, int d )
    {
        Machine.code[adr].d = d;
    }

    private int displayRegister( int currentLevel, int entityLevel )
    {
        if( entityLevel == 0 )
            return Machine.SBr;
        else if( currentLevel - entityLevel <= 6 )
            return Machine.LBr + currentLevel - entityLevel;
        else {
            System.out.println( "Accessing across to many levels" );
            return Machine.L6r;
        }
    }

    public void saveTargetProgram( String fileName )
    {
        try {
            DataOutputStream out = new DataOutputStream( new FileOutputStream( fileName ) );

            for( int i = Machine.CB; i < nextAdr; ++i )
                Machine.code[i].write( out );

            out.close();
        } catch( Exception ex ) {
            ex.printStackTrace();
            System.out.println( "Trouble writing " + fileName );
        }
    }

    public void encode( Program p )
    {
        p.visit( this, null );
    }

    @Override
    public Object visitProgram(Program program, Object arg) {
        currentLevel = 0;

        programEnd = (Integer) program.getBlock().visit( this, -1 );

        program.getBlock().visit( this, 0 );

        emit( Machine.HALTop, 0, 0, 0 );

        return null;
    }

    @Override
    public Object visitBlock(Block block, Object arg) {
        if((Integer)arg == -1){
            int varSizeAndFunc = (Integer) block.getDeclarations().visit(this, arg);
            int commandSize = (Integer) block.getCommands().visit(this, arg);
            return varSizeAndFunc + commandSize + 1; //1 for the jump
        }else if((Integer)arg == -2){
            return (Integer) block.getDeclarations().visit(this, arg)
                    + (Integer) block.getCommands().visit(this, arg);
        }
        int toJump = (Integer) block.getDeclarations().visit(this, -1);//for the jump

        block.getDeclarations().visit(this, toJump);
        block.getCommands().visit( this, arg );
        return null;
    }

    @Override
    public Object visitDeclarations(Declarations declarations, Object arg) {
        if((Integer) arg == -1){
            int size = 0;
            for( Declaration dec : declarations.getDeclaration() ){
                size += (Integer) dec.visit(this, arg);
            }
            return size;
        }else if((Integer) arg == -2) {
            int size = 0;
            for (Declaration dec : declarations.getDeclaration()) {
                size += (Integer) dec.visit(this, -1);
            }
            return size;
        }
        for( Declaration dec : declarations.getDeclaration() ){
            if(dec instanceof InitializationTo0){
                dec.visit(this, arg);
            }
        }
        int sizeOfDecalredVariables = 0;
        for (Declaration dec : declarations.getDeclaration()) {
            if(dec instanceof InitializationTo0){
                sizeOfDecalredVariables += (Integer)dec.visit(this, -1);
            }
        }
        int jumpValue = (Integer)arg - sizeOfDecalredVariables;
        emit(Machine.JUMPop, 0, Machine.CPr, jumpValue + 1);


        for( Declaration dec : declarations.getDeclaration() ){
            if(dec instanceof FunctionDeclaration){
                dec.visit(this, arg);
            }
        }
        return null;
    }

    @Override
    public Object visitInitializationTo0(InitializationTo0 initializationTo0, Object arg) {
        if((Integer) arg == -2){
            return 0;
        }
        return initializationTo0.getTypeVars().visit(this, arg);
    }

    @Override
    public Object visitTypeVars(TypeVars typeVars, Object arg) {
        if((Integer) arg == -1){
            int size = 0;
            for (TypeVar tv: typeVars.getTypeVars()) {
                size += (Integer) tv.visit(this, arg);
            }
            return size;
        }else if((Integer) arg == -2){
            return 0;
        }else if((Integer) arg == -3){
            for (int i = typeVars.getTypeVars().size() - 1; i >= 0 ; i--) {
                ((ArrayList<TypeVar>)typeVars.getTypeVars()).get(i).getVarName().setAddress(new Address(currentLevel, - (i+1)));
            }
            return 0;
        }
        for (TypeVar tv: typeVars.getTypeVars()) {
            tv.visit(this, arg);
        }
        return null;
    }

    @Override
    public Object visitTypeVar(TypeVar typeVar, Object arg) {
        if((Integer) arg == -1){
            return typeVar.getVarName().getArraySize();
        }
        int sizeVar = typeVar.getVarName().getArraySize();

        if((Integer) arg == -2){
            return null;
        }
        typeVar.getVarName().setAddress(new Address(0, stackAddress));
        for (int i = 0; i <sizeVar; i++) {
            emit(Machine.LOADLop, 0,0,0);
            stackAddress++;
        }
        return null;
    }

    @Override
    public Object visitFunctionDeclaration(FunctionDeclaration functionDeclaration, Object arg) {
        if((Integer) arg == -1){
            int sizeArguments =  (Integer) functionDeclaration.getTypeVars().visit(this, -2);
            int blockSize = (Integer)functionDeclaration.getBlock().visit(this, arg);
            return blockSize + sizeArguments + 1; //one for the return

        }else if((Integer) arg == -2){
            return 1 + (Integer) functionDeclaration.getBlock().visit(this, -1) +
                    (Integer) functionDeclaration.getTypeVars().visit(this, -2);
        }

        functionDeclaration.setAddress(new Address( currentLevel, nextAdr ));
        functionDeclaration.getFuncName().setAddress(new Address(currentLevel, nextAdr));
        ++currentLevel;
        functionDeclaration.getTypeVars().visit(this, -3);

/*        for (TypeVar tv: functionDeclaration.getTypeVars().getTypeVars()) {
            tv.getVarName().visit(this, arg);
            emit(Machine.STOREIop, 1, 0, 0);
        }*/

        functionDeclaration.getBlock().visit(this, arg);
        emit(Machine.RETURNop, 1, 0, 0);
        currentLevel--;

        return null;
    }

    @Override
    public Object visitFunctionCall(FunctionCall functionCall, Object arg) {
        if((Integer) arg == -1){
            int sizeArg = 0;
            for (Expression exp: functionCall.getArguments()) {
                if(functionCall.getFuncName().getVarValue().equals("printC")){
                    sizeArg+=1;
                }else if(functionCall.getFuncName().getVarValue().equals("printI")){
                    sizeArg+=1;
                }
                sizeArg += (Integer) exp.visit(this, arg);
                if(exp instanceof VarName || exp instanceof ArrayEntry){
                    sizeArg+=1;
                }
            }
            if(functionCall.getFuncName().getVarValue().equals("scan")){
                return 1;
            }
            return sizeArg + 1;
        }

        if(functionCall.getFuncName().getVarValue().equals("printC")){
            for (Expression exp: functionCall.getArguments()) {
                exp.visit(this, arg);
                if(exp instanceof VarName || exp instanceof ArrayEntry){
                    emit(Machine.LOADAop, 1, 0,0);
                }
                emit(Machine.CALLop, 0,Machine.PBr, Machine.putDisplacement);
                emit(Machine.CALLop, 0,Machine.PBr, Machine.puteolDisplacement);
            }
        }else if(functionCall.getFuncName().getVarValue().equals("printI")){
            for (Expression exp: functionCall.getArguments()) {
                exp.visit(this, arg);
                if(exp instanceof VarName || exp instanceof ArrayEntry){
                    emit(Machine.LOADIop, 1, 0,0);
                }
                emit(Machine.CALLop, 0,Machine.PBr, Machine.putintDisplacement);
                emit(Machine.CALLop, 0,Machine.PBr, Machine.puteolDisplacement);
            }
        }else if(functionCall.getFuncName().getVarValue().equals("scan")){
            emit(Machine.CALLop, 0, Machine.PBr, Machine.getintDisplacement);
        }else{
            for (int i = functionCall.getArguments().size() -1; i >= 0; i--) {
                Expression exp = ((ArrayList<Expression>)functionCall.getArguments()).get(i);
                if(exp instanceof LiteralNumber){
                    emit(Machine.LOADLop, 0 , 0, ((LiteralNumber) exp).getiValue());
                }else if(exp instanceof  VarName){
                    exp.visit(this, arg);
                    emit(Machine.LOADIop, 1, 0, 0);
                }
            }
            int funcAddress = functionCall.getFuncName().getAddress().displacement;
            emit(Machine.CALLop, Machine.SBr,  Machine.CBr,funcAddress +1);
        }
        return null;
    }

    @Override
    public Object visitFunctionCallAlone(FunctionCallAlone functionCallAlone, Object arg) {
        if((Integer) arg == -1){
            int sizeArg = 0;
            for (Expression exp: functionCallAlone.getArguments()) {
                if(functionCallAlone.getFuncName().getVarValue().equals("printC")){
                    sizeArg+=1;
                }else if(functionCallAlone.getFuncName().getVarValue().equals("printI")){
                    sizeArg+=1;
                }
                sizeArg += (Integer) exp.visit(this, arg);
                if(exp instanceof VarName || exp instanceof ArrayEntry){
                    sizeArg+=1;
                }
            }
            if(functionCallAlone.getFuncName().getVarValue().equals("scan")){
                return 1;
            }
            return sizeArg + 1;
        }



        if(functionCallAlone.getFuncName().getVarValue().equals("printC")){
            for (Expression exp: functionCallAlone.getArguments()) {
                exp.visit(this, arg);
                if(exp instanceof VarName || exp instanceof ArrayEntry){
                    emit(Machine.LOADIop, 1, 0,0);
                }
                emit(Machine.CALLop, 0,Machine.PBr, Machine.putDisplacement);
                emit(Machine.CALLop, 0,Machine.PBr, Machine.puteolDisplacement);
            }
        }else if(functionCallAlone.getFuncName().getVarValue().equals("printI")){
            for (Expression exp: functionCallAlone.getArguments()) {
                exp.visit(this, arg);
                if(exp instanceof VarName || exp instanceof ArrayEntry){
                    emit(Machine.LOADIop, 1, 0,0);
                }
                emit(Machine.CALLop, 0,Machine.PBr, Machine.putintDisplacement);
                emit(Machine.CALLop, 0,Machine.PBr, Machine.puteolDisplacement);

            }
        }else if(functionCallAlone.getFuncName().getVarValue().equals("scan")){
            emit(Machine.CALLop, 0, Machine.PBr, Machine.getintDisplacement);
        }else{
            for (int i = functionCallAlone.getArguments().size() -1; i >= 0; i--) {
                Expression exp = ((ArrayList<Expression>)functionCallAlone.getArguments()).get(i);
                if(exp instanceof LiteralNumber){
                    emit(Machine.LOADLop, 0 , 0, ((LiteralNumber) exp).getiValue());
                }else if(exp instanceof  VarName){
                    exp.visit(this, arg);
                    emit(Machine.LOADIop, 1, 0, 0);
                }
            }
            int funcAddress = functionCallAlone.getFuncName().getAddress().displacement;
            emit(Machine.CALLop, Machine.SBr,  Machine.CBr,funcAddress);
        }


        return null;
    }

    @Override
    public Object visitGiveBackWith(GiveBackWith giveBackWith, Object arg) {
        if((Integer)arg==-1){
            if(giveBackWith.getExpression() instanceof Nothing){
                return 1;
            }else if(giveBackWith.getExpression() instanceof VarName || giveBackWith.getExpression() instanceof ArrayEntry){
                return 3;
            }else {
                return (Integer) giveBackWith.getExpression().visit(this, arg) + 1;
            }
        }
        if(giveBackWith.getExpression() instanceof VarName || giveBackWith.getExpression() instanceof ArrayEntry){
            giveBackWith.getExpression().visit(this, arg);
            emit(Machine.LOADIop, 1, 0, 0);
        }else {
            giveBackWith.getExpression().visit(this, arg);
        }
        emit(Machine.RETURNop, 1, 0, 0);
        return null;
    }


    @Override
    public Object visitCommands(Commands commands, Object arg) {
        if((Integer) arg == -1){
            int size = 0;
            for (Command c : commands.getCommands()) {
                size+= (Integer) c.visit(this, arg);
            }
            return size;
        }
        for (Command c : commands.getCommands()) {
            c.visit(this, arg);
        }
        return null;
    }

    @Override
    public Object visitAssignment(Assignment assignment, Object arg) {
        if((Integer) arg == -1){
            int dis = (Integer) assignment.getExpression().visit(this, -1);
            if(assignment.getExpression() instanceof VarName){
                dis++;
            }
            dis += (Integer) assignment.getVarName().visit(this, -1);
            return dis +1;
        }
        assignment.getExpression().visit(this, arg);
        if(assignment.getExpression() instanceof VarName){
            emit(Machine.LOADIop, 1, 0, 0);
        }
        assignment.getVarName().visit(this, arg);
        emit(Machine.STOREIop, 1, 0, 0);
        return null;
    }

    @Override
    public Object visitWhileLoop(WhileLoop whileLoop, Object arg) {
        if((Integer) arg == -1){
            return (Integer) whileLoop.getOperation().visit(this, -1) + 2 ;
            //+3 because 1 command to lad, 2 to jump
        }

        int startAdr = nextAdr;

        whileLoop.getOperation().visit( this, arg);
        int jumpAdr = nextAdr;
        emit( Machine.JUMPIFop, 0, Machine.CBr, startAdr);

        whileLoop.getCommands().visit(this, jumpAdr);

        emit( Machine.JUMPop, 0, Machine.CBr, startAdr ); // update code pointer in TAM
        patch( jumpAdr, nextAdr ); //update code pointer in java
        return null;
    }

    @Override
    public Object visitSwitchStatement(SwitchStatement switchStatement, Object arg) {
        if((Integer) arg == -1){
            int cases = switchStatement.getNumberOfCases();
            int [] displacements = new int[cases];
            int i = 0;
            for (SwitchCase swc: switchStatement.getSwitchCases()) {
                displacements[i] = (Integer) swc.visit(this, -1);
                i++;
            }
            int currentCaseDisplacement;
            int numbersOfIfs = 0;
            for (SwitchCase swCs: switchStatement.getSwitchCases()) {
                if(swCs.getLiteralNumbers() != null){
                    for (LiteralNumber posibilities: swCs.getLiteralNumbers()) {
                        numbersOfIfs++;
                    }
                }else{
                    numbersOfIfs++;
                }
            }
            currentCaseDisplacement = numbersOfIfs;
            for(i = 0; i < displacements.length; i++){
                currentCaseDisplacement += displacements[i];
            }
            return currentCaseDisplacement;
        }
        int endAddress = (Integer) switchStatement.visit(this, -1);
        int cases = switchStatement.getNumberOfCases();
        int [] displacements = new int[cases];
        int i = 0;
        for (SwitchCase swc: switchStatement.getSwitchCases()) {
            //displacements[i] = nextAdr;
            displacements[i] = (Integer) swc.visit(this, -1);
            i++;
        }
        int currentCaseDisplacement = 0;
        int numbersOfIfs = 0;
        int varAddress = switchStatement.getVarName().getAddress().displacement;

        for (SwitchCase swCs: switchStatement.getSwitchCases()) {
            if(swCs.getLiteralNumbers() != null){
                numbersOfIfs += swCs.getLiteralNumbers().size();
            }else {
                numbersOfIfs++;
            }
        }
        currentCaseDisplacement = numbersOfIfs;
        emit(Machine.LOADop, 1, Machine.SBr, varAddress);
        for(i = 0; i < switchStatement.getSwitchCases().size() - 1; i++){
            for (LiteralNumber posibilities: ((ArrayList<SwitchCase>)switchStatement.getSwitchCases()).get(i).getLiteralNumbers()) {
                emit(Machine.JUMPIFop, posibilities.getiValue(), Machine.CPr, currentCaseDisplacement);
            }
            currentCaseDisplacement += displacements[i];
            currentCaseDisplacement--; //for the if that was just emited
        }
        emit(Machine.JUMPop, 0,  Machine.CPr, currentCaseDisplacement);
        currentCaseDisplacement += displacements[i];
        endAddress -= numbersOfIfs;
        for (SwitchCase swc: switchStatement.getSwitchCases()) {
            endAddress -=  (Integer) swc.visit(this, -1);
            swc.visit(this, endAddress);
        }
        return null;
    }

    @Override
    public Object visitSwitchCase(SwitchCase switchCase, Object arg) {
        if((Integer) arg == -1){
            return (Integer)switchCase.getCommands().visit(this, arg) + 1;//one for the end
        }

        switchCase.getCommands().visit(this, arg);
        switchCase.getEnd().visit(this, arg);
        return null;
    }

    @Override
    public Object visitEnd(End end, Object arg) {
        if((Integer) arg == -1){
            return 1;
        }
        emit(Machine.JUMPop,0, Machine.CPr, (Integer)arg + 1);
        return null;
    }

    @Override
    public Object visitLiteralNumber(LiteralNumber literalNumber, Object arg) {
        try {
            if ((Integer) arg == -1){
                return 1;
            }
        }catch (Exception e){

        }
        emit(Machine.LOADLop, 0,0, literalNumber.getiValue());
        return null;
    }

    @Override
    public Object visistNegation(Negation negation, Object arg) {
        try {
            if((Integer) arg == -1){
                return 2;
            }
        }catch (Exception e){

        }
        negation.getVarNameOrLiteralNumber().visit(this, null);
        emit(Machine.CALLop, 0 ,Machine.PBr, Machine.notDisplacement);
        return null;
    }


    @Override
    public Object visitOperator(Operator operator, Object arg) {
        try {
            if((Integer) arg == -1){
                return 1;
            }
        }catch (Exception e){

        }
        String str = operator.getSpelling();
        switch (operator.getOperator()){
            case '+':
                emit(Machine.CALLop, 0 ,Machine.PBr, Machine.addDisplacement);
                break;
            case '-':
                emit(Machine.CALLop, 0 ,Machine.PBr, Machine.subDisplacement);
                break;
            case '*':
                emit(Machine.CALLop, 0 ,Machine.PBr, Machine.multDisplacement);
                break;
            case '/':
                emit(Machine.CALLop, 0 ,Machine.PBr, Machine.divDisplacement);
                break;
            case '%':
                emit(Machine.CALLop, 0 ,Machine.PBr, Machine.modDisplacement);
                break;
            case '<':
                if(str.length() == 3){ // <=
                    emit(Machine.CALLop, 0 ,Machine.PBr, Machine.leDisplacement);
                }else{ // <
                    emit(Machine.CALLop, 0 ,Machine.PBr, Machine.ltDisplacement);
                }
                break;
            case '>':

                if(str.length() == 3){ // >=
                    emit(Machine.CALLop, 0 ,Machine.PBr, Machine.geDisplacement);
                }else{ // >
                    emit(Machine.CALLop, 0 ,Machine.PBr, Machine.gtDisplacement);
                }
                break;
            case '=':
                emit(Machine.CALLop, 1 ,Machine.PBr, Machine.eqDisplacement);
                break;
            default:
                System.out.println("Congratulation, you reached places that nobody ever been before");
                break;
        }
        return null;
    }

    @Override
    public Object visitAssignmentOperator(AssignmentOperator assignmentOperator, Object arg) {
        return null;
    }

    @Override
    public Object visitType(Type type, Object arg) {
        return null;
    }


    @Override
    public Object visistOperation(Operation operation, Object arg) {
        if((Integer) arg == -1) {
            int dis = (Integer) operation.getLeft().visit(this, arg);
            dis += (Integer) operation.getRight().visit(this, arg);
            if (operation.getLeft() instanceof VarName || operation.getLeft() instanceof ArrayEntry) {
                dis++;
            }
            if (operation.getRight() instanceof VarName || operation.getRight() instanceof ArrayEntry) {
                dis++;
            }
            return dis + 1;
        }
        operation.getLeft().visit(this, arg);
        if(operation.getLeft() instanceof VarName || operation.getLeft() instanceof ArrayEntry){
            emit(Machine.LOADIop, 1, 0, 0);
        }
        operation.getRight().visit(this, arg);
        if(operation.getRight() instanceof VarName || operation.getRight() instanceof ArrayEntry){
            emit(Machine.LOADIop, 1, 0, 0);
        }
        operation.getOperator().visit(this, arg);
        return null;
    }



    //ToDo:do arrays
    //ToDo Scan
    @Override
    public Object visitForLoop(ForLoop forLoop, Object arg) {
        if((Integer) arg == -1){
            return (Integer) forLoop.getAssignment().visit(this, arg)
                    + 1 //for rhe jump if
                    + (Integer) forLoop.getOperation().visit(this, arg)
                    + (Integer) forLoop.getCommands().visit(this, arg)
                    + (Integer) forLoop.getAssignment().getVarName().visit(this, arg)
                    + (Integer) forLoop.getModifier().visit(this, arg)
                    + (Integer) forLoop.getOperator().visit(this, arg)
                    + 1 //to load the address to be assigned
                    + 1 //to assign
                    + 1 ;//to jump
        }
        forLoop.getAssignment().visit(this, arg);
        int startAdr = nextAdr;
        int endAddress =  (Integer) forLoop.visit(this, -1)
                - (Integer) forLoop.getAssignment().visit(this, -1)
                - (Integer) forLoop.getOperation().visit(this, -1);
        forLoop.getOperation().visit(this, arg);
        emit( Machine.JUMPIFop, 0, Machine.CPr, endAddress + 1);
        forLoop.getCommands().visit(this, arg);
        forLoop.getAssignment().getVarName().visit(this, arg);
        emit(Machine.LOADIop, 1,0, 0);
        forLoop.getModifier().visit(this, arg);
        forLoop.getOperator().visit(this, arg);
        forLoop.getAssignment().getVarName().visit(this, arg);
        emit(Machine.STOREIop, 1, 0, 0);
        emit(Machine.JUMPop, 0, Machine.CBr, startAdr);
        return null;
    }

    @Override
    public Object visitIfStatement(IfStatement ifStatement, Object arg) {
        if((Integer) arg == -1){
            int mainOp = (Integer) ifStatement.getMainOperation().visit(this, arg)
                    + 1; //jump if
            int mainCom = (Integer) ifStatement.getMainCommands().visit(this, arg)
                    + 1; //jump at the end;
            mainOp+= mainCom;
            int otherOp = 0;
            for (int i = 0; i < ifStatement.getOtherCommands().size(); i++) {
                otherOp += (Integer) ((ArrayList<Expression>)ifStatement.getOtherOperations()).get(i).visit(this, arg)
                        + 1; //for the jump if
                otherOp += (Integer) ((ArrayList<Commands>)ifStatement.getOtherCommands()).get(i).visit(this, arg)
                        + 1; //one for the jump after the commands
            }
            return otherOp + mainOp;
        }

        int endAddress = (Integer) ifStatement.visit(this, -1) + 1; //beacause it has to jump after the last comand
        ifStatement.getMainOperation().visit(this, arg);
        endAddress -= (Integer) ifStatement.getMainOperation().visit(this, -1); //for the jumpif;
        endAddress--;
        int tempSize = (Integer) ifStatement.getMainCommands().visit(this, -1);
        tempSize += 1; //one for the jump
        emit( Machine.JUMPIFop, 0, Machine.CPr, tempSize + 1); //  and one because it has to jump over it)
        endAddress -= tempSize;
        ifStatement.getMainCommands().visit(this, arg);
        emit( Machine.JUMPop, 0, Machine.CPr, endAddress);
        endAddress--;
        for (int i = 0; i < ifStatement.getOtherCommands().size(); i++) {
            ((ArrayList<Expression>)ifStatement.getOtherOperations()).get(i).visit(this, arg);
            tempSize = (Integer) ((ArrayList<Expression>)ifStatement.getOtherOperations()).get(i).visit(this, -1) + 1;
            endAddress -= tempSize;
            tempSize = (Integer) ((ArrayList<Commands>)ifStatement.getOtherCommands()).get(i).visit(this, -1) +1;
            emit( Machine.JUMPIFop, 0, Machine.CPr, tempSize + 1);
            ((ArrayList<Commands>)ifStatement.getOtherCommands()).get(i).visit(this, arg);
            endAddress -= tempSize;
            emit( Machine.JUMPop, 0, Machine.CPr, endAddress + 1);
        }
        return null;
    }


    @Override
    public Object visitVarName(VarName varName, Object arg) {
        try {
            if((Integer) arg == -1){
                return 1;
            }
        }catch (Exception e){

        }
        if(varName.getAddress().displacement >= 0){
            emit(Machine.LOADAop, 0, Machine.SBr, varName.getAddress().displacement);
        }else{
            emit(Machine.LOADAop, 0, Machine.LBr,  varName.getAddress().displacement);
        }
        return null;
    }

    @Override
    public Object visitArrayEntry(ArrayEntry arrayEntry, Object arg) {
        if((Integer) arg == -1){
            if(arrayEntry.getIndex() instanceof VarName){
                return 3 + (Integer) arrayEntry.getIndex().visit(this, arg);
            }
            return 2 + (Integer) arrayEntry.getIndex().visit(this, arg);
        }
        int disp = arrayEntry.getFather().getAddress().displacement;
        arrayEntry.getIndex().visit(this, arg);
        if(arrayEntry.getIndex() instanceof VarName){
            emit(Machine.LOADIop, 1, 0,0);
        }
        emit(Machine.LOADLop, 0,0, disp);
        emit(Machine.CALLop, 0 ,Machine.PBr, Machine.addDisplacement);
        return null;
    }
}