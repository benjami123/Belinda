package AST_P;

import java.util.*;


public class SwitchStatement extends ConditionBlock {
    VarName varName;
    Collection<SwitchCase> switchCases;

    public SwitchStatement(VarName varName, Collection<SwitchCase> switchCases) {
        this.varName = varName;
        this.switchCases = switchCases;
    }
}
