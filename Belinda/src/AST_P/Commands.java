package AST_P;

import java.util.Vector;

public class Commands extends AST {
    public Vector<Command> commands = new Vector<Command>();

    public Commands() {
    }

    public Commands(Command... commands ) {
        for (Command c : commands) {
            this.commands.add(c);
        }
    }

    public Commands(Vector<Command> command) {
        this.commands = command;
    }
}
