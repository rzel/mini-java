package mini.java.syntaxtree;

import mini.java.semantics.TypeVisitor;
import mini.java.semantics.Visitor;

public class IntegerLiteral extends Exp {
	public int i;	

	public IntegerLiteral(int ai, int line) {
		super(line);
		i = ai;		
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

	public Type accept(TypeVisitor v) {
		return v.visit(this);
	}
	
	public String toString() {
		return String.valueOf(i);
	}

	@Override
	public int getLine() {
		return line;
	}
}
