package AST_P;

import java.util.Vector;

public class SwitchStatement extends ConditionBlock {
    VarName varName;
    Vector<LiteralNumber> literalNumbers;
    Vector<Commands> commands;

    public SwitchStatement(VarName varName, Vector<LiteralNumber> literalNumbers, Vector<Commands> commands) {
        this.varName = varName;
        this.literalNumbers = literalNumbers;
        this.commands = commands;
    }
}
