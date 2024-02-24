package com.skyward101.tin365;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import object.Bookmark;
import object.News_Cat;
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

import se.emilsjolander.flipview.FlipView;
import se.emilsjolander.flipview.FlipView.OnFlipListener;
import se.emilsjolander.flipview.FlipView.OnOverFlipListener;
import se.emilsjolander.flipview.OverFlipMode;
import utility.ImageDownloaderMainBanners;
import utility.SharePreferance;
import utility.Utility;
import adapter.TitleListView;
import adapter.TraditionTitle;
import android.Vietnalyze;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.test.MoreAsserts;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import bongda365.BongDa365Activity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.facebook.Session;
import com.facebook.widget.LoginButton;
import com.flurry.android.FlurryAgent;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.skyward101.tin365.R;
import com.slidingmenu.lib.SlidingMenu;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebook.OnLoginListener;
import com.sromku.simple.fb.SimpleFacebook.OnLogoutListener;
import com.sromku.simple.fb.SimpleFacebook.OnProfileRequestListener;
import com.sromku.simple.fb.entities.Profile;

public class ModernNews extends SherlockFragmentActivity implements
		OnOverFlipListener, OnFlipListener, OnClickListener {

	private ImageLoader imageLoader;
	private DisplayImageOptions doption;
	private LayoutInflater inflater;
	private SlidingMenu menu;
	private FlipView flipView;
	private LinearLayout mActionBar_LinearLayout;
	public static boolean isMainCategory = true;
	private boolean isActivityDestroy = false;

	private SharedPreferences sharedpreferences;
	private static final String SP_NAME = "MY_sharedpreferences";
	public static String mActionbar_color;
	public int NUM_PAGE;

	ImageDownloaderMainBanners download;
	SimpleFacebook mSimpleFacebook;
	LoginButton btn_fb_login;
	ImageView img_iconFB;
	String userId;
	// Login listener
	public OnLoginListener mOnLoginListener = new OnLoginListener() {

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
			// getProfileExample();
			// Toast.makeText(ModernNews.this, "complete", 4000).show();
			getProfileExample();

		}

		@Override
		public void onNotAcceptingPermissions() {
			// toast("You didn't accept read permissions");
		}
	};
	// Logout listener
	private OnLogoutListener mOnLogoutListener = new OnLogoutListener() {

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
		public void onLogout() {
			// change the state of the button or do whatever you want
			Toast.makeText(ModernNews.this, "logout", 4000).show();
			SharePreferance share = new SharePreferance(ModernNews.this);
			share.setFbPhoto("");
			img_iconFB.setImageResource(R.drawable.user);
		}

	};

	@Override
	public void onBackPressed() {

		if (menu != null && menu.isMenuShowing())
			menu.toggle();
		else
			super.onBackPressed();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modern);

		// Flip view
		flipView = (FlipView) findViewById(R.id.flip_view);

		init();
		initActionBar();
		initSlidingMenu();

		int i = 0;
		try {
			i = (SplashActivity.cats.size() - 5);
		} catch (Exception e) {
			showDialogChonNgonNgu();
			return;
		}

		if (i % 6 == 0)
			NUM_PAGE = i / 6 + 1;
		else
			NUM_PAGE = i / 6 + 2;
		flipView.setAdapter(new flipViewAdapter());

		flipView.setEmptyView(findViewById(R.id.empty_view));
		flipView.peakNext(true);
		flipView.setOverFlipMode(OverFlipMode.RUBBER_BAND);

		flipView.setOnFlipListener(this);
		flipView.setOnOverFlipListener(this);
		// AsyncTask get contents here
		// new asyTaskGetCats().execute();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
		/*
		 * if (Session.getActiveSession().isOpened()) { // Request user data and
		 * show the results
		 * Request.executeMeRequestAsync(Session.getActiveSession(), new
		 * Request.GraphUserCallback() {
		 * 
		 * @Override public void onCompleted(GraphUser user, Response response)
		 * { // TODO Auto-generated method stub if (user != null) { // Display
		 * the parsed user info Log.v("test", "Response : " + response);
		 * Log.v("test", "UserID : " + user.getId()); Log.v("test",
		 * "User FirstName : " + user.getFirstName());
		 * 
		 * } }
		 * 
		 * }); }
		 */
	}

	@Override
	public void onFlippedToPage(FlipView v, int position, long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onOverFlip(FlipView v, OverFlipMode mode,
			boolean overFlippingPrevious, float overFlipDistance,
			float flipDistancePerPage) {
		// TODO Auto-generated method stub
		// handle event when the last page was focused
	}

	private void initSlidingMenu() {
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menu.setShadowWidthRes(R.dimen.sliding_shadow_width);
		menu.setShadowDrawable(R.drawable.sliding_menu_shadow);
		menu.setBehindOffsetRes(R.dimen.sliding_behind_of_set);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		menu.setMenu(R.layout.sliding_menu);
		menu.setSlidingEnabled(true);

		// Setting action
		LinearLayout sliding_menu_caidat = (LinearLayout) findViewById(R.id.sliding_menu_caidat);
		LinearLayout sliding_menu_caidat_mautieude = (LinearLayout) findViewById(R.id.sliding_menu_caidat_mautieude);

		LinearLayout sliding_menu_caidat_giaodien = (LinearLayout) findViewById(R.id.sliding_menu_caidat_giaodien);

		LinearLayout sliding_menu_gioithieu = (LinearLayout) findViewById(R.id.sliding_menu_gioithieu);

		TextView sliding_menu_caidat_mautieude_tv_bg = (TextView) findViewById(R.id.sliding_menu_caidat_mautieude_tv_bg);
		sliding_menu_caidat_mautieude_tv_bg.setBackgroundColor(Color
				.parseColor(mActionbar_color));

		sliding_menu_caidat_mautieude.setOnClickListener(this);
		sliding_menu_caidat_giaodien.setOnClickListener(this);
		sliding_menu_gioithieu.setOnClickListener(this);
		sliding_menu_caidat.setOnClickListener(this);
		findViewById(R.id.sliding_menu_source).setOnClickListener(this);
		findViewById(R.id.sliding_menu_themnguon).setOnClickListener(this);
		findViewById(R.id.ll_rate).setOnClickListener(this);
		findViewById(R.id.sliding_menu_caidat_notification_img)
				.setOnClickListener(this);
		findViewById(R.id.sliding_menu_caidat_ngonngu_img).setOnClickListener(
				this);
		setSource();

		LinearLayout sliding_menu_loginFB = (LinearLayout) findViewById(R.id.login_ll);
		sliding_menu_loginFB.setOnClickListener(this);

		LinearLayout sliding_menu_bookmark = (LinearLayout) findViewById(R.id.linear_bookmark);
		sliding_menu_bookmark.setOnClickListener(this);

		LinearLayout sliding_menu_contact = (LinearLayout) findViewById(R.id.ll_contact);
		sliding_menu_contact.setOnClickListener(this);

		LinearLayout linear_videobanthang = (LinearLayout) findViewById(R.id.linear_videobanthang);
		linear_videobanthang.setOnClickListener(this);
		findViewById(R.id.linear_worldcup2014).setOnClickListener(this);

		mSimpleFacebook = SimpleFacebook.getInstance(ModernNews.this);
		btn_fb_login = (LoginButton) findViewById(R.id.login_icon);
		btn_fb_login.setOnClickListener(ModernNews.this);

		img_iconFB = (ImageView) findViewById(R.id.user_icon);
		download = new ImageDownloaderMainBanners();

		if (mSimpleFacebook.isLogin())
			download.download(Utility.url_photo_FB, img_iconFB);

		findViewById(R.id.khoiphucmacdinh).setOnClickListener(this);

		if (constant.Constants.isVietnamese)
			((ImageView) findViewById(R.id.sliding_menu_caidat_ngonngu_img))
					.setImageResource(R.drawable.en);
		else
			((ImageView) findViewById(R.id.sliding_menu_caidat_ngonngu_img))
					.setImageResource(R.drawable.vi);

		// Set image for notification status
		boolean getNotification = sharedpreferences.getBoolean(
				"getNotification", false);
		if (getNotification)
			((ImageView) findViewById(R.id.sliding_menu_caidat_notification_img))
					.setImageResource(R.drawable.on);
		else
			((ImageView) findViewById(R.id.sliding_menu_caidat_notification_img))
					.setImageResource(R.drawable.off);

		if (constant.Constants.isVietnamese) {
			findViewById(R.id.linear_worldcup2014).setVisibility(View.VISIBLE);
			findViewById(R.id.linear_videobanthang).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.linear_worldcup2014).setVisibility(View.GONE);
			findViewById(R.id.linear_videobanthang).setVisibility(View.GONE);
		}
	}

	@Override
	protected void onStart() {
		// TODO check and resetActity
		if (constant.Constants.isAddSource) {
			constant.Constants.isAddSource = false;
			isMainCategory = true;
			String api_getCat = "http://news.ctyprosoft.com:8081/service.php?action=getListMainCategory";
			if (!constant.Constants.isVietnamese)
				api_getCat = api_getCat + "&lang=en";
			new asyTaskGetCatsFromSource().execute(api_getCat);
		}
		// trace
		Log.i("Flurry", "onStart second");
		FlurryAgent.onStartSession(this, "YD9XG88S3BBSHG42P5HD");
		FlurryAgent.setLogEnabled(true);
		FlurryAgent.setLogEvents(true);
		FlurryAgent.setLogLevel(Log.VERBOSE);
		// your code

		// Vietnalyze
		Vietnalyze.onStartSession(this, "ZW9SHTZ4PYX5UI7P4FVG");
		super.onStart();
	}

	/**
	 * To set all source that it was saved from shared preference
	 */
	private void setSource() {
		String add_source = sharedpreferences.getString("add_source", "");
		constant.Constants
				.log_d("TAG", "Modern news add Source: " + add_source);

		if (add_source.equals(""))
			return;

		LinearLayout llAddSource = (LinearLayout) findViewById(R.id.sliding_menu_source_sub);
		// LinearLayout llTinTongHop = (LinearLayout)
		// findViewById(R.id.tintonghop);
		// TextView sliding_menu_tv_boder = (TextView)
		// findViewById(R.id.sliding_menu_tv_boder);
		// LinearLayout sliding_menu_themnguon = (LinearLayout)
		// findViewById(R.id.sliding_menu_themnguon);
		//
		// //llAddSource.removeAllViews();
		// llAddSource.addView(llTinTongHop);
		// //llAddSource.addView(sliding_menu_tv_boder);
		// //llAddSource.addView(sliding_menu_tv_boder);
		// //llAddSource.addView(sliding_menu_themnguon);

		// image
		ImageLoader imageLoader = ImageLoader.getInstance();
		// Initialize ImageLoader with configuration. Do it once.
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		// Load and display image asynchronously
		DisplayImageOptions doption = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true).build();

		findViewById(R.id.tintonghop).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				menu.toggle();
				isMainCategory = true;
				String api_getCat = "http://news.ctyprosoft.com:8081/service.php?action=getListMainCategory";
				if (!constant.Constants.isVietnamese)
					api_getCat = api_getCat + "&lang=en";
				new asyTaskGetCatsFromSource().execute(api_getCat);
			}
		});

		String[] eachSource = add_source.split("###");
		for (int i = 0; i < eachSource.length; i++) {
			String[] es = eachSource[i].split("@@@");
			final int id = Integer.parseInt(es[0]);
			String name = es[1];
			String urlIcon = es[2];

			LinearLayout ll = new LinearLayout(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.CENTER_VERTICAL;
			ll.setLayoutParams(params);
			ll.setOrientation(LinearLayout.HORIZONTAL);
			ll.setClickable(true);
			ll.setBackgroundResource(R.drawable.bg_press_slidingmenu);
			int padding = (int) getResources().getDimension(
					R.dimen.sliding_menu_padding);
			ll.setPadding(padding, padding, padding, padding);
			ll.setGravity(Gravity.CENTER_VERTICAL);

			// icon of source
			ImageView icon = new ImageView(this);
			int height = (int) getResources().getDimension(
					R.dimen.sliding_menu_icon_height);
			LayoutParams gg = new LayoutParams(height, height);
			icon.setLayoutParams(gg);
			icon.setScaleType(ScaleType.CENTER_CROP);
			icon.setImageResource(R.drawable.icon_bookmark);

			imageLoader.displayImage(urlIcon, icon, doption);

			// Name of source
			TextView tvName = new TextView(this);
			LayoutParams lp = new TableLayout.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 1f);
			tvName.setLayoutParams(lp);
			tvName.setPadding(padding, 0, 0, 0);
			tvName.setTypeface(null, Typeface.BOLD);
			tvName.setText(name);

			ll.addView(icon);
			ll.addView(tvName);

			ll.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					menu.toggle();
					isMainCategory = false;
					String api_getCat = "http://news.ctyprosoft.com:8081/service.php?action=getListCategoryOneWebsite&webID="
							+ id;
					if (!constant.Constants.isVietnamese)
						api_getCat = api_getCat + "&lang=en";
					new asyTaskGetCatsFromSource().execute(api_getCat);
				}
			});

			// Text boder
			TextView tvBoder = new TextView(this);
			tvBoder.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					1));
			tvBoder.setBackgroundColor(Color.parseColor("#B4B4B4"));

			if (i > 0)
				llAddSource.addView(tvBoder, 3);

			llAddSource.addView(ll, 3); // this was added after boder

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
		final View v = inflator.inflate(R.layout.list_nav_layout, null);

		com.actionbarsherlock.app.ActionBar.LayoutParams params = new com.actionbarsherlock.app.ActionBar.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		actionBar.setCustomView(v, params);
		actionBar.setDisplayShowCustomEnabled(true);

		mActionBar_LinearLayout = (LinearLayout) v
				.findViewById(R.id.llActionBar);
		mActionBar_LinearLayout.setBackgroundColor(Color
				.parseColor(mActionbar_color));
		v.findViewById(R.id.img_down).setVisibility(View.GONE);
		// ((TextView) v.findViewById(R.id.textView1)).setText(getResources()
		// .getString(R.string.app_name));
		v.findViewById(R.id.llIcon).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				menu.toggle();
			}
		});

		v.findViewById(R.id.img_coupon).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						startActivity(new Intent(ModernNews.this,
								HotDealsActivity.class));
						;
					}
				});

		if (constant.Constants.isVietnamese)
			v.findViewById(R.id.img_coupon).setVisibility(View.VISIBLE);
		else
			v.findViewById(R.id.img_coupon).setVisibility(View.GONE);
	}

	@SuppressWarnings("deprecation")
	private void init() {
		// Get Device size
		Display display = getWindowManager().getDefaultDisplay();
		constant.Constants.DEVICE_WIDTH = display.getWidth(); // deprecated
		constant.Constants.DEVICE_HEIGHT = display.getHeight(); // deprecated
		constant.Constants.log_d("TAG", "DEVICE_WIDTH: " + display.getWidth());
		constant.Constants
				.log_d("TAG", "DEVICE_HEIGHT: " + display.getHeight());

		// Init ImageLoader
		imageLoader = ImageLoader.getInstance();
		// Initialize ImageLoader with configuration. Do it once.
		imageLoader.init(ImageLoaderConfiguration
				.createDefault(ModernNews.this));
		// Load and display image asynchronously
		doption = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic)
				.showImageOnFail(R.drawable.ic).cacheInMemory(true)
				.cacheOnDisc(true).build();

		inflater = (LayoutInflater) ModernNews.this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// sharedpreferences
		sharedpreferences = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		mActionbar_color = sharedpreferences.getString("actionbar_color",
				constant.Constants.COLOR_ACTION_BAR);

		boolean wasShowDialogNotification = sharedpreferences.getBoolean(
				"wasShowDialogNotification", false);

		if (!wasShowDialogNotification) {
			SharedPreferences.Editor edit = sharedpreferences.edit();
			edit.putBoolean("wasShowDialogNotification", true);
			edit.commit();

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					showDialogNhanTinHot();
				}
			}, 2000);
		}
	}

	/**
	 * To show dialog get hot news
	 */
	private void showDialogNhanTinHot() {
		new AlertDialog.Builder(this)
				.setTitle(getResources().getString(R.string.thongbao))
				.setMessage(
						getResources().getString(R.string.bancomuonnhantinhot))
				.setCancelable(false)
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								SharedPreferences.Editor edit = sharedpreferences
										.edit();
								edit.putBoolean("getNotification", true);
								edit.commit();

								((ImageView) findViewById(R.id.sliding_menu_caidat_notification_img))
										.setImageResource(R.drawable.on);
							}
						})
				.setNegativeButton(android.R.string.no,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// do nothing
								((ImageView) findViewById(R.id.sliding_menu_caidat_notification_img))
										.setImageResource(R.drawable.off);
							}
						}).setIcon(android.R.drawable.ic_dialog_alert).show();
	}

	/**
	 * To get contents from server
	 * 
	 */
	class asyTaskGetContents extends AsyncTask<String, String, String> {
		String cat;
		// ProgressDialog progressDialog;
		private ArrayList<News_Content> contents;
		private Dialog dialog;

		public asyTaskGetContents(String cat) {
			super();
			this.cat = cat;
		}

		@Override
		protected void onPreExecute() {

			dialog = new Dialog(ModernNews.this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.waitingdialog);
			dialog.show();
			// progressDialog = new ProgressDialog(ModernNews.this);
			// progressDialog.show();
			super.onPreExecute();
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
			contents = new ArrayList<News_Content>();
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

			Intent i = new Intent(ModernNews.this, ModernContents.class);

			i.putExtra("contents", contents);
			i.putExtra("cat", cat);

			if (contents.size() == 0)
				Toast.makeText(
						ModernNews.this,
						getResources().getString(
								R.string.khongcodulieutrongphannay),
						Toast.LENGTH_SHORT).show();
			else
				startActivity(i);

			dialog.dismiss();
			// progressDiaconstant.Constants.log_dismiss();
			super.onPostExecute(result);
		}
	}

	/**
	 * To get Cats from api
	 */
	// class asyTaskGetCats extends AsyncTask<String, Void, Void> {
	//
	// @Override
	// protected Void doInBackground(String... url) {
	// // TODO
	// // get cats from server here
	// constant.Modern.addCats();
	// return null;
	// }
	//
	// @Override
	// protected void onPostExecute(Void result) {
	// // TODO set for flipview here
	// int i = (SplashActivity.cats.size() - 5);
	// if (i % 6 == 0)
	// NUM_PAGE = i / 6 + 1;
	// else
	// NUM_PAGE = i / 6 + 2;
	// flipView.setAdapter(new flipViewAdapter());
	//
	// flipView.setEmptyView(findViewById(R.id.empty_view));
	// flipView.peakNext(true);
	// flipView.setOverFlipMode(OverFlipMode.RUBBER_BAND);
	//
	// super.onPostExecute(result);
	// }
	//
	// }
	public static TextView tv2;

	/**
	 * Base Adapter use for flipView
	 */
	class flipViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return NUM_PAGE;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup parent) {
			constant.Constants.log_d("TAG", "Pos : " + pos);
			View view;
			if (pos == 0) { // the first page
				view = inflater.inflate(R.layout.modern_news_page1, null);
				setLayoutPage1(view);

				// constant.Constants.log_d("TAG", "here: "+ isMainCategory);
				// TextView tv2 = (TextView) findViewById(R.id.textView2);
				// if(!isMainCategory) tv2.setText("vanquan");
			} else { // the another page
				view = inflater.inflate(R.layout.modern_news_page2, null);
				setLayoutPage2(view, pos);
			}

			return view;
		}

	}

	protected void setLayoutPage1(View v) {
		// Set height of image
		LinearLayout ll1 = (LinearLayout) v.findViewById(R.id.ll1);
		LinearLayout ll2 = (LinearLayout) v.findViewById(R.id.ll2);

		LayoutParams params = ll1.getLayoutParams();
		params.height = constant.Constants.DEVICE_WIDTH / 2;

		params = ll2.getLayoutParams();
		params.height = constant.Constants.DEVICE_WIDTH / 2;
		// /////////////////////////////////////

		// Set contents of page
		TextView tv1 = (TextView) v.findViewById(R.id.tv1);
		ImageView img1 = (ImageView) v.findViewById(R.id.img1);
		tv2 = (TextView) v.findViewById(R.id.tv2);
		ImageView img2 = (ImageView) v.findViewById(R.id.img2);
		TextView tv3 = (TextView) v.findViewById(R.id.tv3);
		ImageView img3 = (ImageView) v.findViewById(R.id.img3);
		TextView tv4 = (TextView) v.findViewById(R.id.tv4);
		ImageView img4 = (ImageView) v.findViewById(R.id.img4);
		TextView tv5 = (TextView) v.findViewById(R.id.tv5);
		ImageView img5 = (ImageView) v.findViewById(R.id.img5);

		TextView[] tvs = { tv1, tv2, tv3, tv4, tv5 };
		final ImageView[] imgs = { img1, img2, img3, img4, img5 };

		constant.Constants.log_d("TAG", "cat2 : "
				+ SplashActivity.cats.get(1).getmName());

		for (int i = 0; i < 5; i++) {
			tvs[i].setText(SplashActivity.cats.get(i).getmName());
			imageLoader.displayImage(SplashActivity.cats.get(i).getmUrl(),
					imgs[i], doption);
			final int j = i;
			imgs[i].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					checkSendApi(j);
				}
			});

		}
		tv2.setText(SplashActivity.cats.get(1).getmName());
	}

	protected void setLayoutPage2(View v, int page) {
		// Set height of image
		LinearLayout ll1 = (LinearLayout) v.findViewById(R.id.ll1);
		LinearLayout ll2 = (LinearLayout) v.findViewById(R.id.ll2);

		LayoutParams params = ll1.getLayoutParams();
		params.height = constant.Constants.DEVICE_WIDTH / 2;

		params = ll2.getLayoutParams();
		params.height = constant.Constants.DEVICE_WIDTH / 2;
		// /////////////////////////////////////

		// Set contents of page
		TextView tv1 = (TextView) v.findViewById(R.id.tv1);
		ImageView img1 = (ImageView) v.findViewById(R.id.img1);
		TextView tv2 = (TextView) v.findViewById(R.id.tv2);
		ImageView img2 = (ImageView) v.findViewById(R.id.img2);
		TextView tv3 = (TextView) v.findViewById(R.id.tv3);
		ImageView img3 = (ImageView) v.findViewById(R.id.img3);
		TextView tv4 = (TextView) v.findViewById(R.id.tv4);
		ImageView img4 = (ImageView) v.findViewById(R.id.img4);
		TextView tv5 = (TextView) v.findViewById(R.id.tv5);
		ImageView img5 = (ImageView) v.findViewById(R.id.img5);
		TextView tv6 = (TextView) v.findViewById(R.id.tv6);
		ImageView img6 = (ImageView) v.findViewById(R.id.img6);

		TextView[] tvs = { tv1, tv2, tv3, tv4, tv5, tv6 };
		ImageView[] imgs = { img1, img2, img3, img4, img5, img6 };

		int start = 5 + 6 * (page - 1);
		constant.Constants.log_d("TAG", "page: " + page);
		if (page == NUM_PAGE - 1) { // the last page
			int items = SplashActivity.cats.size() - start;
			int i;
			for (i = 0; i < items; i++) {
				tvs[i].setText(SplashActivity.cats.get(start + i).getmName());
				imageLoader.displayImage(SplashActivity.cats.get(start + i)
						.getmUrl(), imgs[i], doption);
				final int j = start + i;
				imgs[i].setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						checkSendApi(j);
					}
				});
			}
			for (int j = i; j < 6; j++) {
				tvs[j].setVisibility(View.GONE);
				imgs[j].setVisibility(View.GONE);
			}
		} else {
			for (int i = 0; i < 6; i++) {
				tvs[i].setText(SplashActivity.cats.get(start + i).getmName());
				// if (constant.Modern.news_Details.get(start + i).getmImgUrls()
				// .size() > 0)
				imageLoader.displayImage(SplashActivity.cats.get(start + i)
						.getmUrl(), imgs[i], doption);
				final int j = start + i;
				imgs[i].setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						checkSendApi(j);
					}
				});
			}
		}

	}

	@Override
	public void onClick(View view) {
		View v;
		Dialog dialog;
		switch (view.getId()) {

		case R.id.ll_contact:
			Vietnalyze
					.logEvent(getResources().getString(R.string.eventcontact));
			FlurryAgent.logEvent(getResources()
					.getString(R.string.eventcontact));
			Intent it = new Intent(ModernNews.this,
					HotDealsDetailsActivity.class);
			it.putExtra("url", getResources().getString(R.string.FBFanPage));
			it.putExtra("admod", true);
			if (Utility.isInternet(this))
				startActivity(it);
			else
				Utility.showDialog(this,
						getResources().getString(R.string.internetConnection));

			break;
		case R.id.linear_worldcup2014:
			Vietnalyze.logEvent(getResources().getString(
					R.string.eventWorldCup2014));
			FlurryAgent.logEvent(getResources().getString(
					R.string.eventWorldCup2014));

			startActivity(new Intent(ModernNews.this, BongDa365Activity.class));
			break;
		case R.id.linear_videobanthang:
			Vietnalyze.logEvent(getResources().getString(
					R.string.eventvideoGoal));
			FlurryAgent.logEvent(getResources().getString(
					R.string.eventvideoGoal));
			if (Utility.isInternet(this))
				startActivity(new Intent(ModernNews.this,
						Bongda24hActivity.class));
			else
				Utility.showDialog(this,
						getResources().getString(R.string.internetConnection));

			break;
		case R.id.linear_bookmark:
			Vietnalyze.logEvent(getResources()
					.getString(R.string.eventBookMark));
			FlurryAgent.logEvent(getResources().getString(
					R.string.eventBookMark));
			startActivity(new Intent(ModernNews.this, BookmarkActivity.class));
			break;
		case R.id.login_icon:
			Vietnalyze
					.logEvent(getResources().getString(R.string.eventLoginFb));
			FlurryAgent.logEvent(getResources()
					.getString(R.string.eventLoginFb));
			if (Utility.isInternet(this)) {
				if (!mSimpleFacebook.isLogin())
					mSimpleFacebook.login(mOnLoginListener);
				else
					mSimpleFacebook.logout(mOnLogoutListener);
			} else
				Utility.showDialog(this,
						getResources().getString(R.string.internetConnection));

			break;
		case R.id.sliding_menu_caidat_giaodien:
			Vietnalyze.logEvent(getResources().getString(
					R.string.eventchangeAppViewtoTra));
			FlurryAgent.logEvent(getResources().getString(
					R.string.eventchangeAppViewtoTra));
			TextView sliding_menu_caidat_giaodien_tv = (TextView) findViewById(R.id.sliding_menu_caidat_giaodien_tv);

			sliding_menu_caidat_giaodien_tv.setText(getResources().getString(
					R.string.truyenthong));
			SharedPreferences.Editor edit = sharedpreferences.edit();
			edit.putBoolean("isModern", false);
			edit.commit();
			if (Utility.isInternet(this)) {
				startActivity(new Intent(ModernNews.this, SplashActivity.class));
				ModernNews.this.finish();
			} else
				Utility.showDialog(this,
						getResources().getString(R.string.internetConnection));

			break;

		case R.id.sliding_menu_caidat_mautieude:
			createDialog();

			break;
		case R.id.sliding_menu_caidat:
			ImageView sliding_menu_caidat_giaodien_img = (ImageView) findViewById(R.id.sliding_menu_caidat_img_down);
			v = findViewById(R.id.sliding_menu_caidat_sub);
			if (v.isShown()) {
				v.setVisibility(View.GONE);
				sliding_menu_caidat_giaodien_img
						.setImageResource(R.drawable.icon_down_dark);
			} else {
				sliding_menu_caidat_giaodien_img
						.setImageResource(R.drawable.icon_up_dark);
				v.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.sliding_menu_source:
			ImageView sliding_menu_source_icon_down = (ImageView) findViewById(R.id.sliding_menu_source_icon_down);
			v = findViewById(R.id.sliding_menu_source_sub);
			if (v.isShown()) {
				v.setVisibility(View.GONE);
				sliding_menu_source_icon_down
						.setImageResource(R.drawable.icon_down_dark);
			} else {
				sliding_menu_source_icon_down
						.setImageResource(R.drawable.icon_up_dark);
				v.setVisibility(View.VISIBLE);
			}
			break;

		case R.id.sliding_menu_themnguon:
			Vietnalyze.logEvent(getResources().getString(
					R.string.eventaddSources));
			FlurryAgent.logEvent(getResources().getString(
					R.string.eventaddSources));
			if (menu != null && menu.isShown())
				menu.toggle();
			Intent add_source = new Intent(ModernNews.this,
					AddSourceActivity.class);
			startActivity(add_source);
			break;
		case R.id.sliding_menu_gioithieu:
			ImageView sliding_menu_gioithieu_giaodien_img = (ImageView) findViewById(R.id.sliding_menu_gioithieu_img_down);

			v = findViewById(R.id.gioithieu_sub_ll);
			if (v.isShown()) {
				v.setVisibility(View.GONE);
				sliding_menu_gioithieu_giaodien_img
						.setImageResource(R.drawable.icon_down_dark);
			} else {
				sliding_menu_gioithieu_giaodien_img
						.setImageResource(R.drawable.icon_up_dark);
				v.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.ll_rate:
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri
					.parse("market://details?id=com.skyward101.tin365"));
			startActivity(intent);
			break;
		case R.id.khoiphucmacdinh:
			new asynTaskKhoiPhucMacDinh().execute();
			break;
		case R.id.sliding_menu_caidat_notification_img:
			boolean getNotification = sharedpreferences.getBoolean(
					"getNotification", false);
			SharedPreferences.Editor edit2 = sharedpreferences.edit();
			if (getNotification) {
				edit2.putBoolean("getNotification", false);
				edit2.commit();

				Toast.makeText(
						ModernNews.this,
						getResources().getString(
								R.string.tat_chuc_nang_nhan_tin_hot),
						Toast.LENGTH_SHORT).show();
				((ImageView) findViewById(R.id.sliding_menu_caidat_notification_img))
						.setImageResource(R.drawable.off);
			} else {
				edit2.putBoolean("getNotification", true);
				edit2.commit();

				Toast.makeText(
						ModernNews.this,
						getResources().getString(
								R.string.bat_chuc_nang_nhan_tin_hot),
						Toast.LENGTH_SHORT).show();
				((ImageView) findViewById(R.id.sliding_menu_caidat_notification_img))
						.setImageResource(R.drawable.on);
			}

			break;

		case R.id.sliding_menu_caidat_ngonngu_img:
			boolean b = true;
			if (constant.Constants.isVietnamese) {
				b = false;
				constant.Constants.isVietnamese = false;
			} else {
				b = true;
				constant.Constants.isVietnamese = true;
			}

			SharedPreferences.Editor editngongu = sharedpreferences.edit();
			editngongu.putBoolean("isVietnamese", b);
			editngongu.commit();

			startActivity(new Intent(ModernNews.this, SplashActivity.class));
			ModernNews.this.finish();

			break;

		default:
			break;
		}
	}

	/**
	 * To create Dialog for setting Action bar color
	 */
	private void createDialog() {
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.dialog_color_dialog);
		dialog.setTitle("Chọn màu bạn thích");

		final String[] color1 = { "#660000", "#666600", "#006600", "#006666",
				"#000066", "#660066" };
		final String[] color2 = { "#CC0000", "#FD8600", "#639500", "#9230C3",
				"#0099CC", "#00AA80" };
		// set the custom dialog components - text, image and button
		LinearLayout dialog_1 = (LinearLayout) dialog
				.findViewById(R.id.dialog_1);
		LinearLayout dialog_2 = (LinearLayout) dialog
				.findViewById(R.id.dialog_2);

		for (int i = 0; i < color1.length; i++) {
			final int j = i;
			final TextView sliding_menu_caidat_mautieude_tv_bg = (TextView) findViewById(R.id.sliding_menu_caidat_mautieude_tv_bg);
			TextView text = new TextView(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					60, 60);
			params.setMargins(5, 5, 5, 5);
			text.setLayoutParams(params);
			text.setId(i);
			text.setBackgroundColor(Color.parseColor(color1[i]));
			text.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					Vietnalyze.logEvent(getResources().getString(
							R.string.eventsettingColorTile));
					FlurryAgent.logEvent(getResources().getString(
							R.string.eventsettingColorTile));
					SharedPreferences.Editor edit = sharedpreferences.edit();
					edit.putString("actionbar_color", color1[j]);
					edit.commit();
					mActionBar_LinearLayout.setBackgroundColor(Color
							.parseColor(color1[j]));
					sliding_menu_caidat_mautieude_tv_bg
							.setBackgroundColor(Color.parseColor(color1[j]));
				}
			});
			dialog_1.addView(text);

			TextView text1 = new TextView(this);
			text1.setLayoutParams(params);
			text1.setBackgroundColor(Color.parseColor(color2[i]));
			text1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					SharedPreferences.Editor edit = sharedpreferences.edit();
					edit.putString("actionbar_color", color2[j]);
					edit.commit();
					mActionBar_LinearLayout.setBackgroundColor(Color
							.parseColor(color2[j]));
					sliding_menu_caidat_mautieude_tv_bg
							.setBackgroundColor(Color.parseColor(color2[j]));
				}
			});
			dialog_2.addView(text1);
		}
		dialog.show();
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
				userId = profile.getId();
				String userName = profile.getName();
				String url = "http://graph.facebook.com/" + userId
						+ "/picture?height=200&width=200";
				SharePreferance share = new SharePreferance(ModernNews.this);
				share.setFbPhoto(url);
				share.setUserName(userName);
				share.setfbID(profile.getId());
				download.download(url, img_iconFB);
			}
		};

		mSimpleFacebook.getProfile(onProfileRequestListener);

	}

	@Override
	protected void onDestroy() {
		isActivityDestroy = true;
		super.onDestroy();
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (Utility.url_photo_FB != null)
			download.download(Utility.url_photo_FB, img_iconFB);
	}

	class asyTaskGetCatsFromSource extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... uri) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try {
				response = httpclient.execute(new HttpGet(uri[0]));
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

			if (isActivityDestroy)
				this.cancel(true);

			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			SplashActivity.cats = new ArrayList<News_Cat>();
			try {
				JSONArray jsonArray = new JSONArray(result);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					int id = jsonObject.getInt("ID");
					String name = jsonObject.getString("name");
					String newestImage = jsonObject.getString("newestImage");
					// String newestImageSize =
					// jsonObject.getString("newestImageSize");

					News_Cat cat = new News_Cat(id, name, newestImage);
					// News_Cat cat = new News_Cat(id,
					// constant.Constants.categories[i], newestImage);
					SplashActivity.cats.add(cat);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			constant.Constants.log_d("TAG", "Modern  News cats-size: "
					+ SplashActivity.cats.size());

			if (SplashActivity.cats.size() == 0)
				Toast.makeText(
						getApplicationContext(),
						getResources().getString(
								R.string.coloixayrakhongthetaiduocdulieu),
						Toast.LENGTH_LONG).show();
			else {
				int i = (SplashActivity.cats.size() - 5);
				if (i % 6 == 0)
					NUM_PAGE = i / 6 + 1;
				else
					NUM_PAGE = i / 6 + 2;
				constant.Constants.log_d("TAG", "NUMPage: " + NUM_PAGE);

				resetActivity();

			}
		}
	}

	private void resetActivity() {
		Intent intent = getIntent();
		overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		overridePendingTransition(0, 0);
		startActivity(intent);
	}

	private void checkSendApi(int j) {
		if (Utility.isInternet(this)) {
			String s1;
			if (isMainCategory) {
				s1 = "http://news.ctyprosoft.com:8081/service.php?action=getArticleMainCategory&categoryID="
						+ SplashActivity.cats.get(j).getmId()
						+ "&numPerPage=15&pageNumber=1";
				if (!constant.Constants.isVietnamese)
					s1 = s1 + "&lang=en";
				new asyTaskGetContents(SplashActivity.cats.get(j).getmName())
						.execute(s1);
			} else {
				s1 = "http://news.ctyprosoft.com:8081/service.php?action=getArticleCategory&categoryID="
						+ SplashActivity.cats.get(j).getmId()
						+ "&numPerPage=15&pageNumber=1";
				if (!constant.Constants.isVietnamese)
					s1 = s1 + "&lang=en";
				new asyTaskGetContents(SplashActivity.cats.get(j).getmName())
						.execute(s1);
			}
		} else
			Utility.showDialog(this,
					getResources().getString(R.string.internetConnection));

	}

	class asynTaskKhoiPhucMacDinh extends AsyncTask<Void, Void, Void> {

		private Dialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = new Dialog(ModernNews.this);
			dialog.setContentView(R.layout.waitingdialog);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {

			SharedPreferences.Editor edit2 = sharedpreferences.edit();
			edit2.clear();
			edit2.commit();

			try {
				TitleListView.imageLoader.clearDiscCache();
				TitleListView.imageLoader.clearMemoryCache();
			} catch (Exception e) {

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			startActivity(new Intent(ModernNews.this, SplashActivity.class));
			finish();
			dialog.dismiss();

			super.onPostExecute(result);
		}
	}

	/**
	 * To show dialog get hot news
	 */
	private void showDialogChonNgonNgu() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this)
				.setTitle("Error")
				.setMessage(getResources().getString(R.string.loikhiloadcat))
				.setCancelable(false)
				.setPositiveButton(getResources().getString(R.string.tailai),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								startActivity(new Intent(ModernNews.this,
										SplashActivity.class));
								finish();
							}
						})
				.setNegativeButton(getResources().getString(R.string.thoat),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
							}
						}).setIcon(android.R.drawable.ic_dialog_alert);
		AlertDialog alertDialog2 = alertDialog.create();
		alertDialog2.show();
	}
}
