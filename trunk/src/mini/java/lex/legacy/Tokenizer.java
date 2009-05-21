package mini.java.lex.legacy;

import java.util.*;

import mini.java.fa.legacy.*;
import mini.java.fa.v2.DFA;
import mini.java.fa.v2.DFAState;

public class Tokenizer {
    //hey, you must keep the order of the dfa, or no precedence
    //will be mess up!
    private List<DFA> dfaWithOrder = new ArrayList<DFA>();
    private HashMap<DFA, String> dfas;
    private List<Token> tokens;
    private Collection<Modifier> modifiers;
    private String prefix;
    private Token prevToken;

    public Tokenizer() {
        dfas = new HashMap<DFA, String>();
        tokens = new LinkedList<Token>();
        modifiers = new ArrayList<Modifier>();
        prefix = "";
        prevToken = null;
    }

    /**
     * Adds the specified Modifier to this Tokenizer
     * @param m the specified Modifier
     */
    public void addModifier(Modifier m) {
        modifiers.add(m);
    }

    /**
     * Adds the specified DFA to this Tokenzier
     * @param dfa the specified DFA
     * @param dfaName the name of the specified DFA
     */
    public void addDFA(DFA dfa, String dfaName) {
        dfaWithOrder.add(dfa);
        dfas.put(dfa, dfaName);
    }

    /**
     * 
     * @return the tokens resulting from analysiss
     */
    public List<Token> getTokens() {
        return tokens;
    }

    /**
     * Analyzes the specified input string
     * @param s the specified input string
     */
    public void analyze(String s) {
        //TODO: I wonder if I must pad an END tag to the string
        //to make use analyze?
        s = prefix + s;
        int beg = 0; //what is beg stands for?
        //Collection<DFA> collect = dfas.keySet();
        //int dfaSize = collect.size();
        int dfaSize = dfaWithOrder.size();
        DFAState[] states = new DFAState[dfaSize];

        while (beg < s.length()) {
            //so first reset all the DFA?
            //for(Iterator<DFA> iter = collect.iterator(); iter.hasNext();) {
            //	iter.next().reset();
            //}
            for (DFA dfa : dfaWithOrder) {
                dfa.reset();
            }
            //System.out.println("CURRENT STR: " + s.substring(beg, s.length()));
            for (int i = 0; i < dfaSize; i++) {
                states[i] = DFAState.INIT;
            }
            int alive = dfaSize;
            int maxLen = 0;
            DFA matchDFA = null;

            for (int cur = beg; cur < s.length(); cur++) {

                char obj = s.charAt(cur);
                //System.out.println("CURRENT CHAR: " + obj);

                int sIdx = 0;
                //for (Iterator<DFA> it = collect.iterator(); it.hasNext() && alive != 0; sIdx++) {
                //	DFA tmp = it.next();
                for (DFA tmp : dfaWithOrder) {
                    if (alive == 0) break;
                    if (states[sIdx] != DFAState.DEAD && states[sIdx] != DFAState.ACCT_DEAD) {
                        states[sIdx] = tmp.feed(obj);				

                        if (states[sIdx] == DFAState.DEAD || states[sIdx] == DFAState.ACCT_DEAD) {
                            alive--;
                        }
                    } else {
                        ++sIdx;
                        continue;
                    }

                    if (states[sIdx] == DFAState.ACCEPTED) {
                        if (maxLen < cur - beg + 1) {
                            maxLen = cur - beg + 1;
                            matchDFA = tmp;
                        }						
                    }
                    //System.out.println("DFA: " + dfas.get(tmp));
                    //System.out.println("DFAState: " + states[sIdx]);
                    //
                    //if (states[sIdx] != DFAState.DEAD) {
                    //    System.out.print(dfas.get(tmp) + "{" + states[sIdx] + "}\t");
                    //}
                    ++sIdx;
                }
                //System.out.println();
                //System.out.println("  CUR: " + cur);
                //System.out.println("  length(s): " + s.length());

                if (alive == 0
                    //|| cur == s.length()-1 //so this is the end of the string;
                ) {
                    //if (cur == s.length()-1) {
                    //    System.out.println("matchDFA: " + dfas.get(matchDFA));
                    //}
                    if (matchDFA != null) {
                        String tokenType = dfas.get(matchDFA);
                        String tokenText = s.substring(beg, cur);
                        /* Line & Column */
                        int line = 1;
                        int col = 1;
                        if(prevToken != null) {
                            /* FIXME */
                            /* TODO: May contain multiple newline's */
                            String prevTxt = prevToken.getText();
                            int total =0;
                            int last =-1;
                            for(int i=0;i<prevTxt.length();i++){
                                char c=prevTxt.charAt(i);
                                if(c == '\n'){
                                    total ++;
                                    last =i;
                                }
                            }
                            line = prevToken.getLineNum() +total;
                            if(last == -1) {
                                col = prevToken.getColumn() + prevTxt.length(); 
                            } else {
                                col = prevTxt.length() - last;
                            }
                        }
                        Token t = new Token(tokenType, tokenText, line, col);
                        prevToken = t;

                        for(Modifier m : modifiers) {
                            m.modify(t);
                        }
                        tokens.add(t);
                        //System.out.println(tokens);
                        //						System.out.printf("** Type: %s, Text: %s\n", t.getType(), t.getText());
                        //						System.out.printf("** Line: %d, Col: %d\n", t.getLineNum(), t.getColumn());
                        // change cursor
                        beg = cur;									
                        //if (cur == s.length() -1) {
                            //END OF INPUT
                            //return;
                            //++beg;
                            //break;
                        //}

                    } else {
                        String tokenText = s.substring(beg, cur + 1);
                        /* Line & Column */
                        int line = 1;
                        int col = 1;
                        if(prevToken != null) {
                            /* FIXME */
                            /* TODO: May contain multiple newline's */
                            String prevTxt = prevToken.getText();
                            int total =0;
                            int last =-1;
                            for(int i=0;i<prevTxt.length();i++){
                                char c=prevTxt.charAt(i);
                                if(c == '\n'){
                                    total ++;
                                    last =i;
                                }
                            }
                            line = prevToken.getLineNum() +total;
                            if(last == -1) {
                                col = prevToken.getColumn() + prevTxt.length(); 
                            } else {
                                col = prevTxt.length() - last;
                            }
                        }
                        Token t = new Token("UNKNOWN_TOKEN", tokenText, line, col);
                        prevToken = t;
                        tokens.add(t);
                        //				System.out.println("Unknown token: `" +
                        //                          s.substring(beg, cur) + "'");
                        cur++;
                        beg = cur;	// may become s.length(), so we have to purge prefix
                    }
                    // TODO not sure!
                    prefix = "";
                    break;
                } else {
                    if(cur == s.length() - 1) {
                        prefix = s.substring(beg);
                        //				System.out.println("* beg: " + beg);
                        //				System.out.println("* prefix: " + prefix);
                        /* fix lineNum & column */
                        // TODO to be verified!!!
                        return;
                    }
                }
                //if(cur == s.length() - 1) {
                //        return;
                //}
            }
            }
        }
    }
