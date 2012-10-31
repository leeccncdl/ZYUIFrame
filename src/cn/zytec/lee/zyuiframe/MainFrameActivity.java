package cn.zytec.lee.zyuiframe;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import cn.zytec.lee.app.App;

public class MainFrameActivity extends FragmentActivity implements OnClickListener{
	
	private ImageView mic;
	private Button b1;
	private Button b2;
	
//	private FrameLayout secondMenuFrameLayout;
//	private FrameLayout detailFrameLayout;

	private int topMenuPosition = 0;
	
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
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		App.displayWidth = displayMetrics.widthPixels;
		App.displayHeight = displayMetrics.heightPixels;
        
        setContentView(R.layout.main_frame);
       
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
				//ÇÐ»»ËéÆ¬ÄÚÈÝ
				showSecondaryMenu(0);
			}
			break;
		case R.id.top_menu_button2:
			if(topMenuPosition != 1) {
				topMenuPosition = 1;
				//ÇÐ»»ËéÆ¬ÄÚÈÝ
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
    	SecondaryMenuFragment secondaryMenuFrag = SecondaryMenuFragment.newInstance(index);

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
    	
    }
    
    private void addListener(){
    	mic.setOnClickListener(this);
    	b1.setOnClickListener(this);
    	b2.setOnClickListener(this);
    	
    }

    private int CaculateFrameLayoutValue(int percent) {

    	return (int)(App.displayWidth * (percent/100f));
    	
    }

}
