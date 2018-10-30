package AST_P;

import TO_PA_SC.Token;

import java.util.Collection;

public class SwitchCase extends AST {
    Collection<LiteralNumber> literalNumbers;
    Commands commands;
    End end;

    public SwitchCase(Collection<LiteralNumber> literalNumbers, Commands commands, End end) {
        this.literalNumbers = literalNumbers;
        this.commands = commands;
        this.end = end;
    }

    public boolean isDefault(){
        return (literalNumbers == null);
    }
}
