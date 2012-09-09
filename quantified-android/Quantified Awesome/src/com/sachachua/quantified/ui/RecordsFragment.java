package com.sachachua.quantified.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.sachachua.quantified.R;
import com.sachachua.quantified.data.DatabaseHandler;

public class RecordsFragment extends Fragment implements OnClickListener {
	private OnRecordSelectedListener mRecordSelectedListener;
    
	@Override
	public void onAttach(Activity activity) {
    	 super.onAttach(activity);
         try {
             mRecordSelectedListener = (OnRecordSelectedListener) activity;
         } catch (ClassCastException e) {
             throw new ClassCastException(activity.toString() + " must implement OnRecordSelectedListener");
         }
	}

	private TableLayout mTable;
	private DatabaseHandler dbHandler;

	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		dbHandler = new DatabaseHandler(getActivity());
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.records_fragment, container, false);
    	mTable = (TableLayout) view.findViewById(R.id.records);
    	TableRow inflateRow = (TableRow) View.inflate(getActivity(), R.layout.record_item_layout, null);
    	mTable.addView(inflateRow);
    	
    	Cursor cur = dbHandler.getRecordsWithCategoryDetailAsCursor();
    	int i = 0;
    	if (cur.moveToFirst()) {
			do {
				inflateRow = (TableRow) View.inflate(getActivity(), R.layout.record_item_layout, null);
	    		inflateRow.setTag(cur.getInt(0));
	    		inflateRow.setOnClickListener(this);
	    		Calendar cal = Calendar.getInstance();
	    		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
	    		df.setTimeZone(cal.getTimeZone());
	    		
	    		TextView start = (TextView) inflateRow.findViewById(R.id.start);
	    		start.setText(df.format(new Date(cur.getLong(1))));
	    		
	    		TextView end = (TextView) inflateRow.findViewById(R.id.end);
	    		if (cur.isNull(2)) {
	    			end.setText("");
	    		} else {
	    			end.setText(df.format(new Date(cur.getLong(2))));
	    		}
	    		
	    		TextView entry = (TextView) inflateRow.findViewById(R.id.entry);
	    		entry.setText(cur.getString(3));
	    		mTable.addView(inflateRow);
	    	} while (cur.moveToNext());
		}
    	return view;
    }

    public void onClick(View v) {
		TableRow row = null;
		// TODO Auto-generated method stub
		if (v instanceof TextView) {
			row = (TableRow) v.getParent();
		} else if (v instanceof TableRow) {
			row = (TableRow) v;
		}
		Integer tag = (Integer) row.getTag();
		if (tag != null) {
			int id = tag.intValue();
			mRecordSelectedListener.onRecordSelected(id);
		}
	}
}
