package mini.java.syntaxtree;

import mini.java.semantics.TypeVisitor;
import mini.java.semantics.Visitor;

public class MainClass {
	public Identifier i1, i2;
	public Statement s;

	public MainClass(Identifier ai1, Identifier ai2, Statement as) {
		i1 = ai1;	// class name
		i2 = ai2;	// command line argument name
		s = as;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

	public Type accept(TypeVisitor v) {
		return v.visit(this);
	}
	
	public String toString() {
		return "class " + i1 + "{ public static void main ( String [ ] " + i2 + ") {" +
			s + "} }";
	}
}
