package AST_P;

import java.util.*;

public class FunctionCall extends Expression {
    private VarName funcName;
    private Collection<Expression> arguments;
    private int numberOfArg;

    public void setArguments(Collection<Expression> arguments) {
        this.arguments = arguments;
        numberOfArg = arguments.size();
    }

    public FunctionCall(VarName funcName, Collection<Expression> arguments) {
        this.funcName = funcName;
        this.arguments = arguments;
        this.numberOfArg = arguments.size();
    }

    public VarName getFuncName() {
        return funcName;
    }

    public Collection<Expression> getArguments() {
        return arguments;
    }

    public void setFuncName(VarName funcName) {
        this.funcName = funcName;
    }

    @Override
    public Object visit(Visitor v, Object arg) {
        return v.visitFunctionCall(this, arg);
    }
}
