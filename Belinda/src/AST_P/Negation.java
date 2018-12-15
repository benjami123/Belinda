package AST_P;

public class Negation extends Expression {
    public void setVarNameOrLiteralNumber(Expression varNameOrLiteralNumber) {
        this.varNameOrLiteralNumber = varNameOrLiteralNumber;
    }

    private Expression varNameOrLiteralNumber;



    public Negation(VarName varName) {
        this.varNameOrLiteralNumber = varName;
    }

    public Negation(LiteralNumber literalNumber) {
        this.varNameOrLiteralNumber = literalNumber;
    }

    public Expression getVarNameOrLiteralNumber() {
        return varNameOrLiteralNumber;
    }
    @Override
    public Object visit(Visitor v, Object arg) {
        return v.visitNegation(this, arg);
    }
}
