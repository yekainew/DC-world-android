package com.jkkg.hhtx.activity;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.base.BaseActivity;
import com.jkkg.hhtx.block.Block;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.AssetsBean;
import com.jkkg.hhtx.sql.bean.CoinBean;
import com.jkkg.hhtx.utils.GsonUtil;
import com.jkkg.hhtx.utils.ImageUtils;
import com.jkkg.hhtx.utils.ScreenSizeUtil;
import com.jkkg.hhtx.utils.SpUtil;
import com.jkkg.hhtx.widget.ClearEditText;
import com.king.zxing.util.CodeUtils;
import com.mugui.base.base.Autowired;
import com.mugui.base.bean.DefaultJsonBean;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hutool.json.JSONUtil;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.QuickPopupBuilder;
import razerdp.basepopup.QuickPopupConfig;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;


/**
 * Description:
 * Created by ccw on 09/11/2020 10:30
 * 邀请
 * Email:chencw0715@163.com
 *
 * @author admin6
 */
public class JoinActivity extends BaseActivity {

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
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.invite_code)
    TextView inviteCode;
    @BindView(R.id.invite_copy)
    TextView inviteCopy;
    @BindView(R.id.btn_join)
    Button btnJoin;
    @BindView(R.id.lin)
    LinearLayout lin;
    BasePopupWindow InvitePopupWindow;
    @BindView(R.id.nsv_mine)
    NestedScrollView nsvMine;
    private Disposable disposable;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_join;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        toolbarImageLeft.setVisibility(View.VISIBLE);
        toolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
        String invitation = SpUtil.getInstance(this).getString("invitation", "");
        inviteCode.setText(invitation);
    }

    @OnClick({R.id.invite_copy, R.id.btn_join})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.invite_copy:
                //复制激活码
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("inviteCode", inviteCode.getText().toString());
                cm.setPrimaryClip(mClipData);
                showMessage(getString(R.string.string_text_copy_seccess));
                break;
            case R.id.btn_join:
                //分享海报
                RxPermissions rxPermissions = new RxPermissions(JoinActivity.this);
                disposable = rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if (aBoolean) {
                                    requestWebRegUrl();
                                    requestWebDownUrl();
                                    //表示用户同意权限
                                    showPop();
                                } else {
                                    //表示用户不同意权限
                                    showMessage(getString(R.string.string_user_refuse_authority));
                                }
                            }
                        });

                break;
            default:
                break;
        }
    }

    private void showPop() {
        InvitePopupWindow = QuickPopupBuilder.with(getContext())
                .contentView(R.layout.layout_pop_invite)
                .config(new QuickPopupConfig().gravity(Gravity.BOTTOM)
                        .backpressEnable(true)
                        .outSideTouchable(false)
                        .withShowAnimation(AnimationHelper.asAnimation()
                                .withTranslation(TranslationConfig.FROM_BOTTOM)
                                .toShow())
                        .withDismissAnimation(AnimationHelper.asAnimation()
                                .withTranslation(TranslationConfig.TO_BOTTOM)
                                .toDismiss())
                        .withClick(R.id.dialog_left_txt, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                InvitePopupWindow.dismiss();
                            }
                        })
                        .withClick(R.id.dialog_right_txt, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (InvitePopupWindow != null && InvitePopupWindow.isShowing()) {
                                    RelativeLayout rl_invite = InvitePopupWindow.findViewById(R.id.rl_invite);
                                    //保存这个视图就可以
                                    Bitmap bitmap = ImageUtils.screenShotView(rl_invite);
                                    ImageUtils.savePicture(getContext(), bitmap);
                                    InvitePopupWindow.dismiss();
                                }
                            }
                        })).show();

        try {
            //设置要显示的图片
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Autowired
    Dao dao;
    @Autowired
    Block block;

    /**
     * 激活下级

    private void checkInvite() {
        showLoading();
        String s = inviteActivate.getText().toString();
        Map<String, String> map = new ArrayMap<>();
        map.put("user_Invitation", s);
        MyApp.requestSend.sendData("invite.method.checkInvite", GsonUtil.toJsonString(map)).son(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                String s1 = message.getDate().toString();

                String[] split = s1.split("[|]");
                String s2 = split[1];
                String s3 = split[2];
                AssetsBean select = dao.select(new AssetsBean().setSymbol(s3));
                CoinBean select1 = dao.select(new CoinBean().setSymbol(s3));
                BigDecimal bigDecimal = new BigDecimal(s2);

                if (bigDecimal.compareTo(select.getNum()) > 0) {
                    showMessage(getString(R.string.string_money_fail));
                } else {
                    Message tron = block.tron(split[0], select1.getContract_address(), bigDecimal,SpUtil.getInstance(JoinActivity.this).getString("this_wallet_name",""), invite_password.getText().toString());
                    if (tron.getType() == Message.SUCCESS) {
                        String s4 = tron.getDate().toString();
                        activation(s4, s);
                    } else {
                        showMessage(tron.getMsg());
                    }
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

     */
    private void activation(String s4, String s) {
        Map<String, String> map = new HashMap<String, String>() {
            {
                put("user_Invitation", s);
                put("hash", s4);
            }
        };

        MyApp.requestSend.sendData("invite.method.activation",map).son(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                hideLoading();
                showMessage(message.getDate().toString());
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

    private void requestWebRegUrl() {
        MyApp.requestSend.sendData("app.info.webRegUrl", "").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                String data = message.getDate().toString();
                if (InvitePopupWindow != null) {
//                    ImageView login_join = InvitePopupWindow.findViewById(R.id.login_join);
                    Bitmap qr = CodeUtils.createQRCode(data, ScreenSizeUtil.dp2px(getContext(), 50));
//                    GlideApp.with(getContext()).load(qr).into(login_join);
                }
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                showMessage(message.getMsg());
                return Message.ok();
            }
        });
    }

    private void requestWebDownUrl() {
        MyApp.requestSend.sendData("app.info.webDownUrl", "").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                String data = message.getDate().toString();
                if (InvitePopupWindow != null) {
                    ImageView download_app = InvitePopupWindow.findViewById(R.id.download_app);
                    Bitmap qr = CodeUtils.createQRCode(data, ScreenSizeUtil.dp2px(getContext(), 60));
                    Glide.with(getContext()).load(qr).into(download_app);
//                    GlideApp.with(getContext()).load(qr).into(download_app);
                }
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                showMessage(message.getMsg());
                return Message.ok();
            }
        });
    }
/**
 * 激活下级
    private void requestConfigureUrl() {
        MyApp.requestSend.sendData("invite.method.conf", "").main(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                DefaultJsonBean defaultJsonBean = DefaultJsonBean.newBean(DefaultJsonBean.class, message.getDate());
                Object inviteNum = defaultJsonBean.get("invite_num");
                Object contractAddress = defaultJsonBean.get("contract_address");
                jihuoDeduction.setText(getString(R.string.string_jihuo_kouchu) + inviteNum + contractAddress);
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                showMessage(message.getMsg());
                return Message.ok();
            }
        });
    }

*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (InvitePopupWindow != null && InvitePopupWindow.isShowing()) {
            InvitePopupWindow.onDestroy();
            InvitePopupWindow.dismiss();
        }
        InvitePopupWindow = null;
        if (disposable != null) {
            disposable.dispose();
        }
    }

}
