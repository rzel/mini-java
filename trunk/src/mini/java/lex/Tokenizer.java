package mini.java.lex;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import mini.java.syntax.Terminal;

public final class Tokenizer implements ITokenizer {
    // fields
    private final List<IMatcher> _matchers;
    
    public Tokenizer() {
        _matchers = new LinkedList<IMatcher>();
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
        for (IMatcher matcher : getMatchers()) {
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

//    @Override
    public void addMatcher(IMatcher matcher_) {
        _matchers.add(matcher_);
    }

//    @Override
    public IMatcher getMatcher(String type_) {
        for (IMatcher matcher : _matchers) {
            if (matcher.getType().equals(type_)) {
                return matcher;
            }
        }
        return null;
    }

//    @Override
    public IMatcher[] getMatchers() {
        return _matchers.toArray(new IMatcher[0]);
    }

//    @Override
    public String[] getTokenTypes() {
        Set<String> types = new HashSet<String>();
        for (IMatcher matcher : _matchers) {
            types.add(matcher.getType());
        }
        return types.toArray(new String[0]);
    }

}
