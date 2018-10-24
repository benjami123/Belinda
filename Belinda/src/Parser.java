/*
 * 30.08.2016 IParse gone, IScanner gone, minor editing
 * 24.09.2010 IParser
 * 07.10.2009 New package structure
 * 02.10.2006 Small fix in parsePrimary()
 * 28.09.2006 Original version (based on Watt&Brown)
 */
  

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
	
	
	public void parseProgram()
	{
		accept( Token.PROGRAM_START );
		parseBlock();
        accept( Token.PROGRAM_END );

		if( currentTerminal.kind != Token.EOT )
			System.out.println( "Tokens found after end of program" );
	}
	
	
	private void parseBlock()
	{
		accept( Token.DECLARE);
		accept( Token.START);
		parseDeclarations();
		accept( Token.DECLARE_END);
		accept( Token.DO );
		parseComand();
		accept( Token.DO_END );

	}
	
	private void parseDeclarations()
	{
		while( currentTerminal.kind == Token.TYPE || currentTerminal.kind == Token.VARN_NAME ){

			parseOneDeclaration();

		}
	}

	private void parseOneDeclaration() {
		switch (currentTerminal.kind) {
			case Token.TYPE:
				if (currentTerminal.spelling.equals("I")) {
					System.out.println("detected I");

				} else if (currentTerminal.spelling.equals("C")) {
					System.out.println("detected C");
				} else {
					System.out.println("Erorr you pig, how did you get here?");
					return;
				}
				accept(Token.TYPE);
				parseVarList();
				break;

			case Token.VARN_NAME:
				System.out.println("FunctionDeclaration detected");
				accept(Token.VARN_NAME);
				accept(Token.LEFTPARAN);
				parseArguments();
				accept(Token.RIGHTPARAN);
				parseBlock();
				break;
			default:
				System.out.println("FunctionDeclaration or Type expected");
				break;
		}
		accept(Token.SEMICOLON);
	}

	private void parseVarList(){
		while(currentTerminal.kind != Token.SEMICOLON) {
			accept(Token.VARN_NAME);
			if(currentTerminal.spelling.equals(",")){
				accept(Token.COMMA);
			}
		}
	}

	private void parseArguments()
	{
		while(currentTerminal.kind != Token.RIGHTPARAN) {
			if (currentTerminal.spelling.equals("I")) {
				System.out.println("detected I");
			} else if (currentTerminal.spelling.equals("C")) {
				System.out.println("detected C");
			} else {
				System.out.println("Erorr you pig, how did you get here?");
				break;
			}
			accept(Token.TYPE);
			accept(Token.VARN_NAME);
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
	}

	private void parseFunctionCallArguments()
	{
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
	}


	private void parseComand(){
		while(currentTerminal.kind != Token.DO_END && currentTerminal.kind != Token.FOR_END &&
				currentTerminal.kind != Token.WHILE_END){
			parseOneCommand();
		}
	}
	
	private void parseOneCommand(){
		switch(currentTerminal.kind){
			case Token.FOR:
				accept(Token.FOR);
				accept(Token.LEFTPARAN);
				parseAssignment();
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
	}

	private void parseAssignment(){
		switch(currentTerminal.kind){
			case Token.VARN_NAME:
				accept(Token.VARN_NAME);
				switch(currentTerminal.kind){
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

	private void parseOperation(){
		while (currentTerminal.kind != Token.SEMICOLON && currentTerminal.kind != Token.ASSIG_RIGHT && currentTerminal.kind != Token.RIGHTPARAN){
			boolean notNot = false; //flag used to check if the negation was found: if was not look for an operator ora a semicolon or an assignment
			switch (currentTerminal.kind){
				case Token.VARN_NAME:
					accept(Token.VARN_NAME);
					notNot = true;
					break;
				case Token.LITERAL_NUMBER:
					accept(Token.LITERAL_NUMBER);
					notNot = true;
					break;
				case  Token.NOT:
					accept(Token.NOT);
					break;
				default:
					System.out.println("Error, VarName, LiteralNumber or negation expected");
					break;
			}
			if(notNot){
				switch (currentTerminal.kind){
					case Token.OPERATOR:
						accept(Token.OPERATOR);
						break;
					case Token.ASSIG_RIGHT:
					case Token.SEMICOLON:
					case Token.RIGHTPARAN:
						break;
					default:
						System.out.println("Error, Operator, assignment right or semicolon expected");
						break;
				}
			}
		}
	}

	private void parseOperationOrFunctionCall(){
		switch (currentTerminal.kind){
			case Token.LITERAL_NUMBER:
				accept(Token.LITERAL_NUMBER);
				if(currentTerminal.kind == Token.OPERATOR){
					accept(Token.OPERATOR);
					parseOperation();
				}
				break;

				case Token.VARN_NAME:
					accept(Token.VARN_NAME);
					if(currentTerminal.kind == Token.LEFTPARAN){
						accept(Token.LEFTPARAN);
						parseFunctionCallArguments();
						accept(Token.RIGHTPARAN);
					}else if(currentTerminal.kind == Token.OPERATOR){
						accept(Token.OPERATOR);
						parseOperation();
					}else {
						System.out.println("Eroror, letfParan or operator expected");
					}
					break;
		}
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