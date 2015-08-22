/**
 * MainInterface
 * Purpose: Acts as the controller for the Calculator android app. Connects ViewFragment and the
 *          CalModel while allowing them to remain encapsulated.
 * @author Jonnylee
 * @version 1.0 08/08/2015
 */

package com.simplea.jonnylee.calculator;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Locale;


public class MainInterface extends AppCompatActivity implements CalViewFragment.OnFragmentInteractionListener{
    private static String fragmentParem1="";
    private static String fragmentParem2="";
    private CalViewFragment firstFragment;
    private CalModel model;
    private static final String TAG = "MainInterface";

    //Controller variables for all the calculations
    private String num1 = "";
    private boolean negNum1 = false;
    private String num2 = "";
    private boolean negNum2 = false;
    private String crtOper = "";
    private String result = "";

    private DecimalFormat numberFormatter;

    //States that the controller can be in:
    //0 = no number entered yet
    //1 = num1 entered
    //2 = num1 entered and operator entered
    private boolean num1Entered = false;
    private boolean num2Entered = false;
    private boolean operEntered = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_interface);

        model = new CalModel();

        if(findViewById(R.id.fragment_container) != null){
            if (savedInstanceState != null){
                return;
            }

            createFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment).commit();
            firstFragment.onAttach(this);
        }
        setNumFormatter();
    }

    protected void createFragment(){
        firstFragment = CalViewFragment.newInstance(fragmentParem1, fragmentParem2);
    }

    @Override
    public void onLongClick(){
        TextView display = (TextView) findViewById(R.id.displayView);

        num1 = "";
        num1Entered = false;
        negNum1 = false;
        num2 = "";
        num2Entered = false;
        negNum2 = false;
        crtOper = "";
        operEntered = false;
        display.setText("0");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_interface, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void setNumFormatter(){
        numberFormatter = new DecimalFormat();
    }

    private String formatNum(String numIn, boolean neg){
        //This method only does not make any changes to the value of the number
        String formattedString = "";
        if(numIn.equals("")){
            if(neg == true){
                formattedString = "-" + "";
            }
            return formattedString;
        }
        formattedString = numIn;
        if(neg)
            formattedString = "-" + numIn;
        BigDecimal bd = new BigDecimal(formattedString);
        if(numIn.contains(".")) {                          //it is a fraction number
           // numberFormatter.applyPattern("#,###.");
            formattedString = bd.setScale(5, RoundingMode.HALF_UP).toString();
        }
        else {
            bd = bd.setScale(9);
            String temp = bd.toString();
            if(temp.contains("E")){
                numberFormatter.applyPattern("0.####E0");
                formattedString = numberFormatter.format(bd);
            }
            else{
                numberFormatter.setDecimalSeparatorAlwaysShown(false);
                numberFormatter.applyPattern("#,###");
                formattedString = numberFormatter.format(bd);
            }
        }

        return formattedString;
    }

    @Override
    public void numButClicked(View view) {
        TextView display = (TextView) findViewById(R.id.displayView);

        // Gets the number from the clicked button and assigned to local variable butTop
        Button clickedButton = (Button) view;
        String butTop = clickedButton.getText()+"";

        //4 Scenarios:
        //a) before/ after num1Entered
        //b) before/ after num2Entered

        if(!operEntered) {
            if (!num1Entered) {
                result = ""; //might be straight after the last operation
                num1 = butTop;
                num1Entered = true;
            } else if (num1Entered) {
                if (num1.equals("0")) {
                    num1 = butTop;
                } else {
                    if(num1.indexOf(".") != -1){
                        int dp = num1.indexOf(".");                 //the position of the decimal point
                        String fractionPart = num1.substring(dp);
                        if(fractionPart.length() > 10){             //10 digits after the decimal point is the limit of num1
                            //do Nothing
                        }
                        else{
                            num1 = num1 + butTop;                   //appends the butTop
                        }
                    }
                    else {                                          // not a fraction number
                        if(num1.length()>10){
                            //do Nothing
                        }
                        else {
                            num1 = num1 + butTop;
                        }
                    }
                }
            }
            display.setText(formatNum(num1, negNum1));
        }
        else if(operEntered){               //oper has been entered
            if(!num2Entered){               //first check if num1 has been entered
                num2 = butTop;
                num2Entered = true;
            }
            else if(num2Entered){                          //num2 has been entered
                if(num2.equals("0")){
                    num2 = butTop;
                }
                else{
                    if(num2.indexOf(".") != -1){
                        int dp = num2.indexOf(".");                 //the position of the decimal point
                        String fractionPart = num2.substring(dp);
                        if(fractionPart.length() > 10){             //10 digits after the decimal point is the limit of num
                            //do Nothing
                        }
                        else{
                            num2 = num2 + butTop;                   //appends the butTop
                        }
                    }
                    else {                                          // not a fraction number
                        if(num2.length()>10){
                            //do Nothing
                        }
                        else {
                            num2 = num2 + butTop;
                        }
                    }
                }
            }
            display.setText(formatNum(num1, negNum1) + crtOper + formatNum(num2, negNum2));
        }
    }

    @Override
    public void operButClicked(View view) {
        TextView display = (TextView) findViewById(R.id.displayView);

        // Gets the number from the clicked button and assigned to local variable butTop
        Button clickedButton = (Button) view;
        String butTop = clickedButton.getText() + "";

        // 3 scenarios: 1) num1Entered is false
        //              2) operEntered is false
        //              3) operEntered is true

        if(!num1Entered){           // check for result
            if(negNum1){
                //do Nothing
            }
            else if(!result.equals("")){        //result contains something
                if(result.indexOf("-") == 0){   //result is negative
                    negNum1 = true;
                    num1 = result.substring(1);
                }
                else{
                    num1 = result;
                }
                num1Entered = true;
                result = "";
                crtOper = butTop;
                operEntered = true;
                display.setText(formatNum(num1, negNum1) + crtOper);
            } // end of !result
        }
        else if(num1Entered && !operEntered){
            if(num1.indexOf(".") == num1.length()){
                num1 = num1.substring(0, (num1.length() - 1));     //if dp is at the end of num1
            }
            crtOper = butTop;
            operEntered = true;
            display.setText(formatNum(num1, negNum1) + crtOper);
        }
        else if(operEntered) {           //there's 2 scenarios: 1) num2 not entered, so just switch the oper around, 2) num2 entered, doOperation() and treat as above
            if (!num2Entered) {
                if (negNum2) {
                } else {
                    crtOper = butTop;
                    display.setText(formatNum(num1, negNum1) + crtOper);
                }
            } else if (num2Entered) {
                if (num2.indexOf(".") == num2.length()) {
                    num2 = num2.substring(0, (num2.length() - 1));
                }
                result = doOperation(crtOper);
                if (result.indexOf("-") == 0) {
                    negNum1 = true;
                    num1 = result.substring(1);
                } else {                      //result is positive
                    num1 = result;
                }
                num1Entered = true;
                result = "";
                operButClicked(view);       //reloop as if this is a new operButClicked
            }
        }
    }

    @Override
    public void equalButClicked(View button){
        TextView display = (TextView) findViewById(R.id.displayView);
        //4 scenarios:
        //1) before num1Entered
        //2) after num1Entered + before operEntered
        //3) after operEntered + before num2Entered
        //4) after num2Entered

        if(!num1Entered){
            // do nothing because if it's showing result, you dont need to do anything
        }
        else if(num1Entered && !operEntered){
            if(num1.indexOf(".") == num1.length()){
                num1 = num1.substring(0, (num1.length() - 1));
            }
            result = num1;
            num1 = "";
            num1Entered = false;
            display.setText(formatNum(result, false));
        }
        else if(operEntered && !num2Entered){
            if(negNum2){}
            else{
                if(negNum1){
                    result = "-" + num1;
                }
                result = num1;
                num1Entered = false;
                display.setText(formatNum(result, false));
            }
        }
        else if(num2Entered){
            if(num2.indexOf(".") == num2.length()){
                num2 = num2.substring(0, (num2.length() - 1));
            }
            result = doOperation(crtOper);
            display.setText(formatNum(result, false));
        }
    }

    private String negNumConvert(String num, boolean neg){
        if(neg){
            num = "-" + num;
        }
        return num;
    }

    public String doOperation(String operator){
        String rlt = null;
        model.setTotal(negNumConvert(num1, negNum1));
        switch (operator){
            case "+":
                model.add(negNumConvert(num2, negNum2));
                Log.d(TAG, "addition result = " + rlt);
                break;
            case "-":
                model.subtract(negNumConvert(num2, negNum2));
                Log.d(TAG, "subtraction result = " + rlt);
                break;
            case "*":
                model.multiply(negNumConvert(num2, negNum2));
                Log.d(TAG, "multiplication result = " + rlt);
                break;
            case "/":
                model.divide(negNumConvert(num2, negNum2));
                Log.d(TAG, "division result = " + rlt);
                break;
            default:
                break;
        }
        rlt = model.getTotal();

        /*
        if(operator.equals("+")){
            //Log.d(TAG, "at doOperation for addition");
            model.setTotal(negNumConvert(num1, negNum1));
            model.add(negNumConvert(num2, negNum2));
            rlt = model.getTotal();
            Log.d(TAG, "addition result = " + rlt);
        }
        else if(operator.equals("-")){
            model.setTotal(negNumConvert(num1, negNum1));
            model.subtract(negNumConvert(num2, negNum2));
            rlt = model.getTotal();
            Log.d(TAG, "subtraction result = " + rlt);
        }
        else if(operator.equals("/")){
            model.setTotal(negNumConvert(num1, negNum1));
            model.divide(negNumConvert(num2, negNum2));
            rlt = model.getTotal();
            Log.d(TAG, "division result = " + rlt);
        }
        else if(operator.equals("*")){
            //Log.d(TAG, "at doOperation for multiplication");
            model.setTotal(negNumConvert(num1, negNum1));
            model.multiply(negNumConvert(num2, negNum2));
            rlt = model.getTotal();
            Log.d(TAG, "multiplication result = " + rlt);
        }

        */
        num1 = "";
        num2 = "";
        crtOper = "";
        operEntered = false;
        num1Entered = false;
        num2Entered = false;
        negNum1 = false;
        negNum2 = false;
        return rlt;
    }

    public void cancelButClicked(View view){
        TextView display = (TextView) findViewById(R.id.displayView);

        //4 scenarios:
        //1) before num1Entered
        //2) after num1Entered + before operEntered
        //3) after operEntered + before num2Entered
        //4) after num2Entered

        if(!num1Entered){
            onLongClick();          //deletes everything and setDisplay to "0"
        }
        else if(num1Entered && !operEntered){
            if(num1.length() > 20){                         //Length of textview limits the number of digits the user can see
                num1 = num1.substring(0, 20);
            }
            int decimalPosition = num1.indexOf(".");        //check if it's a fraction number
            int length = num1.length();
            if(decimalPosition == -1){                      //not a fraction number
                num1 = num1.substring(0, (length -1));
            }
            else if(decimalPosition != -1) {                 //is a fraction number
                if (decimalPosition == (length - 1)) {
                    num1 = num1.substring(0, (length - 2));
                } else {
                    num1 = num1.substring(0, (length - 1));
                }// end of decimal position != -1
            }
            if(num1.length() == 0){
                num1 = "";
                num1Entered = false;
                negNum1 = false;
                display.setText("0");
            }
            else{                                           //num1 still exists
                display.setText(formatNum(num1, negNum1));
            }
        }
        else if(operEntered && !num2Entered){
            if(negNum2){
                negNum2 = false;
                display.setText(formatNum(num1, negNum1) + crtOper);
            }
            else {
                crtOper = "";
                operEntered = false;
                display.setText(formatNum(num1, negNum1));
            }
        }
        else if(num2Entered){
            int decimalPosition = num2.indexOf(".");
            int length = num2.length();
            if(decimalPosition == -1){
                num2 = num2.substring(0, (length-1));
            }
            else if(decimalPosition != -1){
                if(decimalPosition == (length-1)){
                    num2 = num2.substring(0, length-2);
                }
                else {
                    num2 = num2.substring(0, (length-1));
                }
            }
            if(num2.length() == 0){
                num2 = "";
                num2Entered = false;
                negNum2 = false;
                display.setText(formatNum(num1, negNum1));
            }
            else{                                               //num2 still exists
                display.setText(formatNum(num1, negNum1) + crtOper + formatNum(num2, negNum2));
            }
        }
    }

    @Override
    public void negButClicked(View button){
        TextView display = (TextView) findViewById(R.id.displayView);
        if(!num1Entered){
            if(!negNum1){
                negNum1 = true;
            }
            else{
                negNum1 = false;
            }
            result = ""; //this might be straight after last operation
            display.setText(formatNum(num1, negNum1));
        }
        else if(!num2Entered && operEntered){
            if(!negNum2){
                negNum2 = true;
            }
            else{
                negNum2 = false;
            }
            display.setText(formatNum(num1, negNum1) + crtOper + formatNum(num2, negNum2));
        }
    }

    @Override
    public void dpButClicked(View button){
        TextView display = (TextView) findViewById(R.id.displayView);

        if(!operEntered){
            if(!num1Entered){
                num1 = "0.";
                num1Entered = true;
            }
            else if(num1Entered){
                num1 = num1 + ".";
            }
            display.setText(formatNum(num1, negNum1));
        }
        else if(operEntered){
            if(!num2Entered){
                num2 = "0.";
                num2Entered = true;
            }
            else if(num2Entered){
                num2 = num2 + ".";
            }
            display.setText(formatNum(num2, negNum2));
        }
    }

}
