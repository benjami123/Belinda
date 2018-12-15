package AST_P;

public interface Visitor {
    Object visitProgram(Program program, Object arg);
    Object visitBlock(Block block, Object arg);
    Object visitDeclarations(Declarations declarations, Object arg);
    Object visitInitializationTo0(InitializationTo0 initializationTo0, Object arg);
    Object visitTypeVars(TypeVars typeVars, Object arg);
    Object visitTypeVar(TypeVar typeVar, Object arg);
    Object visitFunctionDeclaration(FunctionDeclaration functionDeclaration, Object arg);
    Object visitCommands(Commands commands, Object arg);
    Object visitAssignment(Assignment assignment, Object arg);
    Object visitAssignmentOperator(AssignmentOperator assignmentOperator, Object arg);
    Object visitForLoop(ForLoop forLoop, Object arg);
    Object visitWhileLoop(WhileLoop whileLoop, Object arg);
    Object visitSwitchStatement(SwitchStatement switchStatement, Object arg);
    Object visitSwitchCase(SwitchCase switchCase, Object arg);
    Object visitIfStatement(IfStatement ifStatement, Object arg);
    Object visitFunctionCall(FunctionCall functionCall, Object arg);
    Object visitFunctionCallAlone(FunctionCallAlone functionCallAlone, Object arg);
    Object visitGiveBackWith(GiveBackWith giveBackWith, Object arg);
    Object visitOperation(Operation operation, Object arg);
    Object visitOperator(Operator operator, Object arg);
    Object visitNegation(Negation negation, Object arg);
    Object visitArrayEntry(ArrayEntry arrayEntry, Object arg);
    Object visitVarName(VarName varName, Object arg);
    Object visitLiteralNumber(LiteralNumber literalNumber, Object arg);
    Object visitEnd(End end, Object arg);
}
