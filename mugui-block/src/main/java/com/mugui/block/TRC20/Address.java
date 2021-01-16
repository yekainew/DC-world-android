package com.mugui.block.TRC20;

import com.google.common.primitives.Bytes;

import org.tron.common.utils.Base58;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.utils.Numeric;

import java.util.Arrays;

public class Address {
    public String hex;
    public String base58;

    private Address(String hex, String base58) {
        this.hex = hex;
        this.base58 = base58;
    }

    public static Address fromPublicKey(String publicKey) {
        String hex = "41" + Keys.getAddress(publicKey);
        String base58 = Address.encode(hex);
        return new Address(hex, base58);
    }

    public static String encode(String hex) {
        byte[] input = Numeric.hexStringToByteArray(hex);
        byte[] hash0 = Hash.sha256(input);
        byte[] hash1 = Hash.sha256(hash0);
        byte[] checksum = Arrays.copyOfRange(hash1, 0, 4);
        byte[] output = Bytes.concat(input, checksum);
        return Base58.encode(output);
    }

    public static String decode(String b58) throws Exception {
        byte[] input = Base58.decode(b58);
        byte[] output = Arrays.copyOfRange(input, 0, input.length - 4);
        return Numeric.toHexStringNoPrefix(output);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(encode("0x410583a68a3bcd86c25ab1bee482bac04a216b0261"));

    }

    public String toString() {
        return this.base58;
    }


}