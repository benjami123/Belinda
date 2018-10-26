package AST_P;

public class AssignmentOperator extends AST {
    String value;

    public AssignmentOperator(String assignmentOperator) {
        this.value = assignmentOperator;
    }
}
