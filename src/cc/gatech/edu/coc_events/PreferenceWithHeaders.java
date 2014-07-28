/**
 * PreferenceWithHeaders
 *
 * The settings menu screen that lets the user toggle notifications
 *
 * @author Inder Dhir
 * @author Dan Holdridge
 * @version 1.0
 *
 */


package cc.gatech.edu.coc_events;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;
import edu.gatech.cc.coc_events.R;
import java.util.List;

public class PreferenceWithHeaders extends PreferenceActivity {

    /* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        Prefs1Fragment prefs1 = new Prefs1Fragment();
        
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, prefs1)
                .commit();
    }

    /**
     * Populate the activity with the top-level headers.
     */

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);
    }
    
    @Override
    public void onHeaderClick(Header header, int position){
    	
    }

    /**
     * This fragment shows the preferences for the first header.
     */
    public static class Prefs1Fragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Make sure default values are applied.  In a real app, you would
            // want this in a shared function that is used to retrieve the
            // SharedPreferences wherever they are needed.
            //PreferenceManager.setDefaultValues(getActivity(),
            //        R.xml.advanced_preferences, false);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.fragmented_preferences);
            
            final CheckBoxPreference notificationPreference = (CheckBoxPreference) this.findPreference("notifications_preference");
    		if (notificationPreference != null) {
    			Log.d("Preferences", "Creating listener");
    	        notificationPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
    	        	public boolean onPreferenceClick(Preference pref){
    	        		//Create alarm
    	        		setUpdateAlarm(getActivity().getBaseContext(), notificationPreference.isChecked() );
    	        		
    	        		return true;
    	        	}
    	        });
    		} else { Log.d("Preferences", "Can't find preference"); }
            
            
        }
        /** Creates a repeating alarm
    	 * @param context
    	 * @param start Creates an alarm if true; else cancels the alarm
    	 */
    	private void setUpdateAlarm(Context context, boolean start) {
    		AlarmManager alarmMgr = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
    		Intent updater = new Intent(context, UpdateReceiver.class);
    		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, 
    				updater, PendingIntent.FLAG_UPDATE_CURRENT);
    		if (start) {	
    			alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 30000, 30000, pendingIntent);
    			Log.d("Alarm", "Repeating alarm has been set");
    		} else {
    			alarmMgr.cancel(pendingIntent);
    			Log.d("Alarm", "Alarm is cancelled");
    		}
    }
	}
}
