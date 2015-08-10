package com.simplea.jonnylee.calculator;

/**
 * CalModel.java
 * Purpose: Model for the Calculator android app. Stores total and manipulates total.
 * @author Jonnylee
 * @version 1.0 08/08/2015
 */

public class CalModel {
    private static final int INITIAL_VALUE = 0;
    private int total;

    public CalModel(){
        reset();
    }

    public void reset(){
        total = INITIAL_VALUE;
    }

    public String getTotal(){
        String retValue = total + "";
        return retValue;
    }

    public void setTotal(String in){
        total = Integer.parseInt(in);
    }

    public void add(String in){
        int operand = Integer.parseInt(in);
        total = total + operand;
    }

    public void subtract(String in){
        int operand = Integer.parseInt(in);
        total = total - operand;
    }

    public void multiply(String in){
        int operand = Integer.parseInt(in);
        total = total * operand;
    }

    public void divide(String in){
        int operand = Integer.parseInt(in);
        total = total / operand;
    }
}
