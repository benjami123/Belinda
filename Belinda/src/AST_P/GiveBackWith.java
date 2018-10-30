package AST_P;

public class GiveBackWith extends Command {
    Expression expression;

    public GiveBackWith(Expression expression) {
        this.expression = expression;
    }
}
