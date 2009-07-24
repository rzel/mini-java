package mini.java.semantics;

import java.util.*;
//import typechecker.model.*;

public class Scope {
	private Scope parent;
	private List<Scope> children;
	private int nxtPtr;
	private Map<Symbol, Record> records;	
	
	public Scope(Scope p) {
		this.parent = p;
		this.children = new ArrayList<Scope>();
		this.nxtPtr = 0;
		records = new LinkedHashMap<Symbol, Record>();
	}
	
	public List<Scope> getChildren() {
		return children;
	}
	
	public Scope getParent() {
		return parent;
	}
	
	public void put(Symbol key, Record item) {
		// TODO: check duplication

		if(records.containsKey(key)) {
			throw new RuntimeException("DEBUG: " + key + " is already defined");
		}
		
		records.put(key, item);
	}
	
	public Scope nextChild() {
		Scope nxt = null;
		if(nxtPtr >= children.size()) {
			nxt = new Scope(this);
			children.add(nxt);
		} else {
			nxt = children.get(nxtPtr);
		}
		
		nxtPtr++;
		
		return nxt;
	}
	
	public Record lookup(Symbol key) {
		if(records.containsKey(key)) {
			return records.get(key);
		} else {
			if(parent == null) {
				return null;
			} else {
				return parent.lookup(key);
			}
		}
	}
	
	public void resetScope() {
		nxtPtr = 0;
		for(int i = 0; i < children.size(); i++) {
			children.get(i).resetScope();
		}
	}
	
	public void dump() {
		dump(0);
	}
	
	public void dump(int level) {
		// pre-order traverse
		System.out.println("* Scope Level " + level + ":");
		for(Iterator<Record> iter = records.values().iterator(); iter.hasNext();) {
			Record r = iter.next();
			System.out.print(r.getSymbol().toString() + "->" + r.getType().toString() + " ");
		}
		System.out.println();
		for(Scope s : children) {
			s.dump(level + 1);
		}
	}
}
