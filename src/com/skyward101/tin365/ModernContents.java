package com.skyward101.tin365;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import object.News_Content;
import se.emilsjolander.flipview.FlipView;
import se.emilsjolander.flipview.FlipView.OnFlipListener;
import se.emilsjolander.flipview.FlipView.OnOverFlipListener;
import se.emilsjolander.flipview.OverFlipMode;
import utility.Utility;
import adapter.TitleListView;
import android.Vietnalyze;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.skyward101.tin365.R;
import com.slidingmenu.lib.SlidingMenu;

public class ModernContents extends SherlockFragmentActivity implements
		OnFlipListener, OnOverFlipListener, OnScrollListener {

	FlipView flipView;
	LinearLayout llActionBar;

	public SlidingMenu menu;

	private LayoutInflater inflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions doption;
	private SharedPreferences sharedpreferences;
	private String mActionbar_color;
	private boolean isFirstOpen;
	private static final String SP_NAME = "MY_sharedpreferences";
	private static final int NUM_PER_PAGE = 15;

	public static int HEIGHT_OF_IMAGE; // 0.45 of Device's height
	public static int HEIGHT_OF_ACTIONBAR;

	// public static int HEIGHT_OF_STATUSBAR;

	public ArrayList<News_Content> contents;
	public static String cat;
	public int cur_pos;
	private TitleListView adapterListView;
	private ListView lvDetails;
	private boolean mLoading = false;
	private flipViewAdapter flipAdapter;
	// private AdView adView;
	String _day = "";

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Setting for transparent actionbar
		// requestWindowFeature(com.actionbarsherlock.view.Window.FEATURE_ACTION_BAR_OVERLAY);
		// getSupportActionBar().setBackgroundDrawable(null);

		setContentView(R.layout.activity_modern_contents);

		Intent intent = getIntent();
		cat = intent.getStringExtra("cat");
		contents = (ArrayList<News_Content>) intent
				.getSerializableExtra("contents");
		constant.Constants.log_d("TAG", " contents'size: " + contents.size());
		init();
		// initActionBar();
		initSlidingMenu();

		// Flipview
		// img = (ImageView) findViewById(R.id.img);
		flipView = (FlipView) findViewById(R.id.flip_view);

		flipAdapter = new flipViewAdapter();
		flipView.setAdapter(flipAdapter);

		flipView.setEmptyView(findViewById(R.id.empty_view));
		flipView.peakNext(true);
		flipView.setOverFlipMode(OverFlipMode.RUBBER_BAND);
		flipView.setOnFlipListener(this);
		flipView.setOnOverFlipListener(this);
		// Create an ad.
		// adView = (AdView) findViewById(R.id.adView);

		// Create an ad request. Check logcat output for the hashed device ID to
		// get test ads on a physical device.
		// AdRequest adRequest = new AdRequest.Builder()
		// .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
		// .addTestDevice("INSERT_YOUR_HASHED_DEVICE_ID_HERE").build();

		// Start loading the ad in the background.
		// adView.loadAd(adRequest);

	}

	private void init() {
		// Init ImageLoader
		imageLoader = ImageLoader.getInstance();
		// Initialize ImageLoader with configuration. Do it once.
		imageLoader.init(ImageLoaderConfiguration
				.createDefault(ModernContents.this));
		// Load and display image asynchronously
		doption = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).build();

		inflater = (LayoutInflater) ModernContents.this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		HEIGHT_OF_IMAGE = constant.Constants.DEVICE_HEIGHT * 45 / 100;

		// sharedpreferences
		sharedpreferences = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		mActionbar_color = sharedpreferences.getString("actionbar_color",
				constant.Constants.COLOR_ACTION_BAR);

		isFirstOpen = sharedpreferences.getBoolean("isFirstOpen", true);
	}

	@Override
	public void onBackPressed() {

		if (menu.isMenuShowing())
			menu.toggle();
		else
			super.onBackPressed();
	}

	private void initSlidingMenu() {
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menu.setShadowWidthRes(R.dimen.sliding_shadow_width);
		menu.setShadowDrawable(R.drawable.sliding_menu_shadow);
		menu.setBehindOffsetRes(R.dimen.sliding_behind_of_set);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT, true);
		menu.setMenu(R.layout.sliding_menu_details);
		menu.setSlidingEnabled(true);

		if (isFirstOpen) {
			menu.toggle();
			SharedPreferences.Editor edit = sharedpreferences.edit();
			edit.putBoolean("isFirstOpen", false);
			edit.commit();
		}
		// Set for xml menu
		lvDetails = (ListView) findViewById(R.id.lvDetailsMenu);
		adapterListView = new TitleListView(this, R.layout.title_item, contents);
		lvDetails.setAdapter(adapterListView);
		lvDetails.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				flipView.smoothFlipTo(pos);
				menu.toggle();
			}
		});
		lvDetails.setOnScrollListener(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (view.getLastVisiblePosition() >= (totalItemCount - 1)
				&& totalItemCount != totalItemCountOld) {
			totalItemCountOld = totalItemCount;
			checkLoadMore();
		}

	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	int page = 1;
	int totalItemCountOld = 0;

	private void checkLoadMore() {
		if (!mLoading && contents.size() > (NUM_PER_PAGE - 2)) {
			constant.Constants.log_d("TAG",
					"Modern Contents - check loadmore  -  clearMemoryCache");
			TitleListView.imageLoader.clearMemoryCache();

			mLoading = true;
			page += 1;
			String url = "http://news.ctyprosoft.com:8081/service.php?action=getArticleMainCategory&categoryID="
					+ contents.get(0).getMainCatID()
					+ "&numPerPage="
					+ NUM_PER_PAGE + "&pageNumber=" + page;
			if (!constant.Constants.isVietnamese)
				url = url + "&lang=en";
			constant.Constants.log_d("TAG",
					"Modern Contents - check loadmore  -  url: " + url);
			new LoadMore().execute(url);
		}
	}

	class LoadMore extends AsyncTask<String, String, String> {
		ProgressBar progressBar;

		public LoadMore() {
			progressBar = (ProgressBar) findViewById(R.id.progressBar1);
			progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(String... urls) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try {
				response = httpclient.execute(new HttpGet(urls[0]));
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					responseString = out.toString();
				} else {
					// Closes the connection.
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (ClientProtocolException e) {
				// TODO Handle problems..
			} catch (IOException e) {
				// TODO Handle problems..
			}
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			// contents = new ArrayList<News_Content>();
			try {
				JSONArray jsonArray = new JSONArray(result);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					int id = jsonObject.getInt("ID");
					int categoryID = jsonObject.getInt("categoryID");
					int mainCatID = jsonObject.getInt("mainCatID");
					int newestImageHeight = 0;
					int newestImageWidth = 0;

					String newestImageSize = jsonObject
							.getString("newestImageSize");
					constant.Constants.log_d("TAG", "newestImageSize: "
							+ newestImageSize);
					if (!newestImageSize.equals("null")) {
						String[] size = newestImageSize.split("x");
						newestImageWidth = Integer.parseInt(size[0]);
						newestImageHeight = Integer.parseInt(size[1]);
					}

					String title = jsonObject.getString("title");
					String url = jsonObject.getString("url");
					String datePost = jsonObject.getString("datePost");
					String excerpt = jsonObject.getString("excerpt");
					String featureImage = constant.Constants
							.editImageLink(jsonObject.getString("featureImage"));
					String newestImage = constant.Constants
							.editImageLink(jsonObject.getString("newestImage"));

					String websiteName = jsonObject.getString("websiteName");
					String websiteURL = jsonObject.getString("websiteURL");
					String websiteIcon = jsonObject.getString("websiteIcon");
					String websiteLogo = jsonObject.getString("websiteLogo");
					int numberComment = jsonObject.getInt("numberComment");

					News_Content content = new News_Content(id, categoryID,
							mainCatID, newestImageHeight, newestImageWidth,
							title, url, datePost, excerpt, featureImage,
							newestImage, websiteName, websiteURL, websiteIcon,
							websiteLogo, numberComment);

					contents.add(content);

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			adapterListView.notifyDataSetChanged();
			flipAdapter.notifyDataSetChanged();
			progressBar.setVisibility(View.GONE);
			mLoading = false;
			super.onPostExecute(result);
		}
	}

	private void initActionBar() {
		// configure action bar
		// action bar
		final ActionBar actionBar = getSupportActionBar();

		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);

		LayoutInflater inflator = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View v = inflator.inflate(
				R.layout.modern_contents_actionbar_layout, null);

		com.actionbarsherlock.app.ActionBar.LayoutParams params = new com.actionbarsherlock.app.ActionBar.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		actionBar.setCustomView(v, params);
		actionBar.setDisplayShowCustomEnabled(true);

		// View llActionbar = v.findViewById(R.id.llActionBar);
		// llActionbar.measure(MeasureSpec.UNSPECIFIED,
		// MeasureSpec.UNSPECIFIED);
		// HEIGHT_OF_ACTIONBAR = llActionbar.getMeasuredHeight();
		llActionBar = (LinearLayout) v.findViewById(R.id.llActionBar);
		TypedValue tv = new TypedValue();
		if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
			HEIGHT_OF_ACTIONBAR = TypedValue.complexToDimensionPixelSize(
					tv.data, getResources().getDisplayMetrics());
		}
		// HEIGHT_OF_STATUSBAR = getStatusBarHeight();

		((TextView) v.findViewById(R.id.textView1)).setText(cat);
		v.findViewById(R.id.llIcon).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	boolean onOverFlip = true;

	@Override
	public void onOverFlip(FlipView v, OverFlipMode mode,
			boolean overFlippingPrevious, float overFlipDistance,
			float flipDistancePerPage) {
		if (onOverFlip) {
			onOverFlip = false;
			constant.Constants.log_d("TAG", "End: " + mode.toString());
			// TODO
			checkLoadMore();
		}

	}

	@Override
	public void onFlippedToPage(FlipView v, int position, long id) {
		constant.Constants.log_d("TAG", "onFlippedToPage: " + position);
		cur_pos = position;
		if (!onOverFlip)
			onOverFlip = true;
	}

	/**
	 * Base Adapter use for flipView
	 */
	class flipViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return contents.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		View v;

		@Override
		public View getView(int pos, View convertView, ViewGroup parent) {
			constant.Constants.log_d("TAG", "Pos : " + pos);
			v = new View(ModernContents.this);
			final int p = pos;

			Log.d("TAG", "metric rate: " + showRateDpToPx());

			int width_img = (int) (showRateDpToPx() * contents.get(pos)
					.getNewestImageWidth());
			int height_img = (int) (showRateDpToPx() * contents.get(pos)
					.getNewestImageHeight());

			int width_device = constant.Constants.DEVICE_WIDTH;
			int height_device = constant.Constants.DEVICE_HEIGHT;

			float rate_width = (width_img * 1.0f) / width_device;
			float rate_height = (height_img * 1.0f) / height_device;

			_day = Utility.getTime(contents.get(pos).getDatePost(),
					ModernContents.this);
			if (height_img > width_img && rate_width > 0.8 && rate_height > 0.8)
				v = setLayoutv3(p);
			// else if (rate_width > 0.8 && rate_height > 0.4)
			// v = setLayoutv2(p);
			else
				v = setLayoutv1(p);

			return v;
		}

	}

	public View setLayoutv1(int pos) {
		constant.Constants.log_d("TAG", "setLayoutv1 - pos: " + pos);
		View v = null;
		v = inflater.inflate(R.layout.modern_contents_v1, null);

		FrameLayout v1_action_bar = (FrameLayout) v
				.findViewById(R.id.v1_action_bar);
		LinearLayout v1_action_bar_back = (LinearLayout) v
				.findViewById(R.id.v1_action_bar_back);
		TextView v1_tvSpinner = (TextView) v.findViewById(R.id.v1_tvSpinner);
		ImageView v1_main_image = (ImageView) v
				.findViewById(R.id.v1_main_image);
		TextView v1_title = (TextView) v.findViewById(R.id.v1_title);
		TextView v1_sub_title = (TextView) v.findViewById(R.id.v1_sub_title);
		LinearLayout v1_footer = (LinearLayout) v.findViewById(R.id.v1_footer);
		ImageView v1_source_icon = (ImageView) v
				.findViewById(R.id.v1_source_icon);
		TextView v1_source_name_and_date = (TextView) v
				.findViewById(R.id.v1_source_name_and_date);
		// TextView v1_comment = (TextView) v.findViewById(R.id.v1_comment);

		// Setting for footer
		imageLoader.displayImage(contents.get(pos).getWebsiteIcon(),
				v1_source_icon, doption);
		v1_source_name_and_date.setText(_day);

		// Setting for Actionbar
		v1_action_bar.setBackgroundColor(Color.parseColor(mActionbar_color));
		LayoutParams params = (LayoutParams) v1_action_bar.getLayoutParams();
		params.height = HEIGHT_OF_IMAGE * 2 / 9; // 0.1% of Deivce's Height
		// params.height = HEIGHT_OF_ACTIONBAR;// + HEIGHT_OF_STATUSBAR;

		// get color from shared preferences
		// v1_action_bar.setBackgroundColor(Color
		// .parseColor(Constants.color_Action_Bar));

		v1_tvSpinner.setText(cat);
		v1_action_bar_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		constant.Constants.log_d("TAG", "setLayoutv1 HEIGHT_OF_ACTIONBAR : "
				+ HEIGHT_OF_ACTIONBAR + " - param Actionbar height: "
				+ HEIGHT_OF_IMAGE * 2 / 9);

		// Setting for Main image
		// 0.35% of Device's Height
		v1_main_image.getLayoutParams().height = HEIGHT_OF_IMAGE * 7 / 9;
		// v1_main_image.getLayoutParams().height = HEIGHT_OF_IMAGE
		// - HEIGHT_OF_ACTIONBAR;
		imageLoader.displayImage(contents.get(pos).getNewestImage(),
				v1_main_image, doption);

		// Setting for title
		v1_title.setText(contents.get(pos).getTitle());
		v1_sub_title.setText(contents.get(pos).getExcerpt());
		constant.Constants.log_d("TAG", "setLayoutv1 - 3");
		// Setting for footer
		params = (LayoutParams) v1_footer.getLayoutParams();
		params.height = HEIGHT_OF_IMAGE * 8 / 45;// 0.8%
		// imageLoader.displayImage(contents.get(pos).g(), v1_source_icon,
		// doption);

		return v;
	}

	public View setLayoutv2(int pos) {
		constant.Constants.log_d("TAG", "setLayoutv2 - pos: " + pos);
		View v = null;
		v = inflater.inflate(R.layout.modern_contents_v2, null);

		FrameLayout v1_action_bar = (FrameLayout) v
				.findViewById(R.id.v2_action_bar);
		LinearLayout v1_action_bar_back = (LinearLayout) v
				.findViewById(R.id.v2_action_bar_back);
		TextView v1_tvSpinner = (TextView) v.findViewById(R.id.v2_tvSpinner);
		ImageView v1_main_image = (ImageView) v
				.findViewById(R.id.v2_main_image);
		TextView v1_title = (TextView) v.findViewById(R.id.v2_title);
		TextView v1_sub_title = (TextView) v.findViewById(R.id.v2_sub_title);
		LinearLayout v1_footer = (LinearLayout) v.findViewById(R.id.v2_footer);
		ImageView v1_source_icon = (ImageView) v
				.findViewById(R.id.v2_source_icon);
		TextView v1_source_name_and_date = (TextView) v
				.findViewById(R.id.v2_source_name_and_date);
		TextView v1_comment = (TextView) v.findViewById(R.id.v2_comment);

		// Setting for footer
		imageLoader.displayImage(contents.get(pos).getWebsiteIcon(),
				v1_source_icon, doption);
		v1_source_name_and_date.setText(_day);

		// Setting for Actionbar
		android.widget.FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) v1_action_bar
				.getLayoutParams();
		params.height = HEIGHT_OF_IMAGE * 2 / 9; // 0.1% of Deivce's Height
		// v1_action_bar.setBackgroundColor(Color
		// .parseColor(Constants.color_Action_Bar));
		v1_tvSpinner.setText(cat);
		v1_action_bar_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		// Setting for Main image
		// 0.35% of Device's Height
		v1_main_image.getLayoutParams().height = HEIGHT_OF_IMAGE;
		imageLoader.displayImage(contents.get(pos).getNewestImage(),
				v1_main_image, doption);

		// Setting for title
		v1_title.setText(contents.get(pos).getTitle());
		v1_sub_title.setText(contents.get(pos).getExcerpt());

		// Setting for footer
		android.widget.RelativeLayout.LayoutParams params1 = (android.widget.RelativeLayout.LayoutParams) v1_footer
				.getLayoutParams();
		params1.height = HEIGHT_OF_IMAGE * 8 / 45;// 0.8%
		// imageLoader.displayImage(contents.get(pos).g(), v1_source_icon,
		// doption);

		return v;
	}

	public View setLayoutv3(int pos) {
		constant.Constants.log_d("TAG", "setLayoutv3");
		// llActionBar.setBackgroundResource(R.drawable.bg_actionbar);
		View v = null;
		v = inflater.inflate(R.layout.modern_contents_v3, null);

		FrameLayout v1_action_bar = (FrameLayout) v
				.findViewById(R.id.v3_action_bar);
		LinearLayout v1_action_bar_back = (LinearLayout) v
				.findViewById(R.id.v3_action_bar_back);
		TextView v1_tvSpinner = (TextView) v.findViewById(R.id.v3_tvSpinner);
		ImageView v1_main_image = (ImageView) v
				.findViewById(R.id.v3_main_image);
		TextView v1_title = (TextView) v.findViewById(R.id.v3_title);
		LinearLayout v1_footer = (LinearLayout) v.findViewById(R.id.v3_footer);
		ImageView v1_source_icon = (ImageView) v
				.findViewById(R.id.v3_source_icon);
		TextView v1_source_name_and_date = (TextView) v
				.findViewById(R.id.v3_source_name_and_date);
		TextView v1_comment = (TextView) v.findViewById(R.id.v3_comment);

		// Setting for footer
		imageLoader.displayImage(contents.get(pos).getWebsiteIcon(),
				v1_source_icon, doption);
		v1_source_name_and_date.setText(_day);

		// Setting for Actionbar
		android.widget.FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) v1_action_bar
				.getLayoutParams();
		params.height = HEIGHT_OF_IMAGE * 2 / 9; // 0.1% of Deivce's Height
		// v1_action_bar.setBackgroundColor(Color
		// .parseColor(Constants.color_Action_Bar));
		v1_tvSpinner.setText(cat);
		v1_action_bar_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		// Setting for Main image
		imageLoader.displayImage(contents.get(pos).getNewestImage(),
				v1_main_image, doption);

		// Setting for title
		v1_title.setText(contents.get(pos).getTitle());

		// Setting for footer
		android.widget.RelativeLayout.LayoutParams params1 = (android.widget.RelativeLayout.LayoutParams) v1_footer
				.getLayoutParams();
		params1.height = HEIGHT_OF_IMAGE * 8 / 45;// 0.8%

		return v;
	}

	public void onClick(View v) {
		Vietnalyze.logEvent(cat);
		FlurryAgent.logEvent(cat);
		Intent i = new Intent(ModernContents.this, ModernDetails.class);
		i.putExtra("articleID", contents.get(cur_pos).getID());
		if (Utility.isInternet(this))
			startActivity(i);
		else
			Utility.showDialog(this,
					getResources().getString(R.string.internetConnection));

	}

	private float showRateDpToPx() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.density;
	}

	//
	// public int getStatusBarHeight() {
	// int result = 0;
	// int resourceId = getResources().getIdentifier("status_bar_height",
	// "dimen", "android");
	// if (resourceId > 0) {
	// result = getResources().getDimensionPixelSize(resourceId);
	// }
	// return result;
	// }

}