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

    public void setArguments(Collection<Expression> arguments) {
        this.arguments = arguments;
        numberOfArg = arguments.size();
    }

    public VarName getFuncName() {
        return funcName;
    }

    public Collection<Expression> getArguments() {
        return arguments;
    }


    @Override
    public Object visit(Visitor v, Object arg) {
        return v.visitFunctionCallAlone(this, arg);
    }

    public void setFuncName(VarName funcName) {
        this.funcName = funcName;
    }
}
