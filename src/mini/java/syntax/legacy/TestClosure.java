package mini.java.syntax.legacy;


public class TestClosure {
//
//	public static void testGetFollows() {
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
//        testGetFollowsHelper(plus, rules);
//        testGetFollowsHelper(id, rules);
//        testGetFollowsHelper(e, rules);
//	}
//	
//	public static void testGetFollowsHelper(Token t, List<Rule> rules) {
//		List<Token> follows = Algorithm.getFollows(t, rules);
//        System.out.println(t.getText() + ": " + follows);
//	}
//	
//    public static void test1() {
//
//    	Token id = new Token("IDENTIFIER", "id");
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
//        System.out.println("\n\n###############################################");
//        table.dump();
//        System.out.println("Number of Entry: " + table.size());
//    }
//    
//    public static void test2() {
//
//    	Token id = new Token("IDENTIFIER", "id");
//        Token plus = new Token("PLUS", "+");
//        Token star = new Token("STAR", "*");
//        Token lp = new Token("LPARA", "(");
//        Token rp = new Token("RPARA", ")");
//        Token e = new Token("NON_TERMINAL", "E");
//        Token t = new Token("NON_TERMINAL", "T");
//        Token f = new Token("NON_TERMINAL", "F");
//        Token s = new Token("VIRTUAL", "E'");
//
//        Rule rule0 = new Rule(s);
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
//        Item item1 = new Item(rule1, 2);
//        List<Item> closure = Algorithm.closure(item1, rules);
//        for(Item it : closure) it.dump();
//    }
//    
//    public static void main(String[] args) {
//    	//test1();
//    	//testGetFollows();
//    	//finalTest1();
//    }
}

