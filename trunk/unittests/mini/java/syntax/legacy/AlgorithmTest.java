package mini.java.syntax.legacy;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;


public class AlgorithmTest {
    // non-terminals
    public static final TokenSpec A = new TokenSpec(TokenSpec.NON_TERMINAL_TYPE, "A");
    public static final TokenSpec C = new TokenSpec(TokenSpec.NON_TERMINAL_TYPE, "C");
    public static final TokenSpec D = new TokenSpec(TokenSpec.NON_TERMINAL_TYPE, "D");
    // terminals
    public static final TokenSpec B = new TokenSpec(TokenSpec.TERMINAL_TYPE, "B");
    public static final TokenSpec E = new TokenSpec(TokenSpec.TERMINAL_TYPE, "E");
    public static final TokenSpec F = new TokenSpec(TokenSpec.TERMINAL_TYPE, "F");
    public static final TokenSpec G = new TokenSpec(TokenSpec.TERMINAL_TYPE, "G");
    public static final TokenSpec H = new TokenSpec(TokenSpec.TERMINAL_TYPE, "H");
    
    // rules
    public static final Rule A_BC;
    public static final Rule C_DE;
    public static final Rule C_DF;
    public static final Rule D_EF;
    public static final Rule A_B, C_A, D_CE;
    public static final Rule A_AC, C_CAB; // recursive rules
    
    static {
        
        A_BC = createRule(A, B, C); // A ::= B C
        C_DE = createRule(C, D, E);
        C_DF = createRule(C, D, F);
        D_EF = createRule(D, E, F);
        
        A_B = createRule(A, B);
        C_A = createRule(C, A);
        D_CE = createRule(D, C, E);
        
        A_AC = createRule(A, A, C);
        C_CAB = createRule(C, C, A, B);
    }
    
    public static Rule createRule(TokenSpec... tokens_) {
        if (tokens_ == null || tokens_.length < 2) {
            throw new IllegalArgumentException("Each rule must contain at least two tokens");
        }
        
        Rule ret = new Rule(tokens_[0]);
        for (int i=1; i<tokens_.length; ++i) {
            ret.addRhsTokenSpec(tokens_[i]);
        }
        return ret;
    }
    
    public static Set<SymbolType> symbols(TokenSpec... tokens_) {
        Set<SymbolType> ret = new HashSet<SymbolType>();
        for (TokenSpec token : tokens_) {
            ret.add(SymbolType.createSymbol(token));
        }
        return ret;
    }
    
    @Test
    public void testClosure() {
        Item item = new Item(A_BC, 1); // A ::= B dot C
        
        Set<Item> got = new HashSet<Item>(Algorithm.closure(item, Arrays.asList(A_BC, C_DE, D_EF, A_B)));
        Set<Item> expected = new HashSet<Item>();
        expected.add(item); // should include itself
        expected.add(new Item(C_DE));
        expected.add(new Item(D_EF));
        
        Assert.assertEquals(expected, got);        
    }
    
    @Test
    public void testClosure00() {
        List<Rule> rules = sampleRules();
        Item item = new Item(rules.get(5), 2); // A ::= A B dot C (e ::= e + dot t)
        Set<Item> got = new HashSet<Item>(Algorithm.closure(item, rules));
        Set<Item> expected = new HashSet<Item>(
                Arrays.asList(
                        item,
                        new Item(rules.get(1)), // C ::= D (t ::= f)
                        new Item(rules.get(2)), // C ::= C E D (t ::= t * f)
                        new Item(rules.get(3)), // D ::= F A G (f ::= (e))
                        new Item(rules.get(4)) // D ::= H (f ::= id)
                        ));
        
        Assert.assertEquals(expected, got);
    }
    
    @Test
    public void testClosureNone() {
        // for Item.getFollow() equals null
        List<Rule> rules = sampleRules();
        Item item = new Item(rules.get(5), 3); // A ::= A B C dot
        Set<Item> got = new HashSet<Item>(Algorithm.closure(item, rules));
        Set<Item> expected = Collections.singleton(item);
        
        Assert.assertEquals(expected, got);
    }
    
    @Test
    public void testGetFollows() {
        Set<SymbolType> got = 
            new HashSet<SymbolType>(Algorithm.getFollows(E, Arrays.asList(D_EF)));
        Set<SymbolType> expected = new HashSet<SymbolType>(
                Arrays.asList(SymbolType.createSymbol(F)));
        Assert.assertEquals(expected, got);
    }
    
    @Test
    public void testGetFollowsLastElement() {
        Set<SymbolType> got = 
            new HashSet<SymbolType>(Algorithm.getFollows(B, Arrays.asList(A_B, C_A, D_CE)));
        Set<SymbolType> expected = new HashSet<SymbolType>(
                Arrays.asList(SymbolType.createSymbol(E)));
        
        Assert.assertEquals(expected, got);
    }
    
    @Test
    public void testGetFollowsNonTerminals() {
        Set<SymbolType> got = 
            new HashSet<SymbolType>(Algorithm.getFollows(B, Arrays.asList(A_BC, C_DF, D_EF)));
        Set<SymbolType> expected = new HashSet<SymbolType>(
                Arrays.asList(SymbolType.createSymbol(E)));
        
        Assert.assertEquals(expected, got);
    }
    
    @Test
    public void testGetFollowsRecursive() {
        Set<SymbolType> got = 
            new HashSet<SymbolType>(Algorithm.getFollows(A, Arrays.asList(A_AC, C_CAB)));
        Set<SymbolType> expected = new HashSet<SymbolType>(
                Arrays.asList(SymbolType.createSymbol(B)));
        
        Assert.assertEquals(expected, got);
    }
    
    @Test
    public void testGetFollowsNone() {
        Set<SymbolType> got = 
            new HashSet<SymbolType>(Algorithm.getFollows(A, Arrays.asList(A_AC)));
        
        Assert.assertEquals(Collections.EMPTY_SET, got);
    }
    
    @Test
    public void testGetFollows00() {
        List<Rule> rules = sampleRules();
        
        Set<SymbolType> follows_for_B = new HashSet<SymbolType>(Algorithm.getFollows(B, rules));
        Set<SymbolType> expected_for_B = new HashSet<SymbolType>();
        expected_for_B.add(SymbolType.createSymbol(F));
        expected_for_B.add(SymbolType.createSymbol(H));
        Assert.assertEquals(expected_for_B, follows_for_B);
        
        Assert.assertEquals(symbols(B,E,G),
                new HashSet<SymbolType>(Algorithm.getFollows(H, rules)));
        Assert.assertEquals(symbols(B,G),
                new HashSet<SymbolType>(Algorithm.getFollows(A, rules)));
    }
    
    
    @Test
    public void testClosureSet() {
        SymbolType END = new Terminal("END");
        
        int NUM = 12;
        State[] s = new State[NUM];
        for (int i=0; i<NUM; ++i) {
            s[i] = new State(i);
        }
        
        List<Rule> rules = sampleRules();
        Rule[] remapped = new Rule[rules.size()];
        remapped[0] = rules.get(5); // e::= e + t
        remapped[1] = rules.get(0); // e::= t
        remapped[2] = rules.get(2); // t ::= t * f
        remapped[3] = rules.get(1); // t ::= f
        remapped[4] = rules.get(3); // f ::= ( e )
        remapped[5] = rules.get(4); // f ::= id
        
        Map<Pair<State, SymbolType>, Action> tbl = Algorithm.closureSet(Arrays.asList(remapped));
        /*
         * s0: r0(0), r1(0), r3(0), r5(0), r4(0), r2(0)
         * s1: r0(1),
         * s2: r1(1), r2(1)
         * s3: r3(1)
         * s4: r5(1)
         * s5: r4(1), r0(0), r1(0), r3(0), r5(0), r4(0), r2(0)
         * s6: r0(2), r3(0), r5(0), r4(0), r2(0)
         * s7: r2(2), r4(0), r5(0)
         * s8: r4(2), r0(1)
         * s9: r0(4), r2(1)
         * s10: r2(3)
         * s11: r4(3),
         *     
         *       A(e)      C(t)      D(f)      E(*)      B(+)        F(()      G())        H(id)      END
         * ----------------------------------------------------------------------------------------------------
         * s0   goto(s1)  goto(s2)  goto(s3)   null      null       shift(s5)  null       shift(s4)
         * s1   null      null      null       null      shift(s6)  null       null       null
         * s2   null      null      null      shift(s7)  reduce(r1) null       reduce(r1) null       reduce(r1)
         * s3   null      null      null      reduce(r3) reduce(r3) null       reduce(r3) null       reduce(r3)
         * s4   null      null      null      reduce(r5) reduce(r5) null       reduce(r5) null       reduce(r5)
         * s5   goto(s8)  goto(s2)  goto(s3)   null      null       shift(s5)  null       shift(s4)
         * s6   null      goto(s9)  goto(s3)   null      null       shift(s5)  null       shift(s4)
         * s7   null      null      goto(s10)  null      null       shift(s5)  null       shift(s4)
         * s8   null      null      null       null      shift(s6)   null      shift(s11)
         * s9   null      null      null      shift(s7)  reduce(r0)  null      reduce(r0)  null      reduce(r0)      
         * s10  null      null      null      reduce(r2) reduce(r2)  null      reduce(r2)  null      reduce(r2)
         * s11  null      null      null      reduce(r4) reduce(r4)  null      reduce(r4)  null      reduce(r4)
         * 
         * total: 44
         * 
         */
        
        
        //-----s0-----
        checkElement(tbl, s[0], A, new Action(Action.ACTION_GOTO, s[1]));
        checkElement(tbl, s[0], C, new Action(Action.ACTION_GOTO, s[2]));
        checkElement(tbl, s[0], D, new Action(Action.ACTION_GOTO, s[3]));
        checkElementNull(tbl, s[0], E);
        checkElementNull(tbl, s[0], B);
        checkElement(tbl, s[0], F, new Action(Action.ACTION_SHIFT, s[5]));
        checkElementNull(tbl, s[0], G);
        checkElement(tbl, s[0], H, new Action(Action.ACTION_SHIFT, s[4]));
        
        //-----s1-----
        checkElement(tbl, s[1], B, new Action(Action.ACTION_SHIFT, s[6]));
        //-----s2-----
        checkElement(tbl, s[2], E, new Action(Action.ACTION_SHIFT, s[7]));
        checkElement(tbl, s[2], B, new Action(Action.ACTION_REDUCE, remapped[1]));
        checkElement(tbl, s[2], G, new Action(Action.ACTION_REDUCE, remapped[1]));
        checkElement(tbl, s[2], END, new Action(Action.ACTION_REDUCE, remapped[1]));
        //-----s3-----
        checkElement(tbl, s[3], E, new Action(Action.ACTION_REDUCE, remapped[3]));
        checkElement(tbl, s[3], B, new Action(Action.ACTION_REDUCE, remapped[3]));
        checkElement(tbl, s[3], G, new Action(Action.ACTION_REDUCE, remapped[3]));
        checkElement(tbl, s[3], END, new Action(Action.ACTION_REDUCE, remapped[3]));
        //-----s4-----
        checkElement(tbl, s[4], E, new Action(Action.ACTION_REDUCE, remapped[5]));
        checkElement(tbl, s[4], B, new Action(Action.ACTION_REDUCE, remapped[5]));
        checkElement(tbl, s[4], G, new Action(Action.ACTION_REDUCE, remapped[5]));
        checkElement(tbl, s[4], END, new Action(Action.ACTION_REDUCE, remapped[5]));
        //-----s5-----
        checkElement(tbl, s[5], A, new Action(Action.ACTION_GOTO, s[8]));
        checkElement(tbl, s[5], C, new Action(Action.ACTION_GOTO, s[2]));
        checkElement(tbl, s[5], D, new Action(Action.ACTION_GOTO, s[3]));
        checkElement(tbl, s[5], F, new Action(Action.ACTION_SHIFT, s[5]));
        checkElement(tbl, s[5], H, new Action(Action.ACTION_SHIFT, s[4]));
        
        //-----s6-----
        checkElementNull(tbl, s[6], A);
        checkElement(tbl, s[6], C, new Action(Action.ACTION_GOTO, s[9]));
        checkElement(tbl, s[6], D, new Action(Action.ACTION_GOTO, s[3]));
        checkElement(tbl, s[6], F, new Action(Action.ACTION_SHIFT, s[5]));
        checkElement(tbl, s[6], H, new Action(Action.ACTION_SHIFT, s[4]));
        
        //-----s7-----
        checkElement(tbl, s[7], D, new Action(Action.ACTION_GOTO, s[10]));
        checkElement(tbl, s[7], F, new Action(Action.ACTION_SHIFT, s[5]));
        checkElement(tbl, s[7], H, new Action(Action.ACTION_SHIFT, s[4]));
        //-----s8-----
        checkElement(tbl, s[8], B, new Action(Action.ACTION_SHIFT, s[6]));
        checkElement(tbl, s[8], G, new Action(Action.ACTION_SHIFT, s[11]));
        
        //-----s9-----
        checkElement(tbl, s[9], E, new Action(Action.ACTION_SHIFT, s[7]));
        checkElement(tbl, s[9], B, new Action(Action.ACTION_REDUCE, remapped[0]));
        checkElement(tbl, s[9], G, new Action(Action.ACTION_REDUCE, remapped[0]));
        checkElement(tbl, s[9], END, new Action(Action.ACTION_REDUCE, remapped[0]));
        //-----s10-----
        checkElement(tbl, s[10], E, new Action(Action.ACTION_REDUCE, remapped[2]));
        checkElement(tbl, s[10], B, new Action(Action.ACTION_REDUCE, remapped[2]));
        checkElement(tbl, s[10], G, new Action(Action.ACTION_REDUCE, remapped[2]));
        checkElement(tbl, s[10], END, new Action(Action.ACTION_REDUCE, remapped[2]));
        //-----s11-----
        checkElement(tbl, s[11], E, new Action(Action.ACTION_REDUCE, remapped[4]));
        checkElement(tbl, s[11], B, new Action(Action.ACTION_REDUCE, remapped[4]));
        checkElement(tbl, s[11], G, new Action(Action.ACTION_REDUCE, remapped[4]));
        checkElement(tbl, s[11], END, new Action(Action.ACTION_REDUCE, remapped[4]));
        
        // check the total number of entries
        Assert.assertEquals(44, tbl.size());
    }
    
    
    private void checkElement(Map<Pair<State, SymbolType>, Action> tbl_,
            State curState_, TokenSpec curToken_, Action expected_)
    {
        checkElement(tbl_, curState_, SymbolType.createSymbol(curToken_), expected_);
    }
    
    private void checkElement(Map<Pair<State, SymbolType>, Action> tbl_,
            State curState_, SymbolType curSym_, Action expected_)
    {
        Action got = tbl_.get(new Pair<State, SymbolType>(curState_, curSym_));
        Assert.assertNotNull("Missing action for + <" + curState_ + ", " + curSym_ + ">", got);
        Assert.assertEquals(expected_, got);
    }
    
    private void checkElementNull(Map<Pair<State, SymbolType>, Action> tbl_,
            State curState_, TokenSpec curToken_)
    {
        SymbolType curSym_ = SymbolType.createSymbol(curToken_);
        Action got = tbl_.get(new Pair<State, SymbolType>(curState_, curSym_));
        Assert.assertNull("Extra action for <" + curState_ + ", " + curSym_ + ">: " + got, got);
    }
    
    
    private List<Rule> sampleRules() {
        Rule r1 = createRule(A, C); // A ::= C
        Rule r2 = createRule(C, D); // C ::= D
        Rule r3 = createRule(C, C, E, D); // C ::= C E D
        Rule r4 = createRule(D, F, A, G); // D ::= F A G
        Rule r5 = createRule(D, H);
        Rule r6 = createRule(A, A, B, C);
        return Arrays.asList(r1, r2, r3, r4, r5, r6);
    }
}
