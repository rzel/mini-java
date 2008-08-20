package mini.java.lex;

import mini.java.lex.legacy.Token;

public class TokenRevampedAdapter extends Token {
    private TokenRevamped token;

    public TokenRevampedAdapter(TokenRevamped token) {
        super(token.getType().toString(), token.getData());
        this.token = token;
    }
}
