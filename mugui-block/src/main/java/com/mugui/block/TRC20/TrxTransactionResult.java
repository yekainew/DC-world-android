package com.mugui.block.TRC20;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrxTransactionResult {
    @SerializedName("code")
    public int code;
    @SerializedName("data")
    public List<TRXTransform> trxTransformList;
}
