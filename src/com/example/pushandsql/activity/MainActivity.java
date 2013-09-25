package com.example.pushandsql.activity;

import java.util.List;

import com.example.pushandsql.GlobalData;
import com.example.pushandsql.R;
import com.example.pushandsql.model.Message;
import com.example.pushandsql.model.MessagesDataSource;
import com.example.pushandsql.receiver.CustomPushReceiver;
import com.pushbots.push.Pushbots;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.app.ListActivity;

public class MainActivity extends ListActivity {

	private static String PUSHBOTS_APPLICATION_ID = "5232d2e84deeaecc060033af";
	private static String SENDER_ID = "23986241753";
	private ArrayAdapter<Message> mAdapter;
	private List<Message> mValues;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GlobalData.sMainActivity = this;
		Pushbots.init(this, SENDER_ID, PUSHBOTS_APPLICATION_ID);
		
		// Customize push notification message
		// PBGenerateCustomNotification PBCustom = new
		// PBGenerateCustomNotification();
		// PBCustom.layout = R.layout.notification;
		// PBCustom.titleId = R.id.tvNotifTitle;
		// PBCustom.messageId = R.id.tvNotifMessage;
		// PBCustom.iconId = R.id.ivNotifImage;
		// PBCustom.icondId = R.drawable.icon;
		// Pushbots.getInstance().setNotificationBuilder(PBCustom);
		Pushbots.getInstance().setMsgReceiver(CustomPushReceiver.class);

		setContentView(R.layout.activity_main);
		
		// View Message
//		String message = getIntent().getStringExtra("message");
//		TextView textMessage = (TextView) findViewById(R.id.tvMessage);
//		textMessage.setText(message);		
		
		// View RegID
		String regId = Pushbots.getInstance().regID();
		TextView textRegId = (TextView) findViewById(R.id.tvRegID);
		if (regId != null) {
			textRegId.setText("Registered successfully!");
		} else {
			textRegId.setText("Not connected!");
		}
		
		MessagesDataSource datasource = new MessagesDataSource(this);
		datasource.open();
		mValues = datasource.getAllMessages();
		datasource.close();
		
		mAdapter = new ArrayAdapter<Message>(this, android.R.layout.simple_list_item_1, mValues);
		setListAdapter(mAdapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		GlobalData.sMainActivity = null;
	}
	
	public void addMessage(Message message) {
		mValues.add(message);
		mAdapter.notifyDataSetChanged();
	}
}
