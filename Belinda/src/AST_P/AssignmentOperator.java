package AST_P;

public class AssignmentOperator extends AST {
    private String value;

    public AssignmentOperator(String assignmentOperator) {
        this.value = assignmentOperator;
    }

    @Override
    public Object visit(Visitor v, Object arg) {
        return v.visitAssignmentOperator(this, arg);
    }

    public String getValue() {
        return value;
    }

}
