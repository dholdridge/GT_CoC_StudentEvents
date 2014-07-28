package cc.gatech.edu.coc_events;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import edu.gatech.cc.coc_events.R;
import java.io.*;
import java.util.ArrayList;

/**Service that retrieves remote information, compares it to the stored information, and creates a notification if they don't match
 * @author Dan Holdridge
 *
 */
public class UpdaterService extends Service {
	
	private boolean shouldNotify;
	
	/**
	 * 
	 */
	public UpdaterService() {
		super();
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		Log.wtf("UpdateService", "Service is trying to bind");
		throw new UnsupportedOperationException("Not yet implemented");
		
		
	}
	
	
	/*
	protected void onHandleIntent(Intent intent){
		
		Log.d("UpdateService", "Intent is being handled, starting task");
		new UpdateTask().execute();
		Log.d("UpdateService", "Trying to send notification");
		this.sendNotification(this);
	}
	*/
	
	/* (non-Javadoc)
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate(){
		
		Log.d("UpdateService", "calling onCreate");
	}
	
	/* (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		
		Log.i("UpdateService", "Service is being started");
		new UpdateTask().execute();
		if ((shouldNotify)) {
				Log.d("UpdateService", "Trying to send notification");
				this.sendNotification(this);
		}
		return START_STICKY;
	}
	
	
	
	/** Asynchronous task that retrieves the remote information, and compares it to a stored local copy.
	 * If the copies don't match, display a notification.
	 * @author Dan Holdridge
	 *
	 */
	private class UpdateTask extends AsyncTask<String, Void, Boolean> {
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */
		protected Boolean doInBackground(String... strings){
			Log.d("UpdaterService", "Calling doInBackground within UpdaterTask");
			ArrayList<EventListing> newList, oldList;
			newList = XmlReader.buildList();
			oldList = null;
			try
			{
				FileInputStream fIn = openFileInput("eventlist.ser");
				ObjectInputStream objIn = new ObjectInputStream(fIn);
				oldList = (ArrayList<EventListing>) objIn.readObject();
				fIn.close();
				objIn.close();
			} catch (IOException e) {
				e.printStackTrace();
				Log.e("UpdaterService", e.getMessage() );
				return false;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if ( oldList.get(0).equals(newList.get(0) ) ){
				//List the same; don't send notification
				shouldNotify = false;
			}else {
				shouldNotify=true;
				//sendNotification(getBaseContext());
				try {
					FileOutputStream fOut = openFileOutput("eventlist.ser", Context.MODE_PRIVATE);
					ObjectOutputStream objOut = new ObjectOutputStream(fOut);
					objOut.writeObject(newList);
					objOut.close();
					fOut.close();
					Log.i("UpdaterService", "Serializable list saved");
				} catch (IOException e) {
					e.printStackTrace();
					Log.e("UpdaterService", e.getMessage() );
				}
			}
			return true;
		}
		
		
	}//end private class
	
	/** Creates and displays an Android notification that tells the user an new event has been detected
	 * @param context
	 */
	private void sendNotification(Context context){
		
		Log.d("UpdateService", "Creating notification");
		Intent notifyIntent = new Intent(context, EventListActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notifyIntent, 0);
		
		NotificationManager notifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder mBuilder = 
				new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.icon_buzz)
				.setContentTitle("Event Added")
				.setContentText("A new event has been added")
				.setContentIntent(contentIntent);
		
		notifyMgr.notify(0, mBuilder.build() );
		Log.d("UpdateService", "Notification build");
	}
	
	
}
