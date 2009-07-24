package mini.java.syntaxtree;

import mini.java.semantics.TypeVisitor;
import mini.java.semantics.Visitor;

public class ArrayLength extends Exp {
	public Exp e;

	public ArrayLength(Exp ae) {
		super(ae.line);
		e = ae;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

	public Type accept(TypeVisitor v) {
		return v.visit(this);
	}

	public String toString() {
		return e + ".length";
	}

}
