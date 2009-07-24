package mini.java.semantics;

import java.util.List;

import mini.java.syntaxtree.And;
import mini.java.syntaxtree.ArrayAssign;
import mini.java.syntaxtree.ArrayLength;
import mini.java.syntaxtree.ArrayLookup;
import mini.java.syntaxtree.Assign;
import mini.java.syntaxtree.Block;
import mini.java.syntaxtree.BooleanType;
import mini.java.syntaxtree.Call;
import mini.java.syntaxtree.ClassDeclExtends;
import mini.java.syntaxtree.ClassDeclSimple;
import mini.java.syntaxtree.False;
import mini.java.syntaxtree.Formal;
import mini.java.syntaxtree.Identifier;
import mini.java.syntaxtree.IdentifierExp;
import mini.java.syntaxtree.IdentifierType;
import mini.java.syntaxtree.If;
import mini.java.syntaxtree.IntArrayType;
import mini.java.syntaxtree.IntegerLiteral;
import mini.java.syntaxtree.IntegerType;
import mini.java.syntaxtree.LessThan;
import mini.java.syntaxtree.MainClass;
import mini.java.syntaxtree.MethodDecl;
import mini.java.syntaxtree.Minus;
import mini.java.syntaxtree.NewArray;
import mini.java.syntaxtree.NewObject;
import mini.java.syntaxtree.Not;
import mini.java.syntaxtree.Plus;
import mini.java.syntaxtree.Print;
import mini.java.syntaxtree.PrioExp;
import mini.java.syntaxtree.Program;
import mini.java.syntaxtree.This;
import mini.java.syntaxtree.Times;
import mini.java.syntaxtree.True;
import mini.java.syntaxtree.VarDecl;
import mini.java.syntaxtree.While;

public class SymbolTableVisitor implements Visitor {

	private SymbolTable symtab;
	private List<ErrorMsg> errors;
	
	public SymbolTableVisitor(SymbolTable symtab, List<ErrorMsg> errors) {
		this.symtab = symtab;
		this.errors = errors;		 
	}
	
	public void addSymtabEntry(Symbol s, Record r, int line) {
		try {
			symtab.put(s, r);		
		} catch(Exception ex) {
			String sym = s.toString();
			if(sym.startsWith(":")) {
				sym = sym.substring(1);				
			}
			String errMsg = "Line " + line + ": " + sym + " is already defined";
			errors.add(new ErrorMsg(errMsg, line));
		}
	}
	
	@Override
	public void visit(Program n) {
		// TODO Auto-generated method stub
		symtab.resetTable();
		symtab.enterScope();
			n.m.accept(this);
			for(int i = 0; i < n.cl.size(); i++) {
				n.cl.elementAt(i).accept(this);
			}
		symtab.exitScope();
	}

	@Override
	public void visit(MainClass n) {
		// TODO Auto-generated method stub
		Symbol s = Symbol.getSymbol(n.i1.s);
		Record r = new Record(s, new IdentifierType(s.toString()));
		addSymtabEntry(s, r, n.i1.getLine());
		
		
		// TODO: special measure specific to main
		symtab.enterScope();
			// put "this" pointer into symbol table
			Symbol thisSymbol = Symbol.getSymbol("this");
			Record thisRecord = new Record(thisSymbol, new IdentifierType(n.i1.s));
			addSymtabEntry(thisSymbol, thisRecord, n.i1.getLine());
			
			symtab.enterScope();
				// MainClass is very special since it has only one
				// method `main'. So we don't have to enter method
				// scope. method scope = class scope!
				Symbol s1 = Symbol.getSymbol(n.i2.s);
				// TODO: tmp solution. this is the only place where String[] is valid
				Record r1 = new Record(s1, new IdentifierType("StringArray"));				
				addSymtabEntry(s1, r1, n.i2.getLine());

				n.s.accept(this);
			symtab.exitScope();
		symtab.exitScope();
	}

	@Override
	public void visit(ClassDeclSimple n) {
		Symbol s = Symbol.getSymbol(n.i.s);
		Record r = new Record(s, new IdentifierType(s.toString()));
		addSymtabEntry(s, r, n.i.getLine());
		
		symtab.enterScope();
			// put "this" pointer into symbol table
			Symbol thisSymbol = Symbol.getSymbol("this");
			Record thisRecord = new Record(thisSymbol, new IdentifierType(n.i.s));
			addSymtabEntry(thisSymbol, thisRecord, n.i.getLine());
			
			for(int i = 0; i < n.vl.size(); i++) {
				n.vl.elementAt(i).accept(this);
			}
			for(int i = 0; i < n.ml.size(); i++) {
				n.ml.elementAt(i).accept(this);
			}
		symtab.exitScope();
	}

	@Override
	public void visit(ClassDeclExtends n) {
		Symbol s = Symbol.getSymbol(n.i.s);
		Record r = new Record(s, new IdentifierType(s.toString()));
		addSymtabEntry(s, r, n.i.getLine());
		
		symtab.enterScope();
			// put "this" pointer into symbol table
			Symbol thisSymbol = Symbol.getSymbol("this");
			Record thisRecord = new Record(thisSymbol, new IdentifierType(n.i.s));

			addSymtabEntry(thisSymbol, thisRecord, n.i.getLine());
			
			for(int i = 0; i < n.vl.size(); i++) {
				n.vl.elementAt(i).accept(this);
			}
			for(int i = 0; i < n.ml.size(); i++) {
				n.ml.elementAt(i).accept(this);
			}
		symtab.exitScope();
	}

	@Override
	public void visit(VarDecl n) {
		// TODO n modified?
		// n.i.s = "v:" + n.i.s;
		System.err.println("DEBUG: vardecl=" + n);
		Symbol s = Symbol.getSymbol(n.i.s);
		Record r = new Record(s, n.t);
		addSymtabEntry(s, r, n.i.getLine());
	}

	@Override
	public void visit(MethodDecl n) {
		// TODO n modified?
		//n.i.s = "m:" + n.i.s;
		Symbol s = Symbol.getSymbol(":" + n.i.s);
		Record r = new Record(s, n.t);
		addSymtabEntry(s, r, n.i.getLine());
		symtab.enterScope();
			for(int i = 0; i < n.fl.size(); i++) {
				n.fl.elementAt(i).accept(this);
			}
			for(int i = 0; i < n.vl.size(); i++) {
				n.vl.elementAt(i).accept(this);
			}			
		symtab.exitScope();
	}

	@Override
	public void visit(Formal n) {
		// TODO modify n?
		//n.i.s = "p:" + n.i.s;
		System.err.println("DEBUG: formal=" + n);
		Symbol s = Symbol.getSymbol(n.i.s);
		Record r = new Record(s, n.t);
		addSymtabEntry(s, r, n.i.getLine());
	}

	@Override
	public void visit(IntArrayType n) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(BooleanType n) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(IntegerType n) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(IdentifierType n) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Identifier n) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(Block n) {
		// TODO Auto-generated method stub
		symtab.enterScope();
			for(int i = 0; i < n.sl.size(); i++) {
				n.sl.elementAt(i).accept(this);
			}
		symtab.exitScope();
	}

	@Override
	public void visit(Assign n) {
		// TODO Auto-generated method stub
		n.e.accept(this);
		n.i.accept(this);
	}

	@Override
	public void visit(ArrayAssign n) {
		// TODO Auto-generated method stub
		n.e1.accept(this);
		n.e2.accept(this);
	}

	@Override
	public void visit(Print n) {
		// TODO Auto-generated method stub
		n.e.accept(this);
	}

	@Override
	public void visit(If n) {
		// TODO Auto-generated method stub
		n.e.accept(this);
		n.s1.accept(this);
		n.s2.accept(this);
	}

	@Override
	public void visit(While n) {
		// TODO Auto-generated method stub
		n.e.accept(this);
		n.s.accept(this);
	}

	@Override
	public void visit(And n) {
		// TODO Auto-generated method stub
		n.e1.accept(this);
		n.e2.accept(this);
	}

	@Override
	public void visit(LessThan n) {
		// TODO Auto-generated method stub
		n.e1.accept(this);
		n.e2.accept(this);
	}

	@Override
	public void visit(Plus n) {
		// TODO Auto-generated method stub
		n.e1.accept(this);
		n.e2.accept(this);
	}

	@Override
	public void visit(Minus n) {
		// TODO Auto-generated method stub
		n.e1.accept(this);
		n.e2.accept(this);
	}

	@Override
	public void visit(Times n) {
		// TODO Auto-generated method stub
		n.e1.accept(this);
		n.e2.accept(this);
	}

	@Override
	public void visit(ArrayLookup n) {
		// TODO Auto-generated method stub
		n.e1.accept(this);
		n.e2.accept(this);
	}

	@Override
	public void visit(ArrayLength n) {
		// TODO Auto-generated method stub
		n.e.accept(this);
	}

	@Override
	public void visit(Call n) {
		// TODO Auto-generated method stub
		n.e.accept(this);
		n.i.accept(this);
		for(int i = 0; i < n.el.size(); i++) {
			n.el.elementAt(i).accept(this);
		}
	}

	@Override
	public void visit(IntegerLiteral n) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(True n) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(False n) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(IdentifierExp n) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(This n) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(NewArray n) {
		// TODO Auto-generated method stub
		n.e.accept(this);
	}

	@Override
	public void visit(NewObject n) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void visit(Not n) {
		// TODO Auto-generated method stub
		n.e.accept(this);
	}

	@Override
	public void visit(PrioExp n) {
		// TODO Auto-generated method stub
		n.e.accept(this);
	}

}
