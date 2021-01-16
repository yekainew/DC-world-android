package com.jkkg.hhtx.sql.bean;


import com.mugui.base.bean.JsonBean;
import com.mugui.sql.SQLDB;
import com.mugui.sql.SQLField;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@SQLDB(TABLE = "invate_log",KEY = "id")
public class InvateLogBean extends JsonBean {

    @SQLField(AUTOINCREMENT = true,PRIMARY_KEY = true,AUTOINCREMENT_value = "AUTOINCREMENT")
    private Integer id;
    /**
     * 时间
     */
    @SQLField
    private Date time;
    /**
     * 激活码
     */
    @SQLField
    private  String invite_code;


    /**
     *已创建
     */
    public static final int status_0=0;
    /**
     *处理中
     */
    public static final int status_1=1;
    /**
     *已完成
     */
    public static final int status_2=2;
    /**
     *失败
     */
    public static final int status_3=3;
    /**
     * 状态
     */
    @SQLField
    private Integer status;
    /**
     * 转账交易hash
     */
    @SQLField
    private String hash;
    /**
     * 转账数量
     */
    @SQLField
    private BigDecimal num;
    /**
     * 转账币种
     */
    @SQLField
    private String symbol;

}
