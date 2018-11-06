package AST_P;

public class Nothing extends Expression {
    @Override
    public Object visit(Visitor v, Object arg) {
        return null;
    }
}
