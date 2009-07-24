package mini.java.syntax.legacy;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
public class Parser {
	private  List<Rule> productions; 
	private static Parser parser;
	private AnalysisTable table;
	private StateSet stateSet;
	private TransitionSet transitionSet;
	private List<TokenSpec> auxList;
	private ErrorManager errManager;
	private Parser(){
		productions = new ArrayList<Rule>();
		table = new AnalysisTable();
		stateSet = new StateSet();
		transitionSet = new TransitionSet();
		auxList = new ArrayList<TokenSpec>();
		errManager = ErrorManager.getInstance();
	}
	public static Parser getInstance(){
		if(parser == null){
			parser = new Parser();
			return parser;
		}
		return parser;
	}
	public  void initProductions(String filename){
		System.out.println("init spec file:	"+filename);
		int index = 0;
		try{
			BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
			String line;
			
			while(!(line = reader.readLine() ).equals("EOF")){
				index ++;
				if(line.equals("")){
					continue;
				}
				if(line.charAt(0) == '#'){
					continue;
				}
				String[] production = line.split(":");
				String left = production[0];
				//System.out.print("[debug] production: "+ production[0]);
				//System.out.println(" : "+ production[1]);
				String[] right = production[1].split(" ");
				Rule rule;
				if(left.equals("S'")){
					rule= new Rule(TokenSpec.START_TOKEN);
				}else{
					rule= new Rule(new TokenSpec(TokenSpec.NON_TERMINAL_TYPE,left));
					table.addEntry(left, TokenSpec.NON_TERMINAL_TYPE);
				}
				
				for (int i =0 ; i< right.length;i++){
					//System.out.println("#####################right[i]  :"+ right[i]+" index is "+i);
					String tmp = right[i];
					if(tmp.equals("String")){
						rule.addRhsTokenSpec(new TokenSpec(TokenSpec.TERMINAL_TYPE,tmp));
						table.addEntry(tmp,TokenSpec.TERMINAL_TYPE);
						continue;
					}else if(tmp.equals("System.out.println")){
						rule.addRhsTokenSpec(new TokenSpec(TokenSpec.TERMINAL_TYPE,tmp));
						table.addEntry(tmp,TokenSpec.TERMINAL_TYPE);
						continue;
					}
					char first = tmp.charAt(0);
					if(first == 36){
						rule.addRhsTokenSpec(TokenSpec.END_TOKEN);
						table.addEntry(tmp,TokenSpec.TERMINAL_TYPE);
					}else if(first == 35){
						rule.addRhsTokenSpec(TokenSpec.EMPTY_TOKEN);
						table.addEntry(tmp,TokenSpec.TERMINAL_TYPE);
					}
					else if(first >=65 && first <=90){
						rule.addRhsTokenSpec(new TokenSpec(TokenSpec.NON_TERMINAL_TYPE,tmp));
						table.addEntry(tmp,TokenSpec.NON_TERMINAL_TYPE);
					}else{
						rule.addRhsTokenSpec(new TokenSpec(TokenSpec.TERMINAL_TYPE,tmp));
						table.addEntry(tmp,TokenSpec.TERMINAL_TYPE);
					}
				}
				productions.add(rule);
			}
		}catch(Exception e){
			//String errorInfor ="There is an error in the production defination file on the line "+index; 
			errManager.addError(new ErrorEntry(ErrorEntry.PRODUCTION_ERROR,index));
		}
	}
	
	public List<Item> closure(List<Item> items) {
		List<Item> result = new ArrayList<Item>();
		for (Item item : items) {
			List<Item> closure = closure(item);
			for (Item i : closure) {
				if (!result.contains(i))
					result.add(i);
			}
		}
		return result;
	}

	public List<Item> closure(Item item) {
		List<Item> result = new ArrayList<Item>();
		Stack<Item> stack = new Stack<Item>();
		stack.push(item);

		List<Rule> addedRules = new ArrayList<Rule>();
		while (!stack.isEmpty()) {
			Item i = stack.pop();
			if (!result.contains(i))
				result.add(i);
			if(i.hasFollow()){
				TokenSpec tokenSpec = i.getFollow();
				if(tokenSpec.getType().equals(TokenSpec.NON_TERMINAL_TYPE)){
					for (Rule rule : productions) {
						if (rule.getLhs().equals(tokenSpec) && !addedRules.contains(rule)) {
							stack.push(new Item(rule, 0));
							addedRules.add(rule);
						}
					}
				}
			}		
		}
		return result;
	}

	public List<Item> goTo(List<Item> list,TokenSpec token){
		List<Item> result = new ArrayList<Item>();
		for(Item item: list ){
			if(item.getFollow()!=null ){
				if(item.getFollow().equals(token)){
					Item newItem = item.move();
					result.add(newItem);
				}
			}
		}
		return this.closure(result);
	}
	
	private int getProductionIndex(Rule rule){
		for(int i =0 ; i< productions.size(); i++){
			if(productions.get(i).equals(rule))
				return i;
		}
		return -1;
	}
	
	public boolean nullable(TokenSpec token){
		//System.out.println(token);
		boolean flag = true;
		if(!token.getType().equals(TokenSpec.NON_TERMINAL_TYPE)){
			return false;
		}
		for(Iterator<Rule> it = productions.iterator();it.hasNext();){
			Rule rule = it.next();
			if(rule.getLhs().equals(token)){
				if(rule.nullable())
					return true;				
				List<TokenSpec> list = rule.getRhs();
				for(Iterator<TokenSpec> it2 = list.iterator();it2.hasNext();){
					TokenSpec tmp = it2.next();
					if(!(tmp.getType().equals(TokenSpec.NON_TERMINAL_TYPE))){
						flag = false;
					}else {
						if(!token.equals(tmp)){
							if(!this.nullable(tmp))
								flag = false;
						}
					}
				}
			}
			
		}
		return flag;
	}
	public void testNullable(){
		System.out.println(this.nullable(productions.get(2).getLhs()));
	}
	
	
	public List<TokenSpec> getFollows(TokenSpec token){
		List<TokenSpec> list = new ArrayList<TokenSpec>();
		List<TokenSpec> non_terminal = new ArrayList<TokenSpec>();
		for(Rule rule: productions){
			List<TokenSpec> rhs = rule.getRhs();
			for(int i = 0; i< rhs.size(); i++){
				
				if(token.equals(rhs.get(i))){
					if(i == rhs.size() - 1) {
						TokenSpec current = rule.getLhs();
						if(!auxList.contains(current)){
							auxList.add(current);
							List<TokenSpec> lhsFollow = getFollows(current);
							for(int j =0 ; j< lhsFollow.size(); j++){
								TokenSpec tok = lhsFollow.get(j);
								if(!list.contains(tok)){
									list.add(tok);
								}
							}
							continue;
						}
						
					}else{
						TokenSpec tmp = rhs.get(i+1);
						if((!tmp.getType().equals(TokenSpec.NON_TERMINAL_TYPE))){
							if(!list.contains(tmp) )
								list.add(tmp);
						}else{
							if(!non_terminal.contains(tmp))
								non_terminal.add(tmp);
							
							
						}
					}
				}
			}
		}
		for(int i = 0; i<non_terminal.size();i++){
			TokenSpec tmp = non_terminal.get(i);
			for(Rule rule2: productions){
				TokenSpec left =rule2.getLhs();
				TokenSpec firstRight = rule2.getRhs().get(0);
				if(left.equals(tmp)){
					if(!firstRight.getType().equals(TokenSpec.NON_TERMINAL_TYPE)){
						list.add(firstRight);
					}else{
						if(!non_terminal.contains(firstRight))
							non_terminal.add(firstRight);
					}
				}
			}
			
		}
		return list;
	}

	
	public AnalysisTable buildAnalysisTable(){
		List<Item> init = this.closure(new Item(parser.getProduction(0),0));
		stateSet.insertState(init);
		int i;
		for(i=0;i<stateSet.getSize();i++){
			List<Item> list = stateSet.getState(i);
			table.insertNewLine();
			for(Item item:list){
				Rule rule = item.getRule();
				int t =0;
				int index = this.getProductionIndex(rule);
				if(item.hasFollow()){
					TokenSpec token = item.getFollow();
					if(token.equals(TokenSpec.END_TOKEN)){
						t=table.updateLine(i, token.getText(), new TableItem("acc","",index));
					}else{
						List<Item> newState = this.goTo(list, token);
						int next=stateSet.insertState(newState);
						transitionSet.insertSet(i,next,token);						
						TableItem flag ;
						if(token.getType().equals(TokenSpec.NON_TERMINAL_TYPE)){
							flag = new TableItem("g",new Integer(next).toString(),index);
							
						}else{
							flag = new TableItem("s",new Integer(next).toString(),index);
						}
						t=table.updateLine(i, token.getText(), flag);
						
					}
				}else{
					//Rule rule = item.getRule();
					//int index = this.getProductionIndex(rule);
					auxList.clear();
					List<TokenSpec> follows = getFollows(rule.getLhs());
					for(Iterator<TokenSpec> it = follows.iterator();it.hasNext();){
						TokenSpec token = it.next();
						TableItem flag = new TableItem("r",new Integer(index).toString(),index);
						t = table.updateLine(i, token.getText(), flag);
					}
				}
				if(t != index){
					//String errorInfo = "Ambigous productions according to ";
					
					errManager.addError(new ErrorEntry(ErrorEntry.AMBIGUOUS_GRAMMAR,productions.get(t),productions.get(index)));
				}
			}
		}
		return table;
	}
	
	public Tree<Symbol> buildAST(List<TokenRevamped> tokenStream){
		//Stack<Token> tokenStack = new Stack<Token>();
		Stack<Integer> stateStack = new Stack<Integer>();
		Node<Symbol> root = new Node<Symbol>(new Symbol(new NonTerminal("Root")));
		
		int line= 0;
		stateStack.push(0);
		for(Iterator<TokenRevamped> it = tokenStream.iterator();it.hasNext();){
		    TokenRevamped current = it.next();
			String currentType=current.getType().getRep();
			if(currentType.equals("UNKNOWN_TOKEN")){
				//System.out.println("********************  UNKNOWN TOKEN");
				continue;
			}
			boolean flag = true;
			while(flag){
				//System.out.println("[Running]: "+ line);
				//System.out.println("[Running]: "+ currentType);
				TableItem entry = table.readTable(line, currentType);
				String action = entry.getAction();
				
				if(action.equals("0")){
					//TODO error handling
					System.out.println("[Error]: "+ line);
					System.out.println("[Error]: "+ currentType);
					//String errorInfo = "There is a syntax error: Toke is "+ current.getText()+" line "+ current.getLineNum()+" column is " +current.getColumn();
					ErrorEntry errorentry = new ErrorEntry(ErrorEntry.SYNTAX_ERROR,current);
					errManager.addError(errorentry);
					flag = false;
				}else if(action.equals("acc")){
					System.out.println("**************************Accept***********************************");
					flag = false;
					break;
				}else{
					int state = Integer.parseInt(entry.getState());
					if(action.equals("s")){
						//tokenStack.add(current);
						stateStack.add(state);
						line = state;
						
						root.addChild(new Node<Symbol>(new Symbol(new Terminal(current.getType().getRep()),
						        current)));
						flag = false;
						//System.out.println("[shift]: "+ line);
						//System.out.println("[shift]: "+ currentType);
					}else if(action.equals("r")){
						Rule rule=productions.get(state);
						int rhLen = rule.getRhsLen();				
						//System.out.println("[reduce]: "+ line);
						//System.out.println("[reduce]: "+ currentType);
						//remove some node from root to tmpNode
						int numOfChildren = root.getNumberOfChildren();
						Node<Symbol> tmpNode = new Node<Symbol>();
						
						for(int i =0 ;i < rhLen;i++){
							stateStack.pop();											
							//tokenStack.pop();
							Node<Symbol> toInsert = root.removeChildAt(numOfChildren- i - 1);
							tmpNode.insertChildAt(0, toInsert);
						}
						
						//new a non terminal token and add the tmpNode to the root					
						Symbol leftmost = tmpNode.getChildren().get(0).getData();
						String leftTokenText = rule.getLhs().getText();
//						int lineNum = leftmost.getLineNum();
//						int column = leftmost.getColumn();
//						TokenRevamped nonTerminal = new TokenRevamped(Token.NON_TERMINAL_TYPE,leftTokenText,lineNum,column);
						Symbol nonTerminal = new Symbol(new NonTerminal("XXX"));
						
						//tokenStack.add(nonTerminal);
						tmpNode.setData(nonTerminal);
						root.addChild(tmpNode);
						
						//get GOTO line number
						line = stateStack.peek();
						TableItem goTo = table.readTable(line, leftTokenText);
						line = Integer.parseInt(goTo.getState());
						stateStack.add(line);
						//System.out.println("[goto]: "+ line);
						//System.out.println("[goto]: "+ currentType);
					}
				}
			}
		}
		Tree<Symbol> ast = new Tree<Symbol>(root);
		return ast;
	}
	public AnalysisTable getAnalysisTable(){
		return table;
	}
	public Rule getProduction(int i){
		return productions.get(i);
	}
	public StateSet getStateSet(){
		return stateSet;
	}
	
	public TransitionSet getTransitionSet(){
		return transitionSet;
	}
	public ErrorManager getErrorList(){
		return errManager;
	}
	public int getRuleID(Rule rule){
		int r=-1;
		for(int i = 0; i< productions.size();i++){
			if(productions.get(i).equals(rule)){
				r=i;
			}
		}
		return r;
	}
	public  void testProductions(){
		for(Iterator<Rule> it = productions.iterator(); it .hasNext(); ){
			Rule rule = it.next();
			System.out.println(rule);
		}
	}
	public void testFollow(){
		
		System.out.println("----------------test follows---------------------");
		
		TokenSpec token2 = productions.get(3).getLhs();
		auxList.clear();
		List<TokenSpec> list2 = this.getFollows(token2);
		System.out.println("**************Token "+token2);
		for(Iterator<TokenSpec> it = list2.iterator();it.hasNext();){
			System.out.println(it.next());
		}
		System.out.println("----------------test follows---------------------");
		
	}
}
