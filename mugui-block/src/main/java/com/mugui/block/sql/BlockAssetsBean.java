package com.mugui.block.sql;

import com.mugui.base.bean.JsonBean;
import com.mugui.sql.SQLDB;
import com.mugui.sql.SQLField;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 钱包资产实体类
 */

@Getter
@Setter
@Accessors(chain = true)
@SQLDB(TABLE = "block_assets",KEY = "block_assets_id")
public class BlockAssetsBean extends JsonBean {


    @SQLField(AUTOINCREMENT = true,PRIMARY_KEY = true,AUTOINCREMENT_value = "AUTOINCREMENT")
    private Integer block_assets_id;
    /**
     * 账号id
     */
    @SQLField
    private Integer block_wallet_id;

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
