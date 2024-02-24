package com.skyward101.tin365;

import java.util.ArrayList;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.flurry.android.FlurryAgent;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.skyward101.tin365.R;

import object.Bookmark;
import object.News_Content;
import object.News_Detail;
import utility.DataBaseHelper;
import utility.GetJsonFromAPI;
import utility.Utility;
import adapter.Bongda24hAdapter;
import adapter.BookmarkAdapter;
import adapter.ListVideoGoalAdapter;
import android.Vietnalyze;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Bongda24hDetailActivity extends SherlockFragmentActivity implements
		OnScrollListener, OnItemClickListener {
	Context context = this;
	// variable listview
	ListView lv_bookmark;
	private ImageLoader imageLoader;
	private DisplayImageOptions doption;
	public static String mActionbar_color;
	Bookmark object_bookmark;
	ListVideoGoalAdapter adapter_bookmark;
	ArrayList<News_Content> array_bookmark;
	ArrayList<String> array_url_video;
	ImageView img_delete;
	// parse page
	private int currentPage = 1;
	private int positionListView = 0;
	private static int totalPage = 0;
	// dialog
	Dialog dialog;
	// database
	DataBaseHelper db;
	// action bar
	ActionBar actionBar;
	// share
	private SharedPreferences sharedpreferences;
	private static final String SP_NAME = "MY_sharedpreferences";
	private int mFontSize;
	String artical = "";
	private TextView tvDate;
	private TextView tvTitle;
	private TextView tvSourceName;
	private ImageView tvSourceIcon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_football_video);

		initListview();
		init();
		initActionBar();
		new bg_getBookmark().execute();
	}

	@SuppressLint("NewApi")
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

		// actionBar.setTitle(ModernContents.cat);
		// actionBar.setTitle(Html.fromHtml("<font color=\"white\">" + "abc"
		// + "</font>"));
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor(mActionbar_color)));
		// HEIGHT_OF_STATUSBAR = getStatusBarHeight();

		((TextView) v.findViewById(R.id.textView1)).setText(getResources()
				.getString(R.string.title_activity_football));
		v.findViewById(R.id.llIcon).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	private void init() {
		sharedpreferences = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		mActionbar_color = sharedpreferences.getString("actionbar_color",
				constant.Constants.COLOR_ACTION_BAR);

		mFontSize = sharedpreferences.getInt("font_size",
				constant.Constants.FONT_SIZE);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvDate = (TextView) findViewById(R.id.tvDate);
		tvSourceName = (TextView) findViewById(R.id.tvSourceName);
		tvSourceIcon = (ImageView) findViewById(R.id.imgSourceIcon);

		// Init ImageLoader
		imageLoader = ImageLoader.getInstance();
		// Initialize ImageLoader with configuration. Do it once.
		imageLoader.init(ImageLoaderConfiguration
				.createDefault(Bongda24hDetailActivity.this));
		// Load and display image asynchronously
		doption = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).build();
	}

	private void initListview() {
		lv_bookmark = (ListView) findViewById(R.id.lv_cmt);
		lv_bookmark.setOnScrollListener(this);

		// dialog
		dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.waitingdialog);
		dialog.setCancelable(false);
		// databse
		array_bookmark = new ArrayList<News_Content>();
		array_url_video = new ArrayList<String>();
		Bundle bundle = getIntent().getExtras();
		artical = bundle.getString("artical");
	}

	int firstVisiblePosition;
	int visibleItem;
	int totalItem;

	@Override
	public void onScroll(AbsListView arg0, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		System.out.println(firstVisibleItem + "  " + visibleItemCount + "  "
				+ totalItemCount);
		firstVisiblePosition = firstVisibleItem;
		totalItem = totalItemCount;
		visibleItem = visibleItemCount;
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int scrollState) {
		// TODO Auto-generated method stub
		boolean flag = (currentPage == totalPage + 1);
		System.out.println(currentPage + " " + totalPage + "  " + flag);
		if (firstVisiblePosition == (totalItem - visibleItem)
				&& scrollState == 0 && flag == false) {
			currentPage++;
			System.out.println("page " + currentPage);
			// getAndShowData();
			new bg_getBookmark().execute();
			positionListView = totalItem - visibleItem;
		}
	}

	class bg_getBookmark extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog.show();
			array_bookmark.removeAll(array_bookmark);
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			array_bookmark = GetJsonFromAPI
					.getListVideodetail("http://news.ctyprosoft.com:8081/service.php?action=getArticleDetail&articleID="
							+ artical);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			String[] video = array_bookmark.get(0).getUrl()
					.split("<1><1>###<1>");
			String temp = "";
			for (int j = 0; j < video.length; j++) {

				if (j == 0) {
					temp = video[j].replace("###<1>", "");
					temp = temp.replace("<1><1>###", "");

				} else if (j == video.length - 1)
					temp = video[j].replace("<1><1>###", "");
				else
					temp = video[j];
				array_url_video.add(temp);
			}
			adapter_bookmark = new ListVideoGoalAdapter(context,
					array_url_video);
			lv_bookmark.setAdapter(adapter_bookmark);
			lv_bookmark.setOnItemClickListener(Bongda24hDetailActivity.this);
			lv_bookmark.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					lv_bookmark.setSelection(positionListView);
					lv_bookmark.requestFocus();
				}
			});

			tvTitle.setText(array_bookmark.get(0).getTitle());
			tvSourceName.setText("- " + array_bookmark.get(0).getWebsiteName());
			// init Day ago
			String time = "";
			int day = Integer.parseInt(Utility.getDayAgo(array_bookmark.get(0)
					.getDatePost()));
			if (Utility.getDayAgo(array_bookmark.get(0).getDatePost()).equals(
					"0")) {
				if (Utility.getHourAgo(array_bookmark.get(0).getDatePost())
						.equals("0"))
					time = Utility.getMinuteAgo(array_bookmark.get(0)
							.getDatePost()) + getResources().getString(R.string.minuteago);
				else
					time = Utility.getHourAgo(array_bookmark.get(0)
							.getDatePost()) + getResources().getString(R.string.hourago);
			} else if (day < 31)
				time = Utility.getDayAgo(array_bookmark.get(0).getDatePost())
						+ getResources().getString(R.string.dayago);
			else
				time = array_bookmark.get(0).getDatePost();
			// init Day ago
			tvDate.setText(time);
			imageLoader.displayImage(array_bookmark.get(0).getWebsiteIcon(),
					tvSourceIcon, doption);
			// cancel Dialog
			dialog.cancel();
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		/*
		 * img_delete = (ImageView) arg0.findViewById(R.id.img_delete);
		 * img_delete.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub Toast.makeText(context, "abc", 400).show(); } });
		 */
		// String video = String.valueOf(array_url_video.get(arg2));
		Vietnalyze.logEvent(getResources().getString(
				R.string.eventwatchVideoGoal));
		FlurryAgent.logEvent(getResources().getString(
				R.string.eventwatchVideoGoal));
		if(Utility.isInternet(context))
		{
			Intent it = new Intent(context, VideoViewActivity.class);
			// it.putExtra("path", video);
			it.putExtra("pos", arg2);
			it.putStringArrayListExtra("array", array_url_video);
			startActivity(it);
		}
		else
			Utility.showDialog(context, getResources().getString(R.string.internetConnection));
		
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
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("Flurry", "onStop");
		FlurryAgent.onEndSession(this);
		// your code

		Vietnalyze.onEndSession(this);
	}

}
