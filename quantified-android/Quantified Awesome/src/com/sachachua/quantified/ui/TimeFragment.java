package com.sachachua.quantified.ui;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.sachachua.quantified.R;
import com.sachachua.quantified.data.DatabaseHandler;
import com.sachachua.quantified.data.Record;
import com.sachachua.quantified.data.RecordCategoryProvider;

public class TimeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, TextWatcher, OnItemClickListener {
    private ListView mCategories;
    private DatabaseHandler dbHandler;
    private static final int RECORD_CATEGORIES_LOADER = 0x01;
	private static final String TAG = "TimeFragment";
    private SimpleCursorAdapter adapter;
	private EditText mEditQuickTrack;
	private Button mTrack;
	private Object mCurFilter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	dbHandler = new DatabaseHandler(getActivity());
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.time_fragment, container, false);
    	// Fill in the record category list
    	mCategories = (ListView) view.findViewById(R.id.categories);
    	String[] uiBindFrom = { DatabaseHandler.KEY_FULL_NAME };
    	int[] uiBindTo = { android.R.id.text1 };
    	getLoaderManager().initLoader(RECORD_CATEGORIES_LOADER, null, this);    	
    	adapter = new SimpleCursorAdapter(getActivity(),
    			android.R.layout.simple_list_item_1,
    			null, uiBindFrom, uiBindTo, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    	mCategories.setAdapter(adapter);
    	mCategories.setOnItemClickListener(this);
    	
    	// Listen for changes
    	mEditQuickTrack = (EditText) view.findViewById(R.id.edit_quick_track);
    	mEditQuickTrack.addTextChangedListener(this);
    	
    	return view;
    }
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { DatabaseHandler.KEY_ID, DatabaseHandler.KEY_FULL_NAME };
		CursorLoader cursorLoader;
		String filter = null;
		String[] filterArgs = null;
		if (mCurFilter != null) {
			filter = DatabaseHandler.KEY_FULL_NAME + " LIKE ?";
			filterArgs = new String[] { "%" + mCurFilter + "%" };
		}
		cursorLoader = new CursorLoader(getActivity(), RecordCategoryProvider.CONTENT_URI, projection, filter, filterArgs, DatabaseHandler.KEY_FULL_NAME);
		return cursorLoader;
	}
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		adapter.swapCursor(cursor);
	}
	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter.swapCursor(null);	
	}
	
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		mCurFilter = !TextUtils.isEmpty(s) ? s : null;
		getLoaderManager().restartLoader(0, null, this);
	}
	@Override
	public void afterTextChanged(Editable s) {
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// Create a record with the current timestamp
		Cursor cursor = (Cursor) parent.getItemAtPosition(position);
		trackCategory(cursor.getInt(0));
	}
	
	public void trackCategory(int recordCategoryId) {
		Record rec = new Record(recordCategoryId);
		Log.d(TAG, "Adding entry for " + recordCategoryId);
		dbHandler.addRecord(rec);
		mEditQuickTrack.setText(null);
	}
	
	public void handleTrack() {
		// Parse the output
		Cursor cursor = (Cursor) mCategories.getItemAtPosition(0);
		trackCategory(cursor.getInt(0));
	}
}