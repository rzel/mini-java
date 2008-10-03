package mini.java.syntax.legacy;
import java.util.*;
public class ErrorManager {
	private List<ErrorEntry> errorList;
	private static ErrorManager manager;
	private ErrorManager(){
		errorList = new ArrayList<ErrorEntry>();
	}
	public static ErrorManager getInstance(){
		if(manager  == null){
			manager = new ErrorManager();
		}
		return manager;
	}
	public List<ErrorEntry> getErrorList(){
		return errorList;
	}
	public void addError(ErrorEntry entry){
		errorList.add(entry);
	}
	public void printAllErrorList(){
		for(Iterator<ErrorEntry> it = errorList.iterator();it.hasNext();){
			System.out.println(it.next().toString());
		}
	}
}
