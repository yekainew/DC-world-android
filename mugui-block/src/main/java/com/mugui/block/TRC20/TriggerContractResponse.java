package com.mugui.block.TRC20;

import com.google.gson.annotations.SerializedName;

public class TriggerContractResponse {
    @SerializedName("result")
    public Result result;

    @SerializedName("constant_result")
    public String[] constantResult;

    @SerializedName("transaction")
    public ContractTransaction transaction;

    public class Result {
        @SerializedName("result")
        public Boolean result;
    }
}