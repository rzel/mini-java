package mini.java.syntaxtree;

import mini.java.semantics.TypeVisitor;
import mini.java.semantics.Visitor;

public class Call extends Exp {
	public Exp e;
	public Identifier i;
	public ExpList el;

	public Call(Exp ae, Identifier ai, ExpList ael) {
		super(ae.line);
		e = ae;
		i = ai;
		el = ael;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

	public Type accept(TypeVisitor v) {
		return v.visit(this);
	}
	
	public String toString() {
		return e + "." + i + "(" + el + ")";
	}
}
