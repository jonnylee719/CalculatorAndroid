package com.simplea.jonnylee.calculator;

import android.util.Log;
import android.widget.TextView;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Jonathan on 25/8/15.
 */
public class Equation implements Serializable{
    String num1;
    String formattedNum1;
    String num2;
    String formattedNum2;
    String operator;
    String result;
    String formattedResult;
    private static final String TAG = "Equation";


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

    public String getFormattedNum1(){return formattedNum1;}

    public String getFormattedNum2(){return formattedNum2;}

    public String getFormattedResult(){return formattedResult;}

    public String getNum2(){
        return num2;
    }

    public String getResult(){
        return result;
    }

    public String getOperator(){
        return operator;
    }

}
