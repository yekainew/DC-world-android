package com.mugui.block.TRC20;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ContractEventResult {
    public int code;
    @SerializedName(value = "data")
    public List<ContractEvent> contractEvents;
}
