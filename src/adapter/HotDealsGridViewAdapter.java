package adapter;

import java.util.ArrayList;

import com.skyward101.tin365.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import object.HotDealsProduct;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HotDealsGridViewAdapter extends BaseAdapter {
	private ArrayList<HotDealsProduct> products;
	private Context mContext;
	private int resId;

	public static ImageLoader imageLoader;
	private DisplayImageOptions doption;

	public HotDealsGridViewAdapter(Context mContext, int resId,
			ArrayList<HotDealsProduct> products) {
		super();
		this.products = products;
		this.mContext = mContext;
		this.resId = resId;

		// Init ImageLoader
		imageLoader = ImageLoader.getInstance();
		// Initialize ImageLoader with configuration. Do it once.
		imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
		// Load and display image asynchronously
		doption = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic)
				.showImageOnFail(R.drawable.ic).cacheInMemory(true)
				.cacheOnDisc(true).build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.products.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int pos, View v, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View gridView;

		if (v == null) {

			gridView = new View(mContext);

			// get layout from mobile.xml
			gridView = inflater.inflate(this.resId, null);
		} else
			gridView = v;

		ImageView img = (ImageView) gridView.findViewById(R.id.hot_deals_img);
		TextView tvTitle = (TextView) gridView
				.findViewById(R.id.hot_deals_title);
		TextView tvPrice = (TextView) gridView
				.findViewById(R.id.hot_deals_price);
		TextView tvPricecompare = (TextView) gridView
				.findViewById(R.id.hot_deals_pricecompare);

		tvTitle.setText(products.get(pos).getTitle());
		tvPrice.setText(products.get(pos).getPrice());
		tvPricecompare.setText(products.get(pos).getPricecompare());
		tvPricecompare.setPaintFlags(tvPricecompare.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

		imageLoader.displayImage(products.get(pos).getImageurl(), img,doption);
		
		return gridView;
	}

}
