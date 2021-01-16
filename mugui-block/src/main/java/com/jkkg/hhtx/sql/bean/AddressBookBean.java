package com.jkkg.hhtx.sql.bean;

import com.mugui.base.bean.JsonBean;
import com.mugui.sql.SQLDB;
import com.mugui.sql.SQLField;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@SQLDB(TABLE = "assetbook",KEY="id")
public class AddressBookBean extends JsonBean {
    @SQLField(AUTOINCREMENT = true,PRIMARY_KEY = true,AUTOINCREMENT_value = "AUTOINCREMENT")
    protected Integer id;

    /**
     * 钱包名称
     */
    @SQLField
    protected String wallet_name;

    /**
     * 钱包备注
     */
    @SQLField
    protected String remarks;

    /**
     * 联系人
     */
    @SQLField
    protected String contacts;

    /**
     * 备注
     */
    @SQLField
    protected String bei_zhu;

    /**
     * 地址的JSON串
     */
    @SQLField
    protected String assets_json;

    /**
     * 钱包底层(因现在为单链钱包，故默认为使用底层名称作为区分，等以后有需要再更改)
     */
    @SQLField
    protected String wallet_bottom;

}
