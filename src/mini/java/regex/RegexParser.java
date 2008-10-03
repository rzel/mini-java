package mini.java.regex;

import java.io.IOException;
import java.io.StringReader;

import mini.java.syntax.ParserImpl;
import mini.java.syntax.ParserConfig;

/**
 * RegexParser is a syntax parser for a simple regular expression syntax. This
 * parser implementation uses the default parsing algorithm. See syntax
 * specification for details.
 * 
 * @author Alex
 */
public class RegexParser extends ParserImpl {    
    // syntax specification for REGEX
    private static String[] _rules = {
        "START ::= E",
        "E ::= E STAR",
        "E ::= E BAR E",
        "E ::= LP E RP",
        "E ::= E E",
        "E ::= C",
    };
    
    @Override
    public ParserConfig getParserConfig() {
        StringBuilder builder = new StringBuilder();
        for (String rule : _rules) {
            builder.append(rule);
            builder.append('\n');
        }

        ParserConfig parserConfig = null;
        try {
            parserConfig = new ParserConfig(
                    new StringReader(builder.toString()));
        } catch (IOException ex_) {
            // DO NOTHING HERE
        }

        return parserConfig;
    }

}
