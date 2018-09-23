/*
 * 30.08.2016 IParse gone, IScanner gone, minor editing
 * 24.09.2010 IParser
 * 07.10.2009 New package structure
 * 02.10.2006 Small fix in parsePrimary()
 * 28.09.2006 Original version (based on Watt&Brown)
 */
  


public class Parser
{
	private Scanner scan;
	private int i = 0;
	
	private Token currentTerminal;
	
	
	public Parser( Scanner scan )
	{
		this.scan = scan;
		
		currentTerminal = scan.scan();
	}
	
	
	public void parseProgram()
	{
		parseBlock();
		
		if( currentTerminal.kind != Token.EOT )
			System.out.println( "Tokens found after end of program" );
	}
	
	
	private void parseBlock()
	{
		accept( Token.PROGRAM );
		accept( Token.START );
		accept( Token.DECLARE);
		accept( Token.START );
		parseDeclarations();
		accept( Token.DECLAREEND );
		accept( Token.DO );
		accept( Token.DOEND );
		accept( Token.PROGRAMEND );
	}
	
	
	private void parseDeclarations()
	{
		while( currentTerminal.kind == Token.I ||
		       currentTerminal.kind == Token.C ){
			parseOneDeclaration();
			System.out.println("Declaration line : "+ ++i );
		}
	}
	
	
	private void parseOneDeclaration()
	{
		switch( currentTerminal.kind ) {
			case Token.I:
				System.out.println("detect I");
				accept( Token.I );
				accept( Token.IDENTIFIER );
				accept( Token.SEMICOLON );
				break;
				
			case Token.C:
				System.out.println("detect C");
				accept( Token.C );
				accept( Token.IDENTIFIER );
				accept( Token.SEMICOLON );
				break;
				
			case Token.CFUNC:
				accept( Token.CFUNC );
				accept( Token.IDENTIFIER );
				accept( Token.LEFTPARAN );
				
				parseIdList();
					
				accept( Token.RIGHTPARAN );
				//parseBlock();
				accept( Token.RETURN );
				//parseExpression();
				accept( Token.SEMICOLON );
				accept( Token.FUNCEND );
				break;
				
			default:
				System.out.println( "var or func expected" );
				break;
		}
	}
	
	
	private void parseIdList()
	{
		while(currentTerminal.kind != Token.RIGHTPARAN) {
			switch( currentTerminal.kind ) {
				case Token.I:
					System.out.println("detect I");
					accept( Token.I );
					accept( Token.IDENTIFIER );
					accept( Token.COMMA );
					break;
			
				case Token.C:
					System.out.println("detect C");
					accept( Token.C );
					accept( Token.IDENTIFIER );
					accept( Token.COMMA );
					break;
		
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
	
	
	private void parseExpression()
	{
		parsePrimary();
		while( currentTerminal.kind == Token.OPERATOR ) {
			accept( Token.OPERATOR );
			parsePrimary();
		}
	}
	
	
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
		System.out.println(currentTerminal.kind + "      " + expected);
		if( currentTerminal.kind == expected )
			currentTerminal = scan.scan();
		else
			System.out.println( "Expected token of kind " + expected );
	}
}