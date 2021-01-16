package com.jkkg.hhtx.sql.bean;



import com.mugui.base.bean.JsonBean;
import com.mugui.sql.SQLDB;
import com.mugui.sql.SQLField;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


/**
 * 用户币种列表
 */
@Getter
@Setter
@Accessors(chain = true)
@SQLDB(TABLE = "coin",KEY = "id")
public class CoinBean extends JsonBean {

    @SQLField(AUTOINCREMENT = true,PRIMARY_KEY = true,AUTOINCREMENT_value = "AUTOINCREMENT")
    private Integer id;
    /**
     * 名称
     */
    @SQLField()
    private String name;
    /**
     * 简称
     */
    @SQLField()
    private String symbol;

    /**
     * 相对USDT价格
     */
    @SQLField()
    private BigDecimal price;
    /**
     * 价格人民币
     */
    @SQLField()
    private BigDecimal price_cny;

    /**
     * 合约地址
     */
    @SQLField()
    private String contract_address;
    /**
     * 发布时间
     */
    @SQLField()
    private Date issue_time;
    /**
     * 发布地址
     */
    @SQLField()
    private String issue_address;
    /**
     * 图标
     */
    @SQLField()
    private String icon_url;
    /**
     * 小数点位数
     */
    @SQLField()
    private Integer decimals;
}
