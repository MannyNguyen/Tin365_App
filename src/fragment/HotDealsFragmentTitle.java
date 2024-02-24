package fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.skyward101.tin365.R;

public class HotDealsFragmentTitle extends SherlockFragment {

	private int pos;

	public HotDealsFragmentTitle(int pos) {
		super();
		constant.Constants.log_d("TAG", "FragmentTitle - constructor : " + pos);
		this.pos = pos;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		constant.Constants.log_d("TAG", "FragmentTitle");

		View v = inflater.inflate(R.layout.fragment_title, container, false);

		return v;

	}

}
