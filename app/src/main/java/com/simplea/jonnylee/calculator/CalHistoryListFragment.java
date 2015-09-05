package com.simplea.jonnylee.calculator;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalHistoryListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalHistoryListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalHistoryListFragment extends Fragment implements Serializable{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "CalHistoryListFragment";

    private OnFragmentInteractionListener mListener;
    private ArrayAdapter mAdaptor;
    private ListView mListView;


    // TODO: Rename and change types and number of parameters
    public static CalHistoryListFragment newInstance() {
        CalHistoryListFragment fragment = new CalHistoryListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public CalHistoryListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "executes onCreate()");
        if (getArguments() != null) {}

        //Create CalHistoryAdaptor

        mAdaptor = new CalHisAdaptor(CalHistory.get(getActivity()).getEquationList());
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
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        Log.d(TAG, "executes onDestroyView()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cal_history, container, false);
        Log.d(TAG, "executes onCreateView()");
        mListView = (ListView) view.findViewById(R.id.list);
        mListView.setAdapter(mAdaptor);


        return view;
    }

    public void updateArrayAdaptor(){
        mAdaptor.notifyDataSetChanged();
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void retrieveEquation(int equationPosition);
        void clearCalHistory(View view);
        void goBackCalView(View view);
    }

    private class CalHisAdaptor extends ArrayAdapter<Equation> {
        private static final String TAG = "CalHisAdaptor";
        public CalHisAdaptor(ArrayList<Equation> equations){
            super(getActivity(), 0, R.id.equation_textView, equations);
        }

        private class ViewHolder{
            RelativeLayout equationFrame;
            TextView num1;
            TextView num2;
            TextView oper;
            TextView result;
            int equationPosition;
        }

        public View getView(int position, View convertView, ViewGroup parent){
            ViewHolder holder = null;
            View viewToUse = null;

            //This is getting the specific equation from CalHistory
            Equation choosenEqua = getItem(position);

            //if there was no previous view, inflate one
            if(convertView == null){
                viewToUse = getActivity().getLayoutInflater().inflate(R.layout.fragment_cal_history_list_view_layout, null);

                holder = new ViewHolder();
                holder.equationFrame = (RelativeLayout) viewToUse.findViewById(R.id.calHisList_equation_frame);
                holder.num1 = (TextView) viewToUse.findViewById(R.id.equation_textView);
                holder.num2 = (TextView) viewToUse.findViewById(R.id.calHisList_num2_view);
                holder.oper = (TextView) viewToUse.findViewById(R.id.calHisList_oper_view);
                holder.result = (TextView) viewToUse.findViewById(R.id.calHisList_result_view);

                holder.equationFrame.setOnClickListener(new EquationClickListener());


                viewToUse.setTag(holder);
                holder.equationFrame.setTag(holder);
            }// end of convertView is null
            else{
                viewToUse = convertView;
                holder = (ViewHolder) viewToUse.getTag();
            }

            if(position%2 == 0){
                holder.equationFrame.setBackgroundColor(Color.LTGRAY);
            }
            else{
                holder.equationFrame.setBackgroundColor(Color.TRANSPARENT);
            }

            holder.num1.setText(choosenEqua.getFormattedNum1());
            holder.num2.setText(choosenEqua.getFormattedNum2());

            holder.oper.setText(choosenEqua.getOperator());
            holder.result.setText(choosenEqua.getFormattedResult());

            holder.equationPosition = position;
            Log.d(TAG, "equationPosition = " + holder.equationPosition);

            return viewToUse;
        }

        private class EquationClickListener implements View.OnClickListener {

            private static final String TAG = "EquationClickListener";

            @Override
            public void onClick(View v) {
                ViewHolder viewHolder = (ViewHolder) v.getTag();
                int equationPosition = viewHolder.equationPosition;
                Log.d(TAG, "Equation " + equationPosition + " clicked.");
                mListener.retrieveEquation(equationPosition);


            }
        }
    }

}
