package com.moemoe.netacalendar;

import java.util.ArrayList;
import java.util.Calendar;
import com.moemoe.netacalendar.FControl.ViewTransListener;
import com.moemoe.netacalendar.adapter.MonthCalendarAdapter;
import com.moemoe.netacalendar.adapter.WeekCalendarAdapter;
import com.moemoe.netacalendar.adapter.helper.CalendarAdapterHelper;
import com.moemoe.netacalendar.adapter.helper.MonthCalendarAdapterHelper;
import com.moemoe.netacalendar.adapter.helper.WeekCalendarAdapterHelper;
import com.moemoe.netacalendar.util.DateManager;
import com.moemoe.netacalendardemo.R;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * NetaCalendarActivity Description: 仿mimu日历效果
 * 
 * @author Lpzahd
 * @date 2015-11-21 上午10:15:14
 */
public class NetaCalendarActivity extends Activity {

	private DateManager mDateManager;
	private ViewPager mMonthViewPager;
	private ViewPager mWeekViewPager;

	private MonthCalendarAdapter mMonthAdapter;
	private WeekCalendarAdapter mWeekAdapter;

	private MonthCalendarAdapterHelper mMonthHelper;
	private WeekCalendarAdapterHelper mWeekHelper;

	private ListView mListView;
	private int childHeight;

	private int mCurrYear;
	private int mCurrMonth;
	private int mCurrDay;

	private float calTargetDragY;
	private float lvTargetDragY;

	private TextView mDateTv;

	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);

		Calendar cal = Calendar.getInstance();
		mCurrYear = cal.get(Calendar.YEAR);
		mCurrMonth = cal.get(Calendar.MONTH);
		mCurrDay = cal.get(Calendar.DAY_OF_MONTH);

		mDateTv = (TextView) findViewById(R.id.tv_date);

		initViewPager();
		initViewPagerListener();

		initListView();

		touchControl();

	}

	private void initViewPager() {
		mDateManager = new DateManager(5);
		mDateManager.initDate();

		mMonthViewPager = (ViewPager) findViewById(R.id.vp_month);
		mMonthAdapter = new MonthCalendarAdapter(this, mDateManager);
		mMonthHelper = mMonthAdapter.getHelper();
		mMonthViewPager.setAdapter(mMonthAdapter);

		mWeekViewPager = (ViewPager) findViewById(R.id.vp_week);
		mWeekAdapter = new WeekCalendarAdapter(this, mDateManager);
		mWeekHelper = mWeekAdapter.getHelper();
		mWeekViewPager.setAdapter(mWeekAdapter);

		mMonthViewPager.post(new Runnable() {

			@Override
			public void run() {
				ViewGroup child = (ViewGroup) mMonthViewPager.getChildAt(0);
				childHeight = child.getChildAt(0).getHeight();
				LayoutParams monthParams = mMonthViewPager.getLayoutParams();
				monthParams.height = childHeight * 6;
				mMonthViewPager.setLayoutParams(monthParams);

				LayoutParams weekParams = mWeekViewPager.getLayoutParams();
				weekParams.height = childHeight;
				mWeekViewPager.setLayoutParams(weekParams);

				lvTargetDragY = childHeight * 5;
				calTargetDragY = childHeight * mMonthAdapter.mSelectedRow;

				int weekPosition = mWeekHelper.getWeekPosition(mCurrYear,
						mCurrMonth, mCurrDay);
				mWeekViewPager.setCurrentItem(weekPosition, false);
				mWeekAdapter.setSelectView(mCurrYear, mCurrMonth, mCurrDay);

				int monthPosition = mMonthHelper.getMonthPosition(mCurrYear,
						mCurrMonth);
				mMonthViewPager.setCurrentItem(monthPosition, false);
				mMonthAdapter.setSelectView(mCurrYear, mCurrMonth, mCurrDay);
				mDateTv.setText(mMonthHelper
						.getDateStrByPosition(monthPosition));

			}
		});

	}

	private void initViewPagerListener() {

		mMonthViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				mMonthAdapter.selectMarkView(position);
				if (isMonthScroll) {
					mDateTv.setText(mMonthHelper.getDateStrByPosition(position));
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				isMonthScroll = true;
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});

		mWeekViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				mWeekAdapter.selectMarkView(position);
				if (isWeekScroll) {
					mDateTv.setText(mWeekHelper.getDateStrByPosition(position));
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				isWeekScroll = true;
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		final ViewGroup ll = (ViewGroup) findViewById(R.id.ll_parent);

		ll.post(new Runnable() {

			@Override
			public void run() {
				LayoutParams layoutParams = ll.getLayoutParams();
				int measuredHeight = ll.getMeasuredHeight();
				layoutParams.height = (int) (lvTargetDragY + measuredHeight);
				ll.setLayoutParams(layoutParams);
			}
		});
	}

	private void initListView() {
		mListView = (ListView) findViewById(R.id.list);

		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < 50; i++) {
			list.add("第 " + i + " 条记录");
		}
		mListView.setAdapter(new ArrayAdapter(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, list));

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
		});
	}

	/**
	 * 区分viewpager 是手指滑动 还是 setcurrentitem
	 */
	private boolean isMonthScroll = true;
	private boolean isWeekScroll = true;

	private void touchControl() {
		final FControl fControl = new FControl(mListView, mMonthViewPager);
		fControl.setMainViewTransListener(new ViewTransListener() {
			/**
			 * 确保每个状态一次只运行一次
			 */
			private boolean isRunTop = true;
			private boolean isRunBottom = true;
			private boolean isRunTrans = true;

			@Override
			public void transToDir(boolean dir) {
				if (isRunTrans) {
					isRunTop = true;
					isRunBottom = true;
					isRunTrans = false;

					if (!dir) {
						isMonthScroll = false;
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								hideWeek();
							}
						});
					}
				}
			}

			@Override
			public void isTop() {
				if (isRunTop) {
					isWeekScroll = false;
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							showWeek();
							mDateTv.setText(mWeekHelper
									.getDateStrByPosition(mWeekViewPager
											.getCurrentItem()));
						}
					});
					isRunTop = false;
					isRunBottom = true;
					isRunTrans = true;
				}
			}

			@Override
			public void isBottom() {
				if (isRunBottom) {
					isRunTop = true;
					isRunBottom = false;
					isRunTrans = true;
					mDateTv.setText(mMonthHelper
							.getDateStrByPosition(mMonthViewPager
									.getCurrentItem()));
				}
			}
		});
		mListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return fControl.onTouchEvent(event, lvTargetDragY, childHeight
						* mMonthAdapter.mSelectedRow);
			}
		});
	}

	/**
	 * 滑动到顶部 显示 周视图
	 */
	private void showWeek() {
		int mSelcetedDay = mMonthAdapter.mSelcetedDay;

		int[] date = mMonthHelper.getDateByPosition(mMonthViewPager
				.getCurrentItem());
		int weekPosition = mWeekHelper.getWeekPosition(date[0], date[1],
				mSelcetedDay);
		mWeekAdapter.setSelectView(date[0], date[1], mSelcetedDay);
		mWeekViewPager.setCurrentItem(weekPosition, false);

		if (mWeekViewPager.getVisibility() != View.VISIBLE) {
			mWeekViewPager.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 开始向下滑动时隐藏周视图
	 */
	private void hideWeek() {

		if (mWeekViewPager.getVisibility() != View.INVISIBLE) {
			mWeekViewPager.setVisibility(View.INVISIBLE);
		}

		int[] dates = mWeekAdapter.getSelectedDateByPosition(mWeekViewPager
				.getCurrentItem());

		if (dates[0] >= CalendarAdapterHelper.STARTYEAR) {
			int monthPosition = mMonthHelper.getMonthPosition(dates[0],
					dates[1]);
			mMonthAdapter.setSelectView(dates[0], dates[1], dates[2]);
			mMonthViewPager.setCurrentItem(monthPosition, false);

			calTargetDragY = mMonthAdapter.mSelectedRow * childHeight;
			mMonthViewPager.setTranslationY(-calTargetDragY);
		}
	}

}
