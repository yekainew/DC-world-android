package com.mugui.block.TRC20;

import com.google.gson.annotations.SerializedName;

public class DeployContractRequest {
    @SerializedName("abi")
    public String abi;

    @SerializedName("bytecode")
    public String bytecode;

    @SerializedName("parameter")
    public String parameter;

    @SerializedName("owner_address")
    public String ownerAddress;

    @SerializedName("name")
    public String name;

    @SerializedName("call_value")
    public int callValue = 0;

    @SerializedName("consume_user_resource_percent")
    public int consumeUserResourcePercent = 0;

    @SerializedName("origin_energy_limit")
    public int originEnergyLimit = 0;

    @SerializedName("fee_limit")
    public long feeLimit = 1000000000;

    @SerializedName("permission_id")
    public int permissionId = 0;

    @SerializedName("visible")
    public boolean visible;

    public DeployContractRequest(String abi, String bytecode, String parameter, String ownerAddress, String name) {
        this.abi = abi;
        this.bytecode = bytecode;
        this.parameter = parameter;
        this.ownerAddress = ownerAddress;
        this.name = name;
    }
}