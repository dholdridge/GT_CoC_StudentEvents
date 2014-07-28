package cc.gatech.edu.coc_events;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/** Launches the UpdateService when a broadcast is received
 * @author Dan Holdridge
 *
 */
public class UpdateReceiver extends BroadcastReceiver {
	public UpdateReceiver() {
	}

	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		
		Intent updater = new Intent(context, UpdaterService.class);
		context.startService(updater);
		Log.e("UpdateReceiver", "Receiver is starting the update service");
	}
}
