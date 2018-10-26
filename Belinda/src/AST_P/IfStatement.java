package AST_P;

import java.util.Vector;

public class IfStatement extends ConditionBlock {
    Operation mainOperation;
    Vector<Operation> otherOperations;
    Commands mainCommands;
    Vector<Commands> otherCommands;


    public IfStatement(Operation mainOperation, Vector<Operation> otherOperations, Commands mainCommands, Vector<Commands> otherCommands) {
        this.mainOperation = mainOperation;
        this.otherOperations = otherOperations;
        this.mainCommands = mainCommands;
        this.otherCommands = otherCommands;
    }
}
