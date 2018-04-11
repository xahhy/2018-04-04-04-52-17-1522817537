package com.tw;

public class Utils {
    static double round(double number) {
        return Math.round(number * 100) / 100d;
    }

    public static String  doubleToString(Double score) {
        score = round(score);
        if (score.toString().endsWith(".0")){
            return String.valueOf(score.intValue());
        }else
            return score.toString();
    }
}
