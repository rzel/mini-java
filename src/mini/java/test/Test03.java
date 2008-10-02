package mini.java.test;
import mini.java.fa.State;
import mini.java.fa.legacy.DFA;
import mini.java.lex.legacy.LexConfig;
import mini.java.lex.legacy.TextLexConfig;
import mini.java.lex.legacy.TokenRevamped;
import mini.java.lex.legacy.TokenType;
import mini.java.lex.legacy.Tokenizer;
import mini.java.lex.legacy.TokenizerAdapter;
import mini.java.lex.legacy.TokenizerIF;
import mini.java.regex.legacy.RegexCompiler;
import mini.java.syntax.legacy.Algorithm;
import mini.java.syntax.legacy.AnalysisTable;
import mini.java.syntax.legacy.Node;
import mini.java.syntax.legacy.NonTerminal;
import mini.java.syntax.legacy.Pair;
import mini.java.syntax.legacy.Rule;
import mini.java.syntax.legacy.Symbol;
import mini.java.syntax.legacy.SymbolType;
import mini.java.syntax.legacy.SyntaxConfig;

import java.util.List;
import java.util.LinkedList;

public class Test03 {
    public static void main(String[] args) throws Exception {
        List<Rule> rules = new LinkedList<Rule>();
        SyntaxConfig config = SyntaxConfig.getInstance();
        for (NonTerminal type : config.getNonTerminals()) {
            //System.out.println(type);
            for (Rule rule : config.getRules(type)) {
                //System.out.println(rule);
                rules.add(rule);
            }
        }

        // The first rule must be "START ::= Goal", should be fixed anyway
        Rule rule = new Rule(new NonTerminal("START"));
        rule.addRhsToken(new NonTerminal("Goal"));
        rules.add(0, rule);
        AnalysisTable drivingManual = Algorithm.closureSet(rules);
        //System.out.println(drivingManual);
        drivingManual.dump();

        //getSampleTokens();
        //for (TokenRevamped token : getSampleTokens()) {
        //    System.out.println(token);
        //}
        ///*Symbol */Node<Pair<SymbolType, TokenRevamped>> root =
            //Algorithm.parse(drivingManual, new State(0), getSampleTokens());
        //root.dump();
        //TreeView.fromNodeToTreeNode(root);
    }

    public static List<TokenRevamped> getSampleTokens() throws Exception {
        Tokenizer tokenizer = new Tokenizer();
        LexConfig lexConfig = TextLexConfig.getInstance();
        for (TokenType type : lexConfig.getTokenTypes()) {
            String tokenSpec = lexConfig.getTokenSpec(type);
            //try {
                //DFA dfa = RegexCompiler.compile(tokenSpec).toDFA().toDFA0();
                //tokenizer.addDFA(dfa, type.toString());
            //} catch (Exception e) {
            //    throw new RuntimeException(e);
            //}
        }
        TokenizerIF newTokenizer = new TokenizerAdapter(tokenizer);
        List<TokenRevamped> tokens = new LinkedList<TokenRevamped>();
        // I can't believe this! WHY I MUST ADD AN EXTRA CHAR TO
        // MAKE THIS TOKENIZER WORK?
        for (TokenRevamped token : newTokenizer.tokenize("class Main { public static void main(String[] args) { System.out.println(123);}}END")) {
            if (!token.getType().equals(new TokenType("WHITESPACE"))
                && !token.getType().equals(new TokenType("COMMENT"))) {
                tokens.add(token);
            }
        }
        //for (TokenRevamped token : tokens) {
        //    System.out.println(token);
        //}
        return tokens;
    }
}
