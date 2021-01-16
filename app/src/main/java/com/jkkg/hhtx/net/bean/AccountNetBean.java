package com.jkkg.hhtx.net.bean;

import java.util.List;

public class AccountNetBean {

    /**
     * freeNetUsed : 730
     * freeNetLimit : 5000
     * NetLimit : 1
     * assetNetUsed : [{"key":"1003406","value":0}]
     * assetNetLimit : [{"key":"1003406","value":0}]
     * TotalNetLimit : 43200000000
     * TotalNetWeight : 26366941257
     */

    private int freeNetUsed;
    private int freeNetLimit;
    private int NetLimit;
    private long TotalNetLimit;
    private long TotalNetWeight;
    private List<AssetNetUsedBean> assetNetUsed;
    private List<AssetNetLimitBean> assetNetLimit;

    public int getFreeNetUsed() {
        return freeNetUsed;
    }

    public void setFreeNetUsed(int freeNetUsed) {
        this.freeNetUsed = freeNetUsed;
    }

    public int getFreeNetLimit() {
        return freeNetLimit;
    }

    public void setFreeNetLimit(int freeNetLimit) {
        this.freeNetLimit = freeNetLimit;
    }

    public int getNetLimit() {
        return NetLimit;
    }

    public void setNetLimit(int NetLimit) {
        this.NetLimit = NetLimit;
    }

    public long getTotalNetLimit() {
        return TotalNetLimit;
    }

    public void setTotalNetLimit(long TotalNetLimit) {
        this.TotalNetLimit = TotalNetLimit;
    }

    public long getTotalNetWeight() {
        return TotalNetWeight;
    }

    public void setTotalNetWeight(long TotalNetWeight) {
        this.TotalNetWeight = TotalNetWeight;
    }

    public List<AssetNetUsedBean> getAssetNetUsed() {
        return assetNetUsed;
    }

    public void setAssetNetUsed(List<AssetNetUsedBean> assetNetUsed) {
        this.assetNetUsed = assetNetUsed;
    }

    public List<AssetNetLimitBean> getAssetNetLimit() {
        return assetNetLimit;
    }

    public void setAssetNetLimit(List<AssetNetLimitBean> assetNetLimit) {
        this.assetNetLimit = assetNetLimit;
    }

    public static class AssetNetUsedBean {
        /**
         * key : 1003406
         * value : 0
         */

        private String key;
        private int value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    public static class AssetNetLimitBean {
        /**
         * key : 1003406
         * value : 0
         */

        private String key;
        private int value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}
