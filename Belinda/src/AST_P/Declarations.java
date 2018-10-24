package AST_P;

import java.util.*;

public class Declarations extends AST {
    public Vector<Declaration> declaration = new Vector<Declaration>();

    public Declarations() {
    }

    public Declarations(Declaration... declarations ) {
        for (Declaration d : declarations) {
            this.declaration.add(d);
        }
    }

    public Declarations(Vector<Declaration> declaration) {
        this.declaration = declaration;
    }
}
