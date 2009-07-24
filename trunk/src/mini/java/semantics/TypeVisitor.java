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
import mini.java.syntaxtree.Type;
import mini.java.syntaxtree.VarDecl;
import mini.java.syntaxtree.While;

public interface TypeVisitor {
	public Type visit(Program n);
	public Type visit(MainClass n);
	
	public Type visit(ClassDeclSimple n);
	public Type visit(ClassDeclExtends n);
	public Type visit(VarDecl n);
	public Type visit(MethodDecl n);
	public Type visit(Formal n);
	public Type visit(IntArrayType n);
	public Type visit(BooleanType n);
	public Type visit(IntegerType n);
	public Type visit(IdentifierType n);
	
	public Type visit(Identifier n);
	
	public Type visit(Block n);
	public Type visit(Assign n);
	public Type visit(ArrayAssign n);
	public Type visit(Print n);
	public Type visit(If n);
	public Type visit(While n);
	
	public Type visit(And n);
	public Type visit(LessThan n);
	public Type visit(Plus n);
	public Type visit(Minus n);
	public Type visit(Times n);
	public Type visit(ArrayLookup n);
	public Type visit(ArrayLength n);
	public Type visit(Call n);
	public Type visit(IntegerLiteral n);
	public Type visit(True n);
	public Type visit(False n);
	public Type visit(IdentifierExp n);
	public Type visit(This n);
	public Type visit(NewArray n);
	public Type visit(NewObject n);
	public Type visit(Not n);
	public Type visit(PrioExp n);
}