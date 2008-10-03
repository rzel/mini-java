package mini.java.fa.legacy.v2;

import mini.java.lex.legacy.Tokenizer;

public interface DFASimulator {
	/**
	 * 
	 * @return the Tokenizer accordingly
	 */
	public Tokenizer getTokenizer();
	
	/**
	 * 
	 * Sets the relevant spec file.
	 * @param fn the name of the spec file
	 */
	public void setSpecFile(String fn);
}
