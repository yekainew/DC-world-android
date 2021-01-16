package com.jkkg.hhtx.blocktask;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.jkkg.hhtx.block.Block;
import com.jkkg.hhtx.sql.Dao;
import com.jkkg.hhtx.sql.bean.AssetsLogBean;
import com.jkkg.hhtx.sql.bean.InvateLogBean;
import com.mugui.base.base.Autowired;
import com.mugui.base.base.Component;
import com.mugui.base.client.net.auto.AutoTask;
import com.mugui.base.client.net.base.Task;
import com.mugui.base.client.net.base.TaskInterface;
import com.mugui.base.client.net.bean.Message;
import com.mugui.base.util.Other;
import com.mugui.sql.loader.Select;
import com.mugui.sql.loader.Where;

import java.util.List;

@AutoTask
@Task(time = 3000)
@Component

public class InvateLogTask implements TaskInterface {

    @Autowired
    private Dao dao;

    public void init() {
        System.out.println(InvateLogTask.class.getName() + "初始化");
    }
    @Override
    public void run() {
        while (true) {
            try {
                handle();
            } catch (Exception e) {
                e.printStackTrace();
                Other.sleep(1000);
            }
        }
    }

    @Autowired
    Block block;
    private void handle() throws InterruptedException {
        while (true) {
            List<InvateLogBean> invateLogBeans = dao.selectList(InvateLogBean.class, Select.q(new InvateLogBean()).where(Where.q().in("status","0,1")));

            if (!invateLogBeans.isEmpty()) {
                for (InvateLogBean invateLogBean : invateLogBeans) {

                    String hash = invateLogBean.getHash();
                    Message message = block.tranById(hash);
                    Log.d("InvateLogTask", "message:" + message);

                    JSONObject jsonObject = JSONObject.parseObject(message.getDate().toString());
                    Boolean confirmed = jsonObject.getBoolean("confirmed");

                    if (jsonObject.getString("contractRet").equals("SUCCESS")) {
                        if (jsonObject.isEmpty()) {
                            dao.updata(invateLogBean.setStatus(InvateLogBean.status_1));
                        }else{
                            if (confirmed) {
                                dao.updata(invateLogBean.setStatus(InvateLogBean.status_2));
                            }else{
                                dao.updata(invateLogBean.setStatus(InvateLogBean.status_1));
                            }
                        }
                    }else{
                        dao.updata(invateLogBean.setStatus(InvateLogBean.status_3));
                    }
                }
            }
            Other.sleep(1000);
        }

    }
}
