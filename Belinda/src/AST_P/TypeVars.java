package AST_P;

import java.util.*;

public class TypeVars extends AST {
    Collection<TypeVar> typeVars;

    public TypeVars(Collection<TypeVar> typeVars) {
        this.typeVars = typeVars;
    }

    public TypeVars(TypeVar... typeVars) {
        this.typeVars = new ArrayList<>();
        for (TypeVar tv : typeVars) {
            this.typeVars.add(tv);
        }

    }
}
