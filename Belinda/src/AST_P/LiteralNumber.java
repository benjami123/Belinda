package AST_P;

public class LiteralNumber extends Expression {
    private String sValue;
    private int iValue;

    public LiteralNumber(String value) {
        this.sValue = value;
    }
    @Override
    public Object visit(Visitor v, Object arg) {
        return v.visitLiteralNumber(this, arg);
    }

    public String getsValue() {
        return sValue;
    }

    public int getiValue() {
        return iValue;
    }

    public void setiValue(int iValue) {
        this.iValue = iValue;
    }
}
