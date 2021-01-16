package com.mugui.block.TRC20;

import com.google.gson.annotations.SerializedName;

public class TriggerContractRequest {
    @SerializedName("contract_address")
    public String contractAddress = "";

    @SerializedName("function_selector")
    public String functionSelector = "";

    @SerializedName("parameter")
    public String parameter = "";

    @SerializedName("fee_limit")
    public int feeLimit = 1000000000;

    @SerializedName("call_value")
    public int callValue = 0;

    @SerializedName("owner_address")
    public String ownerAddress = "";

    @SerializedName("permission_id")
    public int permissionId = 0;

    @SerializedName("visible")
    public Boolean visible = false;

    public TriggerContractRequest(String contractAddress, String functionSelector, String parameter, String ownerAddress) {
        this.contractAddress = contractAddress;
        this.functionSelector = functionSelector;
        this.parameter = parameter;
        this.ownerAddress = ownerAddress;
    }
}