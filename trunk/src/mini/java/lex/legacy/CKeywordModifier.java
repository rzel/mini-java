package mini.java.lex.legacy;
/**
 * Overview: This class defines the keywords of the C language.
 */


public class CKeywordModifier implements Modifier {
	
	/** TODO
	 * 		1. System.out.println?
	 * 		2. 
	 */
	
	private final String keywords[] = {
			"int", "boolean", "String",	// type
			"public", "class", "static", "void", "main",
			"extends","auto","break","case","char","const","continue","default",
			"do","double","else","enum","extern","float","for","goto","long","register",
			"short","signed","unsigned","sizeof","struct","switch","typedef","union",
			"if", "while","new","for" ,"true","false","return","include", "define", "ifndef",
			"endif", "undef", "ifdef"
	};	// TODO: not finished!


	public void modify(Token t) {
		if(!t.getType().equals("IDENTIFIER")) return;
		
		for(String k : keywords) {
			if(k.equals(t.getText())) {
				t.setType("KEYWORD");
			}
		}
	}

}
