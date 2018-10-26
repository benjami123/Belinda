package AST_P;

import java.util.Collection;

public class Operation extends Expression {
    Expression left;
    Operator operator;
    Expression right;

    public Operation(Expression left, Operator operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
}
