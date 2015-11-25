package com.moemoe.netacalendar.adapter.helper;

/**
 * MonthCalendarAdapterHelper Description: 解决 position of month calendar
 * viewpager 的帮助类
 * 
 * @author Lpzahd
 * @date 2015-11-21 上午9:33:18
 */
public class MonthCalendarAdapterHelper extends CalendarAdapterHelper {

	/**
	 * 获取该月份所在 viewpager 中的页数
	 * 
	 * @param year
	 *            何年
	 * @param month
	 *            何月
	 * @return
	 */
	public int getMonthPosition(int year, int month) {
		return (year - STARTYEAR) * 12 + month;
	}

	/**
	 * 获取日期数组(年月)
	 * 
	 * @param position
	 * @return
	 */
	public int[] getDateByPosition(int position) {
		int addYear = position / 12;
		int addMonth = position % 12;

		int[] date = new int[2];
		date[0] = STARTYEAR + addYear;
		date[1] = addMonth;

		return date;
	}

	/**
	 * 获取日期(年月) （月份已经转换）
	 * 
	 * @param position
	 * @return
	 */
	public String getDateStrByPosition(int position) {
		int[] date = getDateByPosition(position);
		return date[0] + "年" + (date[1] + 1) + "月";
	}
}
