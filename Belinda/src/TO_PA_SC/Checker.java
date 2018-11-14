package TO_PA_SC;

import AST_P.*;

import java.util.ArrayList;

public class Checker implements Visitor {

    private IdentificationTable table = new IdentificationTable();

    public void check( Program p )
    {
        p.visit( this, null );
    }

    @Override
    public Object visitProgram(Program program, Object arg) {
        table.openScope();
        program.getBlock().visit(this, null);
        table.closeScope();
        return null;
    }

    @Override
    public Object visitAssignment(Assignment assignment, Object arg) {
        Expression destination = assignment.getVarName();
        destination.visit(this, assignment);
        assignment.getAssignmentOperator().visit(this, null);
        Expression expression = assignment.getExpression();
        expression.visit(this, assignment);
        if(destination instanceof VarName){
            if(((VarName) destination).getArraySize() == 0){
                System.out.println("Error, " +((VarName) destination).getVarValue() + " is not a VarName, but a function");
                System.exit(1);
            }
            ((VarName) destination).setArrayValue(expression);
        }else if(destination instanceof ArrayEntry){
            ((ArrayEntry) destination).setValue(expression);
        }else {
            System.out.println("Error: checker-- Assignment");
            System.exit(1);
        }
        return null;
    }

    @Override
    public Object visitAssignmentOperator(AssignmentOperator assignmentOperator, Object arg) {
        return assignmentOperator.getValue();
    }

    @Override
    public Object visitBlock(Block block, Object arg) {
        block.getDeclarations().visit(this, null);
        block.getCommands().visit(this, null);
        return null;
    }

    @Override
    public Object visitDeclarations(Declarations declarations, Object arg) {
        for (Declaration dec : declarations.getDeclaration()) {
            dec.visit(this, null);
        }
        return null;
    }

    @Override
    public Object visitCommands(Commands commands, Object arg) {
        for (Command com : commands.getCommands()) {
            com.visit(this, null);
        }
        return null;
    }

    @Override
    public Object visitEnd(End end, Object arg) {
        return null;
    }

    @Override
    public Object visitForLoop(ForLoop forLoop, Object arg) {
        forLoop.getAssignment().visit(this, null);
        forLoop.getOperation().visit(this, null);
        forLoop.getOperator().visit(this, null);
        forLoop.getModifier().visit(this, null);
        forLoop.getCommands().visit(this, null);
        return null;
    }

    @Override
    public Object visitFunctionCall(FunctionCall functionCall, Object arg) {
        functionCall.getFuncName().visit(this, functionCall);
        if(!(table.retrive(functionCall.getFuncName().getVarValue()).getVarName().isFunction())){
            System.out.println("Error: " + functionCall.getFuncName() + " is not a function" );
            System.exit(1);
        }
        for (Expression exp: functionCall.getArguments()) {
            exp.visit(this, exp);
        }
        return null;
    }

    @Override
    public Object visitFunctionCallAlone(FunctionCallAlone functionCallAlone, Object arg) {
        functionCallAlone.getFuncName().visit(this, functionCallAlone);
        if(!(table.retrive(functionCallAlone.getFuncName().getVarValue()).getVarName().isFunction())){
            System.out.println("Error: " + functionCallAlone.getFuncName().getVarValue() + " is not a function" );
            System.exit(1);
        }
        for (Expression exp: functionCallAlone.getArguments()) {
            exp.visit(this, exp);
        }
        return null;
    }

    @Override
    public Object visitFunctionDeclaration(FunctionDeclaration functionDeclaration, Object arg) {
        functionDeclaration.getFuncName().visit(this, functionDeclaration);
        table.openScope();
        functionDeclaration.getTypeVars().visit(this, functionDeclaration.getTypeVars());
        functionDeclaration.getBlock().visit(this, null);
        table.closeScope();
        return null;
    }

    @Override
    public Object visitGiveBackWith(GiveBackWith giveBackWith, Object arg) {
        giveBackWith.getExpression().visit(this, giveBackWith);
        return null;
    }

    @Override
    public Object visitIfStatement(IfStatement ifStatement, Object arg) {
        ifStatement.getMainOperation().visit(this, null);
        ifStatement.getMainCommands().visit(this, null);
        for (Expression exp : ifStatement.getOtherOperations()) {
            exp.visit(this, null);
        }
        for (Commands com : ifStatement.getOtherCommands()) {
            com.visit(this, null);
        }
        return null;
    }

    @Override
    public Object visitInitializationTo0(InitializationTo0 initializationTo0, Object arg) {
        initializationTo0.getTypeVars().visit(this, initializationTo0);
        return null;
    }

    @Override
    public Object visitLiteralNumber(LiteralNumber literalNumber, Object arg) {
        String value = literalNumber.getsValue();
        int i;
        try{
            i = Integer.parseInt(value);
        }catch (Exception exep){
            if(value.length() != 3){
                System.out.println("Error: syntax error ->" + value + " impossible to convert into an integer");
                System.exit(1);
            }
            char[] letter = value.toCharArray();
            i = letter[1];
        }
        literalNumber.setiValue(i);
        return null;
    }

    @Override
    public Object visistNegation(Negation negation, Object arg) {
        negation.getVarNameOrLiteralNumber().visit(this, negation);
        return null;
    }

    @Override
    public Object visistOperation(Operation operation, Object arg) {
        operation.getLeft().visit(this, operation);
        operation.getOperator().visit(this, null);
        operation.getRight().visit(this, operation);
        return null;
    }

    @Override
    public Object visitOperator(Operator operator, Object arg) {
        return null;
    }

    @Override
    public Object visitSwitchCase(SwitchCase switchCase, Object arg) {
        if(switchCase.getLiteralNumbers() != null){
            for (LiteralNumber ln : switchCase.getLiteralNumbers()) {
                ln.visit(this, null);
            }
        }
        switchCase.getCommands().visit(this, null);
        switchCase.getEnd().visit(this, null);
        return null;
    }

    @Override
    public Object visitSwitchStatement(SwitchStatement switchStatement, Object arg) {
        switchStatement.getVarName().visit(this, switchStatement);
        for (SwitchCase sc: switchStatement.getSwitchCases()) {
            sc.visit(this, null);
        }
        return null;
    }

    @Override
    public Object visitType(Type type, Object arg) {
        return null;
    }

    @Override
    public Object visitTypeVar(TypeVar typeVar, Object arg) {
        typeVar.getType().visit(this, null);
        typeVar.getVarName().visit(this, arg);
        return null;
    }
    public Object visitTypeVars(TypeVars typeVars, Object arg){
        for (TypeVar tp :typeVars.getTypeVars()) {
            tp.visit(this, arg);
        }
        return null;
    }

    @Override
    public Object visitVarName(VarName varName, Object arg) {
        if(arg instanceof InitializationTo0 ){
            //just getting the type from the firt TypeVar inside the colelction of TypeVars inside the object TypeVars inside the
            //InitializationTo0 object arg
            Type t = ((ArrayList<TypeVar>)(((InitializationTo0) arg).getTypeVars()).getTypeVars()).get(0).getType();
            TypeVar tp = new TypeVar(t, varName);
            table.enter(varName.getVarValue(), tp);
        }else if(arg instanceof TypeVars){
            Type t = ((ArrayList<TypeVar>)((TypeVars) arg).getTypeVars()).get(0).getType();
            TypeVar tp = new TypeVar(t, varName);
            table.enter(varName.getVarValue(), tp);
        }else if (arg instanceof FunctionDeclaration) {
            table.enter(varName.getVarValue(), varName);
        }else if(arg instanceof Assignment){
            if(varName.isFunction()){
                System.out.println("Error: "+ varName.getVarValue() + "is a function, cannot be assigned");
                System.exit(1);
                return null;
            }
            if(!table.isDeclared(varName.getVarValue())){
                System.out.println("Error: " + varName.getVarValue() + " is not declared");
                System.exit(1);
                return null;
            }
        }else if(arg instanceof Operation || arg instanceof VarName){
            if(varName.isFunction()){
                System.out.println("Error:" + varName.getVarValue() + " is a function: must be called like: " + varName.getVarValue() + "(arg, arg2)");
                System.exit(1);
                return null;
            }
            if(!table.isDeclared(varName.getVarValue())){
                System.out.println("Error: " + varName.getVarValue() + " is not declared");
                System.exit(1);
                return null;
            }
        }else if(arg instanceof FunctionCallAlone || arg instanceof FunctionCall){
            if(!varName.isFunction()){
                System.out.println("Error: " + varName.getVarValue() + " is not a function, cannot be called");
                System.exit(1);
                return null;
            }
            if(!table.isDeclared(varName.getVarValue())){
                System.out.println("Error: " + varName.getVarValue() + " is not declared");
                System.exit(1);
                return null;
            }
        }else if(arg instanceof Negation){
            if(varName.isFunction()){
                System.out.println("Error:" + varName.getVarValue() + " is a function. The argument of a negation must be a var name");
                System.exit(1);
                return null;
            }
            if(!table.isDeclared(varName.getVarValue())){
                System.out.println("Error: " + varName.getVarValue() + " is not declared");
                System.exit(1);
                return null;
            }
        }else if(arg instanceof GiveBackWith){
            if(!table.isDeclared(varName.getVarValue())){
                System.out.println("Error: " + varName.getVarValue() + " is not declared");
                System.exit(1);
                return null;
            }
        }else if(arg instanceof SwitchStatement){
            if(varName.isFunction()){
                System.out.println("Error:" + varName.getVarValue() + " is a function. It should be a var name");
                System.exit(1);
                return null;
            }
            if(!table.isDeclared(varName.getVarValue())){
                System.out.println("Error: " + varName.getVarValue() + " is not declared");
                System.exit(1);
                return null;
            }
        }else if(arg instanceof Expression){
            if(varName.isFunction()){
                System.out.println("Error:" + varName.getVarValue() + " is a function. It shouldn't (?)");
                System.exit(1);
                return null;
            }
            if(!table.isDeclared(varName.getVarValue())){
                System.out.println("Error: " + varName.getVarValue() + " is not declared");
                System.exit(1);
                return null;
            }
        }else {
            System.out.println("Error: arg: " + arg.toString());
            System.exit(1);
        }
        return null;
    }

    @Override
    public Object visitWhileLoop(WhileLoop whileLoop, Object arg) {
        whileLoop.getOperation().visit(this, null);
        whileLoop.getCommands().visit(this, null);
        return null;
    }

    @Override
    public Object visitArrayEntry(ArrayEntry arrayEntry, Object arg) {
        String id = arrayEntry.getFather().getVarValue();
        if(table.isDeclared(id)) {
            VarName realFather = table.retrive(id).getVarName();
            if(realFather.isFunction()){
                System.out.println("Error: " + id + " is a function");
                System.exit(1);
            }
            arrayEntry.setFather(realFather);
            realFather.addArrayEntry(arrayEntry);
        }else{
            System.out.println("Error: " + id + " not declared");
            System.exit(1);
        }
        return null;
    }
}
