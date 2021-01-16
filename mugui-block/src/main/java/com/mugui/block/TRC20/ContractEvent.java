package com.mugui.block.TRC20;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class ContractEvent {
    @SerializedName("caller_contract_address")
    public String caller_contract_address;

    @SerializedName("transaction_id")
    public String transactionId;

    @SerializedName("result")
    public Map<String, Object> result;

    @SerializedName("result_type")
    public Map<String, String> resultType;

    @SerializedName("block_timestamp")
    public long blockTimestamp;

    @SerializedName("block_number")
    public long blockNumber;

    @SerializedName("event_name")
    public String eventName;

    @SerializedName("contract_address")
    public String contractAddress;

    @SerializedName("event_index")
    public long eventIndex;
}