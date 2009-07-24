package mini.java.syntaxtree;

import mini.java.semantics.TypeVisitor;
import mini.java.semantics.Visitor;

public class Assign extends Statement {
	public Identifier i;
	public Exp e;

	public Assign(Identifier ai, Exp ae) {
		i = ai;
		e = ae;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

	public Type accept(TypeVisitor v) {
		return v.visit(this);
	}
	
	public String toString() {
		return i + "=" + e + ";\n";
	}
}
