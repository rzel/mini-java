package mini.java.lex;

import misc.Type;

public class TokenType extends Type {
    public TokenType(String rep) {
        super(rep);
    }
    public TokenType() {
    }
//    private String rep;
//
//    public TokenType() {
//    }
//    public TokenType(String rep) {
//        this.setRep(rep);
//    }
//
//    public String getRep() {
//        return rep;
//    }
//    public void setRep(String rep) {
//        this.rep = rep;
//    }
//
//    public boolean equals(Object o) {
//        if (! (o instanceof TokenType)) return false;
//        return getRep().equals(((TokenType) o).getRep());
//    }
//
//    public String toString() {
//        return rep;
//    }
    public boolean equals(Object o) {
        //if (o == null) return false;
        //if (! (o instanceof Type)) return false;
        //return rep.equals(((Type) o).getRep());
        if (this == o)
                return true;
        if ((o == null) || (o.getClass() != this.getClass()))
                return false;
        return getRep().equals(((TokenType) o).getRep());
    }

    public int hashCode() {
        return getRep().hashCode();
    }

    public String toString() {
        return getRep();
    }

}
