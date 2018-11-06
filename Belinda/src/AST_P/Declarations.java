package AST_P;

import java.util.*;

public class Declarations extends AST {
    private Collection<Declaration> declaration;


    public Declarations(Collection<Declaration> declaration) {
        this.declaration = declaration;
    }

    public Collection<Declaration> getDeclaration() {
        return declaration;
    }

    @Override
    public Object visit(Visitor v, Object arg) {
        return v.visitDeclarations(this, arg);
    }
}
