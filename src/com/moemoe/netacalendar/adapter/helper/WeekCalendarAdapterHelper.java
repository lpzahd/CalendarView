package com.moemoe.netacalendar.adapter.helper;

import com.moemoe.netacalendar.util.DateUtil;
import com.moemoe.netacalendar.util.ModelCalendarUtil;

/**
 * WeekCalendarAdapterHelper Description: ��� position of week calendar viewpager
 * �İ�����
 * 
 * @author Lpzahd
 * @date 2015-11-21 ����9:30:00
 */
public class WeekCalendarAdapterHelper extends CalendarAdapterHelper {

	/**
	 * ��ȡ���������� viewpager �е�ҳ��
	 * 
	 * @param year
	 *            ����
	 * @param month
	 *            ����
	 * @param day
	 *            ����
	 * @return
	 */
	public int getWeekPosition(int year, int month, int day) {
		int days = (year - STARTYEAR) * 365;
		for (int i = STARTYEAR; i < year; i++) {
			if (DateUtil.isLeepYear(i)) {
				days++;
			}
		}

		ModelCalendarUtil mCalUtil;
		mCalUtil = new ModelCalendarUtil(STARTYEAR, 0);
		int sDayWeek = mCalUtil.getFirstDayWeek();

		for (int i = 0; i < month; i++) {
			mCalUtil = new ModelCalendarUtil(year, i);
			days += mCalUtil.getDays();
		}

		days += day;

		mCalUtil = new ModelCalendarUtil(year, month);
		int dayWeek = mCalUtil.getDayWeek(day);

		days += 6 - dayWeek + sDayWeek;
		return days / 7 - 1;

		// int extraWeek = ((days + sDayWeek - 1) % 7 != 0 ) ? 1 : 0;
		// return (days + sDayWeek - 1) / 7 + extraWeek - 1;
	}

	/**
	 * ��ȡ��������(������)
	 * 
	 * @param position
	 * @return
	 */
	public int[] getDateByPosition(int position) {
		ModelCalendarUtil mCalUtil;
		mCalUtil = new ModelCalendarUtil(STARTYEAR, 0);
		int sDayWeek = mCalUtil.getFirstDayWeek();
		int days = position * 7 + 8 - sDayWeek;
		int addYear = 0;

		int tDays;
		tDays = DateUtil.getDaysInYear(STARTYEAR + addYear);
		while (days > tDays) {
			days -= tDays;
			addYear++;
			tDays = DateUtil.getDaysInYear(STARTYEAR + addYear);
		}
		int addMonth = 0;
		while (days > new ModelCalendarUtil(STARTYEAR + addYear, addMonth)
				.getDays()) {
			days -= new ModelCalendarUtil(STARTYEAR + addYear, addMonth)
					.getDays();
			addMonth++;
		}
		int weekOfMonth = new ModelCalendarUtil(STARTYEAR + addYear, addMonth)
				.getWeekOfMonth(days);

		int[] date = new int[3];
		date[0] = STARTYEAR + addYear;
		date[1] = addMonth;
		date[2] = weekOfMonth;

		return date;
	}

	/**
	 * ��ȡ����(������) ���·��Ѿ�ת����
	 * 
	 * @param position
	 * @return
	 */
	public String getDateStrByPosition(int position) {
		int[] date = getDateByPosition(position);
		return date[0] + "��" + (date[1] + 1) + "��" + "��" + date[2] + "��";
	}
}
