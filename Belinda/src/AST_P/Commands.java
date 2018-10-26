package AST_P;


import java.util.*;


public class Commands extends AST {
    public Collection<Command> commands;


    public Commands(Command... commands ) {
        this.commands = new ArrayList<>();
        for (Command c : commands) {
            this.commands.add(c);
        }
    }

    public Commands(Collection<Command> command) {
        this.commands = command;
    }
}
