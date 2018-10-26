package AST_P;

import java.util.Vector;

public class TypeVars extends AST {
    Vector<TypeVar> typeVars = new Vector<>();

    public TypeVars(Vector<TypeVar> typeVars) {
        this.typeVars = typeVars;
    }

    public TypeVars(TypeVar... typeVars) {
        for (TypeVar tv : typeVars) {
            this.typeVars.add(tv);
        }

    }
}
