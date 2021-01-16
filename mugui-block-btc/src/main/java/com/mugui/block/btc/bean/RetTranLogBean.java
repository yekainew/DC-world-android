package com.mugui.block.btc.bean;

import com.alibaba.fastjson.JSONArray;
import com.mugui.base.bean.JsonBean;


import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class RetTranLogBean extends JsonBean {
    private String type;

    private String network;

    private int block_no;

    private int height;

    private int index;

    private long time;

    private String txid;

    private String fee;

    private int confirmations;

    private JSONArray inputs;

    private JSONArray outputs;

    private int inputCnt;

    private int outputCnt;

    private int type_int;

    private String type_str;

    private int status;

    @Getter
    @Setter
    @Accessors(chain = true)
    public static class Inputs extends  JsonBean {
        private String address;
        private BigDecimal value;

    }

    @Getter
    @Setter
    @Accessors(chain = true)
    public static class Outputs extends  JsonBean {
        private String address;
        private BigDecimal value;
    }
}
