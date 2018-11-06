package AST_P;

public class Negation extends Expression {
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
        return v.visistNegation(this, arg);
    }
}
