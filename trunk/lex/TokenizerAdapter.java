package lex;

import lex.legacy.Tokenizer;
import lex.legacy.Token;
import java.util.List;
import java.util.LinkedList;

public class TokenizerAdapter implements TokenizerIF {
    private Tokenizer tokenizer;

    public TokenizerAdapter(Tokenizer tokenizer) {
        //this.tokenizer = new Tokenizer();
//        LexConfig lexConfig = TextLexConfig.getInstance();
//        for (TokenType type : lexConfig.getTokenTypes()) {
//            String tokenSpec = lexConfig.getTokenSpec(type);
//            try {
//                DFA dfa = RegexCompiler.compile(tokenSpec).toDFA().toDFA0();
//                this.tokenizer.addDFA(dfa, type.toString());
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
        this.tokenizer = tokenizer;
    }

    public List<TokenRevamped> tokenize(String src) {
        tokenizer.analyze(src);

        List<TokenRevamped> tokens = new LinkedList<TokenRevamped>();
        for (Token token : tokenizer.getTokens()) {
            TokenRevamped newToken = new TokenRevamped();
            newToken.setType(new TokenType(token.getType()));
            newToken.setData(token.getText());
            tokens.add(newToken);
        }
        return tokens;
    }
}
