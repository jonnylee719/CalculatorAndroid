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
    private String num2 = "";
    private String crtOper;
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
        Log.d(TAG, "Set up firstfragment");

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

    private String formatNum(String s){
        Log.d(TAG, "string s = "+s);
        String formattedString = "";
        if(s.equals("")){
            return s;
        }

        BigDecimal bd = new BigDecimal(s);
        formattedString = numberFormat.format(bd);
        Log.d(TAG, "num after formatting : " + formattedString);
        return formattedString;
    }

    @Override
    public void numButClicked(View view) {
        // Gets the number from the clicked button and assigned to local variable butTop
        Button clickedButton = (Button) view;
        String butTop = clickedButton.getText()+"";

        // Assigns display textview in CalViewFragment
        TextView display = (TextView) findViewById(R.id.displayView);

        //2 Scenarios:
        //a) operEntered == false, still inputing num1
        //b) operEntered == true, meaning it's at num2

        if(operEntered==false){
            if(num1Entered == false){
                display.setText(butTop);
                if(butTop.equals("0")){}
                else{
                    num1 = butTop;
                    num1Entered = true;
                }
            }
            else if(num1Entered == true){
                num1 = num1 + butTop;
                display.setText(formatNum(num1));

            }
        }
        else if(operEntered == true){
            if(num2Entered == false){
                display.setText(formatNum(num1) + crtOper + butTop);
                if(butTop.equals("0")){}
                else{
                    num2Entered = true;
                    num2 = butTop;
                }
            }
            else if(num2Entered == true){
                num2 = num2 + butTop;
                display.setText(formatNum(num1) + crtOper + formatNum(num2));
            }
        }
    }

    @Override
    public void operButClicked(View view) {
        // Gets the number from the clicked button and assigned to local variable butTop
        Button clickedButton = (Button) view;
        String butTop = clickedButton.getText()+"";
        Log.d(TAG, "butTop equals " + butTop);

        // Assigns display textview in CalViewFragment
        TextView display = (TextView) findViewById(R.id.displayView);

        if(display.getText().equals("")){
            Log.d(TAG, "display.getText == nothing");
            if(!result.equals("")){
                num1 = result;
                num1Entered = true;
                model.setTotal(num1);
                display.setText(formatNum(num1));
                operButClicked(view);
            }
            else {
                display.setText("");
            }
        }
        else if(operEntered == false){
            if(num1Entered == true){
                model.setTotal(num1);
                crtOper = butTop;
                operEntered = true;
                display.setText(formatNum(num1) + crtOper);
            }
            else if(num1Entered == false){
                if(!result.equals("")){
                    num1 = result;
                    result = "";
                    crtOper = butTop;
                    operEntered = true;
                    model.setTotal(num1);
                    display.setText(formatNum(num1) + crtOper);
                }
                else if(result.equals("")){
                        display.setText("");
                }
            }
        }
        else if(operEntered == true){
            if(num2 == ""){
                crtOper = butTop;
                display.setText(formatNum(num1) + crtOper);
            }
            else{
                result = doOperation(crtOper);
                //Treat result as num1
                num1 = result;
                num1Entered = true;
                //Change the crtOper to the button pressed
                crtOper = butTop;
                operEntered = true;
                display.setText(formatNum(num1) + crtOper);
            }
        }

    }

    @Override
    public void equalButClicked(View button){
        //Get display
        TextView display = (TextView) findViewById(R.id.displayView);

        if(operEntered == true){
            if(num2Entered == true){
                result = doOperation(crtOper);
                display.setText(formatNum(result));
            }
            else if(num2Entered == false){
                operEntered = false;
                equalButClicked(button);
            }
        }
        else if(operEntered == false){
            if(num1Entered == false){
                if(!result.equals("")){
                    display.setText(formatNum(result));
                }
                else{
                    display.setText("");
                }
            }
            else if(num1Entered == true){
                result = num1;
                num1 = "";
                num1Entered = false;
                display.setText(formatNum(result));
            }
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
        return rlt;
    }

    public void cancelButClicked(View view){
        // Assigns display textview in CalViewFragment
        TextView display = (TextView) findViewById(R.id.displayView);

        if(operEntered == true){
            if(num2Entered == true){
                int length = num2.length();
                num2 = num2.substring(0, (length - 1));
                // if the entire num2 is deleted then num2Entered should be false
                if(num2.length() == 0){
                    num2Entered = false;
                    display.setText(formatNum(num1) + crtOper);
                }
                else {
                    display.setText(formatNum(num1) + crtOper + formatNum(num2));
                }
            }
            else if(num2Entered == false){
                crtOper = "";
                operEntered = false;
                display.setText(formatNum(num1));
            }
        }
        else if(operEntered == false){
            if(num1Entered == true){
                int length = num1.length();
                num1 = num1.substring(0, (length-1));
                // if the entire num1 is deleted then num1Entered should be false
                if(num1.length() == 0){
                    num1Entered = false;
                    display.setText("");
                }
                else{
                    display.setText(formatNum(num1));
                }
            }
            else if(result != ""){
                    int length = result.length();
                    result = result.substring(0, (length-1));
                    // if the entire result is deleted
                    if(result.length() == 0){
                        display.setText("");
                    }
                    else{
                        display.setText(formatNum(result));
                    }
            }
            else if(display.getText().equals("")){
                    display.setText("");
            }
        }


    }
}
