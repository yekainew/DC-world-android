package com.jkkg.hhtx.blocktask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jkkg.hhtx.activity.MineExtractActivity;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.block.Block;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.AssetsBean;
import com.jkkg.hhtx.sql.bean.CoinBean;
import com.jkkg.hhtx.utils.SpUtil;
import com.mugui.base.base.Autowired;
import com.mugui.base.base.Component;
import com.mugui.base.client.net.auto.AutoTask;
import com.mugui.base.client.net.base.Task;
import com.mugui.base.client.net.base.TaskInterface;
import com.mugui.base.client.net.bean.Message;
import com.mugui.base.client.net.classutil.DataSave;
import com.mugui.base.util.Other;
import com.mugui.block.BlockHandleApi;
import com.mugui.block.manager.BlockManager;
import com.mugui.block.sql.BlockAssetsBean;
import com.mugui.block.sql.BlockChainBean;
import com.mugui.block.sql.BlockCoinBean;
import com.mugui.block.sql.BlockWalletBean;

import java.math.BigDecimal;
import java.util.List;

@Component
@Task(time = 3000)
@AutoTask
public class AssetsBlockTask implements TaskInterface {
    List<CoinBean> list;

    public void init() {
        dao.createTable(BlockAssetsBean.class);
        System.out.println(AssetsBlockTask.class.getName() + " 初始化");
    }

    @Autowired
    private Dao dao;
    @Autowired
    Block block;


    @Autowired
    BlockManager blockManager;

    public void addBlockCoin(BlockWalletBean blockWalletBean) {

        BlockChainBean select1 = dao.select(new BlockChainBean().setBc_id(blockWalletBean.getBc_id()));
        BlockHandleApi blockHandleApi = blockManager.get(select1.getBc_name());
        blockHandleApi.initBlockCoin();

    }


    @Override
    public void run() {
        while (true) {
            try {
                BlockWalletBean select = dao.select(new BlockWalletBean().setWallet_name(Constants.getWalletName()));
                if (select != null) {
                    BlockChainBean select1 = dao.select(new BlockChainBean().setBc_id(select.getBc_id()));
                    BlockHandleApi blockHandleApi = blockManager.get(select1.getBc_name());

                    blockHandleApi.updateCoin();

                    List<BlockCoinBean> blockCoinBeans = dao.selectList(new BlockCoinBean().setBlock_wallet_id(select.getBlock_wallet_id()));

                    for (BlockCoinBean heyueBean : blockCoinBeans) {
                        //刷新余额
                        BigDecimal balance = blockHandleApi.balance(heyueBean.getContract_address());
                        BlockAssetsBean select2 = dao.select(new BlockAssetsBean().setBlock_wallet_id(select.getBlock_wallet_id()).setSymbol(heyueBean.getSymbol()));
                        if(select2==null){
                            select2=new BlockAssetsBean().setBlock_wallet_id(select.getBlock_wallet_id()).setSymbol(heyueBean.getSymbol());
                            select2=dao.save(select2);
                        }
                        select2.setNum(balance);
                        select2.setNum_usd(select2.getNum().multiply(heyueBean.getPrice()));
                        select2.setNum_cny(select2.getNum().multiply(heyueBean.getPrice_cny()));
                        dao.updata(select2);
                    }
                }
//               Run();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Other.sleep(10000);
        }
    }

    private void Run() {
        boolean b = block.existWallet(SpUtil.getInstance(DataSave.app).getString("this_wallet_name", ""));
        if (!b) {
            return;
        }
        for (CoinBean heyueBean : dao.selectList(new CoinBean())) {
            //刷新余额
            updateAssets(heyueBean);
        }
        assetsLogTask.update();
    }

    /**
     * 账单日志任务
     */
    @Autowired
    private AssetsLogTask assetsLogTask;

    private void updateAssets(CoinBean heyueBean) {
        if (heyueBean.getSymbol() == null) {
            heyueBean.setSymbol(heyueBean.getName());
            dao.updata(heyueBean);
        }
        AssetsBean bean;
        AssetsBean select = dao.select(bean = new AssetsBean().setWallet_name(Constants.getWalletName()).setSymbol(heyueBean.getSymbol()));
        if (select == null) {
            bean.setNum(BigDecimal.ZERO).setNum_cny(BigDecimal.ZERO).setNum_usd(BigDecimal.ZERO);
            select = dao.save(bean);
        }
        if (heyueBean.getPrice() == null) {
            heyueBean.setPrice(BigDecimal.ZERO).setPrice_cny(BigDecimal.ZERO);
            dao.updata(heyueBean);
        }


        Message balance = block.balance(SpUtil.getInstance(DataSave.app).getString("this_wallet_name", ""), heyueBean.getContract_address());
        if (balance.getType() == Message.SUCCESS) {
            select.setNum(new BigDecimal(balance.getDate().toString()));
            select.setNum_usd(select.getNum().multiply(heyueBean.getPrice()));
            select.setNum_cny(select.getNum().multiply(heyueBean.getPrice_cny()));
            dao.updata(select);
        }
    }
}
