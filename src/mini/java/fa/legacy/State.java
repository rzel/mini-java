/**
 * state of the SimpleFA
 */
package mini.java.fa.legacy;

public class State {
    private static int idCount = 0;
    private int id;

    public State(int id) {
        this.id = id;
    }

    public State() {
        this.id = idCount++;
    }

    public int getId() {
        return this.id;
    }

    public boolean equals(Object o) {
        if (o == null) { return false; }
        if (!(o instanceof State)) { return false; }
        if (((State) o).id != this.id) { return false; }
        return true;
    }

    public int hashCode() {
        return id;
    }

    public String toString() {
        return "" + id;
    }
}
