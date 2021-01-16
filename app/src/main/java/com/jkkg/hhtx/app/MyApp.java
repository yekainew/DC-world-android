package com.jkkg.hhtx.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.ASMUtils;
import com.hjq.toast.ToastUtils;
import com.jkkg.hhtx.BuildConfig;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.block.Block;
import com.jkkg.hhtx.sql.bean.AddressBookBean;
import com.jkkg.hhtx.sql.bean.AssetsLogBean;
import com.jkkg.hhtx.sql.bean.CoinBean;
import com.jkkg.hhtx.sql.bean.InvateLogBean;
import com.jkkg.hhtx.sql.bean.UserWalletBean;
import com.jkkg.hhtx.utils.AppManager;
import com.jkkg.hhtx.utils.CrashTree;
import com.jkkg.hhtx.utils.MiitHelper;
import com.jkkg.hhtx.utils.ToastStyle;
import com.meituan.android.walle.WalleChannelReader;
import com.mugui.base.base.ApplicationContext;
import com.mugui.base.base.Autowired;
import com.mugui.base.client.net.baghandle.NetBagModuleManager;
import com.mugui.base.client.net.bagsend.BagSend;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;
import com.mugui.base.client.net.classutil.DataSave;
import com.mugui.block.BlockHandleApi;
import com.mugui.block.manager.BlockManager;
import com.mugui.block.sql.BlockChainBean;
import com.mugui.block.sql.BlockWalletBean;
import com.mugui.sql.SqlModel;
import com.mugui.sql.TableMode;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;


import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import butterknife.ButterKnife;
import cn.hutool.core.date.DateTime;

import cn.hutool.core.thread.ThreadUtil;
import lombok.SneakyThrows;
import timber.log.Timber;

public class MyApp extends Application{
    private static MyApp app;
    private static String oaid;
    private static boolean isSupportOaid;
    private static String channelName ;
    @Autowired
    public static BagSend requestSend;
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, R.color.font_color_text);
                return new MaterialHeader(context).
                        setColorSchemeResources(R.color.refresh_color_1, R.color.refresh_color_2, R.color.refresh_color_3);
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer
                return new ClassicsFooter(context).setPrimaryColorId(R.color.transparent)
                        .setDrawableSize(15)
                        .setTextSizeTitle(12);
            }
        });
    }
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }
    /**
     * 这里会在 {@link MyApp#onCreate} 之前被调用,可以做一些较早的初始化
     * 常用于 MultiDex 以及插件化框架的初始化
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //获取OAID等设备标识符
        MiitHelper miitHelper = new MiitHelper(appIdsUpdater);
        miitHelper.getDeviceIds(this);
    }
    static Handler handler=null;
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            requestWebDownUrl();
        }
    };

    private void requestWebDownUrl() {
        DataSave.context.getBean(BagSend.class).sendData("app.info.webDownUrl", "").son(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                handler.postDelayed(runnable,60*1000);
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                handler.postDelayed(runnable,60*1000);
                return Message.ok();
            }
        });
    }
    @SneakyThrows
    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        AppManager.getAppManager().init(this);
        ToastUtils.init(this);
        ToastUtils.initStyle(new ToastStyle());


        //初始化服务器
        ApplicationContext applicationContext=new ApplicationContext();
        applicationContext.init(getPackageResourcePath());
        DataSave.context=applicationContext;
        DataSave.app=MyApp.this;
        DataSave.context.getBean(NetBagModuleManager.class).init(MyApp.class);
        //测试服http://113.98.201.156:8060/
        //正式服http://47.242.173.217:8060/
        DataSave.context.getBean(BagSend.class).setServerUrl("47.242.173.217:8060");
//        DataSave.context.getBean(BagSend.class).setServerUrl("47.90.94.107:8060");
//        DataSave.context.getBean(BagSend.class).setServerUrl("113.98.201.156:8060");
//        DataSave.context.getBean(BagSend.class).setServerUrl("wobus.cn/api");
        handler=  new Handler(DataSave.app.getMainLooper());
        handler.postDelayed(runnable,60*1000);

        SqlModel sqlModel = new SqlModel();
        String walletName = Constants.getWalletName();
        if(StringUtils.isNotBlank(walletName)){
            BlockWalletBean walletBean=new BlockWalletBean().setWallet_name(walletName);
            walletBean=sqlModel.select(walletBean);
            BlockChainBean chainBean=new BlockChainBean().setBc_id(walletBean.getBc_id());
            chainBean=sqlModel.select(chainBean);
            BlockManager bean = DataSave.context.getBean(BlockManager.class);
            BlockHandleApi blockHandleApi = bean.get(chainBean.getBc_name());
            blockHandleApi.get(walletName);
            blockHandleApi.initBlockCoin();
        }


        {//数据库初始化
            sqlModel.createTable(CoinBean.class);
            sqlModel.createTable(UserWalletBean.class);
            sqlModel.createTable(InvateLogBean.class);
            sqlModel.createTable(AssetsLogBean.class);
            sqlModel.createTable(AddressBookBean.class);
//            try{
//                TableMode tableMode = sqlModel.selectSql("PRAGMA  table_info([assets_log])");
//                Iterator<Object> iterator = tableMode.getData().iterator();
//                boolean equals =false;
//                while(iterator.hasNext()){
//                    JSONObject next =(JSONObject) iterator.next();
//                    equals= next.getString("name").equals("wallet_name");
//                    if(equals)break;
//                }
//                if(!equals) {
//                    sqlModel.updateSql("ALTER TABLE assets_log ADD COLUMN wallet_name varchar(64);");
//                }
//            }catch (Throwable e){
//                e.printStackTrace();
//            }


           /* try{
                TableMode tableMode = sqlModel.selectSql("PRAGMA  table_info([assets_log])");
                Iterator<Object> iterator = tableMode.getData().iterator();
                boolean equals =false;
                while(iterator.hasNext()){
                    JSONObject next =(JSONObject) iterator.next();
                    equals= next.getString("name").equals("block_id");
                    if(equals)break;
                }
                if(!equals) {
                    sqlModel.updateSql("ALTER TABLE assets_log ADD COLUMN block_id Integer;");
                }
            }catch (Throwable e){
                e.printStackTrace();
            }*/
          //  List<CoinBean> coinBeans = sqlModel.selectList(new CoinBean());

//            if (coinBeans.size() == 0) {
//                CoinBean coinBean = new CoinBean().setName("Tether USD").setIssue_time(DateUtils.parseDate("2019-04-16 20:41:20","yyyy-MM-dd HH:mm:ss"))
//                        .setContract_address("TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t")
//                        .setDecimals(6).setIcon_url("https://coin.top/production/logo/usdtlogo.png")
//                        .setIssue_address("THPvaUhoh2Qn2y9THCZML3H815hhFhn5YC").setSymbol("USDT");
//                sqlModel.save(coinBean);
//
//                coinBean = new CoinBean().setName("Dragons Chain").setIssue_time(new DateTime(1600768420000l).toJdkDate())
//                        .setContract_address("TUXDfjhAuwvgPeGJB8C3bSNhpoz9bPcpRt")
//                        .setDecimals(18).setIcon_url("https://coin.top/production/upload/logo/TUXDfjhAuwvgPeGJB8C3bSNhpoz9bPcpRt.png?t=1600772647378")
//                        .setIssue_address("TS3q2DsxKaQ8rQmjeXfTzGWrYvifM1FU2t").setSymbol("DC");
//                sqlModel.save(coinBean);
////                coinBean = new CoinBean().setName("Dragons Chain Bak").setIssue_time(new DateTime(1600768420000l).toJdkDate())
////                        .setContract_address("TTJrdfy6451rDLgxgNBUgg8J2EaFBeZxjZ")
////                        .setDecimals(8).setIcon_url("https://coin.top/production/upload/logo/TTJrdfy6451rDLgxgNBUgg8J2EaFBeZxjZ.png")
////                        .setIssue_address("TS3q2DsxKaQ8rQmjeXfTzGWrYvifM1FU2t").setSymbol("DCB");
////
////                sqlModel.save(coinBean);
//                coinBean = new CoinBean().setName("TRX")
//                        .setDecimals(6).setIcon_url("https://tronscan.org/static/media/TRX.f8b1b685.svg")
//                        .setSymbol("TRX");
//                sqlModel.save(coinBean);
//            }




        }


        //慧子的测试服
//        DataSave.context.getBean(BagSend.class).setServerUrl("31224i499q.qicp.vip");
        DataSave.context.putBean(this);


        initTimber(this);
//        preinitX5WebCore();
        Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
                AppManager.getAppManager().appExit();
            }
        });
    }



    private void initTimber(Application application) {
        if (BuildConfig.TEST) {//Timber初始化
            Timber.plant(new CrashTree());
            ButterKnife.setDebug(true);
        }
    }
    /**
     * 预加载x5内核
     */

 /*   private void preinitX5WebCore() {
        if (!QbSdk.isTbsCoreInited()){
            //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
            QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
                @Override
                public void onViewInitFinished(boolean arg0) {
                    //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                    Timber.w("app  onViewInitFinished is " + arg0);
                }
                @Override
                public void onCoreInitFinished() {
                }
            };
            // 在调用TBS初始化、创建WebView之前进行如下配置
            HashMap map = new HashMap();
            map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
            map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
            QbSdk.initTbsSettings(map);

            // 这个函数内是异步执行所以不会阻塞 App 主线程，这个函数内是轻量级执行所以对 App 启动性能没有影响
            QbSdk.initX5Environment(this, cb);
        }
    }
*/

    /**
     * 在模拟环境中程序终止时会被调用
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    /**
     * 获取程序的Application对象
     */
    public static Application getApp() {
        return app;
    }

    public static String getChannelName() {
        if (channelName == null) {
            channelName = WalleChannelReader.getChannel(getApp(), "1");
        }
        return channelName;
    }

    public static String getOaid() {
        return oaid;
    }

    public static boolean isSupportOaid() {
        return isSupportOaid;
    }

    public static void setIsSupportOaid(boolean enabled) {
        isSupportOaid = enabled;
    }

    private MiitHelper.AppIdsUpdater appIdsUpdater = new MiitHelper.AppIdsUpdater() {
        @Override
        public void OnIdsAvalid(String ids) {
            setIsSupportOaid(true);
            oaid = ids;
        }
    };
    public static MyApp getApplication() {
        return  app;
    }
}
