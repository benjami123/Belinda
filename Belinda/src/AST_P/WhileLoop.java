package AST_P;

public class WhileLoop extends LoopBlock {
    private Expression operation;
    private Commands commands;

    public WhileLoop(Expression operation, Commands commands) {
        this.operation = operation;
        this.commands = commands;
    }

    public Expression getOperation() {
        return operation;
    }

    public Commands getCommands() {
        return commands;
    }
}
