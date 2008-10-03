package mini.java.syntax.legacy;
public class Transition {
	private int left;
	private int right;
	private TokenSpec token;
	
	public Transition(int left,int right,TokenSpec token ){
		this.left = left;
		this.right = right;
		this.token = token;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public int getRight() {
		return right;
	}

	public void setRight(int right) {
		this.right = right;
	}

	public TokenSpec getToken() {
		return token;
	}

	public void setToken(TokenSpec token) {
		this.token = token;
	}
	
	public String toString(){
		return left+" to "+right+" through "+token;
	}
	 public boolean equals(Object obj) {
	        if(obj == null || !(obj instanceof Transition)) {
	            return false;
	        } else {
	        	Transition t = (Transition)obj;      

	           if((this.left == t.getLeft())&&(this.right == t.getRight()) 
	        		   &&(this.token.equals(t.getToken()))){
	        	   return true;
	           }else {
	            	return false;
	            }            
	        }
	    }
	
}
