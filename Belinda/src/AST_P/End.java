package AST_P;

public class End extends Command {
    @Override
    public Object visit(Visitor v, Object arg) {
        return v.visitEnd(this, arg);
    }
}
