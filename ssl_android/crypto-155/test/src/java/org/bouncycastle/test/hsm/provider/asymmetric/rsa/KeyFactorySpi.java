package org.bouncycastle.test.hsm.provider.asymmetric.rsa;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;

import org.bouncycastle.jcajce.provider.asymmetric.util.ExtendedInvalidKeySpecException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;

public class KeyFactorySpi
    extends HSMBaseKeyFactorySpi
{
    public KeyFactorySpi()
    {
    }

    protected KeySpec engineGetKeySpec(
        Key key,
        Class spec)
        throws InvalidKeySpecException
    {
        if (spec.isAssignableFrom(RSAPublicKeySpec.class) && key instanceof RSAPublicKey)
        {
            RSAPublicKey k = (RSAPublicKey)key;

            return new RSAPublicKeySpec(k.getModulus(), k.getPublicExponent());
        }
        else if (spec.isAssignableFrom(RSAPrivateKeySpec.class) && key instanceof java.security.interfaces.RSAPrivateKey)
        {
            java.security.interfaces.RSAPrivateKey k = (java.security.interfaces.RSAPrivateKey)key;

            return new RSAPrivateKeySpec(k.getModulus(), k.getPrivateExponent());
        }
        else if (spec.isAssignableFrom(RSAPrivateCrtKeySpec.class) && key instanceof RSAPrivateCrtKey)
        {
            RSAPrivateCrtKey k = (RSAPrivateCrtKey)key;

            return new RSAPrivateCrtKeySpec(
                k.getModulus(), k.getPublicExponent(),
                k.getPrivateExponent(),
                k.getPrimeP(), k.getPrimeQ(),
                k.getPrimeExponentP(), k.getPrimeExponentQ(),
                k.getCrtCoefficient());
        }

        return super.engineGetKeySpec(key, spec);
    }

    protected Key engineTranslateKey(
        Key key)
        throws InvalidKeyException
    {
        if (key instanceof RSAPublicKey)
        {
            return new HSMBCRSAPublicKey((RSAPublicKey)key);
        }
        else if (key instanceof RSAPrivateCrtKey)
        {
            return new HSMBCRSAPrivateCrtKey((RSAPrivateCrtKey)key);
        }
        else if (key instanceof java.security.interfaces.RSAPrivateKey)
        {
            return new HSMBCRSAPrivateKey((java.security.interfaces.RSAPrivateKey)key);
        }

        throw new InvalidKeyException("key type unknown");
    }

    protected PrivateKey engineGeneratePrivate(
        KeySpec keySpec)
        throws InvalidKeySpecException
    {
        if (keySpec instanceof PKCS8EncodedKeySpec)
        {
            try
            {
                return generatePrivate(PrivateKeyInfo.getInstance(((PKCS8EncodedKeySpec) keySpec).getEncoded()));
            }
            catch (Exception e)
            {
                //
                // in case it's just a RSAPrivateKey object... -- openSSL produces these
                //
                try
                {
                    return new HSMBCRSAPrivateCrtKey(
                        RSAPrivateKey.getInstance(((PKCS8EncodedKeySpec) keySpec).getEncoded()));
                }
                catch (Exception ex)
                {
                    throw new ExtendedInvalidKeySpecException("unable to process key spec: " + e.toString(), e);
                }
            }
        }
        else if (keySpec instanceof RSAPrivateCrtKeySpec)
        {
            return new HSMBCRSAPrivateCrtKey((RSAPrivateCrtKeySpec)keySpec);
        }
        else if (keySpec instanceof RSAPrivateKeySpec)
        {
            return new HSMBCRSAPrivateKey((RSAPrivateKeySpec)keySpec);
        }

        throw new InvalidKeySpecException("Unknown KeySpec type: " + keySpec.getClass().getName());
    }

    protected PublicKey engineGeneratePublic(
        KeySpec keySpec)
        throws InvalidKeySpecException
    {
        if (keySpec instanceof RSAPublicKeySpec)
        {
            return new HSMBCRSAPublicKey((RSAPublicKeySpec)keySpec);
        }

        return super.engineGeneratePublic(keySpec);
    }

    public PrivateKey generatePrivate(PrivateKeyInfo keyInfo)
        throws IOException
    {
        ASN1ObjectIdentifier algOid = keyInfo.getPrivateKeyAlgorithm().getAlgorithm();

        if (RSAUtil.isRsaOid(algOid))
        {
            RSAPrivateKey rsaPrivKey = RSAPrivateKey.getInstance(keyInfo.parsePrivateKey());

            if (rsaPrivKey.getCoefficient().intValue() == 0)
            {
                return new HSMBCRSAPrivateKey(rsaPrivKey);
            }
            else
            {
                return new HSMBCRSAPrivateCrtKey(keyInfo);
            }
        }
        else
        {
            throw new IOException("algorithm identifier " + algOid + " in key not recognised");
        }
    }

    public PublicKey generatePublic(SubjectPublicKeyInfo keyInfo)
        throws IOException
    {
        ASN1ObjectIdentifier algOid = keyInfo.getAlgorithm().getAlgorithm();

        if (RSAUtil.isRsaOid(algOid))
        {
            return new HSMBCRSAPublicKey(keyInfo);
        }
        else
        {
            throw new IOException("algorithm identifier " + algOid + " in key not recognised");
        }
    }
}
