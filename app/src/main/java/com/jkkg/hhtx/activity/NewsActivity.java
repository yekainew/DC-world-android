package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.event.EventBusTags;
import com.jkkg.hhtx.event.EventImpl;
import com.jkkg.hhtx.net.bean.InformationBean;
import com.jkkg.hhtx.utils.Base64Utils;
import com.jkkg.hhtx.utils.FileUtils;
import com.jkkg.hhtx.utils.GlideEngine;
import com.jkkg.hhtx.utils.StringUtils;
import com.jkkg.hhtx.widget.ClearEditText;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;

/**
 * 写资讯
 */
public class NewsActivity extends BaseActivity {

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
    @BindView(R.id.edit_title)
    ClearEditText editTitle;
    @BindView(R.id.edit_text)
    ClearEditText editText;
    @BindView(R.id.add_img1)
    ImageView addImg1;
    @BindView(R.id.add_img2)
    ImageView addImg2;
    @BindView(R.id.add_img3)
    ImageView addImg3;
    @BindView(R.id.add_img4)
    ImageView addImg4;
    @BindView(R.id.btn_true)
    Button btnTrue;
    JSONArray objects = new JSONArray();
    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_new;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_right_new));
    }


    @OnClick({R.id.add_img1, R.id.add_img2, R.id.add_img3, R.id.add_img4, R.id.btn_true})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_img1:
                PictureSelector.create(NewsActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .maxSelectNum(1)// 最大图片选择数量 int
                        .minSelectNum(1)// 最小选择数量 int
                        .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                        .isCompress(true)// 是否压缩
                        .compressQuality(80)
                        .forResult(1);
                break;
            case R.id.add_img2:
                PictureSelector.create(NewsActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .maxSelectNum(1)// 最大图片选择数量 int
                        .minSelectNum(1)// 最小选择数量 int
                        .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                        .isCompress(true)// 是否压缩
                        .compressQuality(80)
                        .forResult(2);
                break;
            case R.id.add_img3:
                PictureSelector.create(NewsActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .maxSelectNum(1)// 最大图片选择数量 int
                        .minSelectNum(1)// 最小选择数量 int
                        .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                        .isCompress(true)// 是否压缩
                        .compressQuality(80)
                        .forResult(3);
                break;
            case R.id.add_img4:
                PictureSelector.create(NewsActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .maxSelectNum(1)// 最大图片选择数量 int
                        .minSelectNum(1)// 最小选择数量 int
                        .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                        .isCompress(true)// 是否压缩
                        .compressQuality(80)
                        .forResult(4);
                break;
            case R.id.btn_true:
                showLoading();
                if (StrUtil.isBlank(editTitle.getText().toString())) {
                    showMessage(getString(R.string.string_title_no_null));
                    return;
                }
                if (StrUtil.isBlank(editText.getText().toString())) {
                    showMessage(getString(R.string.string_text_no_null));
                    return;
                }

                String s = objects.toString();

                InformationBean informationBean = new InformationBean();
                informationBean.setTitle(editTitle.getText().toString());
                informationBean.setContent(editText.getText().toString());
                informationBean.setImgs(s);

                MyApp.requestSend.sendData("information.app.method.submitInformation",informationBean).main(new NetCall.Call() {
                    @Override
                    public Message ok(Message message) {
                        hideLoading();
                        showMessage(getString(R.string.string_new_success));
                        EventBus.getDefault().post(new EventImpl.RefreshInformation());
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


                break;
        }
    }
    String img1="";
    String img2="";
    String img3="";
    String img4="";

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
                showMessage(getString(R.string.string_img_get_fail));
                return;
            }
            if (requestCode == 1) {//正面照
                img1 = Base64Utils.imageToBase64(imagePath);
                objects.add(img1);

                Glide.with(this)
                        .load(imagePath)
                        .into(addImg1);
            }else if (requestCode==2){//反面
                img2 = Base64Utils.imageToBase64(imagePath);
                objects.add(img2);
                Glide.with(this)
                        .load(imagePath)
                        .into(addImg2);
            }else if (requestCode==3){//手持
                img3 = Base64Utils.imageToBase64(imagePath);
                objects.add(img3);
                Glide.with(this)
                        .load(imagePath)
                        .into(addImg3);
            }else if (requestCode==4){
                img4 = Base64Utils.imageToBase64(imagePath);
                objects.add(img4);
                Glide.with(this)
                        .load(imagePath)
                        .into(addImg4);
            }
        }
    }
}
