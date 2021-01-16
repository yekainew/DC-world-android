package com.mugui.block.TRC20;

import com.google.gson.annotations.SerializedName;

public class CreateTransactionRequest {
    @SerializedName("to_address")
    public String toAddress = "";

    @SerializedName("owner_address")
    public String ownerAddress = "";

    @SerializedName("amount")
    public long amount = 0;

    @SerializedName("permission_id")
    public int permissionId = 0;

    @SerializedName("visible")
    public boolean visible = false;

    public CreateTransactionRequest(String toAddress, long amount, String ownerAddress) {
        this.toAddress = toAddress;
        this.amount = amount;
        this.ownerAddress = ownerAddress;
    }
}