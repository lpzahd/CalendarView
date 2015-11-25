package com.moemoe.netacalendar.util;

import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * DateManager Description: ����������
 * 
 * @author Lpzahd
 * @date 2015-11-21 ����10:17:11
 */
public class DateManager {

	public static final int DATENUMBERS = 42;
	public static final int TOTAL_COL = 7;
	public static final int TOTAL_ROW = 6;

	// ��ʼ��������
	public int mYear;
	public int mMonth;

	/**
	 * ������������
	 */
	public int mMaxNumber = 1;

	public LinkedHashMap<Integer, ModelCalendarUtil> mDateUtilMap = new LinkedHashMap<Integer, ModelCalendarUtil>();
	public LinkedHashMap<Integer, int[][]> mDateMap = new LinkedHashMap<Integer, int[][]>();

	// ��ʱ����
	private int[][] tempDays;
	private int tempYear;
	private int tempMonth;

	// �����Ƿ��Ѿ����� ���д����� mMaxNumber �������ֵ����ʱû�����ó�ʼֵ��isFull û�� ��
	private boolean isFull = false;

	public DateManager() {

	}

	public DateManager(int num) {
		if (num < 1) {
			throw new IllegalArgumentException("num < 1");
		}
		mMaxNumber = num;
	}

	public LinkedHashMap<Integer, int[][]> getDateMap() {
		return mDateMap;
	}

	/**
	 * ����ʱ�����л�ȡdays
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	private int[][] getTempMonthDays(int year, int month) {
		int[][] days;
		if (year == tempYear && month == tempMonth) {
			days = tempDays;
		} else {
			days = null;
		}
		return days;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public int[][] getMonthDays(int year, int month) {
		if (month < 0 || month > 11) {
			throw new IllegalArgumentException("month must be between 0 and 11");
		}
		int[][] days;
		// ���ȴ���ʱ�����л�ȡ
		days = getTempMonthDays(year, month);

		if (days == null) {
			days = getDate(year, month);
			if (days == null) {
				// û�л�ȡ����������ʼ����һ��
				buildMonthLyCalendar(getModelCalendarUtil(year, month));

				// ɾ�������е�һ��Ԫ��
				if (isFull) {
					Iterator<Entry<Integer, int[][]>> iterator = mDateMap
							.entrySet().iterator();
					iterator.next();
					iterator.remove();
				}

				days = getDate(year, month);
			}

			// ������ʱ����
			tempDays = days;
			tempYear = year;
			tempMonth = month;

		}
		return days;
	}

	/**
	 * ������Ļ�ȡ��������
	 * 
	 * @param year
	 *            ����
	 * @param month
	 *            �������·�
	 * @return
	 */
	public int[][] getMonthDaysAtEast(int year, int month) {
		int[] date = new int[] { year, month };
		regroupYM(date);
		return getMonthDays(date[0], date[1]);
	}

	/**
	 * ��ȡ����������
	 * 
	 * @param year
	 *            ����
	 * @param month
	 *            ����
	 * @param day
	 *            ����
	 * @return
	 */
	public int[] getWeekDaysByDay(int year, int month, int day) {
		int[] weekDays;
		int[][] date = getMonthDaysAtEast(year, month);
		ModelCalendarUtil mCalUtil = getModelCalendarUtil(year, month);
		int weekOfMonth = mCalUtil.getWeekOfMonth(day);
		weekDays = date[weekOfMonth - 1];
		return weekDays;
	}

	/**
	 * ������Ļ�ȡ����������
	 * 
	 * @param year
	 *            ����
	 * @param month
	 *            ����
	 * @param day
	 *            ����������
	 * @return
	 */
	public int[] getWeekDaysByDayAtEast(int year, int month, int day) {
		int[] date = new int[] { year, month, day };
		regroupYMD(date);
		return getWeekDaysByDay(date[0], date[1], date[2]);
	}

	/**
	 * ��ȡ����������
	 * 
	 * @param year
	 *            ����
	 * @param month
	 *            ����
	 * @param weekOfMonth
	 *            ����
	 * @return
	 */
	public int[] getWeekDays(int year, int month, int weekOfMonth) {
		if (weekOfMonth > 6 || weekOfMonth < 1) {
			throw new IllegalArgumentException(
					"weekofmonth must be between 1 and 6");
		}

		int[] weekDays;
		int[][] date = getMonthDaysAtEast(year, month);
		weekDays = date[weekOfMonth - 1];
		return weekDays;
	}

	/**
	 * ��ȡdateUtils (��Ҫ������ã���Ҫ���ڴ������������ �޹ػ���û��� new һ���µ�ʵ���ͺ�)
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public ModelCalendarUtil getModelCalendarUtil(int year, int month) {
		ModelCalendarUtil mCalUtil;
		mCalUtil = mDateUtilMap.get(year * 100 + month);
		if (mCalUtil == null) {
			ModelCalendarUtil needDateUtil = new ModelCalendarUtil(year, month);
			mDateUtilMap.put((year * 100 + month), needDateUtil);

			// ɾ�������е�һ��Ԫ��
			if (isFull) {
				Iterator<Entry<Integer, ModelCalendarUtil>> iterator = mDateUtilMap
						.entrySet().iterator();
				iterator.next();
				iterator.remove();
			}

			mCalUtil = needDateUtil;
		}
		return mCalUtil;
	}

	/**
	 * ��ʼ����������
	 * 
	 * @param year
	 * @param month
	 */
	public void initDate() {
		buildDateUtils();
		for (Integer key : mDateUtilMap.keySet()) {
			buildMonthLyCalendar(mDateUtilMap.get(key));
		}

		isFull = true;
	}

	/**
	 * ��ʼ����������
	 * 
	 * @param year
	 * @param month
	 */
	public void initOrderDate(int year, int month) {
		buildDateUtilsByOrder(year, month);
		for (Integer key : mDateUtilMap.keySet()) {
			buildMonthLyCalendar(mDateUtilMap.get(key));
		}

		isFull = true;
	}

	/**
	 * ��ʼ����������
	 * 
	 * @param year
	 * @param month
	 */
	public void initSafeOrderDate(int year, int month) {
		buildSafeDateUtilsByOrder(year, month);
		for (Integer key : mDateUtilMap.keySet()) {
			buildMonthLyCalendar(mDateUtilMap.get(key));
		}

		isFull = true;
	}

	/**
	 * ��ȡ����
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	private int[][] getDate(int year, int month) {
		return mDateMap.get(year * 100 + month);
	}

	/**
	 * �������ڹ�����
	 */
	private void buildDateUtils() {
		int year;
		int month;
		year = Calendar.getInstance().get(Calendar.YEAR);
		month = Calendar.getInstance().get(Calendar.MONTH);
		buildDateUtilsByOrder(year, month);
	}

	/**
	 * ������ͬ���������� (��ȫ����Ϊ2�꣬�������3�꣬�����������·ݻ���)
	 * 
	 * @param year
	 * @param month
	 */
	private void buildDateUtilsByOrder(int year, int month) {
		mYear = year;
		mMonth = month;

		int tYear;
		int tMonth;
		int half;

		half = mMaxNumber / 2;

		for (int i = 0; i < mMaxNumber; i++) {
			tYear = year;
			tMonth = month + i - half;
			if (tMonth < 0) {
				tYear = year - 1;
				tMonth = 12 + tMonth;
			} else if (tMonth > 11) {
				tYear = year + 1;
				tMonth = tMonth - 12;
			}
			mDateUtilMap.put((tYear * 100 + tMonth), new ModelCalendarUtil(
					tYear, tMonth));
		}
	}

	/**
	 * ������ͬ����������
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	private void buildSafeDateUtilsByOrder(int year, int month) {
		mYear = year;
		mMonth = month;

		int yearCount;
		int tYear;
		int tMonth;
		int half;

		yearCount = mMaxNumber / 12;
		half = mMaxNumber / 2;

		for (int i = 0; i < mMaxNumber; i++) {
			int tempCount;
			tempCount = yearCount;
			tYear = year;
			tMonth = month + i - half;
			while (--tempCount > 0) {
				if (tMonth < 0) {
					tYear = tYear - 1;
					tMonth = 12 + tMonth;
				} else if (tMonth > 11) {
					tYear = tYear + 1;
					tMonth = tMonth - 12;
				} else {

				}
			}
			mDateUtilMap.put((tYear * 100 + tMonth), new ModelCalendarUtil(
					tYear, tMonth));
		}
	}

	/**
	 * ������������
	 * 
	 * @param mCalUtil
	 */
	private void buildMonthLyCalendar(ModelCalendarUtil mCalUtil) {
		int year = mCalUtil.getYear();
		int month = mCalUtil.getMonth();
		int currMonthDays = mCalUtil.getDays();
		int firstDayWeek = mCalUtil.getFirstDayWeek();
		int preMonthDays = mCalUtil.getDays(-1);

		int[] dates = new int[DATENUMBERS];

		// ��ʼ��֯�ϸ��·ݵ�����
		int preMonth = firstDayWeek - 1;
		for (int i = 0; i < preMonth; i++) {
			dates[i] = preMonthDays + 1 + i - preMonth;
		}

		// ��ʼ��֯���·ݵ�����
		int day = 1;
		for (int i = preMonth; i < preMonth + currMonthDays; i++) {
			dates[i] = day++;
		}

		day = 1;
		// ��ʼ��֯�¸��·ݵ�����
		for (int i = preMonth + currMonthDays; i < DATENUMBERS; i++) {
			dates[i] = day++;
		}

		// ת���ɶ�������
		int[][] days = new int[6][7];
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				days[i][j] = dates[i * 7 + j];
			}
		}
		mDateMap.put((year * 100 + month), days);
	}

	/**
	 * ����ɺ��ʵ����� date[year, month]
	 * 
	 * @param date
	 * @return
	 */
	public void regroupYM(int[] date) {
		if (date[1] > 11) {
			while ((date[1] / 12) != 0) {
				date[1] -= 12;
				date[0]++;
			}
		} else if (date[1] < 0) {
			while (date[1] < 0) {
				date[1] += 12;
				date[0]--;
			}
		}
	}

	/**
	 * ����ɺ��ʵ������� date[year, month, day] (year and month must be correct && day
	 * must be greater than zero) Ordinary people are unable to understand this
	 * rules, haha haha hahahahahaha...
	 * 
	 * @param date
	 * @return
	 */
	public void regroupYMD(int[] date) {
		//
		ModelCalendarUtil mCalUtil = new ModelCalendarUtil(date[0], date[1]);
		// int dayOfYear = dateUtil.getDayOfYear();
		int daysInYear = mCalUtil.getDaysInYear();

		int[] shortDate = new int[] { date[0], date[1] };

		if (date[2] /* + dayOfYear */< daysInYear) {
			// û�г���һ��
			if (date[2] < mCalUtil.getDays()) {
				// û�г�������
			} else {
				int tempDays = date[2];
				int addMonth = 0;
				int monthDays = mCalUtil.getDays(addMonth);
				while (tempDays > monthDays) {
					tempDays -= monthDays;
					addMonth++;
					monthDays = mCalUtil.getDays(addMonth);
				}
				date[2] = tempDays;
				date[1] += addMonth;
				shortDate[1] = date[1];
				regroupYM(shortDate);

				date[0] = shortDate[0];
				date[1] = shortDate[1];
			}

		} else {
			int tempDays = date[2];
			int addYear = 0;
			while (tempDays > mCalUtil.getDaysInYear(addYear)) {
				tempDays -= mCalUtil.getDaysInYear(addYear);
				addYear++;
			}
			date[0] += addYear;
			shortDate[0] = date[0];
			date[2] = tempDays;

			if (date[2] < mCalUtil.getDays()) {
				// û�г�������
			} else {
				int addMonth = 0;
				mCalUtil = new ModelCalendarUtil(date[0], 0);
				int monthDays = mCalUtil.getDays(addMonth);
				while (tempDays > monthDays) {
					tempDays -= monthDays;
					addMonth++;
					monthDays = mCalUtil.getDays(addMonth);
				}
				date[2] = tempDays;
				date[1] += addMonth;
				shortDate[1] = date[1];
				regroupYM(shortDate);
				date[0] = shortDate[0];
				date[1] = shortDate[1];
			}

		}

	}
}
