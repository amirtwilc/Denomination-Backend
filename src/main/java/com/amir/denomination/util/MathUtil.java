package com.amir.denomination.util;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class MathUtil {

    /**
     * Returns how many times a number is divided by a given value
     * @param number the number to divide
     * @param dividingValue the value to check how many times fits inside number
     * @return number of times dividingValue fits inside number
     */
    public static int getDividingTimes(BigDecimal number, BigDecimal dividingValue) {
        return (number.divideAndRemainder(dividingValue))[0].intValue();
    }
}
