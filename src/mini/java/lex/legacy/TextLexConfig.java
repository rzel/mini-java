package mini.java.lex.legacy;

//import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TextLexConfig implements LexConfig {
    public static String defaultConfigFile = "lex.txt";

    private static TextLexConfig lexConfig = null;
    private Map<TokenType, String> tokenSpec = null;
    // for precedence we need to keep the order;
    private List<TokenType> tokenType = null;

    private TextLexConfig(String filename)
        throws FileNotFoundException, IOException {
        tokenSpec = new HashMap<TokenType, String>();
        tokenType = new ArrayList<TokenType>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
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
            lexConfig = new TextLexConfig(defaultConfigFile);
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
