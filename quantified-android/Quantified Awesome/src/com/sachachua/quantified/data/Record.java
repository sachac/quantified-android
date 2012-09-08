package com.sachachua.quantified.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.text.format.DateFormat;

public class Record {
	private int id;
	private String source;
	private int sourceId;
	private Date timestamp;
	private Date endTimestamp;
	private int recordCategoryId;
	private String data;
	private long duration;
	private Date createdAt;
	private Date updatedAt;
	private Date date;
	private boolean manual;
	private Date synced;
	private String recordCategoryName;
	
	public Record() {
	
	}
	
	public Record(int recordCategory) {
		Date now = new Date();
		setSource("android");
		setTimestamp(now);
		setCreatedAt(now);
		setUpdatedAt(now);
		setDate(now);
		setRecordCategoryId(recordCategory);
	}
	

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = new Date(timestamp);
	}
	public boolean isManual() {
		return manual;
	}
	public void setManual(boolean manual) {
		this.manual = manual;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public int getRecordCategoryId() {
		return recordCategoryId;
	}
	public void setRecordCategoryId(int recordCategoryId) {
		this.recordCategoryId = recordCategoryId;
	}
	public Date getEndTimestamp() {
		return endTimestamp;
	}
	public void setEndTimestamp(Date endTimestamp) {
		this.endTimestamp = endTimestamp;
	}
	public int getSourceId() {
		return sourceId;
	}
	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}
	public Date getSynced() {
		return synced;
	}
	public void setSynced(Date synced) {
		this.synced = synced;
	}
	
	public String toString() {
		Date endTime = endTimestamp;
		if (endTime == null) {
			endTime = new Date();
		}
		long difference = (endTime.getTime() - timestamp.getTime()) / 60000;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		df.setTimeZone(cal.getTimeZone());
		String s = df.format(timestamp) + " " + recordCategoryName + " (";		
		long hours = difference / 60;
		long minutes = difference % 60;
		if (hours < 0) {
			s += "0";
		} else {
			s += String.valueOf(hours);
		}
		s += ":";
		if (minutes < 10) {
			s += "0";
		}
		s += String.valueOf(minutes);
		s += ")";
		return s;
	}

	public String getRecordCategoryName() {
		return recordCategoryName;
	}


	public void setRecordCategoryName(String recordCategoryName) {
		this.recordCategoryName = recordCategoryName;
	}
}
