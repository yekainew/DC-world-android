package com.jkkg.hhtx.block;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.ArrayMap;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.net.bean.AccountResourecBean;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.UserWalletBean;
import com.jkkg.hhtx.utils.SpUtil;
import com.mugui.base.base.Autowired;
import com.mugui.base.base.Component;
import com.mugui.base.bean.DefaultJsonBean;
import com.mugui.base.client.net.bagsend.HTTPUtil;
import com.mugui.base.client.net.bean.Message;
import com.mugui.base.client.net.classutil.DataSave;
import com.mugui.block.BIP39;
import com.mugui.block.TRC20.Credential;
import com.mugui.block.TRC20.TransactionResult;
import com.mugui.block.TRC20.TronApi;

import org.apache.commons.lang3.StringUtils;
import org.tron.common.crypto.Hash;
import org.tron.common.utils.ByteArray;
import org.tron.walletserver.DuplicateNameException;
import org.tron.walletserver.InvalidNameException;
import org.tron.walletserver.InvalidPasswordException;
import org.tron.walletserver.Wallet;
import org.tron.walletserver.WalletManager;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cn.hutool.http.HttpUtil;

@Component
public class Block {

    /**
     * 其他功能请优先使用{@link WalletManager}
     */
    public static Block instance() {
        return DataSave.context.getBean(Block.class);
    }


    /**
     * 创建钱包
     */
    public synchronized void createWallet(String wallet_name, String password) throws Exception {
        if (!WalletManager.isNameValid(wallet_name)) {
            throw new NullPointerException("名称错误");
        }
        if (WalletManager.existWallet(wallet_name)) {
            throw new NullPointerException("钱包已创建");
        }
        if (!WalletManager.isPasswordValid(password)) {
            throw new NullPointerException("密码不够安全");
        }
        try {

            Wallet wallet = new Wallet(true);
            wallet.setWalletName(wallet_name);
            wallet.setColdWallet(true);
            WalletManager.store(wallet, password);
            WalletManager.selectWallet(wallet_name);
        } catch (DuplicateNameException | InvalidPasswordException | InvalidNameException e) {
            // Should be already checked above
            e.printStackTrace();
            return;
        }
    }

    /**
     * 得到钱包
     *
     * @param password
     * @return
     */
    public synchronized WalletBean getWallet(String wallet_name, String password) {
        Wallet wallet = WalletManager.getWallet(wallet_name, password);
        if (wallet == null || !wallet.isOpen()) {
            return null;
        }
        WalletBean walletBean = new WalletBean();
        walletBean.setAddress(wallet.getAddress());
        walletBean.setPri(ByteArray.toHexString(wallet.getECKey().getPrivKeyBytes()));
        try {
            walletBean.setRecoveryPhrase(BIP39.encode(wallet.getECKey().getPrivKeyBytes(), "pass"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return walletBean;
    }

    /**
     * 恢复钱包
     *
     * @param walletBean
     * @param password   钱包密码
     * @return
     */
    public synchronized boolean importWallet(WalletBean walletBean, String password) {
        if (StringUtils.isBlank(walletBean.getPri()) && StringUtils.isBlank(walletBean.getRecoveryPhrase())) {
            throw new RuntimeException("无法导入");
        }
        String privKey = "";
        try {
            if (StringUtils.isBlank(walletBean.getPri())) {
                privKey = ByteArray.toHexString(BIP39.decode(walletBean.getRecoveryPhrase().toString(), "pass"));

            } else {
                privKey = walletBean.getPri();
            }
            if (!WalletManager.isNameValid(walletBean.getName())) {
                throw new RuntimeException("名称错误" + walletBean.getName());
            } else if (WalletManager.existWallet(walletBean.getName())) {
                throw new RuntimeException("已创建" + walletBean.getName());
            } else if (!WalletManager.isPasswordValid(password)) {
                throw new RuntimeException("密码错误" + password);
            } else if (!WalletManager.isPrivateKeyValid(privKey)) {
                throw new RuntimeException("私钥错误" + privKey);
            }
            Wallet wallet = new Wallet(privKey);
            wallet.setWalletName(walletBean.getName());
            wallet.setColdWallet(false);
            WalletManager.store(wallet, password);
            WalletManager.selectWallet(walletBean.getName());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 钱包是否已经创建
     *
     * @return
     */
    public synchronized boolean existWallet(String wallet_name) {
        return WalletManager.existWallet(wallet_name);
    }

    /**
     * 删除钱包
     *
     * @return
     */
    public synchronized boolean removeWallet(String walletName) {
//        String selectedWallet = WalletManager.getSelectedWallet().getWalletName();
        SharedPreferences.Editor editor = DataSave.app.getApplicationContext().getSharedPreferences(walletName, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();

        SharedPreferences preferences = DataSave.app.getApplicationContext().getSharedPreferences(DataSave.app.getApplicationContext().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = preferences.edit();
        Set<String> wallets = new HashSet<>(preferences.getStringSet(DataSave.app.getResources().getString(R.string.wallets_key), new HashSet<>()));
        wallets.remove(walletName);
        editor.putStringSet(DataSave.app.getResources().getString(R.string.wallets_key), wallets);

        if (!wallets.isEmpty())
            editor.putString(DataSave.app.getResources().getString(R.string.selected_wallet_key), wallets.iterator().next());
        editor.commit();
        return true;
    }


    /**
     * 转账
     *
     * @param to              去向地址
     * @param contractAddress 合约地址 存在则转合约,否则转trx
     * @param amount          转账数量
     * @param password        钱包密码
     * @return boolean
     */
    public synchronized Message tron(String to, String contractAddress, BigDecimal amount, String name, String password) {

        try {
            WalletBean wallet = getWallet(name, password);
            if (wallet == null || StringUtils.isBlank(wallet.getPri())) {
                return Message.error("钱包密码错误");
            }
            if (StringUtils.isNotBlank(contractAddress)) {
                TransactionResult transactionResult = mainNet.transformToken(wallet.getPri(), to, amount, contractAddress);
                if (StringUtils.isNotBlank(transactionResult.txId)) {
                    return Message.ok(transactionResult.txId);
                }else {
                    System.out.println("tron->"+transactionResult.toString());
                }
            } else {
                TransactionResult transactionResult = mainNet.transformTrx(wallet.getPri(), to, amount.multiply(new BigDecimal("1000000")).longValue());
                if (StringUtils.isNotBlank(transactionResult.txId)) {
                    return Message.ok(transactionResult.txId);
                }
                System.out.println("tron->"+transactionResult.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error(e.getMessage());
        }
        return Message.error("交易发起失败");
    }

    @Autowired
    private TronApi mainNet;
    @Autowired
    Dao dao;

    /**
     * 查询余额
     *
     * @param contractAddress 合约地址 为空则查询trx余额
     * @return
     */
    public synchronized Message balance(String wallet_name, String contractAddress) {
        Wallet wallet = WalletManager.getWallet(wallet_name);
        try {
            BigDecimal balance = mainNet.getBalance(wallet.getAddress(), contractAddress);
            return Message.ok(balance.stripTrailingZeros().toPlainString());
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("无法得到余额");
        }
    }

    /**
     * @param start 第几页
     * @param size  查询多少
     * @return
     */
    public synchronized Message tranList(int start, int size) {
        UserWalletBean this_wallet_name = dao.select(new UserWalletBean().setName(SpUtil.getInstance(DataSave.app).getString("this_wallet_name", "")));
        String url = "https://apilist.tronscan.org/api/trc10trc20-transfer?sort=timestamp&count=true&limit=" + size + "&start=" + start + "&total=0&address=" + this_wallet_name.getAddress();
        Object post = HTTPUtil.get(url);
        DefaultJsonBean defaultJsonBean = DefaultJsonBean.newBean(DefaultJsonBean.class, post);
        if (defaultJsonBean == null) {
            return Message.error("查询失败");
        }
        return Message.ok(defaultJsonBean.get());
    }


    /**
     * 通过hash查询交易
     *
     * @param hash
     * @return TRC20 合约的hash交易
     * {
     * "block": 24177454,
     * "hash": "f6301aa72e56f09b054baf0aa2018d3d213cfce30171fb5bcf37b1493e3b8b28",
     * "timestamp": 1602825813000,
     * "ownerAddress": "TQQrSXL8WCxeEfkZPHDrG6qX9MistA1XgV",
     * "signature_addresses": [],
     * "contractType": 31,
     * "toAddress": "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t",
     * "confirmations": 2083,
     * "confirmed": true,
     * "revert": false,
     * "contractRet": "SUCCESS",
     * "contractData": {
     * "data": "a9059cbb000000000000000000000041d18423d9ec60237689d557e15fd6ef29f11b9c3300000000000000000000000000000000000000000000000000000000000186a0",
     * "owner_address": "TQQrSXL8WCxeEfkZPHDrG6qX9MistA1XgV",
     * "contract_address": "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t"
     * },
     * "data": "",
     * "cost": {
     * "net_fee": 0,
     * "energy_usage": 0,
     * "energy_fee": 296310,
     * "energy_usage_total": 29631,
     * "origin_energy_usage": 0,
     * "net_usage": 346
     * },
     * "trigger_info": {
     * "method": "transfer(address _to,uint256 _value)",
     * "parameter": {
     * "_value": "100000",
     * "_to": "TV52XQKBLG74BmRkaZZGNoTQGJA2VVBt2H"
     * },
     * "contract_address": "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t",
     * "call_value": 0
     * },
     * "internal_transactions": {},
     * "tokenTransferInfo": {
     * "icon_url": "https://coin.top/production/logo/usdtlogo.png",
     * "symbol": "USDT",
     * "decimals": 6,
     * "name": "Tether USD",
     * "to_address": "TV52XQKBLG74BmRkaZZGNoTQGJA2VVBt2H",
     * "contract_address": "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t",
     * "type": "Transfer",
     * "from_address": "TQQrSXL8WCxeEfkZPHDrG6qX9MistA1XgV",
     * "amount_str": "100000"
     * },
     * "trc20TransferInfo": [{
     * "icon_url": "https://coin.top/production/logo/usdtlogo.png",
     * "symbol": "USDT",
     * "decimals": 6,
     * "name": "Tether USD",
     * "to_address": "TV52XQKBLG74BmRkaZZGNoTQGJA2VVBt2H",
     * "contract_address": "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t",
     * "type": "Transfer",
     * "from_address": "TQQrSXL8WCxeEfkZPHDrG6qX9MistA1XgV",
     * "amount_str": "100000"
     * }],
     * "info": {},
     * "contract_map": {
     * "TQQrSXL8WCxeEfkZPHDrG6qX9MistA1XgV": false,
     * "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t": true,
     * "TV52XQKBLG74BmRkaZZGNoTQGJA2VVBt2H": false
     * }
     * }
     * <p>
     * TRX 交易
     * {
     * "block": 24163485,
     * "hash": "c31316badcdc39d7f6b7d81c5bf5def623b4b4523a03f7bec5bd594e0b79a352",
     * "timestamp": 1602783831000,
     * "ownerAddress": "TQQrSXL8WCxeEfkZPHDrG6qX9MistA1XgV",
     * "signature_addresses": [],
     * "contractType": 1,
     * "toAddress": "TAj8fiMfB1iQQshHtLGPoFWZCNVdai9fqG",
     * "confirmations": 16084,
     * "confirmed": true,
     * "revert": false,
     * "contractRet": "SUCCESS",
     * "contractData": {
     * "amount": 2000000,
     * "owner_address": "TQQrSXL8WCxeEfkZPHDrG6qX9MistA1XgV",
     * "to_address": "TAj8fiMfB1iQQshHtLGPoFWZCNVdai9fqG"
     * },
     * "data": "",
     * "cost": {
     * "net_fee": 100000,
     * "energy_usage": 0,
     * "energy_fee": 0,
     * "energy_usage_total": 0,
     * "origin_energy_usage": 0,
     * "net_usage": 0
     * },
     * "trigger_info": {},
     * "internal_transactions": "",
     * "info": {},
     * "contract_map": {
     * "TQQrSXL8WCxeEfkZPHDrG6qX9MistA1XgV": false,
     * "TAj8fiMfB1iQQshHtLGPoFWZCNVdai9fqG": false
     * }
     * }
     */
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

    //签名
    public synchronized Message sign(String wallet_name, String pwd, String msg) {
        WalletBean wallet = getWallet(wallet_name, pwd);
        if (wallet == null) {
            return Message.error("密码错误");
        }
        Credential credential = Credential.fromPrivateKey(wallet.getPri());
        String sign = credential.sign(Hash.sha3(msg.getBytes()));
        return Message.ok(sign);
    }

    //查询账户带宽信息
    public synchronized Message getAccountresource(String address) {
        String url = "https://block.dragonschain.cn/wallet/getaccountresource";

        Map<String,String> map=new ArrayMap<>();
        map.put("address", address);
        map.put("visible", "true");
        Gson gson = new Gson();
        String s = gson.toJson(map);
        String post = HttpUtil.post(url, s);
        return Message.ok(AccountResourecBean.newBean(AccountResourecBean.class,post));
    }
}