package org.bouncycastle.test;


import org.bouncycastle.test.hsm.provider.HSMAESTest;
import org.bouncycastle.test.hsm.provider.HSMDESedeTest;
import org.bouncycastle.test.hsm.provider.HSMSigTest;

public class TestRun {

    public static void main(String[] args) {
        HSMSigTest.main(args);
        HSMAESTest.main(args);
        HSMDESedeTest.main(args);
    }
}
