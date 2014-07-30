/**
 * EventListActivity
 * Retrieves information remotely and displays the information in a ListView
 * @author Dan Holdridge
 * @version 1.0
 * 
 * I'm following an example from here:
 * http://www.softwarepassion.com/android-series-custom-listview-items-and-adapters/
 */
package cc.gatech.edu.coc_events;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import edu.gatech.cc.coc_events.R;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class EventListActivity extends ListActivity {
	
	private ProgressDialog pg = null;
	private ArrayList<EventListing> events;
	private EventAdapter adapter;
	private Runnable viewEvents;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
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
		
				
	}
	
		
	
		


	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_list, menu);
		return true;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
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
	
	/**
	 * Retrieves remote information and saves it locally
	 */
	private void getEvents(){
		try {
			events = XmlReader.buildList();
			
		} catch (Exception e) {
			//Couldn't retrieve information
			e.printStackTrace();
			//Log.e("BACKGROUND_PROC", e.getMessage());
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
	
	
	/**
	 * Adds everything in the arraylist events to the ArraylistAdapter, then
	 * dismisses the progress dialog
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
	
	/**
	 * Starts a new ListDetailsActivity with the information from the selected event
	 * @param view The ListView corresponding to the selected event
	 */
	public void showDetails (View view) {
		
		Intent intent = new Intent(this, ListDetailsActivity.class);
		
		//Log.d("GET_ROW_ID", Integer.toString(view.getId()));
		
		EventListing e = events.get( view.getId() );
		
		intent.putExtra("NAME", e.getEventName() );
		intent.putExtra("TIME", e.getTime());
		intent.putExtra("LOCATION", e.getLocation());
		intent.putExtra("DESCRIPTION", e.getDescription());
		
		startActivity(intent);
	}
	
	
	/** Adapter that can create a ListView for an EventListing object
	 * @author Dan Holdridge
	 *
	 */
	private class EventAdapter extends ArrayAdapter<EventListing> {
		
		private ArrayList<EventListing> eventList;

		public EventAdapter(Context context, int textViewResourceId,
				ArrayList<EventListing> eventList) {
			super(context, textViewResourceId, eventList);
			this.eventList = eventList;
		}
		
		/* (non-Javadoc)
		 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.row, null);
			}
			EventListing evt = eventList.get(position);
			v.setId(position);
			//Log.d("SET_ROW_ID", Integer.toString(v.getId()));
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
