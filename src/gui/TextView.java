package gui;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.Highlighter.HighlightPainter;

import java.util.*;

//import model.Token;
import lex.legacy.Token;

public class TextView extends JPanel{

	private static final long serialVersionUID = 3789257671698953725L;
	private String text;
	private String filename;
	private Collection<Token> tokens;
	private SimpleAttributeSet sas;
	private JTextPane jtp;
	private JScrollPane jsp;
	private Vector<Integer> lineStartPos;
	
	public TextView(){
		jtp = new JTextPane(){ 
			private static final long serialVersionUID = 1L;

			public void paint(Graphics g) { 
                super.paint(g); 
                TextView.this.repaint(); 
            } 
        }; 
		jsp = new JScrollPane(jtp);
		sas = new SimpleAttributeSet();
		lineStartPos = new Vector<Integer>();
		
		StyleConstants.setFontFamily(sas, "Arial");
		StyleConstants.setFontSize(sas, 15);
		//StyleConstants.setLineSpacing(sas,17.0f); 
		//StyleConstants.setSpaceAbove(sas, 30);
		//StyleConstants.setLeftIndent(sas, 100L);
		//StyleConstants.setBold(sas, true); 
		jtp.setEditable(false);
		jtp.setBounds(40, 0, 760, 395);
		jsp.setBounds(40, 0, 760, 395);
		this.add(jsp);
		this.setLayout(null);
		this.setBackground(Color.white);
	}
	
	public void highlight(Token temp, Color color){
		Highlighter hl = jtp.getHighlighter();
		HighlightPainter mhp = new MyHighlightPainter(color);
		try{
			int pos = (this.lineStartPos.get(temp.getLineNum()-1))+temp.getColumn();
			hl.addHighlight(pos-1, pos+temp.getText().length()-1, mhp);
			jtp.setCaretPosition(pos-1);
		}catch(BadLocationException ble){
			JOptionPane.showMessageDialog(this, "Failure","Bad Location",JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
	
	public void highlight(String type,Color color){
		
		Highlighter hl = jtp.getHighlighter();
		for(Iterator<Token> it = tokens.iterator();it.hasNext();){
			Token temp = (Token)it.next();
			if(temp.getType().equals(type)){
				HighlightPainter mhp = new MyHighlightPainter(color);
				try{
					int pos = (this.lineStartPos.get(temp.getLineNum()-1))+temp.getColumn();
					hl.addHighlight(pos-1, pos+temp.getText().length()-1, mhp);
				}catch(BadLocationException ble){
					JOptionPane.showMessageDialog(this, "Failure","Bad Location",JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		}
	}
	
	public void clear(){
		jtp.getHighlighter().removeAllHighlights();
	}
	
	public void setTokens(Collection<Token> tokens){
		this.tokens = tokens;
	}

	public void paint(Graphics g) 
    { 
        super.paint(g); 

        int start = jtp.viewToModel(jsp.getViewport().getViewPosition());
        int end = jtp.viewToModel( new Point( 
                      jsp.getViewport().getViewPosition().x + jtp.getWidth(), 
                      jsp.getViewport().getViewPosition().y + jtp.getHeight())); 

        Document doc = jtp.getDocument(); 
        int startline = doc.getDefaultRootElement().getElementIndex(start) + 1; 
        int endline = doc.getDefaultRootElement().getElementIndex(end) + 1; 

        int fontHeight = g.getFontMetrics(jtp.getFont()).getHeight(); 
        int fontDesc = g.getFontMetrics(jtp.getFont()).getDescent(); 
        int starting_y = -1; 

        try { 
            starting_y = jtp.modelToView(start).y - jsp.getViewport().getViewPosition().y + fontHeight - fontDesc; 
        } catch (BadLocationException e1) { 
            e1.printStackTrace(); 
        } 
        if(text!=null){
        	for (int line = startline, y = starting_y; line <= endline; y += fontHeight, line++) { 
        		g.setColor(Color.blue);
        		g.drawString(Integer.toString(line), 0, y); 
        	} 
        }
    }
	
	public void removeTextPane(){
		text = null;
		jtp.setText(null);
	}
	
	public void open(String filename){
		if(this.filename!=null&&this.filename.equals(filename)){
			jtp.setText(text);
			jtp.validate();
			return;
		}
		
		jtp.setText(null);
		
		BufferedReader br = null;
		StringBuffer textbuffer = new StringBuffer();
		
		try{
			br = new BufferedReader(new FileReader(new File(filename)));
		}catch(IOException ioe){
			JOptionPane.showMessageDialog(this, "Failure","No such file",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		int column = 1;
		int position = 0;
		if(lineStartPos!=null&&lineStartPos.size()!=0){
			lineStartPos.clear();
		}
		
		while(true){
			try{
				String temp = null;
				lineStartPos.add(position);
				if((temp = br.readLine())!=null){
					textbuffer.append(temp);
					textbuffer.append("\n");
					position += temp.length()+1;
					column++;
				}
				else{
					break;
				}
			}catch(IOException ioe){
				JOptionPane.showMessageDialog(this, "Failure","Read Error at Line "+String.valueOf(column),JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		
		try{
			jtp.getDocument().insertString(0, textbuffer.toString(), (AttributeSet)sas);
		}catch(BadLocationException ble){
			JOptionPane.showMessageDialog(this, "Failure","Bad Location "+String.valueOf(column),JOptionPane.ERROR_MESSAGE);
			return;
		}
		jtp.validate();
		this.filename = filename;
		this.text = textbuffer.toString();
	}
	
	class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {
	    public MyHighlightPainter(Color color) {
	        super(color);
	    }
	}
}
