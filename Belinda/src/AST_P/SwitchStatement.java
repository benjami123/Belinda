package AST_P;

import java.util.*;


public class SwitchStatement extends ConditionBlock {
    private VarName varName;
    private Collection<SwitchCase> switchCases;

    public SwitchStatement(VarName varName, Collection<SwitchCase> switchCases) {
        this.varName = varName;
        this.switchCases = switchCases;
    }

    public VarName getVarName() {
        return varName;
    }

    public Collection<SwitchCase> getSwitchCases() {
        return switchCases;
    }
    @Override
    public Object visit(Visitor v, Object arg) {
        return v.visitSwitchStatement(this, arg);
    }

    public int getNumberOfCases(){
        return switchCases.size();
    }
}
