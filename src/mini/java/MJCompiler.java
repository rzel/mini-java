package mini.java;

import mini.java.lex.RegexMatcher;
import mini.java.lex.Tokenizer;

public class MJCompiler {

    public final static Tokenizer TOKENIZER;
    
    // terminals
    public final static String SEMICOLON = "semicolon", PRINT = "print", CLASS = "class", PUBLIC = "public",
        STATIC = "static", VOID = "void", STRING = "string",  LP = "lp", RP = "rp", LEFT_BRACKET = "left_bracket",
        RIGHT_BRACKET = "right_bracket", LEFT_BRACE = "left_brace", RIGHT_BRACE = "right_brace", INTEGER = "integer",
        ID = "id", WHITE = "white";
    
    static {
        TOKENIZER = new Tokenizer();
        TOKENIZER.addMatcher(new RegexMatcher(SEMICOLON, ";"));
        TOKENIZER.addMatcher(new RegexMatcher(PRINT, "System\\.out\\.println"));
        TOKENIZER.addMatcher(new RegexMatcher(CLASS, "class"));
        TOKENIZER.addMatcher(new RegexMatcher(STATIC, "static"));
        TOKENIZER.addMatcher(new RegexMatcher(PUBLIC, "public"));
        TOKENIZER.addMatcher(new RegexMatcher(VOID, "void"));
        TOKENIZER.addMatcher(new RegexMatcher(STRING, "String"));
        TOKENIZER.addMatcher(new RegexMatcher(LP, "("));
        TOKENIZER.addMatcher(new RegexMatcher(RP, ")"));
        TOKENIZER.addMatcher(new RegexMatcher(LEFT_BRACKET, "\\["));
        TOKENIZER.addMatcher(new RegexMatcher(RIGHT_BRACKET, "\\]"));
        TOKENIZER.addMatcher(new RegexMatcher(LEFT_BRACE, "\\{"));
        TOKENIZER.addMatcher(new RegexMatcher(RIGHT_BRACE, "\\}"));
        TOKENIZER.addMatcher(new RegexMatcher(INTEGER, "-?[0-9][0-9]*")); // 00?
        TOKENIZER.addMatcher(new RegexMatcher(ID, "[A-Za-z_][0-9A-Za-z_]*"));
        TOKENIZER.addMatcher(new RegexMatcher(WHITE, "[ \t\n\r][ \t\n\r]*"));
            
    }
}
