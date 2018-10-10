package AST;

public class Declarations extends AST {
    public InitializationTo0 initializationTo0;
    public Function function;

    public Declarations(InitializationTo0 initializationTo0, Function function) {
        this.initializationTo0 = initializationTo0;
        this.function = function;
    }
}
