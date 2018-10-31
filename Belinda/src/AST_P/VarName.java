package AST_P;

public class VarName extends Expression {
    private String value;
    private boolean isFunction = false;
    private int sizeArray = 1;
    private Expression arrayValue;

    public VarName(String value, boolean isFunction) {
        this.value = value;
        this.isFunction = isFunction;
    }

    public VarName(String value) {
        this.value = value;
    }

    public VarName(String value, int arraySize){
        this.value = value;
        this.sizeArray = arraySize;
    }

    public String getVarValue(){
        return this.value;
    }

    public void addArrayValue(Expression expression){
        arrayValue = expression;
    }
}
