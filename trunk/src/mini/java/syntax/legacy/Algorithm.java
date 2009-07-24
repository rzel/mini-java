package mini.java.syntax.legacy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;


public class Algorithm {
    // XXX - this algorithm cannot handle rule sets with multiple "START" rules
    public static Map<Pair<State, SymbolType>, Action> closureSet(
            List<Rule> rules) {
        // Pair<State, SymbolType> -- current state and current input
        Map<Pair<State, SymbolType>, Action> table = new HashMap<Pair<State, SymbolType>, Action>();
        // List<Item> is for closures; result is a "set" of closures
        List<List<Item>> result = new ArrayList<List<Item>>();

        List<Item> firstClosure = new ArrayList<Item>();
        firstClosure.add(new Item(rules.get(0)));
        result.add(firstClosure);
        // num - "id" or the head of the queue
        int num = 0;

        while (num < result.size()) {
            // 1. get a closure from the queue
            List<Item> currentClosure = closure(result.get(num), rules);
            result.set(num, currentClosure);

            // hm - input(symbol) ==> items(possible moves)
            HashMap<SymbolType, List<Item>> hm = new HashMap<SymbolType, List<Item>>();
            // tmpList - the "next" closure for currentClosure;
            // the same as "result" but contains duplicates
            List<List<Item>> tmpList = new ArrayList<List<Item>>();
            List<Item> tmpList2;
            // int idx = 0;

            // 2. iterate through the closure for each of the items
            for (int i = 0; i < currentClosure.size(); i++) {
                Item tmp = currentClosure.get(i);

                // 3. get the token after the "dot"
                TokenSpec t = tmp.getFollow();

                // 4. token is null? no
                // add transition(epsilon): token --> item(state)
                if (t != null) {
                    SymbolType symbol = SymbolType.createSymbol(t);
                    if (!(hm.containsKey(symbol))) {
                        tmpList2 = new ArrayList<Item>();
                        tmpList2.add(tmp.move());
                        hm.put(symbol, tmpList2);
                        tmpList.add(tmpList2);
                    } else {
                        tmpList2 = hm.get(symbol);
                        tmpList2.add(tmp.move());
                    }

                }
                // ...yes;
                // add "reduce" action
                else {
                    State state = new State(num);
                    // XXX - "END" symbol should be the only lookahead for "START" rule
                    Pair<State, SymbolType> reducePair = new Pair<State, SymbolType>(
                            state, new Terminal("END")); // if this is the end of the source
                    Rule rule = tmp.getRule();
                    if (rule.getLhs().equals(TokenSpec.START_TOKEN)) {
                        Action accptAction = new Action(Action.ACTION_ACCT,
                                rule);
                        putAction(table, reducePair, accptAction);
                    } else {
                        Action reduceAction = new Action(Action.ACTION_REDUCE,
                                rule);
                        putAction(table, reducePair, reduceAction);
                        // here we peek at the follows of the lhs token (equivalent to the reduced rule)
                        // to determine whether we should reduce or shift
                        List<SymbolType> follows = getFollows(rule.getLhs(),
                                rules);
                        for (SymbolType tmp2 : follows) { // follows are all Terminals
                            Pair<State, SymbolType> tmpPairs = new Pair<State, SymbolType>(
                                    state, tmp2);
                            putAction(table, tmpPairs, reduceAction);
                        }

                    }

                }

            }

            // dealing with the transitions(epsilons)
            for (int i = 0; i < tmpList.size(); i++) {
                tmpList2 = tmpList.get(i);

                int gotoIdx = isContains(tmpList2, result);
                if (gotoIdx <= 0) { // not in "result"
                    result.add(tmpList2);
                    gotoIdx = result.size() - 1;
                }

                // getPrevious() should return the same item for everything inside
                // tmpList2, which is actually the key in "hm" for tmpList2
                TokenSpec t = tmpList2.get(0).getPrevious();
                SymbolType symbol = SymbolType.createSymbol(t);

                State state = new State(gotoIdx);
                Pair<State, SymbolType> shiftpair = new Pair<State, SymbolType>(
                        new State(num), symbol);

                if (symbol instanceof Terminal) {
                    // basically this "shift" action is simply a transition with "symbol" as input
                    Action shiftAction = new Action(Action.ACTION_SHIFT, state);
                    putAction(table, shiftpair, shiftAction);
                } else {
                    // ...here the "goto" action is for epsilons
                    Action shiftAction = new Action(Action.ACTION_GOTO, state);
                    putAction(table, shiftpair, shiftAction);
                }

            }

            num++;
        }
        return table;
    }
    
    
    private static void putAction(Map<Pair<State, SymbolType>, Action> tbl_,
            Pair<State, SymbolType> pair_, Action action_)
    {
        assert(tbl_ != null);
        assert(pair_ != null);
        assert(action_ != null);
        
        Action prev = tbl_.put(pair_, action_);
        if (prev != null) {
            throw new RuntimeException("Action being substituted: "
                    + prev + " ==> " + action_);
        }
    }
    

    // XXX - in this method we actually don't check whether the input
    // token is terminal or non-terminal...
    public static List<SymbolType> getFollows(TokenSpec token, List<Rule> rules) {
        // list -- result/ret (terminals only)
        List<SymbolType> list = new ArrayList<SymbolType>();
        // List<SymbolType> non_terminal = new ArrayList<SymbolType>();

        // stores tokens/symbols pending checking
        List<TokenSpec> todo = new LinkedList<TokenSpec>();
        Set<TokenSpec> checked = new HashSet<TokenSpec>();

        // the initial set
        todo.add(token);
        while (todo.size() > 0) {
            TokenSpec cur_token = todo.remove(0); // dequeue
            checked.add(cur_token);

            for (Rule rule : rules) {
                List<TokenSpec> rhs = rule.getRhs();
                for (int i = 0; i < rhs.size(); i++) {
                    if (cur_token.equals(rhs.get(i))) {
                        // if this is the last element, we check the whole rule instead
                        if (i == rhs.size() - 1) {
                            if (!checked.contains(rule.getLhs())) {
                                todo.add(rule.getLhs()); // enqueue
                            }
                        } else {
                            // tmp - the following symbol/token
                            TokenSpec tmp = rhs.get(i + 1);
                            // if(tmp instanceof Terminal){
                            if (tmp.getType().equals(TokenSpec.TERMINAL_TYPE)) {
                                SymbolType symbol = SymbolType.createSymbol(tmp);
                                if (!list.contains(symbol))
                                    list.add(symbol);
                            } else {
                                Item item = new Item(rule, i + 1);
                                for (Item ii : closure(item, rules)) {
                                    TokenSpec first = ii.getFollow();
                                    if (first.getType().equals(
                                            TokenSpec.TERMINAL_TYPE)) {
                                        SymbolType symbol = SymbolType.createSymbol(first);
                                        if (!list.contains(symbol)) {
                                            list.add(symbol);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    private static int isContains(List<Item> tmpList2, List<List<Item>> result) {
        for (int i = 0; i < result.size(); i++) {
            int times = 0;
            List<Item> tmp = result.get(i); // TODO - containsAll
            for (int j = 0; j < tmpList2.size(); j++) {
                if (tmp.contains(tmpList2.get(j))) {
                    times++;
                }
            }
            if (times == tmpList2.size()) {
                return i;
            }
        }
        return -1;
    }

    public static List<Item> closure(List<Item> items, List<Rule> rules) {
        List<Item> result = new ArrayList<Item>();

        for (Item item : items) {
            List<Item> closure = closure(item, rules);
            for (Item i : closure) {
                if (!result.contains(i))
                    result.add(i);
            }
        }

        return result;
    }

    public static List<Item> closure(Item item, List<Rule> rules) {
        List<Item> result = new ArrayList<Item>();
        Stack<Item> stack = new Stack<Item>();
        stack.push(item);

        List<Rule> addedRules = new ArrayList<Rule>();
        while (!stack.isEmpty()) {
            Item i = stack.pop();
            if (!result.contains(i))
                result.add(i);

            TokenSpec token = i.getFollow();
            if (token != null) {
                for (Rule rule : rules) {
                    if (rule.getLhs().equals(token)
                            && !addedRules.contains(rule)) {
                        stack.push(new Item(rule, 0));
                        addedRules.add(rule);
                    }
                }
            }
        }
        return result;
    }

    public static Node<Pair<SymbolType, Token>> parse(
            Map<Pair<State, SymbolType>, Action> analysisTbl, State initState,
            List<Token> input) {
        int idx = 0;

        Node<Pair<SymbolType, Token>> root = new Node<Pair<SymbolType, Token>>(
                new Pair<SymbolType, Token>(new NonTerminal("ROOT"),
                        null));
        Stack<State> stack = new Stack<State>();

        // XXX - initState should always be the same?
        stack.push(initState);

        boolean newTok = true; // indicate we need to get a new token from the
                                // queue
        SymbolType currentSymbolType = null;
        Token currentToken = null;
        while (true) {
            State tos = stack.peek();

            if (newTok) {
                currentToken = (idx < input.size()) ? input.get(idx)
                        : new Token("END"); // virtual token,  "END"
                currentSymbolType = new Terminal(
                        currentToken.getType().toString());
            }

            Pair<State, SymbolType> pair = new Pair<State, SymbolType>(tos,
                    currentSymbolType);
            Action action = analysisTbl.get(pair);

            if (action == null) {
                throw new IllegalArgumentException(
                        "Invalid analysis table. Missing action for [" + tos
                                + ", " + currentSymbolType);
            } else {
                int actType = action.getType();
                State state = action.getState();
                Rule rule = action.getRule();
                if (actType == Action.ACTION_ACCT) {
                    break; // we are done

                } else if (actType == Action.ACTION_SHIFT) {
                    Pair<SymbolType, Token> nodeData = new Pair<SymbolType, Token>(
                            new Terminal(currentToken.getType().toString()),
                            currentToken);
                    // XXX - here root is used as a temporary store
                    root.addChild(new Node<Pair<SymbolType, Token>>(
                            nodeData));
                    stack.push(state);
                    // this will cause a new token being read from the queue
                    idx++;
                    newTok = true;

                } else if (actType == Action.ACTION_REDUCE) {
                    TokenSpec lhs = rule.getLhs();
                    SymbolType symbol = SymbolType.createSymbol(lhs);
                    List<TokenSpec> rhs = rule.getRhs();

                    Node<Pair<SymbolType, Token>> tmpNode = new Node<Pair<SymbolType, Token>>(
                            new Pair<SymbolType, Token>(symbol, null)); // non-terminals don't have data token

                    // create a subtree with the nodes previously added as children of the root
                    int numOfChildren = root.getNumberOfChildren();
                    for (int i = 0; i < rule.getRhsLen(); i++) {
                        stack.pop();
                        TokenSpec t = rhs.get(i);

                        Node<Pair<SymbolType, Token>> toInsert = root.removeChildAt(numOfChildren
                                - i - 1);
                        tmpNode.insertChildAt(0, toInsert);
                    }

                    root.addChild(tmpNode);

                    currentSymbolType = SymbolType.createSymbol(lhs);
                    newTok = false;

                } else if (actType == Action.ACTION_GOTO) {
                    stack.push(action.getState());
                    currentSymbolType = SymbolType.createSymbol(rule.getLhs());
                    newTok = false;
                }

                throw new IllegalArgumentException("Invalid Action: " + actType);
            }
        }
        return root;
    }
}
