package AST_P;

public class ArrayEntry extends Expression{
    private VarName father;
    private Expression index;
    private Expression value;

    public ArrayEntry(VarName varName, Expression index, Expression value) {
        this.index = index;
        this.value = value;
        this.father = varName;
    }

    public Expression getIndex() {
        return index;
    }

    public void setIndex(Expression index) {
        this.index = index;
    }

    public Expression getValue() {
        return value;
    }

    public void setValue(Expression value) {
        this.value = value;
    }

    public VarName getFather() {
        return father;
    }

    public void setFather(VarName father) {
        this.father = father;
    }

    @Override
    public Object visit(Visitor v, Object arg) {
        return v.visitArrayEntry(this, arg);
    }
}
