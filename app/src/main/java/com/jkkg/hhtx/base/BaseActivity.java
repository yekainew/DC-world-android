package com.jkkg.hhtx.base;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.view.InflateException;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.gyf.immersionbar.ImmersionBar;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.activity.FlashActivity;
import com.jkkg.hhtx.activity.MainActivity;
import com.jkkg.hhtx.activity.MemberActivity;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.event.EventImpl;
import com.jkkg.hhtx.utils.AppManager;
import com.jkkg.hhtx.utils.StringUtils;
import com.jkkg.hhtx.utils.ToastUtil;
import com.jkkg.hhtx.widget.Gloading;
import com.jkkg.hhtx.widget.LoadingProgressbar;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;
import com.mugui.base.client.net.classutil.DataSave;
import com.zzhoujay.richtext.RichText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Method;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity{
    protected final String TAG = this.getClass().getSimpleName();
    private Unbinder mUnbinder;
    public LoadingProgressbar loadingProgressbar;
    public Gloading.Holder mHolder;
    private boolean isShowToolbar = true;

    public boolean isShowToolbar() {
        return isShowToolbar;
    }

    public void setShowToolbar(boolean showToolbar) {
        isShowToolbar = showToolbar;
    }


    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    /**
     * ????????? View,
     * @param savedInstanceState
     * @return
     */
    protected abstract int initView(@Nullable Bundle savedInstanceState);

    /**
     * ???????????????
     *
     * @param savedInstanceState
     */
    protected abstract void initData(@Nullable Bundle savedInstanceState);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        int languageCode = Constants.getLanguageCode();
//        initLanguage();
        RichText.initCacheDir(this);
        DataSave.context.putBean(this);
        //????????????
        ImmersionBar.with(this)
                .statusBarDarkFont(false)
                .navigationBarColor(R.color.bg_tab) //?????????????????????????????????
                .navigationBarDarkIcon(false)
                .init();//??????????????????????????????
        try {
            int layoutResID = initView(savedInstanceState);
            //??????initView??????0,?????????????????????setContentView(),??????????????? Bind ButterKnife
            if (layoutResID != 0) {
                setContentView(layoutResID);
                initToolbar();
                //?????????butterknife
                mUnbinder = ButterKnife.bind(this);
            }
        } catch (Exception e) {
            if (e instanceof InflateException) {
                throw e;
            }
            e.printStackTrace();
        }
        loadingProgressbar = new LoadingProgressbar(this);
        initData(savedInstanceState);

        if (haveAnnotation(this)){
            //?????????????????????
            EventBus.getDefault().register(this);
        }

    }



    private void requestWebDownUrl() {
        MyApp.requestSend.sendData("app.info.webDownUrl", "").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {

                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                return Message.ok();
            }
        });
    }
    /**
     * ???????????????
     */
    private void initLanguage() {
        Locale l = null;
        int code = Constants.getLanguageCode();
        //????????????
        if (code == 0) {
            l = Locale.CHINESE;
        }else if (code == 1) {
            //??????
            l = Locale.ENGLISH;
        } else if (code==2) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                l = getResources().getConfiguration().getLocales().get(0);
            } else {
                l = getResources().getConfiguration().locale;
            }
        }
        Resources resources = getApplicationContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        config.locale = l;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList localeList = new LocaleList(l);
            LocaleList.setDefault(localeList);
            config.setLocales(localeList);
            getApplicationContext().createConfigurationContext(config);
        }
        Locale.setDefault(l);
        resources.updateConfiguration(config, dm);
    }

    private void initToolbar() {
        if (isShowToolbar) {
            if (findViewById(R.id.toolbar) != null) {
                if (this instanceof AppCompatActivity) {
                    ((AppCompatActivity) this).setSupportActionBar((Toolbar) this.findViewById(R.id.toolbar));
                    ((AppCompatActivity) this).getSupportActionBar().setDisplayShowTitleEnabled(false);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        this.setActionBar((android.widget.Toolbar) this.findViewById(R.id.toolbar));
                        this.getActionBar().setDisplayShowTitleEnabled(false);
                    }
                }
                ImmersionBar.setTitleBar(this, this.findViewById(R.id.toolbar));
            }
            if (this.findViewById(R.id.toolbar_image_left) != null) {
                this.findViewById(R.id.toolbar_image_left).setOnClickListener(v -> {
                    this.onBackPressed();
                });
            }
        }
    }

    protected void onLoadRetry() {
    }

    protected void initLoadingStatusViewIfNeed(View v) {
        if (mHolder == null) {
            mHolder = Gloading.getDefault().wrap(v).withRetry(new Runnable() {
                @Override
                public void run() {
                    onLoadRetry();
                }
            });
        }
    }
    /**
     * ???view??????????????????
     * @param v
     */
    public void showLoadingView(View v) {
        initLoadingStatusViewIfNeed(v);
        mHolder.showLoading();
    }
    /**
     * ???view?????????????????????
     * @param v
     */
    public void showLoadFailed(View v) {
        initLoadingStatusViewIfNeed(v);
        mHolder.showLoadFailed();
    }
    /**
     * ???view??????????????????
     * @param v
     */
    public void showEmpty(View v) {
        initLoadingStatusViewIfNeed(v);
        mHolder.showEmpty();
    }
    /**
     * ??????view????????????
     * @param v
     */
    public void showLoadSuccess(View v) {
        initLoadingStatusViewIfNeed(v);
        mHolder.showLoadSuccess();
    }

    public void showMessage(@NonNull String message) {
        if(StringUtils.isNotEmpty(message)){
            ToastUtil.show(message);
        }
    }

    public void showLoading() {
        if (AppManager.getAppManager().getCurrentActivity() == this) {
            if (loadingProgressbar != null && !loadingProgressbar.isShowing()) {
                loadingProgressbar.show();
            }
        }
    }

    public void hideLoading() {
        if (loadingProgressbar != null && loadingProgressbar.isShowing()) {
            loadingProgressbar.dismiss();
        }
    }

    //??????????????????????????????
    public void hintSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public void copyData2Clipboard(String value) {
        try {
            //???????????????????????????
            ClipboardManager cm = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
            // ?????????????????????ClipData
            ClipData mClipData = ClipData.newPlainText("Label", value);
            // ???ClipData?????????????????????????????????
            cm.setPrimaryClip(mClipData);
            ToastUtil.show(getString(R.string.string_text_copy_seccess));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginOut(EventImpl.loginOut baseEvent) {
        if(AppManager.getAppManager().getCurrentActivity()==this){
            if(this instanceof MemberActivity
                    ||this instanceof FlashActivity){
                return;
            }
            //????????????
            Constants.loginOut();
//            AppManager.getAppManager().killActivity(this.getClass());
            Intent intent=new Intent(this, MemberActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
//            finish();
            if(!(this instanceof MainActivity)){
                finish();
            }
            overridePendingTransition(R.anim.fade_entry, R.anim.fade_exit);
        }
    }

    public Context getContext(){
        return  this;
    }


    /**
     * {@link org.greenrobot.eventbus.EventBus} ??????????????????, ?????????????????????????????????????????? {@link org.greenrobot.eventbus.Subscribe}
     * ???????????????, ???????????????, ????????????????????????????????????????????????, ???????????????????????????????????????????????????
     *
     * @param subscriber ?????????
     * @return ?????? {@code true} ??????????????? {@link org.greenrobot.eventbus.Subscribe} ??????, {@code false} ????????????
     */
    private boolean haveAnnotation(Object subscriber) {
        boolean skipSuperClasses = false;
        Class<?> clazz = subscriber.getClass();
        //???????????????????????????????????????, ??????Object???
        while (clazz != null && !isSystemCalss(clazz.getName()) && !skipSuperClasses) {
            Method[] allMethods;
            try {
                allMethods = clazz.getDeclaredMethods();
            } catch (Throwable th) {
                try {
                    allMethods = clazz.getMethods();
                }catch (Throwable th2){
                    continue;
                }finally {
                    skipSuperClasses = true;
                }
            }
            for (int i = 0; i < allMethods.length; i++) {
                Method method = allMethods[i];
                Class<?>[] parameterTypes = method.getParameterTypes();
                //??????????????????????????? Subscribe ??????
                if (method.isAnnotationPresent(org.greenrobot.eventbus.Subscribe.class) && parameterTypes.length == 1) {
                    return true;
                }
            } //end for
            //????????????, ?????????????????????????????????????????????
            clazz = clazz.getSuperclass();
        }
        return false;
    }

    private boolean isSystemCalss(String name) {
        return name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.");
    }

    @Override
    protected void onResume() {
        AppManager.getAppManager().setCurrentActivity(this);
        super.onResume();
    }

    @Override
    protected void onStop() {
        if( AppManager.getAppManager().getCurrentActivity()==this){
            AppManager.getAppManager().setCurrentActivity(null);
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if(haveAnnotation(this)){
            //????????????????????????????????????
            EventBus.getDefault().unregister(this);
        }
        RichText.clear(this);
        if(loadingProgressbar!=null&&loadingProgressbar.isShowing()){
            loadingProgressbar.dismiss();
        }
        this.loadingProgressbar=null;
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY) {
            mUnbinder.unbind();
        }
        this.mUnbinder = null;
        AppManager.getAppManager().removeActivity(this);
        super.onDestroy();
        DataSave.context.removeBean(this);
        DataSave.context.clearActivity();
    }
}
