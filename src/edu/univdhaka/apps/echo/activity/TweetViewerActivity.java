package edu.univdhaka.apps.echo.activity;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.univdhaka.apps.echo.dao.DBHelper;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TweetViewerActivity extends Activity {

	MainActivity main = new MainActivity();
	DBHelper helper = new DBHelper(this);
	private static String[] FROM = { DBHelper.C_ID, DBHelper.C_EVENT,
			DBHelper.C_Date, DBHelper.C_Type, DBHelper.C_IMAGE,DBHelper.C_Latitude,DBHelper.C_Longitude };
	private static String ORDER_BY = DBHelper.C_ID + " ASC";
	SQLiteDatabase db;
	// Adapter Object
	SimpleCursorAdapter adapter;
	ListView dataList;
	private Integer id;
	private String post;
	private String date;
	private String issue;
	byte[] image;
	private String latitude;
	private String longitude;
	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.database);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(DBHelper.TABLE, FROM, null, null, null, null,
				ORDER_BY);
		startManagingCursor(cursor);
		showEvents(cursor);

	}

	private void showEvents(Cursor cursor) {
		// Stuff them all into a big string
		setContentView(R.layout.database);

		dataList = (ListView) findViewById(R.id.list);

		// Instanciating an array list .
		List<String> arrayList = new ArrayList<String>();
		List<byte[]> blobList = new ArrayList<byte[]>();
		while (cursor.moveToNext()) {
			// Could use getColumnIndexOrThrow() to get indexes
			id = cursor.getInt(0);
			post = cursor.getString(1);
			date = cursor.getString(2);
			issue = cursor.getString(3);
			image = cursor.getBlob(4);
			latitude=cursor.getString(5);
			longitude=cursor.getString(6);
			arrayList.add(id + "\n" + post + "\n" + date + "\n" + issue+"\n"+latitude+"\n"+longitude);
			blobList.add(image);

		}
		// String from[]={"id","post","date","issue","image"};
		// int
		// to[]={R.id.id,R.id.postDone,R.id.dateList,R.id.issueList,R.id.postedPhoto};
		// This is the array adapter, it takes the context of the activity as a
		// first parameter, the type of list view as a second parameter and your
		// array as a third parameter.
		/*
		 * ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
		 * android.R.layout.simple_list_item_1, arrayList);
		 */
		BaseAdapter Adapter = new CustomAdapter(this, arrayList, blobList);

		dataList.setAdapter(Adapter);

	}

}

class CustomAdapter extends BaseAdapter {

	private int position;

	private Context context;
	private List<String> arrayList;
	private List<byte[]> byteList;

	public CustomAdapter(Context context, List<String> arrayList,
			List<byte[]> byteList) {
		super();
		this.context = context;
		this.arrayList = arrayList;
		this.byteList = byteList;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {

		View rowView;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		rowView = inflater.inflate(R.layout.listview_layout, null);

		TextView postText = (TextView) rowView.findViewById(R.id.posted);
		ImageView postedPhoto = (ImageView) rowView
				.findViewById(R.id.postedPhoto);
		postText.setText(arrayList.get(position));
		Log.d("CustomAdatpter", postText.getText().toString());
		byte[] imageArray = byteList.get(position);
		ByteArrayInputStream imageStream = new ByteArrayInputStream(imageArray);
		Bitmap theImage = BitmapFactory.decodeStream(imageStream);
		postedPhoto.setImageBitmap(theImage);

		return rowView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayList.size();
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return position;
	}

}
