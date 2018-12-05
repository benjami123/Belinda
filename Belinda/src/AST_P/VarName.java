package AST_P;

import TO_PA_SC.Address;

import java.util.ArrayList;
import java.util.Collection;

public class VarName extends Expression {

    private String value;
    private boolean isFunction = false;
    private int sizeArray = 0;
    private Collection<ArrayEntry> arrayEntries;
    private Address address;

    public VarName(String value, boolean isFunction) {
        this.value = value;
        this.isFunction = isFunction;
        this.arrayEntries = new ArrayList<>(0); //the function name holds no value
        sizeArray = 0;

    }

    public VarName(String value) {
        this.value = value;
        this.arrayEntries = new ArrayList<>(1); //the normal variable holds one value
        sizeArray = 1;
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
        sizeArray = 1;
    }

    public void addArrayEntry(ArrayEntry ae){
        arrayEntries.add(ae);
    }

    public void setArrayValue(Expression expression){
        if(arrayEntries.size() == 1){
            ((ArrayList<ArrayEntry>)arrayEntries).remove(0);
        }
        arrayEntries.add(new ArrayEntry(this, null, expression));
    }

    public int getArraySize(){
        return sizeArray;
    }

    public boolean isFunction(){
        return isFunction;
    }
    @Override
    public Object visit(Visitor v, Object arg) {
        return v.visitVarName(this, arg);
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
