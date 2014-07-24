package com.example.gt_coc_studentevents;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ListDetailsActivity extends ActionBarActivity {
	
	private TextView nameText, timeText, locationText, detailsText;
	private EventListing event;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_details);

		/*if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}*/
	}
	
	@Override
	 	public void onStart() {
	 		super.onStart();
	 		
	 		nameText = (TextView) findViewById(R.id.event_name);
	 		timeText = (TextView) findViewById(R.id.event_time);
	 		locationText = (TextView) findViewById(R.id.event_location);
	 		detailsText = (TextView) findViewById(R.id.event_details);
	 		
	 		event = new EventListing();
	 		
	 		Bundle extras = getIntent().getExtras();
	 		if (extras != null) {
	 			String name = extras.getString("NAME");
	 			String time = extras.getString("TIME");
	 			String location = extras.getString("LOCATION");
	 			String details = extras.getString("DESCRIPTION");
	 			
	 			try {
	 				event = new EventListing(name, location, time, details);
	 			}
	 			catch (NullPointerException e) {
	 				Log.d("Event Details", "Event detail extras are null");
	 			}
	 		}
	 		
	 		nameText.setText(event.getEventName());
	 		timeText.setText(event.getTime());
	 		locationText.setText(event.getLocation());
	 		detailsText.setText( Html.fromHtml( event.getDescription() ) );
	 	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_list_details,
					container, false);
			return rootView;
		}
	}

}
