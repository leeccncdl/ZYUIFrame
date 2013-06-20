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
import android.widget.Toast;
import cn.zytec.lee.app.App;

/** 
* @ClassName: MainFrameActivity 
* @Description: 主页面 
* @author 香格李拉   limingdl@yeah.net
* @date 2013-5-8 下午04:01:03  
*/
public class MainFrameActivity extends FragmentActivity implements
		OnClickListener {

//	private static final String TAG = "MainFrameActivity";
	
	//几种二级三级fragment的位置滑动状态标记
	private boolean isDragState = false;
	private boolean isDetailFragOpen = false;;
	private boolean isDetailExpand = false;
	private boolean isSecMenuLeftPosition = false;
	private boolean isScrollLeft = false;
	
	private int animationDuration = 250;
	private Interpolator decelerateInterpolater;
	private FixedPositionAnimation fixedAnimation;
	public static GestureDetector gestureDetector;

	private static int fragmentMovePercent = 10;//用于计算屏幕一定距离的数值
	private int windowWidthOfTenPercent;//十分之一屏幕宽度的值

	// layout and fragment
	private SecondaryMenuFragment secondaryMenuFrag;
	private FrameLayout secondMenuFrameLayout;
	private FrameLayout detailFrameLayout;
	
	private int topMenuPosition = 0;//保存顶级菜单选中的值
	private ImageView mic;
	private Button b1;
	private Button b2;
	private Button b3;
	private Button b4;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		//取屏幕信息
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		App.displayWidth = displayMetrics.widthPixels;
		App.displayHeight = displayMetrics.heightPixels;
		//计算屏幕宽度的十分之一，用于后面Fragment显示位置的设定
		windowWidthOfTenPercent = CaculateFrameLayoutValue(fragmentMovePercent);
		
		setContentView(R.layout.main_frame);

		fixedAnimation = new FixedPositionAnimation();
		gestureDetector = new GestureDetector(this, new GestureListener());
		//动画的插值器
		decelerateInterpolater = AnimationUtils.loadInterpolator(this,
				android.R.anim.decelerate_interpolator);
		//显示二级菜单fragment
		showSecondaryMenu(topMenuPosition);
		findView();
		addListener();

	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
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
			Toast.makeText(this, "快捷操作菜单被选中", Toast.LENGTH_SHORT).show();
			break;

		case R.id.top_menu_button3:
			if (topMenuPosition != 2) {
				topMenuPosition = 2;
				showSecondaryMenu(2);
			}
			break;
		case R.id.top_menu_button4:
			if (topMenuPosition != 3) {
				topMenuPosition = 3;
				showSecondaryMenu(3);
			}
			break;
			
		default:
			break;
		}
	}

	/**
	 * @description 显示详细页的Fragment，显示时二级菜单的Fragment滑到靠近左侧的位置
	 * @param position 二级菜单选中的菜单位置
	 */
	public void openDetailFragment(int position) {
		DetailFragment detailFrag = DetailFragment.newInstance(position);
		// 打开详细页之前确保secondaryMenu显示在左侧
		if (!isSecMenuLeftPosition) {
			isScrollLeft = true;
			fixedAnimation.prepareAnimation(secondMenuFrameLayout);
			secondMenuFrameLayout.startAnimation(fixedAnimation);
		}

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.setCustomAnimations(R.anim.slide_in_right,
				android.R.anim.fade_out);
		ft.replace(R.id.detail_view_fl, detailFrag);
		ft.commit();

		isDetailFragOpen = true;
	}

	/**
	 * @description 关闭详细页的Fragment
	 * @param fragment 被关闭的fragment对象
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
	 * @description 展开详细显示页面
	 * @param detailFrag 被展开的详细页fragment对象
	 */
	public void expandDetailFragment(DetailFragment detailFrag) {

		detailFrag.expandSelf();
		isDetailExpand = true;

	}

	/**
	 * @description  显示二级菜单fragment
	 * @param position 顶级菜单所选项
	 * 事件处理机制 消息处理机制（线程间通信） 手势动画   图片显示 存储 网络
	 */
	private void showSecondaryMenu(int position) {
		secondaryMenuFrag = SecondaryMenuFragment.newInstance(position);

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

		secondMenuFrameLayout = (FrameLayout) findViewById(R.id.secondary_menu_view_fl);
		detailFrameLayout = (FrameLayout) findViewById(R.id.detail_view_fl);
	}

	private void addListener() {
		mic.setOnClickListener(this);
		b1.setOnClickListener(this);
		b2.setOnClickListener(this);
		b3.setOnClickListener(this);
		b4.setOnClickListener(this);
	}

	/**
	 *@description 触摸滑动离开屏幕后快照的处理，根据滑动对象的不同（detailFragment or secondaryMenuFragment）设置动画
	 */
	private void processScrollSnap() {
		if (isDetailFragOpen) {//处理的是详细页面fragment的 snap
			int outOfDistance = 0;
			if(isDetailExpand) {
				outOfDistance = (int)(windowWidthOfTenPercent*6.5) + detailFrameLayout.getScrollX();
			} else {
				outOfDistance = (int)(windowWidthOfTenPercent*3.5) + detailFrameLayout.getScrollX();
			}
			if(outOfDistance<0) {
//				 activity.getSupportFragmentManager().findFragmentById(R.id.detail_view_fl).onDestroy();
				detailFrameLayout.removeAllViews();
				isDetailFragOpen = false;
				isDetailExpand = false;
				setOffset(0, 0, detailFrameLayout);
			} else {
				fixedAnimation.prepareAnimation(detailFrameLayout);
				detailFrameLayout.startAnimation(fixedAnimation);
			}
		} else {//处理二级菜单fragment的 snap
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

			if (isDetailFragOpen) {// detailFragment的滑动处理
				int touchLimitLeftX = 0;
				if (isDetailExpand) {
					touchLimitLeftX = windowWidthOfTenPercent * 2;
				} else {
					touchLimitLeftX = windowWidthOfTenPercent * 5;
				}
				if (e2.getAction() == MotionEvent.ACTION_MOVE
						&& e1.getX() >= touchLimitLeftX) {
					float currentScrollDelta = e1.getX() - e2.getX();
					setOffset((int) currentScrollDelta, 0, detailFrameLayout);
				} else {
					//nothing
				}
			} else {// secondaryMenuFragment的滑动处理
				if (e2.getX() > e1.getX()) {
					isScrollLeft = false;
				} else {
					isScrollLeft = true;
				}

				if (e2.getAction() == MotionEvent.ACTION_MOVE) {
					if (e1.getX() >= windowWidthOfTenPercent
							&& e1.getX() <= windowWidthOfTenPercent * 6) {
						float currentScrollDelta = e1.getX() - e2.getX();
						int scrollOffset = 0;
						if (isSecMenuLeftPosition) {
							scrollOffset = Math.round(currentScrollDelta) + windowWidthOfTenPercent;
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

				if (isScrollLeft) {
					mTargetDistance = mTargetOffset - mInitialOffset
							+ windowWidthOfTenPercent;
					isSecMenuLeftPosition = true;
				} else {
					mTargetDistance = mTargetOffset - mInitialOffset;
					isSecMenuLeftPosition = false;
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
					if (isScrollLeft) {
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
