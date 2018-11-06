package AST_P;


import java.util.*;


public class Commands extends AST {
    private Collection<Command> commands;


    public Commands(Collection<Command> command) {
        this.commands = command;
    }

    public Collection<Command> getCommands() {
        return commands;
    }

    @Override
    public Object visit(Visitor v, Object arg) {
        return v.visitCommands(this, arg);
    }
}
