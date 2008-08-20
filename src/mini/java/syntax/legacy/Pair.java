package mini.java.syntax.legacy;
public class Pair<T1, T2> {
    public T1 first;
    public T2 second;

    public Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    public boolean equals(Object obj) {
        if(!(obj instanceof Pair)) {
            return false;
        } else {
            Pair p = (Pair)obj;
            return p.first.equals(this.first) && p.second.equals(this.second);
        }
    }

    public int hashCode() {
        return (first == null ? 0 : first.hashCode()) + (second == null ? 0 :7 * second.hashCode());
    }

    public void dump() {
        System.out.println("\n=== Pair ===");
        System.out.println("First: " + first);
        System.out.println("Second: " + second);
    }

    public String toString() {
        return "" + ((first==null) ? "null" : first.getClass())
                + "{" + first + "}-"
                + ((second==null) ? "null" : second.getClass())
                + "{" + second + "}";
    }
}

