package com.mugui.block.sql;

import com.mugui.base.bean.JsonBean;
import com.mugui.sql.SQLDB;
import com.mugui.sql.SQLField;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 区块链表
 */
@Getter
@Setter
@Accessors(chain = true)
@SQLDB(KEY = "bc_id",TABLE = "block_chain")
public class BlockChainBean extends JsonBean {

    @SQLField(AUTOINCREMENT = true,PRIMARY_KEY = true,AUTOINCREMENT_value = "AUTOINCREMENT")
    private Integer bc_id;
    /**
     * 区块链名称
     */
    @SQLField(NULL = false)
    private String bc_name;

    /**
     * 中文名称
     */
    @SQLField
    private String bc_name_zh;

    /**
     * 图标，采用资源路径或网络路径
     */
    @SQLField(DATA_TYPE = "varchar(512)")
    private String bc_icon;

    /**
     *按下的图片
     */
    @SQLField(DATA_TYPE = "varchar(512)")
    private String bc_down_icon;

    /**
     * 不按下的图片
     */
    @SQLField(DATA_TYPE = "varchar(512)")
    private String bc_up_icon;
    /**
     * 区块链描述
     */
    @SQLField()
    private String bc_description;

    /**
     * 是否为当前选中
     */
    @SQLField(DATA_TYPE = "varchar(5)")
    private Boolean is_open;

}
