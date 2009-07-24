package mini.java.syntaxtree;

import java.util.Vector;

public class ExpList {
   private Vector<Exp> list;

   public ExpList() {
      list = new Vector<Exp>();
   }

   public void addElement(Exp n) {
      list.addElement(n);
   }

   public Exp elementAt(int i)  { 
      return list.elementAt(i); 
   }

   public int size() { 
      return list.size(); 
   }
   
   public String toString() {
	   if(list.isEmpty()) return "";
	   
	   Exp e0 = list.get(0);
	   StringBuffer sb = new StringBuffer(e0.toString());
	   for(int i = 1; i < list.size(); i++) {
		   sb.append(", " + list.get(i).toString());
	   }
	   
	   return sb.toString();
   }
}
