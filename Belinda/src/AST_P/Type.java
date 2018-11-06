package AST_P;

public class Type extends AST {
    private String type;

    public Type(String type) {
        this.type = type;
    }
    @Override
    public Object visit(Visitor v, Object arg) {
        return v.visitType(this, arg);
    }
}
