package mini.java.syntax.legacy;



public class TestAlgorithm {
//	
//    /* @G: Page 144 */
//    /* @IN: id * id + id */
//    public static void test1() {
//        System.out.println("Input: id*id+id");
//        State[] states = new State[12];
//        for(int i = 0; i < 12; i++) states[i] = new State(i);
//
//        Token id = new Token("TERMINAL", "id");
//        Token plus = new Token("TERMINAL", "+");
//        Token star = new Token("TERMINAL", "*");
//        Token lp = new Token("TERMINAL", "(");
//        Token rp = new Token("TERMINAL", ")");
//        Token end = new Token("EOF", "$");
//        Token e = new Token("NON_TERMINAL", "E");
//        Token t = new Token("NON_TERMINAL", "T");
//        Token f = new Token("NON_TERMINAL", "F");
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
//        Action s4 = new Action(Action.ACTION_SHIFT, states[4]);
//        Action s5 = new Action(Action.ACTION_SHIFT, states[5]);
//        Action s6 = new Action(Action.ACTION_SHIFT, states[6]);
//        Action s7 = new Action(Action.ACTION_SHIFT, states[7]);
//        Action s11 = new Action(Action.ACTION_SHIFT, states[11]);
//
//        Action r1 = new Action(Action.ACTION_REDUCE, rule1);
//        Action r2 = new Action(Action.ACTION_REDUCE, rule2);
//        Action r3 = new Action(Action.ACTION_REDUCE, rule3);
//        Action r4 = new Action(Action.ACTION_REDUCE, rule4);
//        Action r5 = new Action(Action.ACTION_REDUCE, rule5);
//        Action r6 = new Action(Action.ACTION_REDUCE, rule6);
//
//        Action g1 = new Action(Action.ACTION_GOTO, states[1]);
//        Action g2 = new Action(Action.ACTION_GOTO, states[2]);
//        Action g3 = new Action(Action.ACTION_GOTO, states[3]);
//        Action g8 = new Action(Action.ACTION_GOTO, states[8]);
//        Action g9 = new Action(Action.ACTION_GOTO, states[9]);
//        Action g10 = new Action(Action.ACTION_GOTO, states[10]);
//
//        Action acc = new Action(Action.ACTION_ACCT);
//
////      HashMap<Pair<State, Token>, Action> analysisTbl =
////          new HashMap<Pair<State, Token>, Action>();
//        AnalysisTable analysisTbl = new AnalysisTable();
//
//        analysisTbl.put(new Pair<State, Token>(states[0], id), s5);
//        analysisTbl.put(new Pair<State, Token>(states[0], lp), s4);
//        analysisTbl.put(new Pair<State, Token>(states[0], e), g1);
//        analysisTbl.put(new Pair<State, Token>(states[0], t), g2);
//        analysisTbl.put(new Pair<State, Token>(states[0], f), g3);
// 
//        analysisTbl.put(new Pair<State, Token>(states[1], plus), s6);
//        analysisTbl.put(new Pair<State, Token>(states[1], end), acc);
//
//        analysisTbl.put(new Pair<State, Token>(states[2], plus), r2);
//        analysisTbl.put(new Pair<State, Token>(states[2], star), s7);
//        analysisTbl.put(new Pair<State, Token>(states[2], rp), r2);
//        analysisTbl.put(new Pair<State, Token>(states[2], end), r2);
//
//        analysisTbl.put(new Pair<State, Token>(states[3], plus), r4);
//        analysisTbl.put(new Pair<State, Token>(states[3], star), r4);
//        analysisTbl.put(new Pair<State, Token>(states[3], rp), r4);
//        analysisTbl.put(new Pair<State, Token>(states[3], end), r4);
//
//        analysisTbl.put(new Pair<State, Token>(states[4], id), s5);
//        analysisTbl.put(new Pair<State, Token>(states[4], lp), s4);
//        analysisTbl.put(new Pair<State, Token>(states[4], e), g8);
//        analysisTbl.put(new Pair<State, Token>(states[4], t), g2);
//        analysisTbl.put(new Pair<State, Token>(states[4], f), g3);
//
//        analysisTbl.put(new Pair<State, Token>(states[5], plus), r6);
//        analysisTbl.put(new Pair<State, Token>(states[5], star), r6);
//        analysisTbl.put(new Pair<State, Token>(states[5], rp), r6);
//        analysisTbl.put(new Pair<State, Token>(states[5], end), r6);
//
//        analysisTbl.put(new Pair<State, Token>(states[6], id), s5);
//        analysisTbl.put(new Pair<State, Token>(states[6], lp), s4);
//        analysisTbl.put(new Pair<State, Token>(states[6], t), g9);
//        analysisTbl.put(new Pair<State, Token>(states[6], f), g3);
//
//        analysisTbl.put(new Pair<State, Token>(states[7], id), s5);
//        analysisTbl.put(new Pair<State, Token>(states[7], lp), s4);
//        analysisTbl.put(new Pair<State, Token>(states[7], f), g10);
//
//        analysisTbl.put(new Pair<State, Token>(states[8], plus), s6);
//        analysisTbl.put(new Pair<State, Token>(states[8], rp), s11);
//
//        analysisTbl.put(new Pair<State, Token>(states[9], plus), r1);
//        analysisTbl.put(new Pair<State, Token>(states[9], star), s7);
//        analysisTbl.put(new Pair<State, Token>(states[9], rp), r1);
//        analysisTbl.put(new Pair<State, Token>(states[9], end), r1);
//
//        analysisTbl.put(new Pair<State, Token>(states[10], plus), r3);
//        analysisTbl.put(new Pair<State, Token>(states[10], star), r3);
//        analysisTbl.put(new Pair<State, Token>(states[10], rp), r3);
//        analysisTbl.put(new Pair<State, Token>(states[10], end), r3);
//
//        analysisTbl.put(new Pair<State, Token>(states[11], plus), r5);
//        analysisTbl.put(new Pair<State, Token>(states[11], star), r5);
//        analysisTbl.put(new Pair<State, Token>(states[11], rp), r5);
//        analysisTbl.put(new Pair<State, Token>(states[11], end), r5);
//
//        List<Token> input = new LinkedList<Token>();
//        input.add(id);
//        input.add(star);
//        input.add(id);
//        input.add(plus);
//        input.add(id);
//        input.add(end);
//
//        Tree<Token> ast = Algorithm.parse(analysisTbl, states[0], input);
//        ast.getRootElement().dump();
//    }
//
//    /* @IN: id + id * id */
//    public static void test1_1() {
//        System.out.println("Input: id+id*id");
//        State[] states = new State[12];
//        for(int i = 0; i < 12; i++) states[i] = new State(i);
//
//        Token id = new Token("TERMINAL", "id");
//        Token plus = new Token("TERMINAL", "+");
//        Token star = new Token("TERMINAL", "*");
//        Token lp = new Token("TERMINAL", "(");
//        Token rp = new Token("TERMINAL", ")");
//        Token end = new Token("EOF", "$");
//        Token e = new Token("NON_TERMINAL", "E");
//        Token t = new Token("NON_TERMINAL", "T");
//        Token f = new Token("NON_TERMINAL", "F");
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
//        Action s4 = new Action(Action.ACTION_SHIFT, states[4]);
//        Action s5 = new Action(Action.ACTION_SHIFT, states[5]);
//        Action s6 = new Action(Action.ACTION_SHIFT, states[6]);
//        Action s7 = new Action(Action.ACTION_SHIFT, states[7]);
//        Action s11 = new Action(Action.ACTION_SHIFT, states[11]);
//
//        Action r1 = new Action(Action.ACTION_REDUCE, rule1);
//        Action r2 = new Action(Action.ACTION_REDUCE, rule2);
//        Action r3 = new Action(Action.ACTION_REDUCE, rule3);
//        Action r4 = new Action(Action.ACTION_REDUCE, rule4);
//        Action r5 = new Action(Action.ACTION_REDUCE, rule5);
//        Action r6 = new Action(Action.ACTION_REDUCE, rule6);
//
//        Action g1 = new Action(Action.ACTION_GOTO, states[1]);
//        Action g2 = new Action(Action.ACTION_GOTO, states[2]);
//        Action g3 = new Action(Action.ACTION_GOTO, states[3]);
//        Action g8 = new Action(Action.ACTION_GOTO, states[8]);
//        Action g9 = new Action(Action.ACTION_GOTO, states[9]);
//        Action g10 = new Action(Action.ACTION_GOTO, states[10]);
//
//        Action acc = new Action(Action.ACTION_ACCT);
//
////      HashMap<Pair<State, Token>, Action> analysisTbl =
////          new HashMap<Pair<State, Token>, Action>();
//        AnalysisTable analysisTbl = new AnalysisTable();
//
//        analysisTbl.put(new Pair<State, Token>(states[0], id), s5);
//        analysisTbl.put(new Pair<State, Token>(states[0], lp), s4);
//        analysisTbl.put(new Pair<State, Token>(states[0], e), g1);
//        analysisTbl.put(new Pair<State, Token>(states[0], t), g2);
//        analysisTbl.put(new Pair<State, Token>(states[0], f), g3);
// 
//        analysisTbl.put(new Pair<State, Token>(states[1], plus), s6);
//        analysisTbl.put(new Pair<State, Token>(states[1], end), acc);
//
//        analysisTbl.put(new Pair<State, Token>(states[2], plus), r2);
//        analysisTbl.put(new Pair<State, Token>(states[2], star), s7);
//        analysisTbl.put(new Pair<State, Token>(states[2], rp), r2);
//        analysisTbl.put(new Pair<State, Token>(states[2], end), r2);
//
//        analysisTbl.put(new Pair<State, Token>(states[3], plus), r4);
//        analysisTbl.put(new Pair<State, Token>(states[3], star), r4);
//        analysisTbl.put(new Pair<State, Token>(states[3], rp), r4);
//        analysisTbl.put(new Pair<State, Token>(states[3], end), r4);
//
//        analysisTbl.put(new Pair<State, Token>(states[4], id), s5);
//        analysisTbl.put(new Pair<State, Token>(states[4], lp), s4);
//        analysisTbl.put(new Pair<State, Token>(states[4], e), g8);
//        analysisTbl.put(new Pair<State, Token>(states[4], t), g2);
//        analysisTbl.put(new Pair<State, Token>(states[4], f), g3);
//
//        analysisTbl.put(new Pair<State, Token>(states[5], plus), r6);
//        analysisTbl.put(new Pair<State, Token>(states[5], star), r6);
//        analysisTbl.put(new Pair<State, Token>(states[5], rp), r6);
//        analysisTbl.put(new Pair<State, Token>(states[5], end), r6);
//
//        analysisTbl.put(new Pair<State, Token>(states[6], id), s5);
//        analysisTbl.put(new Pair<State, Token>(states[6], lp), s4);
//        analysisTbl.put(new Pair<State, Token>(states[6], t), g9);
//        analysisTbl.put(new Pair<State, Token>(states[6], f), g3);
//
//        analysisTbl.put(new Pair<State, Token>(states[7], id), s5);
//        analysisTbl.put(new Pair<State, Token>(states[7], lp), s4);
//        analysisTbl.put(new Pair<State, Token>(states[7], f), g10);
//
//        analysisTbl.put(new Pair<State, Token>(states[8], plus), s6);
//        analysisTbl.put(new Pair<State, Token>(states[8], rp), s11);
//
//        analysisTbl.put(new Pair<State, Token>(states[9], plus), r1);
//        analysisTbl.put(new Pair<State, Token>(states[9], star), s7);
//        analysisTbl.put(new Pair<State, Token>(states[9], rp), r1);
//        analysisTbl.put(new Pair<State, Token>(states[9], end), r1);
//
//        analysisTbl.put(new Pair<State, Token>(states[10], plus), r3);
//        analysisTbl.put(new Pair<State, Token>(states[10], star), r3);
//        analysisTbl.put(new Pair<State, Token>(states[10], rp), r3);
//        analysisTbl.put(new Pair<State, Token>(states[10], end), r3);
//
//        analysisTbl.put(new Pair<State, Token>(states[11], plus), r5);
//        analysisTbl.put(new Pair<State, Token>(states[11], star), r5);
//        analysisTbl.put(new Pair<State, Token>(states[11], rp), r5);
//        analysisTbl.put(new Pair<State, Token>(states[11], end), r5);
//
//        List<Token> input = new LinkedList<Token>();
//        input.add(id);
//        input.add(plus);
//        input.add(id);
//        input.add(star);
//        input.add(id);
//        input.add(end);
//
//        Tree<Token> ast = Algorithm.parse(analysisTbl, states[0], input);
//        ast.getRootElement().dump();
//    }
//
//    /* @IN: (id+id)*id */
//    public static void test1_2() {
//        System.out.println("Input: (id+id)*id");
//        State[] states = new State[12];
//        for(int i = 0; i < 12; i++) states[i] = new State(i);
//
//        Token id = new Token("TERMINAL", "id");
//        Token plus = new Token("TERMINAL", "+");
//        Token star = new Token("TERMINAL", "*");
//        Token lp = new Token("TERMINAL", "(");
//        Token rp = new Token("TERMINAL", ")");
//        Token end = new Token("EOF", "$");
//        Token e = new Token("NON_TERMINAL", "E");
//        Token t = new Token("NON_TERMINAL", "T");
//        Token f = new Token("NON_TERMINAL", "F");
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
//        Action s4 = new Action(Action.ACTION_SHIFT, states[4]);
//        Action s5 = new Action(Action.ACTION_SHIFT, states[5]);
//        Action s6 = new Action(Action.ACTION_SHIFT, states[6]);
//        Action s7 = new Action(Action.ACTION_SHIFT, states[7]);
//        Action s11 = new Action(Action.ACTION_SHIFT, states[11]);
//
//        Action r1 = new Action(Action.ACTION_REDUCE, rule1);
//        Action r2 = new Action(Action.ACTION_REDUCE, rule2);
//        Action r3 = new Action(Action.ACTION_REDUCE, rule3);
//        Action r4 = new Action(Action.ACTION_REDUCE, rule4);
//        Action r5 = new Action(Action.ACTION_REDUCE, rule5);
//        Action r6 = new Action(Action.ACTION_REDUCE, rule6);
//
//        Action g1 = new Action(Action.ACTION_GOTO, states[1]);
//        Action g2 = new Action(Action.ACTION_GOTO, states[2]);
//        Action g3 = new Action(Action.ACTION_GOTO, states[3]);
//        Action g8 = new Action(Action.ACTION_GOTO, states[8]);
//        Action g9 = new Action(Action.ACTION_GOTO, states[9]);
//        Action g10 = new Action(Action.ACTION_GOTO, states[10]);
//
//        Action acc = new Action(Action.ACTION_ACCT);
//
////      HashMap<Pair<State, Token>, Action> analysisTbl =
////          new HashMap<Pair<State, Token>, Action>();
//        AnalysisTable analysisTbl = new AnalysisTable();
//
//        analysisTbl.put(new Pair<State, Token>(states[0], id), s5);
//        analysisTbl.put(new Pair<State, Token>(states[0], lp), s4);
//        analysisTbl.put(new Pair<State, Token>(states[0], e), g1);
//        analysisTbl.put(new Pair<State, Token>(states[0], t), g2);
//        analysisTbl.put(new Pair<State, Token>(states[0], f), g3);
// 
//        analysisTbl.put(new Pair<State, Token>(states[1], plus), s6);
//        analysisTbl.put(new Pair<State, Token>(states[1], end), acc);
//
//        analysisTbl.put(new Pair<State, Token>(states[2], plus), r2);
//        analysisTbl.put(new Pair<State, Token>(states[2], star), s7);
//        analysisTbl.put(new Pair<State, Token>(states[2], rp), r2);
//        analysisTbl.put(new Pair<State, Token>(states[2], end), r2);
//
//        analysisTbl.put(new Pair<State, Token>(states[3], plus), r4);
//        analysisTbl.put(new Pair<State, Token>(states[3], star), r4);
//        analysisTbl.put(new Pair<State, Token>(states[3], rp), r4);
//        analysisTbl.put(new Pair<State, Token>(states[3], end), r4);
//
//        analysisTbl.put(new Pair<State, Token>(states[4], id), s5);
//        analysisTbl.put(new Pair<State, Token>(states[4], lp), s4);
//        analysisTbl.put(new Pair<State, Token>(states[4], e), g8);
//        analysisTbl.put(new Pair<State, Token>(states[4], t), g2);
//        analysisTbl.put(new Pair<State, Token>(states[4], f), g3);
//
//        analysisTbl.put(new Pair<State, Token>(states[5], plus), r6);
//        analysisTbl.put(new Pair<State, Token>(states[5], star), r6);
//        analysisTbl.put(new Pair<State, Token>(states[5], rp), r6);
//        analysisTbl.put(new Pair<State, Token>(states[5], end), r6);
//
//        analysisTbl.put(new Pair<State, Token>(states[6], id), s5);
//        analysisTbl.put(new Pair<State, Token>(states[6], lp), s4);
//        analysisTbl.put(new Pair<State, Token>(states[6], t), g9);
//        analysisTbl.put(new Pair<State, Token>(states[6], f), g3);
//
//        analysisTbl.put(new Pair<State, Token>(states[7], id), s5);
//        analysisTbl.put(new Pair<State, Token>(states[7], lp), s4);
//        analysisTbl.put(new Pair<State, Token>(states[7], f), g10);
//
//        analysisTbl.put(new Pair<State, Token>(states[8], plus), s6);
//        analysisTbl.put(new Pair<State, Token>(states[8], rp), s11);
//
//        analysisTbl.put(new Pair<State, Token>(states[9], plus), r1);
//        analysisTbl.put(new Pair<State, Token>(states[9], star), s7);
//        analysisTbl.put(new Pair<State, Token>(states[9], rp), r1);
//        analysisTbl.put(new Pair<State, Token>(states[9], end), r1);
//
//        analysisTbl.put(new Pair<State, Token>(states[10], plus), r3);
//        analysisTbl.put(new Pair<State, Token>(states[10], star), r3);
//        analysisTbl.put(new Pair<State, Token>(states[10], rp), r3);
//        analysisTbl.put(new Pair<State, Token>(states[10], end), r3);
//
//        analysisTbl.put(new Pair<State, Token>(states[11], plus), r5);
//        analysisTbl.put(new Pair<State, Token>(states[11], star), r5);
//        analysisTbl.put(new Pair<State, Token>(states[11], rp), r5);
//        analysisTbl.put(new Pair<State, Token>(states[11], end), r5);
//
//        List<Token> input = new LinkedList<Token>();
//        input.add(lp);
//        input.add(id);
//        input.add(plus);
//        input.add(id);
//        input.add(rp);
//        input.add(star);
//        input.add(id);
//        input.add(end);
//
//        Tree<Token> ast = Algorithm.parse(analysisTbl, states[0], input);
//        ast.getRootElement().dump();
//    }
//
//    public static void main(String[] args) {
//        test1();
//        test1_1();
//        test1_2();
//    }
}

