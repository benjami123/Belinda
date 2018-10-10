package AST;

public class Block extends AST {
    public Declarations declarations;
    public Commands commands;

    public Block(Declarations declarations, Commands commands) {
        this.declarations = declarations;
        this.commands = commands;
    }
}
