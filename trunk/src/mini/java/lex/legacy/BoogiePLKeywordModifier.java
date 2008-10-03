package mini.java.lex.legacy;
/**
 * Overview: This class defines the keywords of the C language.
 */


public class BoogiePLKeywordModifier implements Modifier {
	
	
	private final String keywords[] = {
			"const","function","returns","axiom","bool","var"
			,"implementation","procedure","goto","requires"
			,"modifies","ensures","return","assert","assume"
			,"havoc","call","true","false","old","cast","null"
			,"int","name","type","free"
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
