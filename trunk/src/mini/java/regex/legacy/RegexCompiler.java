/**
 * A simple Regular Expression compiler.
 * Specification:
 * LEX:
 *     STAR: *
 *     BAR: |
 *     QM: ?
 *     SET: [] [^] (empty set not allowed)
 *     LP,RP: ( )
 *     ESCAPED: \* \| \? \( \) \[ \] \\
 *              \r : CR
 *              \n : LF
 *              \t : TAB
 *              \s : SP ([\r\n\t ])
 *              \d : 0|1|..|8|9
 *              \w : a|b|..|y|z|A|B|..|Y|Z|_
 *     DOT: . (anything)
 *     CHAR: other characters
 *
 * GRAMMER:
 *     EXPR := CHAR | SET | ESCAPED | DOT
 *          := EXPR EXPR
 *          := EXPR BAR EXPR
 *          := LP EXPR RP 
 *          := EXPR STAR
 *          := EXPR QM
 */
package mini.java.regex.legacy;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.EmptyStackException;
import java.util.Set;
import java.util.HashSet;
import java.util.Stack;
import java.util.Arrays;

import mini.java.fa.State;
import mini.java.fa.legacy.v1.SimpleFA;


public class RegexCompiler {
/**
 * tokenizer used by compile
 * @param src string represents the regular expression
 * @return List of the tokens
 * @throws Exception for all errors
 */
    public static List<RegexToken> tokenize(String src) throws Exception {
        List<RegexToken> tokens = new ArrayList<RegexToken>();
        for (int i=0; i<src.length(); ++i) {
            RegexToken.Type type;
            Set<Character> characters = null;
            switch (src.charAt(i)) {
                case '.':
                    // support for dot, which matches "any" character,
                    // only 7-bit charactes are supported
                    type = RegexToken.Type.CHAR;
                    characters = new HashSet<Character>();
                    for (char c=0; c<=127; ++c) {
                        characters.add(c);
                    }
                    break;
                case '*': type = RegexToken.Type.STAR; break;
                case '|': type = RegexToken.Type.BAR; break;
                case '?': type = RegexToken.Type.QM; break;
                case '(': type = RegexToken.Type.LP; break;
                case ')': type = RegexToken.Type.RP; break;
                case ']': throw new Exception("extra right bracket");
                case '[':
                    type = RegexToken.Type.CHAR;
                    Set<Character> characters0 = new HashSet<Character>();
                    boolean hasCaret = false;
                    //StringBuffer strbuff = new StringBuffer();
                    try {
                        if (src.charAt(i+1) == '^') {
                            hasCaret = true;
                        }
                        while (src.charAt(++i) != ']') {
                            if (src.charAt(i) == '\\') {
                                characters0.addAll(escape(src.charAt(++i)));
                            } else {
                                characters0.add(src.charAt(i));
                            }
                        }
                    } catch (IndexOutOfBoundsException ex) {
                        throw new Exception("illegal character set");
                    }
                    if (hasCaret) {
                        //if there is a caret(^), return the complement
                        characters = new HashSet<Character>();
                        for (char c=0; c<=127; ++c) {
                            if (!characters0.contains(c)) {
                                characters.add(c);
                            }
                        }
                    } else {
                        characters = characters0;
                    }
// range operation removed, use special escaped characters instead
/*
                    try {
                        while (src.charAt(++i) != ']') {
                            if (src.charAt(i) == '\\') {
                                for (Object j : escape(src.charAt(++i))) {
                                    strbuff.append((Character)j);
                                }
                            } else {
                                strbuff.append(src.charAt(i));
                            }
                        }

                        char prev=0;
                        boolean canDoRange = false;
                        for (int j=0; j<strbuff.length(); ++j) {
                            char curr = strbuff.charAt(j);
                            if (curr == '-') {
                                char next = strbuff.charAt(++j);
                                if (!canDoRange)
                                    throw new Exception("illegal range");
                                if (!Character.isLetterOrDigit(prev)
                                    || !Character.isLetterOrDigit(next)) {
                                    throw new Exception("illegal range");
                                }
                                if ((Character.isUpperCase(prev)
                                    && Character.isUpperCase(next))
                                    || (Character.isLowerCase(prev)
                                        && (Character.isLowerCase(next)))
                                    || (Character.isDigit(prev))
                                        && (Character.isDigit(next))) {
                                    for (char c=prev; c<=next; ++c)
                                        characters.add(c);
                                } else {
                                    throw new Exception("illegal range");
                                }
                                canDoRange = false;
                            } else {
                                characters.add(curr);
                                prev = curr;
                                canDoRange = true;
                            }
                        }
                    } catch (IndexOutOfBoundsException ex) {
                        throw new Exception("illegal character set");
                    } catch (Exception ex) {
                        throw ex;
                    }
*/
                    if (characters.size() == 0)
                        throw new Exception("empty set");
                    break;
                case '\\':
                    ++i;
                    if (i >= src.length()) {
                        throw new Exception("illegal escaped character");
                    }
                    type = RegexToken.Type.CHAR;
                    characters = new HashSet<Character>();
                    characters.addAll(escape(src.charAt(i)));
                    break;
                default:
                    type = RegexToken.Type.CHAR;
                    characters = new HashSet<Character>();
                    characters.add(src.charAt(i));
            }
            tokens.add(new RegexToken(type, characters));
        }
        return tokens;
    }

/**
 * handle special escaped charactors
 * @param c charactor to escape
 * @return a set of charactors the escaped charactor stands for
 */
    public static Set<Character> escape(char ch) {
        Set<Character> characters = new HashSet<Character>();
        switch (ch) {
            case 'd': //digits
                for (char c='0'; c<='9'; ++c)
                    characters.add(c);
                break;
            case 'w': //words [a-zA-Z_]
                for (char c='a'; c<='z'; ++c)
                    characters.add(c);
                for (char c='A'; c<='Z'; ++c)
                    characters.add(c);
                characters.add('_');
                break;
            case 's': //white space
                characters.add('\n');
                characters.add('\r');
                characters.add('\t');
                characters.add(' ');
                break;
            case 'n': //linefeed
                characters.add('\n');
                break;
            case 'r': //carriage return
                characters.add('\r');
                break;
            case 't': //tab
                characters.add('\t');
                break;
            default: //anything else
                characters.add(ch);
                break;
        }
        return characters;
    }

/**
 * convert a type CHAR RegexToken to a type EXPR RegexToken
 * corresponding GRAMMER RULE:
 *      EXPR := CHAR
 * @param ch the type CHAR RegexToken
 * @param fa the fa to update
 * @return the new EXPR RegexToken
 */
    @SuppressWarnings("unchecked")
    public static RegexToken ch(RegexToken ch, SimpleFA fa) {
        if (ch == null || fa == null) return null;
        State s = fa.createState();
        State e = fa.createState();
        fa.addTransition(s, e, (Set)ch.payload);
        RegexToken expr = new RegexToken(
            RegexToken.Type.EXPR,
            Arrays.asList(new Object[] {s,e})
        );
        return expr;
    }

/**
 * do the Star operation
 * corresponding GRAMMER RULE:
 *      EXPR := EXPR STAR
 * @param expr the expression to apply the operation
 * @param fa the fa to update
 * @return the new EXPR RegexToken
 */
    public static RegexToken star(RegexToken expr, SimpleFA fa) {
        if (expr == null || fa == null) return null;
        State s = (State)((List) expr.payload).get(0);
        State e = (State)((List) expr.payload).get(1);
        State e0 = fa.createState();

        fa.addTransition(e, s);
        fa.addTransition(s, e0);

        RegexToken expr0 = new RegexToken(
            RegexToken.Type.EXPR,
            Arrays.asList(new Object[] {s, e0})
        );
        return expr0;
    }

/**
 * do the QM operation
 * corresponding GRAMMER RULE:
 *      EXPR := EXPR QM
 * @param expr the expression to apply the operation
 * @param fa the fa to update
 * @return the new EXPR RegexToken
 */
    public static RegexToken qm(RegexToken expr, SimpleFA fa) {
        if (expr == null || fa == null) return null;
        State s = (State)((List) expr.payload).get(0);
        State e = (State)((List) expr.payload).get(1);

        fa.addTransition(s, e);
        return expr;
    }

/**
 * do the BAR operation
 * corresponding GRAMMER RULE:
 *      EXPR := EXPR BAR EXPR
 * @param expr the first operator
 * @param expr0 the second operator
 * @param fa the fa to update
 * @return the new EXPR RegexToken
 */
    public static RegexToken bar(RegexToken expr,
        RegexToken expr0, SimpleFA fa) {
        if (expr == null || expr0 == null || fa == null) return null;
        State s = (State)((List)expr.payload).get(0);
        State e = (State)((List)expr.payload).get(1);
        State s0 = (State)((List)expr0.payload).get(0);
        State e0 = (State)((List)expr0.payload).get(1);
        State s1 = fa.createState();
        State e1 = fa.createState();

        fa.addTransition(s1, s);
        fa.addTransition(s1, s0);
        fa.addTransition(e, e1);
        fa.addTransition(e0, e1);
        return new RegexToken(RegexToken.Type.EXPR,
            Arrays.asList(new Object[]{s1, e1}));
    }

/**
 * do the catenation operation
 * corresponding GRAMMER RULE:
 *      EXPR := EXPR EXPR
 * @param expr the first operator
 * @param expr0 the second operator
 * @param fa the fa to update
 * @return the new EXPR RegexToken
 */
    public static RegexToken expr(RegexToken expr,
        RegexToken expr0, SimpleFA fa) {
        if (expr == null || expr0 == null || fa == null) return null;
        State s = (State)((List)expr.payload).get(0);
        State e = (State)((List)expr.payload).get(1);
        State s0 = (State)((List)expr0.payload).get(0);
        State e0 = (State)((List)expr0.payload).get(1);

        fa.addTransition(e, s0);
        RegexToken expr1 = new RegexToken(
            RegexToken.Type.EXPR,
            Arrays.asList(new Object[] {s, e0})
        );
        return expr1;
    }

/**
 * reduce a list of tokens (BARs and EXPRs) according to the grammer rules
 * @param tokens the tokens to reduce
 * @param fa the fa to update
 * @return the new EXPR RegexToken
 * @throws Exception for error type and missing operators
 */
    public static RegexToken reduce(List<RegexToken> tokens, SimpleFA fa)
        throws Exception
    {
        Stack<RegexToken> stack = new Stack<RegexToken>();

        if (tokens == null || tokens.size() == 0) {
            return null;
        }
        for (int i=0; i<tokens.size(); ++i) {
            RegexToken token = tokens.get(i);
            if (token.type != RegexToken.Type.EXPR
                && token.type != RegexToken.Type.BAR) {
                //System.out.println("TOKEN: " + token);
                throw new Exception("error expression");
            }

            if (stack.empty()
                || (token.type == RegexToken.Type.BAR)
                || (stack.peek()).type == RegexToken.Type.BAR) {
                //it's a bar, then push it onto the stack
                stack.push(token);
            } else {
                RegexToken prev = stack.pop();
                //do the catenation
                stack.push(RegexCompiler.expr(prev, token, fa));
            }
        }
        RegexToken curr = stack.pop();
        if (curr.type != RegexToken.Type.EXPR) {
            throw new Exception("missing operator for bar");
        }

        //now do the bar operations
        while (!stack.empty()) {
            if (stack.pop().type != RegexToken.Type.BAR)
                throw new Exception("missing operator for bar");
            RegexToken token = stack.pop();
            curr = RegexCompiler.bar(curr, token, fa);
        }
        return curr;
    }

/**
 * compile the regular expression to the corresponding nfa
 * @param src string represents the regular expression
 * @return the nfa represented by the regular expression
 * @throws Exception for all errors
 */
    public static SimpleFA compile(String src) throws Exception {
        Stack<RegexToken> stack = new Stack<RegexToken>();
        SimpleFA fa = new SimpleFA();
        List<RegexToken> tokens = RegexCompiler.tokenize(src);
        for (int i=0; i<tokens.size(); ++i) {
            RegexToken token = tokens.get(i);
            RegexToken top = null;
            switch (token.type) {
                case RP:
                    if (stack.empty()) {
                        throw new Exception("extra )");
                    }

                    List<RegexToken> stuff = new LinkedList<RegexToken>();
                    try {
                        RegexToken curr = stack.pop();
                        while (curr.type != RegexToken.Type.LP) {
                            stuff.add(0, curr);
                            curr = stack.pop();
                        }
                    } catch (EmptyStackException ex) {
                        throw new Exception("extra )");
                    }
                    stack.push(reduce(stuff, fa));
                    break;
                case CHAR:
                    stack.push(ch(token, fa));
                    break;
                case STAR:
                    if (stack.empty()) {
                        throw new Exception("missing operator for star");
                    }

                    top = stack.pop();
                    if (top == null || top.type != RegexToken.Type.EXPR) {
                        throw new Exception("missing operator for star");
                    }
                    stack.push(star(top, fa));
                    break;
                case QM:
                    if (stack.empty()) {
                        throw new Exception("missing operator for qm");
                    }

                    top = stack.pop();
                    if (top == null || top.type != RegexToken.Type.EXPR) {
                        throw new Exception("missing operator for qm");
                    }
                    stack.push(qm(top, fa));
                    break;
                case LP:
                case BAR:
                    stack.push(token);
                    break;
                default:
                    throw new Exception("error token");
            }
        }

        RegexToken exp;
        if (stack.size() >= 2) {
            exp = reduce(stack, fa);
        } else {
            exp = stack.get(0);
        }
        fa.setInitialState((State)((List)exp.payload).get(0));
        fa.addAcceptedState((State)((List)exp.payload).get(1));
        return fa;
    }
}
