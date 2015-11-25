package com.moemoe.netacalendar.listener;

import android.view.View.OnClickListener;

/**
 * CalendarClickListener Description: super calendar click listener (仅供内部使用)
 * 
 * @author Lpzahd
 * @date 2015-11-21 上午9:57:12
 */
public abstract class CalendarClickListener implements OnClickListener {

	public static final int PREMONTH = 0;
	public static final int CURRMONTH = 1;
	public static final int AFTMONTH = 2;

	private int mYear;
	private int mMonth;
	private int mDay;

	/**
	 * position of click view (this sorting way like gridview's sorting way)
	 */
	private int mPosition;

	private int mFlag = CURRMONTH;

	public CalendarClickListener(int day, int position) {
		mDay = day;
		mPosition = position;
	}

	public CalendarClickListener(int year, int month, int day, int position) {
		mYear = year;
		mMonth = month;
		mDay = day;
		mPosition = position;
	}

	/*
	 * @Override public void onClick(View v) { Log.i("hit", "position : "
	 * +mPosition + " : "+ mYear + "-" + mMonth + "-" + mDay ); }
	 */

	public void setDate(int year, int month) {
		mYear = year;
		mMonth = month;
	}

	public int getYear() {
		return mYear;
	}

	public void setYear(int year) {
		mYear = year;
	}

	public int getMonth() {
		return mMonth;
	}

	public void setMonth(int month) {
		mMonth = month;
	}

	public int getDay() {
		return mDay;
	}

	public void setDay(int day) {
		mDay = day;
	}

	public int getPosition() {
		return mPosition;
	}

	public void setPosition(int position) {
		mPosition = position;
	}

	public int getFlag() {
		return mFlag;
	}

	public void setFlag(int flag) {
		mFlag = flag;
	}
}
