package com.jkkg.hhtx.blocktask;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.block.Block;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.AssetsLogBean;
import com.jkkg.hhtx.sql.bean.CoinBean;
import com.jkkg.hhtx.sql.bean.UserWalletBean;
import com.jkkg.hhtx.utils.SpUtil;
import com.mugui.base.base.Autowired;
import com.mugui.base.base.Component;
import com.mugui.base.bean.DefaultJsonBean;
import com.mugui.base.client.net.auto.AutoTask;
import com.mugui.base.client.net.bagsend.HTTPUtil;
import com.mugui.base.client.net.base.Task;
import com.mugui.base.client.net.base.TaskInterface;
import com.mugui.base.client.net.bean.Message;
import com.mugui.base.client.net.classutil.DataSave;
import com.mugui.base.util.Other;
import com.mugui.block.BlockHandleApi;
import com.mugui.block.TRC20.Address;
import com.mugui.block.manager.BlockManager;
import com.mugui.block.sql.BlockChainBean;
import com.mugui.block.sql.BlockWalletBean;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;

import static com.jkkg.hhtx.utils.TryParse.getBigDecimal;

/**
 * 账单日志
 */
@AutoTask
@Task(time = 3000)
@Component
public class AssetsLogTask implements TaskInterface {

    public void init() {
        System.out.println(AssetsLogTask.class.getName() + "初始化");
        dao.createTable(AssetsLogBean.class);
    }

    @Autowired
    private Dao dao;


    @Autowired
    private BlockManager blockManager;


    @Override
    public void run() {
        while (true) {
            try {

//                handle();

                String walletName = Constants.getWalletName();
               BlockWalletBean blockWalletBeans = dao.select(new BlockWalletBean().setWallet_name(walletName));
                if(blockWalletBeans!=null){
                    BlockChainBean select = dao.select(new BlockChainBean().setBc_id(blockWalletBeans.getBc_id()));
                    BlockHandleApi blockHandleApi = blockManager.get(select.getBc_name());
                    blockHandleApi.updateAssetsLog();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Other.sleep(60000);
        }
    }

    private void handle() throws InterruptedException {
        while (true) {
            AssetsLogBean poll = linkedList.poll();
            if (poll == null) {
                synchronized (linkedList) {
                    if ((poll = linkedList.poll()) == null) {
                        linkedList.wait();
                        continue;
                    }
                }
            }
            if (poll.getLog_type() == AssetsLogBean.log_type_0) {
                //updateLogByType0(poll);
                trc10();
                trc20();

            }

        }

    }

    /**
     * 检索trc20账单
     */
    private void trc20() {
        UserWalletBean this_wallet_name = dao.select(new UserWalletBean().setName(SpUtil.getInstance(DataSave.app).getString("this_wallet_name", "")));
        String url="https://block.dragonschain.cn/v1/accounts/"+this_wallet_name.getAddress()+"/transactions/trc20?limit=200&order_by=block_timestamp%2Cdesc";
        while(true){
            String s = HTTPUtil.get(url).toString();
            JSONObject object = JSONObject.parseObject(s);
            Boolean success = object.getBoolean("success");
            if(!success){
                return;
            }
            JSONArray data = object.getJSONArray("data");
            Iterator<Object> iterator = data.iterator();
            boolean bool=false;
            while(iterator.hasNext()){
                JSONObject next = (JSONObject)iterator.next();
                String txID = next.getString("transaction_id");
                AssetsLogBean bean=new AssetsLogBean().setWallet_name(this_wallet_name.getName()).setHash(txID);
                AssetsLogBean select1 = dao.select(bean);
                if(select1!=null&& StringUtils.isNotBlank(select1.getTo())){
                    return;
                }
                bool=true;
                if(select1!=null){
                    bean=select1;
                }
                String contractRet = next.getString("type");
                if(!"Transfer".equals(contractRet)){
                    continue;
                }
                bean.setStatus(AssetsLogBean.status_2);
                //时间
                Long aLong = next.getLong("block_timestamp");
                bean.setTime(new Date(aLong));

                bean.setFrom(next.getString("from"));
                bean.setTo(next.getString("to"));

                JSONObject token_info = next.getJSONObject("token_info");
                CoinBean coinBean=new CoinBean().setSymbol(token_info.getString("symbol"));
                CoinBean select = dao.select(coinBean);
                if (select == null) {
                    coinBean.setPrice_cny(BigDecimal.ZERO).setPrice(BigDecimal.ZERO).setContract_address(bean.getContract());
                    Message message1 = block.trc20(bean.getContract());
                    if (message1.getType() != Message.SUCCESS) {
                        continue;
                    }
                    JSONArray trc20_tokens = ((JSONObject) message1.getDate()).getJSONArray("trc20_tokens");
                    if (trc20_tokens.isEmpty()) {
                        continue;
                    }
                    JSONObject jsonObject = trc20_tokens.getJSONObject(0);
                    coinBean.setIcon_url(jsonObject.getString("icon_url")).setDecimals(jsonObject.getInteger("decimals")).setName(jsonObject.getString("symbol")).setIssue_address(jsonObject.getString("issue_address")).setIssue_time(jsonObject.getDate("issue_time"));
                    select = dao.save(coinBean);
                }

                bean.setSymbol(select.getSymbol());
                //手续费计算
                if(StringUtils.isBlank(bean.getDetail())){
                    bean.setDetail("0");
                }
                bean.setNum(next.getBigDecimal("value").divide(new BigDecimal(10).pow(select.getDecimals()),8,BigDecimal.ROUND_HALF_UP));
                if(select1!=null){
                    dao.updata(bean);
                }else{

                    bean=dao.save(bean);
                }
            }
            if(!bool){
                return;
            }
            JSONObject meta = object.getJSONObject("meta");
            JSONObject links = meta.getJSONObject("links");
            if(links==null||links.isEmpty()){
                return;
            }
            url = links.getString("next");
        }

    }

    /**
     * 检索trc10账单
     */
    private void trc10() {

        UserWalletBean this_wallet_name = dao.select(new UserWalletBean().setName(SpUtil.getInstance(DataSave.app).getString("this_wallet_name", "")));
        String url="https://block.dragonschain.cn/v1/accounts/" + this_wallet_name.getAddress() + "/transactions?limit=200";
        while(true){
            String s = HTTPUtil.get(url).toString();
            JSONObject object = JSONObject.parseObject(s);
            Boolean success = object.getBoolean("success");
            if(!success){
                return;
            }
            JSONArray data = object.getJSONArray("data");
            Iterator<Object> iterator = data.iterator();
            boolean bool=false;
            while(iterator.hasNext()){
                JSONObject next = (JSONObject)iterator.next();
                String txID = next.getString("txID");
                AssetsLogBean bean=new AssetsLogBean().setWallet_name(this_wallet_name.getName()).setHash(txID);
                if(dao.select(bean)!=null){
                    return;
                }
                bool=true;
                String contractRet = next.getJSONArray("ret").getJSONObject(0).getString("contractRet");
                if(!"SUCCESS".equals(contractRet)){
                    continue;
                }
                bean.setStatus(AssetsLogBean.status_2);
                JSONObject raw_data = next.getJSONObject("raw_data");
                //时间
                Long aLong = raw_data.getLong("expiration");
                bean.setTime(new Date(aLong));

                JSONObject contract = raw_data.getJSONArray("contract").getJSONObject(0);
                String type = contract.getString("type");
                if("TransferContract".equals(type)){
                    CoinBean coinBean=new CoinBean().setSymbol("TRX");
                    coinBean=dao.select(coinBean);
                    bean.setSymbol(coinBean.getSymbol());
                    //手续费计算
                    BigDecimal net_fee = next.getBigDecimal("net_fee");
                    BigDecimal energy_fee = next.getBigDecimal("energy_fee");
                    BigDecimal bigDecimal = new BigDecimal(net_fee.add(energy_fee).toBigInteger().longValue() / Math.pow(10,6)).setScale(4, BigDecimal.ROUND_HALF_DOWN);
                    bean.setDetail(bigDecimal.stripTrailingZeros().toPlainString());

                    JSONObject parameter = contract.getJSONObject("parameter").getJSONObject("value");
                    BigInteger amount = parameter.getBigInteger("amount");
                    double d=amount.longValue()/Math.pow(10,coinBean.getDecimals());
                    bean.setNum(new BigDecimal(d).setScale(8,BigDecimal.ROUND_HALF_UP));

                    bean.setFrom(Address.encode("0x"+parameter.getString("owner_address")));
                    bean.setTo(Address.encode("0x"+parameter.getString("to_address")));
                }else if("TriggerSmartContract".equals(type)){

                    JSONObject parameter = contract.getJSONObject("parameter").getJSONObject("value");
                    bean.setFrom(Address.encode("0x"+parameter.getString("owner_address")));
                    bean.setContract(Address.encode("0x"+parameter.getString("contract_address")));

                    CoinBean coinBean=new CoinBean().setContract_address(bean.getContract());
                    CoinBean select = dao.select(coinBean);
                    if (select==null) {
                        coinBean.setPrice_cny(BigDecimal.ZERO).setPrice(BigDecimal.ZERO).setContract_address(bean.getContract());
                        Message message1 = block.trc20(bean.getContract());
                        if(message1.getType()!=Message.SUCCESS){
                            continue;
                        }
                        JSONArray trc20_tokens = ((JSONObject) message1.getDate()).getJSONArray("trc20_tokens");
                        if(trc20_tokens.isEmpty()){
                            continue;
                        }
                        JSONObject jsonObject = trc20_tokens.getJSONObject(0);
                        coinBean.setIcon_url(jsonObject.getString("icon_url")).setDecimals(jsonObject.getInteger("decimals")).setName(jsonObject.getString("symbol")).setIssue_address(jsonObject.getString("issue_address")).setIssue_time(jsonObject.getDate("issue_time"));
                        select= dao.save(coinBean);
                    }

                    bean.setSymbol(select.getSymbol());
                    //手续费计算
                    BigDecimal net_fee = next.getBigDecimal("net_fee");
                    BigDecimal energy_fee = next.getBigDecimal("energy_fee");
                    BigDecimal bigDecimal = new BigDecimal(net_fee.add(energy_fee).toBigInteger().longValue() / Math.pow(10,6)).setScale(4, BigDecimal.ROUND_HALF_DOWN);
                    bean.setDetail(bigDecimal.stripTrailingZeros().toPlainString());
                    bean.setTo("");
                    bean.setNum(BigDecimal.ZERO);
                }
                else if("TransferAssetContract".equals(type)){
                    JSONObject parameter = contract.getJSONObject("parameter").getJSONObject("value");

                    bean.setFrom(Address.encode("0x"+parameter.getString("owner_address")));
                    bean.setTo(Address.encode("0x"+parameter.getString("to_address")));
                    bean.setContract(parameter.getString("asset_name"));

                    CoinBean coinBean=new CoinBean().setContract_address(bean.getContract());
                    CoinBean select = dao.select(coinBean);
                    if (select==null) {
                        coinBean.setPrice_cny(BigDecimal.ZERO).setPrice(BigDecimal.ZERO).setContract_address(bean.getContract());
                        Message message1 = block.trc10(bean.getContract());
                        if(message1.getType()!=Message.SUCCESS){
                            continue;
                        }
                        JSONArray trc20_tokens = ((JSONObject) message1.getDate()).getJSONArray("data");
                        if(trc20_tokens.isEmpty()){
                            continue;
                        }
                        JSONObject jsonObject = trc20_tokens.getJSONObject(0);
                        coinBean.setIcon_url(jsonObject.getString("imgUrl")).setDecimals(jsonObject.getInteger("precision")).setName(jsonObject.getString("abbr")).setIssue_address(jsonObject.getString("ownerAddress")).setIssue_time(jsonObject.getDate("startTime"));
                        coinBean.setSymbol(jsonObject.getString("addr"));
                        select= dao.save(coinBean);
                    }
                    if(select.getSymbol()==null){
                        select.setSymbol(select.getName());
                        dao.updata(select);
                    }

                    bean.setSymbol(select.getSymbol());
                    //手续费计算
                    BigDecimal net_fee = next.getBigDecimal("net_fee");
                    BigDecimal energy_fee = next.getBigDecimal("energy_fee");
                    BigDecimal bigDecimal = new BigDecimal(net_fee.add(energy_fee).toBigInteger().longValue() / Math.pow(10,6)).setScale(4, BigDecimal.ROUND_HALF_DOWN);
                    bean.setDetail(bigDecimal.stripTrailingZeros().toPlainString());
                    BigInteger amount = parameter.getBigInteger("amount");
                    double d=amount.longValue()/Math.pow(10,coinBean.getDecimals());
                    bean.setNum(new BigDecimal(d).setScale(8,BigDecimal.ROUND_HALF_UP));
                }
                bean=dao.save(bean);
            }
            if(!bool) return;
            JSONObject meta = object.getJSONObject("meta");
            JSONObject links = meta.getJSONObject("links");
            if(links==null||links.isEmpty()){
                return;
            }
            url = links.getString("next");
        }

    }

    @Autowired
    private Block block;

    /**
     * 更新账单
     *
     * @param poll
     */
    private void updateLogByType0(AssetsLogBean poll) {
        int start= SpUtil.getInstance(DataSave.app).getInt("assets_log_start",0);
        int size=10;
        while(true){
            int last_start=start;
            Message message = block.tranList(start, size);
            boolean bool=false;
            if (message.getType() == Message.SUCCESS) {
                JSONObject object = JSONObject.parseObject(message.getDate().toString());
                JSONArray data = object.getJSONArray("transfers");
                if (data != null) {
                    Iterator<Object> iterator = data.iterator();
                    while (iterator.hasNext()) {
                        DefaultJsonBean defaultJsonBean = DefaultJsonBean.newBean(DefaultJsonBean.class, iterator.next());
                        String hash = defaultJsonBean.get().getString("hash");
                        AssetsLogBean assetsLogBean = new AssetsLogBean().setHash(hash);
                        assetsLogBean = dao.select(assetsLogBean);
                        if (assetsLogBean != null) {
                            if (assetsLogBean.getStatus() == AssetsLogBean.status_2) {
                                start++;
                                SpUtil.getInstance(DataSave.app).saveInt("assets_log_start",start);
                                continue;
                            }
                            Boolean confirmed = defaultJsonBean.get().getBoolean("confirmed");
                            if (confirmed != null || confirmed)
                                assetsLogBean.setStatus(2);
                            else {
                                assetsLogBean.setStatus(1);
                            }
                            dao.updata(assetsLogBean);
                        } else {
                            assetsLogBean = new AssetsLogBean().setHash(hash);
                            assetsLogBean.setLog_type(AssetsLogBean.log_type_0);
                            assetsLogBean = dao.save(assetsLogBean);
                        }
                        assetsLogBean.setFrom(defaultJsonBean.get().getString("owner_address"));
                        assetsLogBean.setTo(defaultJsonBean.get().getString("to_address"));

                        assetsLogBean.setContract(defaultJsonBean.get().getString("contract_address")).setSymbol(defaultJsonBean.get().getString("tokenAbbr").toUpperCase());

                        Boolean confirmed = defaultJsonBean.get().getBoolean("confirmed");
                        if (confirmed != null || confirmed)
                            assetsLogBean.setStatus(2);
                        else {
                            assetsLogBean.setStatus(1);
                        }
                        CoinBean coinBean = new CoinBean().setSymbol(assetsLogBean.getSymbol());
                        CoinBean select = dao.select(coinBean);
                        if(select==null){
                            coinBean.setPrice_cny(BigDecimal.ZERO).setPrice(BigDecimal.ZERO).setContract_address(assetsLogBean.getContract());
                            Message message1 = block.trc20(assetsLogBean.getContract());
                            if(message1.getType()!=Message.SUCCESS){
                                start++;
                                SpUtil.getInstance(DataSave.app).saveInt("assets_log_start",start);
                                continue;
                            }
                            JSONArray trc20_tokens = ((JSONObject) message1.getDate()).getJSONArray("trc20_tokens");
                            if(trc20_tokens.isEmpty()){
                                    start++;
                                    SpUtil.getInstance(DataSave.app).saveInt("assets_log_start",start);
                                    continue;
                            }
                            JSONObject jsonObject = trc20_tokens.getJSONObject(0);
                            coinBean.setIcon_url(jsonObject.getString("icon_url")).setDecimals(jsonObject.getInteger("decimals")).setName(jsonObject.getString("symbol")).setIssue_address(jsonObject.getString("issue_address")).setIssue_time(jsonObject.getDate("issue_time"));
                            select= dao.save(coinBean);
                        }
                        assetsLogBean.setNum(defaultJsonBean.get().getBigDecimal("amount").divide(new BigDecimal(Math.pow(10, select.getDecimals())+""),8,BigDecimal.ROUND_HALF_DOWN).setScale(4, BigDecimal.ROUND_HALF_DOWN));
                        assetsLogBean.setTime(new Date(defaultJsonBean.get().getLong("date_created")));
//                        assetsLogBean.setBlock_id()
                        JSONObject cost = defaultJsonBean.get().getJSONObject("cost");
                        if (cost!=null) {
                            BigDecimal net_fee = cost.getBigDecimal("net_fee");
                            BigDecimal energy_fee = cost.getBigDecimal("energy_fee");
                            BigDecimal bigDecimal = new BigDecimal(net_fee.add(energy_fee).toBigInteger().longValue() / Math.pow(10, select.getDecimals())).setScale(4, BigDecimal.ROUND_HALF_DOWN);
                            assetsLogBean.setDetail(bigDecimal.stripTrailingZeros().toPlainString());
                        }
                        dao.updata(assetsLogBean);
                        bool=true;
                        start++;
                        SpUtil.getInstance(DataSave.app).saveInt("assets_log_start",start);
                    }
                }
            }
            if(last_start==start||!bool){
                SpUtil.getInstance(DataSave.app).saveInt("assets_log_start",0);
                return;
            }
            SpUtil.getInstance(DataSave.app).saveInt("assets_log_start",start);
        }


    }

    LinkedList<AssetsLogBean> linkedList = new LinkedList<AssetsLogBean>();

    /**
     * 更新账单
     */
    public void update() {
        synchronized (linkedList) {
            linkedList.add(new AssetsLogBean().setLog_type(AssetsLogBean.log_type_0));
            linkedList.notify();
        }
    }
}
