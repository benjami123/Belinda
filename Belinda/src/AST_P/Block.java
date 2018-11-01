package AST_P;

public class Block extends AST {
    private Declarations declarations;
    private  Commands commands;

    public Block(Declarations declarations, Commands commands) {
        this.declarations = declarations;
        this.commands = commands;
    }

    public Declarations getDeclarations() {
        return declarations;
    }

    public Commands getCommands() {
        return commands;
    }
}
