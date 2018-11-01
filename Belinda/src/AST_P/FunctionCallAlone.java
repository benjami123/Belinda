package AST_P;

import java.util.Collection;

public class FunctionCallAlone extends AssignmentOrFunctionCallAlone {
    private VarName funcName;
    private Collection<Expression> arguments;
    private int numberOfArg;

    public FunctionCallAlone(VarName funcName, Collection<Expression> arguments) {
        this.funcName = funcName;
        this.arguments = arguments;
        this.numberOfArg = arguments.size();
    }

    public FunctionCallAlone(VarName funcName) {
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
