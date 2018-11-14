package AST_P;

import TO_PA_SC.Address;

import java.util.Vector;

public class FunctionDeclaration extends Declaration{
    private VarName funcName;
    private TypeVars typeVars;
    private Block block;
    private Address address;

    public FunctionDeclaration(VarName varName, TypeVars typeVars, Block block){
        this.funcName = varName;
        this.typeVars = typeVars;
        this.block = block;
    }

    public VarName getFuncName() {
        return funcName;
    }

    public TypeVars getTypeVars() {
        return typeVars;
    }

    public Block getBlock() {
        return block;
    }

    @Override
    public Object visit(Visitor v, Object arg) {
        return v.visitFunctionDeclaration(this, arg);
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
