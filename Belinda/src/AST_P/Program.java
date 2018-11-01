package AST_P;

import TO_PA_SC.Checker;

public class Program extends AST{
    private  Block block;

    public Program(Block block) {
        this.block = block;
    }


    public Block getBlock() {
        return block;
    }

    @Override
    public Object visit(Visitor v, Object arg) {
        return v.visitProgram( this, arg );
    }
}
