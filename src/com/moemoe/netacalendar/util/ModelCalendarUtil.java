package com.moemoe.netacalendar.util;

import java.util.Calendar;

/**
 * ModelCalendarUtil Description: 模块化calendar 的类，封装了一些常用的方法
 * 
 * @author Lpzahd
 * @date 2015-11-21 上午10:16:10
 */
public class ModelCalendarUtil {

	public final static int SUNDAY = 1;
	public final static int MONDAY = 2;
	public final static int TUESDAY = 3;
	public final static int WEDNESDAY = 4;
	public final static int THURSDAY = 5;
	public final static int FRIDAY = 6;
	public final static int SATURDAY = 7;

	public Calendar mCalendar = Calendar.getInstance();

	public int mYear;
	public int mMonth;

	public ModelCalendarUtil() {
		mYear = mCalendar.get(Calendar.YEAR);
		mMonth = mCalendar.get(Calendar.MONTH);
		initCalendarUtil();
	}

	public ModelCalendarUtil(int year, int month) {
		mYear = year;
		mMonth = month;
		initCalendarUtil();
	}

	/**
	 * 初始化 日历日期为月初
	 */
	private void initCalendarUtil() {
		mCalendar.set(mYear, mMonth, 1);
	}

	/**
	 * 本月第一天是星期几
	 * 
	 * @return
	 */
	public int getFirstDayWeek() {
		int firstDayWeek;
		firstDayWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
		return firstDayWeek;
	}

	/**
	 * 某月第一天是星期几
	 * 
	 * @param addMonth
	 *            增加月份
	 * @return
	 */
	public int getFirstDayWeek(int addMonth) {
		int firstDayWeek;
		if (addMonth != 0) {
			mCalendar.add(Calendar.MONTH, addMonth);
			firstDayWeek = getFirstDayWeek();
			initCalendarUtil();
		} else {
			firstDayWeek = getFirstDayWeek();
		}
		return firstDayWeek;
	}

	/**
	 * 本月最后一天是星期几
	 * 
	 * @return
	 */
	public int getLastDayWeek() {
		int lastDayWeek;
		mCalendar.roll(Calendar.DATE, -1);
		lastDayWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
		initCalendarUtil();
		return lastDayWeek;
	}

	/**
	 * 某月最后一天是星期几
	 * 
	 * @param addMonth
	 *            增加月份
	 * @return
	 */
	public int getLastDayWeek(int addMonth) {
		int lastDayWeek;
		if (addMonth != 0) {
			mCalendar.add(Calendar.MONTH, addMonth);
		}
		lastDayWeek = getLastDayWeek();
		return lastDayWeek;
	}

	/**
	 * 本月某日是星期几
	 * 
	 * @return
	 */
	public int getDayWeek(int day) {
		int dayWeek;
		if (day != 0) {
			mCalendar.set(mYear, mMonth, day);
			// mCalendar.add(Calendar.DAY_OF_MONTH, day);
			dayWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
			initCalendarUtil();
		} else {
			dayWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
		}
		return dayWeek;
	}

	/**
	 * 本月天数
	 * 
	 * @return
	 */
	public int getDays() {
		int days;
		days = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		return days;
	}

	/**
	 * 某月天数
	 * 
	 * @param addMonth
	 *            增加月份
	 * @return
	 */
	public int getDays(int addMonth) {
		int days;
		if (addMonth != 0) {
			mCalendar.add(Calendar.MONTH, addMonth);
			days = getDays();
			initCalendarUtil();
		} else {
			days = getDays();
		}
		return days;
	}

	/**
	 * 第几周
	 * 
	 * @param day
	 * @return
	 */
	public int getWeekOfMonth(int day) {
		int weekOfMonth;
		if (day != 1) {
			mCalendar.set(mYear, mMonth, day);
			weekOfMonth = mCalendar.get(Calendar.WEEK_OF_MONTH);
			initCalendarUtil();
		} else {
			weekOfMonth = 1;
		}
		return weekOfMonth;
	}

	/**
	 * 某月第几周
	 * 
	 * @param addMonth
	 * @param day
	 * @return
	 */
	public int getWeekOfMonth(int addMonth, int day) {
		int weekOfMonth;
		mCalendar.set(mYear, mMonth, day);
		if (addMonth != 0) {
			mCalendar.add(Calendar.MONTH, addMonth);
			weekOfMonth = mCalendar.get(Calendar.WEEK_OF_MONTH);
			initCalendarUtil();
		} else {
			weekOfMonth = getWeekOfMonth(day);
		}
		return weekOfMonth;
	}

	/**
	 * 今年的第几天
	 * 
	 * @param year
	 * @return
	 */
	public int getDayOfYear() {
		int days;
		days = mCalendar.get(Calendar.DAY_OF_YEAR);
		return days;
	}

	/**
	 * 某年的第几天
	 * 
	 * @param addYear
	 * @return
	 */
	public int getDayOfYear(int addYear) {
		int days;
		if (addYear != 0) {
			mCalendar.add(Calendar.YEAR, addYear);
			days = mCalendar.get(Calendar.DAY_OF_YEAR);
			initCalendarUtil();
		} else {
			days = mCalendar.get(Calendar.DAY_OF_YEAR);
		}
		return days;
	}

	/**
	 * 今年共有多少天
	 * 
	 * @return
	 */
	public int getDaysInYear() {
		int days;
		if (mYear % 400 == 0 || (mYear % 4 == 0 && mYear % 100 != 0)) {
			days = 366;
		} else {
			days = 365;
		}
		return days;
	}

	/**
	 * 某年共有多少天
	 * 
	 * @param addYear
	 * @return
	 */
	public int getDaysInYear(int addYear) {
		int days;
		int tYear = mYear + addYear;
		if (tYear % 400 == 0 || (tYear % 4 == 0 && tYear % 100 != 0)) {
			days = 366;
		} else {
			days = 365;
		}
		return days;
	}

	// get & set
	public int getYear() {
		return mYear;
	}

	public void setYear(int year) {
		this.mYear = year;
	}

	public int getMonth() {
		return mMonth;
	}

	public void setMonth(int month) {
		this.mMonth = month;
	}

	@Override
	public boolean equals(Object o) {
		ModelCalendarUtil other = (ModelCalendarUtil) o;
		if (other.mYear == mYear && other.mMonth == mMonth) {
			return true;
		} else {
			return super.equals(o);
		}
	}

}
