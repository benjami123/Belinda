package AST_P;

public class InitializationTo0  extends Declaration{
    private TypeVars typeVars;

    public InitializationTo0(TypeVars typeVars) {
        this.typeVars = typeVars;
    }

    public TypeVars getTypeVars() {
        return typeVars;
    }
    @Override
    public Object visit(Visitor v, Object arg) {
        return v.visitInitializationTo0(this, arg);
    }
}
