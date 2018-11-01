package TO_PA_SC;

import AST_P.TypeVar;

public class IdEntry {
    public int level;
    public String id;
    public TypeVar attr;


    public IdEntry( int level, String id, TypeVar attr )
    {
        this.level = level;
        this.id = id;
        this.attr = attr;
    }


    public String toString()
    {
        return level + "," + id;
    }
}
