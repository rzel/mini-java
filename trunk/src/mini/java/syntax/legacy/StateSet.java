package mini.java.syntax.legacy;
import java.util.*;
public class StateSet {
	private List<List<Item>> set;
	private boolean changed;
	public StateSet(){
		set = new ArrayList<List<Item>>();
		changed = false;
	}
	public int insertState(List<Item> state){
		for(int i = 0; i< set.size();i++){
			if(set.get(i).equals(state)){
				changed = false;
				return i;
			}
		}
		set.add(state);
		changed = true;
		return set.size()-1;
	}
	
	public boolean isChanged(){
		return changed;
	}
	public List<Item> getState(int state){
		return set.get(state);
	}
	
	public int getSize(){
		return set.size();
	}
	public void printStateSet(){
		
		System.out.println("-------------------------------------------------------------------------------------------");
		System.out.print("State set  ");
		int linenum=0;
		for(Iterator<List<Item>> it2 = set.iterator();it2.hasNext();){
			System.out.println(linenum+" ");
			linenum++;
			for(Iterator<Item> it3 = it2.next().iterator();it3.hasNext();){
				Item tmp = it3.next();
//				tmp.dump();
			}
			System.out.println();
			System.out.println("-------------------------------------------------------------------------------------------");
		}
	}
}
