package org.bouncycastle.test.hsm.provider.symmetric;

import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseKeyGenerator;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;


public class HSMBaseKeyGenerator extends BaseKeyGenerator {
    public HSMBaseKeyGenerator(String algName, int defaultKeySize, CipherKeyGenerator engine) {
        super(algName, defaultKeySize, engine);
    }


    @Override
    protected SecretKey engineGenerateKey() {

        if (uninitialised)
        {
            engine.init(new KeyGenerationParameters(new SecureRandom(), defaultKeySize));
            uninitialised = false;
        }

        return new HSMSecretKeySpec(engine.generateKey(), algName);
    }
}


