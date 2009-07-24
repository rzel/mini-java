package mini.java.semantics;

public class SymbolTable {
	private Scope root;		// root of the scope tree
	private Scope cur;		// current scopen
	
	public SymbolTable() {
		root = new Scope(null);
		cur = root;
	}
		
	public void enterScope() {
		cur = cur.nextChild();
	}
	
	public void exitScope() {
		cur = cur.getParent();
	}
	
	public void put(Symbol key, Record record) {		
		cur.put(key, record);
	}
	
	public Record lookup(Symbol key) {
		return cur.lookup(key);
	}
	
	public Scope getRoot() {
		return root;
	}
	
	public Scope getCur() {
		return cur;
	}
	
	public void dump() {
		root.dump();
	}
	
	public void dumpCurrent() {
		System.err.println("=== DumpCurrent ===");
		cur.dump();
		System.err.println();
	}
	
	public void resetTable() {
		root.resetScope();
	}
	
}
