package com.moemoe.netacalendar.adapter.helper;

/**
 * MonthCalendarAdapterHelper Description: ��� position of month calendar
 * viewpager �İ�����
 * 
 * @author Lpzahd
 * @date 2015-11-21 ����9:33:18
 */
public class MonthCalendarAdapterHelper extends CalendarAdapterHelper {

	/**
	 * ��ȡ���·����� viewpager �е�ҳ��
	 * 
	 * @param year
	 *            ����
	 * @param month
	 *            ����
	 * @return
	 */
	public int getMonthPosition(int year, int month) {
		return (year - STARTYEAR) * 12 + month;
	}

	/**
	 * ��ȡ��������(����)
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
	 * ��ȡ����(����) ���·��Ѿ�ת����
	 * 
	 * @param position
	 * @return
	 */
	public String getDateStrByPosition(int position) {
		int[] date = getDateByPosition(position);
		return date[0] + "��" + (date[1] + 1) + "��";
	}
}
