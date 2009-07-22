package mini.java.regex;

import mini.java.fa.NFAState;
import mini.java.lex.IMatcher;
import mini.java.lex.CharLiteralMatcher;
import mini.java.lex.CharRangeMatcher;
import mini.java.lex.Tokenizer;
import mini.java.syntax.NonTerminal;
import mini.java.syntax.Rule;
import mini.java.syntax.RuleSet;
import mini.java.syntax.Symbol;
import mini.java.syntax.Terminal;
import mini.java.syntax.Rule.IRuleHandler;

public class RegexCompiler {
    // terminals
    public static final String  QM        = "qm"; // question marker
    public static final String  RB        = "rb"; // right bracket
    public static final String  LB        = "lb";
    public static final String  RP        = "rp";
    public static final String  LP        = "lp";
    public static final String  HYPHEN    = "hyphen";
    public static final String  BAR       = "bar";
    public static final String  STAR      = "star";    
//    public static final String  ALPHA     = "alpha";
    public static final String LOWER    = "lower";
    public static final String UPPER    = "upper";
    
    public static final String  NUM       = "num";
    public static final String  DOT       = "dot";    
    public static final String  CH        = "ch"; // any supported characters    
    
    // non-terminals
    public static final String       BAR_EXPR        = "BarExpr", STAR_EXPR = "StarExpr", QM_EXPR = "QMExpr", RANGE = "Range",
            CLASS_EXPR = "ClassExpr", SEQ_EXPR = "SeqExpr", ATOM = "Atom";
    
    
    public static final IRuleHandler DUMMY_HANDLER = new IRuleHandler() {

        @Override
        public void handle(Symbol sym_, Object ctx_) {
            NonTerminal sym = (NonTerminal)sym_;
            if (sym.getChildren().size() != 1) {
                throw new UnsupportedOperationException(
                        "DummyHandler cannot be used for NonTerminals with more than one child.");
            }
            
            Symbol child = sym.getChildren().get(0);
            if (child instanceof NonTerminal)
            {
                // pass the context directly to the child symbol
                ((NonTerminal)child).execute(ctx_);
            }
            else
            {
                
            }
            
        }
        
    };
    
    public static final IRuleHandler LITERAL_HANDLER = new IRuleHandler() {

        @Override
        public void handle(Symbol sym_, Object ctx_) {
            Symbol first = ((NonTerminal)sym_).first();
            NFAState[] st = (NFAState[])ctx_;
            
            st[0].addTransition(st[1], ((Terminal)first).getData());
        }
        
    };
    
    public static final IRuleHandler RANGE_HANDLER = new IRuleHandler() {

        @Override
        public void handle(Symbol sym_, Object ctx_) {
            NFAState[] st = (NFAState[])ctx_;
            NonTerminal sym = (NonTerminal)sym_;
            char from = ((String)((Terminal)sym.first()).getData()).charAt(0);
            char to = ((String)((Terminal)sym.third()).getData()).charAt(0);
            {
                if (from > to) {
                    char t = to; to = from; from = t;
                }
            }
            // XXX - the characters we recognize
            for (char c=from; c <= to; ++c) {
                st[0].addTransition(st[1],
                    new Character(c).toString()); // everything is a string
            }
        }
        
    };
    
    
    
    
    public static final RuleSet RULE_SET;
    
    static
    {
        RULE_SET = new RuleSet();
        RULE_SET.addRule(new Rule().left(RuleSet.START).right(BAR_EXPR).addHandler(DUMMY_HANDLER));
        // BarExpr has the lowest precedence
        RULE_SET.addRule(
                new Rule().left(BAR_EXPR).right(BAR_EXPR, BAR, BAR_EXPR)
                    .addHandler(new IRuleHandler() {

                        @Override
                        public void handle(Symbol sym_, Object ctx_) {
                            NonTerminal self = (NonTerminal)sym_;
                            ((NonTerminal)self.first()).execute(ctx_);
                            ((NonTerminal)self.third()).execute(ctx_);
                        }
                    
                    }));
        RULE_SET.addRule(new Rule().left(BAR_EXPR).right(SEQ_EXPR).addHandler(DUMMY_HANDLER)); // ...then the SeqExpr
        RULE_SET.addRule(new Rule().left(SEQ_EXPR).right(SEQ_EXPR, SEQ_EXPR).addHandler(new IRuleHandler() {

            @Override
            public void handle(Symbol sym_, Object ctx_) {
                NFAState[] st = (NFAState[])ctx_;
                
                NFAState middle = new NFAState();
                NFAState[] first = new NFAState[] {st[0], middle},
                    second = new NFAState[] {middle, st[1]};
                NonTerminal self = (NonTerminal) sym_;
                ((NonTerminal)self.first()).execute(first);
                ((NonTerminal)self.second()).execute(second);
            }

        }));
        
        RULE_SET.addRule(new Rule().left(ATOM).right(LB, CLASS_EXPR, RB).addHandler(new IRuleHandler() {

            @Override
            public void handle(Symbol sym_, Object ctx_) {
                Symbol second = ((NonTerminal)sym_).second();
                ((NonTerminal) second).execute(ctx_);
            }
            
        }));
        
        
        RULE_SET.addRule(new Rule().left(ATOM).right(LP, BAR_EXPR, RP).addHandler(new IRuleHandler() {

            @Override
            public void handle(Symbol sym_, Object ctx_) {
                Symbol second = ((NonTerminal)sym_).second();
                ((NonTerminal) second).execute(ctx_);
            }
            
        }));
        
        RULE_SET.addRule(new Rule().left(ATOM).right(DOT).addHandler(new IRuleHandler() {

            @Override
            public void handle(Symbol sym_, Object ctx_) {
                NFAState[] st = (NFAState[])ctx_;
                
                // XXX - the characters we recognize
                for (char c=0; c<128; ++c) {
                    st[0].addTransition(st[1],
                        new Character(c).toString()); // everything is a string
                }
            }
            
        }));
        RULE_SET.addRule(new Rule().left(ATOM).right(LOWER).addHandler(LITERAL_HANDLER));
        RULE_SET.addRule(new Rule().left(ATOM).right(UPPER).addHandler(LITERAL_HANDLER));
        RULE_SET.addRule(new Rule().left(ATOM).right(NUM).addHandler(LITERAL_HANDLER));
        RULE_SET.addRule(new Rule().left(ATOM).right(CH).addHandler(LITERAL_HANDLER));
        

        RULE_SET.addRule(new Rule().left(SEQ_EXPR).right(STAR_EXPR).addHandler(DUMMY_HANDLER));
        RULE_SET.addRule(new Rule().left(SEQ_EXPR).right(QM_EXPR).addHandler(DUMMY_HANDLER));
        RULE_SET.addRule(new Rule().left(SEQ_EXPR).right(ATOM).addHandler(DUMMY_HANDLER));
        
        RULE_SET.addRule(new Rule().left(STAR_EXPR).right(ATOM, STAR).addHandler(new IRuleHandler() {

            @Override
            public void handle(Symbol sym_, Object ctx_) {
                NFAState[] st = (NFAState[])ctx_,
                    child = new NFAState[] {st[0], st[0]};
                Symbol first = ((NonTerminal)sym_).first();
                ((NonTerminal)first).execute(child);
                st[0].addTransition(st[1]);
            }
            
        }));
        RULE_SET.addRule(new Rule().left(QM_EXPR).right(ATOM, QM).addHandler(new IRuleHandler() {

            @Override
            public void handle(Symbol sym_, Object ctx_) {
                NFAState[] st = (NFAState[])ctx_;
                Symbol first = ((NonTerminal)sym_).first();
                ((NonTerminal)first).execute(st);
                st[0].addTransition(st[1]);
            }
            
        }));

        
        RULE_SET.addRule(new Rule().left(RANGE).right(NUM, HYPHEN, NUM).addHandler(new IRuleHandler() {

            @Override
            public void handle(Symbol sym_, Object ctx_) {
                NFAState[] st = (NFAState[])ctx_;
                NonTerminal sym = (NonTerminal)sym_;
                int from = Integer.parseInt((String)((Terminal)sym.first()).getData());
                int to = Integer.parseInt((String)((Terminal)sym.third()).getData());
                {
                    if (from > to) {
                        int t = to; to = from; from = t;
                    }
                }
                // XXX - the characters we recognize
                for (int i=from; i <= to; ++i) {
                    char c = Character.forDigit(i, 10);
                    st[0].addTransition(st[1],
                        new Character(c).toString()); // everything is a string
                }
            }

        }));
        RULE_SET.addRule(new Rule().left(RANGE).right(LOWER, HYPHEN, LOWER).addHandler(RANGE_HANDLER));
        RULE_SET.addRule(new Rule().left(RANGE).right(UPPER, HYPHEN, UPPER).addHandler(RANGE_HANDLER));
        
        RULE_SET.addRule(new Rule().left(CLASS_EXPR).right(RANGE).addHandler(DUMMY_HANDLER));
        
        for (String s : new String[] {LOWER, UPPER, NUM, CH, STAR, QM, BAR, LP, RP, DOT}) {
            RULE_SET.addRule(new Rule().left(CLASS_EXPR).right(s).addHandler(LITERAL_HANDLER));
        }
        
        // XXX - to avoid ambiguity, we prohibit HYPHEN in the class expr
        RULE_SET.addRule(new Rule().left(CLASS_EXPR).right(CLASS_EXPR, CLASS_EXPR).addHandler(new IRuleHandler() {

            @Override
            public void handle(Symbol sym_, Object ctx_) {
                NonTerminal self = (NonTerminal) sym_;

                ((NonTerminal)self.first()).execute(ctx_);
                ((NonTerminal)self.second()).execute(ctx_);
            }
            
        }));

    }
    
    
    
    // singleton
    public static final Tokenizer TOKENIZER;
    
    static
    {

        
        TOKENIZER = new Tokenizer();
        TOKENIZER.addMatcher(new CharRangeMatcher(NUM, '0', '9'));        
        TOKENIZER.addMatcher(new CharRangeMatcher(LOWER, 'a', 'z'));
        TOKENIZER.addMatcher(new CharRangeMatcher(UPPER, 'A', 'Z'));
        
        TOKENIZER.addMatcher(new CharLiteralMatcher(DOT, '.'));
        TOKENIZER.addMatcher(new CharLiteralMatcher(STAR, '*'));
        TOKENIZER.addMatcher(new CharLiteralMatcher(BAR, '|'));
        TOKENIZER.addMatcher(new CharLiteralMatcher(HYPHEN, '-'));
        TOKENIZER.addMatcher(new CharLiteralMatcher(LP, '('));
        TOKENIZER.addMatcher(new CharLiteralMatcher(RP, ')'));
        TOKENIZER.addMatcher(new CharLiteralMatcher(LB, '['));
        TOKENIZER.addMatcher(new CharLiteralMatcher(RB, ']'));
        TOKENIZER.addMatcher(new CharLiteralMatcher(QM, '?'));
        
        // the lowest precedence: CH
        TOKENIZER.addMatcher(new IMatcher() {
            public String match(String input_) {
                if (input_.length() <= 0) {
                    return null;
                }
                            
                if (input_.length() > 1
                        && input_.charAt(0) == '\\')
                {
                    return input_.substring(0, 2);
                }
                else
                {
                    return "" + input_.charAt(0);
                }
            }
            public String getType() {
                return CH;
            }
        });
    }
}