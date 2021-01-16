package com.mugui.block.sql;

import com.mugui.base.bean.JsonBean;
import com.mugui.sql.SQLDB;
import com.mugui.sql.SQLField;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 用户私钥管理表
 */
@Getter
@Setter
@Accessors(chain = true)
@SQLDB(KEY="block_wallet_id",TABLE = "block_wallet")
public class BlockWalletBean extends JsonBean {

    @SQLField(AUTOINCREMENT = true,PRIMARY_KEY = true,AUTOINCREMENT_value = "AUTOINCREMENT")
    private Integer block_wallet_id;

    /**
     * 公链id
     */
    @SQLField(NULL = false)
    private Integer bc_id;

    /**
     * 钱包名称
     */
    @SQLField
    private String wallet_name;

    /**
     * 地址
     */
    @SQLField(DATA_TYPE = "varchar(128)")
    private String address;

    /**
     * 地址额外信息
     */
    @SQLField(DATA_TYPE = "varchar(128)")
    private String address_extra;


    /**
     * 私钥
     */
    @SQLField(DATA_TYPE = "varchar(128)")
    private String private_key;

    /**
     * 公钥
     */
    @SQLField(DATA_TYPE = "varchar(128)")
    private String public_key;
    /**
     * 一般钱包
     */
    public static final int TYPE_0=0;
    /**
     * 观察钱包
     */
    public static final int TYPE_1=1;
    /**
     * 冷钱包
     */
    public static final int TYPE_2=2;
    /**
     *钱包类型
     */

    @SQLField(DEFAULT = true,DEFAULT_text = "0")
    private Integer type;

    @SQLField
    private Date create_time;

    /**
     * 0未有效 1未失效
     */
    @SQLField(DEFAULT = true,DEFAULT_text = "0")
    private Integer status;

    /**
     * 删除时间
     */
    @SQLField
    private Date remove_time;

}
