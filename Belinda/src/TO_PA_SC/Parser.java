package TO_PA_SC;/*
 * 30.08.2016 IParse gone, IScanner gone, minor editing
 * 24.09.2010 IParser
 * 07.10.2009 New package structure
 * 02.10.2006 Small fix in parsePrimary()
 * 28.09.2006 Original version (based on Watt&Brown)
 */


import AST_P.*;

import java.util.ArrayList;
import java.util.Collection;


public class Parser
{
	private Scanner scan;

	private Token currentTerminal;


	public Parser( Scanner scan )
	{
		this.scan = scan;

		currentTerminal = scan.scan();
	}


	public AST parseProgram()
	{

		accept( Token.PROGRAM_START );
        Block block = parseBlock();
        accept( Token.PROGRAM_END );

		if( currentTerminal.kind != Token.EOT )
			System.out.println( "Tokens found after end of program" );
		return new Program(block);
	}


	private Block parseBlock(){
		accept( Token.DECLARE);
		accept( Token.START);
		Declarations declarations = parseDeclarations();
		accept( Token.DECLARE_END);
		accept( Token.DO );
		Commands commands = parseComand(Token.DO_END);
		accept( Token.DO_END );

		return new Block(declarations, commands);

	}

	private Declarations parseDeclarations(){
        Collection<Declaration> declarations = new ArrayList<>();
		while( currentTerminal.kind == Token.TYPE || currentTerminal.kind == Token.VARN_NAME ){

			declarations.add(parseOneDeclaration());

		}
		return new Declarations(declarations);
	}

	private Declaration parseOneDeclaration() {
	    Type type;
	    VarName funcName;
	    Declaration declaration = null;
		switch (currentTerminal.kind) {
			case Token.TYPE:
				switch (currentTerminal.spelling) {
					case "I":
						System.out.println("detected I");
						type = new Type("I");
						break;
					case "C":
						System.out.println("detected C");
						type = new Type("C");
						break;
					default:
						System.out.println("Erorr you pig, how did you get here?");
						return null;
				}
				accept(Token.TYPE);
                int size = 1;
                if (currentTerminal.kind == Token.LEFTBRA){
                    accept(Token.LEFTBRA);
                    size = -1;
                    if(currentTerminal.kind == Token.LITERAL_NUMBER){
                        size = Integer.parseInt(currentTerminal.spelling);
                        if (size < 2){
                            System.out.println("Error: array dimension must be at least 1");
                            return null;
                        }
                        accept(Token.LITERAL_NUMBER);
                    }
                    accept(Token.SEMICOLON);
                    accept(Token.RIGHTBRA);
                    if(size == -1){
                        size = 100;
                    }
                }

				Collection<VarName> varList = parseVarList(size);
				Collection<TypeVar> typeVars = new ArrayList<>();
                for (VarName v : varList) {
                    if(v == null){
                        return null;
                    }
                    typeVars.add(new TypeVar(type, v));
                }
                declaration = new InitializationTo0(new TypeVars(typeVars));
				break;

			case Token.VARN_NAME:
				System.out.println("FunctionDeclaration detected");
                funcName = new VarName(currentTerminal.spelling, true);
				accept(Token.VARN_NAME);
				accept(Token.LEFTPARAN);
				Collection<TypeVar> typeV = parseArguments();
                if(typeV == null){
                    return null;
                }
				accept(Token.RIGHTPARAN);
				Block block = parseBlock();
				declaration = new FunctionDeclaration(funcName, new TypeVars(typeV), block);
				break;
			default:
				System.out.println("FunctionDeclaration or Type expected");
				break;
		}
		accept(Token.SEMICOLON);
		return declaration;
	}

	private Collection<VarName> parseVarList(int size){ //size of the array/value
        Collection<VarName> varList = new ArrayList<>();
		while(currentTerminal.kind != Token.SEMICOLON) {
			varList.add(new VarName(currentTerminal.spelling, size));
		    accept(Token.VARN_NAME);
			if(currentTerminal.spelling.equals(",")){
				accept(Token.COMMA);
			}
		}
		return varList;
	}

	private Collection<TypeVar> parseArguments(){
	    Collection<TypeVar> typeVars = new ArrayList<>();
	    Type type;
	    VarName varName;
		while(currentTerminal.kind != Token.RIGHTPARAN) {
			switch (currentTerminal.spelling) {
				case "I":
					System.out.println("detected I");
					break;
				case "C":
					System.out.println("detected C");
					break;
				default:
					System.out.println("Erorr you pig, how did you get here?");
					return null;
			}
            type = new Type(currentTerminal.spelling);
			accept(Token.TYPE);
			varName = new VarName(currentTerminal.spelling);
			accept(Token.VARN_NAME);
			if (currentTerminal.spelling.equals(",")) {
				accept(Token.COMMA);
				/*
				 * If the next spelling is not a ')' you get an error because we already checked the comma option
				 * */
			} else if (!currentTerminal.spelling.equals(")")/*||!currentTerminal.spelling(" ")*/) {
				System.out.println("Wrong functionDeclaration declaration: arguments");
				return null;
			}
			typeVars.add(new TypeVar(type, varName));
		}
		return typeVars;
	}

	private Commands parseComand(int tokenValue){

		Collection<Command> commands = new ArrayList<>();
		Command command;


		while (findStop(tokenValue)){
			command = parseOneCommand();
			if(command == null){
				return null;
			}
			commands.add(command);

		}
		return new Commands(commands);
	}

	private boolean findStop(int tokenValue) {
		if(tokenValue == Token.FI){
			return (tokenValue != currentTerminal.kind && Token.ELIF!= currentTerminal.kind && Token.ELSE!= currentTerminal.kind);
		}else {
			return tokenValue != currentTerminal.kind;
		}
	}

	private Command parseOneCommand(){
	    Command command;

		switch(currentTerminal.kind){
			case Token.FOR:
				Expression operation;
				Assignment assignment;
			    Operator operator;
			    Expression modifier;
			    Commands forCommands;
				accept(Token.FOR);
				accept(Token.LEFTPARAN);
				assignment = (Assignment) parseAssignmentOrFunctionCallAlone();
				if(assignment == null) return  null;
				operation = parseOperationOnRight();
				if (operation == null) return  null;
				accept(Token.SEMICOLON);
				operator = new Operator(currentTerminal.spelling);
				accept(Token.OPERATOR);
				if(currentTerminal.kind == Token.VARN_NAME){
					modifier = new VarName(currentTerminal.spelling);
					accept(Token.VARN_NAME);
				}else if (currentTerminal.kind == Token.LITERAL_NUMBER){
					modifier = new LiteralNumber(currentTerminal.spelling);
					accept(Token.LITERAL_NUMBER);
				}else{
					System.out.println("Error: Number or variable name expected");
					return null;
				}
				accept(Token.RIGHTPARAN);
				accept(Token.START);
				forCommands = parseComand(Token.FOR_END);
				accept(Token.FOR_END);
				command = new ForLoop(assignment, operation, operator, modifier, forCommands);
                break;

			case Token.WHILE:
				Expression whileOperation;
				Commands whileComands;
				accept(Token.WHILE);
				accept(Token.LEFTPARAN);
				whileOperation = parseOperationOnRight();
				accept(Token.SEMICOLON);
				accept(Token.RIGHTPARAN);
				accept(Token.START);
				whileComands = parseComand(Token.WHILE_END);
				command = new WhileLoop(whileOperation, whileComands);
				accept(Token.WHILE_END);
				break;

			case Token.IF:
				Expression ifMainOperation;
				Collection<Expression> ifOtherOperations = new ArrayList<>();
				Commands ifMainCommands;
				Collection<Commands> ifOtherCommands = new ArrayList<>();
				accept(Token.IF);
				accept(Token.LEFTPARAN);
				ifMainOperation = parseOperationOnRight();
				accept(Token.SEMICOLON);
				accept(Token.RIGHTPARAN);
				accept(Token.START);
				ifMainCommands = parseComand(Token.FI);
				while (currentTerminal.kind != Token.FI){
					boolean error = false;
					switch (currentTerminal.kind){
						case Token.ELIF:
							accept(Token.ELIF);
							accept(Token.LEFTPARAN);
							ifOtherOperations.add(parseOperationOnRight());
							accept(Token.SEMICOLON);
							accept(Token.RIGHTPARAN);
							accept(Token.START);
							ifOtherCommands.add(parseComand(Token.FI));
							break;
						case Token.ELSE:
							accept(Token.ELSE);
							ifOtherOperations.add(new LiteralNumber("1"));
							ifOtherCommands.add(parseComand(Token.FI));
							if(currentTerminal.kind != Token.FI){
								error = true;
								System.out.println("Error, after else statement expected fi");
							}
							break;
						case Token.FI:
							break;
						default:
							System.out.println("Error, elif, else or fi expected");
							error = true;
							break;
					}
					if(error){
						break;
					}
				}
				accept(Token.FI);
				command = new IfStatement(ifMainOperation, ifOtherOperations, ifMainCommands, ifOtherCommands);
				break;

			case Token.SWITCH:
				VarName switchVarName;
				Collection<SwitchCase> switchCases = new ArrayList<>();
				accept(Token.SWITCH);
				accept(Token.LEFTPARAN);
				switchVarName = new VarName(currentTerminal.spelling);
				accept(Token.VARN_NAME);
				accept(Token.RIGHTPARAN);
				accept(Token.COLON);
				while (currentTerminal.kind != Token.SWTICH_END){
					SwitchCase tempSwitchcase = parseSwitchCase();
					switchCases.add(tempSwitchcase);
					if(tempSwitchcase != null && tempSwitchcase.isDefault()){
						break;
					}
				}
				accept(Token.SWTICH_END);
				command = new SwitchStatement(switchVarName, switchCases);
				break;

			case Token.END:
				accept(Token.END);
				command = new End();
				accept(Token.SEMICOLON);
				break;

			case Token.GIVEBACKWITH:
				GiveBackWith giveBackWith;
				accept(Token.GIVEBACKWITH);
				if(currentTerminal.kind == Token.NOTHING){
					accept(Token.NOTHING);
					giveBackWith = new GiveBackWith(new Nothing());
				}else {
					giveBackWith = new GiveBackWith(parseOperationOnRight());
				}
				accept(Token.SEMICOLON);
				command = giveBackWith;
				break;
            case Token.LITERAL_NUMBER:
			case Token.VARN_NAME:
				command = parseAssignmentOrFunctionCallAlone();
				break;
			default:
				System.out.println(currentTerminal.spelling + " - Unkown command");
				command = null;
				break;
		}
		return command;
	}

    private AssignmentOrFunctionCallAlone parseAssignmentOrFunctionCallAlone(){
	    Expression varName = null;
	    AssignmentOperator assignmentOperator = null;
	    Expression expression = null;
        switch(currentTerminal.kind){
            case Token.VARN_NAME:
                varName = new VarName(currentTerminal.spelling);
                accept(Token.VARN_NAME);
                switch(currentTerminal.kind){
                    case Token.OPERATOR:
                        Operator op = new Operator(currentTerminal.spelling);
                        accept(Token.OPERATOR);
                        expression = parseOperationOnLeft(varName, op);
                        assignmentOperator = new AssignmentOperator(currentTerminal.spelling);
                        accept(Token.ASSIG_RIGHT);
                        varName = new VarName(currentTerminal.spelling);
                        accept(Token.VARN_NAME);
                        break;
                    case Token.ASSIG_LEFT:
                        assignmentOperator = new AssignmentOperator(currentTerminal.spelling);
                        accept(Token.ASSIG_LEFT);
                        expression = parseOperationOnRight();
                        break;
                    case Token.ASSIG_RIGHT:
                        assignmentOperator = new AssignmentOperator(currentTerminal.spelling);
                        expression = varName;
                        accept(Token.ASSIG_RIGHT);
                        varName = new VarName(currentTerminal.spelling);
                        accept(Token.VARN_NAME);
                        break;
					case Token.LEFTPARAN:
						Expression tempFunc = parseFunctionCall(((VarName)varName).getVarValue());
						switch (currentTerminal.kind){
							case Token.ASSIG_RIGHT:
								expression = tempFunc;
								assignmentOperator = new AssignmentOperator(currentTerminal.spelling);
								accept(Token.ASSIG_RIGHT);
								varName = new VarName(currentTerminal.spelling);
								accept(Token.VARN_NAME);
								break;
							case Token.OPERATOR:
								Operator opi = new Operator(currentTerminal.spelling);
								accept(Token.OPERATOR);
								expression = parseOperationOnLeft(tempFunc, opi);
								assignmentOperator = new AssignmentOperator(currentTerminal.spelling);
								accept(Token.ASSIG_RIGHT);
								varName = new VarName(currentTerminal.spelling);
								accept(Token.VARN_NAME);
								break;
							case Token.SEMICOLON:
								expression = tempFunc;
								break;
							default:
								System.out.println("Error expected operator or assignment");
								break;
						}
						break;
                    case Token.LEFTBRA:
                        accept(Token.LEFTBRA);
                        Expression arrayIndex = parseOperationOnRight();
                        accept(Token.SEMICOLON);
                        accept(Token.RIGHTBRA);
                        switch (currentTerminal.kind){
                            case Token.ASSIG_RIGHT:
                                expression = new ArrayEntry((VarName) varName, arrayIndex,null);//the null means that the array entry is already created
                                assignmentOperator = new AssignmentOperator(currentTerminal.spelling);
                                accept(Token.ASSIG_RIGHT);
                                varName = new VarName(currentTerminal.spelling);
                                accept(Token.VARN_NAME);
                                varName = createArrayEntryOrVarName((VarName)varName);
                                break;
                            case Token.OPERATOR:
                                Operator opi = new Operator(currentTerminal.spelling);
                                accept(Token.OPERATOR);
                                expression = parseOperationOnLeft(new ArrayEntry((VarName) varName, arrayIndex,null), opi);//the null means that the array entry is already created
                                assignmentOperator = new AssignmentOperator(currentTerminal.spelling);
                                accept(Token.ASSIG_RIGHT);
                                varName = new VarName(currentTerminal.spelling);
                                accept(Token.VARN_NAME);
                                varName = createArrayEntryOrVarName((VarName) varName);
                                break;
                            case Token.ASSIG_LEFT:
                                assignmentOperator = new AssignmentOperator(currentTerminal.spelling);
                                accept(Token.ASSIG_LEFT);
                                varName = new ArrayEntry((VarName) varName, arrayIndex, null);
                                expression = parseOperationOnRight();
                                break;
                            default:
                                System.out.println("Error expected operator or assignment");
                                break;
                        }
                        break;
                    default:
                        System.out.println("Error expected LeftParan, operator or assignment");
                        break;
                }
                break;
            case Token.LITERAL_NUMBER:
            	LiteralNumber litNum = new LiteralNumber(currentTerminal.spelling);
            	accept(Token.LITERAL_NUMBER);
				switch(currentTerminal.kind){
					case Token.OPERATOR:
						Operator op = new Operator(currentTerminal.spelling);
						accept(Token.OPERATOR);
						expression = parseOperationOnLeft(litNum, op);
						break;
					case Token.ASSIG_RIGHT:
						expression = litNum;
						break;
					default:
						System.out.println("Error expected operator or right assignment after literal number in right assignment");
						break;
				}
				assignmentOperator = new AssignmentOperator(currentTerminal.spelling);
				accept(Token.ASSIG_RIGHT);
				varName = new VarName(currentTerminal.spelling);
				accept(Token.VARN_NAME);
				varName= createArrayEntryOrVarName((VarName) varName);
                break;
            default:
                System.out.println("Expected VAR NAME, LITERAL NUMBER or FUNCTION_CALL");
                break;
        }

        accept(Token.SEMICOLON);
        if (varName == null || expression == null){
            System.out.println("Fatal error during assignment, missing parts");
            return  null;
        }
        if(assignmentOperator == null){
        	return new FunctionCallAlone(((FunctionCall) expression).getFuncName(),((FunctionCall) expression).getArguments());
		}
        if(varName instanceof ArrayEntry){
			return new Assignment((ArrayEntry) varName, assignmentOperator, expression);
		}else if(varName instanceof VarName){
			return new Assignment((VarName) varName, assignmentOperator, expression);
		}else {
        	System.out.println("Error: can only assign to varName or arrayEntry");
        	return null;
		}
    }

    private Expression createArrayEntryOrVarName(VarName varName) {
        if(currentTerminal.kind == Token.LEFTBRA){
            accept(Token.LEFTBRA);
            Expression index = parseOperationOnRight();
            accept(Token.SEMICOLON);
            accept(Token.RIGHTBRA);
            return new ArrayEntry(varName, index, null);
        }else {
        	return varName;
		}

    }

    private Expression parseOperationOnRight(){
	    Operation current;
	    Expression left;
	    Operator operator;
	    Expression right;
        left = parseExpressionMember();
        switch (currentTerminal.kind){
            case Token.OPERATOR:
                operator = new Operator(currentTerminal.spelling);
                accept(Token.OPERATOR);
                break;
            case Token.ASSIG_RIGHT:
            case Token.SEMICOLON:
            case Token.RIGHTPARAN:
            case Token.RIGHTBRA:
                return left;
            default:
                System.out.println("Error, Operator, assignment right or semicolon expected");
                return null;
        }
        right = parseExpressionMember();

        current = new Operation(left, operator, right);
        while (currentTerminal.kind != Token.SEMICOLON){
			if (currentTerminal.kind == Token.OPERATOR) {
				operator = new Operator(currentTerminal.spelling);
				accept(Token.OPERATOR);
			} else {
				System.out.println("Error, Operator, assignment right or semicolon expected");
				break;
			}
			left = current;
            right = parseExpressionMember();
            current = new Operation(left, operator, right);
        }
        return current;
    }

	private Expression parseOperationOnLeft(Expression exp, Operator op){
		Expression right;
		right = parseExpressionMember();
		return buildTree(new Operation(exp, op, right));
	}

	private Negation parseNegation(){
		Negation neg;
		switch (currentTerminal.kind) {
			case Token.LITERAL_NUMBER:
				neg = new Negation(new LiteralNumber(currentTerminal.spelling));
				accept(Token.LITERAL_NUMBER);
				break;
			case Token.VARN_NAME:
				neg = new Negation(new VarName(currentTerminal.spelling));
				accept(Token.VARN_NAME);
				break;
			default:
				System.out.println("Error: After negation is expected a literla number or a varName");
				neg = null;
				break;
		}
		return neg;
	}

	private Expression parseExpressionMember(){
		Expression exp;
		switch (currentTerminal.kind){
			case Token.VARN_NAME:
				exp = new VarName(currentTerminal.spelling);
				accept(Token.VARN_NAME);
				if(currentTerminal.kind == Token.LEFTPARAN){
					exp = parseFunctionCall(((VarName) exp).getVarValue());
				}else if(currentTerminal.kind == Token.LEFTBRA){
				    accept(Token.LEFTBRA);
				    Expression index = parseOperationOnRight();
                    accept(Token.SEMICOLON);
				    accept(Token.RIGHTBRA);
				    exp = new ArrayEntry((VarName) exp, index, null);
                }
				break;
			case Token.LITERAL_NUMBER:
				exp = new LiteralNumber(currentTerminal.spelling);
				accept(Token.LITERAL_NUMBER);

				break;
			case  Token.NOT:
				accept(Token.NOT);
				exp = parseNegation();
				break;
			default:
				System.out.println("Error, VarName, LiteralNumber or negation expected");
				exp = null;
				break;
		}
		return exp;
	}

	private  Expression buildTree(Expression llc){
		Expression current = llc;
		Expression left = llc;
		Operator operator;
		Expression right;
		while (currentTerminal.kind != Token.ASSIG_RIGHT){
			switch (currentTerminal.kind){
				case Token.OPERATOR:
					operator = new Operator(currentTerminal.spelling);
					accept(Token.OPERATOR);
					right = parseExpressionMember();
					current = new Operation(left, operator, right);
					left = current;
					break;
				case Token.ASSIG_RIGHT:
					return current;
				default:
					System.out.println("Error, Operator, assignment right or semicolon expected");
					return llc;
			}
		}
		return current;
	}

	private FunctionCall parseFunctionCall(String funcName){

		VarName varName = new VarName(funcName, true);
		Collection<Expression> arguments = new ArrayList<>();
		accept(Token.LEFTPARAN);
		while(currentTerminal.kind != Token.RIGHTPARAN) {
			switch (currentTerminal.kind){
				case Token.VARN_NAME:
					VarName varT = new VarName(currentTerminal.spelling);

					accept(Token.VARN_NAME);
					if(currentTerminal.kind == Token.LEFTBRA){
						accept(Token.LEFTBRA);
						ArrayEntry ar = new ArrayEntry(varT,parseOperationOnRight(),null);
						accept(Token.SEMICOLON);
						accept(Token.RIGHTBRA);
						arguments.add(ar);
					}else{
						arguments.add(varT);
					}
					break;
				case Token.LITERAL_NUMBER:
					arguments.add(new LiteralNumber(currentTerminal.spelling));
					accept(Token.LITERAL_NUMBER);
					break;
				default:
					System.out.println("Error: expected var name or literal number as arguments for functionDeclaration call");
					break;
			}
			if (currentTerminal.spelling.equals(",")){
				accept(Token.COMMA);
				/*
				 * If the next spelling is not a ')' you get an error because we already checked the comma option
				 * */
			}else if (!currentTerminal.spelling.equals(")")/*||!currentTerminal.spelling(" ")*/) {
				System.out.println("Wrong functionDeclaration declaration: arguments");
				break;
			}
		}
		accept(Token.RIGHTPARAN);
		return new FunctionCall(varName, arguments);
	}

	private SwitchCase parseSwitchCase(){
		Collection<LiteralNumber> values = new ArrayList<>();
		Commands commands;
		End end;
		while (currentTerminal.kind == Token.CASE){
			accept(Token.CASE);
			if(currentTerminal.kind != Token.LITERAL_NUMBER){
				System.out.println("Case must be followed by a literal number");
				return null;
			}
			values.add(new LiteralNumber(currentTerminal.spelling));
			accept(Token.LITERAL_NUMBER);
		}
		if(currentTerminal.kind == Token.DEFAULT){
			accept(Token.DEFAULT);
			values = null;
		}

		accept(Token.START);
		commands = parseComand(Token.END);
		accept(Token.END);
		accept(Token.SEMICOLON);
		end = new End();
		return new SwitchCase(values, commands, end);
	}

	private void accept( byte expected )
	{
		System.out.println(currentTerminal.kind + "\t" + expected + " " + Token.SPELLINGS[currentTerminal.kind]);
		if( currentTerminal.kind == expected )
			currentTerminal = scan.scan();
		else
			System.out.println( "Expected token of kind " + expected );
	}
}