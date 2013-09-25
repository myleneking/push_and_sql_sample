package com.example.pushandsql.receiver;

import java.util.HashMap;

import com.example.pushandsql.GlobalData;
import com.example.pushandsql.activity.MainActivity;
import com.pushbots.push.Pushbots;
import com.example.pushandsql.model.Message;
import com.example.pushandsql.model.MessagesDataSource;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ViewDebug.FlagToString;

public class CustomPushReceiver extends BroadcastReceiver {
	
	private static final String TAG = "CustomPushReceiver";
	private Message mNewMessage = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		
		if (action.equals(Pushbots.MSG_OPENED)) {
			HashMap<?, ?> PushdataOpen = (HashMap<?, ?>) intent.getExtras().get(Pushbots.MSG_OPEN);
			
			if (GlobalData.sMainActivity == null) {
				Intent i = new Intent(context, MainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				Pushbots.getInstance().appContext.startActivity(i);
			}

		} else if (action.equals(Pushbots.MSG_RECEIVE)) {
			HashMap<?, ?> Pushdata = (HashMap<?, ?>) intent.getExtras().get(Pushbots.MSG_RECEIVE);
			Log.w(TAG, "User Received notification with Message: " + Pushdata.get("message"));

			String message = "" + Pushdata.get("message");
			saveMessage(context, message);

			if (GlobalData.sMainActivity != null) {
				GlobalData.sMainActivity.addMessage(mNewMessage);
			}
		}
	}

	private void saveMessage(Context context, String message) {
		MessagesDataSource datasource = new MessagesDataSource(context);
		datasource.open();
		if (message != null) {
			mNewMessage = datasource.createMessage(message);
		}
		datasource.close();
	}
}