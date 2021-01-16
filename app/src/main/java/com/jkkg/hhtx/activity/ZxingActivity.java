package com.jkkg.hhtx.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.stetho.common.LogUtil;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.base.BaseActivity;
import com.king.zxing.CaptureHelper;
import com.king.zxing.OnCaptureCallback;
import com.king.zxing.ViewfinderView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import org.tron.walletserver.WalletManager;

import butterknife.BindView;


/**
 * 二维码扫描
 */
public class ZxingActivity extends BaseActivity implements OnCaptureCallback {
    @BindView(R.id.surfaceView)
    SurfaceView surfaceView;
    @BindView(R.id.viewfinderView)
    ViewfinderView viewfinderView;
    @BindView(R.id.toolbar_image_left)
    ImageView toolbar_image_left;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private boolean isContinuousScan;
    private CaptureHelper mCaptureHelper;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_zxing; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbar_title.setText(getString(R.string.string_zxing_title));
        isContinuousScan = getIntent().getBooleanExtra(Constants.KEY_IS_CONTINUOUS, false);//是否连续扫描
        mCaptureHelper = new CaptureHelper(this, surfaceView, viewfinderView);
        mCaptureHelper.onCreate();
        mCaptureHelper.vibrate(true)
                .continuousScan(isContinuousScan);
        mCaptureHelper.setOnCaptureCallback(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mCaptureHelper.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    /**
     * 关闭闪光灯（手电筒）
     */
    private void offFlash() {
        Camera camera = mCaptureHelper.getCameraManager().getOpenCamera().getCamera();
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(parameters);
    }

    /**
     * 开启闪光灯（手电筒）
     */
    public void openFlash() {
        Camera camera = mCaptureHelper.getCameraManager().getOpenCamera().getCamera();
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(parameters);
    }


    /**
     * 扫码结果回调
     *
     * @param result 扫码结果
     * @return
     */
    @Override
    public boolean onResultCallback(String result) {
        Intent intent = getIntent();
        try {
            if (WalletManager.isAddressValid(WalletManager.decodeFromBase58Check(result))) {
                if (intent.getAction().equals("3")) {
                    Intent intent1 = new Intent(this, MineTransferActivity.class);
                    intent1.putExtra("result",result);
                    intent1.putExtra("btc","USDT");
                    startActivity(intent1);
                    finish();
                }else{
                    intent.putExtra("result",result);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if(result.startsWith("http")||result.startsWith("HTTP")){
            if (result.indexOf("blockchaindc.io")>=0) {
                // TODO: 2021/1/12 包含
                Intent intent1 = new Intent(this, PublicChainActivity.class);
                intent.putExtra("url",result);
                startActivity(intent1);
            }else{
                final Intent intent3 = new Intent();
                intent3.setAction(Intent.ACTION_VIEW);
                intent3.setData(Uri.parse(result));
                // 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
                // 官方解释 : Name of the component implementing an activity that can display the intent
                if (intent3.resolveActivity(getContext().getPackageManager()) != null) {
                    final ComponentName componentName = intent.resolveActivity(getContext().getPackageManager());
                    LogUtil.d("suyan = " + componentName.getClassName());
                    getContext().startActivity(Intent.createChooser(intent, "请选择浏览器"));
                } else {
                    showMessage("链接错误或无浏览器");
                }
            }
        }

        return true;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCaptureHelper.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCaptureHelper.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCaptureHelper.onDestroy();
    }
}
