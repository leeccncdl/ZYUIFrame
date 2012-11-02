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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
//import android.widget.FrameLayout;
import android.widget.ImageView;
import cn.zytec.lee.app.App;

public class MainFrameActivity extends FragmentActivity implements OnClickListener{
	
	private ImageView mic;
	private Button b1;
	private Button b2;
	
	SecondaryMenuFragment secondaryMenuFrag;
	
	private GestureDetector gestureDetector;
	
//	private FlingFrameLayout flingFrameLayout;
	private FrameLayout secondMenuFrameLayout;
//	private FrameLayout detailFrameLayout;

	private int topMenuPosition = 0;
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return gestureDetector.onTouchEvent(event);  
	}

	
	public void OpenDetailFragment(int position) {
		DetailFragment detailFrag = DetailFragment.newInstance(position);

        // Execute a transaction, replacing any existing fragment
        // with this one inside the frame.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.detail_view_fl, detailFrag);
        ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
        ft.commit();
	}
	
	public void ClosedDetailFragment(DetailFragment detailFrag) {


        // Execute a transaction, replacing any existing fragment
        // with this one inside the frame.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(detailFrag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
	}
	
//	@Override
//	public View onCreateView(String name, Context context, AttributeSet attrs) {
//		return super.onCreateView(name, context, attrs);
//	}
	
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		return mGallery.onGalleryTouchEvent(event);
//	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		App.displayWidth = displayMetrics.widthPixels;
		App.displayHeight = displayMetrics.heightPixels;
        
        setContentView(R.layout.main_frame);
        
        gestureDetector = new GestureDetector(this, new GestureListener());
        
        showSecondaryMenu(1);
        findView();
        addListener();
        
    }
    
    @Override
    public void onClick(View v) {
    	// TODO Auto-generated method stub
    	switch (v.getId()) {
		case R.id.top_menu_button1:
			if(topMenuPosition != 0) {
				topMenuPosition = 0;
				showSecondaryMenu(0);
			}
			break;
		case R.id.top_menu_button2:
			if(topMenuPosition != 1) {
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
    
    private void addListener(){
    	mic.setOnClickListener(this);
    	b1.setOnClickListener(this);
    	b2.setOnClickListener(this);
    	
    }

    private int CaculateFrameLayoutValue(int percent) {

    	return (int)(App.displayWidth * (percent/100f));
    	
    }
    
    public void setOffset(int xOffset, int yOffset) {
    	secondMenuFrameLayout.scrollTo(xOffset, yOffset);
    }

    class GestureListener extends SimpleOnGestureListener {
    	public boolean onScroll(MotionEvent e1, MotionEvent e2,  
                float distanceX, float distanceY) {
    		
    		System.out.println();
    		int length = CaculateFrameLayoutValue(10);

    		float currentScrollDelta = e1.getX() - e2.getX();
    		if(Math.abs(currentScrollDelta) < length) {
    			int scrollOffset = Math.round(currentScrollDelta);
    			
    			setOffset(scrollOffset,0);
    			
    		}

    		return true;
    		
    	}
    }
    
}
