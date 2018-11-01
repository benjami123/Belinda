package AST_P;

public class Assignment extends AssignmentOrFunctionCallAlone {
    private VarName varName;
    private AssignmentOperator assignmentOperator;
    private Expression expression;

    public Assignment(VarName varName, AssignmentOperator assignmentOperator, Expression expression) {
        this.varName = varName;
        this.assignmentOperator = assignmentOperator;
        this.expression = expression;
    }
}
