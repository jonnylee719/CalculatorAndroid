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
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
    private int decimalPlace = 2;

    private NumberFormat nf = NumberFormat.getInstance();
    private DecimalFormat numberFormat;

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

            firstFragment = createFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment).commit();
            firstFragment.onAttach(this);
        }
        setNumFormatter();
    }

    protected CalViewFragment createFragment(){
        return CalViewFragment.newInstance(fragmentParem1, fragmentParem2);
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
        numberFormat = new DecimalFormat("#,###.#####");
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
        if(neg == true){
            formattedString = "-" + formattedString;
        }
        BigDecimal bd = new BigDecimal(formattedString);
        formattedString = numberFormat.format(bd);
        return formattedString;
    }

    @Override
    public void numButClicked(View view) {
        TextView display = (TextView) findViewById(R.id.displayView);

        // Gets the number from the clicked button and assigned to local variable butTop
        Button clickedButton = (Button) view;
        String butTop = clickedButton.getText()+"";

        //2 Scenarios:
        //a) operEntered == false, still inputing num1
        //b) operEntered == true, meaning it's at num2

        if(operEntered==false){
            if(num1Entered == false){
                num1 = butTop;
                num1Entered = true;
            }
            else if(num1Entered == true){
                if(num1.equals("0")){
                    num1 = butTop;
                }
                else {
                    num1 = num1 + butTop;
                }
            }
            display.setText(formatNum(num1, negNum1));
        }
        else if(operEntered == true){
            if(num2Entered == false){
                num2 = butTop;
                num2Entered = true;
            }
            else if(num2Entered == true){
                if(num2.equals("0")){
                    num2 = butTop;
                }
                else {
                    num2 = num2 + butTop;
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

        if (operEntered == false) {
            if (num1Entered == true) {
                model.setTotal(num1);
                crtOper = butTop;
                operEntered = true;
                display.setText(formatNum(num1, negNum1) + crtOper);
            }
            else if (num1Entered == false) {
                if (!result.equals("")) {
                    if (result.indexOf("-") != -1) {      //means result is negative
                        negNum1 = true;
                        num1 = result.substring(1);
                    } else {                              //result is positive
                        num1 = result;
                    }
                    num1Entered = true;
                    model.setTotal(num1);
                    display.setText(formatNum(num1, negNum1) + crtOper);
                } else {
                    display.setText("0");
                }
            }
        }  // operEntered == false
        else if (operEntered == true) {
            if (num2Entered == false) {
                crtOper = butTop;
            }
            else if (num2Entered == true) {
                result = doOperation(crtOper);
                //Treat result as num1
                if (result.indexOf("-") != -1) {      //means result is negative
                    negNum1 = true;
                    num1 = result.substring(1);
                }
                else {                              //result is positive
                    num1 = result;
                }
                num1Entered = true;

                //Change the crtOper to the button pressed
                crtOper = butTop;
                operEntered = true;
            }
            display.setText(formatNum(num1, negNum1) + crtOper + formatNum(num2, negNum2));
        } //operEntered == true
    }

    @Override
    public void equalButClicked(View button){
        TextView display = (TextView) findViewById(R.id.displayView);
        if(operEntered == true){
            if(num2Entered == true){
                result = doOperation(crtOper);
            }
            else if(num2Entered == false){
                operEntered = false;
                equalButClicked(button);
            }
            display.setText(formatNum(result, false));
        }
        else if(operEntered == false){
            if(num1Entered == false){
                if(!result.equals("")){
                }
                else{
                }
            }
            else if(num1Entered == true){
                result = num1;
                num1 = "";
                num1Entered = false;
            }
            display.setText(formatNum(num1, negNum1));
        }
    }


    public String doOperation(String operator){
        String rlt = null;
        if(operator.equals("+")){
            //Log.d(TAG, "at doOperation for addition");
            //model.setTotal(num1);
            model.add(num2);
            rlt = model.getTotal();
            Log.d(TAG, "addition result = " + rlt);
        }
        else if(operator.equals("-")){
            //model.setTotal(num1);
            model.subtract(num2);
            rlt = model.getTotal();
            Log.d(TAG, "subtraction result = " + rlt);
        }
        else if(operator.equals("/")){
            //model.setTotal(num1);
            model.divide(num2);
            rlt = model.getTotal();
            Log.d(TAG, "division result = " + rlt);
        }
        //if(crtOper.equals("")), for some reason cannot get crtOper to equal to
        // so solved problem by the logic that if it's not other operators
        // it must be the multiplication operator
        else if(operator.equals("*")){
            //Log.d(TAG, "at doOperation for multiplication");
            //model.setTotal(num1);
            model.multiply(num2);
            rlt = model.getTotal();
            Log.d(TAG, "multiplication result = " + rlt);
        }
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
        int length = 0;
        if(operEntered == true){
            if(num2Entered == true){
                int decimalPosition = num2.indexOf('.');
                length = num2.length();
                if(length < 2){
                    num2 = num2.substring(0, (length-1));
                }
                else if((length-2) == decimalPosition){
                    num2 = num2.substring(0, (length-2));
                }
                else{
                    num2 = num2.substring(0, (length-1));
                }
                if(num2.length() == 0){  //all of num2 has been deleted
                    num2Entered = false;
                }
                display.setText(formatNum(num1, negNum1) + crtOper + formatNum(num2, negNum2));
            } //end of num2 entered
            else if(num2Entered == false){
                if(negNum2 == true){
                    negNum2 = false;
                    display.setText(formatNum(num1, negNum1) + crtOper);
                }
                else if(negNum2 == false){
                    crtOper = "";
                    operEntered = false;
                    display.setText(formatNum(num1, negNum1));
                }
            }
        } //end of operEntered == true
        else if(operEntered == false) {
            if(num1Entered == true){
                int decimalPosition = num1.indexOf('.');
                length = num1.length();
                if(length < 2){
                    num1 = num1.substring(0, (length-1));
                }
                else if((length-2) == decimalPosition){
                    num1 = num1.substring(0, (length-2));
                }
                else{
                    num1 = num1.substring(0, (length-1));
                }
                display.setText(formatNum(num1, negNum1));
                if(num1.length() == 0){  //all of num1 has been deleted
                    num1Entered = false;
                    display.setText("0");
                }
            } //end of num1 entered
            else if(num1Entered == false){
                if(negNum1 == true){
                    negNum1 = false;
                }
                else if(!result.equals("")){
                    result = "";
                }
                display.setText("0");
            }
        }
    }

    @Override
    public void negButClicked(View button){
        TextView display = (TextView) findViewById(R.id.displayView);
        if(operEntered == false){
            if(num1Entered == false){
                negNum1 = !negNum1;
                display.setText(formatNum(num1, negNum1));
            }
            else{}
        }
        else if(operEntered == true){
            if(num2Entered == false){
                negNum2 = !negNum2;
                display.setText(formatNum(num1, negNum1) + crtOper + formatNum(num2, negNum2));
            }
            else{}
        }
    }

}
