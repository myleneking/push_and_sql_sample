package com.example.pushandsql.model;

import java.util.ArrayList;
import java.util.List;

import com.example.pushandsql.MySQLiteHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class MessagesDataSource {
	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_NAME };

	public MessagesDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Message createMessage(String message) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_NAME, message);
		long insertId = database.insert(MySQLiteHelper.TABLE_NAME, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_NAME,
				allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		Message newMessage = cursorToMessage(cursor);
		cursor.close();
		return newMessage;
	}

	public void deleteMessage(Message message) {
		long id = message.getId();
		System.out.println("Message deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_NAME, MySQLiteHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public List<Message> getAllMessages() {
		List<Message> messages = new ArrayList<Message>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_NAME,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Message message = cursorToMessage(cursor);
			messages.add(message);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return messages;
	}

	private Message cursorToMessage(Cursor cursor) {
		Message message = new Message();
		message.setId(cursor.getLong(0));
		message.setMessage(cursor.getString(1));
		return message;
	}
}
