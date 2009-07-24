package mini.java;

import mini.java.lex.IMatcher;
import mini.java.lex.RegexMatcher;
import mini.java.lex.Tokenizer;
import mini.java.syntax.Parser;
import mini.java.syntax.Rule;
import mini.java.syntax.RuleSet;

public class MJCompiler {

    public final static Tokenizer TOKENIZER;
    public final static RuleSet RULE_SET;
    public final static Parser PARSER;
    
    // terminals
    public final static String SEMICOLON = "semicolon", PRINT = "print", CLASS = "class", PUBLIC = "public",
        STATIC = "static", VOID = "void", STRING = "string",  LP = "lp", RP = "rp", LEFT_BRACKET = "left_bracket",
        RIGHT_BRACKET = "right_bracket", LEFT_BRACE = "left_brace", RIGHT_BRACE = "right_brace", INTEGER = "integer",
        ID = "id", WHITESPACE = "whitespace", COMMA = "comma", PLUS = "plus", MINUS = "minus", AND = "and", STAR = "star",
        DOT = "dot", BANG = "bang", EQUAL = "equal", TRUE = "true", FALSE = "false", NEW = "new", THIS = "this",
        EXTENDS = "extends", RETURN = "return", INT = "int", BOOLEAN = "boolean", IF = "if", ELSE = "else", WHILE = "while",
        COMMENT = "comment", MAIN = "main", LESS_THAN = "less_than", LENGTH = "length";
    
    // non-terminals
    public final static String    GOAL      = "Goal", MAIN_CLASS = "MainClass", CLASS_DECLARATION = "ClassDeclaration",
            CLASS_BODY = "ClassBody", VAR_DECLARATION = "VarDeclaration", METHOD_DECLARATION = "MethodDeclaration",
            BLOCK = "Block", PARAM_DEC = "ParamDec", TYPE = "Type", STATEMENTS = "Statements", STATEMENT = "Statement",
            EXPR = "Expr", PARAMS = "Params";
    
    
    static {
        TOKENIZER = new Tokenizer();
        IMatcher[] matchers = new RegexMatcher[] {
                new RegexMatcher(SEMICOLON, ";"),
                new RegexMatcher(COMMA, ","),
                new RegexMatcher(PLUS, "+"),
                new RegexMatcher(MINUS, "-"),
                new RegexMatcher(AND, "&&"),
                new RegexMatcher(LESS_THAN, "<"),
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
                new RegexMatcher(MAIN, "main"),
                new RegexMatcher(LENGTH, "length"),
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
            
        
        
        
        RULE_SET = new RuleSet();
        RULE_SET.addRule(new Rule().left(RuleSet.START).right(GOAL));
        
        RULE_SET.addRule(new Rule().left(GOAL).right(MAIN_CLASS));
        RULE_SET.addRule(new Rule().left(GOAL).right(GOAL, CLASS_DECLARATION));
        
        RULE_SET.addRule(new Rule().left(MAIN_CLASS).right(CLASS, ID, LEFT_BRACE,
                PUBLIC, STATIC, VOID, MAIN, LP, STRING, LEFT_BRACKET, RIGHT_BRACKET, ID, RP, LEFT_BRACE,
                STATEMENT, RIGHT_BRACE, RIGHT_BRACE));
        
        RULE_SET.addRule(new Rule().left(CLASS_DECLARATION).right(CLASS, ID, LEFT_BRACE, CLASS_BODY, RIGHT_BRACE));
        RULE_SET.addRule(new Rule().left(CLASS_DECLARATION).right(
                CLASS, ID, EXTENDS, ID, LEFT_BRACE, CLASS_BODY, RIGHT_BRACE));
        
        RULE_SET.addRule(new Rule().left(CLASS_BODY).right(VAR_DECLARATION));
        RULE_SET.addRule(new Rule().left(CLASS_BODY).right(METHOD_DECLARATION));
        RULE_SET.addRule(new Rule().left(CLASS_BODY).right(VAR_DECLARATION, METHOD_DECLARATION));
        
        RULE_SET.addRule(new Rule().left(VAR_DECLARATION).right(VAR_DECLARATION, VAR_DECLARATION));
        RULE_SET.addRule(new Rule().left(VAR_DECLARATION).right(TYPE, ID, SEMICOLON));
        
        RULE_SET.addRule(new Rule().left(METHOD_DECLARATION).right(
                PUBLIC, TYPE, ID, LP, RP, LEFT_BRACE, BLOCK, RETURN, EXPR, SEMICOLON, RIGHT_BRACE));
        RULE_SET.addRule(new Rule().left(METHOD_DECLARATION).right(
                PUBLIC, TYPE, ID, LP, PARAM_DEC, RP, LEFT_BRACE, BLOCK, RETURN, EXPR, SEMICOLON, RIGHT_BRACE));
        RULE_SET.addRule(new Rule().left(METHOD_DECLARATION).right(METHOD_DECLARATION, METHOD_DECLARATION));
        
        
//        RULE_SET.addRule(new Rule().left(BLOCK).right(VAR_DECLARATION));
        RULE_SET.addRule(new Rule().left(BLOCK).right(STATEMENTS));
        RULE_SET.addRule(new Rule().left(BLOCK).right(VAR_DECLARATION, STATEMENTS));
        
        RULE_SET.addRule(new Rule().left(PARAM_DEC).right(TYPE, ID));
        RULE_SET.addRule(new Rule().left(PARAM_DEC).right(PARAM_DEC));
        
        RULE_SET.addRule(new Rule().left(TYPE).right(INT));
        RULE_SET.addRule(new Rule().left(TYPE).right(BOOLEAN));
        RULE_SET.addRule(new Rule().left(TYPE).right(INT, LEFT_BRACKET, RIGHT_BRACKET));
        RULE_SET.addRule(new Rule().left(TYPE).right(ID));
        
        
        RULE_SET.addRule(new Rule().left(STATEMENTS).right(STATEMENT));
        RULE_SET.addRule(new Rule().left(STATEMENTS).right(STATEMENTS, STATEMENT));
        RULE_SET.addRule(new Rule().left(STATEMENTS).right(LEFT_BRACE, STATEMENTS, RIGHT_BRACE));
        
        RULE_SET.addRule(new Rule().left(STATEMENT).right(IF, LP, EXPR, RP, STATEMENT, ELSE, STATEMENT));
        RULE_SET.addRule(new Rule().left(STATEMENT).right(WHILE, LP, EXPR, RP, STATEMENT));
        RULE_SET.addRule(new Rule().left(STATEMENT).right(PRINT, LP, EXPR, RP, SEMICOLON));
        RULE_SET.addRule(new Rule().left(STATEMENT).right(ID, EQUAL, EXPR, SEMICOLON));
        RULE_SET.addRule(new Rule().left(STATEMENT).right(ID, LEFT_BRACKET, EXPR, RIGHT_BRACKET, EQUAL, EXPR, SEMICOLON));

        
        RULE_SET.addRule(new Rule().left(EXPR).right(EXPR, AND, EXPR));
        RULE_SET.addRule(new Rule().left(EXPR).right(EXPR, LESS_THAN, EXPR));
        RULE_SET.addRule(new Rule().left(EXPR).right(EXPR, PLUS, EXPR));
        RULE_SET.addRule(new Rule().left(EXPR).right(EXPR, MINUS, EXPR));
        RULE_SET.addRule(new Rule().left(EXPR).right(EXPR, STAR, EXPR));
        RULE_SET.addRule(new Rule().left(EXPR).right(BANG, EXPR));
        RULE_SET.addRule(new Rule().left(EXPR).right(LP, EXPR, RP));
        
        RULE_SET.addRule(new Rule().left(EXPR).right(EXPR, LEFT_BRACKET, EXPR, RIGHT_BRACKET));
        RULE_SET.addRule(new Rule().left(EXPR).right(EXPR, DOT, LENGTH));
        RULE_SET.addRule(new Rule().left(EXPR).right(EXPR, DOT, ID, LP, PARAMS, RP));
        
        RULE_SET.addRule(new Rule().left(EXPR).right(TRUE));
        RULE_SET.addRule(new Rule().left(EXPR).right(FALSE));
        RULE_SET.addRule(new Rule().left(EXPR).right(INTEGER));
        RULE_SET.addRule(new Rule().left(EXPR).right(ID));
        RULE_SET.addRule(new Rule().left(EXPR).right(THIS));
        RULE_SET.addRule(new Rule().left(EXPR).right(NEW, INT, LEFT_BRACKET, EXPR, RIGHT_BRACKET));
        RULE_SET.addRule(new Rule().left(EXPR).right(NEW, ID, LP, RP));
        
        
        RULE_SET.addRule(new Rule().left(PARAMS).right(EXPR));
        RULE_SET.addRule(new Rule().left(PARAMS).right(PARAMS, EXPR));
        
        
        PARSER = new Parser(RULE_SET);
    }
}
