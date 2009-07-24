package mini.java;

import mini.java.lex.IMatcher;
import mini.java.lex.RegexMatcher;
import mini.java.lex.Tokenizer;

public class MJCompiler {

    public final static Tokenizer TOKENIZER;
    
    // terminals
    public final static String SEMICOLON = "semicolon", PRINT = "print", CLASS = "class", PUBLIC = "public",
        STATIC = "static", VOID = "void", STRING = "string",  LP = "lp", RP = "rp", LEFT_BRACKET = "left_bracket",
        RIGHT_BRACKET = "right_bracket", LEFT_BRACE = "left_brace", RIGHT_BRACE = "right_brace", INTEGER = "integer",
        ID = "id", WHITESPACE = "whitespace", COMMA = "comma", PLUS = "plus", MINUS = "minus", AND = "and", STAR = "star",
        DOT = "dot", BANG = "bang", EQUAL = "equal", TRUE = "true", FALSE = "false", NEW = "new", THIS = "this",
        EXTENDS = "extends", RETURN = "return", INT = "int", BOOLEAN = "boolean", IF = "if", ELSE = "else", WHILE = "while",
        COMMENT = "comment";
    
    static {
        TOKENIZER = new Tokenizer();
        IMatcher[] matchers = new RegexMatcher[] {
                new RegexMatcher(SEMICOLON, ";"),
                new RegexMatcher(COMMA, ","),
                new RegexMatcher(PLUS, "+"),
                new RegexMatcher(MINUS, "-"),
                new RegexMatcher(AND, "&&"),
                new RegexMatcher(STAR, "[*]"),
                new RegexMatcher(DOT, "[.]"),
                new RegexMatcher(BANG, "!"),
                new RegexMatcher(EQUAL, "="),
                new RegexMatcher(TRUE, "true"),
                new RegexMatcher(FALSE, "false"),
                new RegexMatcher(NEW, "new"),
                new RegexMatcher(THIS, "this"),
                new RegexMatcher(EXTENDS, "extends"),
                new RegexMatcher(RETURN, "return"),
                new RegexMatcher(INT, "int"),
                new RegexMatcher(BOOLEAN, "boolean"),
                new RegexMatcher(IF, "if"),
                new RegexMatcher(ELSE, "else"),
                new RegexMatcher(WHILE, "while"),                
                new RegexMatcher(PRINT, "System\\.out\\.println"),
                new RegexMatcher(CLASS, "class"),
                new RegexMatcher(STATIC, "static"),
                new RegexMatcher(PUBLIC, "public"),
                new RegexMatcher(VOID, "void"),
                new RegexMatcher(STRING, "String"),
                new RegexMatcher(LP, "[(]"),
                new RegexMatcher(RP, "[)]"),
                new RegexMatcher(LEFT_BRACKET, "\\["),
                new RegexMatcher(RIGHT_BRACKET, "\\]"),
                new RegexMatcher(LEFT_BRACE, "\\{"),
                new RegexMatcher(RIGHT_BRACE, "\\}"),
                
                new RegexMatcher(INTEGER, "-?[0-9][0-9]*"), // 00?
                new RegexMatcher(ID, "[A-Za-z_][0-9A-Za-z_]*"),
                new RegexMatcher(WHITESPACE, "[ \t\n\r][ \t\n\r]*"),
                new RegexMatcher(COMMENT, "//[^\n]*\n"),
        };

        for (IMatcher mat : matchers) {
            TOKENIZER.addMatcher(mat);
        }
            
    }
}
