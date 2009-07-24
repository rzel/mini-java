package mini.java.syntax.legacy;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SyntaxConfig {
//    public static String defaultConfigFile = "syntax.txt";
    
    public final static String spec =
        "#Non Terminals\n" +
        "Goal ::= MainClass\n" +
        "#Goal ::= Goal ClassDeclaration\n" +
        "#MainClass ::= class Identifier { public static void main ( String [ ] Identifier ) { Statement } }\n" +
        "MainClass ::= class Identifier { public static void main ( String [ ] Identifier ) { Statement } }\n" +
        "\n" +
        "#ClassDeclaration ::= class Identifier { ClassBody }\n" +
        "#ClassDeclaration ::= class Identifier extends Identifier { ClassBody }\n" +
        "\n" +
        "#ClassBody ::= VarDeclarations\n" +
        "#ClassBody ::= MethodDeclarations\n" +
        "#ClassBody ::= VarDeclarations MethodDeclarations\n" +
        "#\n" +
        "#VarDeclaratoins ::= VarDeclaration\n" +
        "#VarDeclarations ::= VarDeclarations VarDeclaration\n" +
        "#VarDeclaration ::= Type Identifier ;\n" +
        "#\n" +
        "#MethodDeclarations ::= MethodDeclaration\n" +
        "#MethodDeclarations ::= MethodDeclarations MethodDeclaration\n" +
        "#MethodDeclaration ::= public Type Identifier ( ) { Block return Expression ; }\n" +
        "#MethodDeclaration ::= public Type Identifier ( ParameterDeclarations ) { Block return Expression ; }\n" +
        "#\n" +
        "#Block ::= VarDeclarations\n" +
        "#Block ::= Statements\n" +
        "#Block ::= VarDeclarations Statements\n" +
        "#\n" +
        "#ParameterDeclarations ::= ParameterDeclaration\n" +
        "#ParameterDeclarations ::= ParameterDeclarations , ParameterDeclaration\n" +
        "#ParameterDeclaration ::= Type Identifier\n" +
        "#\n" +
        "#Type ::= int\n" +
        "#Type ::= int [ ]\n" +
        "#Type ::= boolean\n" +
        "#Type ::= Identifier\n" +

        "#Statements ::= Statement\n" +
        "#Statements ::= Statements Statement\n" +
        "#Statement ::= { Statements }\n" +
        "\n" +
        "#Statement ::= { Statement }\n" +
        "#Statement ::= Statement Statement\n" +
        "#Statement ::= if ( Expression ) Statement else Statement\n" +
        "#Statement ::= while ( Expression ) Statement\n" +
        "Statement ::= System.out.println ( Expression ) ;\n" +
        "Statement ::= Identifier = Expression ;\n" +
        "Statement ::= Identifier [ Expression ] = Expression ;\n" +
        "\n" +
        "#Expression ::= Expression && Expression\n" +
        "#Expression ::= Expression < Expression\n" +
        "#Expression ::= Expression + Expression\n" +
        "#Expression ::= Expression - Expression\n" +
        "#Expression ::= Expression * Expression\n" +
        "Expression ::= ! Expression\n" +
        "Expression ::= ( Expression )\n" +
        "Expression ::= Expression [ Expression ]\n" +
        "Expression ::= Expression . length\n" +
        "Expression ::= Expression . Identifier ( Parameters )\n" +
        "\n" +
        "Expression ::= Integer\n" +
        "Expression ::= true\n" +
        "Expression ::= false\n" +
        "Expression ::= Identifier\n" +
        "Expression ::= this\n" +
        "Expression ::= new int [ Expression ]\n" +
        "Expression ::= new Identifier ( )\n" +
        "\n" +
        "Parameters ::= Expression\n" +
        "Parameters ::= Parameters , Expression\n";


    private static SyntaxConfig INSTANCE = null;

    private Set<SymbolType> types = new HashSet<SymbolType>();
    private Map<NonTerminal, Set<Rule>> map =
        new HashMap<NonTerminal, Set<Rule>>();

    private SyntaxConfig(String filename)
        throws FileNotFoundException, IOException {
        
        Set<String> symbol = new HashSet<String>();
        Set<String> nonTerminals = new HashSet<String>();
        {
            BufferedReader reader = new BufferedReader(new StringReader(spec));
            String str;
            while ((str = reader.readLine()) != null) {
                if (!str.contains("::=")
                        || str.charAt(0) == '#') //for comments;
                          continue;
                
                String[] leftAndRight = str.split("\\s*::=\\s*");
                String[] rightAfterSplit = leftAndRight[1].split(" ");
                
                nonTerminals.add(leftAndRight[0]); //left
                symbol.add(leftAndRight[0]);
                symbol.addAll(Arrays.asList(rightAfterSplit));
            }
        }
        Set<String> terminals = new HashSet<String>(symbol);
        terminals.removeAll(nonTerminals);
        
        
        BufferedReader reader = new BufferedReader(new StringReader(spec));

        String str = null;
        while ((str = reader.readLine()) != null) {
            if (!str.contains("::=")
                  || str.charAt(0) == '#') //for comments;
                    continue;

            String[] leftAndRight = str.split("\\s*::=\\s*");
            String[] rightAfterSplit = leftAndRight[1].split(" ");

            NonTerminal leftHand = new NonTerminal(leftAndRight[0]);

            
            if (!map.containsKey(leftHand)) {
                map.put(leftHand, new HashSet<Rule>());
            }
            Set<Rule> rules = map.get(leftHand);


            Rule rule = new Rule(new TokenSpec("NON_TERMINAL", leftHand.getRep()));
            for (String s : rightAfterSplit) {
                SymbolType symbolType = terminals.contains(new TokenType(s)) ? 
                    new Terminal(s) : new NonTerminal(s);
                if (! types.contains(symbolType)) {
                    types.add(symbolType);
                }
                rule.addRhsTokenSpec(new TokenSpec((symbolType instanceof NonTerminal)
                        ? "NON_TERMINAL" : "TERMINAL", symbolType.getRep()));
            }

            rules.add(rule);
        }
    }

    public Set<SymbolType> getSymbolTypes() {
        return types;
    }

    public Set<NonTerminal> getNonTerminals() {
        return map.keySet();
    }

    public Set<Rule> getRules(NonTerminal lhs) {
        return map.get(lhs);
    }

    public static SyntaxConfig getInstance() 
        throws FileNotFoundException, IOException {
        if (INSTANCE == null)
            INSTANCE = new SyntaxConfig(null);
        return INSTANCE;
    }
}
