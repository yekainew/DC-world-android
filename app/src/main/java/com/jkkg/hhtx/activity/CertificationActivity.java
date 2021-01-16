package com.jkkg.hhtx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.utils.GsonUtil;
import com.jkkg.hhtx.utils.IDCard;
import com.jkkg.hhtx.widget.ClearEditText;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;

import java.text.ParseException;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 实名认证
 *
 * @author admin6
 */
public class CertificationActivity extends BaseActivity {

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
    @BindView(R.id.certification_nation)
    TextView certificationNation;
    @BindView(R.id.certification_name)
    ClearEditText certificationName;
    @BindView(R.id.certification_id_number)
    ClearEditText certificationIdNumber;
    @BindView(R.id.btn_true)
    Button btnTrue;
    private String name="中国";

    @Override
    protected int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_certification;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        toolbar.setVisibility(View.VISIBLE);
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbarTitle.setText(getString(R.string.string_mine_safe));
    }


    @OnClick({R.id.certification_nation, R.id.btn_true})
    public void onViewClicked(View view){
        switch (view.getId()) {
            case R.id.certification_nation:
                //选择国家
                Intent intent = new Intent(this, AreaSelectActivity.class);
                startActivityForResult(intent,101);
                break;
            case R.id.btn_true:
                //下一步
                if (TextUtils.isEmpty(certificationIdNumber.getText().toString())) {
                    showMessage(getString(R.string.string_edit_shenfenzhenghao));
                    return;
                }
                String s = null;
                try {
                    s = IDCard.IDCardValidate(certificationIdNumber.getText().toString());
                    if (!s.equals("")) {
                        showMessage(s);
                    }else{
                        request();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                break;
            default:
                break;
        }
    }

    private void request(){
        showLoading();
        Map<String, String> map = new ArrayMap<>();
        map.put("user_certificate_no", certificationIdNumber.getText().toString());
        MyApp.requestSend.sendData("verified1.method.checkID", GsonUtil.toJsonString(map)).main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                hideLoading();
                String data=message.getDate().toString();
                if (data.equals("true")) {
                    Intent intent = new Intent(getContext(), CertificationImgActivity.class);
                    intent.putExtra("crdid",certificationIdNumber.getText().toString());
                    intent.putExtra("areaname",name);
                    startActivity(intent);
                }else{
                    showMessage(getString(R.string.string_shenfen_yirenzheng));
                }
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
        if (requestCode==101&&resultCode==RESULT_OK) {
            name = data.getStringExtra("name");
            certificationNation.setText(name);
        }
    }
}