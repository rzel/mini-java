package mini.java.syntax.legacy;

public class TableItem {
	private String action;
	private String state;
	private int ruleID;
	public TableItem(String action, String state,int ruleID){
		this.action = action;
		this.state = state;
		this.ruleID = ruleID;
	}
	
	public TableItem(String action, String state){
		this.action = action;
		this.state = state;
	}
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	public String toString(){
		return action+state;
	}

	public int getRuleID() {
		return ruleID;
	}


	public void setRuleID(int ruleID) {
		this.ruleID = ruleID;
	}
}
