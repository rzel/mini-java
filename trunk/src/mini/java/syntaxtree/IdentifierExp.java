package mini.java.syntaxtree;

import mini.java.semantics.TypeVisitor;
import mini.java.semantics.Visitor;

public class IdentifierExp extends Exp {
	public String s;

	public IdentifierExp(String as, int line) {
		super(line);
		s = as;
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
}
