package com.mugui.block.btc.bean;


import com.mugui.base.bean.JsonBean;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class RetMsgBean extends JsonBean {

    public static final int SUCESS=1;

    private Integer code;
    private String msg;
    private Object data;
}
