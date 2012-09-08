package com.sachachua.quantified.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;


public class RecordProvider extends ContentProvider {
	private DatabaseHandler dbHandler;
	private static final String AUTHORITY = "com.sachachua.quantified.data.RecordProvider";
	public static final int RECORDS = 100;
	public static final int RECORD_ID = 101;
	
	private static final String RECORDS_BASE_PATH = "records";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + RECORDS_BASE_PATH);
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/record";
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/record";
		
	private static final UriMatcher sURIMatcher = new UriMatcher(
	        UriMatcher.NO_MATCH);
	static {
	    sURIMatcher.addURI(AUTHORITY, RECORDS_BASE_PATH, RECORDS);
	    sURIMatcher.addURI(AUTHORITY, RECORDS_BASE_PATH + "/#", RECORD_ID);
	}
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		dbHandler = new DatabaseHandler(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(DatabaseHandler.TABLE_RECORDS); 
		int uriType = sURIMatcher.match(uri);
		switch (uriType) { 
			case RECORD_ID:
				queryBuilder.appendWhere(DatabaseHandler.KEY_ID + "=" + uri.getLastPathSegment());
				break;
			case RECORDS:
				// no filter
				break;
			default:
				throw new IllegalArgumentException("Unknown URI");
		}
		Cursor cursor = queryBuilder.query(dbHandler.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
}
