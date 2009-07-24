package mini.java.semantics;

import java.util.List;

import mini.java.syntax.legacy.Node;
import mini.java.syntax.legacy.Token;
import mini.java.syntaxtree.And;
import mini.java.syntaxtree.ArrayAssign;
import mini.java.syntaxtree.ArrayLength;
import mini.java.syntaxtree.ArrayLookup;
import mini.java.syntaxtree.Assign;
import mini.java.syntaxtree.Block;
import mini.java.syntaxtree.BooleanType;
import mini.java.syntaxtree.Call;
import mini.java.syntaxtree.ClassDecl;
import mini.java.syntaxtree.ClassDeclExtends;
import mini.java.syntaxtree.ClassDeclList;
import mini.java.syntaxtree.ClassDeclSimple;
import mini.java.syntaxtree.Exp;
import mini.java.syntaxtree.ExpList;
import mini.java.syntaxtree.False;
import mini.java.syntaxtree.Formal;
import mini.java.syntaxtree.FormalList;
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
import mini.java.syntaxtree.MethodDeclList;
import mini.java.syntaxtree.Minus;
import mini.java.syntaxtree.NewArray;
import mini.java.syntaxtree.NewObject;
import mini.java.syntaxtree.Not;
import mini.java.syntaxtree.Plus;
import mini.java.syntaxtree.Print;
import mini.java.syntaxtree.PrioExp;
import mini.java.syntaxtree.Program;
import mini.java.syntaxtree.Statement;
import mini.java.syntaxtree.StatementList;
import mini.java.syntaxtree.This;
import mini.java.syntaxtree.Times;
import mini.java.syntaxtree.True;
import mini.java.syntaxtree.Type;
import mini.java.syntaxtree.VarDecl;
import mini.java.syntaxtree.VarDeclList;
import mini.java.syntaxtree.While;

/**
 * 
 * Builder is a helper class which transforms AST
 * into annotated AST. 
 *
 */
public class Builder {
	private static Builder theInstance;
	private Builder() {}
	
	/**
	 * 
	 * Singleton Pattern
	 * 
	 */
	public static Builder newInstance() {
		if(theInstance == null) {
			theInstance = new Builder();
		}
		
		return theInstance;
	}
	
	public Program buildProgram(Node<Token> n) {
		List<Node<Token>> children = n.getChildren();
		
		Node<Token> mcNode = children.get(0);
		if(!mcNode.getData().getData().equals("MainClass")) {
			throw new RuntimeException("MainClassTypeException");
		}
		MainClass mc = buildMainClass(mcNode);
		
		ClassDeclList cdl = new ClassDeclList();
		if(children.size() == 2) {
			 cdl = buildClassDeclList(children.get(1));
		}
		
		return new Program(mc, cdl);		
	}
	
	public MainClass buildMainClass(Node<Token> n) {
		String className = null;
		int l1 = -1;
		
		String argName = null;
		int l2 = -1;
		
		Statement stmt = null;
		
		List<Node<Token>> children = n.getChildren();
		for(Node<Token> node : children) {
			Token t = node.getData();
			if(t.getType().equals("id")) {
				if(className == null) {
					className = t.getText();
					l1 = t.getLineNum();
				} else {
					argName = t.getText();
					l2 = t.getLineNum();
				}
			} else if(t.getText().equals("Statement*")) {
				//stmt = buildStatementList(node).elementAt(0);
				stmt = buildStatement(node.getChildren().get(0));
			}
		}
		Identifier i1 = new Identifier(className, l1);
		Identifier i2 = new Identifier(argName, l2);		
		
		return new MainClass(i1, i2, stmt);
	}
	
	public StatementList buildStatementList(Node<Token> n) {
		List<Node<Token>> nodes = n.getChildren();
		StatementList sl = new StatementList();
		for(Node<Token> node : nodes) {
			Token t = node.getData();
			if(t.getText().equals("Statement*")) {
				StatementList tmp = buildStatementList(node);
				for(int i = 0; i < tmp.size(); i++) {
					sl.addElement(tmp.elementAt(i));
				}
			} else if(t.getText().equals("Statement")) {
				Statement stmt = buildStatement(node);
				sl.addElement(stmt);
			}
		}
		
		return sl;
	}
	
	public Statement buildStatement(Node<Token> n) {
		Token t = n.getData();
		if(!t.getText().equals("Statement")) {
			n.dump();
			throw new RuntimeException("StatementTokenTypeException");
		}
		
		// @TODO: much work!!!
		List<Node<Token>> children = n.getChildren();
		Node<Token> first = children.get(0);
		Token firstToken = first.getData();
		String type = firstToken.getType();
		if(type.equals("{")) {
			// Block Statement
			return buildBlock(n);
		} else if(type.equals("System.out.println")) {
			// Print Statement
			return buildPrint(n);
		} else if(type.equals("if")) {
			// If Statement
			return buildIf(n);
		} else if(type.equals("while")) {
			// While Statement
			return buildWhile(n);
		} else if(type.equals("id")) {
			// Assign Statement or Array Assign Statement
			Node<Token> second = children.get(1);
			if(second.getData().getText().equals("=")) {
				// Assign Statement
				return buildAssign(n);
			} else {
				// Array Assign Statement
				return buildArrayAssign(n);
			}
		}
		
		throw new RuntimeException("Unkown Statement Exception");		
	}
	
	public Block buildBlock(Node<Token> n) {
		StatementList sl = buildStatementList(n);
		return new Block(sl);
	}
	
	public Print buildPrint(Node<Token> n) {
		Exp exp = buildExp(n.getChildren().get(2));
		return new Print(exp);
	}
		
	public If buildIf(Node<Token> n) {
		List<Node<Token>> children = n.getChildren();
		Exp exp = buildExp(children.get(2));
		Statement s1 = buildStatement(children.get(4));
		Statement s2 = buildStatement(children.get(6));
		
		return new If(exp, s1, s2);
	}
	
	public While buildWhile(Node<Token> n) {
		List<Node<Token>> children = n.getChildren();
		Exp exp = buildExp(children.get(2));
		Statement s = buildStatement(children.get(4));
		
		return new While(exp, s);
	}
	
	public Assign buildAssign(Node<Token> n) {
		List<Node<Token>> children = n.getChildren();
		Token t0 = children.get(0).getData();
		Identifier id = new Identifier(t0.getText(), t0.getLineNum());
		Exp exp = buildExp(children.get(2));
		
		return new Assign(id, exp);
	}
	
	public ArrayAssign buildArrayAssign(Node<Token> n) {
		List<Node<Token>> children = n.getChildren();
		Token t0 = children.get(0).getData();
		Identifier id = new Identifier(t0.getText(), t0.getLineNum());
		Exp exp1= buildExp(children.get(2));
		Exp exp2= buildExp(children.get(5));
		
		return new ArrayAssign(id, exp1, exp2);
	}
	
	public Exp buildExp(Node<Token> n) {
		List<Node<Token>> children = n.getChildren();
		boolean single = (children.size() == 1);
		Node<Token> n0 = children.get(0);
		Token t0 = n0.getData();
		
		if(t0.getText().equals("E0")) {
			if(single) {
				return buildExp(n0);
			} else {
				Node<Token> n1 = children.get(1);
				Token t1 = n1.getData();
				if(t1.getText().equals("+")) {
					return buildPlus(n);
				} else {
					return buildMinus(n);
				}
			}
		} else if(t0.getText().equals("E1")) {
			if(single) {
				return buildExp(n0);
			} else {
				return buildTimes(n);
			}
		} else if(t0.getText().equals("E2")) {
			return buildExp(n0);
		} else if(t0.getText().equals("E3")) {
			if(single) {
				return buildExp(n0);
			} else {
				Node<Token> n1 = children.get(1);
				Token t1 = n1.getData();
				if(t1.getText().equals("[")) {
					// ArrayLookup
					return buildArrayLookup(n);
				} else if(t1.getText().equals(".")) {
					Node<Token> n2 = children.get(2);
					Token t2 = n2.getData();
					if(t2.getText().equals("length")) {
						// ArrayLength
						return buildArrayLength(n);
					} else if(t2.getType().equals("id")){
						// Call
						return buildCall(n);
					}
				}
			}
		} else if(t0.getText().equals("E4")) {
			return buildExp(n0);
		} else if(t0.getText().equals("(")) {		
			return new PrioExp(buildExp(children.get(1)));
		} else if(t0.getText().equals("Exp")) {
		
			Node<Token> n1 = children.get(1);
			Token t1 = n1.getData();
			if(t1.getText().equals("&&")) {
				// And
				return buildAnd(n);
			} else if(t1.getText().equals("<")) {
				// LessThan
				return buildLessThan(n);
			}
		} else if(t0.getText().equals("new")) {
			Node<Token> n1 = children.get(1);
			Token t1 = n1.getData();
			if(t1.getText().equals("int")) {
				// NewArray
				return buildNewArray(n);
			} else if(t1.getType().equals("id")) {
				// New Object
				return buildNewObject(n);
			}
		} else if(t0.getType().equals("integer")) {
			// Integer Literal
			return buildIntegerLiteral(n);
		} else if(t0.getText().equals("true")) {
			// True
			return new True(t0.getLineNum());
		} else if(t0.getText().equals("false")) {
			// False
			return new False(t0.getLineNum());
		} else if(t0.getType().equals("id")) {
			// IdentifierExp
			return buildIdentifierExp(n);
		} else if(t0.getText().equals("this")) {
			// This
			return new This(t0.getLineNum());
		} else if(t0.getText().equals("!")) {
			// Not
			return buildNot(n);
		}
		
		n.dump();
		throw new RuntimeException("Unknown Exp Exception");
	}
	
	public And buildAnd(Node<Token> n) {
		List<Node<Token>> children = n.getChildren();
		Exp e1 = buildExp(children.get(0));
		Exp e2 = buildExp(children.get(2));
		
		return new And(e1, e2);
	}
	
	public LessThan buildLessThan(Node<Token> n) {
		List<Node<Token>> children = n.getChildren();
		Exp e1 = buildExp(children.get(0));
		Exp e2 = buildExp(children.get(2));
		
		return new LessThan(e1, e2);
	}
	
	public Plus buildPlus(Node<Token> n) {
		List<Node<Token>> children = n.getChildren();
		Exp e1 = buildExp(children.get(0));
		Exp e2 = buildExp(children.get(2));
		
		return new Plus(e1, e2);
	}
	
	public Minus buildMinus(Node<Token> n) {
		List<Node<Token>> children = n.getChildren();
		Exp e1 = buildExp(children.get(0));
		Exp e2 = buildExp(children.get(2));
		
		return new Minus(e1, e2);
	}
	
	public Times buildTimes(Node<Token> n) {
		List<Node<Token>> children = n.getChildren();
		Exp e1 = buildExp(children.get(0));
		Exp e2 = buildExp(children.get(2));
		
		return new Times(e1, e2);
	}
	
	public ArrayLookup buildArrayLookup(Node<Token> n) {
		List<Node<Token>> children = n.getChildren();
		Exp e1 = buildExp(children.get(0));
		Exp e2 = buildExp(children.get(2));
		
		return new ArrayLookup(e1, e2);
	}
	
	public ArrayLength buildArrayLength(Node<Token> n) {
		List<Node<Token>> children = n.getChildren();
		Exp e = buildExp(children.get(0));	
		
		return new ArrayLength(e);
	}
	
	public Call buildCall(Node<Token> n) {
		List<Node<Token>> children = n.getChildren();
		Exp e = buildExp(children.get(0));
		
		Token t2 = children.get(2).getData();
		Identifier id = new Identifier(t2.getText(), t2.getLineNum());
		ExpList el = buildExpList(children.get(4));
		return new Call(e, id, el);
	}

	public ExpList buildExpList(Node<Token> n) {
		ExpList el = new ExpList();
		List<Node<Token>> children = n.getChildren();
		for(int i = 0; i < children.size(); i += 2) {			
			el.addElement(buildExp(children.get(i)));
		}
		
		return el;
	}
	
	public NewArray buildNewArray(Node<Token> n) {
		List<Node<Token>> children = n.getChildren();
		Exp e = buildExp(children.get(3));
		
		return new NewArray(e);
	}
	
	public NewObject buildNewObject(Node<Token> n) {
		List<Node<Token>> children = n.getChildren();
		
		Token t1 = children.get(1).getData();
		Identifier id = new Identifier(t1.getText(), t1.getLineNum());
		
		return new NewObject(id);
	}
	
	public IdentifierExp buildIdentifierExp(Node<Token> n) {
		List<Node<Token>> children = n.getChildren();
		
		Token t0 = children.get(0).getData();
		return new IdentifierExp(t0.getText(), t0.getLineNum());
	}
	
	public Not buildNot(Node<Token> n) {
		List<Node<Token>> children = n.getChildren();
		Exp e = buildExp(children.get(1));
		
		return new Not(e);
	}
	
	public IntegerLiteral buildIntegerLiteral(Node<Token> n) {
		List<Node<Token>> children = n.getChildren();
		Token t = children.get(0).getData();
		return new IntegerLiteral(Integer.parseInt(t.getText()), t.getLineNum());
	}
	
	public ClassDeclList buildClassDeclList(Node<Token> n) {
		List<Node<Token>> children = n.getChildren();
		ClassDeclList cdl = new ClassDeclList();
		for(Node<Token> node : children) {
			Token t = node.getData();
			if(t.getText().equals("ClassDecl*")) {
				ClassDeclList tmp = buildClassDeclList(node);
				for(int i = 0; i < tmp.size(); i++) {
					cdl.addElement(tmp.elementAt(i));
				}
			} else if(t.getText().equals("ClassDecl")) {
				ClassDecl cd = buildClassDecl(node);
				cdl.addElement(cd);
			}
		}
		
		return cdl;
	}
	
	public ClassDecl buildClassDecl(Node<Token> n) {
		List<Node<Token>> children = n.getChildren();
		Node<Token> n2 = children.get(2);
		Token t2 = n2.getData();
		if(t2.getText().equals("extends")) {
			// ClassDeclExtends
			return buildClassDeclExtends(n);
		} else if(t2.getText().equals("{")) {
			// ClassDeclSimple
			return buildClassDeclSimple(n);
		}
		
		n.dump();
		throw new RuntimeException("Unknow Class Declaration");
	}
	
	public ClassDeclSimple buildClassDeclSimple(Node<Token> n) {
		List<Node<Token>> children = n.getChildren();
		
		Token t1 = children.get(1).getData();
		Identifier id = new Identifier(t1.getText(), t1.getLineNum());
		
		Node<Token> n3 = children.get(3);
		Token t3 = n3.getData();
		VarDeclList vdl = new VarDeclList();
		if(t3.getText().equals("VarDecl*")) {
			vdl = buildVarDeclList(n3);
		}
		MethodDeclList mdl = buildMethodDeclList(n);
		
		return new ClassDeclSimple(id, vdl, mdl);		
	}
	
	public ClassDeclExtends buildClassDeclExtends(Node<Token> n) {
		List<Node<Token>> children = n.getChildren();
		
		Token t1 = children.get(1).getData();
		Identifier i1 = new Identifier(t1.getText(), t1.getLineNum());
		
		Token t3 = children.get(3).getData();
		Identifier i2 = new Identifier(t3.getText(), t3.getLineNum());
		
		Node<Token> n5 = children.get(5);
		Token t5 = n5.getData();
		VarDeclList vdl = new VarDeclList();
		if(t5.getText().equals("VarDecl*")) {
			vdl = buildVarDeclList(n5);
		}
		
		MethodDeclList mdl = buildMethodDeclList(n);
		
		return new ClassDeclExtends(i1, i2, vdl, mdl);
	}
	
	public VarDeclList buildVarDeclList(Node<Token> n) {
		VarDeclList vdl = new VarDeclList();
		List<Node<Token>> children = n.getChildren();
		
		if(children.size() == 1) {
			vdl.addElement(buildVarDecl(children.get(0)));
		} else {
			VarDeclList tmp = buildVarDeclList(children.get(0));
			for(int i = 0; i < tmp.size(); i++) {
				vdl.addElement(tmp.elementAt(i));
			}
			vdl.addElement(buildVarDecl(children.get(1)));
		}
		
		return vdl;
	}
	
	public VarDecl buildVarDecl(Node<Token> n) {
		List<Node<Token>> children = n.getChildren();
		Type type = buildType(children.get(0));
		
		Token t3 = children.get(1).getData();
		Identifier id = new Identifier(t3.getText(), t3.getLineNum());
		
		return new VarDecl(type, id);
	}
	
	public Type buildType(Node<Token> n) {
		List<Node<Token>> children = n.getChildren();
		Node<Token> n0 = children.get(0);
		Token t0 = n0.getData();
		String txt0 = t0.getText();
		String type0 = t0.getType();
		if(children.size() == 1) {
			if(txt0.equals("int")) {
				return new IntegerType();
			} else if(txt0.equals("boolean")) {
				return new BooleanType();
			} else if(type0.equals("id")) {
				return new IdentifierType(txt0);
			}
		} else if(children.size() == 3){
			return new IntArrayType();
		}
		
		n.dump();
		throw new RuntimeException("Unknow Type");
	}
	
	public MethodDeclList buildMethodDeclList(Node<Token> n) {
		MethodDeclList mdl = new MethodDeclList();
		List<Node<Token>> children = n.getChildren();
		for(Node<Token> node : children) {
			Token t = node.getData();
			if(t.getText().equals("MethodDesc*")) {
				MethodDeclList tmp = buildMethodDeclList(node);
				for(int i = 0; i < tmp.size(); i++) {
					mdl.addElement(tmp.elementAt(i));
				}
			} else if(t.getText().equals("MethodDesc")) {
				mdl.addElement(buildMethodDecl(node));				
			}
		}
		
		return mdl;
	}
	
	public MethodDecl buildMethodDecl(Node<Token> n) {
		List<Node<Token>> children = n.getChildren();
		Type type = buildType(children.get(1));
		
		Token t2 = children.get(2).getData();
		Identifier id = new Identifier(t2.getText(), t2.getLineNum());
		FormalList fl = new FormalList();
		VarDeclList vdl = new VarDeclList();
		StatementList sl = new StatementList();
		Exp retExp = null;
		
		int body = 6;
		boolean normal = true;
		
		Node<Token> n4 = children.get(4);
		if(n4.getData().getText().equals("FormalList")) {
			fl = buildFormalList(n4);
			body = 7;
		}		
		
		Node<Token> bodyNode = children.get(body);
		Token bodyToken = bodyNode.getData();
		if(bodyToken.getText().equals("return")) {
			retExp = buildExp(children.get(body + 1));
		} else if(bodyToken.getText().equals("Statement*")) {
			sl = buildStatementList(bodyNode);
			retExp = buildExp(children.get(body + 2));
		} else if(bodyToken.getText().equals("VarDecl*")) {
			vdl = buildVarDeclList(bodyNode);
			Node<Token> bodyNxtNode = children.get(body + 1);
			Token bodyNxtToken = bodyNxtNode.getData();
			if(bodyNxtToken.getText().equals("Statement*")) {
				sl = buildStatementList(bodyNxtNode);
				retExp = buildExp(children.get(body + 3));
			} else if(bodyNxtToken.getText().equals("return")) {
				retExp = buildExp(children.get(body + 2));
			} else {
				normal = false;
			}
		} else {
			normal = false;
		}
		
		if(normal) {
			return new MethodDecl(type, id, fl, vdl, sl, retExp);
		} else {
			n.dump();
			throw new RuntimeException("Unknown MethodDecl");
		}
	}
	
	public FormalList buildFormalList(Node<Token> n) {
		FormalList fl = new FormalList();
		List<Node<Token>> children = n.getChildren();
		Type type = buildType(children.get(0));
		Token t = children.get(1).getData();
		Identifier id = new Identifier(t.getText(), t.getLineNum());
		fl.addElement(new Formal(type, id));
		
		if(children.size() > 2) {
			FormalList tmp = buildFormalRestList(children.get(2));
			for(int i = 0; i < tmp.size(); i++) {
				fl.addElement(tmp.elementAt(i));
			}
		}
		
		return fl;
	}
	
	public FormalList buildFormalRestList(Node<Token> n) {
		FormalList fl = new FormalList();
		List<Node<Token>> children = n.getChildren();
		if(children.size() == 1) {
			fl.addElement(buildFormalRest(children.get(0)));
		} else {
			FormalList tmp = buildFormalRestList(children.get(0));
			for(int i = 0; i < tmp.size(); i++) {
				fl.addElement(tmp.elementAt(i));
			}
			fl.addElement(buildFormalRest(children.get(1)));
		}
		
		return fl;
	}
	
	public Formal buildFormalRest(Node<Token> n) {
		List<Node<Token>> children = n.getChildren();
		Type t = buildType(children.get(1));
		Token token = children.get(2).getData();
		Identifier id = new Identifier(token.getText(), token.getLineNum());
		return new Formal(t, id);
	}
}
