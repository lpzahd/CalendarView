package com.moemoe.netacalendar.adapter;

import java.util.Calendar;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.moemoe.netacalendar.adapter.helper.CalendarAdapterHelper;
import com.moemoe.netacalendar.adapter.helper.WeekCalendarAdapterHelper;
import com.moemoe.netacalendar.listener.CalendarClickListener;
import com.moemoe.netacalendar.listener.OnCalendarClickListener;
import com.moemoe.netacalendar.util.DateManager;
import com.moemoe.netacalendar.util.DateUtil;
import com.moemoe.netacalendar.util.ModelCalendarUtil;
import com.moemoe.netacalendardemo.R;

/**
 * WeekCalendarAdapter Description: adapter of viewpager who contain of week
 * calendar
 * 
 * @author Lpzahd
 * @date 2015-11-21 上午9:35:04
 */
public class WeekCalendarAdapter extends PagerAdapter {

	public static int VIEW_LENGTH = 5;

	public Context mContext;
	public DateManager mDateManager;
	public LinearLayout[] mCalendarViews;

	public TextView mSelectedView;
	public int mSelectedPosition;

	/**
	 * 为了解决一个奇怪的问题，当textview 的width 设置成 match-parent 后，settext 就无效了
	 * 原来wrap-contant 是没有任何问题的 用post方法可以解决~
	 */
	private Handler mHandler = new Handler();

	private WeekCalendarAdapterHelper mHelper;
	private OnCalendarClickListener mOnCalendarClickListener;

	/**
	 * Register a callback to be invoked when this view is clicked. If this view
	 * is not clickable, it becomes clickable.
	 * 
	 * @param listener
	 */
	public void setOncCalendarClickListener(OnCalendarClickListener listener) {
		mOnCalendarClickListener = listener;
	}

	/**
	 * Get this helper who can parse position
	 * 
	 * @return
	 */
	public WeekCalendarAdapterHelper getHelper() {
		return mHelper;
	}

	public WeekCalendarAdapter(Context context, DateManager dateManager) {
		mContext = context;
		mDateManager = dateManager;
		mHelper = new WeekCalendarAdapterHelper();

		initWeekViews();
		initData();

	}

	private void initWeekViews() {
		mCalendarViews = new LinearLayout[VIEW_LENGTH];
		for (int i = 0; i < VIEW_LENGTH; i++) {
			mCalendarViews[i] = (LinearLayout) View.inflate(mContext,
					R.layout.item_week, null);
		}
	}

	private void initData() {
		Calendar calendar = Calendar.getInstance();
		int mCurrYear = calendar.get(Calendar.YEAR);
		int mCurrMonth = calendar.get(Calendar.MONTH);
		int mCurrDay = calendar.get(Calendar.DAY_OF_MONTH);

		setSelectedPosition(mCurrYear, mCurrMonth, mCurrDay);
	}

	/**
	 * set selected position of dayofweek
	 * 
	 * @param year
	 *            何年
	 * @param month
	 *            何月
	 * @param day
	 *            何日
	 */
	public void setSelectedPosition(int year, int month, int day) {
		mSelectedPosition = new ModelCalendarUtil(year, month).getDayWeek(day) - 1;
	}

	/**
	 * set selected position of dayofweek
	 * 
	 * @param dayOfWeek
	 */
	public void setSelectedPosition(int dayOfWeek) {
		mSelectedPosition = dayOfWeek;
	}

	/**
	 * 
	 * @param offsetPosition
	 * @return
	 */
	public int[] getSelectedDateByPosition(int offsetPosition) {
		return getDateByPosition(offsetPosition, mSelectedPosition);
	}

	/**
	 * 获取viewpager那页的选中的日期 获取步骤： 1、根据position of viewpager 计算出经过的天数 获取到
	 * 这一周最后一天的日期 2、根据selectPosition 做减法原运算 获取到选中的日期
	 * 
	 * @param offsetPosition
	 *            position of viewpager
	 * @param selectPosition
	 *            position of dayofweek
	 * @return [年，月，日]
	 */
	public int[] getDateByPosition(int offsetPosition, int selectPosition) {
		ModelCalendarUtil mCalUtil = mDateManager.getModelCalendarUtil(
				CalendarAdapterHelper.STARTYEAR, 0);
		int days = 7 * offsetPosition + 8 - mCalUtil.getFirstDayWeek();
		int[] dates = new int[] { CalendarAdapterHelper.STARTYEAR, 0, days };
		mDateManager.regroupYMD(dates);
		dates[2] = dates[2] + selectPosition - 6;

		if (dates[2] <= 0) {
			int[] shortDates = new int[] { dates[0], dates[1] - 1 };
			mDateManager.regroupYM(shortDates);
			dates[2] = mDateManager.getModelCalendarUtil(shortDates[0],
					shortDates[1]).getDays()
					+ dates[2];
			dates[0] = shortDates[0];
			dates[1] = shortDates[1];
		}
		return dates;
	}

	/**
	 * fill date to calendar view and init calendar click event
	 * 
	 * @param monthCalendar
	 * @param year
	 * @param month
	 */
	private void initWeekCalendarDate(LinearLayout weekCalendar, int year,
			int month, int day) {
		int[] weekDays = mDateManager.getWeekDaysByDayAtEast(year, month, day);

		int[] date = new int[] { year, month, day };
		mDateManager.regroupYMD(date);

		boolean isMultiMonth;
		if (weekDays[0] > weekDays[DateManager.TOTAL_COL - 1]) {
			isMultiMonth = true;
		} else {
			isMultiMonth = false;
		}

		for (int i = 0; i < DateManager.TOTAL_COL; i++) {
			ViewGroup child = (ViewGroup) weekCalendar.getChildAt(i);
			((TextView) child.getChildAt(0)).setText("" + weekDays[i]);
			((TextView) child.getChildAt(1)).setText("");

			CalendarClickListener listener = new WeekCalendarClickListener(
					weekDays[i], i);
			int[] ym;
			if (isMultiMonth) {
				if (date[2] > 15) {
					if (weekDays[i] > 15) {
						ym = new int[] { date[0], date[1] };
						listener.setFlag(CalendarClickListener.CURRMONTH);
					} else {
						ym = new int[] { date[0], date[1] + 1 };
						mDateManager.regroupYM(ym);
						listener.setFlag(CalendarClickListener.AFTMONTH);
					}
				} else {
					if (weekDays[i] < 15) {
						ym = new int[] { date[0], date[1] };
						listener.setFlag(CalendarClickListener.CURRMONTH);
					} else {
						ym = new int[] { date[0], date[1] - 1 };
						mDateManager.regroupYM(ym);
						listener.setFlag(CalendarClickListener.PREMONTH);
					}
				}
			} else {
				ym = new int[] { date[0], date[1] };
				listener.setFlag(CalendarClickListener.CURRMONTH);
			}
			listener.setDate(ym[0], ym[1]);
			child.setOnClickListener(listener);
		}
	}

	/**
	 * 设置选中的日期
	 * 
	 * @param year
	 * @param month
	 * @param day
	 */
	public void setSelectView(int year, int month, int day) {
		setSelectedPosition(year, month, day);

		int weekPosition = mHelper.getWeekPosition(year, month, day);
		TextView selectView = (TextView) ((ViewGroup) mCalendarViews[weekPosition
				% mCalendarViews.length].getChildAt(mSelectedPosition))
				.getChildAt(0);
		setClickViewSyle(selectView);
		mSelectedView = selectView;
	}

	/**
	 * set calendar style
	 * 
	 * @param selectView
	 */
	private void setClickViewSyle(TextView selectView) {
		if (mSelectedView != null) {
			mSelectedView.setTextColor(Color.BLACK);
		}
		selectView.setTextColor(Color.RED);
	}

	/**
	 * 选中标记的日期
	 * 
	 * @param position
	 *            page of viewpager
	 */
	public void selectMarkView(int position) {
		int[] date = getSelectedDateByPosition(position);
		setSelectView(date[0], date[1], date[2]);
	}

	@Override
	public int getCount() {
		return DateUtil.getWeekOfYears(CalendarAdapterHelper.STARTYEAR,
				CalendarAdapterHelper.ENDYEAR);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		if (mCalendarViews[position % mCalendarViews.length].getParent() != null) {
			((ViewPager) container).removeView(mCalendarViews[position
					% mCalendarViews.length]);
		}

		mHandler.post(new Runnable() {

			@Override
			public void run() {
				initWeekCalendarDate(mCalendarViews[position
						% mCalendarViews.length],
						CalendarAdapterHelper.STARTYEAR, 0, 1 + position * 7);
			}
		});

		((ViewPager) container).addView(mCalendarViews[position
				% mCalendarViews.length]);
		return mCalendarViews[position % mCalendarViews.length];
	}

	/**
	 * WeekCalendarClickListener Description: calendar view click listener
	 * 
	 * @author Lpzahd
	 * @date 2015-11-21 上午9:53:43
	 */
	class WeekCalendarClickListener extends CalendarClickListener {

		public WeekCalendarClickListener(int day, int position) {
			super(day, position);
		}

		@Override
		public void onClick(View v) {
			setSelectView(getYear(), getMonth(), getDay());

			if (mOnCalendarClickListener != null) {
				mOnCalendarClickListener.click(v, getYear(), getMonth(),
						getDay());
			}

			Toast.makeText(
					mContext,
					"position : " + getPosition() + " : " + getYear() + "-"
							+ getMonth() + "-" + getDay(), 0).show();

		}

	}
}
