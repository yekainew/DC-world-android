package com.jkkg.hhtx.utils;

import android.widget.TextView;

import java.math.BigDecimal;

public class CHSUtils {

    public static String ghs(BigDecimal bigDecimal) {
        if (bigDecimal.compareTo(new BigDecimal("10000")) > 0) {
            return (bigDecimal.subtract(new BigDecimal("10000")).add(new BigDecimal("100000")).stripTrailingZeros().toPlainString());
        } else {
            return (bigDecimal.multiply(new BigDecimal("10")).stripTrailingZeros().toPlainString());
        }
    }
}
