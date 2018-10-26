package AST_P;

public class WhileLoop extends LoopBlock {
    Operation operation;
    Commands commands;

    public WhileLoop(Operation operation, Commands commands) {
        this.operation = operation;
        this.commands = commands;
    }
}
