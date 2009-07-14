package mini.java.syntax;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

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
}
