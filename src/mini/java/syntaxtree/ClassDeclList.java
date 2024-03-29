package mini.java.syntaxtree;

import java.util.Vector;

public class ClassDeclList {
	private Vector<ClassDecl> list;

	public ClassDeclList() {
		list = new Vector<ClassDecl>();
	}

	public void addElement(ClassDecl n) {
		list.addElement(n);
	}

	public ClassDecl elementAt(int i) {
		return (ClassDecl) list.elementAt(i);
	}

	public int size() {
		return list.size();
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(ClassDecl cd : list) {
			sb.append(cd.toString() + "\n");
		}
		
		return sb.toString();
	}
}