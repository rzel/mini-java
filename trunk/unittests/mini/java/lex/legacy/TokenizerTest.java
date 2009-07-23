package mini.java.lex.legacy;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mini.java.fa.helper.Helper;
import mini.java.fa.helper.IFinder;
import mini.java.fa.v1.SimpleFA;
import mini.java.fa.v1.State;
import mini.java.fa.v2.DFA;
import mini.java.fa.v2.DFANode;
import mini.java.fa.v2.SimpleDFA;
import mini.java.fa.v2.Transition;
import mini.java.regex.legacy.RegexCompiler;
import mini.java.syntax.legacy.TokenType;

import org.junit.Test;


public class TokenizerTest {
    
    private final static boolean DEBUG = true;
    
    @Test
    public void testBug1() {
        Tokenizer tokenizer = __createTokenizer();
        tokenizer.analyze("class");
        
        List<Token> tokens = tokenizer.getTokens();
        assertEquals(Collections.singletonList(new Token("class", "class", 1, 1)),
                tokens);
    }

    @Test
    public void testAnalyze() {
        Tokenizer tokenizer = __createTokenizer();

        tokenizer.analyze(
                "class Main {\n"
              + "    public static void main(String[] args) {\n\n"
              + "    }\n"
              + "} ");
        List<Token> tokens = tokenizer.getTokens();
        
        { // line 1
            List<Token> expected = new LinkedList<Token>();
            expected.add(new Token("class", "class", 1, 1));
            expected.add(new Token("WHITESPACE", " ", 1, 6));
            expected.add(new Token("Identifier", "Main", 1, 7));
            expected.add(new Token("WHITESPACE", " ", 1, 11));
            expected.add(new Token("{", "{", 1, 12));
            expected.add(new Token("WHITESPACE", "\n    ", 1, 13));
            assertEquals(expected, tokens.subList(0, 6));
        }
        
        { // line 2
            List<Token> expected = new LinkedList<Token>();
            
            expected.add(new Token("public", "public", 2, 5));
            expected.add(new Token("WHITESPACE", " ", 2, 11));
            expected.add(new Token("static", "static", 2, 12));
            expected.add(new Token("WHITESPACE", " ", 2, 18));
            expected.add(new Token("void", "void", 2, 19));
            expected.add(new Token("WHITESPACE", " ", 2, 23));
            expected.add(new Token("main", "main", 2, 24));
            expected.add(new Token("(", "(", 2, 28));
            expected.add(new Token("String", "String", 2, 29));
            expected.add(new Token("[", "[", 2, 35));
            expected.add(new Token("]", "]", 2, 36));
            expected.add(new Token("WHITESPACE", " ", 2, 37));
            expected.add(new Token("Identifier", "args", 2, 38));
            expected.add(new Token(")", ")", 2, 42));
            expected.add(new Token("WHITESPACE", " ", 2, 43));
            expected.add(new Token("{", "{", 2, 44));
            expected.add(new Token("WHITESPACE", "\n\n    ", 2, 45));
            
            assertArrayEquals(expected.toArray(new Token[0]),
                    tokens.subList(6, 23).toArray(new Token[0]));
        }
        
//        { // line 3
//            assertEquals(new Token("WHITESPACE", "\n", 3, 1),
//                tokens.get(24));
//        }
        
        { // line 4
            Token[] expected = new Token[] {
//                    new Token("WHITESPACE", "    ", 4, 1), 
                    new Token("}", "}", 4, 5),
                    new Token("WHITESPACE", "\n", 4, 6),
            };
            assertArrayEquals(expected, tokens.subList(23, 25).toArray());
        }
        
        { // line 5
            Token[] expected = new Token[] {
                    new Token("}", "}", 5, 1),
                    new Token("WHITESPACE", " ", 5, 2),
            };
            assertArrayEquals(expected,
                    tokens.subList(25, 27).toArray());
        }
        
    }
    
    
    @Test
    public void testAnalyzeUnknownToken() {
        __testTokenizer("&", new Token("UNKNOWN_TOKEN", "&", 1, 1));
        __testTokenizer("a&b",
                new Token("Identifier", "a", 1, 1),
                new Token("UNKNOWN_TOKEN", "&", 1, 2),
                new Token("Identifier", "b", 1, 3));
    }
    
    
    private static void __testTokenizer(String input_, Token... tokens_) {
        Tokenizer tokenizer = __createTokenizer();
        
        tokenizer.analyze(input_);
        List<Token> tokens = tokenizer.getTokens();
        
        assertEquals(Arrays.asList(tokens_), tokens);
    }
    
    
    private static DFA __convert(SimpleFA fa_) {
        final DFA dfa = new SimpleDFA();
        final SimpleFA fa = fa_;
        final Map<State, DFANode> map = new HashMap<State, DFANode>();
        
        State init = fa.getInitialState();     
        map.put(init, DFANode.getStartNode());
        if (fa.getAcceptedStates().contains(init)) {
            dfa.addAcceptedNode(DFANode.getStartNode());
        }
        
        Helper.visit(init, new IFinder<State>() {

            @Override
            public List<State> findNext(State node_) {
                List<State> ret = new LinkedList<State>();
                for (Character input : fa.getInputMixed(node_)) {
                    State next = fa.move(node_, Collections.singleton(input))
                                    .iterator().next();
                    
                    if (!map.containsKey(next)) {
                        DFANode node = new DFANode(map.size());
                        
                        map.put(next, node);
                        if (fa.getAcceptedStates().contains(next)) {
                            dfa.addAcceptedNode(node);
                        }
                    }
                    
                    {
                        DFANode from = map.get(node_),
                            to = map.get(next);
                        
                        Transition tr = new Transition(from, to);
                        tr.addInput(input);
                        
                        dfa.addTransition(tr);
                    }
                    ret.add(next);
                }
                return ret;
            }
            
        });
        return dfa;
    }
    
    private static Tokenizer __createTokenizer() {
        Tokenizer tokenizer = new Tokenizer();
        try {
            TextLexConfig lexConfig = TextLexConfig.getInstance();

            for (TokenType type : lexConfig.getTokenTypes()) {
                String tokenSpec = lexConfig.getTokenSpec(type);
                SimpleFA fa = RegexCompiler.compile(tokenSpec).toDFA();
                DFA dfa = __convert(fa);

                if (DEBUG) {

                }

                tokenizer.addDFA(dfa, type.toString());
            }
        } catch (Exception ex_) {
            fail("Exception: " + ex_);
        }
        return tokenizer;
    }
}
