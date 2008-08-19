
package gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import java.util.Stack;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeNode;

//import configure.Configure;

//import model.Token;
import lex.legacy.Token;
import lex.TokenRevamped;
import lex.TokenType;
import lex.TokenRevampedAdapter;
import syntax.legacy.Node;
import syntax.legacy.Pair;
import syntax.SymbolType;
import syntax.NonTerminal;

import java.awt.Color;


public class TreeView extends JPanel{

	private static final long serialVersionUID = 8788771075864093192L;
	private Collection<Token> tokens;
	private Vector<String> spec_types;
	
	private JTree tree;
	private DefaultTreeModel dtm;
	private JScrollPane jsp;
	private TextView textp;

        public TreeView() {
		this.textp = textp;
		tree = new JTree();
		tree.setModel(null);
		jsp = new JScrollPane(tree);
		
		jsp.setBounds(0, 0, 200, 595);
		this.add(jsp);
		this.setLayout(null);
        }
	
	public TreeView(TextView textp){
		this.textp = textp;
		tree = new JTree();
		tree.setModel(null);
		jsp = new JScrollPane(tree);
		
		jsp.setBounds(0, 0, 200, 595);
		this.add(jsp);
		this.setLayout(null);
		
		tree.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				//left button clicked  
	            if (e.getButton()==1){  
	                //double click  
	                if(e.getClickCount()==2){   
	                	TreePath tp = tree.getSelectionPath();
	                	DefaultMutableTreeNode selected = (DefaultMutableTreeNode)tp.getLastPathComponent();
	                	if((!selected.isLeaf())&&(!selected.isRoot())){
	                		TreeView.this.textp.clear();
	                		//TreeView.this.textp.highlight(selected.toString().trim(), Configure.getHighlight());
	                		TreeView.this.textp.highlight(selected.toString().trim(), Color.orange);
	                	}
	                	if(selected.isLeaf()){
	                		TreeView.this.textp.clear();
	                		StringBuffer s = new StringBuffer(selected.toString().trim());
	                		String [] temp = s.toString().split("___");
	                		String [] temp_2 = temp[1].split(",");
	                		String type = selected.getParent().toString().trim();
	                		if(type.toLowerCase().equals("skip")){
	                			if(temp[0].equals("WhiteSpace")){
	                				Token t = new Token(type, " ", Integer.parseInt(temp_2[0]), Integer.parseInt(temp_2[1]));
	    	                		//TreeView.this.textp.highlight(t, Configure.getLeaf_highlight());
	    	                		TreeView.this.textp.highlight(t, Color.red);
	    	                		return;
	                			}else if(temp[0].equals("\\n")){
	                				return;
	                			}else if(temp[0].equals("\\t")){
	                				Token t = new Token(type, "\t", Integer.parseInt(temp_2[0]), Integer.parseInt(temp_2[1]));
	    	                		//TreeView.this.textp.highlight(t, Configure.getLeaf_highlight());
	    	                		TreeView.this.textp.highlight(t, Color.red);
	    	                		return;
	                			}
	                		}
	                		Token t = new Token(type, temp[0], Integer.parseInt(temp_2[0]), Integer.parseInt(temp_2[1]));
	                		//TreeView.this.textp.highlight(t, Configure.getLeaf_highlight());
                                        TreeView.this.textp.highlight(t, Color.red);
	                	}
	                }    
	            }
	        }
		});
	}
	
	public void setTokens(Collection<Token> tokens){
		this.tokens = tokens;
	}
	
	public void addTree(){
		DefaultMutableTreeNode top=new DefaultMutableTreeNode("Specs");
		spec_types = new Vector<String>();
		
		for(Iterator<Token> it = tokens.iterator();it.hasNext();){
			Token token = (Token)it.next();
			StringBuffer buffer = new StringBuffer();
			if(spec_types.size()==0||!(spec_types.contains(token.getType()))){
				spec_types.add(token.getType());
				DefaultMutableTreeNode types = new DefaultMutableTreeNode(token.getType());
				top.add(types);
				if(token.getText().equals("\0")){
					buffer.append("EOF___"+token.getLineNum()+" ,"+token.getColumn());
					DefaultMutableTreeNode ele = new DefaultMutableTreeNode(buffer.toString());
					types.add(ele);
					continue;
				}
				if(token.getType().toLowerCase().equals("skip")){
					if(token.getText().equals("\n\n")){
						buffer.append("\\n___"+token.getLineNum()+","+token.getColumn());
						DefaultMutableTreeNode ele = new DefaultMutableTreeNode(buffer.toString());
						types.add(ele);
						continue;
					}else if(token.getText().equals("\n")){
						buffer.append("\\n___"+token.getLineNum()+","+token.getColumn());
						DefaultMutableTreeNode ele = new DefaultMutableTreeNode(buffer.toString());
						types.add(ele);
						continue;
					}else if(token.getText().equals("\t")){
						buffer.append("\\t___"+token.getLineNum()+","+token.getColumn());
						DefaultMutableTreeNode ele = new DefaultMutableTreeNode(buffer.toString());
						types.add(ele);
						continue;
					}else if(token.getText().equals(" ")){
						buffer.append("WhiteSpace___"+token.getLineNum()+","+token.getColumn());
						DefaultMutableTreeNode ele = new DefaultMutableTreeNode(buffer.toString());
						types.add(ele);
						continue;
					}
				}
				buffer.append(token.getText()+"___"+token.getLineNum()+","+token.getColumn());
				DefaultMutableTreeNode ele = new DefaultMutableTreeNode(buffer.toString());
				types.add(ele);
			}else{
				int index = spec_types.indexOf(token.getType());
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)top.getChildAt(index);
				
				if(token.getText().equals("\0")){
					buffer.append("EOF___"+token.getLineNum()+","+token.getColumn());
					DefaultMutableTreeNode ele = new DefaultMutableTreeNode(buffer.toString());
					node.add(ele);
					continue;
				}
				
				if(token.getType().toLowerCase().equals("skip")){
					if(token.getText().equals("\n\n")){
						buffer.append("\\n___"+token.getLineNum()+","+token.getColumn());
						DefaultMutableTreeNode ele = new DefaultMutableTreeNode(buffer.toString());
						node.add(ele);
						continue;
					}else if(token.getText().equals("\n")){
						buffer.append("\\n___"+token.getLineNum()+","+token.getColumn());
						DefaultMutableTreeNode ele = new DefaultMutableTreeNode(buffer.toString());
						node.add(ele);
						continue;
					}else if(token.getText().equals("\t")){
						buffer.append("\\t___"+token.getLineNum()+","+token.getColumn());
						DefaultMutableTreeNode ele = new DefaultMutableTreeNode(buffer.toString());
						node.add(ele);
						continue;
					}else if(token.getText().equals(" ")){
						buffer.append("WhiteSpace___"+token.getLineNum()+","+token.getColumn());
						DefaultMutableTreeNode ele = new DefaultMutableTreeNode(buffer.toString());
						node.add(ele);
						continue;
					}
				}
				buffer.append(token.getText()+"___"+token.getLineNum()+","+token.getColumn());
				DefaultMutableTreeNode ele = new DefaultMutableTreeNode(buffer.toString());
				node.add(ele);
			}
		}
		
		dtm = new DefaultTreeModel(top);
		tree.setModel(dtm);
	}
	
	public void removeTree(){
		tree.setModel(null);
	}

        public void addTree(DefaultMutableTreeNode root) {
            dtm = new DefaultTreeModel(root);
            tree.setModel(dtm);
        }

    public static DefaultMutableTreeNode fromNodeToTreeNode(Node<Pair<SymbolType, TokenRevamped>> node) {
        Stack<Integer> indexStack = new Stack<Integer>();
        //Since Node does not contain its parent's reference, we
        //use a stack to remember the nodes;
        Stack<Node<Pair<SymbolType, TokenRevamped>>> nodeStack = new Stack<Node<Pair<SymbolType, TokenRevamped>>>();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        DefaultMutableTreeNode currentTreeNode = null;
        //Node<Token> currentNode = null;

        //Initialization
        indexStack.push(0);
        //currentNode = node;
        nodeStack.push(node);
        currentTreeNode = root; //In fact, currentTreeNode is one level
                                //above than currentNode;

        while (!nodeStack.empty()) {
            //maybe we should use one stack instead
            assert(indexStack.size() == nodeStack.size());

            //get current state
            Node<Pair<SymbolType, TokenRevamped>> currentNode = nodeStack.pop();
            int index = indexStack.pop();

            if (index == 0) {
                //so this is the first time we come across this node
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode();
                Pair<SymbolType, TokenRevamped> oldPair = currentNode.data;
                TokenRevamped token = null;
                if (oldPair.first instanceof NonTerminal) {
                    token = new TokenRevamped(new TokenType(oldPair.first.getRep()));
                } else {
                    token = oldPair.second;
                }
                System.out.println("PROCESSING: " + token);
                newNode.setUserObject((Object)(new TokenRevampedAdapter(token)));
                //I wonder whether add() will automatically set the
                //parent of the new node?
                //newNode.setParent(currentTreeNode);
                currentTreeNode.insert(newNode, 0);
                currentTreeNode = newNode;
            }

            if (index < currentNode.getNumberOfChildren()) {
                //save the state
                nodeStack.push(currentNode);
                indexStack.push(index+1);

                //give control to the children
                nodeStack.push(currentNode.getChildren().get(index));
                indexStack.push(0);

                //currentNode = currentNode.getChildren().get(index);
            } else {
                //no more children, return control to parent;
                currentTreeNode = (DefaultMutableTreeNode)currentTreeNode.getParent();
            }
        }

        return (DefaultMutableTreeNode)root.getChildAt(0);
    }
}
