package org.bouncycastle.test.hsm.provider.symmetric;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import java.util.Arrays;

/**
 * Created by meganwoods on 23/04/15.
 */
public class HSMSecretKeySpec  extends SecretKeySpec {


    public HSMSecretKeySpec(byte[] bytes, String s) {
        super(bytes, s);
    }

    public HSMSecretKeySpec(byte[] bytes, int i, int i1, String s) {
        super(bytes, i, i1, s);
    }

    protected byte[] hsmGetEncoded() {
        return super.getEncoded();
    }

    @Override
    public byte[] getEncoded() {
        return null;
    }
}


