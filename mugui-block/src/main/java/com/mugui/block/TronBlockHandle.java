package com.mugui.block;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jkkg.hhtx.sql.bean.AssetsLogBean;
import com.jkkg.hhtx.sql.bean.UserWalletBean;
import com.mugui.base.base.Autowired;
import com.mugui.base.base.Component;
import com.mugui.base.client.net.bagsend.BagSend;
import com.mugui.base.client.net.bagsend.HTTPUtil;
import com.mugui.base.client.net.bean.Message;
import com.mugui.base.client.net.classutil.DataSave;
import com.mugui.block.TRC20.Address;
import com.mugui.block.TRC20.Credential;
import com.mugui.block.TRC20.TransactionResult;
import com.mugui.block.TRC20.TronApi;
import com.mugui.block.sql.BlockChainBean;
import com.mugui.block.sql.BlockCoinBean;
import com.mugui.block.sql.BlockSql;
import com.mugui.block.sql.BlockWalletBean;
import com.mugui.block.util.SpUtil;
import com.mugui.robot.mugui_block.R;
import com.mugui.sql.TableMode;

import org.apache.commons.lang3.StringUtils;
import org.tron.common.crypto.Hash;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Component
public class TronBlockHandle implements BlockHandleApi {


    @Autowired
    private TronBlockUtil tronBlockUtil;

    @Autowired
    private BlockSql blockSql;

    private BlockChainBean blockChainBean = null;

    @Getter
    @Setter
    private BlockWalletBean now_wallet;

    @Override
    public void init() {



        if (blockChainBean == null) {
            blockChainBean = new BlockChainBean().setBc_name("Tron");
            BlockChainBean select = blockSql.select(blockChainBean);
            if (select == null) {
                blockChainBean.setBc_description("波场").setBc_name_zh("波场");
                blockChainBean = blockSql.save(blockChainBean);
            } else {
                blockChainBean = select;
            }
            blockChainBean.setBc_down_icon(R.mipmap.icon_tron_up + "");
            blockChainBean.setBc_up_icon(R.mipmap.icon_tron_down + "");

            blockChainBean.setIs_open(false);
            blockSql.updata(blockChainBean);
        }
    }

    @Override
    public String name() {
        return "Tron";
    }

    /**
     * 创建钱包
     *
     * @param pwd
     * @param wallet_name
     * @return
     */
    @Override
    public BlockWalletBean create(String pwd, String wallet_name) {
        BlockWalletBean select = blockSql.select(new BlockWalletBean().setWallet_name(wallet_name));
        if (select != null) {
            throw new RuntimeException("钱包:" + wallet_name + "已存在");
        }
        BlockWalletBean blockWalletBean = tronBlockUtil.create(pwd);
        blockWalletBean.setWallet_name(wallet_name).setBc_id(blockChainBean.getBc_id());
        blockWalletBean.setType(BlockWalletBean.TYPE_0);
        blockWalletBean = blockSql.save(blockWalletBean);
        return now_wallet = blockWalletBean;
    }

    /**
     * 导入钱包
     *
     * @param private_key
     * @param pwd
     * @param wallet_name
     * @return
     */
    @Override
    public BlockWalletBean import_base(String private_key, String pwd, String wallet_name) {
        BlockWalletBean select = blockSql.select(new BlockWalletBean().setWallet_name(wallet_name));
        if (select != null) {
            throw new RuntimeException("钱包:" + wallet_name + "已存在");
        }
        BlockWalletBean blockWalletBean = tronBlockUtil.createByPri(private_key, pwd);

        select = blockSql.select(new BlockWalletBean().setAddress(blockWalletBean.getAddress()));
        if (select != null) {
            throw new RuntimeException("钱包地址已被导入");
        }
        blockWalletBean.setWallet_name(wallet_name).setBc_id(blockChainBean.getBc_id());
        blockWalletBean.setType(BlockWalletBean.TYPE_0);
        blockWalletBean = blockSql.save(blockWalletBean);
        return now_wallet = blockWalletBean;
    }


    /**
     * 导入冷钱包
     *
     * @param private_key
     * @param pwd
     * @param wallet_name
     * @return
     */
    @Override
    public BlockWalletBean import_offline(String private_key, String pwd, String wallet_name) {
        BlockWalletBean select = blockSql.select(new BlockWalletBean().setWallet_name(wallet_name));
        if (select != null) {
            throw new RuntimeException("钱包:" + wallet_name + "已存在");
        }
        BlockWalletBean blockWalletBean = tronBlockUtil.createByPri(private_key, pwd);
        select = blockSql.select(new BlockWalletBean().setAddress(blockWalletBean.getAddress()));
        if (select != null) {
            throw new RuntimeException("钱包地址已被导入");
        }
        blockWalletBean.setWallet_name(wallet_name).setBc_id(blockChainBean.getBc_id());
        blockWalletBean.setType(BlockWalletBean.TYPE_2);
        blockWalletBean = blockSql.save(blockWalletBean);
        return now_wallet = blockWalletBean;
    }

    /**
     * 导入观察钱包
     *
     * @param address
     * @param wallet_name
     * @return
     */
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

    /**
     * 删除钱包
     *
     * @param pwd
     * @return
     */
    @Override
    public boolean delete(String pwd) {
        check();
        String privateStr = tronBlockUtil.getPrivateStr(now_wallet, pwd);
        if (privateStr == null) {
            throw new RuntimeException("密码错误");
        }
        now_wallet = blockSql.select(new BlockWalletBean().setBc_id(blockChainBean.getBc_id()));
        return blockSql.remove(now_wallet);
    }

    /**
     * 通过钱包名获取到这个钱包
     *
     * @param wallet_name
     * @return
     */
    @Override
    public BlockWalletBean get(String wallet_name) {
        return now_wallet = blockSql.select(new BlockWalletBean().setWallet_name(wallet_name));
    }

    @Override
    public BlockWalletBean get(String wallet_name, String pwd) {
        BlockWalletBean blockWalletBean = get(wallet_name);
        if (blockWalletBean != null) {
            blockWalletBean.setPrivate_key(tronBlockUtil.getPrivateStr(blockWalletBean, pwd));
        }
        return now_wallet = blockWalletBean;
    }


    /**
     * 发起交易
     *
     * @param to_address       去向地址
     * @param pwd              密码
     * @param amount           余额
     * @param contract_address 合约地址
     * @return
     */
    @Override
    public Message send(String to_address, String pwd, BigDecimal amount, String contract_address, BigDecimal fee) {
        try {
            check();
            get(now_wallet.getWallet_name(), pwd);
            if (now_wallet == null || StringUtils.isBlank(now_wallet.getPrivate_key())) {
                return Message.error("钱包密码错误");
            }
            if (StringUtils.isNotBlank(contract_address)) {
                TransactionResult transactionResult = mainNet.transformToken(now_wallet.getPrivate_key(), to_address, amount, contract_address);
                if (StringUtils.isNotBlank(transactionResult.txId)) {
                    return Message.ok(transactionResult.txId);
                } else {
                    System.out.println("tron->" + transactionResult.toString());
                }
            } else {
                TransactionResult transactionResult = mainNet.transformTrx(now_wallet.getPrivate_key(), to_address, amount.multiply(new BigDecimal("1000000")).longValue());
                if (StringUtils.isNotBlank(transactionResult.txId)) {
                    return Message.ok(transactionResult.txId);
                }
                System.out.println("tron->" + transactionResult.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error(e.getMessage());
        }
        return Message.error("交易发起失败");
    }

    /**
     * 切换钱包
     */
    private void check() {
        if (now_wallet == null) {
            throw new RuntimeException("未选择钱包");
        }
    }

    @Override
    public Object estimate_fee(BigDecimal amount) {
        check();
        return BigDecimal.ZERO;
    }

    @Override
    public Object estimate_fee(BigDecimal amount, String contract_address) {
        check();
        return BigDecimal.ZERO;
    }

    @Autowired
    private TronApi mainNet;

    @Override
    public BigDecimal balance() {
        return balance(null);
    }

    @Override
    public BigDecimal balance(String contract_address) {
        check();
        String address = now_wallet.getAddress();
        try {
            BigDecimal balance = mainNet.getBalance(address, null);
            return balance;
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }

    @Override
    public String sign(String pwd, String msg) {
        check();
        BlockWalletBean blockWalletBean = get(now_wallet.getWallet_name(), pwd);
        Credential credential = Credential.fromPrivateKey(blockWalletBean.getPrivate_key());
        String sign = credential.sign(Hash.sha3(msg.getBytes()));
        return sign;
    }

    @Override
    public BigDecimal checkSign(String msg) {
        check();
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

            coinBean = new BlockCoinBean().setName("Dragons Chain")
                    .setContract_address("TUXDfjhAuwvgPeGJB8C3bSNhpoz9bPcpRt")
                    .setDecimals(18).setIcon_url("https://coin.top/production/upload/logo/TUXDfjhAuwvgPeGJB8C3bSNhpoz9bPcpRt.png?t=1600772647378")
                    .setSymbol("DC").setPrice(new BigDecimal(0)).setPrice_cny(new BigDecimal(0)).setBlock_wallet_id(now_wallet.getBlock_wallet_id());
            blockSql.save(coinBean);
            coinBean = new BlockCoinBean().setName("TRX")
                    .setDecimals(6).setIcon_url("https://s2.feixiaoquan.com/logo/1/tron.png?x-oss-process=style/coin_36_webp")
                    .setSymbol("TRX").setPrice(new BigDecimal(0)).setPrice_cny(new BigDecimal(0)).setBlock_wallet_id(now_wallet.getBlock_wallet_id());
            blockSql.save(coinBean);
        }
    }

    @Autowired
    BagSend bagSend;

    @Override
    public void updateCoin() {
        bagSend.sendData("");
    }

    @Override
    public void updateAssetsLog() {
        check();
        trc10();
        trc20();
    }


    /**
     * 检索trc20账单
     */
    private void trc20() {
        String url = "https://block.dragonschain.cn/v1/accounts/" + now_wallet.getAddress() + "/transactions/trc20?limit=200&order_by=block_timestamp%2Cdesc";
        while (true) {
            String s = HTTPUtil.get(url).toString();
            JSONObject object = JSONObject.parseObject(s);
            Boolean success = object.getBoolean("success");
            if (!success) {
                return;
            }
            JSONArray data = object.getJSONArray("data");
            Iterator<Object> iterator = data.iterator();
            boolean bool = false;
            while (iterator.hasNext()) {
                JSONObject next = (JSONObject) iterator.next();
                String txID = next.getString("transaction_id");
                AssetsLogBean bean = new AssetsLogBean().setWallet_name(now_wallet.getWallet_name()).setHash(txID);
                AssetsLogBean select1 = blockSql.select(bean);
                if (select1 != null && StringUtils.isNotBlank(select1.getTo())) {
                    return;
                }
                bool = true;
                if (select1 != null) {
                    bean = select1;
                }
                String contractRet = next.getString("type");
                if (!"Transfer".equals(contractRet)) {
                    continue;
                }
                bean.setStatus(AssetsLogBean.status_2);
                //时间
                Long aLong = next.getLong("block_timestamp");
                bean.setTime(new Date(aLong));

                bean.setFrom(next.getString("from"));
                bean.setTo(next.getString("to"));

                JSONObject token_info = next.getJSONObject("token_info");
                BlockCoinBean coinBean = new BlockCoinBean().setBlock_wallet_id(now_wallet.getBlock_wallet_id());
                coinBean.setSymbol(token_info.getString("symbol"));

                BlockCoinBean select = blockSql.select(coinBean);
                if (select == null) {
                    coinBean.setPrice_cny(BigDecimal.ZERO).setPrice(BigDecimal.ZERO).setContract_address(bean.getContract());
                    Message message1 = tronBlockUtil.trc20(bean.getContract());
                    if (message1.getType() != Message.SUCCESS) {
                        continue;
                    }
                    JSONArray trc20_tokens = ((JSONObject) message1.getDate()).getJSONArray("trc20_tokens");
                    if (trc20_tokens.isEmpty()) {
                        continue;
                    }
                    JSONObject jsonObject = trc20_tokens.getJSONObject(0);
                    coinBean.setIcon_url(jsonObject.getString("icon_url")).setDecimals(jsonObject.getInteger("decimals")).setName(jsonObject.getString("symbol"));
                    coinBean.setStatus(1);
                    select = blockSql.save(coinBean);
                }

                bean.setSymbol(select.getSymbol());
                //手续费计算
                if (StringUtils.isBlank(bean.getDetail())) {
                    bean.setDetail("0");
                }
                bean.setNum(next.getBigDecimal("value").divide(new BigDecimal(10).pow(select.getDecimals()), 8, BigDecimal.ROUND_HALF_UP));
                if (select1 != null) {
                    blockSql.updata(bean);
                } else {

                    bean = blockSql.save(bean);
                }
            }
            if (!bool) {
                return;
            }
            JSONObject meta = object.getJSONObject("meta");
            JSONObject links = meta.getJSONObject("links");
            if (links == null || links.isEmpty()) {
                return;
            }
            url = links.getString("next");
        }

    }

    /**
     * 检索trc10账单
     */
    private void trc10() {

        String url = "https://block.dragonschain.cn/v1/accounts/" + now_wallet.getAddress() + "/transactions?limit=200";
        while (true) {
            String s = HTTPUtil.get(url).toString();
            JSONObject object = JSONObject.parseObject(s);
            Boolean success = object.getBoolean("success");
            if (!success) {
                return;
            }
            JSONArray data = object.getJSONArray("data");
            Iterator<Object> iterator = data.iterator();
            boolean bool = false;
            while (iterator.hasNext()) {
                JSONObject next = (JSONObject) iterator.next();
                String txID = next.getString("txID");
                AssetsLogBean bean = new AssetsLogBean().setWallet_name(now_wallet.getWallet_name()).setHash(txID);
                if (blockSql.select(bean) != null) {
                    return;
                }
                bool = true;
                String contractRet = next.getJSONArray("ret").getJSONObject(0).getString("contractRet");
                if (!"SUCCESS".equals(contractRet)) {
                    continue;
                }
                bean.setStatus(AssetsLogBean.status_2);
                JSONObject raw_data = next.getJSONObject("raw_data");
                //时间
                Long aLong = raw_data.getLong("expiration");
                bean.setTime(new Date(aLong));

                JSONObject contract = raw_data.getJSONArray("contract").getJSONObject(0);
                String type = contract.getString("type");
                if ("TransferContract".equals(type)) {
                    BlockCoinBean coinBean = new BlockCoinBean().setSymbol("TRX").setBlock_wallet_id(now_wallet.getBlock_wallet_id());
                    coinBean = blockSql.select(coinBean);
                    bean.setSymbol(coinBean.getSymbol());
                    //手续费计算
                    BigDecimal net_fee = next.getBigDecimal("net_fee");
                    BigDecimal energy_fee = next.getBigDecimal("energy_fee");
                    BigDecimal bigDecimal = new BigDecimal(net_fee.add(energy_fee).toBigInteger().longValue() / Math.pow(10, 6)).setScale(4, BigDecimal.ROUND_HALF_DOWN);
                    bean.setDetail(bigDecimal.stripTrailingZeros().toPlainString());

                    JSONObject parameter = contract.getJSONObject("parameter").getJSONObject("value");
                    BigInteger amount = parameter.getBigInteger("amount");
                    double d = amount.longValue() / Math.pow(10, coinBean.getDecimals());
                    bean.setNum(new BigDecimal(d).setScale(8, BigDecimal.ROUND_HALF_UP));

                    bean.setFrom(Address.encode("0x" + parameter.getString("owner_address")));
                    bean.setTo(Address.encode("0x" + parameter.getString("to_address")));
                } else if ("TriggerSmartContract".equals(type)) {

                    JSONObject parameter = contract.getJSONObject("parameter").getJSONObject("value");
                    bean.setFrom(Address.encode("0x" + parameter.getString("owner_address")));
                    bean.setContract(Address.encode("0x" + parameter.getString("contract_address")));

                    BlockCoinBean coinBean = new BlockCoinBean().setContract_address(bean.getContract()).setBlock_wallet_id(now_wallet.getBlock_wallet_id());
                    BlockCoinBean select = blockSql.select(coinBean);
                    if (select == null) {
                        coinBean.setPrice_cny(BigDecimal.ZERO).setPrice(BigDecimal.ZERO).setContract_address(bean.getContract());
                        Message message1 = tronBlockUtil.trc20(bean.getContract());
                        if (message1.getType() != Message.SUCCESS) {
                            continue;
                        }
                        JSONArray trc20_tokens = ((JSONObject) message1.getDate()).getJSONArray("trc20_tokens");
                        if (trc20_tokens.isEmpty()) {
                            continue;
                        }
                        JSONObject jsonObject = trc20_tokens.getJSONObject(0);
                        coinBean.setIcon_url(jsonObject.getString("icon_url")).setDecimals(jsonObject.getInteger("decimals")).setName(jsonObject.getString("symbol"));
                        coinBean.setStatus(1);
                        select = blockSql.save(coinBean);
                    }

                    bean.setSymbol(select.getSymbol());
                    //手续费计算
                    BigDecimal net_fee = next.getBigDecimal("net_fee");
                    BigDecimal energy_fee = next.getBigDecimal("energy_fee");
                    BigDecimal bigDecimal = new BigDecimal(net_fee.add(energy_fee).toBigInteger().longValue() / Math.pow(10, 6)).setScale(4, BigDecimal.ROUND_HALF_DOWN);
                    bean.setDetail(bigDecimal.stripTrailingZeros().toPlainString());
                    bean.setTo("");
                    bean.setNum(BigDecimal.ZERO);
                } else if ("TransferAssetContract".equals(type)) {
                    JSONObject parameter = contract.getJSONObject("parameter").getJSONObject("value");

                    bean.setFrom(Address.encode("0x" + parameter.getString("owner_address")));
                    bean.setTo(Address.encode("0x" + parameter.getString("to_address")));
                    bean.setContract(parameter.getString("asset_name"));

                    BlockCoinBean coinBean = new BlockCoinBean().setContract_address(bean.getContract()).setBlock_wallet_id(now_wallet.getBlock_wallet_id());
                    BlockCoinBean select = blockSql.select(coinBean);
                    if (select == null) {
                        coinBean.setPrice_cny(BigDecimal.ZERO).setPrice(BigDecimal.ZERO).setContract_address(bean.getContract());
                        Message message1 = tronBlockUtil.trc10(bean.getContract());
                        if (message1.getType() != Message.SUCCESS) {
                            continue;
                        }
                        JSONArray trc20_tokens = ((JSONObject) message1.getDate()).getJSONArray("data");
                        if (trc20_tokens.isEmpty()) {
                            continue;
                        }
                        JSONObject jsonObject = trc20_tokens.getJSONObject(0);
                        coinBean.setIcon_url(jsonObject.getString("imgUrl")).setDecimals(jsonObject.getInteger("precision")).setName(jsonObject.getString("abbr"));
                        coinBean.setSymbol(jsonObject.getString("addr"));
                        coinBean.setStatus(1);
                        select = blockSql.save(coinBean);
                    }
                    if (select.getSymbol() == null) {
                        select.setSymbol(select.getName());
                        blockSql.updata(select);
                    }

                    bean.setSymbol(select.getSymbol());
                    //手续费计算
                    BigDecimal net_fee = next.getBigDecimal("net_fee");
                    BigDecimal energy_fee = next.getBigDecimal("energy_fee");
                    BigDecimal bigDecimal = new BigDecimal(net_fee.add(energy_fee).toBigInteger().longValue() / Math.pow(10, 6)).setScale(4, BigDecimal.ROUND_HALF_DOWN);
                    bean.setDetail(bigDecimal.stripTrailingZeros().toPlainString());
                    BigInteger amount = parameter.getBigInteger("amount");
                    double d = amount.longValue() / Math.pow(10, coinBean.getDecimals());
                    bean.setNum(new BigDecimal(d).setScale(8, BigDecimal.ROUND_HALF_UP));
                }
                bean = blockSql.save(bean);
            }
            if (!bool) return;
            JSONObject meta = object.getJSONObject("meta");
            JSONObject links = meta.getJSONObject("links");
            if (links == null || links.isEmpty()) {
                return;
            }
            url = links.getString("next");
        }

    }
}
