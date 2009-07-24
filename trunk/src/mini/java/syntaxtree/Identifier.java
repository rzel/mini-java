package mini.java.syntaxtree;

import mini.java.semantics.TypeVisitor;
import mini.java.semantics.Visitor;

public class Identifier {
	public String s;
	private int line;

	public Identifier(String as, int line) {
		s = as;
		this.line = line;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

	public Type accept(TypeVisitor v) {
		return v.visit(this);
	}

	public String toString() {
		return s;
	}
	
	public int getLine() {
		return line;
	}
}