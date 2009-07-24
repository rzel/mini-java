/**
 * Tokens parsed from input regular expressions, used internally
 */
package mini.java.regex.legacy;

public class RegexToken {
    public enum Type { CHAR, STAR, QM, BAR, LP, RP, EXPR }
    public final Type type;
    public final Object payload;

    public RegexToken(Type type) {
        this.type = type;
        this.payload = null;
    }

    public RegexToken(Type type, Object payload) {
        this.type = type;
        this.payload = payload;
    }

    public String toString() {
        return new String("[" + type + "]" + payload);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((payload == null) ? 0 : payload.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final RegexToken other = (RegexToken) obj;
        if (payload == null) {
            if (other.payload != null)
                return false;
        } else if (!payload.equals(other.payload))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }


}
