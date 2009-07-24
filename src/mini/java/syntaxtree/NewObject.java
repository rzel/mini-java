package mini.java.syntaxtree;

import mini.java.semantics.TypeVisitor;
import mini.java.semantics.Visitor;

public class NewObject extends Exp {
	public Identifier i;

	public NewObject(Identifier ai) {
		super(ai.getLine());
		i = ai;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

	public Type accept(TypeVisitor v) {
		return v.visit(this);
	}
	
	public String toString() {
		return "new " + i + "()";
	}
}
