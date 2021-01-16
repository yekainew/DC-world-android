package com.jkkg.hhtx.sql.bean;

import com.mugui.base.bean.JsonBean;
import com.mugui.sql.SQLDB;
import com.mugui.sql.SQLField;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 资产实体类
 */

@Getter
@Setter
@Accessors(chain = true)
@SQLDB(TABLE = "assets",KEY = "id")
public class AssetsBean extends JsonBean {


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
    @SQLField
    private String symbol;

    /**
     * 余额
     */
    @SQLField
    private BigDecimal num;

    /**
     * 相对usd价格
     */
    @SQLField
    private BigDecimal num_usd;

    /**
     * 相对人民币价格
     */
    @SQLField
    private BigDecimal num_cny;



}
