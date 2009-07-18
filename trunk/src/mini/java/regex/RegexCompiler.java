package mini.java.regex;

import java.util.HashMap;
import java.util.Map;

import mini.java.lex.IMatcher;
import mini.java.lex.Tokenizer;
import mini.java.syntax.NonTerminal;
import mini.java.syntax.Rule;
import mini.java.syntax.RuleSet;
import mini.java.syntax.Symbol;
import mini.java.syntax.Terminal;
import mini.java.syntax.Rule.IContext;
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
    public static final String  ALPHA     = "alpha";
    public static final String  NUM       = "num";
    public static final String  DOT       = "dot";    
    public static final String  CH        = "ch"; // any supported characters    
    
    // non-terminals
//    public static final String      EXPR      = "Expr";
    public static final String      BAR_EXPR  = "BarExpr";    
    public static final String      STAR_EXPR = "StarExpr";
    public static final String      QM_EXPR   = "QMExpr";
//    public static final String      SET       = "Set";
    public static final String      RANGE     = "Range", /*CHAR_EXPR = "CharExpr",*/ CLASS_EXPR = "ClassExpr",
            /*P_EXPR = "PExpr",*/ SEQ_EXPR = "SeqExpr",/* Q_EXPR = "QExpr", */ATOM = "Atom";
    
    
    public static final IRuleHandler DUMMY_HANDLER = new IRuleHandler() {

        @Override
        public void handle(Symbol sym_, IContext ctx_) {
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
        public void handle(Symbol sym_, IContext ctx_) {
            Symbol first = ((NonTerminal)sym_).first();
            RegexContext ctx = ((RegexContext)ctx_);
            
            ctx.getHead().addTransition(ctx.getTail(), ((Terminal)first).getData());
        }
        
    };
    
    
    
    
    public static final RuleSet RULE_SET;
    
    static
    {
        RULE_SET = new RuleSet();
//        RULE_SET.addRule(new Rule().left(RuleSet.START).right(EXPR).addHandler(DUMMY_HANDLER));
        RULE_SET.addRule(new Rule().left(RuleSet.START).right(BAR_EXPR).addHandler(DUMMY_HANDLER));
        // BarExpr has the lowest precedence
//        RULE_SET.addRule(new Rule().left(EXPR).right(BAR_EXPR).addHandler(DUMMY_HANDLER));
        RULE_SET.addRule(
                new Rule().left(BAR_EXPR).right(BAR_EXPR, BAR, BAR_EXPR)
                    .addHandler(new IRuleHandler() {

                        @Override
                        public void handle(Symbol sym_, IContext ctx_) {
                            RegexContext left = new RegexContext(),
                                right = new RegexContext(),
                                ctx = (RegexContext)ctx_;
                            NonTerminal self = (NonTerminal)sym_,
                                leftChild = (NonTerminal)self.getChildren().get(0),
                                rightChild = (NonTerminal)self.getChildren().get(2);
                            
                            leftChild.execute(left);
                            rightChild.execute(right);
                            
                            
                            ctx.getHead().addTransition(left.getHead());
                            ctx.getHead().addTransition(right.getHead());
                            
                            left.getTail().addTransition(ctx.getTail());
                            right.getTail().addTransition(ctx.getTail());
                        }
                    
                    }));
        RULE_SET.addRule(new Rule().left(BAR_EXPR).right(SEQ_EXPR).addHandler(DUMMY_HANDLER)); // ...then the SeqExpr
        RULE_SET.addRule(new Rule().left(SEQ_EXPR).right(SEQ_EXPR, SEQ_EXPR).addHandler(new IRuleHandler() {

            @Override
            public void handle(Symbol sym_, IContext ctx_) {
                RegexContext first = new RegexContext(), second = new RegexContext(), ctx = (RegexContext) ctx_;
                NonTerminal self = (NonTerminal) sym_;
                ((NonTerminal) self.getChildren().get(0)).execute(first);
                ((NonTerminal) self.getChildren().get(1)).execute(second);

                // Head --> fist --> second --> Tail
                ctx.linkHead(first.getHead());
                first.linkTail(second.getHead());
                second.linkTail(ctx.getTail());
            }

        }));
//        RULE_SET.addRule(new Rule().left(SEQ_EXPR).right(Q_EXPR).addHandler(DUMMY_HANDLER));
        
// RULE_SET.addRule(new Rule().left(EXPR).right(Q_EXPR));
        
        RULE_SET.addRule(new Rule().left(ATOM).right(LB, CLASS_EXPR, RB).addHandler(new IRuleHandler() {

            @Override
            public void handle(Symbol sym_, IContext ctx_) {
                DUMMY_HANDLER.handle(((NonTerminal)sym_).second(), ctx_);
            }
            
        }));
        
        
        RULE_SET.addRule(new Rule().left(ATOM).right(LP, BAR_EXPR, RP).addHandler(new IRuleHandler() {

            @Override
            public void handle(Symbol sym_, IContext ctx_) {
                DUMMY_HANDLER.handle(((NonTerminal)sym_).second(), ctx_);
            }
            
        }));
        
        RULE_SET.addRule(new Rule().left(ATOM).right(DOT).addHandler(new IRuleHandler() {

            @Override
            public void handle(Symbol sym_, IContext ctx_) {
                RegexContext ctx = ((RegexContext)ctx_);
                
                // XXX - the characters we recognize
                for (char c=0; c<128; ++c) {
                    ctx.getHead().addTransition(ctx.getTail(),
                        new Character(c).toString()); // everything is a string
                }
            }
            
        }));
        RULE_SET.addRule(new Rule().left(ATOM).right(ALPHA).addHandler(LITERAL_HANDLER));
        RULE_SET.addRule(new Rule().left(ATOM).right(NUM).addHandler(LITERAL_HANDLER));
        RULE_SET.addRule(new Rule().left(ATOM).right(CH).addHandler(LITERAL_HANDLER));
        

        // QExpr ::= StarExpr | QMExpr | ...
//        RULE_SET.addRule(new Rule().left(Q_EXPR).right(STAR_EXPR).addHandler(DUMMY_HANDLER));
//        RULE_SET.addRule(new Rule().left(Q_EXPR).right(QM_EXPR).addHandler(DUMMY_HANDLER));
//        RULE_SET.addRule(new Rule().left(Q_EXPR).right(ATOM).addHandler(DUMMY_HANDLER));
        RULE_SET.addRule(new Rule().left(SEQ_EXPR).right(STAR_EXPR).addHandler(DUMMY_HANDLER));
        RULE_SET.addRule(new Rule().left(SEQ_EXPR).right(QM_EXPR).addHandler(DUMMY_HANDLER));
        RULE_SET.addRule(new Rule().left(SEQ_EXPR).right(ATOM).addHandler(DUMMY_HANDLER));
        
        RULE_SET.addRule(new Rule().left(STAR_EXPR).right(ATOM, STAR).addHandler(new IRuleHandler() {

            @Override
            public void handle(Symbol sym_, IContext ctx_) {
                RegexContext child = new RegexContext(), ctx = ((RegexContext)ctx_);
                Symbol first = ((NonTerminal)sym_).first();
                ((NonTerminal)first).execute(ctx);
                
                ctx.linkHead(child.getHead());
                child.linkTail(ctx.getHead());
                ctx.linkHead(ctx.getTail());
            }
            
        }));
        RULE_SET.addRule(new Rule().left(QM_EXPR).right(ATOM, QM).addHandler(new IRuleHandler() {

            @Override
            public void handle(Symbol sym_, IContext ctx_) {
                RegexContext child = new RegexContext(), ctx = ((RegexContext)ctx_);
                Symbol first = ((NonTerminal)sym_).first();
                ((NonTerminal)first).execute(ctx);
                
                ctx.linkHead(child.getHead());
                child.linkTail(ctx.getTail());
                ctx.linkHead(ctx.getTail());                
            }
            
        }));
        

        // Char ::= (Alpha | Num | Dot | Ch);

//        RULE_SET.addRule(new Rule().left(CHAR_EXPR).right(DOT).addHandler(new IRuleHandler() {
//
//            @Override
//            public void handle(Symbol sym_, IContext ctx_) {
//                RegexContext ctx = ((RegexContext)ctx_);
//                
//                // XXX - the characters we recognize
//                for (char c=0; c<128; ++c) {
//                    ctx.getHead().addTransition(ctx.getTail(),
//                        new Character(c).toString()); // everything is a string
//                }
//            }
//            
//        }));
    

//        RULE_SET.addRule(new Rule().left(SEQ).right(SEQ, SEQ));
//        RULE_SET.addRule(new Rule().left(SEQ).right(CHAR));
        
        RULE_SET.addRule(new Rule().left(RANGE).right(NUM, HYPHEN, NUM).addHandler(new IRuleHandler() {

            @Override
            public void handle(Symbol sym_, IContext ctx_) {
                RegexContext ctx = ((RegexContext)ctx_);
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
                    ctx.getHead().addTransition(ctx.getTail(),
                        new Character(c).toString()); // everything is a string
                }
            }

        }));
        RULE_SET.addRule(new Rule().left(RANGE).right(ALPHA, HYPHEN, ALPHA).addHandler(new IRuleHandler() {

            @Override
            public void handle(Symbol sym_, IContext ctx_) {
                RegexContext ctx = ((RegexContext)ctx_);
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
                    ctx.getHead().addTransition(ctx.getTail(),
                        new Character(c).toString()); // everything is a string
                }
            }
            
        }));
        
        RULE_SET.addRule(new Rule().left(CLASS_EXPR).right(RANGE).addHandler(DUMMY_HANDLER));
        
        for (String s : new String[] {ALPHA, NUM, CH, STAR, QM, BAR, LP, RP, DOT}) {
            RULE_SET.addRule(new Rule().left(CLASS_EXPR).right(s).addHandler(LITERAL_HANDLER));
        }
//        RULE_SET.addRule(new Rule().left(CLASS_EXPR).right(ALPHA).addHandler(literal));
//        RULE_SET.addRule(new Rule().left(CLASS_EXPR).right(NUM).addHandler(literal));
//        RULE_SET.addRule(new Rule().left(CLASS_EXPR).right(CH).addHandler(literal));    
//        
////        RULE_SET.addRule(new Rule().left(CLASS_EXPR).right(CHAR_EXPR).addHandler(DUMMY_HANDLER));
//        RULE_SET.addRule(new Rule().left(CLASS_EXPR).right(STAR).addHandler(literal));
//        RULE_SET.addRule(new Rule().left(CLASS_EXPR).right(QM).addHandler(literal));
//        RULE_SET.addRule(new Rule().left(CLASS_EXPR).right(BAR).addHandler(literal));
//        RULE_SET.addRule(new Rule().left(CLASS_EXPR).right(LP).addHandler(literal));
//        RULE_SET.addRule(new Rule().left(CLASS_EXPR).right(RP).addHandler(literal));
//        RULE_SET.addRule(new Rule().left(CLASS_EXPR).right(DOT).addHandler(literal));
        // XXX - to avoid ambiguity, we prohibit HYPHEN in the class expr
//        RULE_SET.addRule(new Rule().left(CLASS_EXPR).right(HYPHEN).addHandler(literal));
        RULE_SET.addRule(new Rule().left(CLASS_EXPR).right(CLASS_EXPR, CLASS_EXPR).addHandler(new IRuleHandler() {

            @Override
            public void handle(Symbol sym_, IContext ctx_) {
                RegexContext left = new RegexContext(), right = new RegexContext(), ctx = (RegexContext) ctx_;
                NonTerminal self = (NonTerminal) sym_;

                ((NonTerminal)self.first()).execute(left);
                ((NonTerminal)self.second()).execute(right);

                // same as the BarExpr
                ctx.linkHead(left.getHead());
                ctx.linkHead(right.getHead());

                left.getTail().addTransition(ctx.getTail());
                right.getTail().addTransition(ctx.getTail());
            }
            
        }));

    }
    
    
    
    // singleton
    public static final Tokenizer TOKENIZER;
    
    static
    {
        Map<String, String> tokenSpecs = new HashMap<String, String>();
        tokenSpecs.put(DOT, ".");
        tokenSpecs.put(STAR, "*");
        tokenSpecs.put(BAR, "|");
        tokenSpecs.put(HYPHEN, "-");
        tokenSpecs.put(LP, "(");
        tokenSpecs.put(RP, ")");
        tokenSpecs.put(LB, "[");
        tokenSpecs.put(RB, "]");
        tokenSpecs.put(QM, "?");
        
        TOKENIZER = new Tokenizer();
        TOKENIZER.addMatcher(new IMatcher() {

            @Override
            public String getType() {
                return NUM;
            }

            @Override
            public String match(String input_) {
                if (input_.length() <= 0) {
                    return null;
                }
                
                char ch = input_.charAt(0);
                if (ch >= '0' && ch <= '9')
                {
                    return "" + ch;
                }
                else
                {
                    return null;
                }
            }
            
        });
        
        TOKENIZER.addMatcher(new IMatcher() {

            @Override
            public String getType() {
                return ALPHA;
            }

            @Override
            public String match(String input_) {
                if (input_.length() <= 0) {
                    return null;
                }
                
                char ch = input_.charAt(0);
                if ((ch >= 'A' && ch <= 'Z')
                        || (ch >= 'a' && ch <= 'z')
                        || (ch == '_'))
                {
                    return "" + ch;
                }
                else
                {
                    return null;
                }
            }
            
        });
        
        
        for (Map.Entry<String, String> tokenSpec : tokenSpecs.entrySet()) {
            final String type = tokenSpec.getKey();
            final String spec = tokenSpec.getValue();
            
            TOKENIZER.addMatcher(new IMatcher() {
                public String match(String input_) {
                    return input_.startsWith(spec) ? spec : null;
                }
                public String getType() {
                    return type;
                }
            });
        }
        
        // the lowest precedence: CH
        TOKENIZER.addMatcher(new IMatcher() {
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
}