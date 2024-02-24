package com.skyward101.tin365;

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

import adapter.TitleListView;
import adapter.TraditionAdapterDetails;
import android.Vietnalyze;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.flurry.android.FlurryAgent;
import com.slidingmenu.lib.SlidingMenu;
import com.sromku.simple.fb.SimpleFacebook;

public class TraditionDetailsActivity extends SherlockFragmentActivity
		implements OnScrollListener {

	Context context = this;
	private ActionBar actionBarDetails;
	public static ViewPager viewPagerDetails;
	private SlidingMenu menuDetails;

	public static boolean isCall;
	public static int pos_news;
	public static int page_cur_details;
	public static ArrayList<News_Content> news;
	public static String cat_details;

	public static SharedPreferences sharedpreferences_Tradition;
	public static final String SP_NAME_TRADITION = "MY_sharedpreferences";
	public static int mFontSize_Tradition;
	public static String mActionbar_color;

	public static Activity TraditionDetailsActivity;

	public int articleID;
	private ProgressBar progressBar;
	String articalID = "";
	String _cmt_count = "";
	TextView tv;
	String apiCountCmtbyArticle = "http://news.ctyprosoft.com:8081/service.php?action=getCountComment&articleID=";

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tradition_details);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		Intent intent = getIntent();
		isCall = true;
		pos_news = intent.getIntExtra("pos", 0);
		page_cur_details = intent.getIntExtra("page", 1);
		constant.Constants.log_d("TAG", "Intent page_cur_details: "
				+ page_cur_details);
		// cat_details = intent.getStringExtra("cat");
		news = (ArrayList<News_Content>) intent.getSerializableExtra("news");
		articalID = String.valueOf(news.get(pos_news).getID());
		// get cmt number from api
		// _cmt_count = GetJsonFromAPI.getCountCmt(apiCountCmtbyArticle
		// + articalID);
		init();
		initActionBar();
		initSlidingMenu();

		constant.Constants.log_d("TAG", "pos: " + pos_news);
		constant.Constants.log_d("TAG", "Size of news: " + news.size());

		constant.Constants.log_d("TAG", news.get(pos_news).getTitle());

		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		viewPagerDetails = (ViewPager) findViewById(R.id.pager);

		viewPagerDetails.setOffscreenPageLimit(2);
		viewPagerDetails.setOnPageChangeListener(onPageChangeListener);
		viewPagerDetails.setAdapter(new TraditionAdapterDetails(
				getSupportFragmentManager()));

	}

	@Override
	public void onDestroy() {
		isActivityDestroy = true;
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("Flurry", "onStop");
		FlurryAgent.onEndSession(this);
		// your code

		Vietnalyze.onEndSession(this);
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

	// handler for received Intents for the "my-event" event
	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// Extract data included in the Intent
			_cmt_count = intent.getStringExtra("cmt");
			tv.setText(_cmt_count);
		}
	};

	private void initActionBar() {
		// configure action bar
		// action bar
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setIcon(R.drawable.ic_launcher);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);

		// actionBar.setTitle(ModernContents.cat);
		actionBar.setTitle(Html.fromHtml("<font color=\"white\">"
				+ TraditionNews.cat + "</font>"));
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor(mActionbar_color)));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		/*
		 * menu.add(0, 1, 0, "TEXT" ) .setIcon(R.drawable.icon_comment)
		 * .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		 */
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.add_cmt, menu);

		RelativeLayout badgeLayout = (RelativeLayout) menu.findItem(R.id.badge)
				.getActionView();

		ImageView img = (ImageView) badgeLayout.findViewById(R.id.click);
		img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Vietnalyze.logEvent(getResources().getString(
						R.string.eventcmtArticle));
				FlurryAgent.logEvent(getResources().getString(
						R.string.eventcmtArticle));
				Intent i = new Intent(TraditionDetailsActivity.this,
						CommentActivity.class);
				i.putExtra("articleID", articalID);
				if (Utility.isInternet(context))
					startActivityForResult(i, 1);
				else
					Utility.showDialog(
							context,
							getResources().getString(
									R.string.internetConnection));

			}
		});
		tv = (TextView) badgeLayout
				.findViewById(R.id.actionbar_notifcation_textview);
		// tv.setText(_cmt_count);
		tv.setText(String.valueOf(news.get(0).getNumberComment()));
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		SimpleFacebook.getInstance(this).onActivityResult(this, arg0, arg1,
				arg2);
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0 == 1) {
			String message = arg2.getStringExtra("MESSAGE");
			tv.setText(message);

		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.badge:
			Intent i = new Intent(TraditionDetailsActivity.this,
					CommentActivity.class);
			i.putExtra("articleID", String.valueOf(pos_news));
			startActivity(i);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public static Context TraditionDetailsContext;

	private void init() {
		TraditionDetailsActivity = TraditionDetailsActivity.this;
		TraditionDetailsContext = this;

		// sharedpreferences
		sharedpreferences_Tradition = getSharedPreferences(SP_NAME_TRADITION,
				Context.MODE_PRIVATE);
		mActionbar_color = sharedpreferences_Tradition.getString(
				"actionbar_color", constant.Constants.COLOR_ACTION_BAR);

		mFontSize_Tradition = sharedpreferences_Tradition.getInt("font_size",
				constant.Constants.FONT_SIZE);
	}

	private ViewPager.SimpleOnPageChangeListener onPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
		@Override
		public void onPageSelected(final int position) {
			super.onPageSelected(position);
			// if(news != null && tv != null)
			// tv.setText(news.get(position).getNumberComment());
			runOnUiThread(new Runnable() {
				public void run() {
					tv.setText(String.valueOf(news.get(position)
							.getNumberComment()));
				}
			});
			articalID = String.valueOf(news.get(position).getID());
			constant.Constants.log_d("TAG",
					"onPageSelected: " + news.get(position).getNumberComment());
			Vietnalyze.logEvent(TraditionNews.cat);
			FlurryAgent.logEvent(TraditionNews.cat);

		}
	};
	private TitleListView adapterListView;
	private ListView lvDetails;

	@Override
	public void onBackPressed() {

		if (menuDetails.isMenuShowing())
			menuDetails.toggle();
		else
			super.onBackPressed();
	}

	private void initSlidingMenu() {
		menuDetails = new SlidingMenu(this);
		menuDetails.setMode(SlidingMenu.LEFT);
		menuDetails.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menuDetails.setShadowWidthRes(R.dimen.sliding_shadow_width);
		menuDetails.setShadowDrawable(R.drawable.sliding_menu_shadow);
		menuDetails.setBehindOffsetRes(R.dimen.sliding_behind_of_set);
		menuDetails.setFadeDegree(0.35f);
		menuDetails.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menuDetails.setMenu(R.layout.sliding_menu_details);
		menuDetails.setSlidingEnabled(true);

		// Set for xml menu
		lvDetails = (ListView) findViewById(R.id.lvDetailsMenu);
		adapterListView = new TitleListView(this, R.layout.title_item, news);
		lvDetails.setAdapter(adapterListView);
		lvDetails.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				viewPagerDetails.setCurrentItem(pos, true);
				menuDetails.toggle();
			}
		});

		lvDetails.setOnScrollListener(this);

		pos_cat = SplashActivity.cats.indexOf(TraditionNews.cat);
	}

	// int i = page_cur_details;
	private final int NUM_PER_PAGE = 15;
	int pos_cat;
	private boolean mLoading = false;
	private String url = "http://news.ctyprosoft.com:8081/service.php?action=getArticleMainCategory&categoryID="
			+ TraditionNews.catId
			+ "&numPerPage="
			+ NUM_PER_PAGE
			+ "&pageNumber=";
	private boolean isActivityDestroy = false;

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

	private void checkLoadMore() {
		if (!mLoading && news.size() > (NUM_PER_PAGE - 2)) {
			TitleListView.imageLoader.clearMemoryCache();
			constant.Constants.log_d("TAG", "Loading more -clear memory");

			progressBar.setVisibility(View.VISIBLE);
			mLoading = true;

			page_cur_details += 1;
			constant.Constants.log_d("TAG", "url: " + url + page_cur_details);
			// constant.Constants.log_d("TAG", "pos_cat: " + pos_cat);
			// constant.Constants.log_d("TAG", "TraditionNews.cat: "
			// + TraditionNews.cat);
			// constant.Constants.log_d("TAG", "pos_cat size: "
			// + SplashActivity.cats.size());
			//
			if (!constant.Constants.isVietnamese)
				new LoadMore().execute(url + page_cur_details + "&lang=en");
			else
				new LoadMore().execute(url + page_cur_details);
		}
	}

	int totalItemCountOld = 0;

	class LoadMore extends AsyncTask<String, String, String> {

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
			makeContents(responseString);
			if (isActivityDestroy)
				this.cancel(true);
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			// if (isFirst) {
			// isFirst = false;
			// contents.add(0, contentsAtZero);
			// }

			adapterListView.notifyDataSetChanged();
			// flipAdapter.notifyDataSetChanged();
			progressBar.setVisibility(View.GONE);
			mLoading = false;
			lvDetails.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int pos, long arg3) {
					viewPagerDetails.setCurrentItem(pos, true);
					menuDetails.toggle();
				}
			});
			super.onPostExecute(result);
		}

	}

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
				String featureImage = jsonObject.getString("featureImage");
				String newestImage = jsonObject.getString("newestImage");
				String websiteName = jsonObject.getString("websiteName");
				String websiteURL = jsonObject.getString("websiteURL");
				String websiteIcon = jsonObject.getString("websiteIcon");
				String websiteLogo = jsonObject.getString("websiteLogo");
				int numberComment = jsonObject.getInt("numberComment");

				News_Content content = new News_Content(id, categoryID,
						mainCatID, newestImageHeight, newestImageWidth, title,
						url, datePost, excerpt, featureImage, newestImage,
						websiteName, websiteURL, websiteIcon, websiteLogo,
						numberComment);

				news.add(content);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}
