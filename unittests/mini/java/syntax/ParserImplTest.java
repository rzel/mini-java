package mini.java.syntax;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;


public class ParserImplTest {
    
    private static String[] _dummyRules = {
        "START ::= A C", "START ::= C", "A ::= B", "C ::= B B"
    };
    
    private class DummyParser extends ParserImpl {
        @Override
        public ParserConfig getParserConfig() {
            StringBuilder builder = new StringBuilder();
            for (String rule : _dummyRules) {
                builder.append(rule);
                builder.append('\n');
            }

            ParserConfig parserConfig = null;
            try {
                parserConfig = new ParserConfig(
                        new StringReader(builder.toString()));
            } catch (IOException ex_) {
                throw new IllegalArgumentException(ex_);
            }
            return parserConfig;
        }        
    }
    
    @Test
    @Ignore("Won't Fix")
    public void testParse() {
        // input: B B B -> A C -> S
        Terminal[] tokens = new Terminal[3];
        for (int i=0; i<tokens.length; ++i) {
            tokens[i] = new Terminal("B");
        }
        
        NonTerminal got =
            new DummyParser().parse(Arrays.asList(tokens));
        assertNotNull(got);
        assertEquals("START", got.getType());
        assertEquals(2, got.getChildren().size());
        // got an extra "B" at the end
    }
    
    @Test
    @Ignore("Won't Fix")
    public void testReduceReduceConflict() {
        Parser parser = new ParserImpl() {

            @Override
            public ParserConfig getParserConfig() {
                StringBuilder sb = new StringBuilder();
                sb.append("START ::= E\n");
                sb.append("E ::= A 1\n");
                sb.append("E ::= B 2\n");
                sb.append("A ::= 1\n");
                sb.append("B ::= 1\n");
                
                ParserConfig conf = null;
                try { conf = new ParserConfig(new StringReader(sb.toString())); }
                catch (Exception ex_)
                {
                    fail("ParserConfig throws an exception: " + ex_);
                }
                
                return conf;
            }
            
        };
        
        List<Terminal> tokens = new LinkedList<Terminal>();
        for (String s : new String[] {"1", "1", "1", "2"}) {
            tokens.add(new Terminal(s));
        }
        {
            NonTerminal got = (NonTerminal)parser.parse(tokens.subList(0, 1)); // "1", "1"
            assertNotNull(got);
            assertEquals("START", got.getType());
            
            NonTerminal E = (NonTerminal)got.getChildren().get(0);
            assertNotNull(E);
            assertEquals("A", E.getChildren().get(0).getType());
            // got syntax error since the parser would do "1" -> "B" and end up with "B", "1"
            // which cannot be reduced
        }
        {
            NonTerminal got = (NonTerminal)parser.parse(tokens.subList(2, 3)); // "1", "2"
            assertNotNull(got);
            assertEquals("START", got.getType());
            
            NonTerminal E = (NonTerminal)got.getChildren().get(0);
            assertNotNull(E);
            assertEquals("B", E.getChildren().get(0).getType());
        }
    }
}
