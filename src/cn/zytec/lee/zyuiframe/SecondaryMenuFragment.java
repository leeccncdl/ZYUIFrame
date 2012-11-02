package cn.zytec.lee.zyuiframe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SecondaryMenuFragment extends Fragment implements OnClickListener {
	
	private TextView textView;
	private Button buttonTest;
	
    public static SecondaryMenuFragment newInstance(int index) {
    	SecondaryMenuFragment f = new SecondaryMenuFragment();

    	
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);

        return f;
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		textView = (TextView) getView().findViewById(R.id.secondary_menu_text);
		buttonTest = (Button) getView().findViewById(R.id.secondary_menu_frag_button_test);
		buttonTest.setOnClickListener(this);
		int pos = getArguments().getInt("index", 0);
		System.out.println(pos+"--------------------");
		if(pos==0) {
			textView.setText("top_menu_One",TextView.BufferType.EDITABLE);
		} else {
			textView.setText("top_menu_Two",TextView.BufferType.EDITABLE);
			
		}
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.secondary_menu_fragment, container, false);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.secondary_menu_frag_button_test) {
			 ((MainFrameActivity)getActivity()).OpenDetailFragment(0);
		}
	}
}
