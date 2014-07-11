package com.winify.happy_hours.activities.service;

import java.util.Calendar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import com.winify.happy_hours.R;
import com.winify.happy_hours.activities.controller.Prefs;
import com.winify.happy_hours.activities.listeners.ServiceListener;

public class CalendarActivity extends Prefs implements View.OnClickListener{
    private int mYear;
    private int mMonth;
    private int mDay;

    private TextView from;
    private TextView till;

    private int viewId;
    private Button fromBtn;private Button tillBtn ;

    static final int DATE_DIALOG_ID= 0;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_date);

        from = (TextView) findViewById(R.id.startDate);
        till= (TextView) findViewById(R.id.endDate);
        fromBtn = (Button) findViewById(R.id.fromDate);
        fromBtn.setOnClickListener(this);
        tillBtn = (Button) findViewById(R.id.tillDate);
        tillBtn.setOnClickListener(this);






        // get the current date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // display the current date
        updateDisplay(from);
        updateDisplay(till);
    }

    private void updateDisplay(TextView tv) {
        tv.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mMonth + 1).append("-")
                        .append(mDay).append("-")
                        .append(mYear).append(" "));
    }


    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;

                    switch(viewId){
                        case 1 : {updateDisplay(from);}break;
                        case 2 : {updateDisplay(till);}break;

                    }


                }
            };

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fromDate :{viewId=1; showDialog(DATE_DIALOG_ID); }break;

            case R.id.tillDate :{viewId=2; showDialog(DATE_DIALOG_ID);}break;
        }


    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);




        }
        return null;
    }

}