package mini.java.syntax.legacy;

class TokenRevampedWithGeneric<T> {
    private TokenType type;
    private T data;

    public TokenType getType() {
        return type;
    }
    public void setType(TokenType type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }

    public String toString() {
        //return "TOKEN[" + type + "] ==> " + data.toString();
        return "" + type + "{" + data + "}";
    }
}

public class TokenRevamped extends TokenRevampedWithGeneric<String> {
    public TokenRevamped(String type) {
        //this.type = type;
        super.setType(new TokenType(type));
    }
    public TokenRevamped(TokenType type) {
        super.setType(type);
    }
    public TokenRevamped() {
    }
}
