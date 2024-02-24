package adapter;


import com.skyward101.tin365.TraditionDetailsActivity;

import fragment.TraditionFragmentDetails;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

public class TraditionAdapterDetails extends FragmentStatePagerAdapter {

	public TraditionAdapterDetails(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		constant.Constants.log_d("TAG", "TraditionFragmentDetails pos: "+ position);
		return new TraditionFragmentDetails(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		//super.destroyItem(container, position, object);
	}
	
	@Override
	public int getCount() {
		return TraditionDetailsActivity.news.size();
	}
}