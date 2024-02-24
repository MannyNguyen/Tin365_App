package adapter;

import java.util.ArrayList;

import object.News_Content;
import utility.Utility;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.skyward101.tin365.ModernContents;
import com.skyward101.tin365.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class TitleListView extends ArrayAdapter<News_Content> {
	private Context mContext;
	private int mRes;
	private ArrayList<News_Content> mNews;
	public static ImageLoader imageLoader;
	DisplayImageOptions doption = null;
	String _day = "";

	String ngaytruoc, giotruoc, phuttruoc;

	public TitleListView(Context context, int resource,
			ArrayList<News_Content> objects) {
		super(context, resource, objects);
		this.mContext = context;
		this.mRes = resource;
		this.mNews = objects;

		ngaytruoc = mContext.getResources().getString(R.string.ngaytruoc);
		giotruoc = mContext.getResources().getString(R.string.giotruoc);
		phuttruoc = mContext.getResources().getString(R.string.phuttruoc);

		// image
		imageLoader = ImageLoader.getInstance();
		// Initialize ImageLoader with configuration. Do it once.
		imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
		// Load and display image asynchronously
		doption = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true)
				.showImageForEmptyUri(R.drawable.ic)
				.showImageOnFail(R.drawable.ic).cacheInMemory(true)
				.cacheOnDisc(true).build();

		imageLoader.clearMemoryCache();

		// doption = new DisplayImageOptions.Builder()
		// .resetViewBeforeLoading(true)
		// .bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
		// .cacheOnDisc(true).build();
		//
		// ImageLoaderConfiguration config = new
		// ImageLoaderConfiguration.Builder(getContext())
		// .defaultDisplayImageOptions(doption)
		// .memoryCache(new WeakMemoryCache()).build();
		//
		// ImageLoader.getInstance().init(config);

	}

	@Override
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

		// Log.d("TAG", "pos: + " + position);
		viewHolder.tvTitle.setText(mNews.get(position).getTitle());

		_day = Utility.getTime(mNews.get(position).getDatePost(), getContext());

		viewHolder.tvDate.setText(_day);

		imageLoader.displayImage(mNews.get(position).getFeatureImage(),
				viewHolder.icon, doption);

		// ImageLoader.getInstance().displayImage(mNews.get(position).getFeatureImage(),viewHolder.icon);

		return vi;

	}

	static class ViewHolder {
		public TextView tvTitle;
		public TextView tvDate;
		public ImageView icon;
	}
}