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
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.BigInteger;
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

    @Override
    protected void onStart(){
        super.onStart();
        Button delBut = (Button) findViewById(R.id.delBut);
        delBut.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onLongClickDel();
                return true;
            }
        });
    }

    protected void createFragment(){
        firstFragment = CalViewFragment.newInstance(fragmentParem1, fragmentParem2);
    }

    @Override
    public void onLongClickDel(){
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

    private String formatNum(String numIn, boolean neg) {
        //This method only does not make any changes to the value of the number
        String formattedString = "";
        if (numIn.equals("") || numIn.equals("0.") || numIn.equals("0")) {
            if (neg == true) {
                formattedString = "-" + numIn;
            }
            else {
                formattedString = numIn;
            }
            Log.d(TAG, "Empty num Length at formatNum() = " + (formattedString.length() + ""));
            return formattedString;
        }
        formattedString = numIn;

        if (neg)
            formattedString = "-" + numIn;
        BigDecimal bd = new BigDecimal(formattedString);



        if (numIn.contains(".")) {                          //it is a fraction number
            Log.d(TAG, "Entered into formatNum() numIn has dp");
            int zerosAfterDp = 0;
            int dpPosition = numIn.indexOf(".");

            for (int i = dpPosition + 1; i < numIn.length(); i++) {
                if ((numIn.charAt(i) + "").equals("0")) {
                    zerosAfterDp++;
                } else {
                    break;
                }
            }
            Log.d(TAG, "No. of zeros after dp: " + zerosAfterDp);


            if ((numIn.substring(0, (dpPosition - 1)).length() > 9) || zerosAfterDp > 8) {
                numberFormatter.applyPattern("0.0###E0");
                formattedString = numberFormatter.format(bd);
                Log.d(TAG, "num with dp formatted as: " + formattedString);
            }
            else {
                numberFormatter.applyPattern("#,###.#########");
                formattedString = numberFormatter.format(bd.setScale(9, RoundingMode.HALF_UP));
                Log.d(TAG, "num with dp formatted as: " + formattedString);
            }

        }
        else {
            numberFormatter.setDecimalSeparatorAlwaysShown(false);
            if (numIn.length() > 10) {
                numberFormatter.applyPattern("0.####E0");
                formattedString = numberFormatter.format(bd);
            } else {
                numberFormatter.applyPattern("#,###");
                formattedString = numberFormatter.format(bd);
            }
        }

        return formattedString;

    }

    private String formatInputNum(String num, boolean neg){
        String formattedString = "";
        if (num.equals("") || num.equals("0.") || num.equals("0")) {
            if (neg == true) {
                formattedString = "-" + num;
            }
            else {
                formattedString = num;
            }
            return formattedString;
        }
        formattedString = num;
        if(neg){
            formattedString = "-" + num;
        }

        int dpPosition = formattedString.length();
        String fracPart = "";

        if(num.contains(".")){
            dpPosition = formattedString.indexOf(".");
            if(formattedString.substring(dpPosition).length()>10){
                fracPart = formattedString.substring(dpPosition, (dpPosition+9));
            }
            else {
                fracPart = formattedString.substring(dpPosition);
            }
        }

        // Formatting the Integer part
        String intString = formattedString.substring(0, dpPosition);

        //If this is a number of the previous result, then it might need to be expressed in scientific notation
        if(intString.length() > 9){
            formattedString = formatNum(num, neg);
            return formattedString;
        }

        BigInteger integerPart = new BigInteger(intString);
        String formattedIntPart = String.format("%,d", integerPart);

        formattedString = formattedIntPart + fracPart;

        return formattedString;
    }

    private void scrollDisplayToEnd(){
        final HorizontalScrollView scrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_RIGHT);
            }
        });
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
                        if(fractionPart.length() > 9){             //10 digits after the decimal point is the limit of num1
                            //do Nothing
                        }
                        else{
                            num1 = num1 + butTop;                   //appends the butTop
                        }
                    }
                    else {                                          // not a fraction number
                        if(num1.length()>9){
                            //do Nothing
                        }
                        else {
                            num1 = num1 + butTop;
                        }
                    }
                }
            }
            display.setText(formatInputNum(num1, negNum1));
            scrollDisplayToEnd();
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
                        if(fractionPart.length() > 9){             //10 digits after the decimal point is the limit of num
                            //do Nothing
                        }
                        else{
                            num2 = num2 + butTop;                   //appends the butTop
                        }
                    }
                    else {                                          // not a fraction number
                        if(num2.length()>9){
                            //do Nothing
                        }
                        else {
                            num2 = num2 + butTop;
                        }
                    }
                }
            }
            display.setText(formatNum(num1, negNum1) + crtOper + formatInputNum(num2, negNum2));
            scrollDisplayToEnd();
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
                display.setText(formatInputNum(num1, negNum1) + crtOper);
                scrollDisplayToEnd();
            } // end of !result
        }
        else if(num1Entered && !operEntered){
            Log.d(TAG, "num1Entered and !operEntered for operButClicked");
            if(num1.indexOf(".") == (num1.length() - 1)){
                num1 = num1.substring(0, (num1.indexOf(".")));     //if dp is at the end of num1
                Log.d(TAG, "dp is at the end of the num1");
            }
            crtOper = butTop;
            operEntered = true;
            display.setText(formatNum(num1, negNum1) + crtOper);
            scrollDisplayToEnd();
        }
        else if(operEntered) {           //there's 2 scenarios: 1) num2 not entered, so just switch the oper around, 2) num2 entered, doOperation() and treat as above
            if (!num2Entered) {
                if (negNum2) {
                } else {
                    crtOper = butTop;
                    display.setText(formatNum(num1, negNum1) + crtOper);
                    scrollDisplayToEnd();
                }
            } else {
                if (num2.indexOf(".") == (num2.length() - 1)) {
                    num2 = num2.substring(0, (num2.indexOf(".")));
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
            model.setTotal(negNumConvert(num1, negNum1));
            result = model.getTotal();
            num1 = "";
            negNum1 = false;
            num1Entered = false;
            display.setText(formatNum(result, false));
            scrollDisplayToEnd();
        }
        else if(operEntered && !num2Entered){
            if(negNum2){}
            else{
                if(negNum1){
                    result = "-" + num1;
                }
                result = num1;
                num1Entered = false;
                crtOper = "";
                operEntered = false;
                display.setText(formatNum(result, false));
                scrollDisplayToEnd();
            }
        }
        else if(num2Entered){
            if(num2.indexOf(".") == num2.length()){
                num2 = num2.substring(0, (num2.length() - 1));
            }
            result = doOperation(crtOper);
            display.setText(formatNum(result, false));
            scrollDisplayToEnd();
        }
    }

    private String negNumConvert(String num, boolean neg){
        if(neg){
            num = "-" + num;
        }
        return num;
    }

    public String doOperation(String operator){
        model.setTotal(negNumConvert(num1, negNum1));
        switch (operator){
            case "+":
                model.add(negNumConvert(num2, negNum2));
                break;
            case "-":
                model.subtract(negNumConvert(num2, negNum2));
                break;
            case "*":
                model.multiply(negNumConvert(num2, negNum2));
                break;
            case "/":
                model.divide(negNumConvert(num2, negNum2));
                break;
            default:
                break;
        }
        String rlt = model.getTotal();
        Log.d(TAG, operator.toString() + " operation equals " + rlt);

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
            onLongClickDel();          //deletes everything and setDisplay to "0"
        }
        else if(num1Entered && !operEntered){
            if(num1.length() > 20){                         //Length of textview limits the number of digits the user can see
                num1 = num1.substring(0, 20);
            }

            int length = num1.length();
            num1 = num1.substring(0, (length -1));

            if(num1.length() == 0){
                num1 = "";
                num1Entered = false;
                negNum1 = false;
                display.setText("0");
            }
            else{                                           //num1 still exists
                display.setText(formatInputNum(num1, negNum1));
                scrollDisplayToEnd();
            }
        }
        else if(operEntered && !num2Entered){
            if(negNum2){
                negNum2 = false;
                display.setText(formatInputNum(num1, negNum1) + crtOper);
                scrollDisplayToEnd();
            }
            else {
                crtOper = "";
                operEntered = false;
                display.setText(formatInputNum(num1, negNum1));
                scrollDisplayToEnd();
            }
        }
        else if(num2Entered){
            int length = num2.length();
            num2 = num2.substring(0, (length-1));

            if(num2.length() == 0){
                num2 = "";
                num2Entered = false;
                negNum2 = false;
                display.setText(formatInputNum(num1, negNum1) + crtOper);
                scrollDisplayToEnd();
            }
            else{                                               //num2 still exists
                display.setText(formatInputNum(num1, negNum1) + crtOper + formatInputNum(num2, negNum2));
                scrollDisplayToEnd();
            }
        }
    }

    @Override
    public void negButClicked(View button){
        TextView display = (TextView) findViewById(R.id.displayView);
        if(!operEntered){
            if(!negNum1){
                negNum1 = true;
            }
            else{
                negNum1 = false;
            }
            result = ""; //this might be straight after last operation
            display.setText(formatInputNum(num1, negNum1));
            scrollDisplayToEnd();
        }
        else if(!num2Entered && operEntered){
            if(!negNum2){
                negNum2 = true;
            }
            else{
                negNum2 = false;
            }
            display.setText(formatInputNum(num1, negNum1) + crtOper + formatInputNum(num2, negNum2));
            scrollDisplayToEnd();
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
                Log.d(TAG, "dpButClicked() num1 is entered");
                if(!num1.contains(".")){
                    num1 = num1 + ".";
                    Log.d(TAG, "dpButClicked() num1 is entered but does not contain dp, num1 is formated as: " + num1);
                }
                else{
                    //Do nothing
                }
            }
            display.setText(formatInputNum(num1, negNum1));
            scrollDisplayToEnd();
        }
        else if(operEntered){
            if(!num2Entered){
                num2 = "0.";
                num2Entered = true;
            }
            else if(num2Entered){
                if(!num2.contains(".")) {
                    num2 = num2 + ".";
                }
                else {
                    //Do Nothing
                }
            }
            display.setText(formatInputNum(num1, negNum1) + crtOper + formatInputNum(num2, negNum2));
            scrollDisplayToEnd();
        }
    }

}
