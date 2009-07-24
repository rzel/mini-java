package mini.java.syntaxtree;

import mini.java.semantics.TypeVisitor;
import mini.java.semantics.Visitor;

public abstract class Statement {
	public abstract void accept(Visitor v);

	public abstract Type accept(TypeVisitor v);
}
