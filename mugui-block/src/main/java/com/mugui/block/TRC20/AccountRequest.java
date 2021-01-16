package com.mugui.block.TRC20;

import com.google.gson.annotations.SerializedName;

public class AccountRequest {
    @SerializedName("address")
    public String address = "";

    @SerializedName("visible")
    public Boolean visible = false;

    public AccountRequest(String address) {
        this.address = address;
    }
}