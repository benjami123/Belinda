package AST_P;

public class Operator extends AST {
    private String spelling;

    public Operator(String spelling) {
        this.spelling = spelling;
    }
    @Override
    public Object visit(Visitor v, Object arg) {
        return v.visitOperator(this, arg);
    }

    public char getOperator(){
        return spelling.toCharArray()[1];
    }

    public String getSpelling() {
        return spelling;
    }
}
