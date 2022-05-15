package com.depromeet.threedollar.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MathUtils {

    /**
     * digit: 보여질 소수점 자리수 기준
     * value=3.8888, digit=0 => 4
     * value=3.8888, digit=1 => 3.9
     */
    public static double round(double value, int digit) {
        if (value == 0) {
            return 0;
        }
        double pow = Math.pow(10, digit);
        return Math.round(value * pow) / pow;
    }

    public static double divide(int dividend, int divisor) {
        return divide(dividend, (double) divisor);
    }

    public static double divide(double dividend, double divisor) {
        if (divisor == 0) {
            return 0.0;
        }
        return dividend / divisor;
    }

}
