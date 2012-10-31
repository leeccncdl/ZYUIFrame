package cn.zytec.lee.zyuiframe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class DetailFragment extends Fragment  implements OnClickListener {
	
	private Button b;

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
		
		b.setOnClickListener(this);
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		if(v.getId() == R.id.detail_button_frag) {
			((MainFrameActivity)getActivity()).ClosedDetailFragment(this);
		}
	}
	
}
