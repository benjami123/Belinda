package AST_P;

public class VarName extends Expression {
    String value;
    boolean isFunction = false;

    public VarName(String value, boolean isFunction) {
        this.value = value;
        this.isFunction = isFunction;
    }

    public VarName(String value) {
        this.value = value;
    }

    public String getVarValue(){
        return this.value;
    }
}
