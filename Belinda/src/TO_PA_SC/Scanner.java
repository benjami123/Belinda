package TO_PA_SC;

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

	private boolean isType( char c )
	{
		for (char arC: Token.TYPES) {
			if(arC == c){
				return true;
			}
		}
		return false;
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
			case '!':
				takeIt();
				while( currentChar != SourceFile.EOL && currentChar != SourceFile.EOT )
					takeIt();

				if( currentChar == SourceFile.EOL )
					takeIt();
				break;

			case ' ': case '\n': case '\r': case '\t':
				takeIt();
				break;
		}
	}
	
	
	private byte scanToken()
	{
		if(isType(currentChar)){
			takeIt();
			return Token.TYPE;
		}else if( isLetter( currentChar ) ) {
			takeIt();
			while( isLetter( currentChar ) || isDigit( currentChar ) )
				takeIt();				
			return Token.VARN_NAME;
			
		} else if( isDigit( currentChar ) ) {
			takeIt();
			while( isDigit( currentChar ) )
				takeIt();
				
			return Token.LITERAL_NUMBER;
			
		} else if(currentChar == '|'){
			takeIt();
			takeIt();	//Char that will be transformed with the ascii table
			if(currentChar == '|'){
				takeIt();
				return Token.LITERAL_NUMBER;
			}else{
				return Token.ERROR;
			}

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
                        default:
                        	takeIt();
							return Token.ERROR;
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
			case '<':
				takeIt();
				if(currentChar == '-'){
					takeIt();
					return Token.ASSIG_LEFT;
				}else {
					System.out.println("Error: expected - after < to do the left assignment");
					takeIt();
					return Token.ERROR;
				}
			case '-':
				takeIt();
				if(currentChar == '>'){
					takeIt();
					return Token.ASSIG_RIGHT;
				}else {
					System.out.println("Error: expected > after - to do the right assignment");
					takeIt();
					return Token.ERROR;
				}
				
			case SourceFile.EOT:
				return Token.EOT;
				
			default:
				takeIt();
				return Token.ERROR;
		}
	}
	
	
	public Token scan()
	{
		while( currentChar == '!' || currentChar == '\n' ||
		       currentChar == '\r' || currentChar == '\t' ||
		       currentChar == ' ' )
			scanSeparator();
			
		currentSpelling = new StringBuffer( "" );
		byte kind = scanToken();
		
		return new Token( kind, new String( currentSpelling ) );
	}
}