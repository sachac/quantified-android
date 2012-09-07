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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.sachachua.quantified.R;
import com.sachachua.quantified.data.DatabaseHandler;
import com.sachachua.quantified.data.RecordCategoryProvider;

public class TimeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, TextWatcher {
    private ListView mCategories;
    private DatabaseHandler dbHandler;
    private static final int RECORD_CATEGORIES_LOADER = 0x01;
    private SimpleCursorAdapter adapter;
	private EditText mEditQuickTrack;
	private Object mCurFilter;
    
    public TimeFragment(DatabaseHandler dbHandler) {
    	this.dbHandler = dbHandler;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
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
}