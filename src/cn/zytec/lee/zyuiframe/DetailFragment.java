package cn.zytec.lee.zyuiframe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class DetailFragment extends Fragment implements OnClickListener {

//	private static final String TAG = "DetailFragment";
	
	private Button b;
	private Button open;
	private LinearLayout l1;
	
	private int mIndex;

	
	public static DetailFragment newInstance(int detailIndex) {
		DetailFragment f = new DetailFragment();
		Bundle args = new Bundle();
		args.putInt("detailIndex", detailIndex);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		b = (Button) getView().findViewById(R.id.detail_button_frag);
		open = (Button) getView().findViewById(R.id.detail_button_open);
		l1 = (LinearLayout) getView().findViewById(R.id.detail_fragment_range_ll_2);

		b.setOnClickListener(this);
		open.setOnClickListener(this);
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		

		
		super.onCreate(savedInstanceState);
		mIndex = getArguments().getInt("detailIndex");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		return inflater.inflate(R.layout.detail_fragment, container, false);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.detail_button_frag:
			((MainFrameActivity) getActivity()).closedDetailFragment(this);
			break;
		case R.id.detail_button_open:
			((MainFrameActivity) getActivity()).expandDetailFragment(this);
			break;
		default:
			break;
		}
	}
	
	public void expandSelf() {
		l1.setVisibility(View.GONE);
	}

}
