package cn.zytec.lee.zyuiframe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
	private boolean isDragState;
	private int animationDuration = 250;
	private Interpolator decelerateInterpolater;
	private FixedPositionAnimation fixedAnimation;
	private boolean isSecMenuLeftPosition = false;
//	private boolean isDetailRightPosition = false;
	private boolean isDetailFragOpen;
	private boolean isDetailExpand;

	public static GestureDetector gestureDetector;

	private static int fragmentMovePercent = 10;
	private int windowWidthOfPercentten;

	// test
	private ImageView mic;

	// top_menu
	private Button b1;
	private Button b2;
	private Button b3;
	private Button b4;
	private ImageView refresh;
	private ImageView setup;

	private int topMenuPosition = 0;

	private FragmentActivity activity;

	// layout and fragment
	private SecondaryMenuFragment secondaryMenuFrag;
	private FrameLayout secondMenuFrameLayout;
	private FrameLayout detailFrameLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		activity = this;

		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		App.displayWidth = displayMetrics.widthPixels;
		App.displayHeight = displayMetrics.heightPixels;
		windowWidthOfPercentten = CaculateFrameLayoutValue(fragmentMovePercent);
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
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean temp = gestureDetector.onTouchEvent(event);
		if (event.getAction() == MotionEvent.ACTION_UP && isDragState) {
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

		case R.id.top_menu_button3:

			break;
		case R.id.top_menu_button4:
			break;
		// case R.id.top_menu_refresh_iv:
		// break;
		// case R.id.top_menu_setup_iv:
		// break;
		default:
			break;
		}
	}

	/**
	 * @description 详细页面的fragment在最上层的FrameLayout上，该方法由SecondaryFragment从Menu中触发
	 * @param position
	 *            secondaryMenu（fragment）的所选菜单Position
	 */
	public void openDetailFragment(int position) {
		DetailFragment detailFrag = DetailFragment.newInstance(position);
		// 打开之前确保secondary显示在左侧
		if (!isSecMenuLeftPosition) {
			isSecMenuLeftPosition = true;
			fixedAnimation.prepareAnimation(secondMenuFrameLayout);
			secondMenuFrameLayout.startAnimation(fixedAnimation);
		}

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.setCustomAnimations(android.R.anim.fade_in,
				android.R.anim.slide_out_right);
		ft.replace(R.id.detail_view_fl, detailFrag);
		ft.commit();

		isDetailFragOpen = true;
	}

	/**
	 * @description 关闭详细页的Fragment，类本身调用（Activity 也要控制）
	 * @param fragment
	 */
	public void closedDetailFragment(Fragment fragment) {

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.setCustomAnimations(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
		ft.remove(fragment);
		ft.commit();
		
		isDetailFragOpen = false;
	}

	/**
	 * @description 最大化详细页面，二级菜单页面被动移动到靠左的位置，详细页面打开
	 * @param detailFrag
	 *            来自哪个详细页面的fragment
	 */
	public void expandDetailFragment(DetailFragment detailFrag) {

		detailFrag.expandSelf();
		isDetailFragOpen = true;
		isDetailExpand = true;

	}

	/**
	 * @description 显示二级菜单
	 * @param index
	 *            topMenu 页面做菜单的所选 index，根据传入的不同时显示不同的 secondary菜单
	 */
	private void showSecondaryMenu(int index) {
		secondaryMenuFrag = SecondaryMenuFragment.newInstance(index);

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
		ft.replace(R.id.secondary_menu_view_fl, secondaryMenuFrag);
		ft.commit();
	}

	private void findView() {
		mic = (ImageView) findViewById(R.id.mic);

		b1 = (Button) findViewById(R.id.top_menu_button1);
		b2 = (Button) findViewById(R.id.top_menu_button2);
		b3 = (Button) findViewById(R.id.top_menu_button3);
		b4 = (Button) findViewById(R.id.top_menu_button4);
		// refresh = (ImageView) findViewById(R.id.top_menu_refresh_iv);
		// setup = (ImageView) findViewById(R.id.top_menu_setup_iv); onTouch gesture 

		secondMenuFrameLayout = (FrameLayout) findViewById(R.id.secondary_menu_view_fl);
		detailFrameLayout = (FrameLayout) findViewById(R.id.detail_view_fl);

	}

	private void addListener() {
		mic.setOnClickListener(this);
		b1.setOnClickListener(this);
		b2.setOnClickListener(this);
		b3.setOnClickListener(this);
		b4.setOnClickListener(this);
		// refresh.setOnClickListener(this);
		// setup.setOnClickListener(this);

	}

	/**
	 * @description 二级菜单所手指触摸滑动后， 执行返回相应位置的动画
	 */
	private void processScrollSnap() {
		if (isDetailFragOpen) {
			System.out.println("*********"+detailFrameLayout.getScrollX()+"**********");
			int outOfDistance = 0;
			if(isDetailExpand) {
				outOfDistance = windowWidthOfPercentten*7 + detailFrameLayout.getScrollX();
			} else {
				outOfDistance = windowWidthOfPercentten*4 + detailFrameLayout.getScrollX();
			}
			if(outOfDistance<0) {
//				 activity.getSupportFragmentManager().findFragmentById(R.id.detail_view_fl).onDestroy();
				detailFrameLayout.removeAllViews();
				isDetailFragOpen = false;
				isDetailExpand = false;
				setOffset(0, 0, detailFrameLayout);
//				if(activity.getSupportFragmentManager().findFragmentById(R.id.detail_view_fl) == null ) {
//					System.out.println("Good");
//				}
			} else {
				fixedAnimation.prepareAnimation(detailFrameLayout);
				detailFrameLayout.startAnimation(fixedAnimation);
			}
		} else {

			fixedAnimation.prepareAnimation(secondMenuFrameLayout);
			secondMenuFrameLayout.startAnimation(fixedAnimation);
		}
	}

	/**
	 * @description 设置显示区域的位移
	 * @param xOffset 
	 * @param yOffset
	 * @param view 该view所在层的显示区域
	 */
	private void setOffset(int xOffset, int yOffset, View view) {
		view.scrollTo(xOffset, yOffset);
	}

	class GestureListener extends SimpleOnGestureListener {

//		@Override
//		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//				float velocityY) {
//			 if (isDetailFragOpen) {
//			 // e1起始点在右半个屏幕
//			 float flingDistance = e2.getX() - e1.getX();
//			 float startPositionX = 0;
//			 float maxDistance = 0;
//			 if (isDetailExpand) {
//			 startPositionX = windowWidthOfPercentten * 2;
//			 maxDistance = windowWidthOfPercentten * 5;
//			 } else {
//			 startPositionX = windowWidthOfPercentten * 5;
//			 maxDistance = windowWidthOfPercentten * 3;
//			 }
//			
//			 if (e1.getX() > startPositionX && flingDistance > maxDistance) {
//			 closedDetailFragment(activity.getSupportFragmentManager()
//			 .findFragmentById(R.id.detail_view_fl));
//			 }
//			
//			 return true;
//			 }
//
//			return super.onFling(e1, e2, velocityX, velocityY);
//		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {

			if (isDetailFragOpen) {
				// 判断详细页面滑动方向，以确定滑动结束时，详细页面的停留位置
//				if (e2.getX() > e1.getX()) {
//					isDetailRightPosition = true;
//				} else {
//					isSecMenuLeftPosition = false;
//				}
				int limitLeftX = 0;
				if (isDetailExpand) {
					limitLeftX = windowWidthOfPercentten * 2;
				} else {
					limitLeftX = windowWidthOfPercentten * 5;
				}
				if (e2.getAction() == MotionEvent.ACTION_MOVE
						&& e1.getX() >= limitLeftX) {
					float currentScrollDelta = e1.getX() - e2.getX();
					setOffset((int) currentScrollDelta, 0, detailFrameLayout);
				}
				

			} else {// secondary的滑动处理

				if (e2.getX() > e1.getX()) {
					isSecMenuLeftPosition = false;
				} else {
					isSecMenuLeftPosition = true;
				}

				if (e2.getAction() == MotionEvent.ACTION_MOVE) {

					if (e1.getX() >= windowWidthOfPercentten
							&& e1.getX() <= windowWidthOfPercentten * 6) {

						float currentScrollDelta = e1.getX() - e2.getX();
						int scrollOffset = 0;
						if (isSecMenuLeftPosition) {
							scrollOffset = Math.round(currentScrollDelta)
									+ windowWidthOfPercentten;
						} else {
							scrollOffset = Math.round(currentScrollDelta);
						}

						setOffset(scrollOffset, 0, secondMenuFrameLayout);
					}
				}
			}
			isDragState = true;
			return true;
		}
	}

	class FixedPositionAnimation extends Animation {

		private int mInitialOffset;
		private int mTargetOffset;
		private int mTargetDistance;

		private boolean isDetailFragOpenAnimation;

		public void prepareAnimation(View view) {
			isDetailFragOpenAnimation = isDetailFragOpen;
			mInitialOffset = view.getScrollX();
			mTargetOffset = 0;

			if (isDetailFragOpenAnimation) {
				mTargetDistance = mTargetOffset - mInitialOffset;
			} else {

				if (isSecMenuLeftPosition) {
					mTargetDistance = mTargetOffset - mInitialOffset
							+ windowWidthOfPercentten;
				} else {
					mTargetDistance = mTargetOffset - mInitialOffset;
				}
			}

			this.setDuration(animationDuration);
			this.setInterpolator(decelerateInterpolater);

			isDragState = false;
		}

		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation transformation) {
			interpolatedTime = (interpolatedTime > 1.0f) ? 1.0f
					: interpolatedTime;
			int offset = mInitialOffset
					+ (int) (mTargetDistance * interpolatedTime);
			if (isDetailFragOpenAnimation) {
				setOffset(offset, 0, detailFrameLayout);
			} else {
				setOffset(offset, 0, secondMenuFrameLayout);
			}

		}

		@Override
		public boolean getTransformation(long currentTime,
				Transformation outTransformation) {
			if (super.getTransformation(currentTime, outTransformation) == false) {
				// 最后停在什么位置
				if (isDetailFragOpen) {
					setOffset(0, 0, detailFrameLayout);
				} else {
					if (isSecMenuLeftPosition) {
						setOffset(CaculateFrameLayoutValue(10), 0,
								secondMenuFrameLayout);
					} else {
						setOffset(0, 0, secondMenuFrameLayout);
					}
				}

				return false;
			}
			return true;
		}
	}
	
	/**
	 * @description 根据当前设备的width 计算所需展示的 值
	 * @param percent
	 *            长度百分比
	 * @return
	 */
	private int CaculateFrameLayoutValue(int percent) {
		return (int) (App.displayWidth * (percent / 100f));
	}
}
