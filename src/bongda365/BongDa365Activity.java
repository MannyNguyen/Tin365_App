package bongda365;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import object.News_Content;

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

import utility.Utility;
import view.GridViewHeader;
import adapter.Bongda365GridViewAdapter;
import android.Vietnalyze;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.flurry.android.FlurryAgent;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.skyward101.tin365.Bongda24hActivity;
import com.skyward101.tin365.R;
import com.slidingmenu.lib.SlidingMenu;

public class BongDa365Activity extends SherlockFragmentActivity implements
		OnClickListener {

	private ActionBar actionBar;
	private SlidingMenu menu_bongda365;
	private GridViewHeader gridView;

	private ArrayList<News_Content> bongda365GridItems;
	private Bongda365GridViewAdapter bongda365GridViewAdapter;
	private ProgressBar progressBarWaiting;
	private ProgressBar progressBarLoadMore;

	public static ImageLoader imageLoader_BongDa365;
	private DisplayImageOptions doption;

	int totalItemCountOld = 0;
	private boolean loadingMore = false;
	int page = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bong_da365);

		progressBarWaiting = (ProgressBar) findViewById(R.id.progressBarWaiting);
		progressBarLoadMore = (ProgressBar) findViewById(R.id.progressBarLoadMore);

		init();
		initActionBar();
		//initSlidingMenu();

	}

	private void init() {
		/**
		 * Setting for ImageLoader
		 */
		// Init ImageLoader
		imageLoader_BongDa365 = ImageLoader.getInstance();
		// Initialize ImageLoader with configuration. Do it once.
		imageLoader_BongDa365.init(ImageLoaderConfiguration
				.createDefault(BongDa365Activity.this));
		// Load and display image asynchronously
		doption = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic)
				.showImageOnFail(R.drawable.ic).cacheInMemory(true)
				.cacheOnDisc(true).build();

		newGridView();
	}

	/**
	 * To create grid view
	 */
	private void newGridView() {
		bongda365GridItems = new ArrayList<News_Content>();
		bongda365GridViewAdapter = new Bongda365GridViewAdapter(this,
				bongda365GridItems, R.layout.bongda365_item_grid);

		gridView = (GridViewHeader) findViewById(R.id.bongda365_gridview_header);
		gridView.setAdapter(bongda365GridViewAdapter);

		new asyTaskGetItem().execute(selectLink(1));
	}

	private static boolean isNews = true;

	/**
	 * We have a adapter but two link , a link get news and a link get video so
	 * we must choose correct link to get data into array Get news if isNews =
	 * true otherwise get video
	 * 
	 * @param page
	 */
	private String selectLink(int cur_page) {
		if (isNews) {
			String news_url = "http://news.ctyprosoft.com:8081/service.php?action=getArticleMainCategory&categoryID=31&numPerPage=15&pageNumber="
					+ cur_page;
			if(!constant.Constants.isVietnamese) news_url = news_url + "&lang=en";
			return news_url;
		} else {
			String video_url = "http://news.ctyprosoft.com:8081/service.php?action=getArticleMainCategory&categoryID=31&numPerPage=15&pageNumber="
					+ cur_page + "&type=video";
			if(!constant.Constants.isVietnamese) video_url = video_url + "&lang=en";
			return video_url;
		}
	}

	@Override
	public void onBackPressed() {

//		if (menu_bongda365.isMenuShowing())
//			menu_bongda365.toggle();
//		else
			super.onBackPressed();
	}

	private void initSlidingMenu() {
		menu_bongda365 = new SlidingMenu(this);
		menu_bongda365.setMode(SlidingMenu.RIGHT);
		menu_bongda365.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menu_bongda365.setShadowWidthRes(R.dimen.sliding_shadow_width);
		menu_bongda365.setShadowDrawable(R.drawable.sliding_menu_shadow_right);
		menu_bongda365
				.setBehindOffsetRes(R.dimen.hot_deals_sliding_behind_of_set);
		menu_bongda365.setFadeDegree(0.35f);
		menu_bongda365.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu_bongda365.setMenu(R.layout.sliding_bongda365);
		menu_bongda365.setSlidingEnabled(true);

		/**
		 * Set focus for default item
		 */
		if (isNews)
			findViewById(R.id.bongda365_tintucworldcup2014)
					.setBackgroundResource(
							R.drawable.bongda365_actionbar_gradient);
		else
			findViewById(R.id.bongda365_thuvienvideo).setBackgroundResource(
					R.drawable.bongda365_actionbar_gradient);

		findViewById(R.id.bongda365_tintucworldcup2014)
				.setOnClickListener(this);
		findViewById(R.id.bongda365_doihinh).setOnClickListener(this);
		findViewById(R.id.bongda365_lichthidauketqua).setOnClickListener(this);
		findViewById(R.id.bongda365_thuvienvideo).setOnClickListener(this);
		findViewById(R.id.bongda365_tyle).setOnClickListener(this);
	}

	private void initActionBar() {
		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);

		LayoutInflater inflator = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View v = inflator.inflate(R.layout.actionbar_bongda365, null);

		com.actionbarsherlock.app.ActionBar.LayoutParams params = new com.actionbarsherlock.app.ActionBar.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		actionBar.setCustomView(v, params);
		actionBar.setDisplayShowCustomEnabled(true);

		if (!isNews)
			((TextView) v.findViewById(R.id.tvSpinner)).setText(getResources()
					.getString(R.string.bongda365_thuvienVideo));
//		v.findViewById(R.id.img_cat).setOnClickListener(this);
		v.findViewById(R.id.img_cat).setVisibility(View.GONE);
		v.findViewById(R.id.llIcon).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.img_cat:
			//menu_bongda365.toggle();
			break;
		case R.id.llIcon:
			finish();
			break;
		case R.id.bongda365_tintucworldcup2014:
			isNews = true;
			page = 1;
			bongda365GridItems.clear();
			resetActivity();
			setFocusItemSlidingMenu(R.id.bongda365_tintucworldcup2014);
			break;
		case R.id.bongda365_lichthidauketqua:
			setFocusItemSlidingMenu(R.id.bongda365_lichthidauketqua);
			Intent i = new Intent(BongDa365Activity.this,
					BongDaDetailsActivity.class);
			i.putExtra("articleID", 59621);
			startActivity(i);

			break;
		case R.id.bongda365_thuvienvideo:
//			isNews = false;
//			page = 1;
//			bongda365GridItems.clear();
//			resetActivity();
			startActivity(new Intent(BongDa365Activity.this,ThuVienVideo.class));
			
			setFocusItemSlidingMenu(R.id.bongda365_thuvienvideo);
			break;

		case R.id.bongda365_tyle:
			setFocusItemSlidingMenu(R.id.bongda365_tyle);
			Intent i2 = new Intent(BongDa365Activity.this,
					BongDaDetailsActivity.class);
			i2.putExtra("articleID", 59622);
			startActivity(i2);
			break;

		case R.id.bongda365_doihinh:
			setFocusItemSlidingMenu(R.id.bongda365_doihinh);
			Intent i3 = new Intent(BongDa365Activity.this,
					BongDaDetailsActivity.class);
			i3.putExtra("articleID", 59623);
			startActivity(i3);
			break;

		default:
			break;
		}
	}

	/**
	 * To set focus for item of Sliding menu
	 * 
	 * @param id
	 *            : id of item linear layout
	 */
	private void setFocusItemSlidingMenu(int id) {
		menu_bongda365.toggle();
		resetBackgroundItemSlidingMenu();
		findViewById(id).setBackgroundResource(
				R.drawable.bongda365_actionbar_gradient);
	}

	/**
	 * To set default background of item in sliding menu
	 */
	private void resetBackgroundItemSlidingMenu() {
		int[] ids = { R.id.bongda365_doihinh, R.id.bongda365_lichthidauketqua,
				R.id.bongda365_thuvienvideo, R.id.bongda365_tintucworldcup2014,
				R.id.bongda365_tyle };

		for (int i = 0; i < ids.length; i++)
			findViewById(ids[i]).setBackgroundColor(Color.TRANSPARENT);
	}

	@Override
	protected void onDestroy() {
		isActivityDestroy = true;
		super.onDestroy();
	}

	private boolean isActivityDestroy = false;
	private String _day = "";

	/**
	 * AsyTask to get Categories List from server
	 * 
	 * @author My PC
	 * 
	 */
	class asyTaskGetItem extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... uri) {
			String responseString = doBackGround(uri[0]);
			if (isActivityDestroy)
				this.cancel(true);
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			initBongda365ArrayList(result);

			constant.Constants.log_d("TAG",
					"size: " + bongda365GridItems.size());
			constant.Constants.log_d("TAG", "page1: " + page);

			/**
			 * To set Header for the first time
			 */
			if (page == 1 && bongda365GridItems.size() > 0) {
				constant.Constants.log_d("TAG", "set Header");
				addHeaderForGridView();
			}

			bongda365GridViewAdapter.notifyDataSetChanged();

			if (progressBarWaiting != null)
				progressBarWaiting.setVisibility(View.GONE);
			if (progressBarLoadMore != null)
				progressBarLoadMore.setVisibility(View.GONE);

			/**
			 * Set on item click of Grid view
			 */
			gridView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View v, int pos,
						long id) {
					Intent i = new Intent(BongDa365Activity.this,
							BongDaDetailsActivity.class);
					i.putExtra("articleID", bongda365GridItems.get(pos).getID());
					startActivity(i);
				}
			});

			// bongda365GridViewAdapter = new
			// Bongda365GridViewAdapter(BongDa365Activity.this,
			// bongda365GridItems, R.layout.bongda365_item_grid);
			// gridView.setAdapter(bongda365GridViewAdapter);

			gridView.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view,
						int scrollState) {
				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {

					int lastInScreen = firstVisibleItem + visibleItemCount;

					if (totalItemCount != 0 && (lastInScreen == totalItemCount)
							&& !loadingMore
							&& totalItemCount != totalItemCountOld) {

						totalItemCountOld = totalItemCount;

						constant.Constants.log_d("TAG", "Load More = true");
						loadingMore = true;

						// Clear Memory cache of ImageLoader
						imageLoader_BongDa365.clearMemoryCache();

						// Hide Load More progress bar
						progressBarLoadMore.setVisibility(View.VISIBLE);
						page += 1;
						constant.Constants.log_d("TAG", "page2: " + page);
						new asyTaskGetItem().execute(selectLink(page));
					}
				}
			});
			loadingMore = false;

		}

		/**
		 * This is method to make Array list of bongda365
		 * 
		 * @param result
		 */
		private void initBongda365ArrayList(String result) {
			makeContents(result);
		}

		boolean isOk = false; // return true if the first item has newestImage

		private void makeContents(String result) {
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
					// constant.Constants.log_d("TAG", "newestImageSize: " +
					// newestImageSize);
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

					if (!isOk
							&& (!newestImage.equals("null") || !featureImage
									.equals("null"))) {
						isOk = true;
						header = content;
						// bongda365GridItems.add(content);

					} else {
						if (isOk) {
							bongda365GridItems.add(content);
						}
					}

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		/**
		 * This is background method to get data from api
		 * 
		 * @param url
		 * @return
		 */
		private String doBackGround(String url) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try {
				response = httpclient.execute(new HttpGet(url));
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

	}

	String ngaytruoc, giotruoc, phuttruoc;
	News_Content header;

	/**
	 * To add Header for Grid View in the first time
	 */
	private void addHeaderForGridView() {
		View header = View.inflate(BongDa365Activity.this,
				R.layout.bongda365_header, null);
		gridView.addHeaderView(header);

		ImageView imgView = (ImageView) header.findViewById(R.id.img);
		TextView title = (TextView) header.findViewById(R.id.tvTitle);
		TextView date = (TextView) header.findViewById(R.id.tvDate);

		ngaytruoc = getResources().getString(R.string.ngaytruoc);
		giotruoc = getResources().getString(R.string.giotruoc);
		phuttruoc = getResources().getString(R.string.phuttruoc);

		String dd = BongDa365Activity.this.header.getDatePost();

		int day = Integer.parseInt(Utility.getDayAgo(dd));
		if (Utility.getDayAgo(dd).equals("0")) {
			if (Utility.getHourAgo(dd).equals("0"))
				_day = Utility.getMinuteAgo(dd) + phuttruoc;
			else
				_day = Utility.getHourAgo(dd) + giotruoc;
		} else if (day < 31)
			_day = Utility.getDayAgo(dd) + ngaytruoc;
		else
			_day = dd;

		title.setText(BongDa365Activity.this.header.getTitle());
		date.setText(_day);

		if (!BongDa365Activity.this.header.getNewestImage().equals("null"))
			imageLoader_BongDa365.displayImage(
					BongDa365Activity.this.header.getNewestImage(), imgView,
					doption);
		else
			imageLoader_BongDa365.displayImage(
					BongDa365Activity.this.header.getFeatureImage(), imgView,
					doption);

		// TODO set onclick for header
		imgView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(BongDa365Activity.this,
						BongDaDetailsActivity.class);
				// i.putExtra("articleID", bongda365GridItems.get(0).getID());
				i.putExtra("articleID", BongDa365Activity.this.header.getID());
				constant.Constants.log_d("TAG", "articleId : "
						+ BongDa365Activity.this.header.getID());
				startActivity(i);
			}
		});

		// remove header position
		// bongda365GridItems.remove(0);
	}

	/**
	 * To run onCreate again
	 */
	private void resetActivity() {
		Intent intent = getIntent();
		overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		overridePendingTransition(0, 0);
		startActivity(intent);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i("Flurry", "onStart second");
		FlurryAgent.onStartSession(this, "YD9XG88S3BBSHG42P5HD");
		FlurryAgent.setLogEnabled(true);
		FlurryAgent.setLogEvents(true);
		FlurryAgent.setLogLevel(Log.VERBOSE);
		// your code

		// Vietnalyze
		Vietnalyze.onStartSession(this, "ZW9SHTZ4PYX5UI7P4FVG");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i("Flurry", "onStop");
		FlurryAgent.onEndSession(this);
		// your code

		Vietnalyze.onEndSession(this);
	}
}
