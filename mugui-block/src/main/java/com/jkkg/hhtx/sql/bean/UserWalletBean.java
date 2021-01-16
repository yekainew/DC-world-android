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
@SQLDB(TABLE = "user_wallet",KEY = "id")
public class UserWalletBean extends JsonBean {
    /**
     * id
     */
    @SQLField(AUTOINCREMENT = true,PRIMARY_KEY = true,AUTOINCREMENT_value = "AUTOINCREMENT")
    private Integer id;
    /**
     * 名称
     */
    @SQLField()
    private String name;

    /**
     * 地址
     */
    @SQLField()
    private String address;
    /**
     * 手机号
     */
    @SQLField()
    private String phone;
    /**
     * 登录用token
     */
    @SQLField(DATA_TYPE = "varchar(256)")
    private String token;




}
