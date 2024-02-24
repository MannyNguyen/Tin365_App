package adapter;

import com.skyward101.tin365.SplashActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;


import fragment.TraditionFragmentTitle;

public class TraditionTitle extends FragmentStatePagerAdapter {

	public TraditionTitle(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		constant.Constants.log_d("TAG", "AdapterTitle pos: " + position);
		return new TraditionFragmentTitle(position);

	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		// super.destroyItem(container, position, object);
	}

	@Override
	public int getCount() {
		return SplashActivity.cats.size();
	}
}
