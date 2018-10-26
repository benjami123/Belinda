package AST_P;

import java.util.*;

public class IfStatement extends ConditionBlock {
    Operation mainOperation;
    Collection<Operation> otherOperations;
    Commands mainCommands;
    Collection<Commands> otherCommands;


    public IfStatement(Operation mainOperation, Collection<Operation> otherOperations, Commands mainCommands, Collection<Commands> otherCommands) {
        this.mainOperation = mainOperation;
        this.otherOperations = otherOperations;
        this.mainCommands = mainCommands;
        this.otherCommands = otherCommands;
    }
}
