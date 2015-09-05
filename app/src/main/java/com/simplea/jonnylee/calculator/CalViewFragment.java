package com.simplea.jonnylee.calculator;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.AttributedCharacterIterator;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalViewFragment extends Fragment implements Serializable {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String EQUATION_POSITION = "com.simplea.jonnylee.calculator.equation_position";
    private static final String TAG = "CalViewFragment";
    private static final String TEXT_IN_DISPLAY_KEY = "textInDisplayKey";
    private static final String LAST_EQUATION_DISPLAY_KEY = "lastEquationDisplayKey";


    // TODO: Rename and change types of parameters
    private OnFragmentInteractionListener mListener;
    public TextView displayView;
    public String textInDisplay;
    public String lastEquationDisplay;

    // TODO: Rename and change types and number of parameters
    public static CalViewFragment newInstance() {
        CalViewFragment fragment = new CalViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public CalViewFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        //Put textInDisplay in bundle
        savedInstanceState.putString(TEXT_IN_DISPLAY_KEY, textInDisplay);
        savedInstanceState.putString(LAST_EQUATION_DISPLAY_KEY, lastEquationDisplay);

        Log.d(TAG, "textInDisplay is: " + textInDisplay);
        Log.d(TAG, "lastEquationDisplay is: " + lastEquationDisplay);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState){
        super.onViewStateRestored(savedInstanceState);
        // This method is called after onActivityCreated(Bundle) and before onStart()
        if(savedInstanceState != null){
            textInDisplay = savedInstanceState.getString(TEXT_IN_DISPLAY_KEY);
            View view = getView();
            TextView displayView = (TextView)view.findViewById(R.id.displayView);
            displayView.setText(textInDisplay);

            lastEquationDisplay = savedInstanceState.getString(LAST_EQUATION_DISPLAY_KEY);
            TextView lastEquationView = (TextView)view.findViewById(R.id.lastEquatView);
            lastEquationView.setText(lastEquationDisplay);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d(TAG, "executes onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cal_view, container, false);

        //Set displayView 0 as default
        TextView displayView = (TextView)view.findViewById(R.id.displayView);
        displayView.setText("0");

        Button delButton = (Button) view.findViewById(R.id.delBut);
        delButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mListener.onLongClickDel();
                return false;
            }
        });

        Log.d(TAG, "onCreateView executed");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "executes onActivityCreated()");
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "executes onStart()");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "executes onResume()");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "executes onStop()");

        //Get the text in displayView and lastEquationView before calViewFragment's view get destroyed
        View view = getView();
        TextView displayView = (TextView)view.findViewById(R.id.displayView);
        TextView lastEquationView = (TextView)view.findViewById(R.id.lastEquatView);

        // Get current text in displayView and lastEquationDisplay
        textInDisplay = (String) displayView.getText();
        lastEquationDisplay = (String) lastEquationView.getText();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        Log.d(TAG, "executes onDestroyView()");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        //Calculator Functions
        void numButClicked(View button);
        void operButClicked(View button);
        void cancelButClicked(View button);
        void equalButClicked(View button);
        void negButClicked(View button);
        void dpButClicked(View button);

        //CalView view setup
        void onLongClickDel();
        void openCalHistoryListFragment(View textView);
    }




}
