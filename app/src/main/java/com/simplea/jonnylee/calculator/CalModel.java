package com.simplea.jonnylee.calculator;

import android.util.Log;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * CalModel.java
 * Purpose: Model for the Calculator android app. Stores total and manipulates total.
 * @author Jonnylee
 * @version 1.0 08/08/2015
 */

public class CalModel {
    private static final int INITIAL_VALUE = 0;
    private BigDecimal total;
    private String TAG = "CalModel";
    private MathContext context = new MathContext(120, RoundingMode.HALF_UP);

    public CalModel(){
        reset();
    }

    public void reset(){
        total = BigDecimal.valueOf(INITIAL_VALUE, 120);
    }

    public String getTotal(){
        Log.d(TAG, "get Total as: " + total.toPlainString());
        return total.stripTrailingZeros().toPlainString();
    }

    public void setTotal(String in){
        total = stringToNum(in);
        Log.d(TAG, "set Total to: " + total);
    }

    public void add(String in){
        BigDecimal augend = stringToNum(in);
        total = total.add(augend);
    }

    public void subtract(String in){
        BigDecimal subtrahend = stringToNum(in);
        total = total.subtract(subtrahend);
    }

    public void multiply(String in){
        BigDecimal multiplicand = stringToNum(in);
        total = total.multiply(multiplicand);
    }

    public void divide(String in){
        BigDecimal divisor = stringToNum(in);
        total = total.divide(divisor, context);
    }

    public BigDecimal stringToNum(String numString){
        Log.d(TAG, "stringToNum numString is: " + numString);
        BigDecimal num = new BigDecimal(numString, context);
        return num;
    }



}
