package com.mugui.block.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSONObject;
import com.jkkg.hhtx.sql.bean.UserWalletBean;
import com.mugui.base.base.Autowired;
import com.mugui.base.base.Component;
import com.mugui.base.client.net.auto.AutoManager;
import com.mugui.base.client.net.base.Manager;
import com.mugui.base.client.net.classutil.DataSave;
import com.mugui.block.BlockHandleApi;
import com.mugui.block.sql.BlockAssetsBean;
import com.mugui.block.sql.BlockChainBean;
import com.mugui.block.sql.BlockCoinBean;
import com.mugui.block.sql.BlockSql;
import com.mugui.block.sql.BlockWalletBean;
import com.mugui.block.util.Constants;
import com.mugui.sql.JsonBeanAttr;
import com.mugui.sql.TableMode;

import org.bouncycastle.util.encoders.Hex;
import org.tron.walletserver.Wallet;
import org.tron.walletserver.WalletManager;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Component
@AutoManager
public class BlockManager extends Manager<String, BlockHandleApi> {


    @Autowired
    private BlockSql sql;


    @Override
    public boolean init(Object object) {
        sql.createTable(BlockChainBean.class);
        sql.createTable(BlockWalletBean.class);
        sql.createTable(BlockCoinBean.class);
        sql.createTable(BlockAssetsBean.class);
        boolean init = super.init(object);
        try {
            TableMode tableMode = sql.selectSql("PRAGMA  table_info([block_chain])");
            Iterator<Object> iterator = tableMode.getData().iterator();
            boolean equals = false;
            while (iterator.hasNext()) {
                JSONObject next = (JSONObject) iterator.next();
                equals = next.getString("name").equals("bc_down_icon");
                if (equals) break;
            }
            if (!equals) {
                sql.updateSql("ALTER TABLE block_chain ADD COLUMN bc_down_icon varchar(256);");
            }
            tableMode = sql.selectSql("PRAGMA  table_info([block_chain])");
            iterator = tableMode.getData().iterator();
            equals = false;
            while (iterator.hasNext()) {
                JSONObject next = (JSONObject) iterator.next();
                equals = next.getString("name").equals("bc_up_icon");
                if (equals) break;
            }
            if (!equals) {
                sql.updateSql("ALTER TABLE block_chain ADD COLUMN bc_up_icon varchar(256);");
            }
            tableMode = sql.selectSql("PRAGMA  table_info([block_chain])");
            iterator = tableMode.getData().iterator();
            equals = false;
            while (iterator.hasNext()) {
                JSONObject next = (JSONObject) iterator.next();
                equals = next.getString("name").equals("is_open");
                if (equals) break;
            }
            if (!equals) {
                sql.updateSql("ALTER TABLE block_chain ADD COLUMN is_open varchar(5);");
            }
            tableMode = sql.selectSql("PRAGMA  table_info([block_coin])");
            iterator = tableMode.getData().iterator();
            equals = false;
            while (iterator.hasNext()) {
                JSONObject next = (JSONObject) iterator.next();
                equals = next.getString("name").equals("status");
                if (equals) break;
            }
            if (!equals) {
                sql.updateSql("ALTER TABLE block_coin ADD COLUMN status integer;");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }


        for (Class<?> class_name : DataSave.initClassList((Class<?>) object)) {
            if (BlockHandleApi.class.isAssignableFrom(class_name)) {
                BlockHandleApi bean = (BlockHandleApi) DataSave.context.getBean(class_name);
                bean.init();
                map.put(bean.name(), bean);
            }


        }

        {

            Context context = DataSave.app.getApplicationContext();

            SharedPreferences walletPref1 = context.getSharedPreferences("升级", Context.MODE_PRIVATE);
            boolean is_upDate = walletPref1.getBoolean("is_upDate", false);
            if (!is_upDate) {
                BlockChainBean chainBean=new BlockChainBean().setBc_name("Tron");
                BlockChainBean select1 = sql.select(chainBean);
                sql.createTable(UserWalletBean.class);
                List<UserWalletBean> userWalletBeans = sql.selectList(new UserWalletBean());
                for (UserWalletBean userWalletBean : userWalletBeans) {
                    BlockWalletBean select = sql.select(new BlockWalletBean().setWallet_name(userWalletBean.getName()));
                    if (select == null) {
                        Wallet wallet = WalletManager.getWallet(userWalletBean.getName());
                        SharedPreferences walletPref = context.getSharedPreferences(userWalletBean.getName(), Context.MODE_PRIVATE);
                        String privateKeyEncrypted = walletPref.getString(context.getString(com.mugui.robot.mugui_block.R.string.priv_key), "");
                        sql.save(new BlockWalletBean().setWallet_name(userWalletBean.getName()).setAddress(userWalletBean.getAddress()).setAddress_extra("").setBc_id(select1.getBc_id()).setCreate_time(new Date()).setPrivate_key(privateKeyEncrypted).setPublic_key(Hex.toHexString(wallet.getPublicKey())));
                    }
                }
                SharedPreferences.Editor edit = walletPref1.edit();
                edit.putBoolean("is_upDate",true);
                edit.commit();
            }
        }

        BlockWalletBean select = sql.select(new BlockWalletBean().setWallet_name(Constants.getWalletName()));
        if(select!=null) {
            BlockChainBean select1 = sql.select(new BlockChainBean().setBc_id(select.getBc_id()));
            select1.setIs_open(true);
            sql.updata(select1);
        }
        return init;
    }
}
