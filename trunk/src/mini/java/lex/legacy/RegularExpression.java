package mini.java.lex.legacy;
/**
 * 
 * A typical representation of a regular expression.
 *
 */
public class RegularExpression {
	private String type;
	private String regExpress;
	
	public void setType(String type){
		this.type=type;
	}
	public String getType(){
		return type;
	}
	public void setRegExpress(String regExpress){
		this.regExpress=regExpress;
	}
	public String getRegExpress(){
		return regExpress;
	}
	
	public void showLink(){
		System.out.println("type:  "+type +"  regExpress "+regExpress);
	}
	
}
