package AST_P;

import java.util.Vector;

public class FunctionDeclaration extends Declaration{
    private VarName funcName;
    private TypeVars typeVars;
    private Block block;

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
}
