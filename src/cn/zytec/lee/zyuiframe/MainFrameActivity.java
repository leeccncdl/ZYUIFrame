package cn.zytec.lee.zyuiframe;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import cn.zytec.lee.app.App;

public class MainFrameActivity extends FragmentActivity implements
		OnClickListener {

	// about animation
	private boolean isDrag;
	private int animationDuration = 250;
	private Interpolator decelerateInterpolater;
	private FixedPositionAnimation fixedAnimation;
	private boolean isLeftPosition = false;
	private boolean isDetailFragExpand;
	private GestureDetector gestureDetector;

	private static int fragmentMovePercent = 10;
	private int fragmentMoveDistance;
	private int widthOf60;

	// test
	private ImageView mic;
	private Button b1;
	private Button b2;
	private int topMenuPosition = 0;

	// layout and fragment
	private SecondaryMenuFragment secondaryMenuFrag;
	private FrameLayout secondMenuFrameLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		App.displayWidth = displayMetrics.widthPixels;
		App.displayHeight = displayMetrics.heightPixels;
		fragmentMoveDistance = CaculateFrameLayoutValue(fragmentMovePercent);
		widthOf60 = CaculateFrameLayoutValue(60);
		setContentView(R.layout.main_frame);

		fixedAnimation = new FixedPositionAnimation();
		gestureDetector = new GestureDetector(this, new GestureListener());
		decelerateInterpolater = AnimationUtils.loadInterpolator(this,
				android.R.anim.decelerate_interpolator);

		showSecondaryMenu(0);
		findView();
		addListener();

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean temp = gestureDetector.onTouchEvent(event);
		if (event.getAction() == MotionEvent.ACTION_UP && isDrag) {
			processScrollSnap();
		}
		return temp;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.top_menu_button1:
			if (topMenuPosition != 0) {
				topMenuPosition = 0;
				showSecondaryMenu(0);
			}
			break;
		case R.id.top_menu_button2:
			if (topMenuPosition != 1) {
				topMenuPosition = 1;
				showSecondaryMenu(1);
			}
			break;
		case R.id.mic:
			System.out.println("MIC----------------");
			break;

		default:
			break;
		}
	}

	/**
	 * @description 详细页面的fragment在最上层的FrameLayout上，该方法由SecondaryFragment从Menu中触发
	 * @param position secondaryMenu（fragment）的所选菜单Position
	 */
	public void openDetailFragment(int position) {
		DetailFragment detailFrag = DetailFragment.newInstance(position);

		// Execute a transaction, replacing any existing fragment
		// with this one inside the frame.
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.detail_view_fl, detailFrag);
		ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
		ft.commit();
	}

	/**
	 * @description 关闭详细页的Fragment，类本身调用（Activity 也要控制）
	 * @param detailFrag
	 */
	public void closedDetailFragment(DetailFragment detailFrag) {

		// Execute a transaction, replacing any existing fragment
		// with this one inside the frame.
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.remove(detailFrag);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();
		isDetailFragExpand = false;
	}
	
	/**
	 * @description 最大化详细页面，二级菜单页面被动移动到靠左的位置，详细页面打开
	 * @param detailFrag 来自哪个详细页面的fragment
	 */
	public void expandDetailFragment(DetailFragment detailFrag) {
		if(isLeftPosition) {
			//1.关闭当前的Fragment，打开新的fragment，用户可能已经做了某些当前操作，需需要保存状态
			//2.直接更改Fragment 的 布局，显示效果未知
			detailFrag.expandSelf();
			isDetailFragExpand = true;
		} else {
			isLeftPosition = true;
			fixedAnimation.prepareAnimation();
			secondMenuFrameLayout.startAnimation(fixedAnimation);
			detailFrag.expandSelf();
			isDetailFragExpand = true;
		}
	}

	/**
	 * @description 显示二级菜单
	 * @param index topMenu 页面做菜单的所选 index，根据传入的不同时显示不同的 secondary菜单
	 */
	private void showSecondaryMenu(int index) {
		secondaryMenuFrag = SecondaryMenuFragment.newInstance(index);

		// Execute a transaction, replacing any existing fragment
		// with this one inside the frame.
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.secondary_menu_view_fl, secondaryMenuFrag);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();
	}

	private void findView() {
		mic = (ImageView) findViewById(R.id.mic);
		b1 = (Button) findViewById(R.id.top_menu_button1);
		b2 = (Button) findViewById(R.id.top_menu_button2);
		secondMenuFrameLayout = (FrameLayout) findViewById(R.id.secondary_menu_view_fl);

	}

	private void addListener() {
		mic.setOnClickListener(this);
		b1.setOnClickListener(this);
		b2.setOnClickListener(this);

	}

	/**
	 * @description 根据当前设备的width 计算所需展示的 值 
	 * @param percent 长度百分比
	 * @return
	 */
	private int CaculateFrameLayoutValue(int percent) {

		return (int) (App.displayWidth * (percent / 100f));

	}

	/**
	 * @description 二级菜单所手指触摸滑动后， 执行返回相应位置的动画
	 */
	private void processScrollSnap() {
		fixedAnimation.prepareAnimation();
		secondMenuFrameLayout.startAnimation(fixedAnimation);
	}


	private void setOffset(int xOffset, int yOffset) {
		secondMenuFrameLayout.scrollTo(xOffset, yOffset);
	}

	class GestureListener extends SimpleOnGestureListener {

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			
			if(isDetailFragExpand) {
				return true;
			}
			
			if (e2.getX() > e1.getX()) {
				isLeftPosition = false;
			} else {
				isLeftPosition = true;
			}
			return super.onFling(e1, e2, velocityX, velocityY);
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			
			if(isDetailFragExpand) {
				return true;
			}

			if (e2.getAction() == MotionEvent.ACTION_MOVE) {

				if (e1.getX() >= fragmentMoveDistance && e1.getX() <= widthOf60) {

					float currentScrollDelta = e1.getX() - e2.getX();
					int scrollOffset = 0;
					if(isLeftPosition) {
						scrollOffset = Math.round(currentScrollDelta) + fragmentMoveDistance;
					} else {				
						scrollOffset = Math.round(currentScrollDelta);
					}

					setOffset(scrollOffset, 0);
					isDrag = true;
				}
			}
			return true;
		}
	}

	class FixedPositionAnimation extends Animation {

		private int mInitialOffset;
		private int mTargetOffset;
		private int mTargetDistance;

		public void prepareAnimation() {

			mInitialOffset = secondMenuFrameLayout.getScrollX();
			mTargetOffset = 0;

			if (isLeftPosition) {
				mTargetDistance = mTargetOffset - mInitialOffset
						+ CaculateFrameLayoutValue(10);
			} else {
				mTargetDistance = mTargetOffset - mInitialOffset;
			}

			this.setDuration(animationDuration);
			this.setInterpolator(decelerateInterpolater);

			isDrag = false;
		}

		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation transformation) {
			interpolatedTime = (interpolatedTime > 1.0f) ? 1.0f
					: interpolatedTime;
			int offset = mInitialOffset
					+ (int) (mTargetDistance * interpolatedTime);
			setOffset(offset, 0);

		}

		@Override
		public boolean getTransformation(long currentTime,
				Transformation outTransformation) {

			if (super.getTransformation(currentTime, outTransformation) == false) {
				// 最后停在什么位置
				if (isLeftPosition) {
					setOffset(CaculateFrameLayoutValue(10), 0);
				} else {
					setOffset(0, 0);
				}
				return false;
			}
			return true;
		}
	}
}
