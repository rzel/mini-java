package syntax;

import lex.TokenType;
import lex.LexConfig;
import lex.TextLexConfig;
import syntax.legacy.Rule;
import java.util.Set;
import java.util.HashSet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
//import java.util.ArrayList;
import java.util.List;

public class SyntaxConfig {
    public static String defaultConfigFile = "syntax.txt";

    private static SyntaxConfig INSTANCE = null;
        //new SyntaxConfig(defaultConfigFile);
    //private Set<Rule> rules = new HashSet<Rule>();
    private Set<SymbolType> types = new HashSet<SymbolType>();
    private Map<NonTerminal, Set<Rule>> map =
        new HashMap<NonTerminal, Set<Rule>>();

    private SyntaxConfig(String filename)
        throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        LexConfig lexConfig = TextLexConfig.getInstance();
        //Set<TokenType> terminals = lexConfig.getTokenTypes();
        List<TokenType> terminals = lexConfig.getTokenTypes();
        //System.out.println(terminals);
        //System.out.println((new TokenType("LBRACKET")).equals(new TokenType("LBRACKET")));
        //TokenType lbracket = new TokenType("LBRACKET");
        //for (TokenType t : terminals) {
        //    System.out.println("" + t + ": " + t.equals(lbracket));
        //}
        //System.out.println(terminals.contains(lbracket));

        String str = null;
        while ((str = reader.readLine()) != null) {
            if (!str.contains("::=")
                  || str.charAt(0) == '#') //for comments;
                    continue;

            String[] leftAndRight = str.split("\\s*::=\\s*");
            String[] rightAfterSplit = leftAndRight[1].split(" ");

            NonTerminal leftHand = new NonTerminal(leftAndRight[0]);
            //if (! types.contains(leftHand)) {
            //    types.add(leftHand);
            //}
            Set<Rule> rules = map.get(leftHand);
            if (rules == null) {
                rules = new HashSet<Rule>();
                map.put(leftHand, rules);
            }


            Rule rule = new Rule(leftHand);
            for (String s : Arrays.asList(rightAfterSplit)) {
                SymbolType symbolType = terminals.contains(new TokenType(s)) ? 
                    new Terminal(s) : new NonTerminal(s);
                if (! types.contains(symbolType)) {
                    types.add(symbolType);
                }
                //System.out.println("SymbolType " + symbolType + "[" +
                //    ((symbolType instanceof Terminal) ?
                //        "TERMINAL" :
                //        "NONTERMINAL")
                //    + "]");
                rule.addRhsToken(symbolType);
            }

            //System.out.println(rule);
            rules.add(rule);
        }
    }

    public Set<SymbolType> getSymbolTypes() {
        return types;
    }

    public Set<NonTerminal> getNonTerminals() {
        return map.keySet();
    }

    //public Set<Rule> getRules(SymbolType lhs) {
    public Set<Rule> getRules(NonTerminal lhs) {
        //Set<Rule> rulesFound = new HashSet<Rule>();
        //for (Rule r : rules) {
        //    if (r.getLhs().equals(lhs)) {
        //        rulesFound.add(r);
        //    }
        //}
        return map.get(lhs);
    }

    public static SyntaxConfig getInstance() 
        throws FileNotFoundException, IOException {
        if (INSTANCE == null)
            INSTANCE = new SyntaxConfig(defaultConfigFile);
        return INSTANCE;
    }
}
