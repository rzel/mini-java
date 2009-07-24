package mini.java.syntaxtree;

import mini.java.semantics.TypeVisitor;
import mini.java.semantics.Visitor;

public class While extends Statement {
	public Exp e;
	public Statement s;

	public While(Exp ae, Statement as) {
		e = ae;
		s = as;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

	public Type accept(TypeVisitor v) {
		return v.visit(this);
	}
	
	public String toString() {
		return "while(" + e + ") " + s + "\n";
	}
}
