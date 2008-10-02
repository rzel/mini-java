package mini.java.syntax.legacy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import mini.java.fa.State;
import mini.java.lex.legacy.TokenRevamped;
import mini.java.lex.legacy.TokenType;

public class Algorithm {
	
	public static AnalysisTable closureSet(List<Rule> rules) {
		AnalysisTable table = new AnalysisTable();
                //for (Rule r : rules)
                //    //System.out.println(r);
	
                // closure is certainly a set;
                // What is result used for? Why can't List<Item> be
                // merged together?
		List<List<Item>> result = new ArrayList<List<Item>>();
		List<Item> firstClosure= new ArrayList<Item>();
		firstClosure.add(new Item(rules.get(0)));
		result.add(firstClosure);
		int num =0;
		
		while(num < result.size() ){
			List<Item> currentClosure = closure(result.get(num), rules);
                        //System.out.println("currentClosure " + currentClosure);
			result.set(num, currentClosure);
			
			//HashMap <Token, List<Item>> hm = new HashMap<Token, List<Item>>();
			//HashMap <Symbol, List<Item>> hm = new HashMap<Symbol, List<Item>>();
			HashMap <SymbolType, List<Item>> hm = new HashMap<SymbolType, List<Item>>();
			List<List<Item>> tmpList = new ArrayList<List<Item>>();
			List<Item> tmpList2;
			//int idx = 0;
			
			
			for(int i =0; i< currentClosure.size(); i++){
				Item tmp = currentClosure.get(i);
                                //System.out.println("Item tmp [" + tmp + "]");
				
				//Token t = new Token(tmp.getFollow());
				SymbolType  t = tmp.getFollow();
                                //System.out.println("SymbolType  t = " + t);
				if(t!=null){
					if(!(hm.containsKey(t))){
						tmpList2= new ArrayList<Item>();
						tmpList2.add(tmp.move());
						hm.put(t, tmpList2);
						tmpList.add(tmpList2);
					}else{
						tmpList2 = hm.get(t);
						tmpList2.add(tmp.move());
					}
				}else{
					
					//State state = new State(num);
				    State state = new State();
					//Pair<State, Token> reducePair = new Pair<State, Token>(state, Token.END_TOKEN);
					// Hey, I don't know that this Token.END_TOKEN all about.., any ideas?
					Pair<State, SymbolType> reducePair = new Pair<State, SymbolType>(state, new /*SymbolType*/Terminal("END"));
					Rule rule = tmp.getRule();		
                                        //System.out.println("Rule rule = " +rule);
					
					//if(rule.getLhs().equals(Token.START_TOKEN)){
					if (rule.getLhs().getRep().equals("START")) {// a little hack, just to make it working
						Action accptAction = new Action(Action.ACTION_ACCT,rule);
						table.put(reducePair, accptAction);
					}else{
						Action reduceAction = new Action(Action.ACTION_REDUCE, rule);
						table.put(reducePair, reduceAction);
						//List<Token> follows = getFollows(rule.getLhs(),rules);
						////System.out.println("getFollows started...");
						List<SymbolType> follows = getFollows(rule.getLhs(), rules);
						////System.out.println("getFollows is DONE");
						//System.out.println("GETFOLLOWS RESULT: " + follows);
						//for(Token tmp2: follows){
						for(SymbolType tmp2: follows) {
                                                        //System.out.println("SymbolType tmp2" + tmp2);
							Pair<State, SymbolType> tmpPairs = new Pair<State, SymbolType>(
                                                                state, /*new SymbolType(tmp2.getRep().toString())*/tmp2);
							table.put(tmpPairs, reduceAction);
						}
						
					}
					
					
				}
				
			}
		
                        //System.out.println("FOR LOOP");
			for(int i = 0; i<tmpList.size(); i++){
				tmpList2 = tmpList.get(i);
                                //System.out.println("tmpList2 = " + tmpList2);
				int gotoIdx = isContains(tmpList2,result);
                                //System.out.println("gotoIdx = " + gotoIdx);
				if(gotoIdx <= 0){
					result.add(tmpList2);
					gotoIdx = result.size() - 1;
				}
				//Token t = tmpList2.get(0).getPrevious();
				SymbolType t = tmpList2.get(0).getPrevious();
				//State state = new State(gotoIdx);
				State state = new State();
				
				//Pair<State , Token> shiftpair = new Pair<State, Token>(new State(num), t);
				//Pair<State , SymbolType> shiftpair = new Pair<State, SymbolType>(new State(num), t);
				Pair<State, SymbolType> shiftpair = new Pair<State, SymbolType>(new State(), t);
				
				 //if(!t.getType().equals(Token.NON_TERMINAL_TYPE)){
				 if(t instanceof Terminal) {
					Action shiftAction = new Action(Action.ACTION_SHIFT,state);
					table.put(shiftpair, shiftAction);
				}else{
					Action shiftAction = new Action(Action.ACTION_GOTO,state);
					table.put(shiftpair, shiftAction);
				}
				
			}
			
			
			num++;
		}
//		int gid = 0;
//		for(List<Item> itemSet : result) {
//			//System.out.println("=== ItemSet " + (gid++) + " ===");
//			for(Item item : itemSet) {
//				item.dump();
//			}
//			//System.out.println("===============================\n\n");
//		}
			
		return table;
	}
	
	//public static List<Token> getRecursiveFollows(Token token, List<Rule> rules) {
		
	//}
	
	//public static List<Token> getFollows(Token token, List<Rule> rules){
	// What does this method used for?
	public static List<SymbolType> getFollows(SymbolType token, List<Rule> rules){
		//List<Token> list = new ArrayList<Token>();
		//List<Token> non_terminal = new ArrayList<Token>();
		//
		////System.out.println("DOING GETFOLLOWS() FOR " + token);

		List<SymbolType> list = new ArrayList<SymbolType>();
		List<SymbolType> non_terminal = new ArrayList<SymbolType>();
		for(Rule rule: rules){
                        ////System.out.println("  NOW WE ARE AT RULE: " + rule);

			//List<Token> rhs = rule.getRhs();
			List<SymbolType> rhs = rule.getRhs();
			for(int i = 0; i< rhs.size(); i++){
				
                                ////System.out.println("    NOW WE ARE AT SymbolType: " + rhs.get(i));
				if(token.equals(rhs.get(i))){
					if(i == rhs.size() - 1) { //so this is the last symbol?
                                            ////System.out.println("      HEY THIS IS THE LAST ONE! " + token);
						//List<Token> lhsFollow = getFollows(rule.getLhs(), rules);
						//
						// I think the rules should be a member field, rather than being
						// a parameter passed around again and again.
						if (rule.getLhs() != token) {
                                                    List<SymbolType> lhsFollow = getFollows(rule.getLhs(), rules);
                                                    for(int j =0 ; j< lhsFollow.size(); j++){
                                                            //Token tok = lhsFollow.get(j);
                                                            SymbolType tok = lhsFollow.get(j);
                                                            if(!list.contains(tok)){
                                                                    list.add(tok);
                                                            }
                                                    }
                                                    continue; //same as break...
                                                } else {
                                                    //TODO: What should we do here?
                                                    break;
                                                }
					}

					//Token tmp = rhs.get(i+1);
					SymbolType tmp = rhs.get(i+1); //tmp is the the SymbolType following, checking tmp
                                                                       //should be put in another method!
					//if((!tmp.getType().equals(Token.NON_TERMINAL_TYPE))){
					if(tmp instanceof Terminal){
                                            ////System.out.println("      HEY THIS IS A TERMINAL! " + tmp);
						if(!list.contains(tmp) )
							list.add(tmp);
					}else{
                                            ////System.out.println("      HEY THIS IS A NONTERMINAL! " + tmp);
                                               if (! non_terminal.contains(tmp)) { //So non_terminal is a temporay variable?
                                                ////System.out.println("        >ADDING TO NON_TERMINAL");
						non_terminal.add(tmp);
						while(non_terminal.size()!=0){
                                                        SymbolType tmp000 = non_terminal.get(0);
                                                        //System.out.println("    ****tmp000 = " + tmp000);
							for(Rule rule2: rules){
                                                                ////System.out.println("            WE ARE AT RULE2: " + rule2);
								//Token left =rule2.getLhs();
								//Token firstRight = rule2.getRhs().get(0);
								// We should skip the current rule
								if (rule2.equals(rule)) continue; 

								SymbolType left = rule2.getLhs();
                                                                SymbolType firstRight = rule2.getRhs().get(0);
                                                                //System.out.println("      ****left = " + left);
                                                                //System.out.println("      ****firstRight = " + firstRight);
								if(left.equals(tmp000)){
									//if(!firstRight.getType().equals(Token.NON_TERMINAL_TYPE)){
									if (firstRight instanceof Terminal) {
                                                                                if(!list.contains(firstRight)) {
                                                                                    ////System.out.println("                ADDING TO LIST" + firstRight);
                                                                                        list.add(firstRight);
                                                                                }
									}else{
                                                                                if(!non_terminal.contains(firstRight)) {
                                                                                    ////System.out.println("                ADDING TO NON_TERMINAL" + firstRight);
                                                                                    non_terminal.add(firstRight);
                                                                                }
									}
								}
							}
                                                        ////System.out.println("        <REMOVING FROM NON_TERMINAL");
							non_terminal.remove(tmp000);
						}
                                            }
						
					}
				}
			}
		}
		return list;
	}

        // whether tmpList2 is contained in result? why List.contains cannot be used?
	private static int isContains(List<Item> tmpList2, List<List<Item>> result){
		
		for(int i = 0 ; i < result.size(); i++){
			int times = 0;
			List<Item> tmp = result.get(i);
			for(int j =0 ; j < tmpList2.size(); j++){
				if(tmp.contains(tmpList2.get(j))){
					times++;
				}
			}
			if(times == tmpList2.size()){
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

        // item's closure is a set of items, then why passing a set of rules to closure()?
	public static List<Item> closure(Item item, List<Rule> rules) {
                //System.out.println("Entering closure()...");
                //System.out.println(rules);
		List<Item> result = new ArrayList<Item>();
                // Using stack is ok, but a queue is more common
		Stack<Item> stack = new Stack<Item>();
		stack.push(item);

		// TODO can record lhs only
		List<Rule> addedRules = new ArrayList<Rule>();
		while (!stack.isEmpty()) {
			Item i = stack.pop();
                        //System.out.println("  Examining Item i: [" + i + "]");
			if (!result.contains(i))
				result.add(i);

			//Token token = i.getFollow();
			SymbolType token = i.getFollow();
                        //System.out.println("  i.getFollow(): [" + token + "]");
			for (Rule rule : rules) {
                                //System.out.println("    Examining rule: [" + rule + "]");
                                //System.out.println("      {" + rule.getLhs() + "}.equals(" + token + ")=" + rule.getLhs().equals(token));
                                //System.out.println("      {" + rule.getLhs() + "}.getClass()=" + rule.getLhs().getClass());
                                if (token != null)
                                    //System.out.println("      {" + token + "}.getClass()=" + token.getClass());
                                // cannot use rule.getLhs().equals(token) here. Probably because of the types
				//if (rule.getLhs().getRep().equals(token.getRep()) && !addedRules.contains(rule)) {
				if (rule.getLhs().equals(token) && !addedRules.contains(rule)) {
                                        //System.out.println("    Found rule: [" + rule + "]");
					stack.push(new Item(rule, 0));
					addedRules.add(rule);
				}
			}
		}

		return result;
	}

	//public static Tree<Token> parse(
	//public static Symbol parse(
	public static Node<Pair<SymbolType, TokenRevamped>> parse(
	// HashMap<Pair<State, Token>, Action> analysisTbl,
			AnalysisTable analysisTbl, State initState, /*
														 * initial state on the
														 * stack
														 */
			List<TokenRevamped> input) {
                System.out.println(input);
                //analysisTbl.dump();

		int idx = 0;

		//Node<Token> root = new Node<Token>(new Token("Virtual", "START"));
		//Symbol root = new Symbol(new SymbolType("START"));
		// Oh.. my, I shouldn't use Pair anymore!
		// Here in fact, Node<Pair<SymbolType, TokenRevamped>> is used as Symbol, I
		// know it looks very ugly, but I've no chooose because of the poor design
		// of Node<T>.
		Node<Pair<SymbolType, TokenRevamped>> root =
                        new Node<Pair<SymbolType, TokenRevamped>>(
                                new Pair<SymbolType, TokenRevamped>(new /*SymbolType*/NonTerminal("ROOT"), null));
		Stack<State> stack = new Stack<State>();
		// In fact, we don't need to store the symbol type explictly. The symbol type
		// information can be deduced from the state and corresponding set of items.
		//
		// Oops, I missed it, sorry...
		//
		//Stack<Pair<State, SymbolType>> stack = new Stack<Pair<State, SymbolType>>();
		stack.push(initState);

                //boolean ended = false; //indicate we have added token END;
                //boolean noMoreTokens = false; //indicate whether we should get more tokens or not;
                SymbolType currentSymbolType = null; //keep track of current symbol type;
                // NO MORE currentSymbolType! We need a stack to keep track of the symbol types;
                TokenRevamped currentToken = null;
		while (true) {
                        //System.out.println(stack);
			State tos = stack.peek();
                        //System.out.println("CURRENT STATE: " + tos);

                        //
                        // TODO: We need a method to convert TOKEN to SYMBOL,
                        // or at least do the convertion explicitly
                        if (currentToken == null) {
                            // fetch new tokens
                            currentToken = (idx < input.size()) ?
                                input.get(idx) :
                                new TokenRevamped(new TokenType("END")); //virtual token, "END"
                            currentSymbolType = new Terminal(currentToken.getType().toString());
                        } else {
                            // do nothing... or make sure currentSymbolType is not null;
                            assert(currentSymbolType != null);
                        }
                        //System.out.println("CURRENT TOKEN: " + currentToken);
                        //System.out.println("CURRENT SYMBOL: " + currentSymbolType);
                             
                        Pair<State, SymbolType> pair = new Pair<State, SymbolType>(tos, currentSymbolType);
			Action action = analysisTbl.get(pair);
                        //System.out.println("  FOUND ACTION: " + action);

			if (action == null) {
                                System.err.println("currentState: " + tos);
                                System.err.println("currentToken: " + currentToken);
                                System.err.println("currentSymbolType: " + currentSymbolType);
				throw new RuntimeException("Parse Error (handling to be added)");
			} else {
				int actType = action.getType();
				State state = action.getState();
				Rule rule = action.getRule();
				if (actType == Action.ACTION_ACCT) {
					// accept
					//System.out.println("ACCEPT!");
					break;
				} else if (actType == Action.ACTION_SHIFT) {
					// shift
					//root.addChild(new Node<Token>(token));
					//Symbol symbol = new Symbol(new SymbolType(token.getType().toString()));
                                        //symbol.setToken(token);
                                        Pair<SymbolType, TokenRevamped> nodeData = new Pair<SymbolType, TokenRevamped>(
                                            /*new SymbolType(token.getType().toString())*/new Terminal(currentToken.getType().toString()), currentToken);
					//root.addChild(symbol);
					root.addChild(new Node<Pair<SymbolType, TokenRevamped>>(nodeData));

					stack.push(state);
					//stack.push(new Pair<state, currentSymbolType>);
					idx++;
                                        //noMoreTokens = false;
                                        // I decided to use currentSymbolType == null instead of noMoreTokens;
                                        //currentSymbolType = null;
                                        currentToken = null; // we've shift the token to the stack, so forget it already
				} else if (actType == Action.ACTION_REDUCE) {
                                        //System.out.println("REDUCE IT!");
					// TODO: maybe can be optimized
					// reduce & goto
					// modify tree & stack
					// //System.out.println(rule);
					//
					//Token lhs = rule.getLhs();
					//List<Token> rhs = rule.getRhs();
					//
				        //NonTerminal lhs = rule.getLhs();
				        SymbolType lhs = rule.getLhs();
					List<SymbolType> rhs = rule.getRhs();
					int numOfChildren = root.getNumberOfChildren();

					//Node<Token> tmpNode = new Node<Token>(lhs);
					//Symbol tmpNode = new Symbol();
                                        //tmpNode.setSymbolType(lhs);
                                        //tmpNode.setToken(token);
                                        // //Nonterminal symbol has no corresponding token
                                        Node<Pair<SymbolType, TokenRevamped>> tmpNode =
                                                new Node<Pair<SymbolType, TokenRevamped>>(new Pair<SymbolType, TokenRevamped>(lhs, null));

                                        //System.out.println(rule);
                                        //System.out.println(stack);
                                        //root.dump();
                                        //System.out.println("rhs LEN: " + rule.getRhsLen());
                                        //System.out.println("root.numOfChildren: " + numOfChildren);
					for (int i = 0; i < rule.getRhsLen(); i++) {
						stack.pop();
						//Token t = rhs.get(i);
						SymbolType t = rhs.get(i);
						/* reduction 1: Generate rule subtree */
						//Node<Token> toInsert = root.removeChildAt(numOfChildren
						//		- i - 1);
						//Symbol toInsert = root.removeChildAt(numOfChildren - i - 1);
						Node<Pair<SymbolType, TokenRevamped>> toInsert = root.removeChildAt(numOfChildren -i -1);
                                                //System.out.println("  I: " + i);
                                                //System.out.println("  toInsert: " + toInsert);
						tmpNode.insertChildAt(0, toInsert);
					}
					/* reduction 2: Append the rule subtree */
					root.addChild(tmpNode);
                                        //currentSymbolType = lhs;
                                        //noMoreTokens = true;
					tos = stack.peek();
                                        //Pair<State, SymbolType> oncePairForTos = stack.peek();
                                        //tos = oncePairForTos.first;

                                        //System.out.println("  ONCE--CURRENT STATE: " + tos);
                                        //System.out.println("  ONCE--CURRENT SYMBOL: " + lhs);
					Action gotoAct = analysisTbl.get(new Pair<State, SymbolType>(tos, lhs));
                                        //System.out.println("  FOUND ACTION: " + gotoAct);
					if (gotoAct==null || gotoAct.getType() != Action.ACTION_GOTO)
						throw new RuntimeException("Action type should be GOTO");
					stack.push(gotoAct.getState());
                                        //currentSymbolType = lhs;
                                        //while ((gotoAct=analysisTbl.get(new Pair<State, SymbolType>(stack.peek(), lhs))) != null) {
                                        //    stack.push(gotoAct.getState());
                                        //    //currentSymbolType = lhs;
                                        //}
                                        //currentSymbolType = gotoAct.getRule().getLhs();
                                        //noMoreTokens = true;

                                        
				} else if (actType == Action.ACTION_GOTO) {
                                // Hey man!, you need to deal with multiple GOTOs in a row!
                                // WIth the help of currentSymbolType, it can be done simply by adding a new branch..
                                //
                                // Oh, shit. Forget about the new branch. Maybe we should use a while loop..
                                // No, we should do it once after reduction. And then add a new branch anyway.
                                //
                                        //System.out.println(stack);
					//tos = stack.peek();
					//Action gotoAct = analysisTbl.get(new Pair<State, Token>(
					//		tos, lhs));
					//Action gotoAct = analysisTbl.get(new Pair<State, SymbolType>(tos, lhs));
                                        //System.out.println("  FOUND ACTION: " + gotoAct);
					//if (gotoAct.getType() != Action.ACTION_GOTO)
					//	throw new RuntimeException("Action type should be GOTO");
					//stack.push(gotoAct.getState());
					//stack.push(action.getState());
                                        //currentSymbolType = lhs;
                                        //noMoreTokens = false;
                                    throw new RuntimeException("Parse Error (handling to be added)");
				} else {
                                // ..and don't forget the defualt one
                                    throw new RuntimeException("Parse Error (handling to be added)");
                                }
			}
			// root.dump();
		}

		//Tree<Token> ast = new Tree<Token>(root);
		//return ast;
		return root;
	}
}
