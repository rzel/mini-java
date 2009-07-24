package mini.java.test;
import java.util.LinkedList;
import java.util.List;

import mini.java.fa.v2.DFA;
//import mini.java.lex.legacy.LexConfig;
import mini.java.lex.legacy.LexConfig;
import mini.java.lex.legacy.Tokenizer;
//import mini.java.lex.legacy.TokenizerAdapter;
//import mini.java.lex.legacy.TokenizerIF;
import mini.java.regex.legacy.RegexCompiler;
import mini.java.syntax.legacy.TokenRevamped;
import mini.java.syntax.legacy.TokenType;

public class Test04 {
//    public static void main(String[] args) throws Exception {
//        List<Rule> rules = new LinkedList<Rule>();
//        SyntaxConfig config = SyntaxConfig.getInstance();
//        for (NonTerminal type : config.getNonTerminals()) {
//            //System.out.println(type);
//            for (Rule rule : config.getRules(type)) {
//                //System.out.println(rule);
//                rules.add(rule);
//            }
//        }
//
//        // The first rule must be "START ::= Goal", should be fixed anyway
//        Rule rule = new Rule(new NonTerminal("START"));
//        rule.addRhsToken(new NonTerminal("Goal"));
//        rules.add(0, rule);
//        AnalysisTable drivingManual = Algorithm.closureSet(rules);
//        //System.out.println(drivingManual);
//        drivingManual.dump();
//
//        //getSampleTokens();
//        //for (TokenRevamped token : getSampleTokens()) {
//        //    System.out.println(token);
//        //}
//        /*Symbol */Node<Pair<SymbolType, TokenRevamped>> root =
//            Algorithm.parse(drivingManual, new State(0), getSampleTokens());
//        root.dump();
//        JFrame frame = new MiniViewer(TreeView.fromNodeToTreeNode(root));
//    }

//    public static List<TokenRevamped> getSampleTokens() throws Exception {
//        Tokenizer tokenizer = new Tokenizer();
//        LexConfig lexConfig = TextLexConfig.getInstance();
//        for (TokenType type : lexConfig.getTokenTypes()) {
//            String tokenSpec = lexConfig.getTokenSpec(type);
//            //try {
//                //DFA dfa = RegexCompiler.compile(tokenSpec).toDFA().toDFA0();
//                //tokenizer.addDFA(dfa, type.toString());
//            //} catch (Exception e) {
//            //    throw new RuntimeException(e);
//            //}
//        }
//        TokenizerIF newTokenizer = new TokenizerAdapter(tokenizer);
//        List<TokenRevamped> tokens = new LinkedList<TokenRevamped>();
//        // I can't believe this! WHY I MUST ADD AN EXTRA CHAR TO
//        // MAKE THIS TOKENIZER WORK?
//        for (TokenRevamped token : newTokenizer.tokenize("class Main { public static void main(String[] args) { System.out.println(123);}}END")) {
//            if (!token.getType().equals(new TokenType("WHITESPACE"))
//                && !token.getType().equals(new TokenType("COMMENT"))) {
//                tokens.add(token);
//            }
//        }
//        //for (TokenRevamped token : tokens) {
//        //    System.out.println(token);
//        //}
//        return tokens;
//    }
}

//class MiniViewer extends JFrame {
//    private TreeView treep = new TreeView();
//    public MiniViewer(DefaultMutableTreeNode node) {
//        treep.addTree(node);
//        treep.setBounds(0, 25, 200, 595);
//        this.add(treep);
//        this.setLayout(null);
//        this.setTitle("Mini Java");
//        this.setResizable(false);
//        this.setSize(1010,654);
//        this.setVisible(true);
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    }
//}
