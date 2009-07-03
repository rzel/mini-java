package mini.java;

import mini.java.fa.adapter.V3Adapter;
import mini.java.fa.v3.DFABuilder;
import mini.java.fa.v3.NFABuilder;

/**
 * Provides implementations to various interfaces.
 */
public final class ComponentFactory {
    // -----implementations for V3 interfaces
    public static DFABuilder createDFABuilder() {
        return new V3Adapter();
    }
    
    public static NFABuilder createNFABuilder() {
        return new V3Adapter();
    }
}
