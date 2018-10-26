package AST_P;

import java.util.*;

public class Declarations extends AST {
    public Collection<Declaration> declaration;
    public Declarations() {
    }

    public Declarations(Declaration... declarations ) {
        this.declaration = new ArrayList<>();
        for (Declaration d : declarations) {
            this.declaration.add(d);
        }
    }

    public Declarations(Collection<Declaration> declaration) {
        this.declaration = declaration;
    }
}
