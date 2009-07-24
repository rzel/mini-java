package mini.java.syntaxtree;

import mini.java.semantics.TypeVisitor;
import mini.java.semantics.Visitor;

public class VoidType extends Type {

	@Override
	public void accept(Visitor v) {
		// TODO Auto-generated method stub

	}

	@Override
	public Type accept(TypeVisitor v) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String toString() {
		return "VOID";
	}

	public boolean equals(Object o) {
		return (o != null) && (o instanceof VoidType); 
	}
}
