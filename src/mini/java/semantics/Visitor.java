package mini.java.semantics;

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

public interface Visitor {
	public void visit(Program n);
	// MainClass
	public void visit(MainClass n);
	// ClassDecl	
	public void visit(ClassDeclSimple n);
	public void visit(ClassDeclExtends n);
	public void visit(VarDecl n);
	public void visit(MethodDecl n);
	public void visit(Formal n);
	public void visit(IntArrayType n);
	public void visit(BooleanType n);
	public void visit(IntegerType n);
	public void visit(IdentifierType n);
	public void visit(Identifier n);
	
	// Statement
	public void visit(Block n);
	public void visit(Assign n);
	public void visit(ArrayAssign n);
	public void visit(Print n);
	public void visit(If n);
	public void visit(While n);
		
	public void visit(And n);
	public void visit(LessThan n);
	public void visit(Plus n);
	public void visit(Minus n);
	public void visit(Times n);
	public void visit(ArrayLookup n);
	public void visit(ArrayLength n);
	public void visit(Call n);
	public void visit(IntegerLiteral n);
	public void visit(True n);
	public void visit(False n);
	public void visit(IdentifierExp n);
	public void visit(This n);
	public void visit(NewArray n);
	public void visit(NewObject n);
	public void visit(Not n);
	public void visit(PrioExp n);
}