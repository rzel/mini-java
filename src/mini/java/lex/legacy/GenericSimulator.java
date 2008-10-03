package mini.java.lex.legacy;
import java.util.Iterator;

import mini.java.fa.legacy.v2.DFA;
import mini.java.regex.legacy.RegexCompiler;
import mini.java.regex.legacy.RegularExpression;

public class GenericSimulator implements DFASimulator {
	public static final String SPEC_FILE = "spec/MiniJava.spec";
	private JDomParser parser;
	
	public GenericSimulator(String fn) {
		if(fn == null || fn == "") {
			fn = SPEC_FILE;
		}
		parser = new JDomParser(fn);
	}
	
	public void setSpecFile(String fn) {
		if(fn == null || fn == "") return;
		System.out.println("setSpecFile: "+ fn);
		parser = new JDomParser(fn);
	}


	public Tokenizer getTokenizer() {
		Tokenizer t = new Tokenizer();

        try {
            parser.jDomParser();
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
        
        for(Iterator<RegularExpression> iter = parser.getRegularExpressions(); iter.hasNext();) {        
            RegularExpression regExp = iter.next();
            String type = regExp.getType();
            String regStr = regExp.getRegExpress();
          //  System.out.println("** regStr: " + regStr);
            //System.out.println("** type: " + type);
            DFA dfa = null;
            try {
                // FIXME add convertor
                //dfa = RegexCompiler.compile(regStr).toDFA().toDFA0();
            } catch (Exception e) {
                System.out.println(e);
                throw new RuntimeException(e);
            }
            t.addDFA(dfa, type);
        }
        
        for(Iterator<Modifier> iter = parser.getModifiers(); iter.hasNext();) {
        	t.addModifier(iter.next());
        }
//        t.addModifier(new KeywordModifier());
		
		// TODO Auto-generated method stub
		return t;
	}

}
