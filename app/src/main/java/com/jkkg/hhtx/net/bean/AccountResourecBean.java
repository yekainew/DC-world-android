package com.jkkg.hhtx.net.bean;

import com.mugui.base.bean.JsonBean;

import java.math.BigDecimal;
import java.util.List;

public class AccountResourecBean extends JsonBean {

    /**
     * freeNetUsed : 729
     * freeNetLimit : 5000
     * NetLimit : 1
     * assetNetUsed : [{"key":"1003406","value":0}]
     * assetNetLimit : [{"key":"1003406","value":0}]
     * TotalNetLimit : 43200000000
     * TotalNetWeight : 26366937476
     * EnergyUsed : 444
     * EnergyLimit : 471
     * TotalEnergyLimit : 90000000000
     * TotalEnergyWeight : 2674392200
     */

    private BigDecimal freeNetUsed;
    private BigDecimal freeNetLimit;
    private BigDecimal NetLimit;
    private long TotalNetLimit;
    private long TotalNetWeight;
    private BigDecimal EnergyUsed;
    private BigDecimal EnergyLimit;
    private long TotalEnergyLimit;
    private long TotalEnergyWeight;
    private List<AssetNetUsedBean> assetNetUsed;
    private List<AssetNetLimitBean> assetNetLimit;

    public BigDecimal getFreeNetUsed() {
        return freeNetUsed;
    }

    public void setFreeNetUsed(BigDecimal freeNetUsed) {
        this.freeNetUsed = freeNetUsed;
    }

    public BigDecimal getFreeNetLimit() {
        return freeNetLimit;
    }

    public void setFreeNetLimit(BigDecimal freeNetLimit) {
        this.freeNetLimit = freeNetLimit;
    }

    public BigDecimal getNetLimit() {
        return NetLimit;
    }

    public void setNetLimit(BigDecimal NetLimit) {
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

    public BigDecimal getEnergyUsed() {
        return EnergyUsed;
    }

    public void setEnergyUsed(BigDecimal EnergyUsed) {
        this.EnergyUsed = EnergyUsed;
    }

    public BigDecimal getEnergyLimit() {
        return EnergyLimit;
    }

    public void setEnergyLimit(BigDecimal EnergyLimit) {
        this.EnergyLimit = EnergyLimit;
    }

    public long getTotalEnergyLimit() {
        return TotalEnergyLimit;
    }

    public void setTotalEnergyLimit(long TotalEnergyLimit) {
        this.TotalEnergyLimit = TotalEnergyLimit;
    }

    public long getTotalEnergyWeight() {
        return TotalEnergyWeight;
    }

    public void setTotalEnergyWeight(long TotalEnergyWeight) {
        this.TotalEnergyWeight = TotalEnergyWeight;
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
