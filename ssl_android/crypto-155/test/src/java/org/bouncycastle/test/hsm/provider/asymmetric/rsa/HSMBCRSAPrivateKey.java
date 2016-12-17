package org.bouncycastle.test.hsm.provider.asymmetric.rsa;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.jce.interfaces.PKCS12BagAttributeCarrier;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.RSAPrivateKeySpec;
import java.util.Enumeration;

public class HSMBCRSAPrivateKey
    implements RSAPrivateKey, PKCS12BagAttributeCarrier
{
    static final long serialVersionUID = 5110188922551353628L;

    private static BigInteger ZERO = BigInteger.valueOf(0);

    protected BigInteger modulus;
    protected BigInteger privateExponent;

    private transient PKCS12BagAttributeCarrierImpl attrCarrier = new PKCS12BagAttributeCarrierImpl();

    protected HSMBCRSAPrivateKey()
    {
    }

    HSMBCRSAPrivateKey(
            RSAKeyParameters key)
    {
        this.modulus = key.getModulus();
        this.privateExponent = key.getExponent();
    }

    HSMBCRSAPrivateKey(
            RSAPrivateKeySpec spec)
    {
        this.modulus = spec.getModulus();
        this.privateExponent = spec.getPrivateExponent();
    }

    HSMBCRSAPrivateKey(
            RSAPrivateKey key)
    {
        this.modulus = key.getModulus();
        this.privateExponent = key.getPrivateExponent();
    }

    HSMBCRSAPrivateKey(org.bouncycastle.asn1.pkcs.RSAPrivateKey key)
    {
        this.modulus = key.getModulus();
        this.privateExponent = key.getPrivateExponent();
    }

    public BigInteger getModulus()
    {
        return modulus;
    }

    public BigInteger getPrivateExponent()
    {
        return null;
    }

    public String getAlgorithm()
    {
        return "RSA";
    }

    public String getFormat()
    {
        return "PKCS#8";
    }

    public byte[] getEncoded()
    {
        return null; //KeyUtil.getEncodedPrivateKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, DERNull.INSTANCE), new org.bouncycastle.asn1.pkcs.RSAPrivateKey(getModulus(), ZERO, getPrivateExponent(), ZERO, ZERO, ZERO, ZERO, ZERO));
    }

    BigInteger hsmGetPrivateExponent()
    {
        return privateExponent;
    }

    public boolean equals(Object o)
    {
        if (!(o instanceof RSAPrivateKey))
        {
            return false;
        }

        if (o == this)
        {
            return true;
        }

        RSAPrivateKey key = (RSAPrivateKey)o;

        return getModulus().equals(key.getModulus())
            && getPrivateExponent().equals(key.getPrivateExponent());
    }

    public int hashCode()
    {
        return getModulus().hashCode() ^ getPrivateExponent().hashCode();
    }

    public void setBagAttribute(
        ASN1ObjectIdentifier oid,
        ASN1Encodable attribute)
    {
        attrCarrier.setBagAttribute(oid, attribute);
    }

    public ASN1Encodable getBagAttribute(
        ASN1ObjectIdentifier oid)
    {
        return attrCarrier.getBagAttribute(oid);
    }

    public Enumeration getBagAttributeKeys()
    {
        return attrCarrier.getBagAttributeKeys();
    }

    private void readObject(
        ObjectInputStream   in)
        throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();

        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
    }

    private void writeObject(
        ObjectOutputStream  out)
        throws IOException
    {
        out.defaultWriteObject();
    }
}
