package mini.java.syntax.legacy;

//public class Symbol {
//public interface Symbol {
//public class Symbol extends Node<SymbolType> {
public class Symbol extends Node<Token> {
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
    public Symbol(SymbolType type, Token token) {
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

    public Token getToken() {
        return data;
    }
    public void setToken(Token token) {
        data = token;
    }

    //public List<Symbol>
}
