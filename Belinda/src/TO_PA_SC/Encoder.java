package TO_PA_SC;

import AST_P.*;
import TAM.*;
import java.io.*;

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
        Expression destination = assignment.getVarName();
        Expression value = assignment.getExpression();


        return null;
    }







    @Override
    public Object visitAssignmentOperator(AssignmentOperator assignmentOperator, Object arg) {
        return null;
    }

    @Override
    public Object visitEnd(End end, Object arg) {
        return null;
    }

    @Override
    public Object visitForLoop(ForLoop forLoop, Object arg) {
        return null;
    }

    @Override
    public Object visitFunctionCall(FunctionCall functionCall, Object arg) {
        return null;
    }

    @Override
    public Object visitFunctionCallAlone(FunctionCallAlone functionCallAlone, Object arg) {
        return null;
    }

    @Override
    public Object visitGiveBackWith(GiveBackWith giveBackWith, Object arg) {
        return null;
    }

    @Override
    public Object visitIfStatement(IfStatement ifStatement, Object arg) {
        return null;
    }

    @Override
    public Object visitLiteralNumber(LiteralNumber literalNumber, Object arg) {
        return null;
    }

    @Override
    public Object visistNegation(Negation negation, Object arg) {
        return null;
    }

    @Override
    public Object visistOperation(Operation operation, Object arg) {
        switch (operation.getOperator().getOperator()){
            case '+':
                break;
            case '-':
                break;
            case '*':
                break;
            case '/':
                break;
            case '%':
                break;
            case '<':
                break;
                
        }
        return null;
    }

    @Override
    public Object visitOperator(Operator operator, Object arg) {
        return null;
    }

    @Override
    public Object visitWhileLoop(WhileLoop whileLoop, Object arg) {
        return null;
    }

    @Override
    public Object visitSwitchCase(SwitchCase switchCase, Object arg) {
        return null;
    }

    @Override
    public Object visitSwitchStatement(SwitchStatement switchStatement, Object arg) {
        return null;
    }



    @Override
    public Object visitType(Type type, Object arg) {
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