
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

/*	public byte getKind(String value){
		for (int i = 0; i < SPELLINGS.length; i++) {
			if(SPELLINGS[i].equals(value)){
				return (byte)i;
			}
		}
	}*/

	public static final byte TYPE = 0;
	public static final byte VARN_NAME = 1;
	public static final byte LITERAL_NUMBER = 2;
	public static final byte OPERATOR = 3;
	public static final byte PROGRAM_START = 4;
	public static final byte PROGRAM_END = 5;
	public static final byte DECLARE = 6;
	public static final byte DECLARE_END = 7;
	public static final byte DO = 8;
	public static final byte DO_END = 9;
	public static final byte FOR = 10;
	public static final byte FOR_END = 11;
	public static final byte WHILE = 12;
	public static final byte WHILE_END = 13;
	public static final byte IF = 14;
	public static final byte ELIF = 15;
	public static final byte ELSE = 16;
	public static final byte FI = 17;
	public static final byte SWITCH = 18;
	public static final byte CASE = 19;
	public static final byte DEFAULT = 20;
	public static final byte SWTICH_END = 21;
	public static final byte START = 22;
	public static final byte END = 23;
	public static final byte GIVEBACKWITH = 24;
	public static final byte NOTHING = 25;
	public static final byte NOT = 26;

	public static final byte COMMA = 27;
	public static final byte SEMICOLON = 28;
	public static final byte LEFTPARAN = 29;
	public static final byte RIGHTPARAN = 30;
	public static final byte COLON = 31;
	public static final byte ASSIG_RIGHT = 32;
	public static final byte ASSIG_LEFT = 33;

    public static final byte EOT = 34;
    public static final byte ERROR = 35;



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

	private static final String TYPES[] = {
			"C",
			"I"
	};
}