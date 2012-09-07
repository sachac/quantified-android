package com.sachachua.quantified.data;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;

public class RecordCategory {
	private int id;
	private String name;
	private int parentID;
	private String dottedIDs;
	private String categoryType;
	private String data;
	private String fullName;
	private String color;
	
	public RecordCategory() { }

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getParentID() {
		return parentID;
	}

	public void setParentID(int parentID) {
		this.parentID = parentID;
	}

		public String getDottedIDs() {
		return dottedIDs;
	}

	public void setDottedIDs(String dottedIDs) {
		this.dottedIDs = dottedIDs;
	}

	public static RecordCategory fromCursor(Cursor cursor) {
		RecordCategory cat = new RecordCategory();
		cat.setId(cursor.getInt(0));
		cat.setName(cursor.getString(1));
		cat.setParentID(cursor.getInt(2));
		cat.setDottedIDs(cursor.getString(3));
		cat.setCategoryType(cursor.getString(4));
		cat.setData(cursor.getString(5));
		cat.setFullName(cursor.getString(6));
		cat.setColor(cursor.getString(7));
		return cat;
	}
	
	public static RecordCategory fromJSON(JSONObject obj) throws JSONException {
		RecordCategory cat = new RecordCategory();
		cat.setId(obj.getInt("id"));
		cat.setName(obj.getString("name"));
		if (!obj.isNull("parent_id")) {
			cat.setParentID(obj.getInt("parent_id"));
		}
		cat.setDottedIDs(obj.getString("dotted_ids"));
		cat.setCategoryType(obj.getString("category_type"));
		cat.setData(obj.getString("data"));
		cat.setFullName(obj.getString("full_name"));
		cat.setColor(obj.getString("color"));
		return cat;
	}
}
