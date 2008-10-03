package mini.java.syntax.legacy;




public class FinalTest {
//	/* @IN: id * id + id */
//	public static void test1() {
//		Token id = new Token("IDENTIFIER", "id");
//        Token plus = new Token("PLUS", "+");
//        Token star = new Token("STAR", "*");
//        Token lp = new Token("LPARA", "(");
//        Token rp = new Token("RPARA", ")");
//        Token e = new Token("NON_TERMINAL", "E");
//        Token t = new Token("NON_TERMINAL", "T");
//        Token f = new Token("NON_TERMINAL", "F");
////        Token s = new Token("VIRTUAL", "E'");
//
//        Rule rule0 = new Rule(Token.START_TOKEN);
//        rule0.addRhsToken(e);
//        
//        Rule rule1 = new Rule(e);
//        rule1.addRhsToken(e);
//        rule1.addRhsToken(plus);
//        rule1.addRhsToken(t);
//
//        Rule rule2 = new Rule(e);
//        rule2.addRhsToken(t);
//
//        Rule rule3 = new Rule(t);
//        rule3.addRhsToken(t);
//        rule3.addRhsToken(star);
//        rule3.addRhsToken(f);
//
//        Rule rule4 = new Rule(t);
//        rule4.addRhsToken(f);
//
//        Rule rule5 = new Rule(f);
//        rule5.addRhsToken(lp);
//        rule5.addRhsToken(e);
//        rule5.addRhsToken(rp);
//
//        Rule rule6 = new Rule(f);
//        rule6.addRhsToken(id);
//
//        List<Rule> rules = new ArrayList<Rule>();
//        rules.add(rule0);
//        rules.add(rule1);
//        rules.add(rule2);
//        rules.add(rule3);
//        rules.add(rule4);
//        rules.add(rule5);
//        rules.add(rule6);
//     
//        AnalysisTable table = Algorithm.closureSet(rules);
//        
//        List<Token> input = new LinkedList<Token>();
//        input.add(id);
//        input.add(star);
//        input.add(id);
//        input.add(plus);
//        input.add(id);
//        input.add(Token.END_TOKEN);
//        
//        Tree<Token> ast = Algorithm.parse(table, new State(0), input);
//        ast.getRootElement().dump();
//	}
//	
//	/* @IN: id + id * id */
//	public static void test2() {
//		Token id = new Token("IDENTIFIER", "id");
//        Token plus = new Token("PLUS", "+");
//        Token star = new Token("STAR", "*");
//        Token lp = new Token("LPARA", "(");
//        Token rp = new Token("RPARA", ")");
//        Token e = new Token("NON_TERMINAL", "E");
//        Token t = new Token("NON_TERMINAL", "T");
//        Token f = new Token("NON_TERMINAL", "F");
////        Token s = new Token("VIRTUAL", "E'");
//
//        Rule rule0 = new Rule(Token.START_TOKEN);
//        rule0.addRhsToken(e);
//        
//        Rule rule1 = new Rule(e);
//        rule1.addRhsToken(e);
//        rule1.addRhsToken(plus);
//        rule1.addRhsToken(t);
//
//        Rule rule2 = new Rule(e);
//        rule2.addRhsToken(t);
//
//        Rule rule3 = new Rule(t);
//        rule3.addRhsToken(t);
//        rule3.addRhsToken(star);
//        rule3.addRhsToken(f);
//
//        Rule rule4 = new Rule(t);
//        rule4.addRhsToken(f);
//
//        Rule rule5 = new Rule(f);
//        rule5.addRhsToken(lp);
//        rule5.addRhsToken(e);
//        rule5.addRhsToken(rp);
//
//        Rule rule6 = new Rule(f);
//        rule6.addRhsToken(id);
//
//        List<Rule> rules = new ArrayList<Rule>();
//        rules.add(rule0);
//        rules.add(rule1);
//        rules.add(rule2);
//        rules.add(rule3);
//        rules.add(rule4);
//        rules.add(rule5);
//        rules.add(rule6);
//     
//        AnalysisTable table = Algorithm.closureSet(rules);
//        
//        List<Token> input = new LinkedList<Token>();
//        input.add(id);
//        input.add(plus);
//        input.add(id);
//        input.add(star);
//        input.add(id);
//        input.add(Token.END_TOKEN);
//        
//        Tree<Token> ast = Algorithm.parse(table, new State(0), input);
//        ast.getRootElement().dump();
//	}
//	
//	/* @IN: (id+id)*id */
//	public static void test3() {
//        Token id = new Token("IDENTIFIER", "id");
//        Token plus = new Token("PLUS", "+");
//        Token star = new Token("STAR", "*");
//        Token lp = new Token("LPARA", "(");
//        Token rp = new Token("RPARA", ")");
//        Token e = new Token("NON_TERMINAL", "E");
//        Token t = new Token("NON_TERMINAL", "T");
//        Token f = new Token("NON_TERMINAL", "F");
////        Token s = new Token("VIRTUAL", "E'");
//
//        Rule rule0 = new Rule(Token.START_TOKEN);
//        rule0.addRhsToken(e);
//        
//        Rule rule1 = new Rule(e);
//        rule1.addRhsToken(e);
//        rule1.addRhsToken(plus);
//        rule1.addRhsToken(t);
//
//        Rule rule2 = new Rule(e);
//        rule2.addRhsToken(t);
//
//        Rule rule3 = new Rule(t);
//        rule3.addRhsToken(t);
//        rule3.addRhsToken(star);
//        rule3.addRhsToken(f);
//
//        Rule rule4 = new Rule(t);
//        rule4.addRhsToken(f);
//
//        Rule rule5 = new Rule(f);
//        rule5.addRhsToken(lp);
//        rule5.addRhsToken(e);
//        rule5.addRhsToken(rp);
//
//        Rule rule6 = new Rule(f);
//        rule6.addRhsToken(id);
//
//        List<Rule> rules = new ArrayList<Rule>();
//        rules.add(rule0);
//        rules.add(rule1);
//        rules.add(rule2);
//        rules.add(rule3);
//        rules.add(rule4);
//        rules.add(rule5);
//        rules.add(rule6);
//     
//        AnalysisTable table = Algorithm.closureSet(rules);
//        
//        List<Token> input = new LinkedList<Token>();
//        input.add(lp);
//        input.add(id);
//        input.add(plus);
//        input.add(id);
//        input.add(rp);
//        input.add(star);
//        input.add(id);
//        input.add(Token.END_TOKEN);
//        
//        Tree<Token> ast = Algorithm.parse(table, new State(0), input);
//        ast.getRootElement().dump();
//	}
//	
//	public static void main(String[] args) {
//		//test3();
//		test1();
//	}
}
