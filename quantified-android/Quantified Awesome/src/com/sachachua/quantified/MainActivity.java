package com.sachachua.quantified;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.sachachua.quantified.data.DatabaseHandler;
import com.sachachua.quantified.tasks.LoadRecordCategoriesTask;
import com.sachachua.quantified.ui.OnRecordSelectedListener;
import com.sachachua.quantified.ui.OnTimeTrackListener;
import com.sachachua.quantified.ui.RecordDetailFragment;
import com.sachachua.quantified.ui.RecordsFragment;
import com.sachachua.quantified.ui.TimeFragment;


public class MainActivity extends FragmentActivity implements ActionBar.OnNavigationListener, OnRecordSelectedListener, OnTimeTrackListener {

    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
    private DatabaseHandler dbHandler;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHandler = new DatabaseHandler(this);
        
        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        // Populate the list of categories
        
        // Set up the dropdown list navigation in the action bar.
        actionBar.setListNavigationCallbacks(
                // Specify a SpinnerAdapter to populate the dropdown list.
                new ArrayAdapter(
                        actionBar.getThemedContext(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        new String[]{
                                getString(R.string.title_time),
                                getString(R.string.title_records),
                                getString(R.string.title_clothing),
                        }),
                this);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,
                getActionBar().getSelectedNavigationIndex());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    
    private static final int TIME_POSITION = 0;
    private static final int RECORDS_POSITION = 1;
    
    @Override
    public boolean onNavigationItemSelected(int position, long id) {
        // When the given tab is selected, show the tab contents in the container
    	switch (position) {
    	case TIME_POSITION:
    		startRecord();
    		return true;
    	case RECORDS_POSITION:
    		Fragment recordList = new RecordsFragment();
    		getSupportFragmentManager().beginTransaction()
            .replace(R.id.container, recordList)
            .commit();
    	}
    	return false;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.menu_sync:
    		LoadRecordCategoriesTask task = new LoadRecordCategoriesTask(dbHandler);
    		task.execute();
    		return true;
    	}
    	return false;
    }
    
	@Override
	public void onRecordSelected(long id) {
		RecordDetailFragment fragment = (RecordDetailFragment) getSupportFragmentManager().findFragmentById(R.id.record_details);
        if (fragment == null || fragment.getShownId() != id) {
            // Make new fragment to show this selection.
            fragment = new RecordDetailFragment(id);
        }
		getSupportFragmentManager().beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit();
	}

	@Override
	public void startRecord() {
		Fragment fragment = new TimeFragment();
		getSupportFragmentManager().beginTransaction()
            .replace(R.id.container, fragment)
            .commit();
    }
}
