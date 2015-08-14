package com.simplea.jonnylee.calculator;

import java.math.BigDecimal;

/**
 * CalModel.java
 * Purpose: Model for the Calculator android app. Stores total and manipulates total.
 * @author Jonnylee
 * @version 1.0 08/08/2015
 */

public class CalModel {
    private static final int INITIAL_VALUE = 0;
    private BigDecimal total;

    public CalModel(){
        reset();
    }

    public void reset(){
        total = BigDecimal.valueOf(INITIAL_VALUE);
    }

    public String getTotal(){
        return total.toString();
    }

    public void setTotal(String in){
        total = new BigDecimal(in);
    }

    public void add(String in){
        BigDecimal augend = new BigDecimal(in);
        total = total.add(augend);
    }

    public void subtract(String in){
        BigDecimal subtrahend = new BigDecimal(in);
        total = total.subtract(subtrahend);
    }

    public void multiply(String in){
        BigDecimal multiplicand = new BigDecimal(in);
        total = total.subtract(multiplicand);
    }

    public void divide(String in){
        BigDecimal divisor = new BigDecimal(in);
        total = total.divide(divisor);
    }
}
