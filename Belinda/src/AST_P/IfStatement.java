package AST_P;

import java.util.*;

public class IfStatement extends ConditionBlock {
    Expression mainOperation;
    Collection<Expression> otherOperations;
    Commands mainCommands;
    Collection<Commands> otherCommands;


    public IfStatement(Expression mainOperation, Collection<Expression> otherOperations, Commands mainCommands, Collection<Commands> otherCommands) {
        this.mainOperation = mainOperation;
        this.otherOperations = otherOperations;
        this.mainCommands = mainCommands;
        this.otherCommands = otherCommands;
    }
}
