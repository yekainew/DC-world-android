package com.mugui.block.btc;

import com.mugui.base.base.Component;
import com.mugui.block.sql.BlockWalletBean;

import org.apache.commons.lang3.StringUtils;
import org.bitcoinj.core.Base58;
import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.core.UTXO;
import org.bitcoinj.crypto.DeterministicHierarchy;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.wallet.Wallet;
import org.bouncycastle.util.encoders.Hex;
import org.tron.common.crypto.SymmEncoder;
import org.tron.common.utils.ByteArray;
import org.tron.common.utils.Utils;
import org.web3j.crypto.Hash;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class BTCBlockUtil {

    public BlockWalletBean create(String pwd) {
        byte[] passwordHash = getPasswordHash(pwd);
        if (passwordHash == null) {
            return null;
        }
        ECKey ecKey = new ECKey(Utils.getRandom());

        BlockWalletBean blockWalletBean = new BlockWalletBean();
        String pwdAsc = ByteArray.toHexString(passwordHash);
        initUser(blockWalletBean, ecKey, pwdAsc);
        return blockWalletBean;
    }

    public BlockWalletBean createByPri(String private_key, String pwd) {
        byte[] passwordHash = getPasswordHash(pwd);
        if (passwordHash == null) {
            return null;
        }
        ECKey ecKey = DumpedPrivateKey.fromBase58(networkParameters, private_key).getKey();
        BlockWalletBean blockWalletBean = new BlockWalletBean();
        String pwdAsc = ByteArray.toHexString(passwordHash);
        initUser(blockWalletBean, ecKey, pwdAsc);
        return blockWalletBean;
    }

    public byte[] getPasswordHash(String password) {
        if (!isPasswordValid(password)) {
            return null;
        }
        byte[] pwd;
        pwd = Hash.sha256(password.getBytes());
        pwd = Hash.sha256(pwd);
        pwd = Arrays.copyOfRange(pwd, 0, 16);
        return pwd;
    }

    private void initUser(BlockWalletBean blockWalletBean, ECKey ecKey, String pwd) {
        byte[] aes128EcbEnc = SymmEncoder.AES128EcbEnc(ecKey.getPrivKeyBytes(), ByteArray.fromHexString(pwd));
        blockWalletBean.setPrivate_key(ByteArray.toHexString(aes128EcbEnc));
        blockWalletBean.setPublic_key(ecKey.getPublicKeyAsHex());
        blockWalletBean.setAddress(ecKey.toAddress(networkParameters).toBase58());
    }

    public boolean isPasswordValid(String password) {
        if (StringUtils.isEmpty(password)) {
            return false;
        }
        if (password.length() < 6) {
            return false;
        }
        if (password.contains("\\s")) {
            return false;
        }
        return true;
    }

    NetworkParameters networkParameters = TestNet3Params.get();


    /**
     * 得到私钥
     *
     * @param select
     * @param pwd
     */
    public String getPrivateStr(BlockWalletBean select, String pwd) {
        byte[] passwordHash = getPasswordHash(pwd);
        if (passwordHash == null) {
            return null;
        }
        String private_key = select.getPrivate_key();
        byte[] decodeHex = Hex.decode(private_key);
        byte[] aes128EcbDec = SymmEncoder.AES128EcbDec(decodeHex, passwordHash);
        ECKey fromPrivate = ECKey.fromPrivate(aes128EcbDec);
        String encode58Check = fromPrivate.toAddress(networkParameters).toBase58();
        if (select.getAddress().equals(encode58Check)) {
            return fromPrivate.getPrivateKeyEncoded(networkParameters).toBase58();
        }
        return null;
    }

    public ECKey getECKey(String pwd, BlockWalletBean blockWalletBean) {
        byte[] passwordHash = getPasswordHash(pwd);
        if (passwordHash == null) {
            return null;
        }
        String private_key = blockWalletBean.getPrivate_key();
        byte[] decodeHex = Hex.decode(private_key);
        byte[] aes128EcbDec = SymmEncoder.AES128EcbDec(decodeHex, passwordHash);
        ECKey fromPrivate = ECKey.fromPrivate(aes128EcbDec);
        return fromPrivate;
    }




}
