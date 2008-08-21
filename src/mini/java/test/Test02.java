package mini.java.test;
import mini.java.fa.legacy.DFA;
import mini.java.fa.legacy.SimpleFA;
import mini.java.lex.LexConfig;
import mini.java.lex.TextLexConfig;
import mini.java.lex.TokenRevamped;
import mini.java.lex.TokenType;
import mini.java.lex.TokenizerAdapter;
import mini.java.lex.TokenizerIF;
import mini.java.lex.legacy.RegexCompiler;
import mini.java.lex.legacy.Tokenizer;

import java.util.List;

public class Test02 {
    public static void main(String[] args) throws Exception {
        Tokenizer tokenizer = new Tokenizer();
        LexConfig lexConfig = TextLexConfig.getInstance();
        for (TokenType type : lexConfig.getTokenTypes()) {
            String tokenSpec = lexConfig.getTokenSpec(type);
            //try {
                //DFA dfa = RegexCompiler.compile(tokenSpec).toDFA().toDFA0();
                SimpleFA fa = RegexCompiler.compile(tokenSpec).toDFA();
                //System.out.println("********");
                //fa.dump();
                //System.out.println("********");
                //DFA dfa = fa.toDFA0();
                
                //tokenizer.addDFA(dfa, type.toString());
            //} catch (Exception e) {
            //    throw new RuntimeException(e);
            //}
        }
        TokenizerIF newTokenizer = new TokenizerAdapter(tokenizer);
        List<TokenRevamped> tokens = newTokenizer.tokenize("class Main { public static void main(String[] args) { }} ");
        for (TokenRevamped token : tokens) {
            System.out.println(token);
        }
    }
}
