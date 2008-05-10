/**
 * Tokens parsed from input regular expressions, used internally
 */
package lex.legacy;
import java.util.Set;

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

    public boolean equals(Object o) {
        if (o == null) { return false; }
        if (!(o instanceof RegexToken)) { return false; }

        RegexToken o0 = (RegexToken)o;
        if (this.type != o0.type) { return false; }
        if (this.payload == null) {
            if (o0.payload != null) { return false; }
            else { return true; }
        }

        if (!this.payload.equals(o0.payload)) { return false; }
        else { return true; }
    }
}
