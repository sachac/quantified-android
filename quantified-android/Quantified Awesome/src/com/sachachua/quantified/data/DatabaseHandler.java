package com.sachachua.quantified.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "quantified";
	public static final String TABLE_RECORD_CATEGORIES = "record_categories";
	
	// Record category column names
	public static final String KEY_ID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_PARENT_ID = "parent_id";
	public static final String KEY_DOTTED_IDS = "dotted_ids"; 
	private static final String KEY_CATEGORY_TYPE = "category_type";
	private static final String KEY_DATA = "data";
	public static final String KEY_FULL_NAME = "full_name";
	private static final String KEY_COLOR = "color";
	private static final String COLUMNS_RECORD_CATEGORIES[] = {KEY_ID, KEY_NAME, KEY_PARENT_ID, KEY_DOTTED_IDS, KEY_CATEGORY_TYPE, KEY_DATA, KEY_FULL_NAME, KEY_COLOR };
	private static final String TAG = "QA DatabaseHandler";
	
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_RECORD_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_RECORD_CATEGORIES + "("
				+ KEY_ID + " INTEGER PRIMARY KEY,"
				+ KEY_NAME + " TEXT,"
				+ KEY_PARENT_ID + " INTEGER,"
				+ KEY_DOTTED_IDS + " TEXT,"
				+ KEY_CATEGORY_TYPE + " TEXT,"
				+ KEY_DATA + " TEXT,"
				+ KEY_FULL_NAME + " TEXT,"
				+ KEY_COLOR + " TEXT"
				+ ");";
		db.execSQL(CREATE_RECORD_CATEGORIES_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORD_CATEGORIES);
		onCreate(db);
	}

	public void addRecordCategory(RecordCategory cat) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		Log.d(TAG, "Added record category: " + cat.getName());
		values.put(KEY_ID, cat.getId());
		values.put(KEY_NAME, cat.getName());
		values.put(KEY_PARENT_ID, cat.getParentID());
		values.put(KEY_DOTTED_IDS, cat.getDottedIDs());
		values.put(KEY_CATEGORY_TYPE, cat.getCategoryType());
		values.put(KEY_DATA, cat.getData());
		values.put(KEY_FULL_NAME, cat.getFullName());
		values.put(KEY_COLOR, cat.getColor());
		db.insert(TABLE_RECORD_CATEGORIES, null, values);
		db.close();
	}
	
	public RecordCategory getRecordCategory(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_RECORD_CATEGORIES, 
				COLUMNS_RECORD_CATEGORIES, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		return RecordCategory.fromCursor(cursor);
	}
	
	public void deleteAllRecordCategories() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_RECORD_CATEGORIES);
	}
	
	public List<RecordCategory> getAllRecordCategories() {
		List<RecordCategory> list = new ArrayList<RecordCategory>();
		String selectQuery = "SELECT * FROM " + TABLE_RECORD_CATEGORIES;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				list.add(RecordCategory.fromCursor(cursor));
			} while (cursor.moveToNext());
		}
		return list;
	}
	
	public Cursor getChildRecordCategoriesAsCursor(int parentId) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cur = db.query(TABLE_RECORD_CATEGORIES, 
				new String[] {KEY_ID, KEY_NAME, KEY_FULL_NAME}, 
				KEY_ID + "=?", 
				new String[] { parentId > 0 ? String.valueOf(parentId) : null },
				null,
				null,
				KEY_NAME + " ASC");
		return cur;
	}
}
