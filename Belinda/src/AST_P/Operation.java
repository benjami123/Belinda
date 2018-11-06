package AST_P;

import java.util.Collection;

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
        return v.visistOperation(this, arg);
    }
}
