/*
 * 16.08.2016 IScanner gone, minor editing
 * 20.09.2010 IScanner
 * 25.09.2009 New package structure
 * 22.09.2006 Original version (based on Example 4.21 in Watt&Brown)
 */




public class Scanner
{
	private SourceFile source;
	
	private char currentChar;
	private StringBuffer currentSpelling = new StringBuffer();
	
	
	public Scanner( SourceFile source )
	{
		this.source = source;
		
		currentChar = source.getSource();
	}
	
	
	private void takeIt()
	{
		currentSpelling.append( currentChar );
		currentChar = source.getSource();
	}
	
	
	private boolean isLetter( char c )
	{
		return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
	}
	
	
	private boolean isDigit( char c )
	{
		return c >= '0' && c <= '9';
	}
	
	
	private void scanSeparator()
	{
		switch( currentChar ) {
//			case '#':
//				takeIt();
//				while( currentChar != SourceFile.EOL && currentChar != SourceFile.EOT )
//					takeIt();
//
//				if( currentChar == SourceFile.EOL )
//					takeIt();
//				break;
				
			case ' ': case '\n': case '\r': case '\t':
				takeIt();
				break;
		}
	}
	
	
	private byte scanToken()
	{
		if( isLetter( currentChar ) ) {
			takeIt();
			while( isLetter( currentChar ) || isDigit( currentChar ) )
				takeIt();
				
			return Token.VARN_NAME;
			
		} else if( isDigit( currentChar ) ) {
			takeIt();
			while( isDigit( currentChar ) )
				takeIt();
				
			return Token.LITERAL_NUMBER;
			
		} switch( currentChar ) {
            case '.':
                takeIt();
                switch( currentChar ){
                    case '+': case '-': case '*': case '/': case '%': case '<': case '>': case '=':
                        takeIt();
                        return Token.OPERATOR;
                    case 'a':
                        takeIt();
                        if(currentChar == 'n'){
                            takeIt();
                            if(currentChar == 'd'){
                                takeIt();
                                return Token.OPERATOR;
                            }else {
                                return Token.ERROR;
                            }
                        }else {
                            return Token.ERROR;
                        }
                    case 'o':
                        takeIt();
                        if(currentChar == 'r'){
                            takeIt();
                            return Token.OPERATOR;
                        }else {
                            return Token.ERROR;
                        }
                    case  'n':
                        takeIt();
                        if(currentChar == 'o'){
                            takeIt();
                            if(currentChar == 't'){
                                takeIt();
                                return Token.NOT;
                            }else {
                                return Token.ERROR;
                            }
                        }else {
                            return Token.ERROR;
                        }
                }
			case ':':
				takeIt();
				return Token.COLON;
				
			case ',':
				takeIt();
				return Token.COMMA;
				
			case ';':
				takeIt();
				return Token.SEMICOLON;
				
			case '(':
				takeIt();
				return Token.LEFTPARAN;
				
			case ')':
				takeIt();
				return Token.RIGHTPARAN;
				
			case SourceFile.EOT:
				return Token.EOT;
				
			default:
				takeIt();
				return Token.ERROR;
		}
	}
	
	
	public Token scan()
	{
		while( /*currentChar == '#' ||*/ currentChar == '\n' ||
		       currentChar == '\r' || currentChar == '\t' ||
		       currentChar == ' ' )
			scanSeparator();
			
		currentSpelling = new StringBuffer( "" );
		byte kind = scanToken();
		
		return new Token( kind, new String( currentSpelling ) );
	}
}