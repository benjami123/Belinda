package AST_P;

public class Assignment extends AssignmentOrFunctionCallAlone {
    VarName varName;
    AssignmentOperator assignmentOperator;
    Expression expression;

    public Assignment(VarName varName, AssignmentOperator assignmentOperator, Expression expression) {
        this.varName = varName;
        this.assignmentOperator = assignmentOperator;
        this.expression = expression;
    }
}
