package AST_P;

import java.util.*;

public class FunctionCall extends Expression {
    VarName funcName;
    Collection<Expression> arguments;
    int numberOfArg;

    public FunctionCall(VarName funcName, Collection<Expression> arguments) {
        this.funcName = funcName;
        this.arguments = arguments;
        this.numberOfArg = arguments.size();
    }

    public FunctionCall(VarName funcName) {
        this.funcName = funcName;
        this.arguments = null;
        this.numberOfArg = 0;
    }

    public VarName getFuncName() {
        return funcName;
    }

    public Collection<Expression> getArguments() {
        return arguments;
    }

    public int getNumberOfArg() {
        return numberOfArg;
    }
}
