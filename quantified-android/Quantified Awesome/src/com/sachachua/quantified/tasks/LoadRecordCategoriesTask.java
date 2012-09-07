package com.sachachua.quantified.tasks;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.AsyncTask;

import com.sachachua.quantified.client.NetworkUtilities;
import com.sachachua.quantified.data.DatabaseHandler;
import com.sachachua.quantified.data.RecordCategory;

public class LoadRecordCategoriesTask extends AsyncTask<Void, Void, Void> {

	private DatabaseHandler dbHandler;
	
	public LoadRecordCategoriesTask(DatabaseHandler dbHandler) {
		this.dbHandler = dbHandler;
	}
	@Override
	protected Void doInBackground(Void... params) {
		// Replace all my record category information with the JSONObject
		// Get http://quantifiedawesome.com/record_categories.json?all=1
		JSONArray json = NetworkUtilities.readJSONArrayfromURL("http://quantifiedawesome.com/record_categories.json?all=1");
		if ((json != null) && (json.length() > 0)) {
			dbHandler.deleteAllRecordCategories();
			for (int i = 0; i < json.length(); i++) {
				try {
					RecordCategory cat = RecordCategory.fromJSON(json.getJSONObject(i));
					dbHandler.addRecordCategory(cat);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
