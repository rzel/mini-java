package mini.java.syntax.legacy;
import java.util.*;
public class AnalysisTable {
	private List<List<TableItem>> table;
	private List<String> entry;
	
	public AnalysisTable(){
		table = new ArrayList<List<TableItem>>();
		entry = new ArrayList<String>();
	}
	
	public void addEntry(String text, String type){
		if(!entry.contains(text)){
			if(type.equals(TokenSpec.NON_TERMINAL_TYPE)){
				entry.add(text);
			}else{
			entry.add(0, text);
			}
		}
		
	}
	public int insertNewLine(){
		List<TableItem> line = new ArrayList<TableItem>();
		for(int i =0 ; i< entry.size(); i++){
			line.add(new TableItem("0","0",-1));
		}
		table.add(line);
		return table.size()-1;
	}
	public int updateLine(int lineNum, String token, TableItem flag){
		int index =0;
		for(int i=0;i<entry.size();i++){
			if(token.equals(entry.get(i)))
				index =i;
		}
		TableItem tmp = table.get(lineNum).get(index);
		if(tmp.getAction().equals("0")){
			table.get(lineNum).set(index, flag);
			//System.out.println("******************************************");
			return flag.getRuleID();
		}
		else{
			if(!tmp.getAction().equals(flag.getAction())){
				//System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
				return tmp.getRuleID();
			}else{
				return flag.getRuleID();
			}
			
		}
	}
	
	public TableItem readTable(int line, String token){
		int index = 0;
		for(int i=0;i<entry.size();i++){
			if(token.equals(entry.get(i)))
				index =i;
		}
		TableItem tmp = table.get(line).get(index);
		return tmp;
	}
	public void printTable(){
		System.out.print("State");
		for(Iterator<String> it = entry.iterator();it.hasNext();){
			String tmp = it.next();
			System.out.print("	"+tmp);
		}
		System.out.println();
		System.out.println("-------------------------------------------------------------------------------------------");
		int linenum=0;
		for(Iterator<List<TableItem>> it2 = table.iterator();it2.hasNext();){
			System.out.print(linenum+" ");
			linenum++;
			for(Iterator<TableItem> it3 = it2.next().iterator();it3.hasNext();){
				TableItem tmp = it3.next();
				System.out.print("	"+tmp);
			}
			System.out.println();
			System.out.println("-------------------------------------------------------------------------------------------");
		}
	}
}
