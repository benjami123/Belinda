package AST_P;

public class WhileLoop extends LoopBlock {
    Expression operation;
    Commands commands;

    public WhileLoop(Expression operation, Commands commands) {
        this.operation = operation;
        this.commands = commands;
    }
}
