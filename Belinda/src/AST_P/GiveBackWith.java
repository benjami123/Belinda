package AST_P;

public class GiveBackWith extends Command {
    private Expression expression;

    public GiveBackWith(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }
    @Override
    public Object visit(Visitor v, Object arg) {
        return v.visitGiveBackWith(this, arg);
    }
}
