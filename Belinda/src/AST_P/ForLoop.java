package AST_P;

public class ForLoop extends LoopBlock{
    Assignment assignment;
    Operation operation;
    Operator operator;
    LiteralNumber literalNumber;
    Commands commands;

    public ForLoop(Assignment assignment, Operation operation, Operator operator, LiteralNumber literalNumber, Commands commands) {
        this.assignment = assignment;
        this.operation = operation;
        this.operator = operator;
        this.literalNumber = literalNumber;
        this.commands = commands;
    }

}
