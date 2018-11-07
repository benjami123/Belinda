package AST_P;

import java.util.ArrayList;
import java.util.Collection;

public class VarName extends Expression {
    private String value;
    private boolean isFunction = false;
    private int sizeArray = 1;
    private Collection<ArrayEntry> arrayEntries;

    public VarName(String value, boolean isFunction) {
        this.value = value;
        this.isFunction = isFunction;
        this.arrayEntries = new ArrayList<>(0); //the function name holds no value

    }

    public VarName(String value) {
        this.value = value;
        this.arrayEntries = new ArrayList<>(1); //the normal variable holds one value
    }

    public VarName(String value, int arraySize){
        this.value = value;
        this.sizeArray = arraySize;
        this.arrayEntries = new ArrayList<>(arraySize);
    }

    public String getVarValue(){
        return this.value;
    }

    public void setArrayValue(Expression expression, Expression index){
        arrayEntries.add(new ArrayEntry(this, index, expression));
    }

    public void addArrayEntry(ArrayEntry ae){
        arrayEntries.add(ae);
    }

    public void setArrayValue(Expression expression){
        ((ArrayList<ArrayEntry>)arrayEntries).remove(0);
        arrayEntries.add(new ArrayEntry(this, null, expression));
    }

    public boolean isFunction(){
        return isFunction;
    }
    @Override
    public Object visit(Visitor v, Object arg) {
        return v.visitVarName(this, arg);
    }

}
