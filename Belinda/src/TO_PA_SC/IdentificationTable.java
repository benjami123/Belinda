package TO_PA_SC;

import AST_P.Type;
import AST_P.TypeVar;
import AST_P.VarName;


import java.util.ArrayList;

class IdentificationTable {
    private ArrayList<IdEntry> idEntries = new ArrayList<>();
    private int level = 0;

    IdentificationTable()
    {
    }

    void enter(String id, TypeVar tp){
        if(Token.isKeyWord(id)){
            System.out.println( id + " is a keyword, cannote be used as function name or variable name" );
            System.exit(1);
        }
        IdEntry entry = find( id );
        if( entry != null && entry.level == level ){
            System.out.println( id + " declared twice" );
            System.exit(1);
        }
        else{
            idEntries.add(new IdEntry(level, id, tp));
        }
    }
    void enter(String id, VarName varName){
        IdEntry entry = find( id );
        if( entry != null && entry.level == level ){
            System.out.println( id + " declared twice" );
            System.exit(1);
        }
        else{
            idEntries.add(new IdEntry(level, id, new TypeVar(new Type("F"), varName)));
        }

    }

    TypeVar retrive(String id){
        IdEntry temp = find(id);
        if(temp != null){
            return temp.attr;
        }else {
            return null;
        }
    }

    void openScope(){
        ++level;
    }

    void closeScope(){
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

    boolean isDeclared(String id){
        return find(id) != null;
    }

}
