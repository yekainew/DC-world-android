package com.jkkg.hhtx.utils;

import lombok.Getter;

public enum PublicChainNameEnum {
    比特币("BTC"),波场("Tron"),以太坊("ETH");

    @Getter
    String name;

    PublicChainNameEnum(String name) {
        this.name = name;
    }
}
