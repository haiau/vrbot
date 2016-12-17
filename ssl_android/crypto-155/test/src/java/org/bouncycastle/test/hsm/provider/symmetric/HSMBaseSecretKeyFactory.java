package org.bouncycastle.test.hsm.provider.symmetric;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.jcajce.provider.symmetric.util.PBE;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactorySpi;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Constructor;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class HSMBaseSecretKeyFactory  extends SecretKeyFactorySpi
        implements PBE
{
    protected String                algName;
    protected ASN1ObjectIdentifier algOid;

    protected HSMBaseSecretKeyFactory(
            String algName,
            ASN1ObjectIdentifier algOid)
    {
        this.algName = algName;
        this.algOid = algOid;
    }

    protected SecretKey engineGenerateSecret(
            KeySpec keySpec)
            throws InvalidKeySpecException
    {
        if (keySpec instanceof HSMSecretKeySpec)
        {
            return (SecretKey)keySpec;
        }

        throw new InvalidKeySpecException("Invalid KeySpec");
    }

    protected KeySpec engineGetKeySpec(
            SecretKey key,
            Class keySpec)
            throws InvalidKeySpecException
    {
        if (keySpec == null)
        {
            throw new InvalidKeySpecException("keySpec parameter is null");
        }
        if (key == null)
        {
            throw new InvalidKeySpecException("key parameter is null");
        }

        if (SecretKeySpec.class.isAssignableFrom(keySpec))
        {
            return new SecretKeySpec(key.getEncoded(), algName);
        }

        try
        {
            Class[] parameters = { byte[].class };

            Constructor c = keySpec.getConstructor(parameters);
            Object[]    p = new Object[1];

            p[0] = key.getEncoded();

            return (KeySpec)c.newInstance(p);
        }
        catch (Exception e)
        {
            throw new InvalidKeySpecException(e.toString());
        }
    }

    protected SecretKey engineTranslateKey(
            SecretKey key)
            throws InvalidKeyException
    {
        if (key == null)
        {
            throw new InvalidKeyException("key parameter is null");
        }

        if (!key.getAlgorithm().equalsIgnoreCase(algName))
        {
            throw new InvalidKeyException("Key not of type " + algName + ".");
        }

        return new HSMSecretKeySpec(key.getEncoded(), algName);
    }
}
