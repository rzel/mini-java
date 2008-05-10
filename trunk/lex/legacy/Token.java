package lex.legacy;

public class Token {
	private String type;
	private String text;
	private int lineNum;
	private int column;
	
	public static String NON_TERMINAL_TYPE = "NON_TERMINAL";
	public static Token START_TOKEN = new Token(Token.NON_TERMINAL_TYPE, "S'");
	public static Token END_TOKEN = new Token("END", "$");
	
	
	public Token(String type, String text, int lineNum, int column) {
		this.type = type;
		this.text = text;
		this.lineNum = lineNum;
		this.column = column;
	}

    public Token(String type, String text) {
        this(type, text, 0, 0);
    }
    
    /* For terminals */
    /* TODO: May not be necessary */
    public Token(String type) {
    	this(type, "", 0, 0);
    }
    
	public String getType() {
		return type;
	}
	
	public void setType(String t) {
		type = t;
	}

	public String getText() {
		return text;
	}
	
	public int getLineNum() {
		return lineNum;
	}
	
	public int getColumn() {
		return column;
	}

	public String toString(){
		return "text:	"+text+"	type:	"+type+"	line:	"+lineNum+"	col:	"+column;
//        return "<" + type + ", " + text + ">";	
    }

    public int hashCode() {
    	return 7 * text.hashCode() + 11 * type.hashCode() + 17 * lineNum + 23 * column;
    }

    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof Token)) {
            return false;
        } else {
            Token t = (Token)obj;
            return t.text.equals(this.text) && t.type.equals(this.type) &&
            	t.lineNum == this.lineNum && t.column == this.column;
        }
    }
}

