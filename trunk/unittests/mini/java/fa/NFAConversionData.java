package mini.java.fa;

import java.util.Arrays;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public abstract class NFAConversionData {
    @Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
                { "ABa",        "ABa" },
                { "ABa,BCb",    "ABa,BCb" },
                { "ABb,ACc",    "ABb,ACc" },
                { "ABa,BC",     "ABa" },
                { "AB,BCa",     "ABa" },
                { "ABa,BC,BD",  "ABa" },
                { "AB,BC,BDa",  "ABa" },
                { "ABa,BA,AC,CDb,CDc", "ADb,ADc,ACa,CCa,CDb,CDc" }, // a*(b|c)
                { "ABa,BCa,BCb,CB,BD,ADc", "ABa,BEa,BEb,EEa,EEb,ADc" }, // (a(a|b)*)|c
        });
    }
}
