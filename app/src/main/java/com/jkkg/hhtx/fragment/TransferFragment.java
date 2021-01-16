package com.jkkg.hhtx.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jkkg.hhtx.R;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.base.BaseFragment;
import com.jkkg.hhtx.block.Block;
import com.jkkg.hhtx.block.WalletBean;
import com.jkkg.hhtx.net.bean.UserBindAddressBean;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.AssetsBean;
import com.jkkg.hhtx.sql.bean.CoinBean;
import com.jkkg.hhtx.utils.SpUtil;
import com.jkkg.hhtx.widget.ClearEditText;
import com.mugui.base.base.Autowired;
import com.mugui.base.bean.user.User;
import com.mugui.base.client.net.bagsend.BagSend;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.bean.Message;
import com.mugui.base.client.net.classutil.DataSave;
import com.mugui.base.util.Other;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.OnClick;
import cn.hutool.core.util.PhoneUtil;

/**
 * 转账
 * R.layout.fragment_transfer
 *
 * @author admin6
 */
public class TransferFragment extends BaseFragment {
    @BindView(R.id.transfer_phone)
    ClearEditText transferPhone;
    @BindView(R.id.transfer_yue)
    TextView transferYue;
    @BindView(R.id.transfer_num)
    ClearEditText transferNum;
    @BindView(R.id.transfer_phone_code)
    ClearEditText transferPhoneCode;
    @BindView(R.id.send_code)
    TextView sendCode;
    @BindView(R.id.transfer_fee)
    TextView transferFee;
    @BindView(R.id.transfer_min_num)
    TextView transferMinNum;
    @BindView(R.id.btn_true)
    Button btnTrue;

    @Override
    public View initLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transfer, container, false);
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {

    }

    @OnClick({R.id.send_code, R.id.btn_true})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.send_code:
                //点击发送验证码
                break;
            case R.id.btn_true:
                //确认转账
                //转账金额不大于余额
                //验证码不为空
                String phone=transferPhone.getText().toString();
                if(!PhoneUtil.isPhone(phone)){
                    //提示手机号码错误
                    return ;
                }
                String s = transferPhoneCode.getText().toString();

                WalletBean wallet = block.getWallet(SpUtil.getInstance(DataSave.app).getString("this_wallet_name",""),s);
                if(wallet==null||StringUtils.isBlank(wallet.getPri())){
                    //提示钱包密码错误
                    return ;
                }
                //TODO 需填入币种名称
                CoinBean coin = dao.select(new CoinBean().setSymbol(getString(R.string.string_biz_name)));
                AssetsBean select = dao.select(new AssetsBean().setWallet_name(Constants.getWalletName()).setSymbol(coin.getSymbol()));
                if(!Other.isDouble(transferNum.getText().toString())){
                    //提示金额错误
                    return;
                }
                BigDecimal bigDecimal = new BigDecimal(transferNum.getText().toString());
                if(bigDecimal.compareTo(select.getNum())>0){
                    //提示金额错误
                    return;
                }
                bagSend.sendData("app.user.bindAddress",new User().setUser_phone(phone)).main(new NetCall.Call() {
                    @Override
                    public Message ok(Message message) {
                        UserBindAddressBean userBindAddressBean = UserBindAddressBean.newBean(UserBindAddressBean.class, message.getDate());
                        if(userBindAddressBean!=null){
                            if(StringUtils.isNotBlank(userBindAddressBean.getAddress())){

                                Message tron = block.tron(userBindAddressBean.getAddress(), coin.getContract_address(), bigDecimal,SpUtil.getInstance(DataSave.app).getString("this_wallet_name",""), s);
                                if(tron.getType()==Message.SUCCESS){
                                    //转账成功
                                }else {
                                    //失败提醒错误原因
                                }

                            }
                        }
                        return Message.ok();
                    }

                    @Override
                    public Message err(Message message) {
                        //提示错误信息
                        message.getMsg();
                        return Message.ok();
                    }
                });
                break;
            default:
                break;
        }
    }
    @Autowired
    private BagSend bagSend;

    @Autowired
    private Block block;
    @Autowired
    private Dao dao;

}