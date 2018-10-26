package AST_P;

public class LiteralNumber extends Expression {
    int value;
    String type;

    public LiteralNumber(int value, String type) {
        this.value = value;
        this.type = type;
    }
}
