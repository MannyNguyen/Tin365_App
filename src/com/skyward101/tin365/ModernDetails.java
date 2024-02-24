package com.skyward101.tin365;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import object.Bookmark;
import object.EachVideoBongDa365;
import object.News_Detail;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import utility.DataBaseHelper;
import utility.GetJsonFromAPI;
import utility.ScrollViewExt;
import utility.SharePreferance;
import utility.Utility;
import utility.ScrollViewExt.ScrollViewListener;

import android.Vietnalyze;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.Session;
import com.flurry.android.FlurryAgent;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebook.OnLoginListener;
import com.sromku.simple.fb.SimpleFacebook.OnProfileRequestListener;
import com.sromku.simple.fb.SimpleFacebook.OnPublishListener;
import com.sromku.simple.fb.entities.Feed;
import com.sromku.simple.fb.entities.Profile;

@SuppressLint("NewApi")
public class ModernDetails extends SherlockFragmentActivity implements
		OnClickListener, ScrollViewListener, OnTouchListener {

	private boolean fromNotification = false;

	private SharedPreferences sharedpreferences;
	private static final String SP_NAME = "MY_sharedpreferences";

	public static String mActionbar_color;

	public int articleID;
	private ImageLoader imageLoader;
	private DisplayImageOptions doption;
	private View progress;
	private LinearLayout llDetails;
	private TextView tvDate;
	private TextView tvTitle;
	private News_Detail details;
	private TextView tvSourceName;
	private ImageView tvSourceIcon;
	private LinearLayout footerSetting;
	private ImageView footerImgShowSetting;
	private int mFontSize;
	// variable bookmark
	private ImageView img_bookmark, img_shareFB;
	DataBaseHelper db;
	Context context = this;
	Bookmark object_bookmark;
	private SimpleFacebook mSimpleFacebook;
	public Dialog waitingDialog;
	// private AdView adView;
	String _day = "";
	String _cmt_count = "";
	TextView tv;
	String apiCountCmtbyArticle = "http://news.ctyprosoft.com:8081/service.php?action=getCountComment&articleID=";

	private TextView tvExcerpt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modern_details);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		Intent intent = getIntent();
		try {
			fromNotification = intent
					.getBooleanExtra("fromNotification", false);
		} catch (Exception e) {
			fromNotification = false;
		}
		articleID = intent.getIntExtra("articleID", 0);
		// get cmt number from api
		_cmt_count = GetJsonFromAPI.getCountCmt(apiCountCmtbyArticle
				+ articleID);
		llDetails = (LinearLayout) findViewById(R.id.llDetails);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvExcerpt = (TextView) findViewById(R.id.tvExcerpt);
		tvDate = (TextView) findViewById(R.id.tvDate);
		tvSourceName = (TextView) findViewById(R.id.tvSourceName);
		tvSourceIcon = (ImageView) findViewById(R.id.imgSourceIcon);
		footerSetting = (LinearLayout) findViewById(R.id.footerSetting);
		footerImgShowSetting = (ImageView) findViewById(R.id.footerImgShowSetting);

		progress = findViewById(R.id.progressBar1);

		// bookmark init
		img_bookmark = (ImageView) findViewById(R.id.img_bookmark);
		img_bookmark.setOnClickListener(this);
		db = new DataBaseHelper(context);
		// share FB init
		img_shareFB = (ImageView) findViewById(R.id.icon_fb);
		img_shareFB.setOnClickListener(this);

		footerImgShowSetting.setOnClickListener(this);
		findViewById(R.id.footerFont).setOnClickListener(this);
		findViewById(R.id.webview).setOnClickListener(this);
		findViewById(R.id.share).setOnClickListener(this);
		// disappear footer
		showSetting();
		init();
		initActionBar();
		// Create an ad.
		// adView = (AdView) findViewById(R.id.adView);

		// Create an ad request. Check logcat output for the hashed device ID to
		// get test ads on a physical device.
		// AdRequest adRequest = new AdRequest.Builder()
		// .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
		// .addTestDevice("INSERT_YOUR_HASHED_DEVICE_ID_HERE").build();

		String linkk = "http://news.ctyprosoft.com:8081/service.php?action=getArticleDetail&articleID="
				+ articleID;
		if (!constant.Constants.isVietnamese)
			linkk = linkk + "&lang=en";
		new asyTaskGetDetails().execute(linkk);
	}

	private void initActionBar() {
		// configure action bar
		// action bar
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setIcon(R.drawable.ic_launcher);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);

		// actionBar.setTitle(ModernContents.cat);
		constant.Constants.log_d("TAG", "ModernContents.cat: "
				+ ModernContents.cat);
		actionBar.setTitle(Html.fromHtml("<font color=\"white\">"
				+ ModernContents.cat + "</font>"));
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor(mActionbar_color)));
	}

	@Override
	public void onBackPressed() {
		if (fromNotification)
			startActivity(new Intent(ModernDetails.this, SplashActivity.class));

		super.onBackPressed();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			if (fromNotification)
				startActivity(new Intent(ModernDetails.this,
						SplashActivity.class));

			break;
		case 1:

			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		/*
		 * menu.add(0, 1, 0, "TEXT" ) .setIcon(R.drawable.icon_comment)
		 * .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		 */
		com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
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
				String articleID = String.valueOf(details.getmId());
				Intent i = new Intent(ModernDetails.this, CommentActivity.class);
				i.putExtra("articleID", articleID);
				startActivityForResult(i, 1);
			}
		});
		tv = (TextView) badgeLayout
				.findViewById(R.id.actionbar_notifcation_textview);
		tv.setText(_cmt_count);
		return super.onCreateOptionsMenu(menu);
	}

	@SuppressWarnings("deprecation")
	private void init() {
		// Get Device size
		Display display = getWindowManager().getDefaultDisplay();
		constant.Constants.DEVICE_WIDTH = display.getWidth(); // deprecated
		constant.Constants.DEVICE_HEIGHT = display.getHeight(); // deprecated

		// Init ImageLoader
		imageLoader = ImageLoader.getInstance();
		// Initialize ImageLoader with configuration. Do it once.
		imageLoader.init(ImageLoaderConfiguration
				.createDefault(ModernDetails.this));
		// Load and display image asynchronously
		doption = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).showImageForEmptyUri(R.drawable.trang)
				.showImageOnFail(R.drawable.trang).build();

		// sharedpreferences
		sharedpreferences = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		mActionbar_color = sharedpreferences.getString("actionbar_color",
				constant.Constants.COLOR_ACTION_BAR);

		mFontSize = sharedpreferences.getInt("font_size",
				constant.Constants.FONT_SIZE);
		waitingDialog = new Dialog(context);
		waitingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		waitingDialog.setContentView(R.layout.waitingdialog);
		waitingDialog.setCancelable(false);

		ScrollViewExt scroll = (ScrollViewExt) findViewById(R.id.scrollView1);
		// ScrollView scroll = (ScrollView) findViewById(R.id.scrollView1);
		scroll.setScrollViewListener(this);
		// ImageView img = (ImageView) findViewById(R.id.parent);
		// img.setOnTouchListener(this);
	}

	/**
	 * To get Details from server
	 * 
	 */
	class asyTaskGetDetails extends AsyncTask<String, String, String> {

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
			// TODO create Details
			// check artical has bookmark already or not
			makeDetails(result);
			// enable footer
			showSetting();
			boolean _bookmark = db.checkArticalBookmarkExist(
					String.valueOf(details.getmId()),
					Utility._str_table_Bookmark);
			if (_bookmark)
				img_bookmark.setImageResource(R.drawable.icon_bookmarked);
			tvTitle.setText(details.getmTitle());
			tvSourceName.setText("- " + details.getmSourceName());
			constant.Constants.log_d("TAG", "date: " + details.getmDate());
			// init day
			_day = Utility.getTime(details.getmDate(), ModernDetails.this);
			// init day
			tvTitle.setText(details.getmTitle());
			tvExcerpt.setText(details.getmHeader());
			tvSourceName.setText("- " + details.getmSourceName());
			constant.Constants.log_d("TAG", "date: " + details.getmDate());
			tvDate.setText(_day);
			imageLoader.displayImage(details.getmSourceIcon(), tvSourceIcon,
					doption);

			android.widget.LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			tvContentsId = new ArrayList<Integer>();
			tvHintContentsId = new ArrayList<Integer>();
			for (int i = 0; i < details.getmContent().size(); i++) {
				String txt = details.getmContent().get(i) + "~";
				String[] nb = txt.split("###Table###");

				// //////
				// ///// add text
				if (!nb[0].equals("~")) {
					TextView tvv = new TextView(ModernDetails.this);

					tvv.setId(i + 1000);
					tvContentsId.add(i + 1000);

					tvv.setTextSize(mFontSize);
					tvv.setLayoutParams(lp);
					String s2 = nb[0];
					s2 = s2.replace("~", "");
					tvv.setTextIsSelectable(true);
					tvv.setText(s2);
					llDetails.addView(tvv);
				}

				for (int ti = 1; ti < nb.length; ti++) {
					current_number_of_table += 1;
					// /////////////////
					// add Table
					// /////////////////

					WebView webViewq = new WebView(ModernDetails.this);
					android.widget.LinearLayout.LayoutParams wv_lp = new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT);
					webViewq.setLayoutParams(wv_lp);
					webViewq.getSettings().setJavaScriptEnabled(true);
					String urlwb = "http://news.ctyprosoft.com:8081/tableWebView.php?articleID="
							+ details.getmId()
							+ "&tableNumber="
							+ current_number_of_table;
					constant.Constants.log_d("TAG", "urlwb: " + urlwb);
					webViewq.loadUrl(urlwb);
					llDetails.addView(webViewq);

					// //////
					// ///// add text
					if (!nb[ti].equals("~")) {
						TextView tv1 = new TextView(ModernDetails.this);

						tv1.setId(i + 1000);
						tvContentsId.add(i + 1000);

						tv1.setTextSize(mFontSize);
						tv1.setLayoutParams(lp);
						String s3 = nb[ti];
						s3 = s3.replace("~", "");
						tv1.setTextIsSelectable(true);
						tv1.setText(s3);
						llDetails.addView(tv1);
					}

				}

				if (details.getmContent().size() < 2
						|| i == (details.getmContent().size() - 1))
					break;

				final ImageView img1 = new ImageView(ModernDetails.this);
				android.widget.LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

				layout.gravity = Gravity.CENTER;

				// if (constant.Constants.DEVICE_WIDTH > 600)
				// layout = new LinearLayout.LayoutParams(600,
				// LayoutParams.WRAP_CONTENT);
				img1.setLayoutParams(layout);

				// imageLoader = ImageLoader.getInstance();
				// // Initialize ImageLoader with configuration. Do it once.
				// imageLoader.init(ImageLoaderConfiguration
				// .createDefault(ModernDetails.this));
				// // Load and display image asynchronously

				// TODO error here

				imageLoader.displayImage(details.getmImgUrls().get(i), img1,
						doption, new ImageLoadingListener() {
							@Override
							public void onLoadingStarted(String imageUri,
									View view) {
							}

							@Override
							public void onLoadingFailed(String imageUri,
									View view, FailReason failReason) {
							}

							@Override
							public void onLoadingComplete(String imageUri,
									View view, Bitmap loadedImage) {
								// TODO Auto-generated method stub
								int h_img = 0;
								// if (constant.Constants
								// .isPhone(ModernDetails.this))
								// {
								try {
									float rate = (float) (constant.Constants.DEVICE_WIDTH * 1.0 / loadedImage
											.getWidth());
									constant.Constants.log_d("TAG", "rate: "
											+ rate);

									h_img = (int) (loadedImage.getHeight() * rate);
								} catch (Exception d) {

								}
								android.widget.LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
										LayoutParams.MATCH_PARENT, h_img);

								layout.gravity = Gravity.CENTER;

								img1.setLayoutParams(layout);
								// }
							}

							@Override
							public void onLoadingCancelled(String imageUri,
									View view) {
							}
						});
				llDetails.addView(img1);

				// Set Textview Hint for image
				if (!details.getmImgHints().get(i).equals("~")) {
					TextView tvHint = new TextView(ModernDetails.this);

					tvHint.setId(i + 10000);
					tvHintContentsId.add(i + 10000);

					tvHint.setTextSize(mFontSize - 2);
					tvHint.setLayoutParams(lp);
					String s4 = details.getmImgHints().get(i);
					s4 = s4.replace("~", "");
					tvHint.setTextIsSelectable(true);
					tvHint.setText(s4);
					tvHint.setPadding(50, 0, 50, 0);
					tvHint.setTypeface(null, Typeface.ITALIC);
					llDetails.addView(tvHint);
				}

			}

			// ////////////////////////////////
			// Add Video and Table
			// ///////////////////////////////
			addVideoAndTable();

			progress.setVisibility(View.GONE);

			super.onPostExecute(result);
		}
	}

	int current_number_of_table = 0;
	int number_of_table = 0;

	private void makeDetails(String result) {
		// constant.Constants.log_d("TAG", "result: " + result);
		try {
			JSONObject jsonObject = new JSONObject(result);

			int id = jsonObject.getInt("ID");
			int numberComment = jsonObject.getInt("numberComment");

			String title = jsonObject.getString("title");
			String url = jsonObject.getString("url");
			String websiteName = jsonObject.getString("websiteName");
			String websiteIcon = jsonObject.getString("websiteIcon");
			String excerpt = jsonObject.getString("excerpt");

			String datePost = jsonObject.getString("datePost");
			// String[] dates = datePost.split("\"");
			// datePost = dates[3];

			String content = jsonObject.getString("content") + "~";
			String imageURL = jsonObject.getString("imageURL");

			ArrayList<EachVideoBongDa365> mVideos = new ArrayList<EachVideoBongDa365>();
			String videoURL = "~" + jsonObject.getString("videoURL") + "~";
			String[] eachVideo = videoURL.split("###");
			// constant.Constants.log_d("TAG", "videoURL: " + videoURL);
			for (int e = 1; e < (eachVideo.length - 1); e++) {
				try {
					String text_eachVideo = "~" + eachVideo[e] + "~";
					String[] part_eachVideo = text_eachVideo.split("<1>");
					String hint = part_eachVideo[1];
					ArrayList<String> videos = new ArrayList<String>();
					for (int k = 2; k < (part_eachVideo.length - 1); k = k + 2) {
						videos.add(part_eachVideo[k]);
						// constant.Constants.log_d("TAG", "part_eachVideo:"
						// + part_eachVideo[k]);
					}

					EachVideoBongDa365 mEachVideo = new EachVideoBongDa365(
							hint, videos);
					mVideos.add(mEachVideo);
				} catch (Exception gf) {

				}
			}

			content = Html.fromHtml(content).toString().trim();
			String[] nb = content.split("###Table###");
			number_of_table = nb.length - 1;
			// content = content.replace("###Video###",
			// getResources().getString(R.string.bongda365_xemvideobenduoi));
			content = content.replace("###Video###", "");
			// content = content.replace("###Table###",
			// getResources()
			// .getString(R.string.bongda365_xemtablebenduoi));

			String[] contents = content.split("###Image###");
			ArrayList<String> mContents = new ArrayList<String>();
			for (int j = 0; j < contents.length; j++) {
				mContents.add(contents[j]);
			}

			ArrayList<String> mImgUrls = new ArrayList<String>();
			ArrayList<String> mImgHints = new ArrayList<String>();
			if (contents.length > 1) {
				imageURL = imageURL + "~";
				String[] eachImage = imageURL.split("###");
				// Log.d("TAG", "ID: " + id + " - imageURL: " + imageURL);
				// Log.d("TAG", "size: " + eachImage.length);

				for (int k = 1; k < eachImage.length; k++) {
					String s = eachImage[k];
					s = s + "~";
					try {
						String[] item = s.split("<1>");
						String img_fixed1 = item[1];
						String img_fixed = img_fixed1.replace(" ", "%20");
						mImgUrls.add(img_fixed);
						mImgHints.add(item[2]);
					} catch (Exception e) {
						mImgHints.add("a");
						mImgUrls.add("a");
					}
				}
			}

			// TODO Video se lam sau
			if (content != null && content.length() < 10) {
				try {
					mContents.remove(0);
					mContents.add(getResources().getString(
							R.string.baivietdangcapnhat));
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			details = new News_Detail(id, title, excerpt, datePost,
					websiteName, websiteIcon, url, mContents, mImgUrls,
					mImgHints, mVideos, numberComment);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * To add Video and table at the bottom of details
	 */
	@SuppressLint("SetJavaScriptEnabled")
	private void addVideoAndTable() {
		// details.getmVideoUrls().get(0).getHint();
		android.widget.LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		for (int i = 0; i < details.getmVideoUrls().size(); i++) {
			final int ps = i;
			String s1 = details.getmVideoUrls().get(i).getHint();
			if (!s1.equals("")) {

				TextView tv1 = new TextView(ModernDetails.this);

				tv1.setId(i + 10000);
				tvContentsId.add(i + 10000);

				tv1.setTextSize(mFontSize);
				tv1.setLayoutParams(lp);
				s1 = s1.replace("~", "");
				tv1.setTextIsSelectable(true);
				tv1.setText(s1);
				llDetails.addView(tv1);
			}

			final ImageView img1 = new ImageView(ModernDetails.this);
			android.widget.LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			img1.setImageResource(R.drawable.button_video);
			layout.gravity = Gravity.CENTER;
			img1.setLayoutParams(layout);
			img1.setClickable(true);
			img1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent it = new Intent(context, VideoViewActivity.class);
					// it.putExtra("path", video);
					it.putExtra("pos", 0);
					it.putStringArrayListExtra("array", details.getmVideoUrls()
							.get(ps).getVideos());
					startActivity(it);
				}
			});
			llDetails.addView(img1);

			if (details.getmVideoUrls().get(i).getVideos().size() > 1) {
				TextView tv2 = new TextView(ModernDetails.this);
				lp.gravity = Gravity.CENTER;
				tv2.setId(i + 20000);
				tv2.setPadding(0, 0, 0, 20);
				tvContentsId.add(i + 20000);

				tv2.setTextSize(mFontSize - 4);
				tv2.setLayoutParams(lp);
				tv2.setText("(Có "
						+ details.getmVideoUrls().get(i).getVideos().size()
						+ " Video)");
				llDetails.addView(tv2);
			}

		}

		// // /////////////////
		// // add Table
		// // /////////////////
		//
		// constant.Constants.log_d("TAG", " number_of_table :" +
		// number_of_table);
		// for (int n = 1; n <= number_of_table; n++) {
		// WebView webViewq = new WebView(BongDaDetailsActivity.this);
		// android.widget.LinearLayout.LayoutParams wv_lp = new
		// LinearLayout.LayoutParams(
		// LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		// webViewq.setLayoutParams(wv_lp);
		// webViewq.getSettings().setJavaScriptEnabled(true);
		// String urlwb =
		// "http://news.ctyprosoft.com:8081/tableWebView.php?articleID="
		// + details.getmId() + "&tableNumber=" + n;
		// constant.Constants.log_d("TAG", "urlwb: " + urlwb);
		// webViewq.loadUrl(urlwb);
		// llDetails.addView(webViewq);
		// }

	}

	boolean footerIsShowing = true;
	boolean fontBigger = true;
	ArrayList<Integer> tvContentsId;
	ArrayList<Integer> tvHintContentsId;
	private boolean isWebview = false;

	/**
	 * To Show footer setting
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.icon_fb:
			Vietnalyze
					.logEvent(getResources().getString(R.string.eventShareFb));
			FlurryAgent.logEvent(getResources()
					.getString(R.string.eventShareFb));
			mSimpleFacebook = SimpleFacebook.getInstance(this);
			if (Utility.isInternet(this)) {
				boolean islogin = mSimpleFacebook.isLogin();
				if (islogin) {
					waitingDialog.show();
					publishFeedExample();
				} else
					mSimpleFacebook.login(mOnLoginListener);
			} else
				Utility.showDialog(this,
						getResources().getString(R.string.internetConnection));

			break;
		case R.id.img_bookmark:
			Vietnalyze.logEvent(getResources().getString(
					R.string.eventaddBookMark));
			FlurryAgent.logEvent(getResources().getString(
					R.string.eventaddBookMark));
			boolean _bookmark = db.checkArticalBookmarkExist(
					String.valueOf(details.getmId()),
					Utility._str_table_Bookmark);
			if (_bookmark) {
				img_bookmark.setImageResource(R.drawable.icon_bookmark);
				db.deleteOneBookamrk(String.valueOf(details.getmId()),
						Utility._str_table_Bookmark);
			} else {
				String imgThum = "";
				try {
					imgThum = details.getmImgUrls().get(0).toString();
				} catch (Exception e) {
					imgThum = "";
				}
				img_bookmark.setImageResource(R.drawable.icon_bookmarked);
				object_bookmark = new Bookmark(
						String.valueOf(details.getmId()), "", imgThum,
						details.getmTitle(), "", details.getmSourceName(),
						details.getmDate(), "", "");
				db.addBookmark(object_bookmark, Utility._str_table_Bookmark);
			}
			break;
		case R.id.footerImgShowSetting:
			showSetting();
			break;

		case R.id.share:
			String title = "Tin 365";
			if (details.getmTitle() != null && !details.getmTitle().equals(""))
				title = details.getmTitle();

			Vietnalyze.logEvent(getResources().getString(
					R.string.eventshareWithoutFB));
			FlurryAgent.logEvent(getResources().getString(
					R.string.eventshareWithoutFB));
			// Create the text message with a string
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, details.getmSourceUrl());
			sendIntent.setType(HTTP.PLAIN_TEXT_TYPE); // "text/plain" MIME type

			// Verify that the intent will resolve to an activity
			if (sendIntent.resolveActivity(getPackageManager()) != null) {
				startActivity(sendIntent);
			}
			break;
		case R.id.webview:
			Vietnalyze.logEvent(getResources().getString(
					R.string.eventoriginalNews));
			FlurryAgent.logEvent(getResources().getString(
					R.string.eventoriginalNews));
			webview = (WebView) findViewById(R.id.webView1);
			if (isWebview) {
				isWebview = false;
				webview.setVisibility(View.GONE);
				findViewById(R.id.scrollView1).setVisibility(View.VISIBLE);
				findViewById(R.id.progressBar1).setVisibility(View.GONE);
			} else {
				isWebview = true;
				webview.setVisibility(View.VISIBLE);
				findViewById(R.id.scrollView1).setVisibility(View.GONE);
				findViewById(R.id.progressBar1).setVisibility(View.VISIBLE);
				startWebView(details.getmSourceUrl());
			}

			break;
		case R.id.footerFont:
			if (fontBigger) {
				mFontSize += 2;
				if (mFontSize >= 26)
					fontBigger = false;

			} else {
				mFontSize -= 2;
				if (mFontSize <= 14)
					fontBigger = true;

			}

			for (int i = 0; i < tvContentsId.size(); i++)
				((TextView) findViewById(tvContentsId.get(i)))
						.setTextSize(mFontSize);

			for (int i = 0; i < tvHintContentsId.size(); i++)
				((TextView) findViewById(tvHintContentsId.get(i)))
						.setTextSize(mFontSize - 2);

			break;
		default:
			break;
		}

	}

	@SuppressLint("NewApi")
	private void showSetting() {
		View horizontalScrollView1 = findViewById(R.id.horizontalScrollView1);

		horizontalScrollView1.measure(MeasureSpec.UNSPECIFIED,
				MeasureSpec.UNSPECIFIED);
		int width = horizontalScrollView1.getMeasuredWidth();

		if (!footerIsShowing) {
			footerIsShowing = true;
			horizontalScrollView1.setVisibility(View.VISIBLE);

			footerImgShowSetting
					.setBackgroundResource(R.drawable.bg_press_white);
			footerImgShowSetting.setImageResource(R.drawable.icon_right);

			ObjectAnimator oa = ObjectAnimator.ofFloat(footerSetting,
					"translationX", width, 0f);
			oa.setDuration(500);
			oa.start();

		} else {
			footerIsShowing = false;

			footerImgShowSetting.setBackgroundResource(R.drawable.bg_press);
			footerImgShowSetting.setImageResource(R.drawable.icon_left);

			horizontalScrollView1.setVisibility(View.INVISIBLE);
		}

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
		SharedPreferences.Editor edit = sharedpreferences.edit();
		edit.putInt("font_size", mFontSize);
		edit.commit();
		super.onStop();
		Log.i("Flurry", "onStop");
		FlurryAgent.onEndSession(this);
		// your code

		Vietnalyze.onEndSession(this);
	}

	// /////////////////////////////////////
	// // WebView
	// ////////////////////////////////////
	private WebView webview;

	@SuppressLint("SetJavaScriptEnabled")
	private void startWebView(String url) {

		// Create new webview Client to show progress dialog
		// When opening a url or click on link

		webview.setWebViewClient(new WebViewClient() {
			ProgressDialog progressDialog;

			// If you will not use this method url links are opeen in new brower
			// not in webview
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			// Show loader on url load
			public void onLoadResource(WebView view, String url) {
				if (progressDialog == null) {
					// in standard case YourActivity.this
					progressDialog = new ProgressDialog(ModernDetails.this);
					progressDialog.setMessage("Loading...");
					progressDialog.show();
				}
			}

			public void onPageFinished(WebView view, String url) {
				try {
					progressDialog.dismiss();
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}

		});

		// Javascript inabled on webview
		webview.getSettings().setJavaScriptEnabled(true);

		// Other webview options
		/*
		 * webView.getSettings().setLoadWithOverviewMode(true);
		 * webView.getSettings().setUseWideViewPort(true);
		 * webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		 * webView.setScrollbarFadingEnabled(false);
		 * //webview.getSettings().setBuiltInZoomControls(true);
		 */
		/*
		 * String summary =
		 * "<html><body>You scored <b>192</b> points.</body></html>";
		 * webview.loadData(summary, "text/html", null);
		 */

		// Load url in webview
		webview.loadUrl(url);

	}

	// Login listener
	private OnLoginListener mOnLoginListener = new OnLoginListener() {

		@Override
		public void onFail(String reason) {
		}

		@Override
		public void onException(Throwable throwable) {
		}

		@Override
		public void onThinking() {
			// show progress bar or something to the user while login is
			// happening
		}

		@Override
		public void onLogin() {
			// change the state of the button or do whatever you want
			// toast("You are logged in");
			getProfileExample();

		}

		@Override
		public void onNotAcceptingPermissions() {
			// toast("You didn't accept read permissions");
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			mSimpleFacebook.onActivityResult(this, requestCode, resultCode,
					data);
		} catch (Exception e) {
			// TODO: handle exception
		}

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			try {
				String message = data.getStringExtra("MESSAGE");
				tv.setText(message);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	private void publishFeedExample() {
		// listener for publishing action
		final OnPublishListener onPublishListener = new SimpleFacebook.OnPublishListener() {

			@Override
			public void onFail(String reason) {
			}

			@Override
			public void onException(Throwable throwable) {
			}

			@Override
			public void onThinking() {
				// show progress bar or something to the user while publishing
			}

			@Override
			public void onComplete(String postId) {
				waitingDialog.cancel();
				Utility.showDialog(context, "Done");
			}
		};

		// feed builder
		String img = details.getmImgUrls().get(0);
		String desc = "";
		if (details.getmContent().toString().equals("")) {
			if (!details.getmHeader().equals(""))
				desc = details.getmHeader();
		} else
			desc = details.getmContent().toString();

		final Feed feed;
		if (img.endsWith("")) {
			feed = new Feed.Builder()
					.setMessage(getResources().getString(R.string.app_name))
					.setName(details.getmTitle()).setDescription(desc)
					.setLink(details.getmSourceUrl()).build();
		} else {
			feed = new Feed.Builder()
					.setMessage(getResources().getString(R.string.app_name))
					.setName(details.getmTitle()).setDescription(desc)
					.setLink(details.getmSourceUrl()).setPicture(img).build();
		}

		// click on button and publish
		mSimpleFacebook.publish(feed, onPublishListener);
	}

	@Override
	public void onScrollChanged(ScrollViewExt scrollView, int x, int y,
			int oldx, int oldy) {
		// TODO Auto-generated method stub
		View view = (View) scrollView
				.getChildAt(scrollView.getChildCount() - 1);
		int diff = (view.getBottom() - (scrollView.getHeight() + scrollView
				.getScrollY()));

		// if diff is zero, then the bottom has been reached
		if (diff < 500) {
			// do stuff
			// Toast.makeText(context, "down", 400).show();
			if (footerIsShowing) {
				showSetting();
			}
		} else if (diff > 500 && !footerIsShowing) {
			showSetting();
		}
	}

	public int count = 0;
	public Timer timer;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub

		if (event.getAction() == MotionEvent.ACTION_UP) {

			// Do what you want
			timer.cancel();
			if (count == 1)
				showSetting();
			// Toast.makeText(context, "up", 100).show();
			return true;
		} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// Toast.makeText(context, "down", 100).show();
			// Do what you want
			timer = new Timer();
			count = 0;
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					count++;
				}
			}, 0, 100);
			return true;
		}

		return false;
	}

	private void getProfileExample() {
		// listener for profile request
		final OnProfileRequestListener onProfileRequestListener = new SimpleFacebook.OnProfileRequestListener() {

			@Override
			public void onFail(String reason) {
				// insure that you are logged in before getting the profile
			}

			@Override
			public void onException(Throwable throwable) {
			}

			@Override
			public void onThinking() {
				// show progress bar or something to the user while fetching
				// profile
			}

			@Override
			public void onComplete(Profile profile) {
				Session session = Session.getActiveSession();
				String userId = profile.getId();
				String userName = profile.getUsername();
				String url = "http://graph.facebook.com/" + userId
						+ "/picture?height=200&width=200";
				SharePreferance share = new SharePreferance(ModernDetails.this);
				share.setFbPhoto(url);
				share.setUserName(userName);
				share.setfbID(profile.getId());
				Utility.url_photo_FB = url;
				Utility.fbID = profile.getId();
				Utility.user_name = userName;
				publishFeedExample();
				// download.download(url, img_iconFB);
			}
		};

		mSimpleFacebook.getProfile(onProfileRequestListener);

	}
}
