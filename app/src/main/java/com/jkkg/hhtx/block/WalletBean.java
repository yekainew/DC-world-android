package com.jkkg.hhtx.block;

import com.mugui.base.bean.JsonBean;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 地址bean
 */
@Getter
@Setter
@Accessors(chain = true)
public class WalletBean extends JsonBean {


    private String name;

    /**
     * 地址
     */
    private String address;
    /**
     * 私钥
     */
    private String pri;
    /**
     * 助记词
     */
    private String recoveryPhrase;

}
