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
import com.moemoe.netacalendar.adapter.helper.MonthCalendarAdapterHelper;
import com.moemoe.netacalendar.listener.CalendarClickListener;
import com.moemoe.netacalendar.listener.OnCalendarClickListener;
import com.moemoe.netacalendar.util.DateManager;
import com.moemoe.netacalendar.util.DateUtil;
import com.moemoe.netacalendar.util.ModelCalendarUtil;
import com.moemoe.netacalendardemo.R;

/**
 * MonthCalendarAdapter Description: adapter of viewpager who contain of month
 * calendar
 * 
 * @author Lpzahd
 * @date 2015-11-21 上午9:18:14
 */
public class MonthCalendarAdapter extends PagerAdapter {

	public static int VIEW_LENGTH = 5;

	public Context mContext;
	public DateManager mDateManager;
	public LinearLayout[] mCalendarViews;

	private MonthCalendarAdapterHelper mHelper;

	public TextView mSelectedView;
	public int mSelcetedDay;
	public int mSelectedRow;

	/**
	 * 为了解决一个奇怪的问题，当textview 的width 设置成 match-parent 后，settext 就无效了
	 * 原来wrap-contant 是没有任何问题的 用post方法可以解决~
	 */
	private Handler mHandler = new Handler();

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
	public MonthCalendarAdapterHelper getHelper() {
		return mHelper;
	}

	public MonthCalendarAdapter(Context context, DateManager dateManager) {
		mContext = context;
		mDateManager = dateManager;
		mHelper = new MonthCalendarAdapterHelper();

		initMonthViews();
		initData();
	}

	private void initMonthViews() {
		mCalendarViews = new LinearLayout[VIEW_LENGTH];
		for (int i = 0; i < VIEW_LENGTH; i++) {
			mCalendarViews[i] = (LinearLayout) View.inflate(mContext,
					R.layout.item_month, null);
		}
	}

	private void initData() {
		Calendar calendar = Calendar.getInstance();
		int mCurrYear = calendar.get(Calendar.YEAR);
		int mCurrMonth = calendar.get(Calendar.MONTH);
		int mCurrDay = calendar.get(Calendar.DAY_OF_MONTH);

		mSelcetedDay = mCurrDay;
		mSelectedRow = new ModelCalendarUtil(mCurrYear, mCurrMonth)
				.getWeekOfMonth(mCurrDay);
	}

	/**
	 * fill date to calendar view and init calendar click event
	 * 
	 * @param monthCalendar
	 * @param year
	 * @param month
	 */
	private void initMonthCalendarDate(LinearLayout monthCalendar, int year,
			int month) {
		int[][] monthDays = mDateManager.getMonthDaysAtEast(year, month);

		int[] date = new int[] { year, month };
		mDateManager.regroupYM(date);
		ModelCalendarUtil mCalUtil = new ModelCalendarUtil(date[0], date[1]);
		int firstDayWeek = mCalUtil.getFirstDayWeek();
		int days = mCalUtil.getDays();

		for (int i = 0; i < DateManager.TOTAL_ROW; i++) {
			ViewGroup parent = (ViewGroup) monthCalendar.getChildAt(i);
			for (int j = 0; j < DateManager.TOTAL_COL; j++) {
				ViewGroup child = (ViewGroup) parent.getChildAt(j);
				((TextView) child.getChildAt(0)).setText("" + monthDays[i][j]);
				((TextView) child.getChildAt(1)).setText("");

				int position = i * DateManager.TOTAL_COL + j;
				CalendarClickListener listener = new MonthCalendarClickListener(
						monthDays[i][j], position);

				int[] rightDate;
				if (position < firstDayWeek - 1) {
					rightDate = new int[] { year, month - 1 };
					mDateManager.regroupYM(rightDate);
					listener.setFlag(CalendarClickListener.PREMONTH);
				} else if (position >= firstDayWeek + days - 1) {
					rightDate = new int[] { year, month + 1 };
					mDateManager.regroupYM(rightDate);
					listener.setFlag(CalendarClickListener.AFTMONTH);
				} else {
					rightDate = new int[] { year, month };
					mDateManager.regroupYM(rightDate);
					listener.setFlag(CalendarClickListener.CURRMONTH);
				}
				listener.setDate(rightDate[0], rightDate[1]);
				child.setOnClickListener(listener);
			}
		}
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
	 * 获取日期(年月日)
	 * 
	 * @param position
	 */
	public int[] getDate(int position) {
		int[] date = new int[3];
		int[] tDate = mHelper.getDateByPosition(position);

		date[0] = tDate[0];
		date[1] = tDate[1];
		date[2] = mSelcetedDay;

		return date;
	}

	/**
	 * 设置选中的日期
	 * 
	 * @param year
	 * @param month
	 * @param day
	 */
	public void setSelectView(int year, int month, int day) {
		mSelcetedDay = day;

		int monthPosition = mHelper.getMonthPosition(year, month);
		ModelCalendarUtil mCalUtil = new ModelCalendarUtil(year, month);
		mSelectedRow = mCalUtil.getWeekOfMonth(day) - 1;

		int firstDayWeek = mCalUtil.getFirstDayWeek();
		int calPosition = mSelcetedDay + firstDayWeek - 2;
		int row = calPosition / 7;
		int col = calPosition % 7;
		TextView selectView = (TextView) ((ViewGroup) ((ViewGroup) mCalendarViews[monthPosition
				% mCalendarViews.length].getChildAt(row)).getChildAt(col))
				.getChildAt(0);
		setClickViewSyle(selectView);
		mSelectedView = selectView;
	}

	/**
	 * 选中标记的日期
	 * 
	 * @param position
	 *            page of viewpager
	 */
	public void selectMarkView(int position) {
		int[] date = mHelper.getDateByPosition(position);
		ModelCalendarUtil mCalUtil = new ModelCalendarUtil(date[0], date[1]);
		int days = mCalUtil.getDays();
		if (days < mSelcetedDay) {
			mSelcetedDay = days;
		}
		setSelectView(date[0], date[1], mSelcetedDay);
	}

	@Override
	public int getCount() {
		return DateUtil.getMonthOfYears(CalendarAdapterHelper.STARTYEAR,
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
				initMonthCalendarDate(mCalendarViews[position
						% mCalendarViews.length],
						CalendarAdapterHelper.STARTYEAR, position);
			}

		});

		((ViewPager) container).addView(mCalendarViews[position
				% mCalendarViews.length]);
		return mCalendarViews[position % mCalendarViews.length];
	}

	/**
	 * MonthCalendarClickListener Description: calendar view click listener
	 * 
	 * @author Lpzahd
	 * @date 2015-11-21 上午9:23:25
	 */
	class MonthCalendarClickListener extends CalendarClickListener {

		public MonthCalendarClickListener(int day, int position) {
			super(day, position);
		}

		@Override
		public void onClick(View v) {
			int flag = getFlag();
			if (flag == PREMONTH) {

			} else if (flag == CURRMONTH) {
				setSelectView(getYear(), getMonth(), getDay());
			} else if (flag == AFTMONTH) {

			}

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
