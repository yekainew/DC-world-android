package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.utils.AppManager;
import com.jkkg.hhtx.utils.Base64Utils;
import com.jkkg.hhtx.utils.FileUtils;
import com.jkkg.hhtx.utils.GlideEngine;
import com.jkkg.hhtx.utils.GsonUtil;
import com.jkkg.hhtx.utils.StringUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;

import java.io.File;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 实名认证  上传证件照
 */
public class CertificationImgActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_tv_left)
    TextView toolbarTvLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_image_right)
    ImageView toolbarImageRight;
    @BindView(R.id.toolbar_tv_right)
    TextView toolbarTvRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_front)
    ImageView imgFront;
    @BindView(R.id.img_verso)
    ImageView imgVerso;
    @BindView(R.id.img_hand_cat)
    ImageView imgHandCat;
    @BindView(R.id.btn_true)
    Button btnTrue;
    @BindView(R.id.nsv_mine)
    NestedScrollView nsvMine;
    private String img1;
    private String img2;
    private String img3;
    private String crdid;
    private String name;

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_certification_img;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_mine_safe));

        Intent intent = getIntent();
        crdid = intent.getStringExtra("crdid");
        name = intent.getStringExtra("areaname");

    }

    @OnClick({R.id.img_front, R.id.img_verso, R.id.img_hand_cat, R.id.btn_true})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_front:
                //正面照
                PictureSelector.create(CertificationImgActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .maxSelectNum(1)// 最大图片选择数量 int
                        .minSelectNum(1)// 最小选择数量 int
                        .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                        .isCompress(true)// 是否压缩
                        .compressQuality(80)
                        .forResult(1);
                break;
            case R.id.img_verso:
                //反面
                PictureSelector.create(CertificationImgActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .maxSelectNum(1)// 最大图片选择数量 int
                        .minSelectNum(1)// 最小选择数量 int
                        .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                        .isCompress(true)// 是否压缩
                        .compressQuality(80)
                        .forResult(2);
                break;
            case R.id.img_hand_cat:
                //手持
                PictureSelector.create(CertificationImgActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .maxSelectNum(1)// 最大图片选择数量 int
                        .minSelectNum(1)// 最小选择数量 int
                        .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                        .isCompress(true)// 是否压缩
                        .compressQuality(80)
                        .forResult(3);
                break;
            case R.id.btn_true:
                //提交
                request();
                break;
            default:
                break;
        }
    }

    private void request(){
        showLoading();
        Map<String,String> map=new ArrayMap<>();
        map.put("user_country",name);
        map.put("user_card_photo_just",img1);
        map.put("user_card_photo_back",img2);
        map.put("user_card_photo_hold",img3);
        map.put("user_certificate_no",crdid);
        MyApp.requestSend.sendData("verified1.method.upload", GsonUtil.toJsonString(map)).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                hideLoading();
                showMessage(getString(R.string.string_tip_moderated));
                AppManager.getAppManager().killActivity(CertificationActivity.class);
                finish();
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                hideLoading();
                showMessage(message.getMsg());
                return Message.ok();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // 图片选择结果回调
            // 例如 LocalMedia 里面返回三种path
            // 1.media.getPath(); 为原图path
            // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
            // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
            // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
            if (PictureSelector.obtainMultipleResult(data).size() == 0) {
                return;
            }
            LocalMedia media = PictureSelector.obtainMultipleResult(data).get(0);
            String imagePath = null;
            if (media.isCompressed()) {
                imagePath=media.getCompressPath();
            } else if (!TextUtils.isEmpty(media.getAndroidQToPath())) {
                imagePath=media.getAndroidQToPath();
            } else {
                if (!TextUtils.isEmpty(media.getPath())) {
                    String uri_path = FileUtils.getPAth(getContext(), Uri.parse(media.getPath()));
                    if (!TextUtils.isEmpty(uri_path)) {
                        imagePath=uri_path;
                    }
                }
            }
            if (StringUtils.isEmpty(imagePath)) {
                showMessage(getString(R.string.string_getimg_fail));
                return;
            }

            if (requestCode == 1) {//正面照
                img1 = Base64Utils.imageToBase64(imagePath);
                Glide.with(this)
                        .load(imagePath)
                        .into(imgFront);
            }else if (requestCode==2){//反面
                img2 = Base64Utils.imageToBase64(imagePath);
                Glide.with(this)
                        .load(imagePath)
                        .into(imgVerso);
            }else if (requestCode==3){//手持
                img3 = Base64Utils.imageToBase64(imagePath);
                Glide.with(this)
                        .load(imagePath)
                        .into(imgHandCat);
            }
        }
    }
}