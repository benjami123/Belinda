package AST_P;

import java.util.Collection;

public class Operation extends Expression {
    Collection<Expression> expressions;
    Collection<Operator> operators;

    public Operation(Collection<Expression> expressions, Collection<Operator> operators) {
        this.expressions = expressions;
        this.operators = operators;
    }
}
