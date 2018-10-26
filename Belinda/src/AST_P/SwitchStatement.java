package AST_P;

import java.util.*;


public class SwitchStatement extends ConditionBlock {
    VarName varName;
    Collection<LiteralNumber> literalNumbers;
    Collection<Commands> commands;

    public SwitchStatement(VarName varName, Collection<LiteralNumber> literalNumbers, Collection<Commands> commands) {
        this.varName = varName;
        this.literalNumbers = literalNumbers;
        this.commands = commands;
    }
}
