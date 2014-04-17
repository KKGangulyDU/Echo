package edu.univdhaka.apps.echo.activity;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import edu.univdhaka.apps.echo.dao.*;
import edu.univdhaka.apps.echo.domain.Tweet;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity implements OnClickListener,
		LocationListener {
	private AutoCompleteTextView issues;
	static TextView dateSelection;
	static EditText postBox;
	private CheckBox dateConfirmation;
	private String issueTypes[] = { "Accident", "Fire",
			"local utility problem", "Theft", "Technological Problems" };

	SimpleCursorAdapter adapter;

	static final int ID_DATEPICKER = 0;
	private ImageView capturedPhoto;
	private ListView list;
	public DBHelper helper = new DBHelper(this);
	private SQLiteDatabase db;
	private byte[] byteArray;
	private boolean checkMark = false;
	private Tweet tweetPost;
	private Button addPhoto, removePhoto;
	private byte[] convertedImage;
	private String latituteField;
	private String longitudeField;
	private LocationManager locationManager;
	private String provider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tweetPost = new Tweet();
		postBox = (EditText) findViewById(R.id.post);
		issues = (AutoCompleteTextView) findViewById(R.id.autocomplete_issue);
		ArrayAdapter adapter = new ArrayAdapter(this,
				android.R.layout.simple_list_item_1, issueTypes);
		issues.setAdapter(adapter);

		dateSelection = (TextView) findViewById(R.id.display);
		Calendar c = Calendar.getInstance();
		SimpleDateFormat format1 = new SimpleDateFormat("EEEE,dd MMMM,yyyy",
				Locale.US);

		dateSelection.setText(format1.format(c.getTime()));
		dateSelection.setOnClickListener(this);
		dateConfirmation = (CheckBox) findViewById(R.id.confirmDate);
		dateConfirmation.setOnClickListener(this);

		addPhoto = (Button) findViewById(R.id.addPhoto);
		addPhoto.setOnClickListener(this);
		removePhoto = (Button) findViewById(R.id.removePhoto);
		removePhoto.setOnClickListener(this);
		capturedPhoto = (ImageView) findViewById(R.id.imageView1);
		this.trackLocation();
		ActionBar echoActionBar = getActionBar();
		echoActionBar.show();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		super.onCreateOptionsMenu(menu);

		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void post() {
		tweetPost.setIssues(issues.getText().toString());
		tweetPost.setPost(postBox.getText().toString());
		tweetPost.setSelectedDate(dateSelection.getText().toString());
		tweetPost.setConvertedImage(convertedImage);
		tweetPost.setLatitude(latituteField);
		tweetPost.setLongitude(longitudeField);
		if (checkMark == true && !tweetPost.getPost().matches("")
				&& !tweetPost.getIssues().matches("")) {
			SQLiteDatabase db = helper.getWritableDatabase();
			ContentValues values = new ContentValues();
			// values.put(DBHelper.C_EVENT,getPost().getText().toString());
			// System.out.println(getPost().getText(.toString());
			values.put(DBHelper.C_EVENT, tweetPost.getPost());
			values.put(DBHelper.C_Date, tweetPost.getSelectedDate());
			values.put(DBHelper.C_Type, tweetPost.getIssues());
			values.put(DBHelper.C_IMAGE, tweetPost.getConvertedImage());
			values.put(DBHelper.C_Latitude, tweetPost.getLatitude());
			values.put(DBHelper.C_Longitude, tweetPost.getLongitude());
			db.insertOrThrow(DBHelper.TABLE, null, values);
			Toast.makeText(this.getApplicationContext(), "Posted Successfully",
					Toast.LENGTH_SHORT).show();
			Intent i = new Intent(this, TweetViewerActivity.class);
			startActivity(i);
			checkMark = false;
		} else {
			Toast.makeText(this.getApplicationContext(),
					"check empty or uncheked field", Toast.LENGTH_SHORT).show();

		}
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.postMenuItem:
			this.post();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.display:
			Intent i = new Intent(this, DatePickerActivity.class);
			startActivity(i);
			break;
		case R.id.confirmDate:
			if (((CheckBox) v).isChecked()) {
				dateSelection.setClickable(false);
				checkMark = true;
			} else {
				dateSelection.setClickable(true);
			}
			break;

		case R.id.addPhoto:
			open();
			break;

		case R.id.removePhoto:
			this.capturedPhoto.setImageDrawable(null);
			Toast.makeText(this, "photo removed successfully",
					Toast.LENGTH_SHORT).show();
			break;

		}

	}

	public void trackLocation() {
		// Get the location manager
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// Define the criteria how to select the locatioin provider -> use
		// default
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);

		// Initialize the location fields
		if (location != null) {
			Toast.makeText(this,
					"Provider " + provider + " has been selected.",
					Toast.LENGTH_SHORT).show();
			onLocationChanged(location);
		} else {
			latituteField = "Location not available";
			longitudeField = "Location not available";
		}
	}

	/* Request updates at startup */
	@Override
	protected void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(provider, 400, 1, this);
	}

	/* Remove the locationlistener updates when Activity is paused */
	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
	}

	public void open() {
		Intent intent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, 0);
	}

	public void onProviderDisabled(String provider) {
		Toast.makeText(this, "Disabled provider " + provider,
				Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				// everything should be OK and you can process the expected
				// result
				if (data.getData() != null) {
					super.onActivityResult(requestCode, resultCode, data);
					Bitmap photo = (Bitmap) data.getExtras().get("data");
					capturedPhoto.setImageBitmap(photo);
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					photo.compress(Bitmap.CompressFormat.PNG, 100,
							byteArrayOutputStream);
					convertedImage = byteArrayOutputStream.toByteArray();
				}

			} else if (resultCode == RESULT_CANCELED) {
				// user explicitly canceled the called activity - you shouldn't
				// expect to process the expected result.

			} else {
				// not sure what happened here - but result isn't 'RESULT_OK' so
				// shouldn't expect to process the expected result.

			}
		}

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		int latitude = (int) (location.getLatitude());
		int longitude = (int) (location.getLongitude());
		latituteField = Location.convert(latitude, Location.FORMAT_DEGREES);
		longitudeField = Location.convert(longitude, Location.FORMAT_DEGREES);

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

}
