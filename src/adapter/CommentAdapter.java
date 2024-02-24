package adapter;

import java.util.ArrayList;
import object.Comment;
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

public class CommentAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Comment> array;
	private ImageLoader imageLoader;
	DisplayImageOptions doption = null;
	String time = "";
	public CommentAdapter(Context context,
			ArrayList<Comment> objects) {  
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
			convertView = View.inflate(mContext, R.layout.comment_item, null);
			
			holder.tvTitle = (TextView) convertView.findViewById(R.id.titleComment);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
			holder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
			holder.icon_avarta = (ImageView) convertView.findViewById(R.id.imgIcon);
			convertView.setTag(holder);

		}
		else
			holder = (ViewHolder) convertView.getTag();
		
		holder.tvTitle.setText(array.get(position).getTitle());
		holder.tvContent.setText(array.get(position).getContent());
		int day  = Integer.parseInt(Utility.getDayAgo(array.get(position).getCreatDate()));
		if(Utility.getDayAgo(array.get(position).getCreatDate()).equals("0"))
		{
			if(Utility.getHourAgo(array.get(position).getCreatDate()).equals("0"))
				time = Utility.getMinuteAgo(array.get(position).getCreatDate()) + 
						mContext.getResources().getString(R.string.minuteago);
			else
				time = Utility.getHourAgo(array.get(position).getCreatDate()) + 
						mContext.getResources().getString(R.string.hourago);
		}
		else if(day < 31)
			time = Utility.getDayAgo(array.get(position).getCreatDate()) + 
					mContext.getResources().getString(R.string.dayago);
		else
			time = array.get(position).getCreatDate();
		holder.tvDate.setText(time);
		imageLoader.displayImage(array.get(position).getAvartaFB(),
				holder.icon_avarta, doption);
		return convertView;
	}
}
