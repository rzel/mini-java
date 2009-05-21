package mini.java.fa.my;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import mini.java.fa.NFAClosure;
import mini.java.fa.NFAState;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class GetTargetClosureTest {
    private NFAState      _initialState;
    private Set<NFAState> _targetStates;
    private Object        _input;
    
    // Constructor
    public GetTargetClosureTest(String states_, String transitions_,
            Object input_, String targets_) {
        TestHelper helper = new TestHelper();
        helper.addNFAStates(states_);
        helper.addTransitions(transitions_);
        _initialState = helper.getNFAState(TestHelper.INITIAL_STATE);
        _input = input_;

        _targetStates = new HashSet<NFAState>();
        // create the expected closure
        for (Character c : targets_.toCharArray()) {
            _targetStates.add(helper.getNFAState(c));
        }
    }
    
    @Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
                {"AB",           "ABa",         'a',        "B"},
                {"ABC",          "AB,BCa",      'a',        "C"},
                {"ABC",          "ABa,AC",      'a',        "B"},
                {"ABC",          "ACa,BC",      'a',        "C"},
                {"ABC",          "ABa,BC",      'a',        "BC"},
                {"ABCD",         "AB,BCa,CD",   'a',        "CD"},
                {"ABCD",         "AB,ACa,CD",   'a',        "CD"},
                {"AB",           "AB,BBa",      'a',        "B"},
                {"AB",           "AB,AAa",      'a',        "AB"},
                {"ABC",          "AB,BC,BBa",   'a',        "B"},
        });
    }
    
    @Test
    public void testFindClosure() {
        NFAClosure closure = new NFAClosure(_initialState);
        assertNotNull(closure);
        
        NFAClosure got = closure.getClosure(_input);
        NFAClosure expected = new NFAClosure(_targetStates);
        assertEquals(expected, got);
    }
}

