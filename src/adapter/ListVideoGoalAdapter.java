package adapter;

import java.util.ArrayList;
import object.Comment;
import object.News_Content;
import object.News_Detail;
import utility.Utility;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.skyward101.tin365.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ListVideoGoalAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<String> array;
	private ImageLoader imageLoader;
	DisplayImageOptions doption = null;
	String time = "";
	public ListVideoGoalAdapter(Context context,
			ArrayList<String> objects) {  
		this.mContext = context;
		this.array = objects;

		// image
		imageLoader = ImageLoader.getInstance();
		// Initialize ImageLoader with configuration. Do it once.
		imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
		// Load and display image asynchronously
		doption = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).build();
	}
	


	static class ViewHolder {
		public TextView tvTitle;
		public TextView tvContent;
		public TextView tvDate;
		public ImageView icon_avarta;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return array.size();
	}


	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}


	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = new ViewHolder();
		if(convertView == null)
		{
			convertView = View.inflate(mContext, R.layout.videobongda_item, null);
			
//			holder.tvTitle = (TextView) convertView.findViewById(R.id.titleComment);
//			holder.tvContent = (TextView) convertView.findViewById(R.id.subTitle);
//			holder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
//			holder.icon_avarta = (ImageView) convertView.findViewById(R.id.imgIcon);
			convertView.setTag(holder);

		}
		else
			holder = (ViewHolder) convertView.getTag();
		
//		holder.tvTitle.setText(array.get(position));
		return convertView;
	}
}
