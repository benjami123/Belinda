package AST_P;

public class Operation extends Expression {
    private Expression left;
    private Operator operator;
    private Expression right;

    public Operation(Expression left, Operator operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public Expression getLeft() {
        return left;
    }

    public Operator getOperator() {
        return operator;
    }

    public Expression getRight() {
        return right;
    }
    @Override
    public Object visit(Visitor v, Object arg) {
        return v.visitOperation(this, arg);
    }

    public void setLeft(Expression temp) {
        left = temp;
    }

    public void setRight(Expression temp){
        right = temp;
    }
}
