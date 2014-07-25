package com.example.gt_coc_studentevents;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class UpdaterService extends Service {
	
	private boolean shouldNotify;
	
	public UpdaterService() {
		super();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		Log.wtf("UpdateService", "Service is trying to bind");
		throw new UnsupportedOperationException("Not yet implemented");
		
		
	}
	
	protected void onHandleIntent(Intent intent){
		
		Log.d("UpdateService", "Intent is being handled, starting task");
		new UpdateTask().execute();
		Log.d("UpdateService", "Trying to send notification");
		this.sendNotification(this);
	}
	
	public void onCreate(){
		
		Log.d("UpdateService", "calling onCreate");
	}
	
	public int onStartCommand(Intent intent, int flags, int startId){
		
		Log.i("UpdateService", "Service is being started");
		new UpdateTask().execute();
		if (shouldNotify) {
				Log.d("UpdateService", "Trying to send notification");
				this.sendNotification(this);
		}
		return START_STICKY;
	}
	
	
	
	private class UpdateTask extends AsyncTask<String, Void, Boolean> {
		
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
				//List the same; dont' send notification
				shouldNotify = false;
			}else {
				shouldNotify=true;
			}
			return true;
		}
		
		
	}//end private class
	
	private void sendNotification(Context context){
		
		Log.d("UpdateService", "Creating notification");
		Intent notifyIntent = new Intent(context, EventListActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notifyIntent, 0);
		
		NotificationManager notifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder mBuilder = 
				new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("Event Added")
				.setContentText("A new event has been added")
				.setContentIntent(contentIntent);
		
		notifyMgr.notify(0, mBuilder.build() );
		Log.d("UpdateService", "Notification build");
	}
	
	
}
