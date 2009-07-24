package mini.java.syntaxtree;

import mini.java.semantics.TypeVisitor;
import mini.java.semantics.Visitor;

public class IdentifierType extends Type {
	public String s;

	public IdentifierType(String as) {
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
	
	public boolean equals(Object o) {
		if((o == null) || !(o instanceof IdentifierType)) {
			return false;
		} else {
			IdentifierType it = (IdentifierType)o;
			return it.s.equals(this.s);
		}
	}
}
