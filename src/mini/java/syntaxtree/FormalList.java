package mini.java.syntaxtree;

import java.util.Vector;

public class FormalList {
	private Vector<Formal> list;

	public FormalList() {
		list = new Vector<Formal>();
	}

	public void addElement(Formal n) {
		list.addElement(n);
	}

	public Formal elementAt(int i) {
		return list.elementAt(i);
	}

	public int size() {
		return list.size();
	}
	
	public String toString() {
		if(list.isEmpty()) return "EMPTY";
		
		StringBuffer sb = new StringBuffer(list.get(0).toString());
		for(int i = 1; i < list.size(); i++) {
			sb.append(", " + list.get(i).toString());
		}
		
		return sb.toString();
	}
	
	public boolean equals(Object o) {
		if(o == null || !(o instanceof FormalList)) {
			return false;
		} else {
			FormalList fl = (FormalList)o;
			return fl.list.equals(this.list);
		}
	}
}
