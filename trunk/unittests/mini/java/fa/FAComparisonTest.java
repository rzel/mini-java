package mini.java.fa;

import java.util.Arrays;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public abstract class FAComparisonTest {
    @Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
                // A, B,  result
                {"ABa",         "CDa",          true}, // single transition
                {"ABb,ACc",     "CDb,CEc",      true}, // multiple transitions
                {"ABa,BCb",     "CDa,DEb",      true},
                {"abA,acB",     "abA,acB",      true},
                {"ABa,BAa",     "CDa,DCa",      true}, // loop
                {"ABa,BAa",     "ABa,BCb",      false},
                {"ABa",         "ABa,ABb",      false}, // different structure
                {"ABb",         "ACc",          false}, // different inputs
                {"DFd,BEd,ACd,ECa,ACa", "EFd",   true},
                {"ABb,ACc,ADd",         "ABb,ABc,ADd",  false}, //bug
        });
    }
}