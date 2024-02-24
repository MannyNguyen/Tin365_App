package adapter;

import java.util.ArrayList;

import object.Bookmark;
import object.News_Content;
import utility.DataBaseHelper;
import utility.Utility;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.skyward101.tin365.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class BookmarkAdapter extends BaseAdapter {
	private Context mContext;
	private int mRes;
	private ArrayList<Bookmark> mNews;
	private ImageLoader imageLoader;
	DisplayImageOptions doption = null;
	DataBaseHelper db;

	public BookmarkAdapter(Context context,
			ArrayList<Bookmark> objects) {  
		this.mContext = context;
		this.mNews = objects;

		// image
		imageLoader = ImageLoader.getInstance();
		// Initialize ImageLoader with configuration. Do it once.
		imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
		// Load and display image asynchronously
		doption = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).build();
		db = new DataBaseHelper(mContext);
	}
	

	/*@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View vi = convertView;
		final ViewHolder viewHolder;
		if (vi == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// if (mNews.get(position).getmHeader())
			// vi = inflater.inflate(R.layout.title_item_header, null);
			// else
				vi = inflater.inflate(this.mRes, null);
			viewHolder = new ViewHolder();
			viewHolder.tvTitle = (TextView) vi.findViewById(R.id.tvTitle);
			viewHolder.tvDate = (TextView) vi.findViewById(R.id.tvDate);
			viewHolder.icon = (ImageView) vi.findViewById(R.id.imgIcon);
			vi.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) vi.getTag();

		//Log.d("TAG", "pos: + " + position);
//		viewHolder.tvTitle.setText(mNews.get(position).getmTitle());
//		viewHolder.tvDate.setText(mNews.get(position).getmDate());
//
//		imageLoader.displayImage(mNews.get(position).getmImgUrl(),
//				viewHolder.icon, doption);

		return vi;

	}*/

	static class ViewHolder {
		public TextView tvTitle;
		public TextView tvDate;
		public ImageView icon_delete;
		public ImageView thumbImg;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mNews.size();
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
			convertView = View.inflate(mContext, R.layout.bookmark_item, null);
			
			holder.tvTitle = (TextView) convertView.findViewById(R.id.titleBookmark);
			holder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
			holder.thumbImg = (ImageView) convertView.findViewById(R.id.imgIcon);
			holder.icon_delete = (ImageView) convertView.findViewById(R.id.img_delete);
			holder.icon_delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					db.deleteOneBookamrk(mNews.get(position).getIDNews(), Utility._str_table_Bookmark);
					mNews.remove(position);
					notifyDataSetChanged();
				}
			});
			convertView.setTag(holder);

		}
		else
			holder = (ViewHolder) convertView.getTag();
		
		holder.tvTitle.setText(mNews.get(position).getTitle());
		imageLoader.displayImage(mNews.get(position).getThumbImg(),
				holder.thumbImg, doption);
		String time = "";
		int day  = Integer.parseInt(Utility.getDayAgo(mNews.get(position).getCreateDate()));
		if(Utility.getDayAgo(mNews.get(position).getCreateDate()).equals("0"))
		{
			if(Utility.getHourAgo(mNews.get(position).getCreateDate()).equals("0"))
				time = Utility.getMinuteAgo(mNews.get(position).getCreateDate()) + 
						mContext.getResources().getString(R.string.minuteago);
			else
				time = Utility.getHourAgo(mNews.get(position).getCreateDate()) +
						mContext.getResources().getString(R.string.hourago);
		}
		else if(day < 31)
			time = Utility.getDayAgo(mNews.get(position).getCreateDate()) +
					mContext.getResources().getString(R.string.dayago);
		else
			time = mNews.get(position).getCreateDate();
		holder.tvDate.setText(time);
		Log.i("adapter=", String.valueOf(position));
		
		return convertView;
	}
}
