package com.sachachua.quantified.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sachachua.quantified.R;
import com.sachachua.quantified.data.DatabaseHandler;
import com.sachachua.quantified.data.Record;

public class RecordDetailFragment extends Fragment implements OnClickListener {
	private int mDisplayedId;
	private Record record;
	private DatabaseHandler dbHandler;
	private TextView mEntry;
	private TextView mStart;
	private TextView mEnd;
	private ImageButton mDelete;
	
	public int getShownId() {
		return mDisplayedId;
	}
	
	public RecordDetailFragment(int id) {
		this.mDisplayedId = id;
	}
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	dbHandler = new DatabaseHandler(getActivity());
    }
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.record_detail_fragment, container, false);
		if (mDisplayedId > 0 && (this.record == null || this.record.getId() != mDisplayedId)) {
			this.record = dbHandler.getRecord(mDisplayedId);
		}
		if (record != null) {
			mEntry = (TextView) view.findViewById(R.id.entry);
		    mEntry.setText(record.getRecordCategoryName());
		    mStart = (TextView) view.findViewById(R.id.start);
		    Calendar cal = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat();
			df.setTimeZone(cal.getTimeZone());
			mStart.setText(df.format(record.getTimestamp()));
			mEnd = (TextView) view.findViewById(R.id.end);
		    if (record.getEndTimestamp() != null) {
				mEnd.setText(df.format(record.getEndTimestamp()));
			} else {
				mEnd.setText("-");
			}
			mDelete = (ImageButton) view.findViewById(R.id.delete);
			mDelete.setOnClickListener(this);
			mDelete.setTag(Integer.valueOf(mDisplayedId));
		} else {
			getActivity().getSupportFragmentManager().popBackStack();
		}
		return view;
	}

	@Override
	public void onClick(View v) {
		Integer tag = (Integer) v.getTag();
		dbHandler.deleteRecord(tag.intValue());
		RecordsFragment fragment = new RecordsFragment();
		getActivity().getSupportFragmentManager().beginTransaction()
		.replace(R.id.container, fragment)
		.commit();
	}
}
