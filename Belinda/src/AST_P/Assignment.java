package AST_P;

public class Assignment extends Command {
    VarName varName;
    AssignmentOperators assignmentOperator;
    Expression expression;

    public Assignment(VarName varName, AssignmentOperators assignmentOperator, Expression expression) {
        this.varName = varName;
        this.assignmentOperator = assignmentOperator;
        this.expression = expression;
    }
}
