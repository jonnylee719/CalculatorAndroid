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
import android.view.Menu;
import android.view.MenuItem;



public class MainInterface extends AppCompatActivity implements CalViewFragment.OnFragmentInteractionListener{
    private static String fragmentParem1="";
    private static String fragmentParem2="";
    private CalViewFragment firstFragment;
    private CalModel model;

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

    @Override
    public void numButClicked(Uri uri) {

    }

    @Override
    public void operButClicked(Uri uri) {

    }
}
