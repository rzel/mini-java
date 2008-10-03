package mini.java.lex.legacy;

/**
 * 
 * A typical Modifier defining the keywords of MiniJava
 *
 */
public class MiniJavaKeywordModifier implements Modifier {
	
	/** TODO
	 * 		1. System.out.println?
	 * 		2. 
	 */
	
	private final String keywords[] = {
			"int", "boolean", "String",	// type
			"public", "class", "static", "void", "main",
			"extends","this","<",">","&&","this","length",
			"if","else", "while","new","for" ,"true","false","return"
	};	// TODO: not finished!


	public void modify(Token t) {
		if(!t.getType().equals("id")) return;
		
		for(String k : keywords) {
			if(k.equals(t.getText())) {
				t.setType(t.getText());
			}
		}
	}

}
