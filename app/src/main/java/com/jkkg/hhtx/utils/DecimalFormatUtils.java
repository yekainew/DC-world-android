package com.jkkg.hhtx.utils;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2019/7/22.
 */

public class DecimalFormatUtils {

    public static String formatNumber(double number, String pattern) {
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(number);
    }
}
