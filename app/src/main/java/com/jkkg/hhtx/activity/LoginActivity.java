package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.base.BaseActivity;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.QuickPopupBuilder;
import razerdp.basepopup.QuickPopupConfig;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;


/**
 * Description:
 * Created by ccw on 09/08/2020 16:34
 * Email:chencw0715@163.com
 * 登录选择语言，账户
 *
 * @author admin6
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.btn_create)
    Button btnCreate;
    @BindView(R.id.btn_recover)
    Button btnRecover;
    @BindView(R.id.tv_lanauage)
    TextView tvLanauage;
    BasePopupWindow languagePopupWindow;
    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_login;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        if (Constants.getLanguageCode() == 0) {
            tvLanauage.setText(R.string.string_language_cn);
        } else if (Constants.getLanguageCode() == 1) {
            tvLanauage.setText(R.string.string_language_en);
        }
    }

    @OnClick({R.id.tv_lanauage, R.id.btn_create, R.id.btn_recover})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_lanauage:
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
                                          tvLanauage.setText(R.string.string_language_cn);
                                          Constants.saveLanguageCode(0);
                                          languagePopupWindow.dismiss();
                                          reStartActivity();
                                      }
                                  })
                                  .withClick(R.id.tv_pop_en, new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          tvLanauage.setText(R.string.string_language_en);
                                          Constants.saveLanguageCode(1);
                                          languagePopupWindow.dismiss();
                                          reStartActivity();
                                      }
                                  })).show(tvLanauage);
                break;
            case R.id.btn_create:
                //创建身份
//                startActivity(new Intent(this, AgreementActivity.class));
                startActivity(new Intent(this,CreateUserActivity.class));
                break;
            case R.id.btn_recover:
                //恢复身份
                startActivity(new Intent(this, RecoverActivity.class));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(languagePopupWindow!=null&&languagePopupWindow.isShowing()){
            languagePopupWindow.onDestroy();
            languagePopupWindow.dismiss();
        }
        languagePopupWindow=null;
    }
}
