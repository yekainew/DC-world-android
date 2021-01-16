package com.mugui.block.btc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.jkkg.hhtx.sql.bean.AssetsLogBean;
import com.mugui.base.base.Autowired;
import com.mugui.base.base.Component;
import com.mugui.base.client.net.bagsend.HTTPUtil;
import com.mugui.base.client.net.bean.Message;
import com.mugui.base.util.Other;
import com.mugui.block.BlockHandleApi;
import com.mugui.block.btc.bean.BTCFee;
import com.mugui.block.btc.bean.BTCUnspentBean;
import com.mugui.block.btc.bean.RetBalanceBean;
import com.mugui.block.btc.bean.RetMsgBean;
import com.mugui.block.btc.bean.RetTranLogBean;
import com.mugui.block.btc.bean.RetTranLogBriefBean;
import com.mugui.block.sql.BlockChainBean;
import com.mugui.block.sql.BlockCoinBean;
import com.mugui.block.sql.BlockSql;
import com.mugui.block.sql.BlockWalletBean;

import org.apache.commons.lang3.StringUtils;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionOutPoint;
import org.bitcoinj.core.UTXO;
import org.bitcoinj.script.Script;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Component
public class BtcBlockHandle implements BlockHandleApi {


    @Autowired
    private BlockSql blockSql;

    private BlockChainBean blockChainBean = null;

    @Getter
    @Setter
    private BlockWalletBean now_wallet;

    @Override
    public void init() {
        if (blockChainBean == null) {
            blockChainBean = new BlockChainBean().setBc_name("BTC");
            BlockChainBean select = blockSql.select(blockChainBean);
            if (select == null) {
                blockChainBean.setBc_description("比特币").setBc_name_zh("比特币");
                blockChainBean = blockSql.save(blockChainBean);
            } else {
                blockChainBean = select;
            }
            blockChainBean.setBc_down_icon(com.mugui.robot.mugui_block.R.mipmap.icon_tron_up + "");
            blockChainBean.setBc_up_icon(com.mugui.robot.mugui_block.R.mipmap.icon_tron_down + "");
            blockChainBean.setIs_open(false);
            blockSql.updata(blockChainBean);
        }

    }

    @Override
    public String name() {
        return "BTC";
    }


    @Autowired
    private BTCBlockUtil btcBlockUtil;

    @Override
    public BlockWalletBean create(String pwd, String wallet_name) {
        BlockWalletBean select = blockSql.select(new BlockWalletBean().setWallet_name(wallet_name));
        if (select != null) {
            throw new RuntimeException("钱包:" + wallet_name + "已存在");
        }
        BlockWalletBean blockWalletBean = btcBlockUtil.create(pwd);
        blockWalletBean.setWallet_name(wallet_name).setBc_id(blockChainBean.getBc_id());
        blockWalletBean.setType(BlockWalletBean.TYPE_0);
        blockWalletBean = blockSql.save(blockWalletBean);
        return now_wallet = blockWalletBean;
    }


    @Override
    public BlockWalletBean import_base(String private_key, String pwd, String wallet_name) {
        BlockWalletBean select = blockSql.select(new BlockWalletBean().setWallet_name(wallet_name));
        if (select != null) {
            throw new RuntimeException("钱包:" + wallet_name + "已存在");
        }
        BlockWalletBean blockWalletBean = btcBlockUtil.createByPri(private_key, pwd);

        select = blockSql.select(new BlockWalletBean().setAddress(blockWalletBean.getAddress()));
        if (select != null) {
            throw new RuntimeException("钱包地址已被导入");
        }
        blockWalletBean.setWallet_name(wallet_name).setBc_id(blockChainBean.getBc_id());
        blockWalletBean.setType(BlockWalletBean.TYPE_0);
        blockWalletBean = blockSql.save(blockWalletBean);
        return now_wallet = blockWalletBean;
    }

    @Override
    public BlockWalletBean import_offline(String private_key, String pwd, String wallet_name) {
        BlockWalletBean select = blockSql.select(new BlockWalletBean().setWallet_name(wallet_name));
        if (select != null) {
            throw new RuntimeException("钱包:" + wallet_name + "已存在");
        }
        BlockWalletBean blockWalletBean = btcBlockUtil.createByPri(private_key, pwd);
        select = blockSql.select(new BlockWalletBean().setAddress(blockWalletBean.getAddress()));
        if (select != null) {
            throw new RuntimeException("钱包地址已被导入");
        }
        blockWalletBean.setWallet_name(wallet_name).setBc_id(blockChainBean.getBc_id());
        blockWalletBean.setType(BlockWalletBean.TYPE_2);
        blockWalletBean = blockSql.save(blockWalletBean);
        return now_wallet = blockWalletBean;
    }

    @Override
    public BlockWalletBean import_observed(String address, String wallet_name) {
        BlockWalletBean select = blockSql.select(new BlockWalletBean().setWallet_name(wallet_name));
        if (select != null) {
            throw new RuntimeException("钱包:" + wallet_name + "已存在");
        }
        select = blockSql.select(new BlockWalletBean().setAddress(address));
        if (select != null) {
            throw new RuntimeException("钱包地址已被导入");
        }
        BlockWalletBean blockWalletBean = new BlockWalletBean();
        blockWalletBean.setAddress(address).setWallet_name(wallet_name).setBc_id(blockChainBean.getBc_id()).setType(BlockWalletBean.TYPE_1);
        blockWalletBean = blockSql.save(blockWalletBean);
        return now_wallet = blockWalletBean;
    }

    @Override
    public boolean delete(String pwd) {
        check();
        String privateStr = btcBlockUtil.getPrivateStr(now_wallet, pwd);
        if (privateStr == null) {
            throw new RuntimeException("密码错误");
        }
        now_wallet = blockSql.select(new BlockWalletBean().setBc_id(blockChainBean.getBc_id()));
        return blockSql.remove(now_wallet);
    }


    @Override
    public BlockWalletBean get(String wallet_name) {
        return now_wallet = blockSql.select(new BlockWalletBean().setWallet_name(wallet_name));
    }

    @Override
    public BlockWalletBean get(String wallet_name, String pwd) {
        BlockWalletBean blockWalletBean = get(wallet_name);
        if (blockWalletBean != null) {
            blockWalletBean.setPrivate_key(btcBlockUtil.getPrivateStr(blockWalletBean, pwd));
        }
        return now_wallet = blockWalletBean;
    }

    private void check() {
        if (now_wallet == null) {
            throw new RuntimeException("未选择钱包");
        }
    }

    public List<BTCUnspentBean> unspent() {
        check();
        Object o = HTTPUtil.get("http://www.tokenview.com:8088/unspent/btc/" + now_wallet.getAddress() + "/1/50");

        LinkedList<BTCUnspentBean> linkedList = new LinkedList<>();
        RetMsgBean retMsgBean = RetMsgBean.newBean(RetMsgBean.class, o);
        if (retMsgBean.getCode() == RetMsgBean.SUCESS) {
            JSONArray objects = JSON.parseArray(retMsgBean.getData().toString());
            Iterator<Object> iterator = objects.iterator();
            while (iterator.hasNext()) {
                Object next = iterator.next();
                BTCUnspentBean btcUnspentBean = BTCUnspentBean.newBean(BTCUnspentBean.class, next);
                linkedList.add(btcUnspentBean);
            }
        }
        return linkedList;
    }


    @Override
    public Message send(String to_address, String pwd, BigDecimal amount, String contract_address, BigDecimal fee) {
        check();
        String s = offlineSign(to_address, pwd, amount, contract_address, fee);
        HashMap object = new HashMap();
        object.put("Content-Type", "application/json");
        //广播交易
        String post = HTTPUtil.post("https://wallet.tokenview.com/onchainwallet/btc", object, "{\"jsonrpc\": \"2.0\", \"id\":\"viewtoken\", \"method\": \"sendrawtransaction\", \"params\": [\"" + s + "\"] }");
        JSONObject object1 = JSONObject.parseObject(post);
        String result = object1.getString("result");
        if (StringUtils.isNotBlank(result)) {
            return Message.ok(result, "交易发送成功");
        }
        return Message.error("交易失败" + object1.getJSONObject("error").getString("message"));
    }

    public String offlineSign(String to_address, String pwd, BigDecimal amount, String contract_address, BigDecimal fee) {
        if (!Other.isInteger(contract_address)) {
            BigDecimal total = amount;
            if (fee != null && fee.compareTo(BigDecimal.ZERO) > 0) {
                total = total.add(fee);
            } else {
                fee = compute_fee(1, 2, 15);
                amount = total.subtract(fee);
            }
            Transaction transaction = new Transaction(btcBlockUtil.networkParameters);
            ECKey ecKey = btcBlockUtil.getECKey(pwd, now_wallet);
            List<UTXO> list = new ArrayList<>();
            List<BTCUnspentBean> unspent = unspent();
            BigDecimal totalMoney = BigDecimal.ZERO;
            BigDecimal total_max = total.multiply(big10000000);
            for (BTCUnspentBean us : unspent) {
                if (total_max.compareTo(totalMoney) >= 0) {
                    break;
                }
                UTXO utxo = new UTXO(Sha256Hash.wrap(us.getTxid()), us.getOutput_no(), Coin.valueOf(us.getValue().longValue()),
                        us.getBlock_no(), false, new Script(Hex.decode(us.getHex())));
                list.add(utxo);
                totalMoney = totalMoney.add(us.getValue());
            }
            if (total_max.compareTo(totalMoney) < 0) {
                throw new RuntimeException("余额不足");
            }
            transaction.addOutput(Coin.valueOf(amount.multiply(big10000000).longValue()), Address.fromBase58(btcBlockUtil.networkParameters, to_address));

            BigDecimal ret_self = totalMoney.subtract(total_max);
            if (ret_self.compareTo(BigDecimal.ZERO) > 0) {
                transaction.addOutput(Coin.valueOf(ret_self.longValue()), Address.fromBase58(btcBlockUtil.networkParameters, now_wallet.getAddress()));
            }
            for (UTXO utxo : list) {
                TransactionOutPoint outPoint = new TransactionOutPoint(btcBlockUtil.networkParameters, utxo.getIndex(), utxo.getHash());
                transaction.addSignedInput(outPoint, utxo.getScript(), ecKey, Transaction.SigHash.ALL, true);
            }
            return Hex.toHexString(transaction.bitcoinSerialize());
        }
        throw new RuntimeException("签名失败");
    }

    @Override
    public Object estimate_fee(BigDecimal amount) {
        return estimate_fee(amount, null);
    }

    @Override
    public Object estimate_fee(BigDecimal amount, String contract_address) {
        Object o = HTTPUtil.get("https://bitcoinfees.earn.com/api/v1/fees/list");
        JSONArray fees = JSONObject.parseObject(o.toString()).getJSONArray("fees");

        HashMap<Integer, Integer> map = new HashMap<>();

        Iterator<Object> iterator = fees.iterator();
        while (iterator.hasNext()) {
            JSONObject next = (JSONObject) iterator.next();
            map.put(next.getInteger("minFee"), next.getInteger("maxMinutes"));
            map.put(next.getInteger("maxFee"), next.getInteger("maxMinutes"));
        }

        return map;
    }

    /**
     * 计算手续费
     *
     * @param from
     * @param to
     * @param fee_scale
     * @return
     */
    public BigDecimal compute_fee(long from, long to, long fee_scale) {
        long x = from * 148 + to * 34 + 10;
        return new BigDecimal(x * fee_scale).divide(big10000000);
    }

    /**
     * 比特币换算比例
     */
    public static final BigDecimal big10000000 = new BigDecimal("10000000");

    @Override
    public BigDecimal balance() {
        return balance(null);
    }

    @Override
    public BigDecimal balance(String contract_address) {
        check();
        RetMsgBean retMsgBean = null;
        if (!Other.isInteger(contract_address)) {
            Object o = HTTPUtil.get("http://www.tokenview.com:8088/address/btc/" + now_wallet.getAddress() + "/1/1");
            retMsgBean = RetMsgBean.newBean(RetMsgBean.class, o);

        } else if (Integer.parseInt(contract_address) == 31) {
            Object o = HTTPUtil.get("http://www.tokenview.com:8088/address/usdt/" + now_wallet.getAddress() + "/1/1");
            retMsgBean = RetMsgBean.newBean(RetMsgBean.class, o);
        } else {
            return BigDecimal.ZERO;
        }
        if (retMsgBean.getCode() == RetMsgBean.SUCESS) {
            RetBalanceBean retBalanceBean = RetBalanceBean.newBean(RetBalanceBean.class, retMsgBean.getData());
            return retBalanceBean.getSpend().add(retBalanceBean.getReceive());
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String sign(String pwd, String msg) {
        return null;
    }

    @Override
    public BigDecimal checkSign(String msg) {
        return null;
    }

    @Override
    public void initBlockCoin() {
        check();
        List<BlockCoinBean> blockCoinBeans = blockSql.selectList(new BlockCoinBean().setBlock_wallet_id(now_wallet.getBlock_wallet_id()));
        if (blockCoinBeans.isEmpty()) {
            BlockCoinBean coinBean = new BlockCoinBean().setName("Tether USD")
                    .setContract_address("TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t")
                    .setDecimals(6).setIcon_url("https://coin.top/production/logo/usdtlogo.png")
                    .setSymbol("USDT").setPrice(new BigDecimal(0)).setPrice_cny(new BigDecimal(0)).setBlock_wallet_id(now_wallet.getBlock_wallet_id());
            blockSql.save(coinBean);
            coinBean = new BlockCoinBean().setName("BTC")
                    .setDecimals(8).setIcon_url("https://s2.feixiaoquan.com/logo/1/bitcoin.png?x-oss-process=style/coin_36_webp")
                    .setSymbol("BTC").setPrice(new BigDecimal(0)).setPrice_cny(new BigDecimal(0)).setBlock_wallet_id(now_wallet.getBlock_wallet_id());
            blockSql.save(coinBean);
        }
    }

    @Override
    public void updateCoin() {

    }

    /**
     * BTC ,USDT 日志
     */
    @Override
    public void updateAssetsLog() {
        Log("usdt");
        Log("btc");
    }

    private void Log(String coin_type) {

        int page_num = 1;
        int page_size = 50;
        while (true) {
            Object o = HTTPUtil.get("http://www.tokenview.com:8088/address/" + coin_type + "/" + now_wallet.getAddress() + "/" + page_num + "/" + page_size);
            RetMsgBean retMsgBean = RetMsgBean.newBean(RetMsgBean.class, o);
            if (retMsgBean.getCode() != RetMsgBean.SUCESS) {
                return;
            }
            JSONArray objects = JSONArray.parseArray(retMsgBean.getData().toString());
            RetTranLogBriefBean retTronLogBriefBean = RetTranLogBriefBean.newBean(RetTranLogBriefBean.class, objects.get(0));
            Iterator<Object> iterator = retTronLogBriefBean.getTxs().iterator();
            while (iterator.hasNext()) {
                Object next = iterator.next();
                RetTranLogBean retTranLogBean = RetTranLogBean.newBean(RetTranLogBean.class, next);
                AssetsLogBean assetsLogBean = new AssetsLogBean();
                assetsLogBean.setHash(retTranLogBean.getTxid());
                assetsLogBean.setWallet_name(now_wallet.getWallet_name());
                AssetsLogBean select = blockSql.select(assetsLogBean);
                if (select != null) {
                    if (select.getStatus() >= 2) {
                        continue;
                    }
                    if (!select.getSymbol().equals(coin_type.toUpperCase())) {
                        continue;
                    }
                    assetsLogBean = select;
                } else {
                    assetsLogBean = blockSql.save(assetsLogBean);
                }
                assetsLogBean.setSymbol(coin_type.toUpperCase());
                assetsLogBean.setStatus(AssetsLogBean.status_2);
                if (coin_type.equals("usdt")) {
                    assetsLogBean.setContract("31");
                }
                assetsLogBean.setLog_type(0);
                assetsLogBean.setTime(new Date(retTranLogBean.getTime() * 1000));
                {
                    JSONArray inputs = retTranLogBean.getInputs();
                    Iterator<Object> iterator1 = inputs.iterator();
                    BigDecimal num = BigDecimal.ZERO;
                    String from = null;
                    while (iterator1.hasNext()) {
                        Object next1 = iterator1.next();
                        RetTranLogBean.Inputs inputs1 = RetTranLogBean.Inputs.newBean(RetTranLogBean.Inputs.class, next1);
                        num = num.add(inputs1.getValue());
                        if (inputs1.getAddress().equals(now_wallet.getAddress())) {
                            if (StringUtils.isBlank(assetsLogBean.getFrom())) {
                                assetsLogBean.setFrom(now_wallet.getAddress());
                            }
                            break;
                        } else {
                            from = inputs1.getAddress();
                        }
                    }
                    if (StringUtils.isBlank(assetsLogBean.getFrom())) {
                        assetsLogBean.setFrom(from);
                    }
                    assetsLogBean.setNum(num);
                }
                {
                    JSONArray inputs = retTranLogBean.getOutputs();
                    Iterator<Object> iterator1 = inputs.iterator();
                    BigDecimal num = BigDecimal.ZERO;
                    String to = null;
                    while (iterator1.hasNext()) {
                        Object next1 = iterator1.next();
                        RetTranLogBean.Outputs inputs1 = RetTranLogBean.Outputs.newBean(RetTranLogBean.Outputs.class, next1);
                        num = num.add(inputs1.getValue());
                        if (inputs1.getAddress().equals(now_wallet.getAddress())) {
                            if (StringUtils.isBlank(assetsLogBean.getTo())) {
                                assetsLogBean.setTo(now_wallet.getAddress());
                            }
                            break;
                        } else {
                            to = inputs1.getAddress();
                        }
                    }
                    if (StringUtils.isBlank(assetsLogBean.getTo())) {
                        assetsLogBean.setTo(to);
                    }
                    assetsLogBean.setNum(num);
                }
                blockSql.updata(assetsLogBean);
            }
        }

    }


}
