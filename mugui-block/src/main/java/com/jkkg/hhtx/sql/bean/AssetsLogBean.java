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
@SQLDB(TABLE = "assets_log",KEY = "id")
public class AssetsLogBean extends JsonBean {
    @SQLField(AUTOINCREMENT = true,PRIMARY_KEY = true,AUTOINCREMENT_value = "AUTOINCREMENT")
    private Integer id;

    /**
     * 钱包名称
     */
    @SQLField
    private String wallet_name;

    /**
     * 简称
     */
    @SQLField()
    private String symbol;
    /**
     * 普通账单
     */
    public static final Integer log_type_0=0;

    /**
     * 平台账号转账
     */
    public static final Integer log_type_1=1;

    /**
     * 日志类型
     */
    @SQLField
    private Integer log_type;

    /**
     * 操作数量
     */
    @SQLField
    private BigDecimal num;

    /**
     * 合约地址
     */
    @SQLField
    private String contract;
    /**
     * 去向地址
     */
    @SQLField
    private String to;
    /**
     * 去向手机号
     */
    @SQLField
    private String to_phone;

//    /**
//     * 区块高度
//     */
//    @SQLField
//    private Integer block_id;

    /**
     * 来源地址
     */
    @SQLField
    private String from;
    /**
     * 交易hash
     */
    @SQLField(DATA_TYPE = "varchar(128)")
    private String hash;

    /**
     * 时间
     */
    @SQLField
    private Date time;


    /**
     *已创建
     */
    public static final Integer status_0=0;
    /**
     *处理中
     */
    public static final Integer status_1=1;
    /**
     *已完成
     */
    public static final Integer status_2=2;
    /**
     *失败
     */
    public static final Integer status_3=3;
    /**
     * 状态
     */
    @SQLField
    private Integer status;
    /**
     * 额外描述
     */
    @SQLField(DATA_TYPE = "varchar(64)")
    private String detail;

}
