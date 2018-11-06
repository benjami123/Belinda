package AST_P;

import TO_PA_SC.Token;

import java.util.Collection;

public class SwitchCase extends AST {
    private Collection<LiteralNumber> literalNumbers;
    private Commands commands;
    private End end;

    public SwitchCase(Collection<LiteralNumber> literalNumbers, Commands commands, End end) {
        this.literalNumbers = literalNumbers;
        this.commands = commands;
        this.end = end;
    }

    public Collection<LiteralNumber> getLiteralNumbers() {
        return literalNumbers;
    }

    public Commands getCommands() {
        return commands;
    }

    public End getEnd() {
        return end;
    }

    public boolean isDefault(){
        return (literalNumbers == null);
    }

    @Override
    public Object visit(Visitor v, Object arg) {
        return v.visitSwitchCase(this, arg);
    }
}
