package com.mugui.block.TRC20;

import com.google.common.primitives.Bytes;
import com.mugui.block.TRC20.Address;

import org.bouncycastle.asn1.x9.X9IntegerConverter;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

public class Credential {
    private ECKeyPair keyPair;

    private Credential(ECKeyPair keyPair) {
        this.keyPair = keyPair;
    }

    public static Credential create() {
        try {
            ECKeyPair keyPair = Keys.createEcKeyPair();
            return new Credential(keyPair);
        } catch (Exception e) {

            return null;
        }
    }

    public static Credential fromPrivateKey(String key) {
        BigInteger privateKey = Numeric.toBigInt(key);
        ECKeyPair keyPair = ECKeyPair.create(privateKey);
        return new Credential(keyPair);
    }

    public static boolean verifySignature(byte[] sigR, byte[] sigS, byte[] sigV, byte[] message, byte[] pubKey) {
        byte[] arrPubKey = recoverPublicKey(sigR, sigS, sigV, message);
        if (ByteUtils.toHexString(arrPubKey).equals(ByteUtils.toHexString(pubKey))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据私钥的签名推算对应得公钥
     *
     * @return
     */
    public static byte[] recoverPublicKey(byte[] sigR, byte[] sigS, byte[] sigV, byte[] message) {
        ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec("secp256k1");
        BigInteger pointN = spec.getN();
        try {
            BigInteger pointX = new BigInteger(1, sigR);
            X9IntegerConverter x9 = new X9IntegerConverter();
            byte[] compEnc = x9.integerToBytes(pointX, 1 + x9.getByteLength(spec.getCurve()));
            compEnc[0] = (byte) ((sigV[0] & 1) == 1 ? 0x03 : 0x02);
            ECPoint pointR = spec.getCurve().decodePoint(compEnc);
            if (!pointR.multiply(pointN).isInfinity()) {
                return new byte[0];
            }

            BigInteger pointE = new BigInteger(1, message);
            BigInteger pointEInv = BigInteger.ZERO.subtract(pointE).mod(pointN);
            BigInteger pointRInv = new BigInteger(1, sigR).modInverse(pointN);
            BigInteger srinv = pointRInv.multiply(new BigInteger(1, sigS)).mod(pointN);
            BigInteger pointEInvRInv = pointRInv.multiply(pointEInv).mod(pointN);
            ECPoint pointQ = ECAlgorithms.sumOfTwoMultiplies(spec.getG(), pointEInvRInv, pointR, srinv);
            byte[] pointQBytes = pointQ.getEncoded(false);
            return pointQBytes;
        } catch (Exception e) {
            //
        }
        return new byte[0];
    }

    public String getPrivateKey() {
        return keyPair.getPrivateKey().toString(16);
    }

    public String getPublicKey() {
        return keyPair.getPublicKey().toString(16);
    }

    public Address getAddress() {
        return Address.fromPublicKey(getPublicKey());
    }

    public String sign(byte[] txhash) {
        Sign.SignatureData sd = Sign.signMessage(txhash, keyPair, false);
        byte[] v = sd.getV();
        v[0] -= 27;
        byte[] output = Bytes.concat(sd.getR(), sd.getS(), v);

        return Numeric.toHexStringNoPrefix(output);
    }

    public String sign(String txid) {
        byte[] txhash = Numeric.hexStringToByteArray(txid);
        return sign(txhash);
    }


}