package mini.java.syntax.legacy;
public class TokenSpec {
	private String type;
	private String text;
	
	public static String NON_TERMINAL_TYPE = "NON_TERMINAL";
	public static String TERMINAL_TYPE = "TERMINAL";
	public static TokenSpec START_TOKEN = new TokenSpec(TokenSpec.NON_TERMINAL_TYPE, "S'");
	public static TokenSpec END_TOKEN = new TokenSpec("END", "$");
	public static TokenSpec EMPTY_TOKEN = new TokenSpec("EMPTY", "#");
	
	public TokenSpec(String type, String text) {
		this.type = type;
		this.text = text;
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

	public String toString() {
       // return "<" + type + ", " + text + ">";	
		return text;
    }

    public int hashCode() {
    	if(type.equals(TokenSpec.NON_TERMINAL_TYPE)) {
    		return 11 * type.hashCode() + 7 * text.hashCode();
    	} else {
    		return 17 * type.hashCode();
    	}    
    }
    public boolean compareToken(TokenRevamped token){
    	if(this.text.equals(token.getType())){
    		return true;
    	}else{
    		return false;
    	}
    }
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof TokenSpec)) {
            return false;
        } else {
            TokenSpec t = (TokenSpec)obj;      

            if((this.text.equals(t.getText()) && (this.type.equals(t.getType())))) {
            	return true;
            } else {
            	return false;
            }            
        }
    }
}
