package mini.java.syntax.legacy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
public class TransitionSet {
	private List<Transition> list;
	private boolean changed;
	public TransitionSet(){
		list = new ArrayList<Transition>();
		changed = false;
	}
	public void insertSet(int left,int right,TokenSpec token){
		Transition transition = new Transition(left, right, token);	
		changed = true;
		for(int i = 0; i< list.size();i++){
			if(list.get(i).equals(transition)){
				changed = false;
			}
		}
		if(changed == true){
			list.add(transition);
		}
	}
	public boolean isChanged(){
		return changed;
	}
	public int nextState(int left,TokenSpec token){
		for(int i = 0; i< list.size();i++){
			if(list.get(i).getLeft() == left){
				if(list.get(i).getToken().equals(token)){
					return list.get(i).getRight();
				}
			}
		}
		return -1;
	}
	
	public void printTransitionSet(){
		System.out.println("Transition Set");
		for(Iterator<Transition> it = list.iterator();it.hasNext();){
			System.out.println(it.next());
		}
	}
}
