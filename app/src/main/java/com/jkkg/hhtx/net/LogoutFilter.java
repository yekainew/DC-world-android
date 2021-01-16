package com.jkkg.hhtx.net;

import android.util.ArrayMap;

import com.jkkg.hhtx.activity.FlashActivity;
import com.jkkg.hhtx.app.Constants;
import com.jkkg.hhtx.app.MyApp;
import com.jkkg.hhtx.event.EventImpl;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.UserWalletBean;
import com.jkkg.hhtx.utils.SpUtil;
import com.jkkg.hhtx.utils.ToastUtil;
import com.jkkg.hhtx.utils.UniqueIdUtils;
import com.mugui.base.base.Autowired;
import com.mugui.base.base.Component;
import com.mugui.base.bean.user.User;
import com.mugui.base.client.net.bagsend.BagSend;
import com.mugui.base.client.net.bagsend.NetCall;
import com.mugui.base.client.net.base.Filter;
import com.mugui.base.client.net.bean.Message;
import com.mugui.base.client.net.bean.NetBag;
import com.mugui.base.client.net.classutil.DataSave;
import com.mugui.base.client.net.filter.FilterModel;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import cn.hutool.core.thread.ThreadUtil;
import timber.log.Timber;

/**
 * 过滤器
 */
@Filter(value = {"*"},type = Filter.POSITIVE)
@Component
public class LogoutFilter implements FilterModel {


    @Autowired
    BagSend bagSend;

    @Override
    public NetBag filter(NetBag netBag) {
        System.out.println("服务器返回");
        Timber.i("服务器返回》》》"+netBag.toString());
        Message message=Message.newBean(Message.class,netBag.getData());
        //如果是退出登录消息
        if (message.getType()== Message.LOGOUT) {
                    login();
        }else if(message.getType()== 500){
            ToastUtil.show("服务器超时！");
        }else if (message.getMsg()!=null&&message.getMsg().equals("设备不一致，请重新登录")){
            ToastUtil.show(message.getMsg());
            EventBus.getDefault().post(new EventImpl.loginOut());
        }
        return netBag;
    }

    @Autowired
    Dao dao;
    public void login(){
        String is_login = Constants.getUserLogin();
        if (org.apache.commons.lang3.StringUtils.isBlank(is_login)) {
            Constants.loginOut();
            return;
        }
        UserWalletBean this_wallet_name = dao.select(new UserWalletBean().setName(SpUtil.getInstance(DataSave.app).getString("this_wallet_name", "")));
        Map<String, Object> requestMap1 = new ArrayMap<>();
        requestMap1.put("is_login", is_login);
        System.out.println("本地地址》》》" + this_wallet_name.getAddress());
        requestMap1.put("bind_address", this_wallet_name.getAddress());
        requestMap1.put("login_log_device", UniqueIdUtils.getUniquePsuedoID(MyApp.getApp()));
        bagSend.sendData("user.login.token",requestMap1).son(new NetCall.Call() {
            @Override
            public Message ok(Message message) {
                String data = message.getDate().toString();
                User user = User.newBean(User.class, data);
//                String bindState = user.get("bindState").toString();
                String is_login = user.get("is_login").toString();
                String is_activate = user.getUser_sub();
                String user_invitation = user.getUser_Invitation();
                String user_id = String.valueOf(user.getUser_id());
                String user_phone = user.getUser_phone();
                String user_name = user.get("user_call").toString();

                UserWalletBean select = dao.select(new UserWalletBean().setAddress(this_wallet_name.getAddress()));

                if (select==null) {
                    dao.save(new UserWalletBean().setAddress( this_wallet_name.getAddress()).setName(SpUtil.getInstance(DataSave.app).getString("this_wallet_name","")).setToken(is_login));
                }else{
                    dao.updata(select.setAddress( this_wallet_name.getAddress()).setName(SpUtil.getInstance(DataSave.app).getString("this_wallet_name","")).setToken(is_login));
                }
//                if (StringUtils.isNotEmpty(bindState) && getString(R.string.string_zhu_or_si_null).equals(bindState)) {
//                    startActivity(new Intent(getContext(), MemberActivity.class));
//                } else {
                Constants.saveUserInfo(is_login, user_invitation, user_phone, user_id, user_name, is_activate);
                return Message.ok();
            }

            @Override
            public Message err(Message message) {
                EventBus.getDefault().post(new EventImpl.loginOut());
                return Message.ok();
            }
        });
    }
}
