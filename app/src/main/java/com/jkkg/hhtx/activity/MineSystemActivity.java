package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.base.BaseActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.QuickPopupBuilder;
import razerdp.basepopup.QuickPopupConfig;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;


/**
 * Description:
 * Created by ccw on 09/17/2020 14:27
 * Email:chencw0715@163.com
 * 系统
 */
public class MineSystemActivity extends BaseActivity {

    @BindView(R.id.toolbar_image_left)
    ImageView toolbarImageLeft;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_image_right)
    ImageView toolbarImageRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.sysytem_change_language_type)
    TextView sysytemChangeLanguageType;
    @BindView(R.id.sysytem_change_language)
    RelativeLayout sysytemChangeLanguage;
    @BindView(R.id.syatem_notice)
    TextView syatemNotice;
    @BindView(R.id.system_version_code)
    TextView systemVersionCode;
    @BindView(R.id.system_version)
    RelativeLayout systemVersion;

    BasePopupWindow languagePopupWindow;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_mine_system;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_mine_common6));
    }

    @OnClick({R.id.sysytem_change_language, R.id.syatem_notice, R.id.system_version})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sysytem_change_language:
                //语言切换
                languagePopupWindow = QuickPopupBuilder.with(getContext())
                        .contentView(R.layout.layout_pop_language)
                        .config(new QuickPopupConfig().gravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM)
                                .backpressEnable(true)
                                .outSideTouchable(false)
                                .withShowAnimation(AnimationHelper.asAnimation()
                                        .withTranslation(TranslationConfig.FROM_TOP)
                                        .toShow())
                                .withDismissAnimation(AnimationHelper.asAnimation()
                                        .withTranslation(TranslationConfig.TO_TOP)
                                        .toDismiss())
                                .withClick(R.id.tv_pop_cn, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        sysytemChangeLanguageType.setText(R.string.string_language_cn);
                                        Constants.saveLanguageCode(0);
                                        languagePopupWindow.dismiss();
                                        reStartActivity();
                                    }
                                })
                                .withClick(R.id.tv_pop_en, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        sysytemChangeLanguageType.setText(R.string.string_language_en);
                                        Constants.saveLanguageCode(1);
                                        languagePopupWindow.dismiss();
                                        reStartActivity();
                                    }
                                })).show(sysytemChangeLanguageType);
                break;
            case R.id.syatem_notice:
                //系统公告
                startActivity(new Intent(this,MineSystemNoticeActivity.class));
                break;
            case R.id.system_version:
                //当前版本
                break;
            default:
                break;
        }
    }

    private void reStartActivity() {
        Intent intent = getIntent();
        finish();
        overridePendingTransition(R.anim.fade_entry, R.anim.fade_exit);
        startActivity(intent);
    }
}
