package AST_P;

import java.util.*;

public class IfStatement extends ConditionBlock {
    private Expression mainOperation;
    private Collection<Expression> otherOperations;
    private Commands mainCommands;
    private Collection<Commands> otherCommands;


    public IfStatement(Expression mainOperation, Collection<Expression> otherOperations, Commands mainCommands, Collection<Commands> otherCommands) {
        this.mainOperation = mainOperation;
        this.otherOperations = otherOperations;
        this.mainCommands = mainCommands;
        this.otherCommands = otherCommands;
    }

    public Expression getMainOperation() {
        return mainOperation;
    }

    public Collection<Expression> getOtherOperations() {
        return otherOperations;
    }

    public Commands getMainCommands() {
        return mainCommands;
    }

    public Collection<Commands> getOtherCommands() {
        return otherCommands;
    }
    @Override
    public Object visit(Visitor v, Object arg) {
        return v.visitIfStatement(this, arg);
    }
}
