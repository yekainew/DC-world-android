package com.mugui.block.TRC20;

import com.google.gson.annotations.SerializedName;

public class ApiResult {
    @SerializedName("result")
    public Boolean result;
    @SerializedName("message")
    public String message;

    @SerializedName("code")

    public String code;


    @SerializedName("txid")

    public String txid;
}