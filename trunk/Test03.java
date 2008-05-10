import lex.LexConfig;
import lex.TextLexConfig;
import lex.TokenRevamped;
import lex.TokenType;
import lex.TokenizerIF;
import lex.TokenizerAdapter;
import lex.legacy.Tokenizer;
import lex.legacy.RegexCompiler;
import fa.legacy.DFA;
import fa.legacy.State;
import syntax.Symbol;
import syntax.SymbolType;
import syntax.SyntaxConfig;
import syntax.NonTerminal;
import syntax.legacy.Node;
import syntax.legacy.Pair;
import syntax.legacy.Rule;
import syntax.legacy.Algorithm;
import syntax.legacy.AnalysisTable;

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
        rule.addRhsToken(new NonTerminal("GOAL"));
        rules.add(0, rule);
        AnalysisTable drivingManual = Algorithm.closureSet(rules);
        //System.out.println(drivingManual);
        //drivingManual.dump();

        //getSampleTokens();
        //for (TokenRevamped token : getSampleTokens()) {
        //    System.out.println(token);
        //}
        /*Symbol */Node<Pair<SymbolType, TokenRevamped>> root =
            Algorithm.parse(drivingManual, new State(0), getSampleTokens());
        root.dump();
        //
    }

    public static List<TokenRevamped> getSampleTokens() throws Exception {
        Tokenizer tokenizer = new Tokenizer();
        LexConfig lexConfig = TextLexConfig.getInstance();
        for (TokenType type : lexConfig.getTokenTypes()) {
            String tokenSpec = lexConfig.getTokenSpec(type);
            //try {
                DFA dfa = RegexCompiler.compile(tokenSpec).toDFA().toDFA0();
                tokenizer.addDFA(dfa, type.toString());
            //} catch (Exception e) {
            //    throw new RuntimeException(e);
            //}
        }
        TokenizerIF newTokenizer = new TokenizerAdapter(tokenizer);
        List<TokenRevamped> tokens = new LinkedList<TokenRevamped>();
        // I can't believe this! WHY I MUST ADD AN EXTRA CHAR TO
        // MAKE THIS TOKENIZER WORK?
        for (TokenRevamped token : newTokenizer.tokenize("class Main { public static void main(String[] args) { System.out.println(123);}}END")) {
            if (!token.getType().equals(new TokenType("WHITE"))) {
                tokens.add(token);
            }
        }
        //for (TokenRevamped token : tokens) {
        //    System.out.println(token);
        //}
        return tokens;
    }
}
