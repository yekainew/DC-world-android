package com.mugui.block.TRC20;

import com.google.gson.annotations.SerializedName;

public class DeployContractTransaction {
    @SerializedName("visible")
    public boolean visible;
    @SerializedName("txID")
    public String txId;
    @SerializedName("raw_data")
    public RawData rawData;
    @SerializedName("raw_data_hex")
    public String rawDataHex;
    @SerializedName("signature")
    public String[] signature;

    public class RawData {
        @SerializedName("contract")
        public Contract[] contract;
        @SerializedName("ref_block_bytes")
        public String refBlockBytes;
        @SerializedName("ref_block_hash")
        public String refBlockHash;
        @SerializedName("expiration")
        public long expiration;
        @SerializedName("timestamp")
        public long timestamp;
    }

    public class Contract {
        @SerializedName("parameter")
        public Parameter parameter;
        @SerializedName("type")
        public String type;
    }

    public class Parameter {
        @SerializedName("value")
        public Value value;
        @SerializedName("type_url")
        public String typeUrl;
    }

    public class Value {
        @SerializedName("amount")
        public long amount;
        @SerializedName("to_address")
        public String toAddress;
        @SerializedName("owner_address")
        public String ownerAddress;
    }

}