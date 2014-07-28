/*
 * I'm following an example from here:
 * http://www.softwarepassion.com/android-series-custom-listview-items-and-adapters/
 */
package com.example.gt_coc_studentevents;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EventListActivity extends ListActivity {
	
	private ProgressDialog pg = null;
	private ArrayList<EventListing> events;
	private EventAdapter adapter;
	private Runnable viewEvents;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_list);

//		if (savedInstanceState == null) {
//			getFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
//		}
		
		events = new ArrayList<EventListing>();
		adapter = new EventAdapter(this, R.layout.row, events);
		setListAdapter(this.adapter);
		
		viewEvents = new Runnable() {
			@Override
			public void run() {
				getEvents();
			}
			
		};
		
		Thread thread = new Thread(null, viewEvents, "MagnetoBackground");
		thread.start();
		pg = ProgressDialog.show(this, "Please wait...", "Retrieving data...", true);
		setUpdateAlarm(this);
				
	}
	
		
	private void setUpdateAlarm(Context context) {
		if (true) {
			Intent updater = new Intent(context, UpdateReceiver.class);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, 
					updater, PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 30000, 30000, pendingIntent);
			Log.d("EventListActivity", "Repeating alarm has been set");
		}
		
	}
		


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
            startActivity(new Intent(EventListActivity.this, PreferenceWithHeaders.class));
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
			View rootView = inflater.inflate(R.layout.fragment_event_list,
					container, false);
			return rootView;
		}
	}
	
	/*
	 * 
	 */
	private void getEvents(){
        try {
            events = XmlReader.buildList();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		
		
		try {
			FileOutputStream fOut = openFileOutput("eventlist.ser", Context.MODE_PRIVATE);
			ObjectOutputStream objOut = new ObjectOutputStream(fOut);
			objOut.writeObject(events);
			objOut.close();
			fOut.close();
			Log.i("EventListActivity", "Serializable list saved");
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("EventListActivity", e.getMessage() );
		}
		
		runOnUiThread(returnRes);
		
	}

    public void logoutUser(View view) {
        SharedPreferences sp = getSharedPreferences(EventApp.PACKAGE_BASE, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
        AccountManager am = AccountManager.get(this);
        if (am != null) {
            Account[] accounts = am.getAccountsByType(EventApp.PACKAGE_BASE);
            for (Account account:accounts) {
                am.removeAccount(account, null, null);
            }
        }
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
	
	/*
	 * 
	 */
	private Runnable returnRes = new Runnable() {
		@Override
		public void run() {
			if (events != null && events.size() > 0){
				adapter.notifyDataSetChanged();
				for (int i=0;i<events.size();i++){
					adapter.add(events.get(i));
				}

			}
			pg.dismiss();
			adapter.notifyDataSetChanged();
		}
	};
	
	/*
	 * Method run when a row view is clicked
	 */
	public void showDetails (View view) {
		Intent intent = new Intent(this, ListDetailsActivity.class);
		
		Log.e("GET_ROW_ID", Integer.toString(view.getId()));
		
		EventListing e = events.get( view.getId() );
		
		intent.putExtra("NAME", e.getEventName() );
		intent.putExtra("TIME", e.getTime());
		intent.putExtra("LOCATION", e.getLocation());
		intent.putExtra("DESCRIPTION", e.getDescription());
		
		startActivity(intent);
	}
	
	/*
	 * Generates a view for a single event
	 */
	private class EventAdapter extends ArrayAdapter<EventListing> {
		
		private ArrayList<EventListing> eventList;

		public EventAdapter(Context context, int textViewResourceId,
				ArrayList<EventListing> eventList) {
			super(context, textViewResourceId, eventList);
			this.eventList = eventList;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.row, null);
			}
			EventListing evt = eventList.get(position);
			v.setId(position);
			Log.e("SET_ROW_ID", Integer.toString(v.getId()));
			if (evt != null){
				TextView tt = (TextView) v.findViewById(R.id.toptext);
				TextView bt = (TextView) v.findViewById(R.id.bottomtext);
				if (tt != null)
					tt.setText(evt.getEventName());
				if (bt != null)
					bt.setText(evt.getTime());
			}
			return v;
		}
	}//end private class

}
