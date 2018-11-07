package AST_P;

public class Assignment extends AssignmentOrFunctionCallAlone {
    private Expression varNameOrArrayEntr;
    private AssignmentOperator assignmentOperator;
    private Expression expression;

    public Assignment(VarName varName, AssignmentOperator assignmentOperator, Expression expression) {
        this.varNameOrArrayEntr = varName;
        this.assignmentOperator = assignmentOperator;
        this.expression = expression;
    }
    public Assignment(ArrayEntry arrayEntry, AssignmentOperator assignmentOperator, Expression expression) {
        this.varNameOrArrayEntr = arrayEntry;
        this.assignmentOperator = assignmentOperator;
        this.expression = expression;
    }

    public Expression getVarName() {
        return varNameOrArrayEntr;
    }

    public AssignmentOperator getAssignmentOperator() {
        return assignmentOperator;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public Object visit(Visitor v, Object arg) {
        return v.visitAssignment(this, arg);
    }
}
