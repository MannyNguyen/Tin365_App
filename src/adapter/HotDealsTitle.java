package adapter;

import fragment.HotDealsFragmentTitle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

public class HotDealsTitle extends FragmentStatePagerAdapter {

	public HotDealsTitle(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		constant.Constants.log_d("TAG", "AdapterTitle pos: " + position);
		return new HotDealsFragmentTitle(position);

	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		// super.destroyItem(container, position, object);
	}

	@Override
	public int getCount() {
		//TODO set count here
		return 6;
	}
}
