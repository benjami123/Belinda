package TO_PA_SC;

import AST_P.*;
import TAM.*;
import java.io.*;
import java.util.ArrayList;

public class Encoder implements Visitor {

    private int nextAdr = Machine.CB;
    private int currentLevel = 0;

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

        program.getBlock().visit( this, new Address() );

        emit( Machine.HALTop, 0, 0, 0 );

        return null;
    }

    @Override
    public Object visitBlock(Block block, Object arg) {
        int before = nextAdr;
        emit( Machine.JUMPop, 0, Machine.CB, 0 );

        int size = ((Integer) block.getDeclarations().visit( this, arg )).intValue();

        patch( before, nextAdr );
        for(int i = 0; i < size; i++){
            emit(Machine.LOADLop, 0, 0, 0);
            //emit( Machine.PUSHop, 0, 0, 1); //if the code doesn't work uncoment
        }
        block.getCommands().visit( this, null );
        return size;
    }

    @Override
    public Object visitDeclarations(Declarations declarations, Object arg) {
        int startDisplacement = ((Address) arg).displacement;
        for( Declaration dec : declarations.getDeclaration() ){
            arg = new Address(((Address) arg).level, (Integer) dec.visit( this, arg ));
        }
        Address adr = (Address) arg;
        int size = adr.displacement - startDisplacement;
        return new Integer( size );
    }

    @Override
    public Object visitInitializationTo0(InitializationTo0 initializationTo0, Object arg) {
        return initializationTo0.getTypeVars().visit(this, arg);
    }

    @Override
    public Object visitTypeVars(TypeVars typeVars, Object arg) {

        for (TypeVar tv: typeVars.getTypeVars()) {
            arg = tv.visit(this, arg);
        }
        Address adr = (Address) arg;
        return adr.displacement;
    }

    @Override
    public Object visitTypeVar(TypeVar typeVar, Object arg) {
        int sizeVar = typeVar.getVarName().getArraySize();
        typeVar.getVarName().setAddress((Address) arg);
        return new Address((Address) arg, sizeVar);
    }

    @Override
    public Object visitFunctionDeclaration(FunctionDeclaration functionDeclaration, Object arg) {
        functionDeclaration.setAddress(new Address( currentLevel, nextAdr ));

        ++currentLevel;

        Address adr = new Address( (Address) arg );

        int size = ((Integer) functionDeclaration.getTypeVars().visit( this, adr ) ).intValue();

        size += (Integer) functionDeclaration.getTypeVars().visit( this, new Address( adr, -size ) );

        functionDeclaration.getBlock().visit(this, new Address(adr, Machine.linkDataSize));

        emit( Machine.RETURNop, 1, 0, size );

        currentLevel--;

        return arg;
    }


    @Override
    public Object visitCommands(Commands commands, Object arg) {
        for (Command c : commands.getCommands()) {
            c.visit(this, null);
        }
        return null;
    }

    @Override
    public Object visitAssignment(Assignment assignment, Object arg) {
        try {
            if((Integer) arg == -1){
                int dis = (Integer) assignment.getExpression().visit(this, -1);
                dis += (Integer) assignment.getVarName().visit(this, -1);
                return dis +1;
            }
        }catch (Exception e){

        }
        assignment.getExpression().visit(this, null);
        assignment.getVarName().visit(this, null);
        emit(Machine.STOREIop, 1, 0, 0);
        return null;
    }

    @Override
    public Object visitWhileLoop(WhileLoop whileLoop, Object arg) {
        try {
            if((Integer) arg == -1){
                return (Integer) whileLoop.getOperation().visit(this, -1) + 2 ;
                //+3 because 1 command to lad, 2 to jump
            }
        }catch (Exception e){

        }

        int startAdr = nextAdr;

        whileLoop.getOperation().visit( this, null );
        int jumpAdr = nextAdr;
        emit( Machine.JUMPIFop, 0, Machine.CBr, startAdr);

        whileLoop.getCommands().visit(this, jumpAdr);

        emit( Machine.JUMPop, 0, Machine.CBr, startAdr ); // update code pointer in TAM
        patch( jumpAdr, nextAdr ); //update code pointer in java
        return null;
    }

    @Override
    public Object visitSwitchStatement(SwitchStatement switchStatement, Object arg) {
        try {
            Integer integer = (Integer) arg;
            if(integer == -1){
                int cases = switchStatement.getNumberOfCases();
                int [] displacements = new int[cases];
                int i = 0;
                for (SwitchCase swc: switchStatement.getSwitchCases()) {
                    i++;
                    displacements[i] = (Integer) swc.visit(this, -1);
                }
                int currentCaseDisplacement;
                int numbersOfIfs = 0;
                for (SwitchCase swCs: switchStatement.getSwitchCases()) {
                    for (LiteralNumber posibilities: swCs.getLiteralNumbers()) {
                        numbersOfIfs++;
                    }
                }
                currentCaseDisplacement = numbersOfIfs + 1;
                for(i = 0; i < displacements.length; i++){
                    currentCaseDisplacement += displacements[i];
                }
                return currentCaseDisplacement;
            }
        }catch (Exception e){

        }
        int cases = switchStatement.getNumberOfCases();
        int [] displacements = new int[cases];
        int i = 0;
        for (SwitchCase swc: switchStatement.getSwitchCases()) {
            //displacements[i] = nextAdr;
            i++;
            displacements[i] = (Integer) swc.visit(this, -1);
        }
        i = 0;
        int currentCaseDisplacement = 0;
        int numbersOfIfs = 0;
        int varAddress = switchStatement.getVarName().getAddress().displacement;

        for (SwitchCase swCs: switchStatement.getSwitchCases()) {
                numbersOfIfs += swCs.getLiteralNumbers().size();
        }
        currentCaseDisplacement = numbersOfIfs + 1;
        emit(Machine.LOADop, 1, varAddress, 0);
        for(i = 0; i < switchStatement.getSwitchCases().size() - 1; i++){
            for (LiteralNumber posibilities: ((ArrayList<SwitchCase>)switchStatement.getSwitchCases()).get(i).getLiteralNumbers()) {
                emit(Machine.JUMPIFop, posibilities.getiValue(), Machine.CBr, nextAdr + currentCaseDisplacement);
            }
            currentCaseDisplacement += displacements[i];
        }
        emit(Machine.JUMPop, 0,  Machine.CBr,nextAdr + currentCaseDisplacement);
        currentCaseDisplacement += displacements[i + 1];
        //patch( addreses[i], nextAdr ); maybe

        int switchEndAddress = nextAdr; //        ------ //TOdO:: check
        for (SwitchCase swc: switchStatement.getSwitchCases()) {
            swc.visit(this, currentCaseDisplacement);
        }
        return null;
    }

    @Override
    public Object visitSwitchCase(SwitchCase switchCase, Object arg) {
        try {
            if((Integer) arg == -1){
                int startAddress  = 0;
                int endAddress = (Integer) switchCase.getCommands().visit(this, arg);
                //increase each dispacement by one because of the break
                return endAddress - startAddress;
            }
        }catch (Exception e){

        }

        switchCase.getCommands().visit(this, arg);
        return null;
    }

    @Override
    public Object visitEnd(End end, Object arg) {
        try {
            Integer i = (Integer) arg;
            if(i == -1){
                return 1;
            }
        }catch (Exception e){

        }
        emit(Machine.JUMPop,0, Machine.CBr, (Integer)arg);
        return null;
    }

    @Override
    public Object visitFunctionCall(FunctionCall functionCall, Object arg) {
        try {
            if((Integer) arg == -1){
                int nArg  = functionCall.getNumberOfArg();

                if(functionCall.getFuncName().getVarValue().equals("printC")){
                    return nArg + 2;
                }else if(functionCall.getFuncName().getVarValue().equals("printI")){
                    return nArg + 4;
                }else {
                    return nArg + 1;
                }
            }
        }catch (Exception e){

        }

        if(functionCall.getFuncName().getVarValue().equals("printC")){
            for (Expression exp: functionCall.getArguments()) {
                exp.visit(this, arg);
                emit(Machine.CALLop, 0,Machine.PBr, Machine.putintDisplacement);
                emit(Machine.CALLop, 0,Machine.PBr, Machine.puteolDisplacement);
            }
        }else if(functionCall.getFuncName().getVarValue().equals("printI")){
            for (Expression exp: functionCall.getArguments()) {
                exp.visit(this, arg);
                emit(Machine.LOADLop, 0,0, 48);
                emit(Machine.CALLop, 0, Machine.PBr, Machine.addDisplacement);
                emit(Machine.CALLop, 0,Machine.PBr, Machine.putintDisplacement);
                emit(Machine.CALLop, 0,Machine.PBr, Machine.puteolDisplacement);
            }
        }else if(functionCall.getFuncName().getVarValue().equals("scan")){
            emit(Machine.CALLop, 0,Machine.PBr, Machine.getintDisplacement);
        }
        for (Expression exp: functionCall.getArguments()) {
            if(exp instanceof LiteralNumber){
                emit(Machine.LOADLop, 0 , 0, ((LiteralNumber) exp).getiValue());
            }else if(exp instanceof  VarName){
                int addresVariable = ((VarName) exp).getAddress().displacement;
                emit(Machine.LOADop, 1, addresVariable, Machine.SBr);
            }
        }
        int funcAddress = functionCall.getFuncName().getAddress().displacement;
        emit(Machine.JUMPop, 0, funcAddress, Machine.CBr);
        return null;
    }

    @Override
    public Object visitFunctionCallAlone(FunctionCallAlone functionCallAlone, Object arg) {
        try {
            if((Integer) arg == -1){
                int nArg  = functionCallAlone.getNumberOfArg();

                if(functionCallAlone.getFuncName().getVarValue().equals("printC")){
                    return nArg + 2;
                }else if(functionCallAlone.getFuncName().getVarValue().equals("printI")){
                    return nArg + 4;
                }else {
                    return nArg + 1;
                }
            }
        }catch (Exception e){

        }

        if(functionCallAlone.getFuncName().getVarValue().equals("printC")){
            for (Expression exp: functionCallAlone.getArguments()) {
                exp.visit(this, arg);
                emit(Machine.CALLop, 0,Machine.PBr, Machine.putintDisplacement);
                emit(Machine.CALLop, 0,Machine.PBr, Machine.puteolDisplacement);
            }
        }else if(functionCallAlone.getFuncName().getVarValue().equals("printI")){
            for (Expression exp: functionCallAlone.getArguments()) {
                exp.visit(this, arg);
                emit(Machine.LOADLop, 0,0, 48);
                emit(Machine.CALLop, 0, Machine.PBr, Machine.addDisplacement);
                emit(Machine.CALLop, 0,Machine.PBr, Machine.putintDisplacement);
                emit(Machine.CALLop, 0,Machine.PBr, Machine.puteolDisplacement);
            }
        }else if(functionCallAlone.getFuncName().getVarValue().equals("scan")){
            emit(Machine.CALLop, 0,Machine.PBr, Machine.getintDisplacement);
        }
        for (Expression exp: functionCallAlone.getArguments()) {
            if(exp instanceof LiteralNumber){
                emit(Machine.LOADLop, 0 , 0, ((LiteralNumber) exp).getiValue());
            }else if(exp instanceof  VarName){
                int addresVariable = ((VarName) exp).getAddress().displacement;
                emit(Machine.LOADop, 1, addresVariable, Machine.SBr);
            }
        }
        int funcAddress = functionCallAlone.getFuncName().getAddress().displacement;
        emit(Machine.JUMPop, 0, funcAddress, Machine.CBr);
        return null;
    }

    @Override
    public Object visitGiveBackWith(GiveBackWith giveBackWith, Object arg) {
        try {
            if((Integer) arg == -1){
                return 1;
            }
        }catch (Exception e){

        }
        emit(Machine.RETURNop, 1, 0, giveBackWith.getNumberOfArguments());
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
        emit(Machine.CALLop, 0 ,0, Machine.notDisplacement);
        return null;
    }


    @Override
    public Object visitOperator(Operator operator, Object arg) {
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
        try {
            if((Integer) arg == -1){
                int dis = (Integer) operation.getLeft().visit(this, arg);
                dis += (Integer) operation.getRight().visit(this, arg);
                return dis + 1;
            }
        }catch (Exception e){

        }
        String str = operation.getOperator().getSpelling();
        operation.getLeft().visit(this, arg);
        operation.getRight().visit(this, arg);
        switch (operation.getOperator().getOperator()){
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
                emit(Machine.CALLop, 0 ,Machine.PBr, Machine.eqDisplacement);
                break;
            default:
                if(str.equals(".and")){
                    emit(Machine.CALLop, 0 ,Machine.PBr, Machine.andDisplacement);
                }else if(str.equals(".or")){
                    emit(Machine.CALLop, 0 ,Machine.PBr, Machine.orDisplacement);
                }else{
                    System.out.println("Congratulation, you reached places that nobody ever been before");
                }
                break;
        }
        return null;
    }



    //ToDo: visitVarName has to: Load it on the stack, calcualate its adress when declared, maybe manage integer/char conversion
    //ToDo: debug everything, specially loops and ifs

    @Override
    public Object visitForLoop(ForLoop forLoop, Object arg) {
        return null;
    }

    @Override
    public Object visitIfStatement(IfStatement ifStatement, Object arg) {
        return null;
    }


    @Override
    public Object visitVarName(VarName varName, Object arg) {
        return null;
    }

    @Override
    public Object visitArrayEntry(ArrayEntry arrayEntry, Object arg) {
        return null;
    }
}