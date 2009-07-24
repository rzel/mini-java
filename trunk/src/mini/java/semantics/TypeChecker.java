package mini.java.semantics;

import mini.java.syntaxtree.Program;

public class TypeChecker {
	private static TypeChecker theInstance;
//	private List<ErrorMsg> errors; 
	
	private TypeChecker() {
//		errors = new ArrayList<ErrorMsg>();
	}
	
	public static TypeChecker newInstance() {
		if(theInstance == null) {
			theInstance = new TypeChecker();
		}
		
		return theInstance;
	}
	
	public void check(Program prog) {
		
	}
	
}
