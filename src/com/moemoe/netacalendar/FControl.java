package com.moemoe.netacalendar;

import android.animation.Animator.AnimatorListener;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;

/**
 * FControl
 * Description: ��MIMU��������(ListView & MonthViewPager)
 * 				ͬ��ƽ��  ( 0 - ���Ե�Ŀ��߶�  )
 * @author Lpzahd
 * @date 2015-11-21 ����10:03:25
 */
public class FControl {

	/**
	 * main view (touch view)
	 */
	private AbsListView mMainView;
	
	/**
	 * second view (���� main view)
	 */
	private View mSecondView;

	// ��Сƽ�Ƹ߶�~ ƽ�ƾ���С�����ֵ���ͻ᷵�ص�ԭ����״̬
	private float mMinTransHeight = 100;

	/**
	 * ����ִ���ٶ�
	 */
	private float speed = 2f;
	
	/**
	 * ��������
	 */
	private float jitter = 5f; 
	
	/**
	 * ǰһ����view ƽ�Ƶ�λ��
	 */
	private float preMainViewTransY;
	
	/**
	 * ǰһ�δ�view ƽ�Ƶ�λ��
	 */
	private float preSecViewTransY;
	
	/**
	 * ��view ��Ŀ��߶�
	 */
	private float mTargetHeight;
	
	/**
	 * ��view ��Ŀ��߶�
	 */
	private float sTargetHeight;
	
	/**
	 * ��ָ���µ�y �����λ��
	 */
	private float downY;
	
	/**
	 * ����ƽ��֮ǰ ����ȡ���� λ��
	 */
	private float lastY;
	
	/**
	 * �ǲ���ƽ��
	 * 	(ƽ�ƺͶ����������ģ�û��ƽ�ƾ�û�ж���)
	 */
	private boolean isTrans;
	
	/**
	 * �����Ƿ�ִ�н���
	 */
	private boolean isAnimEnd = true;
	
	private ObjectAnimator mObjectAnimator;
	private ObjectAnimator sObjectAnimator;
	
	private ViewTransListener mViewListener;
	private ViewTransListener sViewListener;
	
	/**
	 * ��view ����״̬�ļ���
	 * @param listener
	 */
	public void setMainViewTransListener(ViewTransListener listener){
		mViewListener = listener;
	}
	
	/**
	 * ��view ����״̬�ļ���
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
	 * ��ʼ������
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
	 * ���ü�������
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
	 * ����һ����Ч�� (����ontouchevent ����� -- �Ѿ����� abslistview ��ͻ)
	 * @param event
	 * @param mTargetHeight		��view ��Ŀ��߶�
	 * @param sTargetHeight		��view ��Ŀ��߶�
	 * @return
	 */
	public boolean onTouchEvent(MotionEvent event, float mTargetHeight, float sTargetHeight) {
		this.mTargetHeight = mTargetHeight;
		this.sTargetHeight = sTargetHeight;
		
		int action = event.getAction();
		if(action == MotionEvent.ACTION_DOWN){
			// ��¼���µ�λ��
			downY = event.getY();
			lastY = event.getY();
			// ��¼views �����λ��
			preMainViewTransY = mMainView.getTranslationY();
			preSecViewTransY = mSecondView.getTranslationY();
			// �¼�����
			return false;
		} else if(action == MotionEvent.ACTION_MOVE){
			if(-(mMainView.getTranslationY()) == mTargetHeight && downY > event.getY()){
				// ��¼��absListView ����ͷ�touch�¼���λ��
				lastY = event.getY();
				return false;
			} else if(-(mMainView.getTranslationY()) == 0 && downY < event.getY()){
				// ��¼��absListView ����ͷ�touch�¼���λ��
				lastY = event.getY();
				return false;
			} else if(-(mMainView.getTranslationY()) == mTargetHeight && !isListViewReachTopEdge(mMainView)){
				// ��¼��absListView ����ͷ�touch�¼���λ��
				lastY = event.getY();
				return false;
			} else {
				// ����δ����ǰ������ִ��ƽ���¼�
				if(!isAnimEnd) {
					return true;
				}
				// ���� ƽ���¼�
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
	 * ִ��ƽ��
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
	 * ִ�ж���
	 * @param mViewDownTransY
	 * @param sViewDownTransY
	 * @return   �����Ƿ�ִ���˶���
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
	 * �ж�listview �ǲ����ڶ���
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
		 * view ƽ�Ƶ��˶���
		 */
		void isTop();
		
		/**
		 * view ƽ�Ƶ��˵ײ�
		 */
		void isBottom();
		
		/**
		 * view ��ʼƽ��
		 * @param dir 
		 * 		true - �ӵײ���ʼƽ��(����ƽ��)
		 * 		false - �Ӷ�����ʼƽ��(����ƽ��)
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
