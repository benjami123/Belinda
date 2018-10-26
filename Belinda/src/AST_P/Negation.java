package AST_P;

public class Negation extends Expression {
    VarName varName;
    LiteralNumber literalNumber;


    public Negation(VarName varName) {
        this.varName = varName;
    }

    public Negation(LiteralNumber literalNumber) {
        this.literalNumber = literalNumber;
    }
}
