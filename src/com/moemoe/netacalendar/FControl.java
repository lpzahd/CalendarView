package com.moemoe.netacalendar;

import android.animation.Animator.AnimatorListener;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;

/**
 * FControl
 * Description: 仿MIMU日历滑动(ListView & MonthViewPager)
 * 				同步平移  ( 0 - 各自的目标高度  )
 * @author Lpzahd
 * @date 2015-11-21 上午10:03:25
 */
public class FControl {

	/**
	 * main view (touch view)
	 */
	private AbsListView mMainView;
	
	/**
	 * second view (伴随 main view)
	 */
	private View mSecondView;

	// 最小平移高度~ 平移距离小于这个值，就会返回到原来的状态
	private float mMinTransHeight = 100;

	/**
	 * 动画执行速度
	 */
	private float speed = 2f;
	
	/**
	 * 抖动参数
	 */
	private float jitter = 5f; 
	
	/**
	 * 前一次主view 平移的位置
	 */
	private float preMainViewTransY;
	
	/**
	 * 前一次次view 平移的位置
	 */
	private float preSecViewTransY;
	
	/**
	 * 主view 的目标高度
	 */
	private float mTargetHeight;
	
	/**
	 * 次view 的目标高度
	 */
	private float sTargetHeight;
	
	/**
	 * 手指按下的y 方向的位置
	 */
	private float downY;
	
	/**
	 * 处理平移之前 最后获取到的 位置
	 */
	private float lastY;
	
	/**
	 * 是不是平移
	 * 	(平移和动画是联动的，没有平移就没有动画)
	 */
	private boolean isTrans;
	
	/**
	 * 动画是否执行结束
	 */
	private boolean isAnimEnd = true;
	
	private ObjectAnimator mObjectAnimator;
	private ObjectAnimator sObjectAnimator;
	
	private ViewTransListener mViewListener;
	private ViewTransListener sViewListener;
	
	/**
	 * 主view 滑动状态的监听
	 * @param listener
	 */
	public void setMainViewTransListener(ViewTransListener listener){
		mViewListener = listener;
	}
	
	/**
	 * 次view 滑动状态的监听
	 * @param listener
	 */
	public void setSecondViewTransListener(ViewTransListener listener){
		sViewListener = listener;
		
		sObjectAnimator.addListener(new SimpleAnimatorListener(){
			@Override
			public void onAnimationEnd(Animator animation) {
				if(mSecondView.getTranslationY() == 0){
					sViewListener.isBottom();
				} else {
					sViewListener.isTop();
				}
			}
		});
	}
	
	public FControl(AbsListView mainView, View secondView) {
		mMainView = mainView;
		mSecondView = secondView;
		
		initAnimator();
	}
	
	/**
	 * 初始化动画
	 */
	private void initAnimator(){
		mObjectAnimator = new ObjectAnimator();
		mObjectAnimator.setTarget(mMainView);
		mObjectAnimator.setPropertyName("translationY");
		mObjectAnimator.setInterpolator(null);	// kill default interpolator
		mObjectAnimator.addListener(new SimpleAnimatorListener(){
			
			@Override
			public void onAnimationStart(Animator animation) {
				isAnimEnd = false;
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				isAnimEnd = true;
				if(mViewListener != null){
					if(mMainView.getTranslationY() == 0){
						mViewListener.isBottom();
					} else {
						mViewListener.isTop();
					}
				}
			}
		});
		
		sObjectAnimator = new ObjectAnimator();
		sObjectAnimator.setTarget(mSecondView);
		sObjectAnimator.setPropertyName("translationY");
		sObjectAnimator.setInterpolator(null);
	}
	
	/**
	 * 设置监听动作
	 * @param v
	 * @param downTransY
	 * @param targetHeight
	 * @param listener
	 */
	private void doListener(View v, float downTransY, float targetHeight, ViewTransListener listener){
		if(listener == null){
			return ;
		} else {
			float currTransY = v.getTranslationY();
			if(currTransY == 0){
				listener.isBottom();
			} else if(-currTransY == targetHeight){
				listener.isTop();
			} else {
				if(downTransY > currTransY){
					listener.transToDir(true);
				} else {
					listener.transToDir(false);
				}
			}
		}
	}
	

	/**
	 * 处理一整套效果 (需在ontouchevent 中添加 -- 已经处理 abslistview 冲突)
	 * @param event
	 * @param mTargetHeight		主view 的目标高度
	 * @param sTargetHeight		次view 的目标高度
	 * @return
	 */
	public boolean onTouchEvent(MotionEvent event, float mTargetHeight, float sTargetHeight) {
		this.mTargetHeight = mTargetHeight;
		this.sTargetHeight = sTargetHeight;
		
		int action = event.getAction();
		if(action == MotionEvent.ACTION_DOWN){
			// 记录按下的位置
			downY = event.getY();
			lastY = event.getY();
			// 记录views 最初的位置
			preMainViewTransY = mMainView.getTranslationY();
			preSecViewTransY = mSecondView.getTranslationY();
			// 事件返还
			return false;
		} else if(action == MotionEvent.ACTION_MOVE){
			if(-(mMainView.getTranslationY()) == mTargetHeight && downY > event.getY()){
				// 记录下absListView 最后释放touch事件的位置
				lastY = event.getY();
				return false;
			} else if(-(mMainView.getTranslationY()) == 0 && downY < event.getY()){
				// 记录下absListView 最后释放touch事件的位置
				lastY = event.getY();
				return false;
			} else if(-(mMainView.getTranslationY()) == mTargetHeight && !isListViewReachTopEdge(mMainView)){
				// 记录下absListView 最后释放touch事件的位置
				lastY = event.getY();
				return false;
			} else {
				// 动画未结束前不允许执行平移事件
				if(!isAnimEnd) {
					return true;
				}
				// 处理 平移事件
				runTransLation(event.getY(), lastY);
				isTrans = true;
				doListener(mMainView, preMainViewTransY, mTargetHeight, mViewListener);
				doListener(mSecondView, preSecViewTransY, sTargetHeight, sViewListener);
				return true;
			}
		} else if(action == MotionEvent.ACTION_UP){
			if(isTrans){
				isTrans = false;
				return runAnimator(preMainViewTransY, preSecViewTransY);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 执行平移
	 * 
	 * @param ev
	 */
	private void runTransLation(float currY, float lastY) {
		float deltaY = lastY - currY;

		float lvTransY = mMainView.getTranslationY();
		float calTransY = mSecondView.getTranslationY();

		if (-lvTransY + deltaY > mTargetHeight - jitter) {
			mMainView.setTranslationY(-mTargetHeight);
		} else if (-lvTransY + deltaY < jitter) {
			mMainView.setTranslationY(0);
		} else {
			if (Math.abs(deltaY) > jitter) {
				mMainView.setTranslationY(-deltaY + lvTransY);
			}
		}

		if (-calTransY + deltaY > sTargetHeight - jitter) {
			mSecondView.setTranslationY(-sTargetHeight);
		} else if (-calTransY + deltaY < jitter) {
			mSecondView.setTranslationY(0);
		} else {
			if (Math.abs(deltaY) > jitter) {
				mSecondView.setTranslationY(-deltaY + calTransY);
			}
		}

	}
	
	/**
	 * 执行动画
	 * @param mViewDownTransY
	 * @param sViewDownTransY
	 * @return   返回是否执行了动画
	 */
	private boolean runAnimator(float mViewDownTransY, float sViewDownTransY){
		
		float diffY;
		
		float lvTransY = mMainView.getTranslationY();
		diffY = mViewDownTransY - lvTransY;
		
		boolean lvAnimator;
		if (-lvTransY  > mTargetHeight - jitter || -lvTransY < jitter) {
			lvAnimator = false;
		} else {
			lvAnimator = true;
		}
		if (lvAnimator) {
			if (diffY < 0) {
				// down
				if (-diffY < mMinTransHeight) {
					upLvAnimator();
				} else {
					downLvAnimator();
				}
			} else {
				// up
				if (diffY > mMinTransHeight) {
					upLvAnimator();
				} else {
					downLvAnimator();
				}
			}
		} else {

		}
		
		float calTransY = mSecondView.getTranslationY(); 
		
		boolean calAnimator;
		if (-calTransY  > sTargetHeight - jitter || -calTransY  < jitter) {
			calAnimator = false;
		} else {
			calAnimator = true;
		}
		if (calAnimator) {
			if (diffY < 0) {
				// down
				if (-diffY < mMinTransHeight) {
					upCalAnimator();
				} else {
					downCalAnimator();
				}
			} else {
				// up
				if (diffY > mMinTransHeight) {
					upCalAnimator();
				} else {
					downCalAnimator();
				}
			}
		} else {

		}

		return calAnimator || lvAnimator;
	}

	private void upCalAnimator() {
		sObjectAnimator.setFloatValues(mSecondView.getTranslationY(), -sTargetHeight);
		sObjectAnimator
				.setDuration((long) ((sTargetHeight + mSecondView.getTranslationY()) / speed));
		sObjectAnimator.start();
	}

	private void upLvAnimator() {
		mObjectAnimator.setFloatValues(mMainView.getTranslationY(), -mTargetHeight);
		mObjectAnimator
				.setDuration((long) ((mTargetHeight + mMainView.getTranslationY()) / speed));
		mObjectAnimator.start();
	}

	private void downCalAnimator() {
		sObjectAnimator.setFloatValues(mSecondView.getTranslationY(), 0);
		sObjectAnimator.setDuration((long) ((-mSecondView.getTranslationY()) / speed));
		sObjectAnimator.start();
	}

	private void downLvAnimator() {
		mObjectAnimator.setFloatValues(mMainView.getTranslationY(), 0);
		mObjectAnimator.setDuration((long) ((-mMainView.getTranslationY()) / speed));
		mObjectAnimator.start();
	}

	/**
	 * 判断listview 是不是在顶部
	 * @param absListView
	 * @return
	 */
	public boolean isListViewReachTopEdge(AbsListView absListView) {
		boolean result = false;
		if (absListView.getFirstVisiblePosition() == 0) {
			View topChildView = absListView.getChildAt(0);
			result = topChildView.getTop() == 0;
		}
		return result;
	}
	
	interface ViewTransListener {
		/**
		 * view 平移到了顶部
		 */
		void isTop();
		
		/**
		 * view 平移到了底部
		 */
		void isBottom();
		
		/**
		 * view 开始平移
		 * @param dir 
		 * 		true - 从底部开始平移(向上平移)
		 * 		false - 从顶部开始平移(向下平移)
		 */
		void transToDir(boolean dir);
	}
	
	class SimpleAnimatorListener implements AnimatorListener {

		@Override
		public void onAnimationStart(Animator animation) {}

		@Override
		public void onAnimationEnd(Animator animation) {}

		@Override
		public void onAnimationCancel(Animator animation) {}

		@Override
		public void onAnimationRepeat(Animator animation) {}
		
	}

}
