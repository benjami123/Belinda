package AST_P;

public class ForLoop extends LoopBlock{
    private Assignment assignment;
    private Expression operation;
    private Operator operator;
    private Expression modifier;
    private Commands commands;

    public ForLoop(Assignment assignment, Expression operation, Operator operator, Expression modifier, Commands commands) {
        this.assignment = assignment;
        this.operation = operation;
        this.operator = operator;
        this.modifier = modifier;
        this.commands = commands;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public Expression getOperation() {
        return operation;
    }

    public Operator getOperator() {
        return operator;
    }

    public Expression getModifier() {
        return modifier;
    }

    public Commands getCommands() {
        return commands;
    }

    @Override
    public Object visit(Visitor v, Object arg) {
        return v.visitForLoop(this, arg);
    }
}
