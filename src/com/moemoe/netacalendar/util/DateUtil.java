package com.moemoe.netacalendar.util;

/**
 * DateUtil Description: 日期工具类
 * 
 * @author Lpzahd
 * @date 2015-11-21 上午10:16:51
 */
public class DateUtil {

	/**
	 * 获取两年之间月份总和
	 * 
	 * @param sYear
	 * @param eYear
	 * @return
	 */
	public static int getMonthOfYears(int sYear, int eYear) {
		return (eYear - sYear) * 12;
	}

	/**
	 * 获取两年之间一共的星期数
	 * 
	 * @param sYear
	 * @param eYear
	 * @return
	 */
	public static int getWeekOfYears(int sYear, int eYear) {
		int days = (eYear - sYear) * 365;
		for (int i = sYear; i < eYear; i++) {
			if (isLeepYear(i)) {
				days++;
			}
		}

		//
		ModelCalendarUtil mCalUtil;
		mCalUtil = new ModelCalendarUtil(sYear, 0);
		int sDayWeek = mCalUtil.getFirstDayWeek();
		// 条件重复
		// mCalUtil = new ModelCalendarUtil(eYear, 0);
		// int eDayWeek = mCalUtil.getFirstDayWeek();

		int extraWeek = ((days + sDayWeek - 1) % 7 != 0) ? 1 : 0;
		return (days + sDayWeek - 1) / 7 + extraWeek - 1;
	}

	/**
	 * 判断该年份是不是闰年
	 * 
	 * @param year
	 * @return
	 */
	public static boolean isLeepYear(int year) {
		boolean isLeep = false;
		if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) {
			isLeep = true;
		}
		return isLeep;
	}

	/**
	 * 获取该年份的天数
	 * 
	 * @param year
	 * @return
	 */
	public static int getDaysInYear(int year) {
		int days;
		if (isLeepYear(year)) {
			days = 366;
		} else {
			days = 365;
		}
		return days;
	}

}
