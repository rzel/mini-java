package syntax.legacy;
import java.util.*;

import fa.legacy.State;
import syntax.SymbolType;
//import lex.legacy.SymbolType;

public class AnalysisTable extends HashMap<Pair<State, SymbolType>, Action> {
	public void dump() {
		for(Iterator<Pair<State, SymbolType>> iter = this.keySet().iterator();
			iter.hasNext();) {
			
			Pair<State, SymbolType> pair = iter.next();
			Action action = this.get(pair);
			
                        System.out.println("<" + pair.toString() + "><" + action +">");
			//System.out.println(" Key: <" + pair.first + ", " + pair.second + ">\t Value: " + action);
			//System.out.println(" Key: <" + pair.first.getClass() + ", " + pair.second.getClass + ">");
			//System.out.println();
		}
	}
    //public String toString() {
    //}
}

