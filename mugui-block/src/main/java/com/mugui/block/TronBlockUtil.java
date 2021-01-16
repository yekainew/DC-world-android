package com.mugui.block;

import com.mugui.base.base.Component;
import com.mugui.base.bean.DefaultJsonBean;
import com.mugui.base.client.net.bagsend.HTTPUtil;
import com.mugui.base.client.net.bean.Message;
import com.mugui.block.sql.BlockWalletBean;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.tron.common.crypto.ECKey;
import org.tron.common.crypto.Hash;
import org.tron.common.crypto.SymmEncoder;
import org.tron.common.utils.Base58;
import org.tron.common.utils.ByteArray;
import org.tron.common.utils.Utils;

import java.util.Arrays;

@Component
public class TronBlockUtil {

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
        ECKey ecKey = ECKey.fromPrivate(Hex.decode(private_key));
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
        blockWalletBean.setPublic_key(ByteArray.toHexString(ecKey.getPubKey()));
        blockWalletBean.setAddress(encode58Check(ecKey.getAddress()));
    }

    public String encode58Check(byte[] input) {
        byte[] hash0 = Hash.sha256(input);
        byte[] hash1 = Hash.sha256(hash0);
        byte[] inputCheck = new byte[input.length + 4];
        System.arraycopy(input, 0, inputCheck, 0, input.length);
        System.arraycopy(hash1, 0, inputCheck, input.length, 4);
        return Base58.encode(inputCheck);
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
        String encode58Check = encode58Check(fromPrivate.getAddress());
        if (select.getAddress().equals(encode58Check)) {
            return Hex.toHexString(aes128EcbDec);
        }
        return null;
    }
    public synchronized Message tranById(String hash) {
        try {
            String url = "https://apilist.tronscan.org/api/transaction-info?hash=" + hash;
            Object post = HTTPUtil.get(url);
            return Message.ok(DefaultJsonBean.newBean(DefaultJsonBean.class, post).get());
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("查询失败");
        }
    }

    /**
     * 拿取一个trc20合约信息
     *
     * @param contract 合约地址
     * @return
     */
    public synchronized Message trc20(String contract) {
        try {
            String url = "https://apilist.tronscan.org/api/token_trc20?contract=" + contract;
            Object post = HTTPUtil.get(url);
            return Message.ok(DefaultJsonBean.newBean(DefaultJsonBean.class, post).get());
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("无法拿取合约信息");
        }
    }

    public synchronized Message trc10(String id) {

        try {
            String url = "https://apilist.tronscan.org/api/token?id=" + id + "&showAll=1";
            Object post = HTTPUtil.get(url);
            return Message.ok(DefaultJsonBean.newBean(DefaultJsonBean.class, post).get());
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("无法拿取合约信息");
        }
    }

}
