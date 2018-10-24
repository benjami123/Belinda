package AST_P;

public class TypeVar extends AST {
    Type type;
    VarName varName;

    public TypeVar(Type type, VarName varName) {
        this.type = type;
        this.varName = varName;
    }
}
