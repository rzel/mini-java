package misc;

public class Type {
    private String rep;

    public Type() {
    }
    public Type(String rep) {
        this.setRep(rep);
    }

    public String getRep() {
        return rep;
    }
    public void setRep(String rep) {
        this.rep = rep;
    }

    public boolean equals(Object o) {
        //if (o == null) return false;
        //if (! (o instanceof Type)) return false;
        //return rep.equals(((Type) o).getRep());
        if (this == o)
                return true;
        if ((o == null) || (o.getClass() != this.getClass()))
                return false;
        return rep.equals(((Type) o).getRep());
    }

    public int hashCode() {
        return rep.hashCode();
    }

    public String toString() {
        return rep;
    }
}
