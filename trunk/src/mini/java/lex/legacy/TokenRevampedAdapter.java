package mini.java.lex.legacy;


public class TokenRevampedAdapter extends Token {
    private TokenRevamped token;

    public TokenRevampedAdapter(TokenRevamped token) {
        super(token.getType().toString(), token.getData());
        this.token = token;
    }
}
