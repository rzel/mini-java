package mini.java.syntax.legacy;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestCase {
	public static void testcase1(){
		Parser parser = Parser.getInstance();
		parser.initProductions("test1.spec");
		parser.testProductions();
		parser.buildAnalysisTable().printTable();
		parser.getStateSet().printStateSet();
		parser.getTransitionSet().printTransitionSet();
		
	}
	public static void testcase2(){
		Parser parser = Parser.getInstance();
		parser.initProductions("test2.spec");
		parser.testProductions();
		parser.buildAnalysisTable().printTable();
		parser.getStateSet().printStateSet();
		parser.getTransitionSet().printTransitionSet();
		
		//parser.getAnalysisTable().printTable();
	}
	public static void testcase3(){
		Parser parser = Parser.getInstance();
		parser.initProductions("test3.spec");
		parser.testProductions();
		parser.buildAnalysisTable().printTable();
		parser.getStateSet().printStateSet();
		parser.getTransitionSet().printTransitionSet();
		
	}
	public static void testcase4(){
		List<Token> stream = new ArrayList<Token>();
		
		stream.add(new Token("id","a",1,1));
		stream.add( new Token("=","=",1,2));
		stream.add( new Token("num","7",1,3));
		stream.add( new Token(";",";",1,3));
		
		
		
		/**
		 * b=c+(d = 5+6 ,d)
		 */
		stream.add(new Token("id","b",2,1));
		stream.add( new Token("=","=",2,2));
		stream.add( new Token("id","c",2,3));
		stream.add( new Token("+","+",2,6));
		stream.add(new Token("(","(",2,1));
		stream.add( new Token("id","d",2,2));
		stream.add( new Token("=","=",2,2));
		stream.add( new Token("num","5",2,6));		
		stream.add(new Token("*","*",2,1));
		stream.add( new Token("num","6",2,2));
		stream.add( new Token(",",",",2,3));
		stream.add( new Token("id","d",2,6));
		stream.add( new Token(")",")",2,6));
		stream.add( new Token("$","$",3,6));
		
		
		Parser parser = Parser.getInstance();
		parser.initProductions("test4.spec");
		parser.testProductions();
		parser.buildAnalysisTable().printTable();
		//parser.getStateSet().printStateSet();
		//parser.getTransitionSet().printTransitionSet();
		
		parser.buildAST(stream).getRootElement().dump();
		parser.getErrorList().printAllErrorList();
	}
	public static void testParserInitProductions(){
		Parser parser = Parser.getInstance();
		parser.initProductions("test1.spec");
		parser.testProductions();
		parser.buildAnalysisTable().printTable();
		parser.getStateSet().printStateSet();
		//parser.getAnalysisTable().printTable();
	}
	
	public static void testParserClosure(){
		Parser parser = Parser.getInstance();
		parser.initProductions("test1.spec");
		parser.testProductions();
		System.out.println("==============================================================");
		Item item = new Item(parser.getProduction(0),0);
		List<Item> result = parser.closure(item);
		System.out.println("==============================================================");
		for(Iterator<Item> it= result.iterator();it.hasNext();){
			Item tmp = it.next();
//			tmp.dump();
		}
		System.out.println("==============================================================");
	}
	public static void testParserGoto(){
		Parser parser = Parser.getInstance();
		parser.initProductions("test1.spec");
		parser.testProductions();
		System.out.println("==============================================================");
		
		Item item = new Item(parser.getProduction(0),0);
		List<Item> closure = parser.closure(item);		
		List<Item> result0 = parser.goTo(closure, new TokenSpec(TokenSpec.NON_TERMINAL_TYPE,"T"));
		List<Item> result = parser.goTo(result0, new TokenSpec(TokenSpec.TERMINAL_TYPE,"+"));
		
		System.out.println("==============================================================");
		for(Iterator<Item> it= result.iterator();it.hasNext();){
			Item tmp = it.next();
//			tmp.dump();
		}
		System.out.println("==============================================================");
	}
	
	public static void testParserNullable(){
		Parser parser = Parser.getInstance();
		parser.initProductions("test5.spec");
		parser.testProductions();
		System.out.println("==============================================================");
		parser.testNullable();
		
		System.out.println("==============================================================");
	}
	public static void testParserFollowSet(){
		Parser parser = Parser.getInstance();
		parser.initProductions("test1.spec");
		parser.testProductions();
		System.out.println("==============================================================");
		parser.testFollow();
		
		System.out.println("==============================================================");
	}
	public static void testParserAST(){
		String entry = "s3";
		String[] tmp = entry.split("");
		
		char action = entry.charAt(0);
		int state =Integer.parseInt(String.valueOf(entry.charAt(1)));
		
		System.out.println(action);
		System.out.println(state);
	}
	
	public static List<Token> getTokens(String fileName) {		 
//		DFASimulator sim = new GenericSimulator("spec/MiniJava.spec");
//		Tokenizer t = sim.getTokenizer();
//	    Tokenizer t = new Tokenizer();
		
		BufferedReader br = null;
		try{
			br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName))));		
		}catch(FileNotFoundException fnfe){
			fnfe.printStackTrace();
			System.exit(-1);
		}
		String line = "";
		int count = 0;

//		while(true) {
//			try{
//				line = br.readLine();
//			}catch(IOException ioe){
//				ioe.printStackTrace();
//				System.exit(-1);
//			}
//			if(line==null){
//				break;
//			}
//			
//			t.analyze(line + "\n");
//			if(count==0)count++;
//		}
//		t.analyze("\0");
		
		//System.out.println("\n===== Token Stream =====");
//		List<Token> tokens = t.getTokens();
		/* size - 1 since EOF is in it */
		/* TODO: need to provide a better way! */
//		for(int i = 0; i < tokens.size() - 1; i++) {
//			Token tok = tokens.get(i);
//		//	System.out.println("DEBUG type:" + tok.getType());
//			if(tok.getType().equals("SKIP") || tok.getType().equals("COMMENT")||tok.getType().equals("UNKNOWN_TOKEN")) 
//				tokens.remove(i--);
//			//else System.out.println(tok);
//		}
//		/* EOF token */
//		tokens.add(Token.END_TOKEN);
		//System.out.println(Token.END_TOKEN);
		//System.out.println("======================\n");
		
//		return tokens;
		return null;
	}
	public static void testMiniJava(){
		List<Token> stream = getTokens("MiniJavaPrograms/test2.java");
		Parser parser = Parser.getInstance();
		parser.initProductions("test5.spec");
		parser.buildAnalysisTable();
		parser.buildAST(stream);
		parser.getErrorList().printAllErrorList();
 	}
	public static void main(String args[]){
		//testParserInitProductions();
		//testParserClosure();
		//testParserGoto();
		//testcase1();
		//testcase2();
		//testcase3();
		//testcase4();
		//testParserNullable();
		//testParserFollowSet();
		testMiniJava();
	}
}
