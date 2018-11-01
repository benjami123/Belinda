package TO_PA_SC;

import AST_P.*;

public class Checker implements Visitor {

    private IdentificationTable table = new IdentificationTable();

    public void check( Program p )
    {
        p.visit( this, null );
    }

    @Override
    public Object visitProgram(Program program, Object arg) {
        table.openScope();
        program.getBlock().visit(this, null);
        table.closeScope();
        return null;
    }

    @Override
    public Object visitBlock(Block block, Object arg) {
        block.getDeclarations().visit(this, null);
        block.getCommands().visit(this, null);
        return null;
    }

    @Override
    public Object visitDeclarations(Declarations declarations, Object arg) {
        for (Declaration dec : declarations.declaration) {
            dec.visit(this, null);
        }
        return null;
    }

    @Override
    public Object visitCommands(Commands commands, Object arg) {
        for (Command com : commands.commands) {
            com.visit(this, null);
        }
        return null;
    }

    @Override
    public Object visitEnd(End end, Object arg) {
        end.visit(this, null);
        return null;
    }

    @Override
    public Object visitForLoop(ForLoop forLoop, Object arg) {
        forLoop.assignment.visit(this, null);
        forLoop.operation.visit(this, null);
        forLoop.operator.visit(this, null);
        forLoop.operator.visit(this, null);
        forLoop.commands.visit(this, null);
        return null;
    }

    @Override
    public Object visitFunctionCall(FunctionCall functionCall, Object arg) {
        functionCall.getFuncName().visit(this, null);
        for (Expression exp: functionCall.getArguments()) {
            exp.visit(this, null);
        }
        return null;
    }

    @Override
    public Object visitFunctionCallAlone(FunctionCallAlone functionCallAlone, Object arg) {
        functionCallAlone.getFuncName().visit(this, null);
        for (Expression exp: functionCallAlone.getArguments()) {
            exp.visit(this, null);
        }
        return null;
    }

    @Override
    public Object visitFunctionDeclaration(FunctionDeclaration functionDeclaration, Object arg) {
        functionDeclaration.getFuncName().visit(this, null);
        table.openScope();
        functionDeclaration.getTypeVars().visit(this, null);
        functionDeclaration.getBlock().visit(this, null);
        table.closeScope();
        return null;
    }

    @Override
    public Object visitGiveBackWith(GiveBackWith giveBackWith, Object arg) {
        giveBackWith.getExpression().visit(this, null);
        return null;
    }

    @Override
    public Object visitIfStatement(IfStatement ifStatement, Object arg) {
        ifStatement.getMainOperation().visit(this, null);
        ifStatement.getMainCommands().visit(this, null);
        for (Expression exp : ifStatement.getOtherOperations()) {
            exp.visit(this, null);
        }
        for (Commands com : ifStatement.getOtherCommands()) {
            com.visit(this, null);
        }
        return null;
    }

    @Override
    public Object visitInitializationTo0(InitializationTo0 initializationTo0, Object arg) {
        initializationTo0.getTypeVars().visit(this, null);
        return null;
    }

    @Override
    public Object visitLiteralNumber(LiteralNumber literalNumber, Object arg) {
        //TODO: implement
        return null;
    }

    @Override
    public Object visistNegation(Negation negation, Object arg) {
        negation.getVarNameOrLiteralNumber().visit(this, null);
        return null;
    }

    @Override
    public Object visistOperation(Operation operation, Object arg) {
        operation.getLeft().visit(this, null);
        operation.getOperator().visit(this, null);
        operation.getRight().visit(this, null);
        return null;
    }

    @Override
    public Object visitOperator(Operator operator, Object arg) {
        //TODO: implement
        return null;
    }

    @Override
    public Object visitSwitchCase(SwitchCase switchCase, Object arg) {
        for (LiteralNumber ln : switchCase.getLiteralNumbers()) {
            ln.visit(this, null);
        }
        switchCase.getCommands().visit(this, null);
        switchCase.getEnd().visit(this, null);
        return null;
    }

    @Override
    public Object visitSwitchStatement(SwitchStatement switchStatement, Object arg) {
        switchStatement.getVarName().visit(this, null);
        for (SwitchCase sc: switchStatement.getSwitchCases()) {
            sc.visit(this, null);
        }
        return null;
    }

    @Override
    public Object visitType(Type type, Object arg) {
        //TOdo: implement
        return null;
    }

    @Override
    public Object visitTypeVar(TypeVar typeVar, Object arg) {
        typeVar.getType().visit(this, null);
        typeVar.getVarName().visit(this, null);
        return null;
    }
    public Object visitTypeVars(TypeVars typeVars, Object arg){
        for (TypeVar tp :typeVars.getTypeVars()) {
            tp.visit(this, null);
        }
        return null;
    }

    @Override
    public Object visitVarName(VarName varName, Object arg) {
        //TOdo: implement
        return null;
    }

    @Override
    public Object visitWhileLoop(WhileLoop whileLoop, Object arg) {
        whileLoop.getOperation().visit(this, null);
        whileLoop.getCommands().visit(this, null);
        return null;
    }
}
