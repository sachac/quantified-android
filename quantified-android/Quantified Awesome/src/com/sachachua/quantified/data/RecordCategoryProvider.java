package com.sachachua.quantified.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class RecordCategoryProvider extends ContentProvider {
	private DatabaseHandler dbHandler;
	private static final String AUTHORITY = "com.sachachua.quantified.data.RecordCategoryProvider";
	public static final int RECORD_CATEGORIES = 100;
	public static final int RECORD_CATEGORY_ID = 101;
	public static final int RECORD_CATEGORIES_TOP = 102;
	public static final int RECORD_CATEGORIES_BY_PARENT = 103;
	public static final int RECORD_CATEGORIES_SEARCH = 104;
	
	private static final String RECORD_CATEGORIES_BASE_PATH = "record_categories";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + RECORD_CATEGORIES_BASE_PATH);
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/record_category";
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/record_category";
		
	private static final UriMatcher sURIMatcher = new UriMatcher(
	        UriMatcher.NO_MATCH);
	static {
	    sURIMatcher.addURI(AUTHORITY, RECORD_CATEGORIES_BASE_PATH, RECORD_CATEGORIES);
	    sURIMatcher.addURI(AUTHORITY, RECORD_CATEGORIES_BASE_PATH + "/#", RECORD_CATEGORY_ID);
	    sURIMatcher.addURI(AUTHORITY, RECORD_CATEGORIES_BASE_PATH + "/parent", RECORD_CATEGORIES_TOP);
	    sURIMatcher.addURI(AUTHORITY, RECORD_CATEGORIES_BASE_PATH + "/search", RECORD_CATEGORIES_SEARCH);
	    sURIMatcher.addURI(AUTHORITY, RECORD_CATEGORIES_BASE_PATH + "/parent/#", RECORD_CATEGORIES_BY_PARENT);
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
		queryBuilder.setTables(DatabaseHandler.TABLE_RECORD_CATEGORIES); 
		int uriType = sURIMatcher.match(uri);
		switch (uriType) { 
			case RECORD_CATEGORY_ID:
				queryBuilder.appendWhere(DatabaseHandler.KEY_ID + "=" + uri.getLastPathSegment());
				break;
			case RECORD_CATEGORIES:
				// no filter
				break;
			case RECORD_CATEGORIES_TOP:
				queryBuilder.appendWhere(DatabaseHandler.KEY_PARENT_ID + " IS NULL");
				break;
			case RECORD_CATEGORIES_BY_PARENT:
				queryBuilder.appendWhere(DatabaseHandler.KEY_PARENT_ID + "=" + uri.getLastPathSegment());
				break;
			case RECORD_CATEGORIES_SEARCH:
				queryBuilder.appendWhere(DatabaseHandler.KEY_FULL_NAME + " LIKE ");
				queryBuilder.appendWhereEscapeString("%" + uri.getLastPathSegment() + "%");
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
