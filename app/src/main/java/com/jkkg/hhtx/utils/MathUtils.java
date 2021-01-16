package com.jkkg.hhtx.utils;

import java.math.BigDecimal;

/**
 * 计算工具类
 * ROUND_HALF_EVEN  银行家舍入法
 */
public class MathUtils {
    //精确到n位小数不保留小数位 向下截断
    public static String getBigDecimalRundNumberDownSTZ(String number, int n) {
        if (StringUtils.isNotEmpty(number) && Float.parseFloat(number) > 0) {
            BigDecimal resultBD = new BigDecimal(number.trim()).setScale(n, BigDecimal.ROUND_DOWN);
            return resultBD.stripTrailingZeros().toPlainString();
        } else {
            return String.valueOf(0);
        }
    }

    //精确到n位小数保留小数位 向下截断
    public static String getBigDecimalRundNumberDown(String number, int n) {
        if (StringUtils.isNotEmpty(number) && Float.parseFloat(number) > 0) {
            BigDecimal resultBD = new BigDecimal(number.trim()).setScale(n, BigDecimal.ROUND_DOWN);
            return resultBD.toPlainString();
        } else {
            return "0.00";
        }
    }

    //精确到n位小数保留小数位 向上截断
    public static String getBigDecimalRundNumberUp(String number, int n) {
        if (StringUtils.isNotEmpty(number) && Float.parseFloat(number) > 0) {
            BigDecimal resultBD = new BigDecimal(number.trim()).setScale(n, BigDecimal.ROUND_UP);
            return resultBD.toPlainString();
        } else {
            return "0.00";
        }
    }

    //精确到n位小数不保留小数位
    public static String getBigDecimalRundNumberSTZ(String number, int n) {
        if (StringUtils.isNotEmpty(number) && Float.parseFloat(number) > 0) {
            BigDecimal resultBD = new BigDecimal(number.trim()).setScale(n, BigDecimal.ROUND_HALF_EVEN);
            return resultBD.stripTrailingZeros().toPlainString();
        } else {
            return String.valueOf(0);
        }
    }

    //精确到n位小数保留小数位
    public static String getBigDecimalRundNumber(String number, int n) {
        if (StringUtils.isNotEmpty(number) && Float.parseFloat(number) > 0) {
            BigDecimal resultBD = new BigDecimal(number.trim()).setScale(n, BigDecimal.ROUND_HALF_EVEN);
            return resultBD.toPlainString();
        } else {
            return "0.00";
        }
    }

    //BigDecimal加
    public static String getBigDecimalAdd(String number1, String number2, int n) {
        if (StringUtils.isNotEmpty(number1, number2)) {
            BigDecimal aBD = new BigDecimal(number1.trim()).setScale(n, BigDecimal.ROUND_HALF_EVEN);
            BigDecimal bBD = new BigDecimal(number2.trim()).setScale(n, BigDecimal.ROUND_HALF_EVEN);
            BigDecimal resultBD = aBD.add(bBD).setScale(n, BigDecimal.ROUND_HALF_EVEN);
            return resultBD.toPlainString();
        } else {
            return String.valueOf(0);
        }
    }

    //BigDecimal减
    public static String getBigDecimalSubtract(String number1, String number2, int n) {
        if (StringUtils.isNotEmpty(number1, number2)) {
            BigDecimal aBD = new BigDecimal(number1.trim()).setScale(n, BigDecimal.ROUND_HALF_EVEN);
            BigDecimal bBD = new BigDecimal(number2.trim()).setScale(n, BigDecimal.ROUND_HALF_EVEN);
            BigDecimal resultBD = aBD.subtract(bBD).setScale(n, BigDecimal.ROUND_HALF_EVEN);
            return resultBD.toPlainString();
        } else {
            return String.valueOf(0);
        }
    }

    //BigDecimal乘
    public static String getBigDecimalMultiply(String number1, String number2, int n) {
        if (StringUtils.isNotEmpty(number1, number2) && Float.parseFloat(number1) != 0 && Float.parseFloat(number2) != 0) {
            BigDecimal aBD = new BigDecimal(number1.trim()).setScale(n, BigDecimal.ROUND_HALF_EVEN);
            BigDecimal bBD = new BigDecimal(number2.trim()).setScale(n, BigDecimal.ROUND_HALF_EVEN);
            BigDecimal resultBD = aBD.multiply(bBD).setScale(n, BigDecimal.ROUND_HALF_EVEN);
            return resultBD.toPlainString();
        } else {
            return String.valueOf(0);
        }
    }

    //BigDecimal除
    public static String getBigDecimalDivide(String number1, String number2, int n) {
        if (StringUtils.isNotEmpty(number1, number2) && Float.parseFloat(number1) != 0 && Float.parseFloat(number2) != 0) {
            BigDecimal aBD = new BigDecimal(number1.trim()).setScale(n, BigDecimal.ROUND_HALF_EVEN);
            BigDecimal bBD = new BigDecimal(number2.trim()).setScale(n, BigDecimal.ROUND_HALF_EVEN);
            BigDecimal resultBD = aBD.divide(bBD, n, BigDecimal.ROUND_HALF_EVEN);
            return resultBD.toPlainString();
        } else {
            return String.valueOf(0);
        }
    }

    //BigDecimal乘方
    public static String getBigDecimalPow(String number1, int number2, int n) {
        if (StringUtils.isNotEmpty(number1)) {
            BigDecimal aBD = new BigDecimal(number1.trim()).setScale(n, BigDecimal.ROUND_HALF_EVEN);
            BigDecimal resultBD = aBD.pow(number2).setScale(n, BigDecimal.ROUND_HALF_EVEN);
            return resultBD.toPlainString();
        } else {
            return String.valueOf(0);
        }
    }

    //BigDecimal 10的8次方
    public static String getBigDecimal10Pow8() {
        BigDecimal aBD = new BigDecimal("10").setScale(8, BigDecimal.ROUND_HALF_EVEN);
        BigDecimal resultBD = aBD.pow(8).setScale(8, BigDecimal.ROUND_HALF_EVEN);
        return resultBD.toPlainString();
    }

    //BigDecimal 10的18次方
    public static String getBigDecimal10Pow18() {
        BigDecimal aBD = new BigDecimal("10").setScale(8, BigDecimal.ROUND_HALF_EVEN);
        BigDecimal resultBD = aBD.pow(18).setScale(8, BigDecimal.ROUND_HALF_EVEN);
        return resultBD.toPlainString();
    }

    //BigDecimal对比 BigDecimal为小于val返回-1，如果BigDecimal为大于val返回1，如果BigDecimal为等于val返回0
    public static int getBigDecimalCompareTo(String number1, String number2, int n) {
        if (StringUtils.isNotEmpty(number1, number2)) {
            BigDecimal aBD = new BigDecimal(number1.trim()).setScale(n, BigDecimal.ROUND_HALF_EVEN);
            BigDecimal bBD = new BigDecimal(number2.trim()).setScale(n, BigDecimal.ROUND_HALF_EVEN);
            return aBD.compareTo(bBD);
        } else {
            return -2;
        }
    }


}
