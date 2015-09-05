package com.simplea.jonnylee.calculator;

import android.content.Context;

import java.util.ArrayList;

/**
 * Purpose: a database that stores previous calculation
 * Created by Jonathan on 25/8/15.
 */
public class CalHistory {
    private static CalHistory sCalHistory;
    private Context mAppContext;
    private ArrayList<Equation> equationList;

    private CalHistory(Context appContext){
        mAppContext = appContext;
        equationList = new ArrayList<Equation>();
    }

    public static CalHistory get(Context c){
        if(sCalHistory == null){
            sCalHistory = new CalHistory(c.getApplicationContext());
        }
        return sCalHistory;
    }

    public void setEquationList(ArrayList<Equation> oldList){equationList = oldList;}

    public ArrayList<Equation> getEquationList(){
        return equationList;
    }

    public void addEquation(String num1, String formattedNum1, String num2, String formattedNum2, String result, String formattedResult, String oper){
        Equation newEquation = new Equation(num1, formattedNum1, num2, formattedNum2, result, formattedResult, oper);
        equationList.add(newEquation);
    }

    public Equation getEquation(int equaIndex){
        Equation retriEqua = equationList.get(equaIndex);
        return retriEqua;
    }

    public void clearHistory(){
        equationList.clear();
    }
}
