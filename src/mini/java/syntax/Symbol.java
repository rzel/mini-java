package mini.java.syntax;

import mini.java.lex.TokenRevamped;
import mini.java.syntax.legacy.Node;
//public class Symbol {
//public interface Symbol {
//public class Symbol extends Node<SymbolType> {
public class Symbol extends Node<TokenRevamped> {
    private SymbolType type;

    //public SymbolType getType() {
    //    return type;
    //}
    //public void setType(SymbolType type) {
    //    this.type = type;
    //}
    //public SymbolType getSymbolType();
    //public void setSymbolType(SymbolType type);
    //
    public Symbol(SymbolType type, TokenRevamped token) {
        super(token);
        setSymbolType(type);
    }
    public Symbol(SymbolType type) {
        //super(type);
        super();
        setSymbolType(type);
    }
    public Symbol() {
        super();
    }

    public SymbolType getSymbolType() {
        return type;
    }
    public void setSymbolType(SymbolType type) {
        this.type = type;
    }

    public TokenRevamped getToken() {
        return data;
    }
    public void setToken(TokenRevamped token) {
        data = token;
    }

    //public List<Symbol>
}
