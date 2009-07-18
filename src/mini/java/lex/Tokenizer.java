package mini.java.lex;

import java.util.LinkedList;
import java.util.List;

import mini.java.syntax.Terminal;

public final class Tokenizer implements ITokenizer {
    // fields
    private ITokenizerConfig _conf;
    
    public Tokenizer(ITokenizerConfig conf_) {
        _conf = conf_;
    }

    @Override
    public Terminal[] tokenize(String input_) {
        List<Terminal> ret = new LinkedList<Terminal>();
        Terminal token = null;
        
        while ((token = getToken(input_)) != null) {
            ret.add(token);
            
            input_ = input_.substring(
                    ((String)token.getData()).length());
        }
        // finished
        if (input_.length() > 0) {
            throw new IllegalArgumentException("Invalid token found at: " + input_);
        }
        return ret.toArray(new Terminal[0]);
    }
    
    /**
     * Returns the first token from the given input.
     */
    public Terminal getToken(String input_) {
        String longest = "";
        String type = null;
        for (IMatcher matcher : _conf.getMatchers()) {
            String token = matcher.match(input_);
            if (token != null
                    && token.length() > longest.length())
            {
                longest = token;
                type = matcher.getType();
            }
        }
        
        return (type != null)
            ? new Terminal(type, longest) : null;
    }

}
