package mini.java.semantics;

public class ErrorMsg {
	private String msg;
	private int line;
	public ErrorMsg(String msg, int line) {		
		this.msg = msg;
		this.line = line;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
	
	public String toString() {
		return msg;
	}
	
}
