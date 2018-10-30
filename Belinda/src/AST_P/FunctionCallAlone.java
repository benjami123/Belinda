package AST_P;

import java.util.Collection;

public class FunctionCallAlone extends AssignmentOrFunctionCallAlone {
    VarName funcName;
    Collection<Expression> arguments;
    int numberOfArg;

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
}
