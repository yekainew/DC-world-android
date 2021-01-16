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
public class RetTranLogBriefBean extends JsonBean {
//    "type": "address",
//            "network": "USDT",
//            "hash": "14ctzVrH8CMi2uscTm2MdxGLUrubmAMhDu",
//            "rank": 154384,
//            "txCount": 803,
//            "spend": "-511307.60961334",
//            "receive": "511308.15861199",
//            "normalTxCount": 0,

    private String type;
    private String network;
    private String hash;
    private Integer rank;
    private Integer txCount;
    private BigDecimal spend;
    private BigDecimal receive;

    private JSONArray txs;

}
