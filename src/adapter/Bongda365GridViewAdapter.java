package adapter;

import java.util.ArrayList;

import object.News_Content;
import utility.Utility;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import bongda365.BongDa365Activity;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.skyward101.tin365.R;

public class Bongda365GridViewAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<News_Content> mBongda365GridItems;
	private int mRes;

	private DisplayImageOptions doption;

	String ngaytruoc, giotruoc, phuttruoc;
	private String _day = "";

	public Bongda365GridViewAdapter(Context mContext,
			ArrayList<News_Content> mBongda365GridItems, int mRes) {
		super();
		this.mContext = mContext;
		this.mBongda365GridItems = mBongda365GridItems;
		this.mRes = mRes;

		ngaytruoc = mContext.getResources().getString(R.string.ngaytruoc);
		giotruoc = mContext.getResources().getString(R.string.giotruoc);
		phuttruoc = mContext.getResources().getString(R.string.phuttruoc);

		// Load and display image asynchronously
		doption = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic)
				.showImageOnFail(R.drawable.ic).cacheInMemory(true)
				.cacheOnDisc(true).build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mBongda365GridItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public class ViewHolder {
		public ImageView imgView;
		public TextView title;
		public TextView date;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, this.mRes, null);
			holder.imgView = (ImageView) convertView.findViewById(R.id.img);
			holder.title = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.date = (TextView) convertView.findViewById(R.id.tvDate);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String datepost = mBongda365GridItems.get(position).getDatePost();

		int day = Integer.parseInt(Utility.getDayAgo(datepost));
		if (Utility.getDayAgo(datepost).equals("0")) {
			if (Utility.getHourAgo(datepost).equals("0"))
				_day = Utility.getMinuteAgo(datepost) + phuttruoc;
			else
				_day = Utility.getHourAgo(datepost) + giotruoc;
		} else if (day < 31)
			_day = Utility.getDayAgo(datepost) + ngaytruoc;
		else
			_day = datepost;

		holder.title.setText(mBongda365GridItems.get(position).getTitle());
		holder.date.setText(_day);

		if (!mBongda365GridItems.get(position).getNewestImage().equals("null"))
			BongDa365Activity.imageLoader_BongDa365.displayImage(
					mBongda365GridItems.get(position).getNewestImage(),
					holder.imgView, doption);
		else
			BongDa365Activity.imageLoader_BongDa365.displayImage(
					mBongda365GridItems.get(position).getFeatureImage(),
					holder.imgView, doption);

		return convertView;
	}
}