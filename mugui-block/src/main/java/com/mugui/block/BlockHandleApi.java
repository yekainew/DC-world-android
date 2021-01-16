package com.mugui.block;

import com.mugui.base.Mugui;
import com.mugui.base.client.net.bean.Message;
import com.mugui.block.sql.BlockWalletBean;

import java.math.BigDecimal;

/**
 * 公链处理API
 */
public interface BlockHandleApi extends Mugui {



    void init();

    /**
     * 得到处理器名称
     * @return
     */
    String name();
    /**
     * 创建
     * @return
     */
    BlockWalletBean create(String pwd, String wallet_name);

    /**
     * 导入普通钱包
     * @param private_key
     * @param wallet_name
     * @return
     */
    BlockWalletBean import_base(String private_key,String pwd,String wallet_name);

    /**
     * 导入离线钱包
     * @param private_key
     * @param pwd
     * @param wallet_name
     * @return
     */
    BlockWalletBean import_offline(String private_key,String pwd,String wallet_name);


    /**
     * 导入观察钱包
     * @param address
     * @param wallet_name
     * @return
     */
    BlockWalletBean import_observed(String address,String wallet_name);


    /**
     * 删除
     * @return
     */

    boolean delete(String pwd);

    /**
     * 得到
     * @return
     */
    BlockWalletBean get(String wallet_name);

    /**
     * 得到
     * @param pwd
     * @return
     */

    BlockWalletBean get(String wallet_name, String pwd);

    /**
     *
     * @param to_address 去向地址
     * @param pwd 密码
     * @param amount 余额
     * @param contract_address 合约地址
     * @param fee 手续费
     * @return
     */
    Message send(String to_address, String pwd, BigDecimal amount,String contract_address,BigDecimal fee);

    /**
     * 预估手续费
     * @param amount
     * @return
     */
    Object estimate_fee(BigDecimal amount);
    /**
     * 手续费
     * @param amount
     * @param contract_address
     * @return
     */
    Object estimate_fee(BigDecimal amount,String contract_address);

    /**
     * 余额
     * @return
     */
    BigDecimal balance();

    BigDecimal balance(String contract_address);

    /**
     * 签名
     * @param pwd
     * @param msg
     * @return
     */
    String sign(String pwd, String msg);

    /**
     * 校验签名
     * @param msg
     * @return
     */
    BigDecimal checkSign(String msg);

    /**
     * 初始化币种
     */
    void initBlockCoin();

    /**
     * 刷新币种信息
     */
    void updateCoin();

    /**
     * 刷新转账日志
     */
    void updateAssetsLog();
}
