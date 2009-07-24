package mini.java.syntax.legacy;
public class ErrorEntry {
	public static String PRODUCTION_ERROR ="Production Parse Error";
	public static String AMBIGUOUS_GRAMMAR = "Ambigous Grammar";
	public static String SYNTAX_ERROR ="Syntax Error";	
	private String errorType;
	private String errorInfo;
	private int line;
	private Rule first ;
	private Rule second;
//	private Token token;
	private TokenRevamped token;
	
	public ErrorEntry(String errorType, Rule first,Rule second) {
		this.errorInfo = "Ambigous Grammar according to ";
		this.errorType = errorType;
		this.first = first;
		this.second = second;
	}
	public ErrorEntry(String errorType,int line) {
		this.errorInfo = "There is an error in the production defination file on the line #";
		this.errorType = errorType;
		this.line = line;
	}
	public ErrorEntry(String errorType, TokenRevamped token) {
		this.errorInfo = "There is a syntax error: Token is ";
		this.errorType = errorType;
		this.token=token;
	}
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	public String getErrorType() {
		return errorType;
	}
	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}
	public String getErrorInfo() {
		return errorInfo;
	}
	public String toString(){
		if(errorType.equals(ErrorEntry.PRODUCTION_ERROR)){
			return  errorType+"	"+errorInfo+line;
		}else if(errorType.equals(ErrorEntry.AMBIGUOUS_GRAMMAR)){
			return  errorType+"	"+errorInfo+first.toString()+" and  "+second.toString();
		}else{
			return  errorType+"	"+errorInfo+token.getData();
		}
		//return errorType+"	"+errorInfo;
	}

	public int getErrorProductionLine() {
		return line;
	}
	
	public Rule getFirstConflictProduction() {
		return first;
	}
	
	public Rule getSecondConflictProduction() {
		return second;
	}
	
	public TokenRevamped getErrorToken() {
		return token;
	}
	
	
}
