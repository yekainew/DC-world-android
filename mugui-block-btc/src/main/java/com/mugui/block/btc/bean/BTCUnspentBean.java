package com.mugui.block.btc.bean;

import com.mugui.base.bean.JsonBean;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 比特币UTXO对象
 *
 */
@Getter
@Setter
@Accessors(chain = true)
public class BTCUnspentBean  extends JsonBean {

    private Integer block_no;
    private Integer output_no;
    private String index;
    private String txid;
    private String hex;
    private Integer confirmations;
    private BigDecimal value;
}
