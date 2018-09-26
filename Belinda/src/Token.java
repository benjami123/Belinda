/*
 * 16.08.2016 Minor editing
 * 29.10.2009 New package structure
 * 22.10.2006 isAssignOp(), isAddOp(), isMulOp()
 * 28.09.2006 New keyword: return
 * 22.09.2006 Keyword recoqnition in constructor
 * 22.09.2006 ERROR added
 * 17.09.2006 Original Version (based on Example 4.2 in Watt&Brown)
 *//*
 



public class Token
{
	public byte kind;
	public String spelling;
	
	
	public Token( byte kind, String spelling )
	{
		this.kind = kind;
		this.spelling = spelling;
		
		if( kind == IDENTIFIER )
			for( byte i = 0; i < SPELLINGS.length; ++i )
				if( spelling.equals( SPELLINGS[i] ) ) {
					this.kind = i;
					break;
				}
	}
	
	
	public boolean isAssignOperator()
	{
		if( kind == OPERATOR )
			return containsOperator( spelling, ASSIGNOPS );
		else
			return false;
	}
	
	public boolean isAddOperator()
	{
		if( kind == OPERATOR )
			return containsOperator( spelling, ADDOPS );
		else
			return false;
	}
	
	public boolean isMulOperator()
	{
		if( kind == OPERATOR )
			return containsOperator( spelling, MULOPS );
		else
			return false;
	}
	
	
	private boolean containsOperator( String spelling, String OPS[] )
	{
		for( int i = 0; i < OPS.length; ++i )
			if( spelling.equals( OPS[i] ) )
				return true;
				
		return false;
	}
	
	
	public static final byte IDENTIFIER = 0;
	public static final byte INTEGERLITERAL = 1;
	public static final byte OPERATOR = 2;
	
	public static final byte PROGRAMSTART = 3;
	public static final byte START = 4;
	public static final byte DECLARE = 5;
	public static final byte I = 6;
	public static final byte C = 7;
	public static final byte DECLAREEND = 8;
	public static final byte DO = 9;
	public static final byte DOEND = 10;
	public static final byte ELSE = 11;
	public static final byte FI = 12;
	public static final byte CFUNC = 13;
	public static final byte IFUNC = 14;
	public static final byte IF = 15;
	public static final byte GIVEBACKWITH = 16;
	public static final byte SAY = 17;
	public static final byte THEN = 18;
	public static final byte WHILE = 19;
	public static final byte PROGRAMEND = 20;
	
	public static final byte COMMA = 21;
	public static final byte SEMICOLON = 22;
	public static final byte LEFTPARAN = 23;
	public static final byte RIGHTPARAN = 24;
	
	public static final byte EOT = 25;
	
	public static final byte ERROR = 26;
	
	
	private static final String SPELLINGS[] =
	{
		"<identifier>",
		"<integerliteral>",
		"<operator>",
		
		"programStart",
		"start",
		"declare",
		"I",
		"C",
		"declareEnd",
		"do",
		"doEnd",
		"else",
		"fi",
		"C",
		"I",
		"if",
		"giveBackWith",
		"say",
		"then",
		"while",
		"programEnd",
		
		",",
		";",
		"(",
		")",
		"<eot>",
		"<error>",
	};
	
	
	private static final String ASSIGNOPS[] =
	{
		"<-",
		"->",
	};
	
	private static final String ADDOPS[] =
	{
		"+",
		"-",
	};
	
	private static final String MULOPS[] =
	{
		"*",
		"/",
		"%",
	};
}*/ //version on git


public class Token {

	public byte kind;
	public String spelling;

	public Token(byte kind, String spelling){
		this.kind = kind;
		this.spelling = spelling;

		if(kind == VARN_NAME){
			for (byte i = 0; i < SPELLINGS.length; i++) {
				if(spelling.equals(SPELLINGS[i])){
					this.kind = i;
					break;
				}
			}
		}
	}

	public boolean isLogicOperator(){
		if(kind == OPERATOR){
			return containsOperator(spelling, LOGICOPS);
		}else {
			return false;
		}
	}

	public boolean isAddOperator(){
		if(kind == OPERATOR){
			return containsOperator(spelling, ADDOPS);
		}else {
			return false;
		}
	}
	public boolean isMulOperator(){
		if(kind == OPERATOR){
			return containsOperator(spelling, MULOPS);
		}else {
			return false;
		}
	}

	private boolean containsOperator(String spelling, String OPS[]){
		for(int i = 0; i < OPS.length; i++){
			if(spelling.equals(OPS[i])){
				return true;
			}
		}
		return false;
	}

	public static final byte VARN_NAME = 0;
	public static final byte LITERAL_NUMBER = 1;
	public static final byte OPERATOR = 2;
	public static final byte PROGRAM_START = 3;
	public static final byte PROGRAM_END = 4;
	public static final byte DECLARE = 5;
	public static final byte DECLARE_END = 6;
	public static final byte DO = 7;
	public static final byte DO_END = 8;
	public static final byte FOR = 9;
	public static final byte FOR_END = 10;
	public static final byte WHILE = 11;
	public static final byte WHILE_END = 12;
	public static final byte IF = 13;
	public static final byte ELIF = 14;
	public static final byte ELSE = 15;
	public static final byte FI = 16;
	public static final byte SWITCH = 17;
	public static final byte CASE = 18;
	public static final byte DEFAULT = 19;
	public static final byte SWTICH_END = 20;
	public static final byte START = 21;
	public static final byte END = 22;
	public static final byte GIVEBACKWITH = 23;
	public static final byte NOTHING = 24;
	public static final byte NOT = 25;

	public static final byte COMMA = 26;
	public static final byte SEMICOLON = 27;
	public static final byte LEFTPARAN = 28;
	public static final byte RIGHTPARAN = 29;
	public static final byte COLON = 30;
	public static final byte ASSIG_RIGHT = 31;
	public static final byte ASSIG_LEFT = 32;

    public static final byte EOT = 33;
    public static final byte ERROR = 34;


	private static final String SPELLINGS[] = {
			"<var_name>",
			"<Literal_number>",
			"<operator>",

			"programStart",
			"programEnd",
			"declare",
			"declareEnd",
			"do",
			"doEnd",
			"for",
			"forEnd",
			"while",
			"whileEnd",
			"if",
			"elif",
			"else",
			"fi",
			"switch",
			"case",
			"default",
			"switchEnd",
			"start",
			"end",
			"giveBackWith",
			"nothing",
			".not",
			",",
			";",
			"(",
			")",
			":",
			"->",
			"<-",

            "<eot>",
            "<error>"
	};

	private static final String LOGICOPS[] = {
			".=",
			".<",
			".>",
			".<=",
			".>=",
			".or",
			".and"
	};

	private static final String ADDOPS[] = {
			".+",
			".-"
	};

	private static final String MULOPS[] = {
			".*",
			"./",
			".%"
	};
}