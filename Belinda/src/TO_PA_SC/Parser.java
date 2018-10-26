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

//TODO: Finish parseOneComand
public class Parser
{
	private Scanner scan;
	private int iLineNumber = 0;

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
		Commands commands = parseComand();
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
				if (currentTerminal.spelling.equals("I")) {
					System.out.println("detected I");
					type = new Type("I");

				} else if (currentTerminal.spelling.equals("C")) {
					System.out.println("detected C");
                    type = new Type("C");
				} else {
					System.out.println("Erorr you pig, how did you get here?");
					return null;
				}
				accept(Token.TYPE);
				Collection<VarName> varList = parseVarList();
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

	private Collection<VarName> parseVarList(){
        Collection<VarName> varList = new ArrayList<>();
		while(currentTerminal.kind != Token.SEMICOLON) {
			varList.add(new VarName(currentTerminal.spelling));
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
			if (currentTerminal.spelling.equals("I")) {
				System.out.println("detected I");
			} else if (currentTerminal.spelling.equals("C")) {
				System.out.println("detected C");
			} else {
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

    private Commands parseComand(){
	    Collection<Command> commands = new ArrayList<>();
	    Command command;
        while(currentTerminal.kind != Token.DO_END && currentTerminal.kind != Token.FOR_END &&
                currentTerminal.kind != Token.WHILE_END){
            command = parseOneCommand();
            if(command == null){
                return null;
            }
            commands.add(command);

        }
        return new Commands(commands);
    }

	private Command parseOneCommand(){
	    Command command;
		switch(currentTerminal.kind){
			case Token.FOR:
			    Assignment assignment;
			    Operation operation;
			    Operator operator;
			    LiteralNumber literalNumber;
			    Commands commands;
				accept(Token.FOR);
				accept(Token.LEFTPARAN);
				assignment = parseAssignment(); //here
				if(assignment == null) return  null;
				parseOperation();
				accept(Token.SEMICOLON);
				accept(Token.OPERATOR);
				if(currentTerminal.kind == Token.VARN_NAME){
					accept(Token.VARN_NAME);
				}else if (currentTerminal.kind == Token.LITERAL_NUMBER){
					accept(Token.LITERAL_NUMBER);
				}else{
					System.out.println("Error: Number or variable name expected");
				}
				accept(Token.RIGHTPARAN);
				accept(Token.START);
				parseComand();
				accept(Token.FOR_END);
				command = new ForLoop();
                break;

			case Token.WHILE:
				accept(Token.WHILE);
				accept(Token.LEFTPARAN);
				parseOperation();
				accept(Token.RIGHTPARAN);
				accept(Token.START);
				parseComand();
				accept(Token.WHILE_END);
				break;

			case Token.IF:
				accept(Token.IF);
				accept(Token.LEFTPARAN);
				parseOperation();
				accept(Token.RIGHTPARAN);
				accept(Token.START);
				parseComand();
				while (currentTerminal.kind != Token.FI){
					boolean error = false;
					switch (currentTerminal.kind){
						case Token.ELIF:
							accept(Token.ELIF);
							accept(Token.LEFTPARAN);
							parseOperation();
							accept(Token.RIGHTPARAN);
							accept(Token.START);
							parseComand();
							break;
						case Token.ELSE:
							accept(Token.ELSE);
							parseComand();
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
				break;

			case Token.SWITCH:

				accept(Token.SWITCH);
				accept(Token.LEFTPARAN);
				accept(Token.VARN_NAME);
				accept(Token.RIGHTPARAN);
				accept(Token.COLON);
				accept(Token.CASE);
				accept(Token.LITERAL_NUMBER);
				accept(Token.START);
				parseComand();
				accept(Token.END);
				while (currentTerminal.kind != Token.SWTICH_END){
					boolean error = false;
					boolean out_Default = false;
					switch (currentTerminal.kind){
						case Token.CASE:
							accept(Token.CASE);
							accept(Token.LITERAL_NUMBER);
							accept(Token.START);
							parseComand();
							accept(Token.END);
							break;
						case Token.DEFAULT:
							accept(Token.DEFAULT);
							accept(Token.START);
							parseComand();
							accept(Token.END);
							out_Default = true;
							break;
						default:
							System.out.println("Error, case or default expected");
							error = true;
							break;
					}
					if(error || out_Default){
						break;
					}
				}
				accept(Token.SWTICH_END);
				break;

			case Token.END:
				accept(Token.END);
				accept(Token.SEMICOLON);
				break;

			case Token.GIVEBACKWITH:
				accept(Token.GIVEBACKWITH);
				if(currentTerminal.kind == Token.NOTHING){
					accept(Token.NOTHING);
				}else {
					parseOperation();
				}
				accept(Token.SEMICOLON);
				break;
			default:
				parseAssignmentOrFunction();
				break;
		}
		return command;
	}

    private Assignment parseAssignment(){
	    VarName varName = null;
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
                        expression = parseOperation(varName, op);
                        accept(Token.ASSIG_RIGHT);
                        varName = new VarName(currentTerminal.spelling);
                        accept(Token.VARN_NAME);
                        break;
                    case Token.ASSIG_LEFT:
                        assignmentOperator = new AssignmentOperator(currentTerminal.spelling);
                        accept(Token.ASSIG_LEFT);
                        expression = parseOperationOrFunctionCall();
                        break;
                    case Token.ASSIG_RIGHT:
                        assignmentOperator = new AssignmentOperator(currentTerminal.spelling);
                        expression = varName;
                        accept(Token.ASSIG_RIGHT);
                        varName = new VarName(currentTerminal.spelling);
                        accept(Token.VARN_NAME);
                        break;
                    default:
                        System.out.println("Error expected LeftParan, operator or assignment");
                        break;
                }
                break;
            case Token.LITERAL_NUMBER:
                expression = parseOperation();
                assignmentOperator = new AssignmentOperator(currentTerminal.spelling);
                accept(Token.ASSIG_RIGHT);
                varName = new VarName(currentTerminal.spelling);
                accept(Token.VARN_NAME);
                //accept(Token.LITERAL_NUMBER);
//                switch(currentTerminal.kind){
//                    case Token.OPERATOR:
//                        accept(Token.OPERATOR);
//                        parseOperation();
//                        accept(Token.ASSIG_RIGHT);
//                        accept(Token.VARN_NAME);
//                        break;
//                    case Token.ASSIG_RIGHT:
//                        accept(Token.ASSIG_RIGHT);
//                        accept(Token.VARN_NAME);
//                        break;
//                    default:
//                        System.out.println("Error expected operator or assignment right");
//                        break;
//                }         -------------------check later if needed
                break;
            default:
                System.out.println("Expected VAR NAME, LITERAL NUMBER or FUNCTION_CALL");
                break;
        }

        accept(Token.SEMICOLON);
        if (varName == null || assignmentOperator == null || expression == null){
            System.out.println("Fatal error");
            return  null;
        }
        return new Assignment(varName, assignmentOperator, expression);
    }

    private Expression parseOperation(){
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
                return left;
            default:
                System.out.println("Error, Operator, assignment right or semicolon expected");
                return null;
        }
        right = parseExpressionMember();

        current = new Operation(left, operator, right);
        while (currentTerminal.kind != Token.SEMICOLON && currentTerminal.kind != Token.ASSIG_RIGHT && currentTerminal.kind != Token.RIGHTPARAN){
            left = current;
            switch (currentTerminal.kind){
                case Token.OPERATOR:
                    operator = new Operator(currentTerminal.spelling);
                    accept(Token.OPERATOR);
                    break;
                case Token.ASSIG_RIGHT:
                case Token.SEMICOLON:
                case Token.RIGHTPARAN:
                    return current;
                default:
                    System.out.println("Error, Operator, assignment right or semicolon expected");
                    break;
            }
            right = parseExpressionMember();
            current = new Operation(left, operator, right);
        }
        return current;
    }

    private Negation parseNegation(){
        Negation neg;
        switch (currentTerminal.kind) {
            case Token.LITERAL_NUMBER:
                neg = new Negation(new LiteralNumber(Integer.parseInt(currentTerminal.spelling)));
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

                break;
            case Token.LITERAL_NUMBER:
                exp = new LiteralNumber(Integer.parseInt(currentTerminal.spelling));
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

    private Expression parseOperationOrFunctionCall(){
	    Expression current = null;
	    Expression left;
	    Operator operator;
	    Expression right;
	    String tempVarName;
        switch (currentTerminal.kind){
            case Token.LITERAL_NUMBER:
                left = parseExpressionMember();
                break;
            case Token.VARN_NAME:
                tempVarName = currentTerminal.spelling;
                accept(Token.VARN_NAME);
                if(currentTerminal.kind == Token.LEFTPARAN){
                    current = parseFunctionCall(tempVarName);
                }else if(currentTerminal.kind == Token.OPERATOR){
                    left = new VarName(tempVarName);
                    operator = new Operator(currentTerminal.spelling);
                    accept(Token.OPERATOR);
                    current = parseOperation(left, operator);
                }else {
                    System.out.println("Eroror, letfParan or operator expected");
                }
                break;
        }
        return current;
    }

    private Expression parseOperation(Expression exp, Operator op){
        Expression left;
        Expression right;
        left = exp;
        right = parseExpressionMember();
        return buildTree(new Operation(left, op, right));
    }

    private  Expression buildTree(Expression llc){
        Operation current = null;
        Expression left;
        Operator operator;
        Expression right;
        while (currentTerminal.kind != Token.SEMICOLON && currentTerminal.kind != Token.ASSIG_RIGHT && currentTerminal.kind != Token.RIGHTPARAN){
            left = llc;
            switch (currentTerminal.kind){
                case Token.OPERATOR:
                    operator = new Operator(currentTerminal.spelling);
                    accept(Token.OPERATOR);
                    break;
                case Token.ASSIG_RIGHT:
                case Token.SEMICOLON:
                case Token.RIGHTPARAN:
                    return llc;
                default:
                    System.out.println("Error, Operator, assignment right or semicolon expected");
                    return llc;
            }
            right = parseExpressionMember();
            current = new Operation(left, operator, right);
        }
        if(current == null){
            return llc;
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
                    arguments.add(new VarName(currentTerminal.spelling));
                    accept(Token.VARN_NAME);
                    break;
                case Token.LITERAL_NUMBER:
                    arguments.add(new LiteralNumber(Integer.parseInt(currentTerminal.spelling)));
                    accept(Token.LITERAL_NUMBER);
                    break;
                default:
                    System.out.println("Error: expected var name or literal number as arguments for functionDeclaration call");
                    break;
            }
            if (currentTerminal.spelling.equals(",")) {
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

    private void parseFunctionCallArguments(){
        while(currentTerminal.kind != Token.RIGHTPARAN) {
            switch (currentTerminal.kind){
                case Token.VARN_NAME:
                    accept(Token.VARN_NAME);
                    break;
                case Token.LITERAL_NUMBER:
                    accept(Token.LITERAL_NUMBER);
                    break;
                default:
                    System.out.println("Error: expected var name or literal number as arguments for functionDeclaration call");
                    break;
            }

            if (currentTerminal.spelling.equals(",")) {
                accept(Token.COMMA);
                /*
                 * If the next spelling is not a ')' you get an error because we already checked the comma option
                 * */
            } else if (!currentTerminal.spelling.equals(")")/*||!currentTerminal.spelling(" ")*/) {
                System.out.println("Wrong functionDeclaration declaration: arguments");
                break;
            }
        }
    } //to be deleted

	private void parseAssignmentOrFunction() {
		switch(currentTerminal.kind){
			case Token.VARN_NAME:
				accept(Token.VARN_NAME);
				switch(currentTerminal.kind){
					case Token.LEFTPARAN:
						accept(Token.LEFTPARAN);
						parseFunctionCallArguments();
						accept(Token.RIGHTPARAN);
						break;
					case Token.OPERATOR:
						accept(Token.OPERATOR);
						parseOperation();
						accept(Token.ASSIG_RIGHT);
						accept(Token.VARN_NAME);
						break;
					case Token.ASSIG_LEFT:
						accept(Token.ASSIG_LEFT);
						parseOperationOrFunctionCall();
						break;
					case Token.ASSIG_RIGHT:
						accept(Token.ASSIG_RIGHT);
						accept(Token.VARN_NAME);
						break;
					default:
						System.out.println("Error expected LeftParan, operator or assignment");
						break;
				}
				break;
			case Token.LITERAL_NUMBER:
				accept(Token.LITERAL_NUMBER);
				switch(currentTerminal.kind){
					case Token.OPERATOR:
						accept(Token.OPERATOR);
						parseOperation();
						accept(Token.ASSIG_RIGHT);
						accept(Token.VARN_NAME);
						break;
					case Token.ASSIG_RIGHT:
						accept(Token.ASSIG_RIGHT);
						accept(Token.VARN_NAME);
						break;
					default:
						System.out.println("Error expected operator or assignment right");
						break;
				}
				break;
				default:
					System.out.println("Expected VAR NAME, LITERAL NUMBER or FUNCTION_CALL");
					break;
		}
		accept(Token.SEMICOLON);
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