package cn.zytec.lee.zyuiframe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class SecondaryMenuFragment extends Fragment implements OnClickListener {
	
//	private static final String TAG = "SecondaryMenuFragment";
	
	private TextView textView;
	private Button buttonTest;
	
    public static SecondaryMenuFragment newInstance(int index) {
    	SecondaryMenuFragment f = new SecondaryMenuFragment();

        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);

        return f;
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		textView = (TextView) getView().findViewById(R.id.secondary_menu_text);
		buttonTest = (Button) getView().findViewById(R.id.secondary_menu_frag_button_test);
		buttonTest.setOnClickListener(this);
		int pos = getArguments().getInt("index", 0);
		if(pos==0) {
//			textView.setText("top_menu_One",TextView.BufferType.EDITABLE);
//			String strLink = "<a href=\"http://3g.sina.com.cn/prog/wapsite/sso/register.php?"
//					+ "ns=1&backURL=http%3A%2Fdpool%2Fttt%2Fhome.php&backTitle="
//					+ "%D0%C2%C0%CB%CE%A2%B2%A9&vt=\">注册新浪微博</a>";
//			textView.setText(Html.fromHtml(strLink));
//			textView.setMovementMethod(LinkMovementMethod.getInstance());
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
		if(v.getId() == R.id.secondary_menu_frag_button_test) {
			 ((MainFrameActivity)getActivity()).openDetailFragment(0);
		} else if(v.getId() == R.id.testid)	 {
			System.out.println("Onclick In");
		}
	}
}
