package mini.java.lex;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CharTokenizerConfig implements ITokenizerConfig {
    // terminals
    public static final String  QM        = "qm"; // question marker
    public static final String  RB        = "rb"; // right bracket
    public static final String  LB        = "lb";
    public static final String  RP        = "rp";
    public static final String  LP        = "lp";
    public static final String  HYPHEN    = "hyphen";
    public static final String  BAR       = "bar";
    public static final String  STAR      = "star";    
    public static final String  ALPHA     = "alpha";
    public static final String  NUM       = "num";
    public static final String  DOT       = "dot";    
    public static final String  CH        = "ch"; // any supported characters    
    
    // singleton
    public static final CharTokenizerConfig _instance = new CharTokenizerConfig();    
    private final List<IMatcher> _matchers;
    
    // private ctor.
    private CharTokenizerConfig() {        
        Map<String, String> tokenSpecs = new HashMap<String, String>();
        tokenSpecs.put(NUM, "\\d");
        tokenSpecs.put(ALPHA, "\\w");
        tokenSpecs.put(DOT, ".");
        tokenSpecs.put(STAR, "*");
        tokenSpecs.put(BAR, "|");
        tokenSpecs.put(HYPHEN, "-");
        tokenSpecs.put(LP, "(");
        tokenSpecs.put(RP, ")");
        tokenSpecs.put(LB, "[");
        tokenSpecs.put(RB, "]");
        tokenSpecs.put(QM, "?");
        
        _matchers = new LinkedList<IMatcher>();
        
        for (Map.Entry<String, String> tokenSpec : tokenSpecs.entrySet()) {
            final String type = tokenSpec.getKey();
            final String spec = tokenSpec.getValue();
            
            _matchers.add(new IMatcher() {
                public String match(String input_) {
                    return input_.startsWith(spec) ? spec : null;
                }
                public String getType() {
                    return type;
                }
            });
        }
        
        // the lowest precedence: CH
        _matchers.add(new IMatcher() {
            public String match(String input_) {
                if (input_.length() <= 0) {
                    return null;
                }
                
                char ch = '\0';                
                if (input_.length() > 1
                        && input_.charAt(0) == '\\')
                {
                    // handle escaped characters
                    ch = input_.charAt(1);
                } else {
                    ch = input_.charAt(0);
                }
                
                // TODO - should return only supported characters
                return "" + ch;
            }
            public String getType() {
                return CH;
            }
        });
    }

    @Override
    public IMatcher[] getMatchers() {
        return _matchers.toArray(new IMatcher[0]);
    }

    @Override
    public String[] getTokenTypes() {
        Set<String> types = new HashSet<String>();
        for (IMatcher matcher : _matchers) {
            types.add(matcher.getType());
        }
        return types.toArray(new String[0]);
    }

    @Override
    public IMatcher getMatcher(String type_) {
        for (IMatcher matcher : _matchers) {
            if (matcher.getType().equals(type_)) {
                return matcher;
            }
        }
        return null;
    }

}
