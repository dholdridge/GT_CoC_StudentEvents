package com.example.gt_coc_studentevents;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class UpdateReceiver extends BroadcastReceiver {
	public UpdateReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Intent updater = new Intent(context, UpdaterService.class);
		context.startService(updater);
		Log.d("UpdateReceiver", "Receiver is starting the update service");
	}
}
