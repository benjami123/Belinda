package AST_P;

public class ForLoop extends LoopBlock{
    Assignment assignment;
    Expression operation;
    Operator operator;
    Expression modifier;
    Commands commands;

    public ForLoop(Assignment assignment, Expression operation, Operator operator, Expression modifier, Commands commands) {
        this.assignment = assignment;
        this.operation = operation;
        this.operator = operator;
        this.modifier = modifier;
        this.commands = commands;
    }

}
