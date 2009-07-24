package mini.java.semantics;

import java.util.*;

public class Symbol {
	// to avoid object over-creation
	private static Map<String, Symbol> table = new HashMap<String, Symbol>();
	private String lexeme;
	
	private Symbol(String l) { lexeme = l; }
	
	public String toString() { return lexeme; }
	
	public static Symbol getSymbol(String n) {
		Symbol s = table.get(n);
		if(s == null) {
			s = new Symbol(n);
			table.put(n, s);
		}
		
		return s;
	}
	
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Symbol)) {
			return false;
		} else {
			Symbol s = (Symbol)o;
			return s.lexeme.equals(this.lexeme);
		}
	}
	
	public int hashCode() {
		return lexeme.hashCode();
	}
}
