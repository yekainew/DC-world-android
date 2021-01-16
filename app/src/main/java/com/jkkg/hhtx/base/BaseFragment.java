package com.jkkg.hhtx.base;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import com.gyf.immersionbar.components.SimpleImmersionOwner;
import com.gyf.immersionbar.components.SimpleImmersionProxy;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.utils.AppManager;
import com.jkkg.hhtx.utils.StringUtils;
import com.jkkg.hhtx.utils.ToastUtil;
import com.jkkg.hhtx.widget.Gloading;
import com.jkkg.hhtx.widget.LoadingProgressbar;
import com.mugui.base.client.net.classutil.DataSave;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Method;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;


public abstract class BaseFragment extends Fragment implements  SimpleImmersionOwner {
    protected final String TAG = this.getClass().getSimpleName();
    protected Context mContext;
    public boolean isViewCreated; // 界面是否已创建完成
    public boolean isVisibleToUser; // 是否对用户可见
    public boolean isDataLoaded; // 数据是否已请求
    private Unbinder mUnbinder;
    private LoadingProgressbar loadingProgressbar;
    private Gloading.Holder mHolder;
    public View rootView;

    /**
     * ImmersionBar代理类
     */
    private SimpleImmersionProxy mSimpleImmersionProxy = new SimpleImmersionProxy(this);


    @Override
    public void initImmersionBar() {

    }

    /**
     * 是否可以实现沉浸式，当为true的时候才可以执行initImmersionBar方法
     * Immersion bar enabled boolean.
     *
     * @return the boolean
     */
    @Override
    public boolean immersionBarEnabled() {
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariable();
        DataSave.context.putBean(this);
    }

    private void initVariable() {
        isViewCreated = false;
        isVisibleToUser = false;
        isDataLoaded = false;
    }

    /**
     * 初始化 View
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    protected abstract View initLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    /**
     * 初始化 View
     * @return
     */
    protected abstract void initView(View view, @Nullable Bundle savedInstanceState);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=initLayout(inflater, container, savedInstanceState);
        if (rootView != null){
            mUnbinder = ButterKnife.bind(this, rootView);
        }
        if (mContext instanceof BaseActivity) {
            loadingProgressbar = ((BaseActivity) mContext).loadingProgressbar;
            mHolder=((BaseActivity) mContext).mHolder;
        }
        if (haveAnnotation(this)){
            EventBus.getDefault().register(this);//注册到事件主线
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view,savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSimpleImmersionProxy.onActivityCreated(savedInstanceState);

    }

    /**
     * 第一次可见时触发调用,此处实现具体的数据请求逻辑
     */
    public  void lazyLoadData(){

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mSimpleImmersionProxy.onHiddenChanged(hidden);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mSimpleImmersionProxy.onConfigurationChanged(newConfig);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        tryLoadData();
    }

    /**
     * 保证在initData后触发
     */
    @Override
    public void onResume() {
        super.onResume();
        isViewCreated = true;
        if (getUserVisibleHint()) {
            setUserVisibleHint(true);
        }
    }

    /**
     * ViewPager场景下，判断父fragment是否可见
     */
    private boolean isParentVisible() {
        Fragment fragment = getParentFragment();
        return fragment == null || (fragment instanceof BaseFragment && ((BaseFragment) fragment).isVisibleToUser);
    }

    /**
     * ViewPager场景下，当前fragment可见时，如果其子fragment也可见，则让子fragment请求数据
     */
    private void dispatchParentVisibleState() {
        FragmentManager fragmentManager = getChildFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments.isEmpty()) {
            return;
        }
        for (Fragment child : fragments) {
            if (child instanceof BaseFragment && ((BaseFragment) child).isVisibleToUser) {
                ((BaseFragment) child).tryLoadData();
            }
        }
    }

    public void tryLoadData() {
        if (isViewCreated && isVisibleToUser && isParentVisible() && !isDataLoaded) {
            lazyLoadData();
            isDataLoaded = true;
            //通知子Fragment请求数据
            dispatchParentVisibleState();
        }
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

    protected void onLoadRetry() {

    }

    /**
     * 在view上显示加载框
     * @param v
     */
    public void showLoadingView(View v) {
        initLoadingStatusViewIfNeed(v);
        mHolder.showLoading();
    }
    /**
     * 在view上显示加载失败
     * @param v
     */
    public void showLoadFailed(View v) {
        initLoadingStatusViewIfNeed(v);
        mHolder.showLoadFailed();
    }
    /**
     * 在view上显示空视图
     * @param v
     */
    public void showEmpty(View v) {
        initLoadingStatusViewIfNeed(v);
        mHolder.showEmpty();
    }
    /**
     * 清除view加载状态
     * @param v
     */
    public void showLoadSuccess(View v) {
        initLoadingStatusViewIfNeed(v);
        mHolder.showLoadSuccess();
    }


    public void showLoading() {
        if (AppManager.getAppManager().getCurrentActivity() == mContext) {
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

    public void showMessage(@NonNull String message) {
        if(StringUtils.isNotEmpty(message)){
            ToastUtil.show(message);
        }
    }

    //此方法只是关闭软键盘
    public void hintSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getActivity().getCurrentFocus() != null) {
            if (getActivity().getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            return AnimationUtils.loadAnimation(getActivity(), R.anim.fade_entry);
        } else {
            return AnimationUtils.loadAnimation(getActivity(), R.anim.fade_exit);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(haveAnnotation(this)){
            //当视图销毁的时候解绑数据
            EventBus.getDefault().unregister(this);
        }
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY) {
            try {
                mUnbinder.unbind();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                //fix Bindings already cleared
                Timber.w("onDestroyView: " + e.getMessage());
            }
        }
    }

    /**
     * {@link org.greenrobot.eventbus.EventBus} 要求注册之前, 订阅者必须含有一个或以上声明 {@link org.greenrobot.eventbus.Subscribe}
     * 注解的方法, 否则会报错, 所以如果要想完成在基类中自动注册, 避免报错就要先检查是否符合注册资格
     *
     * @param subscriber 订阅者
     * @return 返回 {@code true} 则表示含有 {@link org.greenrobot.eventbus.Subscribe} 注解, {@code false} 为不含有
     */
    private boolean haveAnnotation(Object subscriber) {
        boolean skipSuperClasses = false;
        Class<?> clazz = subscriber.getClass();
        //查找类中符合注册要求的方法, 直到Object类
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
                //查看该方法是否含有 Subscribe 注解
                if (method.isAnnotationPresent(org.greenrobot.eventbus.Subscribe.class) && parameterTypes.length == 1) {
                    return true;
                }
            } //end for
            //获取父类, 以继续查找父类中符合要求的方法
            clazz = clazz.getSuperclass();
        }
        return false;
    }

    private boolean isSystemCalss(String name) {
        return name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSimpleImmersionProxy.onDestroy();
        DataSave.context.removeBean(this);
    }
}
