package AST_P;

import java.util.Vector;

public class FunctionDeclaration extends Declaration{
    VarName funcName;
    TypeVars typeVars;
    Block block;

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
}
