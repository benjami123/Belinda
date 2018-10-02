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
			/*System.out.println("Declaration line : "+ ++iLineNumber );*/
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
				System.out.println("Function detected");
				accept(Token.VARN_NAME);
				accept(Token.LEFTPARAN);
				parseArguments();
				accept(Token.RIGHTPARAN);
				parseBlock();
				break;
			default:
				System.out.println("Function or Type expected");
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
				System.out.println("Wrong function declaration: arguments");
				break;
			}
		}
	}



	private void parseComand(){
		while(currentTerminal.kind != Token.DO_END){
			parseOneCommand();
		}
	}
	
	private void parseOneCommand(){
		switch(currentTerminal.kind){
			case Token.FOR:
				accept(Token.FOR);
				accept(Token.FOR_END);
				break;

			case Token.WHILE:
				accept(Token.WHILE);
				parseComand();
				accept(Token.WHILE_END);
				break;

			case.Token.IF:
				break;

			case Token.SWITCH:
				break;

			default:
				parseExpression();
				break;
		}
	}

	private void parseExpression(){
		switch(currentTerminal.kind){
			case Token.VARN_NAME:
				accept(Token.VARN_NAME);
				switch(currentTerminal.kind){
					case Token.OPERATOR:
						accept(Token.OPERATOR);
						parseExpressionLeft();
						accept(Token.ASSIG_RIGHT);
						accept(Token.VARN_NAME);
						break;
					
					case Token.ASSIG_LEFT:
						accept(Token.ASSIG_LEFT);
						parseExpressionRight();
						break;

					case Token.ASSIG_RIGHT:
						accept(Token.ASSIG_RIGHT);
						accept(Token.VARN_NAME);	
						break;

					case Token.LEFTPARAN:
						accept(Token.LEFTPARAN);
						parseArguments();
						accept(Token.RIGHTPARAN);

						break;

					default:
						System.out.println("Expected Operator or ArgsList");
						break;
				}
				accept(Token.SEMICOLON);
				break;
			
			case Token.LITERAL_NUMBER:

				break;

			default:
				System.out.println("Expected VAR NAME or LITERAL NUMBER");
				break;
		}
	}

	private void parseExpressionRight(){		//For VarName <- Expression;
		while(currentTerminal.kind != Token.SEMICOLON){
			switch(currentTerminal.kind){
				case Token.VarName:
					accept(Token.VarName);
					if(currentTerminal.kind == Token.LEFTPARAN){
						accept(Token.LEFTPARAN);
						parseArguments();
						accept(Token.RIGHTPARAN);
					}
					break;

				case Token.LITERAL_NUMBER:
					accept(Token.LITERAL_NUMBER);
					break;

				default :
					System.out.println("Expression error, expected LITERAL NUMBER");
					break;
			}
			if(currentTerminal.kind == Token.SEMICOLON){
				break;
			}else{
				if(currentTerminal.kind == Token.OPERATOR){
					accept(Token.OPERATOR);
				}else{
					System.out.println("Expected ;");
					break;
				}
			} 			
		}
	}

	private void parseExpressionLeft(){			//For Expression -> VarName;
		while(currentTerminal.kind != Token.ASSIG_RIGHT){
			switch(currentTerminal.kind){
				case Token.VarName:
					accept(Token.VarName);
					if(currentTerminal.kind == Token.LEFTPARAN){
						accept(Token.LEFTPARAN);
						parseArguments();
						accept(Token.RIGHTPARAN);
					}
					break;

				case Token.LITERAL_NUMBER:
					accept(Token.LITERAL_NUMBER);
					break;

				default :
					System.out.println("Expression error, expected LITERAL NUMBER");
					break;
			}
			if(currentTerminal.kind == Token.ASSIG_RIGHT){
				break;
			}else{
				if(currentTerminal.kind == Token.OPERATOR){
					accept(Token.OPERATOR);
				}else{
					System.out.println("Expected ->");
					break;
				}
			} 			
		}	
	}
	
	

	
	
	private void parseStatements()
	{
		while( currentTerminal.kind == Token.IDENTIFIER ||
		       currentTerminal.kind == Token.OPERATOR ||
		       currentTerminal.kind == Token.INTEGERLITERAL ||
		       currentTerminal.kind == Token.LEFTPARAN ||
		       currentTerminal.kind == Token.IF ||
		       currentTerminal.kind == Token.WHILE ||
		       currentTerminal.kind == Token.SAY )
			parseOneStatement();
	}
	
	
	private void parseOneStatement()
	{
		switch( currentTerminal.kind ) {
			case Token.IDENTIFIER:
			case Token.INTEGERLITERAL:
			case Token.OPERATOR:
			case Token.LEFTPARAN:
				parseExpression();
				accept( Token.SEMICOLON );
				break;
				
			case Token.IF:
				accept( Token.IF );
				parseExpression();
				accept( Token.THEN );
				parseStatements();
				
				if( currentTerminal.kind == Token.ELSE ) {
					accept( Token.ELSE );
					parseStatements();
				}
				
				accept( Token.FI );
				accept( Token.SEMICOLON );
				break;
				
//			case Token.WHILE:
//				accept( Token.WHILE );
//				parseExpression();
//				accept( Token.DO );
//				parseStatements();
//				accept( Token.OD );
//				accept( Token.SEMICOLON );
//				break;
				
			case Token.SAY:
				accept( Token.SAY );
				parseExpression();
				accept( Token.SEMICOLON );
				break;
				
			default:
				System.out.println( "Error in statement" );
				break;
		}
	}
	
	
/*	private void parseExpression()
	{
		parsePrimary();
		while( currentTerminal.kind == Token.OPERATOR ) {
			accept( Token.OPERATOR );
			parsePrimary();
		}
	}*/
	
	
	private void parsePrimary()
	{
		switch( currentTerminal.kind ) {
			case Token.IDENTIFIER:
				accept( Token.IDENTIFIER );
				
				if( currentTerminal.kind == Token.LEFTPARAN ) {
					accept( Token.LEFTPARAN );
					
					if( currentTerminal.kind == Token.IDENTIFIER ||
					    currentTerminal.kind == Token.INTEGERLITERAL ||
					    currentTerminal.kind == Token.OPERATOR ||
					    currentTerminal.kind == Token.LEFTPARAN )
						parseExpressionList();
						
					
					accept( Token.RIGHTPARAN );
				}
				break;
				
			case Token.INTEGERLITERAL:
				accept( Token.INTEGERLITERAL );
				break;
				
			case Token.OPERATOR:
				accept( Token.OPERATOR );
				parsePrimary();
				break;
				
			case Token.LEFTPARAN:
				accept( Token.LEFTPARAN );
				parseExpression();
				accept( Token.RIGHTPARAN );
				break;
				
			default:
				System.out.println( "Error in primary" );
				break;
		}
	}
	
	
	private void parseExpressionList()
	{
		parseExpression();
		while( currentTerminal.kind == Token.COMMA ) {
			accept( Token.COMMA );
			parseExpression();
		}
	}
	
	
	private void accept( byte expected )
	{
		System.out.println(currentTerminal.kind + "\t" + expected);
		if( currentTerminal.kind == expected )
			currentTerminal = scan.scan();
		else
			System.out.println( "Expected token of kind " + expected );
	}
}