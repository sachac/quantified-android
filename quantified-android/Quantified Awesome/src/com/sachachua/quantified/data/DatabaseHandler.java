package com.sachachua.quantified.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {
	private static final String TAG = "QA DatabaseHandler";

	private static final int DATABASE_VERSION = 3;
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

	public static final String TABLE_RECORDS = "records";
	// ID
	public static final String KEY_SOURCE = "source";
	public static final String KEY_SOURCE_ID = "id";
	public static final String KEY_TIMESTAMP = "timestamp";
	public static final String KEY_RECORD_CATEGORY_ID = "record_category_id";
	// DATA
	public static final String KEY_END_TIMESTAMP = "end_timestamp";
	public static final String KEY_DURATION = "duration";
	public static final String KEY_CREATED_AT = "created_at";
	public static final String KEY_UPDATED_AT = "updated_at";
	public static final String KEY_DATE = "date";
	public static final String KEY_MANUAL = "manual";
	public static final String KEY_SYNCED = "synced";
	public static final String[] COLUMNS_RECORDS = {KEY_ID, KEY_SOURCE, KEY_SOURCE_ID, 
		KEY_TIMESTAMP, KEY_RECORD_CATEGORY_ID, KEY_DATA, KEY_END_TIMESTAMP, KEY_DURATION,
		KEY_CREATED_AT, KEY_UPDATED_AT, KEY_DATE, KEY_MANUAL, KEY_SYNCED};

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	private static String CREATE_RECORD_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_RECORD_CATEGORIES + "("
			+ KEY_ID + " INTEGER PRIMARY KEY,"
			+ KEY_NAME + " TEXT,"
			+ KEY_PARENT_ID + " INTEGER,"
			+ KEY_DOTTED_IDS + " TEXT,"
			+ KEY_CATEGORY_TYPE + " TEXT,"
			+ KEY_DATA + " TEXT,"
			+ KEY_FULL_NAME + " TEXT,"
			+ KEY_COLOR + " TEXT"
			+ ");";
	private static String CREATE_RECORDS_TABLE = "CREATE TABLE " + TABLE_RECORDS + "("
			+ KEY_ID + " INTEGER PRIMARY KEY," 
			+ KEY_SOURCE + " TEXT,"
			+ KEY_SOURCE_ID + " INTEGER,"
			+ KEY_TIMESTAMP + " INTEGER,"
			+ KEY_RECORD_CATEGORY_ID + " INTEGER,"
			+ KEY_DATA + " TEXT," 
			+ KEY_END_TIMESTAMP + " INTEGER,"
			+ KEY_DURATION + " INTEGER,"
			+ KEY_CREATED_AT + " INTEGER," 
			+ KEY_UPDATED_AT + " INTEGER," 
			+ KEY_DATE + " INTEGER,"
			+ KEY_MANUAL + " INTEGER,"
			+ KEY_SYNCED + " INTEGER"
			+ ")";
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_RECORD_CATEGORIES_TABLE);
		db.execSQL(CREATE_RECORDS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion < 3) {
			db.execSQL(CREATE_RECORDS_TABLE);
		}
	}

	public void addRecordCategory(RecordCategory cat) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
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

	public long addRecord(Record r) {
		SQLiteDatabase db = this.getWritableDatabase();
		Log.d(TAG, r.toString());
		ContentValues values = new ContentValues();
		if (r.getId() > 0) {
			values.put(KEY_ID, r.getId());
		}
		values.put(KEY_SOURCE, r.getSource());
		values.put(KEY_SOURCE_ID, r.getSourceId());
		values.put(KEY_TIMESTAMP, r.getTimestamp().getTime());
		values.put(KEY_RECORD_CATEGORY_ID, r.getRecordCategoryId());
		values.put(KEY_DATA, r.getData());
		if (r.getEndTimestamp() != null) {
			values.put(KEY_END_TIMESTAMP, r.getEndTimestamp().getTime());
		}
		values.put(KEY_DURATION, r.getDuration());
		values.put(KEY_CREATED_AT, r.getCreatedAt().getTime()); 
		values.put(KEY_UPDATED_AT, r.getUpdatedAt().getTime()); 
		values.put(KEY_DATE, r.getDate().getTime());
		values.put(KEY_MANUAL, r.isManual() ? 1 : 0);
		if (r.getSynced() != null) {
			values.put(KEY_SYNCED, r.getSynced().getTime());
		}
		long id = db.insert(TABLE_RECORDS, null, values);
		db.close();
		return id;
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

	public Cursor getRecordsWithCategoryDetailAsCursor() {
		Cursor cur = getReadableDatabase().rawQuery(
				"SELECT " + TABLE_RECORDS + "." + KEY_ID + ", " + KEY_TIMESTAMP + ", " + KEY_END_TIMESTAMP + ", " + KEY_FULL_NAME 
				+ " FROM " + TABLE_RECORDS  + " INNER JOIN " + TABLE_RECORD_CATEGORIES 
				+ " ON (" + KEY_RECORD_CATEGORY_ID + "=" + TABLE_RECORD_CATEGORIES + "." + KEY_ID + ") "
				+ " ORDER BY " + KEY_TIMESTAMP + " DESC", null);
		return cur;
	}

	public Record getRecord(long id) {
		Cursor cur = getReadableDatabase().rawQuery(
				"SELECT " 
						+ TABLE_RECORDS + "." + KEY_ID + ", " 
						+ KEY_SOURCE + ", " 
						+ KEY_SOURCE_ID + ", " 
						+ KEY_TIMESTAMP + ", " 
						+ KEY_END_TIMESTAMP + ", "
						+ KEY_RECORD_CATEGORY_ID + ", "
						+ TABLE_RECORDS + "." + KEY_DATA + ", "
						+ KEY_DURATION + ", "
						+ TABLE_RECORDS + "." + KEY_CREATED_AT + ", "
						+ TABLE_RECORDS + "." + KEY_UPDATED_AT + ", "
						+ KEY_DATE + ", "
						+ KEY_MANUAL + ", "
						+ KEY_SYNCED + ", "
						+ KEY_FULL_NAME 
						+ " FROM " + TABLE_RECORDS  + " INNER JOIN " + TABLE_RECORD_CATEGORIES 
						+ " ON (" + KEY_RECORD_CATEGORY_ID + "=" + TABLE_RECORD_CATEGORIES + "." + KEY_ID + ") "
						+ " WHERE " + TABLE_RECORDS + "." + KEY_ID + "=?", new String[] { String.valueOf(id) });
		if (cur.moveToFirst()) {
			Record rec = new Record();
			rec.setId(cur.getInt(0));
			rec.setSource(cur.getString(1));
			rec.setSourceId(cur.getInt(2));
			rec.setTimestamp(new Date(cur.getLong(3)));
			if (!cur.isNull(4) && cur.getLong(4) > 0) {
				rec.setEndTimestamp(new Date(cur.getLong(4)));
			}
			rec.setRecordCategoryId(cur.getInt(5));
			rec.setData(cur.getString(6));
			rec.setDuration(cur.getLong(7));
			rec.setCreatedAt(new Date(cur.getLong(8)));
			rec.setUpdatedAt(new Date(cur.getLong(9)));
			if (!cur.isNull(10) && cur.getLong(10) > 0) {
				rec.setDate(new Date(cur.getLong(10)));
			}
			rec.setManual(cur.getInt(11) > 0);
			if (!cur.isNull(12) && cur.getLong(12) > 0) {
				rec.setSynced(new Date(cur.getLong(12)));
			}
			rec.setRecordCategoryName(cur.getString(13));
			return rec;
		} else {
			return null;
		}
	}

	public void deleteRecord(long id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_RECORDS, KEY_ID + " = ?",
				new String[] { String.valueOf(id) });
		db.close();
	}

	public int updateRecord(Record r) {
		SQLiteDatabase db = this.getWritableDatabase();
		Log.d(TAG, r.toString());
		ContentValues values = new ContentValues();
		values.put(KEY_SOURCE, r.getSource());
		values.put(KEY_SOURCE_ID, r.getSourceId());
		values.put(KEY_TIMESTAMP, r.getTimestamp().getTime());
		values.put(KEY_RECORD_CATEGORY_ID, r.getRecordCategoryId());
		values.put(KEY_DATA, r.getData());
		if (r.getEndTimestamp() != null) {
			values.put(KEY_END_TIMESTAMP, r.getEndTimestamp().getTime());
		}
		values.put(KEY_DURATION, r.getDuration());
		values.put(KEY_CREATED_AT, r.getCreatedAt().getTime()); 
		values.put(KEY_UPDATED_AT, r.getUpdatedAt().getTime()); 
		values.put(KEY_DATE, r.getDate().getTime());
		values.put(KEY_MANUAL, r.isManual() ? 1 : 0);
		if (r.getSynced() != null) {
			values.put(KEY_SYNCED, r.getSynced().getTime());
		}

	    // updating row
	    return db.update(TABLE_RECORDS, values, KEY_ID + " = ?",
	            new String[] { String.valueOf(r.getId()) });
	    
	}
}
