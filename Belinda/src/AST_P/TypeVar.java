package AST_P;

public class TypeVar extends AST {
    private Type type;
    private VarName varName;

    public TypeVar(Type type, VarName varName) {
        this.type = type;
        this.varName = varName;
    }

    public Type getType() {
        return type;
    }

    public VarName getVarName() {
        return varName;
    }
}
