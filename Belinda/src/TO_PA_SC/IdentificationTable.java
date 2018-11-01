package TO_PA_SC;

import AST_P.TypeVar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class IdentificationTable {
    private ArrayList<IdEntry> idEntries = new ArrayList<>();
    private int level = 0;

    public IdentificationTable()
    {
    }

    public void enter(String id, TypeVar tp){
        IdEntry entry = find( id );
        if( entry != null && entry.level == level ){
            System.out.println( id + " declared twice" );
        }
        else{
            idEntries.add(new IdEntry(level, id, tp));
        }

    }

    private TypeVar retrive(String id){
        IdEntry temp = find(id);
        if(temp != null){
            return temp.attr;
        }else {
            return null;
        }
    }

    public void openScope(){
        ++level;
    }

    public void closeScope(){
        int pos = idEntries.size() - 1;
        while (pos >= 0 && idEntries.get(pos).level == level){
            idEntries.remove(pos);
            pos--;
        }
        --level;
    }

    private IdEntry find(String id) {
        for (IdEntry idTemp : idEntries) {
            if(idTemp.id.equals(id)){
                return idTemp;
            }
        }
        return null;
    }


}
