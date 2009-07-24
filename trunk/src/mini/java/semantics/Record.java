package mini.java.semantics;

import mini.java.syntaxtree.Type;

public class Record {
	private Symbol symbol;
	private Type type;
	public Record() {		
	}
	
	public Record(Symbol symbol, Type type) {		
		this.symbol = symbol;
		this.type = type;
	}
	
	public Symbol getSymbol() {
		return symbol;
	}
	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
}
