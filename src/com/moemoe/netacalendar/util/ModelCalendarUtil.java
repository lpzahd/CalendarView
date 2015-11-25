package com.moemoe.netacalendar.util;

import java.util.Calendar;

/**
 * ModelCalendarUtil Description: ģ�黯calendar ���࣬��װ��һЩ���õķ���
 * 
 * @author Lpzahd
 * @date 2015-11-21 ����10:16:10
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
	 * ��ʼ�� ��������Ϊ�³�
	 */
	private void initCalendarUtil() {
		mCalendar.set(mYear, mMonth, 1);
	}

	/**
	 * ���µ�һ�������ڼ�
	 * 
	 * @return
	 */
	public int getFirstDayWeek() {
		int firstDayWeek;
		firstDayWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
		return firstDayWeek;
	}

	/**
	 * ĳ�µ�һ�������ڼ�
	 * 
	 * @param addMonth
	 *            �����·�
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
	 * �������һ�������ڼ�
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
	 * ĳ�����һ�������ڼ�
	 * 
	 * @param addMonth
	 *            �����·�
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
	 * ����ĳ�������ڼ�
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
	 * ��������
	 * 
	 * @return
	 */
	public int getDays() {
		int days;
		days = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		return days;
	}

	/**
	 * ĳ������
	 * 
	 * @param addMonth
	 *            �����·�
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
	 * �ڼ���
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
	 * ĳ�µڼ���
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
	 * ����ĵڼ���
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
	 * ĳ��ĵڼ���
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
	 * ���깲�ж�����
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
	 * ĳ�깲�ж�����
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
