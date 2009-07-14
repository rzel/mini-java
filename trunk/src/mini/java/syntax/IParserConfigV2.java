package mini.java.syntax;


/**
 * Refactored parser config interface. Contains everything a parser needs.
 */
public interface IParserConfigV2 {
    /**
     * Returns the DFA used for parsing.
     */
    public ParserState getEngine();
    
//    /**
//     * Returns the corresponding rule for this acceptable state based 
//     * on the given look ahead symbol/token.
//     */
//    public Rule inferRule(AcceptableNFAState state_, Terminal lookahead_);
    
    /**
     * Returns all the rules supported.
     */
    public RuleSet getRuleSet();
}
