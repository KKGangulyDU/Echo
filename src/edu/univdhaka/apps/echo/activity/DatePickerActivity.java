package edu.univdhaka.apps.echo.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.R.string;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;

public class DatePickerActivity extends Activity  {
	  
	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.date);
	         
	        Button bt = (Button) findViewById(R.id.pickDate);
	        bt.setOnClickListener(new View.OnClickListener() {
								
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					DatePicker bt = (DatePicker) findViewById(R.id.datePicker);
					//TextView tv = (TextView) findViewById(R.id.display);

					int   day  = bt.getDayOfMonth();
					int   month= bt.getMonth();
					int   year = bt.getYear();
					
					@SuppressWarnings("deprecation")
					
					Calendar c = Calendar.getInstance();
					c.set(year, month, day);
					SimpleDateFormat format1;

					format1 = new SimpleDateFormat("EEEE,dd MMMM,yyyy", Locale.US);
					
					MainActivity.dateSelection.setText(format1.format(c.getTime()));
					finish();
				}
			});
	    }
}
