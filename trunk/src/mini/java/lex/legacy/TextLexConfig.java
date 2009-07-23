package mini.java.lex.legacy;

//import java.util.Set;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mini.java.syntax.legacy.TokenType;

public class TextLexConfig {
//    public static String defaultConfigFile = "lex.txt";
    public static final String spec =
        "; ::= ;\n" +
        ", ::= ,\n" +
        "+ ::= +\n" +
        "- ::= -\n" +
        "&& ::= &&\n" +
        "* ::= \\*\n" +
        "] ::= \\]\n" +
        "[ ::= \\[\n" +
        ") ::= \\)\n" +
        "( ::= \\(\n" +
        "} ::= }\n" +
        "{ ::= {\n" +
        ". ::= \\.\n" +
        "! ::= !\n" +
        "= ::= =\n" +
        "true ::= true\n" +
        "false ::= false\n" +
        "this ::= this\n" +
        "new ::= new\n" +
        "System.out.println ::= System\\.out\\.println\n" +
        "class ::= class\n" +
        "static ::= static\n" +
        "public ::= public\n" +
        "void ::= void\n" +
        "main ::= main\n" +
        "String ::= String\n" +
        "extends ::= extends\n" +
        "return ::= return\n" +
        "int ::= int\n" +
        "boolean ::= boolean\n" +
        "if ::= if\n" +
        "else ::= else\n" +
        "while ::= while\n" +
        "Integer ::= -?\\d\\d*\n" +
        "Identifier ::= \\w[\\w\\d]*\n" +
        "WHITESPACE ::= \\s\\s*\n" +
        "COMMENT ::= //.*\n";

    private static TextLexConfig lexConfig = null;
    private Map<TokenType, String> tokenSpec = null;
    // for precedence we need to keep the order;
    private List<TokenType> tokenType = null;

    private TextLexConfig(String filename)
        throws FileNotFoundException, IOException {
        tokenSpec = new HashMap<TokenType, String>();
        tokenType = new ArrayList<TokenType>();
        BufferedReader reader = new BufferedReader(new StringReader(spec));
        String str = null;
        while ((str=reader.readLine()) != null) {
            if (!str.contains("::=")
                || str.charAt(0) == '#' )
                    continue;
            String[] afterSplit = str.split("\\s*::=\\s*");
            TokenType type = new TokenType(afterSplit[0]);
            //System.out.println(type);
            tokenType.add(type);
            tokenSpec.put(type, afterSplit[1]);
        }
    }

    public static TextLexConfig getInstance()
        throws FileNotFoundException, IOException {
        if (lexConfig == null)
            lexConfig = new TextLexConfig(null);
        return lexConfig;
    }

    //public Set<TokenType> getTokenTypes() {
    public List<TokenType> getTokenTypes() {
        //return tokenSpec.keySet();
        return tokenType;
    }

    public String getTokenSpec(TokenType type) {
        return tokenSpec.get(type);
    }
}
