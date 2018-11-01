package AST_P;

public interface Visitor {
    public Object visitProgram(Program program, Object arg);
    public Object visitBlock(Block block, Object arg);
    public Object visitDeclarations(Declarations declarations, Object arg);
    public Object visitCommands(Commands commands, Object arg);
    public Object visitEnd(End end, Object arg);
    public Object visitForLoop(ForLoop forLoop, Object arg);
    public Object visitFunctionCall(FunctionCall functionCall, Object arg);
    public Object visitFunctionCallAlone(FunctionCallAlone functionCallAlone, Object arg);
    public Object visitFunctionDeclaration(FunctionDeclaration functionDeclaration, Object arg);
    public Object visitGiveBackWith(GiveBackWith giveBackWith, Object arg);
    public Object visitIfStatement(IfStatement ifStatement, Object arg);
    public Object visitInitializationTo0(InitializationTo0 initializationTo0, Object arg);
    public Object visitLiteralNumber(LiteralNumber literalNumber, Object arg);
    public Object visistNegation(Negation negation, Object arg);
    public Object visistOperation(Operation operation, Object arg);
    public Object visitOperator(Operator operator, Object arg);
    public Object visitSwitchCase(SwitchCase switchCase, Object arg);
    public Object visitSwitchStatement(SwitchStatement switchStatement, Object arg);
    public Object visitType(Type type, Object arg);
    public Object visitTypeVar(TypeVar typeVar, Object arg);
    public Object visitTypeVars(TypeVars typeVars, Object arg);
    public Object visitVarName(VarName varName, Object arg);
    public Object visitWhileLoop(WhileLoop whileLoop, Object arg);















}
