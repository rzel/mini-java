package mini.java.lex.legacy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import mini.java.fa.v2.DFA;
import mini.java.fa.v2.DFAState;

public class Tokenizer {

    private List<DFA>            dfaWithOrder = new ArrayList<DFA>();
    private HashMap<DFA, String> dfas;
    private List<Token>          tokens;    
    private Token                prevToken;
    
    private String               _input;

    public Tokenizer() {
        dfas = new HashMap<DFA, String>();
        tokens = new LinkedList<Token>();
        _input = "";
        prevToken = null;
    }

    /**
     * Adds the specified DFA to this Tokenzier
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
        
        int beg = 0; // beginning
        int dfaSize = dfaWithOrder.size();
        DFAState[] states = new DFAState[dfaSize];

        while (beg < _input.length()) {
            for (DFA dfa : dfaWithOrder) {
                dfa.reset();
            }

            for (int i = 0; i < dfaSize; i++) {
                states[i] = DFAState.INIT;
            }
            
            int cur;
            int alive = dfaSize;
            int maxLen = 0;
            DFA matchDFA = null;

            for (cur = beg; cur < _input.length(); cur++) {
                char obj = _input.charAt(cur);

                int sIdx = 0; // state index
                for (DFA tmp : dfaWithOrder) {
                    if (alive == 0)
                        break;
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
                    ++sIdx;
                }

                
                if ((alive == 0)
                        || (cur == _input.length() - 1)) // force an update
                {
                    String tokenType, tokenText;
                    
                    if (matchDFA != null) {
                        tokenType = dfas.get(matchDFA);
                        tokenText = _input.substring(beg, beg + maxLen);
                        
                        beg = beg + maxLen;
                    } else {
                        
                        if ((alive != 0)
                                && (cur == _input.length() - 1)) {
                            // in this case we should include the current char as
                            // part of the unknown token
                            ++cur;
                        }
                        
                        tokenType = "UNKNOWN_TOKEN"; // default type
                        tokenText = _input.substring(beg, cur);
                        
                        beg = cur;
                    }
                        
                    
                    Token t = __createToken(tokenType, tokenText);
                    tokens.add(prevToken = t);
                    
                    break;
                    
                }
            }
        }
        
        return tokens;
    }

    /**
     * Analyzes the specified input string
     */
    public void analyze(String s) {
        _input += s;
    }    
    
    

    private Token __createToken(String type_, String text_) {
        /* Line & Column */
        int line = 1;
        int col = 1;
        if (prevToken != null) {
            String prevTxt = prevToken.getText();
            int total = 0;
            int last = -1;
            for (int i = 0; i < prevTxt.length(); i++) {
                char c = prevTxt.charAt(i);
                if (c == '\n') {
                    total++;
                    last = i;
                }
            }
            line = prevToken.getLineNum() + total;
            if (last == -1) {
                col = prevToken.getColumn() + prevTxt.length();
            } else {
                col = prevTxt.length() - last;
            }
        }

        return new Token(type_, text_, line, col);
    }
}
