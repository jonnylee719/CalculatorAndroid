package com.simplea.jonnylee.calculator;

/**
 * Created by Jonathan on 25/8/15.
 */
public class Equation {
    String num1;
    String formattedNum1;
    String num2;
    String formattedNum2;
    String operator;
    String result;
    String formattedResult;


    public Equation(String numA, String formattedNumA, String numB, String formattedNumB, String answer, String formattedAns, String oper){
        num1 = numA;
        formattedNum1 = formattedNumA;
        num2 = numB;
        formattedNum2 = formattedNumB;
        operator = oper;
        result = answer;
        formattedResult = formattedAns;
    }

    public String getEquation(){
        String equation;
        equation = formattedNum1 + " " + operator + " " + formattedNum2 + " = " + formattedResult;
        return equation;
    }

    public String getNum1(){
        return num1;
    }

    public String getNum2(){
        return num1;
    }

    public String getResult(){
        return num1;
    }

    public String getOperator(){
        return operator;
    }

}
