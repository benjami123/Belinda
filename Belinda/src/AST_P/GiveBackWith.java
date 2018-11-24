package AST_P;

public class GiveBackWith extends Command {
    private Expression expression;
    private int numberOfArguments;

    public GiveBackWith(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    public int getNumberOfArguments() {
        return numberOfArguments;
    }

    public void setNumberOfArguments(int numberOfArguments) {
        this.numberOfArguments = numberOfArguments;
    }

    @Override
    public Object visit(Visitor v, Object arg) {
        return v.visitGiveBackWith(this, arg);
    }
}
