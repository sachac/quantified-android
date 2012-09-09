package com.sachachua.quantified.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sachachua.quantified.R;
import com.sachachua.quantified.data.DatabaseHandler;
import com.sachachua.quantified.data.Record;
import com.sachachua.quantified.data.RecordCategory;

public class RecordDetailFragment extends Fragment implements OnClickListener {
	private long mDisplayedId;
	private Record record;
	private DatabaseHandler dbHandler;
	private TextView mEntry;
	private TextView mStart;
	private TextView mEnd;
	private Button mDelete;
	private Button mSave;
	private JSONArray mDataDefinitions;
	private LinearLayout mLayout;
	
	public long getShownId() {
		return mDisplayedId;
	}
	
	private OnRecordSelectedListener mRecordSelectedListener;
	private OnTimeTrackListener mTimeTrackListener;
	private Button mTrack;
	
	@Override
	public void onAttach(Activity activity) {
    	 super.onAttach(activity);
    	 try {
             mRecordSelectedListener = (OnRecordSelectedListener) activity;
         } catch (ClassCastException e) {}
         try {
             mTimeTrackListener = (OnTimeTrackListener) activity;
         } catch (ClassCastException e) {}
	}

	public RecordDetailFragment(long id) {
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
		if (record == null) { getActivity().getSupportFragmentManager().popBackStack(); return view; }
		
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
		mDelete = (Button) view.findViewById(R.id.delete);
		mDelete.setOnClickListener(this);

		mTrack = (Button) view.findViewById(R.id.track);
		mTrack.setOnClickListener(this);
		
		mLayout = (LinearLayout) view.findViewById(R.id.record_detail_fragment);

		// Retrieve the record category to see if we need to display data fields
		RecordCategory cat = dbHandler.getRecordCategory(record.getRecordCategoryId());
		if (cat.getData() != null) {
			try {
				JSONObject dataValues = null;
				// Fill in the stored information
				if (record.getData() != null) {
					dataValues = new JSONObject(record.getData());
				}
				mDataDefinitions = new JSONArray(cat.getData());
				for (int i = 0; i < mDataDefinitions.length(); i++) {
					JSONObject dataField = mDataDefinitions.optJSONObject(i);
					if (dataField != null) {
						// Add the label
						String dataLabel = dataField.getString("label");
						String dataKey = dataField.getString("key");
						TextView text = new TextView(getActivity());
						text.setText(dataLabel);
						mLayout.addView(text);

						EditText input = new EditText(getActivity());
						input.setTag(dataKey);
						mLayout.addView(input);
						if (dataValues != null && dataValues.getString(dataKey) != null) {
							input.setText(dataValues.getString(dataKey));
						}
					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		return view;
	}

	public void saveData() {
		// Set data based on the text fields
		if (mDataDefinitions != null) {
			JSONObject values = new JSONObject();
			for (int i = 0; i < mDataDefinitions.length(); i++) {
				JSONObject dataField = mDataDefinitions.optJSONObject(i);
				if (dataField != null) {
					try {
						String dataKey = dataField.getString("key");
						EditText input = (EditText) mLayout.findViewWithTag(dataKey);
						if (input != null) {
							values.put(dataKey, input.getText().toString());
						}
					} catch (JSONException ex) { ex.printStackTrace(); }
				}
			}
			record.setData(values.toString());
			record.setUpdatedAt(new Date());
			dbHandler.updateRecord(record);
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		saveData();
	}

	@Override
	public void onClick(View v) {
		if (v.equals(mDelete)) {
			dbHandler.deleteRecord(mDisplayedId);
			record = null;
			RecordsFragment fragment = new RecordsFragment();
			getActivity().getSupportFragmentManager().beginTransaction()
			.replace(R.id.container, fragment)
			.commit();
		}
		if (v.equals(mTrack)) {
			mTimeTrackListener.startRecord();
		}
	}
}
