package mini.java.syntaxtree;

import mini.java.semantics.TypeVisitor;
import mini.java.semantics.Visitor;

public class IntArrayType extends Type {
	public void accept(Visitor v) {
		v.visit(this);
	}

	public Type accept(TypeVisitor v) {
		return v.visit(this);
	}
	
	public String toString() {
		return "int[]";
	}
	
	public boolean equals(Object o) {
		return (o != null) && (o instanceof IntArrayType);
	}
}
