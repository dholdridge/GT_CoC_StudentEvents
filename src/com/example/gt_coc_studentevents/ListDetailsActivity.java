package com.example.gt_coc_studentevents;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.CalendarContract;
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
import android.widget.Toast;

import java.util.Calendar;

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
            startActivity(new Intent(ListDetailsActivity.this, PreferenceWithHeaders.class));
            return true;
		}
		return super.onOptionsItemSelected(item);
	}

    @TargetApi(14)
    public void addToCalendar(View view) {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= 14) {
            SharedPreferences sp = getSharedPreferences(EventApp.PACKAGE_BASE, Context.MODE_PRIVATE);
            String email = sp.getString(EventApp.ACCOUNT_EMAIL_KEY, "");

            String date = timeText.getText().toString();
            int year = Integer.parseInt(date.substring(0,4));
            int month = Integer.parseInt(date.substring(5,7));
            int day = Integer.parseInt(date.substring(8,10));
            int hour = Integer.parseInt(date.substring(11,13));
            int minute = Integer.parseInt(date.substring(14,16));

            Calendar beginTime = Calendar.getInstance();
            Calendar endTime = Calendar.getInstance();
;
            switch(month) {
                case 1:
                    beginTime.set(year, Calendar.JANUARY, day, hour, minute);
                    endTime.set(year, Calendar.JANUARY, day, hour+1, minute);
                    break;
                case 2:
                    beginTime.set(year, Calendar.FEBRUARY, day, hour, minute);
                    endTime.set(year, Calendar.FEBRUARY, day, hour+1, minute);
                    break;
                case 3:
                    beginTime.set(year, Calendar.MARCH, day, hour, minute);
                    endTime.set(year, Calendar.MARCH, day, hour+1, minute);
                    break;
                case 4:
                    beginTime.set(year, Calendar.APRIL, day, hour, minute);
                    endTime.set(year, Calendar.APRIL, day, hour+1, minute);
                    break;
                case 5:
                    beginTime.set(year, Calendar.MAY, day, hour, minute);
                    endTime.set(year, Calendar.MAY, day, hour+1, minute);
                    break;
                case 6:
                    beginTime.set(year, Calendar.JUNE, day, hour, minute);
                    endTime.set(year, Calendar.JUNE, day, hour+1, minute);
                    break;
                case 7:
                    beginTime.set(year, Calendar.JULY, day, hour, minute);
                    endTime.set(year, Calendar.JULY, day, hour+1, minute);
                    break;
                case 8:
                    beginTime.set(year, Calendar.AUGUST, day, hour, minute);
                    endTime.set(year, Calendar.AUGUST, day, hour+1, minute);
                    break;
                case 9:
                    beginTime.set(year, Calendar.SEPTEMBER, day, hour, minute);
                    endTime.set(year, Calendar.SEPTEMBER, day, hour+1, minute);
                    break;
                case 10:
                    beginTime.set(year, Calendar.OCTOBER, day, hour, minute);
                    endTime.set(year, Calendar.OCTOBER, day, hour+1, minute);
                    break;
                case 11:
                    beginTime.set(year, Calendar.NOVEMBER, day, hour, minute);
                    endTime.set(year, Calendar.NOVEMBER, day, hour+1, minute);
                    break;
                case 12:
                    beginTime.set(year, Calendar.DECEMBER, day, hour, minute);
                    endTime.set(year, Calendar.DECEMBER, day, hour+1, minute);
                    break;
            }

            Intent intent = new Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                    .putExtra(CalendarContract.Events.TITLE, nameText.getText().toString())
                    .putExtra(CalendarContract.Events.DESCRIPTION, detailsText.getText().toString())
                    .putExtra(CalendarContract.Events.EVENT_LOCATION, locationText.getText().toString())
                    .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                    .putExtra(Intent.EXTRA_EMAIL, email);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), "Sorry, this feature is not supported on your version of Android",
                    Toast.LENGTH_LONG).show();
        }
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
