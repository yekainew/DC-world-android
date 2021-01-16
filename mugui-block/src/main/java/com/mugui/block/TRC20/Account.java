package com.mugui.block.TRC20;

import com.google.gson.annotations.SerializedName;

public class Account {
    @SerializedName("account_name")
    public String accountName;

    @SerializedName("address")
    public String address;

    @SerializedName("balance")
    public long balance;

    @SerializedName("asset")
    public KeyValue[] asset;

    @SerializedName("create_time")
    public long createTime;

    @SerializedName("latest_operation_time")
    public long latestOperationTime;

    @SerializedName("latest_consume_time")
    public long latestConsumeTime;

    @SerializedName("latest_consume_free_time")
    public long latestConsumeFreeTime;

    @SerializedName("account_resource")
    public AccountResource accountResource;

    @SerializedName("assetV2")
    public KeyValue[] assetV2;

    @SerializedName("free_asset_net_usageV2")
    public KeyValue[] freeAssetNetUsageV2;

    @SerializedName("free_net_usage")
    public long freeNetUsage;

    @SerializedName("owner_permission")
    public Permission ownerPermission;

    @SerializedName("active_permission")
    public Permission[] activePermission;

    public class KeyValue {
        @SerializedName("key")
        public String key;

        @SerializedName("value")
        public long value;
    }

    public class AccountResource {
        @SerializedName("last_consume_time_for_energy")
        public long lastConsumeTimeForEnergy;
    }

    public class Permission {
        @SerializedName("permission_name")
        public String permissionName;

        @SerializedName("type")
        public String type;

        @SerializedName("id")
        public int id;

        @SerializedName("threshold")
        public int threshold;

        @SerializedName("operations")
        public String operations;

        @SerializedName("keys")
        public Key[] keys;
    }

    public class Key {
        @SerializedName("address")
        public String address;

        @SerializedName("weight")
        public int weight;
    }


}