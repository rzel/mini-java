package mini.java.syntaxtree;

import mini.java.semantics.TypeVisitor;
import mini.java.semantics.Visitor;

public abstract class Exp {
	protected int line;
	
	public Exp(int line) {
		this.line = line;
	}
	
	public abstract void accept(Visitor v);

	public abstract Type accept(TypeVisitor v);

	public int getLine() {
		return line;
	}
}
