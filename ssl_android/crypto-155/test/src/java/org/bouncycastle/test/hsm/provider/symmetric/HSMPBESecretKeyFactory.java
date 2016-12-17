package org.bouncycastle.test.hsm.provider.symmetric;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.jcajce.provider.symmetric.util.BCPBEKey;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseSecretKeyFactory;
import org.bouncycastle.jcajce.provider.symmetric.util.PBE;

import javax.crypto.SecretKey;
import javax.crypto.spec.PBEKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;


public class HSMPBESecretKeyFactory
        extends BaseSecretKeyFactory
        implements PBE {
    private boolean forCipher;
    private int scheme;
    private int digest;
    private int keySize;
    private int ivSize;

    public HSMPBESecretKeyFactory(
            String algorithm,
            ASN1ObjectIdentifier oid,
            boolean forCipher,
            int scheme,
            int digest,
            int keySize,
            int ivSize) {
        super(algorithm, oid);

        this.forCipher = forCipher;
        this.scheme = scheme;
        this.digest = digest;
        this.keySize = keySize;
        this.ivSize = ivSize;
    }

    protected SecretKey engineGenerateSecret(
            KeySpec keySpec)
            throws InvalidKeySpecException {
        if (keySpec instanceof PBEKeySpec) {
            PBEKeySpec pbeSpec = (PBEKeySpec) keySpec;
            CipherParameters param;

            if (pbeSpec.getSalt() == null) {
                return new BCPBEKey(this.algName, this.algOid, scheme, digest, keySize, ivSize, pbeSpec, null);
            }

            if (forCipher) {
                param = PBE.Util.makePBEParameters(pbeSpec, scheme, digest, keySize, ivSize);
            } else {
                param = PBE.Util.makePBEMacParameters(pbeSpec, scheme, digest, keySize);
            }

            return new HSMBCPBEKey(this.algName, this.algOid, scheme, digest, keySize, ivSize, pbeSpec, param);
        }

        throw new InvalidKeySpecException("Invalid KeySpec");
    }


    class HSMBCPBEKey extends BCPBEKey {


        public HSMBCPBEKey(String algorithm, ASN1ObjectIdentifier oid, int type, int digest, int keySize, int ivSize, PBEKeySpec pbeKeySpec, CipherParameters param) {
            super(algorithm, oid, type, digest, keySize, ivSize, pbeKeySpec, param);
        }

        @Override
        public byte[] getEncoded() {
            return null;
        }
    }

}



