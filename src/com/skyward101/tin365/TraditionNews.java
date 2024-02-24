package com.skyward101.tin365;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import object.News_Cat;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import utility.ImageDownloaderMainBanners;
import utility.SharePreferance;
import utility.Utility;

import bongda365.BongDa365Activity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.Session;
import com.facebook.widget.LoginButton;
import com.flurry.android.FlurryAgent;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.skyward101.tin365.R;
import com.skyward101.tin365.ModernNews.asynTaskKhoiPhucMacDinh;
import com.slidingmenu.lib.SlidingMenu;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebook.OnLoginListener;
import com.sromku.simple.fb.SimpleFacebook.OnLogoutListener;
import com.sromku.simple.fb.SimpleFacebook.OnProfileRequestListener;
import com.sromku.simple.fb.entities.Profile;

import adapter.TitleListView;
import adapter.TraditionTitle;
import android.Vietnalyze;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class TraditionNews extends SherlockFragmentActivity implements
		OnGlobalLayoutListener, OnClickListener {

	public static boolean isMainCategory = true;

	private ActionBar actionBar;
	private ViewPager viewPager;
	private SlidingMenu menu;

	private int pageSelected = 0;
	private String mActionbar_color;

	private SharedPreferences sharedpreferences;
	private static final String SP_NAME = "MY_sharedpreferences";
	private LinearLayout mActionBar_LinearLayout;
	private LinearLayout llSpinnerDialog;
	// private Spinner spinner;
	private boolean isModern;
	private String color; // The color was selected
	// Login listener

	ImageDownloaderMainBanners download;
	SimpleFacebook mSimpleFacebook;
	LoginButton btn_fb_login;
	ImageView img_iconFB;
	String userId;
	SharePreferance share;
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
			Toast.makeText(TraditionNews.this, "logout", 4000).show();
			img_iconFB.setImageResource(R.drawable.user);
		}

	};

	@Override
	public void onBackPressed() {

		if (menu.isMenuShowing())
			menu.toggle();
		else
			super.onBackPressed();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tradition_news);
		share = new SharePreferance(TraditionNews.this);
		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setOffscreenPageLimit(2);
		viewPager.setOnPageChangeListener(onPageChangeListener);
		viewPager.setAdapter(new TraditionTitle(getSupportFragmentManager()));
		// addActionBarTabs();
		initActionBar();
		initSlidingMenu();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
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

		isModern = sharedpreferences.getBoolean("isModern", true);

		// Setting action
		LinearLayout sliding_menu_caidat = (LinearLayout) findViewById(R.id.sliding_menu_caidat);
		LinearLayout sliding_menu_caidat_mautieude = (LinearLayout) findViewById(R.id.sliding_menu_caidat_mautieude);

		LinearLayout sliding_menu_caidat_giaodien = (LinearLayout) findViewById(R.id.sliding_menu_caidat_giaodien);

		LinearLayout sliding_menu_gioithieu = (LinearLayout) findViewById(R.id.sliding_menu_gioithieu);

		TextView sliding_menu_caidat_mautieude_tv_bg = (TextView) findViewById(R.id.sliding_menu_caidat_mautieude_tv_bg);
		sliding_menu_caidat_mautieude_tv_bg.setBackgroundColor(Color
				.parseColor(mActionbar_color));

		if (!isModern) {
			((TextView) findViewById(R.id.sliding_menu_caidat_giaodien_tv))
					.setText(getResources().getString(R.string.truyenthong));
			((ImageView) findViewById(R.id.sliding_menu_caidat_giaodien_img))
					.setImageResource(R.drawable.layout_list);
		}

		sliding_menu_caidat_mautieude.setOnClickListener(this);
		sliding_menu_caidat_giaodien.setOnClickListener(this);
		sliding_menu_gioithieu.setOnClickListener(this);
		sliding_menu_caidat.setOnClickListener(this);
		findViewById(R.id.sliding_menu_source).setOnClickListener(this);
		findViewById(R.id.sliding_menu_themnguon).setOnClickListener(this);
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
		mSimpleFacebook = SimpleFacebook.getInstance(TraditionNews.this);
		btn_fb_login = (LoginButton) findViewById(R.id.login_icon);
		btn_fb_login.setOnClickListener(TraditionNews.this);

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

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("Flurry", "onStop");
		FlurryAgent.onEndSession(this);
		// your code

		Vietnalyze.onEndSession(this);
	}

	/**
	 * To set all source that it was saved from shared preference
	 */
	private void setSource() {
		String add_source = sharedpreferences.getString("add_source", "");
		constant.Constants.log_d("TAG", "add Source: " + add_source);

		if (add_source.equals(""))
			return;

		LinearLayout llAddSource = (LinearLayout) findViewById(R.id.sliding_menu_source_sub);

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

	int left;
	int widthllTvSpinner;
	int widthSpinner;

	int widthLlHome;
	int heightLlHome;

	LinearLayout llTvSpinner;
	TextView tv;
	LinearLayout llHome;

	String[] topics;
	public static String cat;
	public static int catId;

	/**
	 * To init action bar
	 */
	private void initActionBar() {

		sharedpreferences = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		mActionbar_color = sharedpreferences.getString("actionbar_color",
				constant.Constants.COLOR_ACTION_BAR);

		// configure action bar
		// action bar
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);

		LayoutInflater inflator = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View v = inflator.inflate(R.layout.list_nav_layout_tradition,
				null);

		com.actionbarsherlock.app.ActionBar.LayoutParams params = new com.actionbarsherlock.app.ActionBar.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		actionBar.setCustomView(v, params);
		actionBar.setDisplayShowCustomEnabled(true);

		// Get Position of Spinner Dialog
		mActionBar_LinearLayout = (LinearLayout) v
				.findViewById(R.id.llActionBar);
		mActionBar_LinearLayout.setBackgroundColor(Color
				.parseColor(mActionbar_color));
		tv = (TextView) v.findViewById(R.id.textView1);
		llHome = (LinearLayout) v.findViewById(R.id.llIcon);
		llTvSpinner = (LinearLayout) v.findViewById(R.id.llTvSpinner);
		final ImageView img_down = (ImageView) v.findViewById(R.id.img_down);

		llHome.getViewTreeObserver().addOnGlobalLayoutListener(this);
		llTvSpinner.getViewTreeObserver().addOnGlobalLayoutListener(this);

		v.findViewById(R.id.img_coupon).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						startActivity(new Intent(TraditionNews.this,
								HotDealsActivity.class));
						;
					}
				});

		v.findViewById(R.id.llIcon).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				menu.toggle();
			}
		});

		try {
			topics = new String[SplashActivity.cats.size()];
			for (int i = 0; i < SplashActivity.cats.size(); i++)
				topics[i] = SplashActivity.cats.get(i).getmName();
		} catch (Exception e) {
			showDialogChonNgonNgu();
			return;
		}

		constant.Constants.log_d("TAG",
				"cats.size() : " + SplashActivity.cats.size());
		constant.Constants.log_d("TAG", "topics.name() : "
				+ SplashActivity.cats.get(0).getmName());

		tv.setText(SplashActivity.cats.get(0).getmName());
		cat = SplashActivity.cats.get(0).getmName();
		constant.Constants.log_d("TAG", "cat: " + cat);

		llTvSpinner.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				img_down.setImageResource(R.drawable.icon_up);

				final Dialog dialog = new Dialog(TraditionNews.this,
						android.R.style.Theme_Panel);

				dialog.setContentView(R.layout.popup);
				dialog.setCancelable(true);
				dialog.setCanceledOnTouchOutside(true);
				// final FrameLayout llLinearLayout = (FrameLayout) dialog
				// .findViewById(R.id.fl_dialog);
				llSpinnerDialog = (LinearLayout) dialog
						.findViewById(R.id.llSpinnerDialog);

				ListView lvSpinner = (ListView) dialog
						.findViewById(R.id.lvSpinnerDialog);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						TraditionNews.this, R.layout.item_spinner_dialog,
						topics);
				llSpinnerDialog.setBackgroundColor(Color
						.parseColor(mActionbar_color));
				if (color != null)
					llSpinnerDialog.setBackgroundColor(Color.parseColor(color));
				lvSpinner.setAdapter(adapter);
				lvSpinner.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int pos, long arg3) {
						dialog.dismiss();
						cat = topics[pos];
						catId = SplashActivity.cats.get(pos).getmId();
						Log.d("TAG", "onItemClick- cat: " + cat);
						Log.d("TAG", "onItemClick- catId: " + catId);

						tv.setText(topics[pos]);
						constant.Constants.log_d("TAG", "Spinner cat: " + cat);
						viewPager.setCurrentItem(pos, true);
					}

				});

				// llSpinnerDialog.getViewTreeObserver()
				// .addOnGlobalLayoutListener(DanTriNewsActivity.this);

				WindowManager.LayoutParams params = dialog.getWindow()
						.getAttributes();
				params.y = heightLlHome;
				params.x = left;
				params.gravity = Gravity.TOP | Gravity.LEFT;
				dialog.getWindow().setAttributes(params);

				dialog.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface dialog) {
						img_down.setImageResource(R.drawable.icon_down);
					}
				});
				dialog.show();

			}
		});

		// Check to show dialog get notification
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

		if (constant.Constants.isVietnamese)
			v.findViewById(R.id.img_coupon).setVisibility(View.VISIBLE);
		else
			v.findViewById(R.id.img_coupon).setVisibility(View.GONE);
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

	private ViewPager.SimpleOnPageChangeListener onPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
		@Override
		public void onPageSelected(int position) {
			super.onPageSelected(position);
			cat = topics[position];
			catId = SplashActivity.cats.get(position).getmId();

			tv.setText(topics[position]);
			constant.Constants.log_d("TAG", "Pager cat : " + cat);
			constant.Constants.log_d("TAG", "onPageSelected: " + position);
			pageSelected = position;
		}
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onGlobalLayout() {
		widthllTvSpinner = llTvSpinner.getWidth();

		widthLlHome = llHome.getWidth();
		heightLlHome = llHome.getHeight();

		widthSpinner = (int) (getResources()
				.getDimension(R.dimen.spinner_width));

		left = widthLlHome + widthllTvSpinner / 2 - widthSpinner / 2;

	}

	@Override
	public void onClick(View view) {

		View v;
		switch (view.getId()) {
		case R.id.ll_contact:
			Vietnalyze
					.logEvent(getResources().getString(R.string.eventcontact));
			FlurryAgent.logEvent(getResources()
					.getString(R.string.eventcontact));
			Intent it = new Intent(TraditionNews.this,
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

			startActivity(new Intent(TraditionNews.this,
					BongDa365Activity.class));
			break;
		case R.id.linear_videobanthang:
			Vietnalyze.logEvent(getResources().getString(
					R.string.eventvideoGoal));
			FlurryAgent.logEvent(getResources().getString(
					R.string.eventvideoGoal));
			if (Utility.isInternet(this))
				startActivity(new Intent(TraditionNews.this,
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
			startActivity(new Intent(TraditionNews.this, BookmarkActivity.class));
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
					R.string.eventchangeAppViewtoModern));
			FlurryAgent.logEvent(getResources().getString(
					R.string.eventchangeAppViewtoModern));
			TextView sliding_menu_caidat_giaodien_tv = (TextView) findViewById(R.id.sliding_menu_caidat_giaodien_tv);

			sliding_menu_caidat_giaodien_tv.setText(getResources().getString(
					R.string.hiendai));
			SharedPreferences.Editor edit = sharedpreferences.edit();
			edit.putBoolean("isModern", true);
			edit.commit();
			if (Utility.isInternet(this)) {
				startActivity(new Intent(TraditionNews.this,
						SplashActivity.class));
				TraditionNews.this.finish();
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
			Intent add_source = new Intent(TraditionNews.this,
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
						TraditionNews.this,
						getResources().getString(
								R.string.tat_chuc_nang_nhan_tin_hot),
						Toast.LENGTH_SHORT).show();
				((ImageView) findViewById(R.id.sliding_menu_caidat_notification_img))
						.setImageResource(R.drawable.off);
			} else {
				edit2.putBoolean("getNotification", true);
				edit2.commit();

				Toast.makeText(
						TraditionNews.this,
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

			startActivity(new Intent(TraditionNews.this, SplashActivity.class));
			TraditionNews.this.finish();

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
					color = color1[j];
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
					color = color2[j];
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
				String userName = profile.getUsername();
				String url = "http://graph.facebook.com/" + userId
						+ "/picture?height=200&width=200";
				SharePreferance share = new SharePreferance(TraditionNews.this);
				share.setFbPhoto(url);
				share.setUserName(userName);
				share.setfbID(profile.getId());
				download.download(url, img_iconFB);
			}
		};

		mSimpleFacebook.getProfile(onProfileRequestListener);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (Utility.url_photo_FB != null)
			download.download(Utility.url_photo_FB, img_iconFB);
	}

	@Override
	protected void onDestroy() {
		isActivityDestroy = true;
		super.onDestroy();
	}

	private boolean isActivityDestroy = false;

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
					SplashActivity.cats.add(cat);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			constant.Constants.log_d("TAG", "Tradition News cats-size: "
					+ SplashActivity.cats.size());

			if (SplashActivity.cats.size() == 0)
				Toast.makeText(
						getApplicationContext(),
						getResources().getString(
								R.string.coloixayrakhongthetaiduocdulieu),
						Toast.LENGTH_LONG).show();
			else {
				viewPager.setAdapter(new TraditionTitle(
						getSupportFragmentManager()));
				initActionBar();
				initSlidingMenu();
			}
		}
	}

	class asynTaskKhoiPhucMacDinh extends AsyncTask<Void, Void, Void> {

		private Dialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = new Dialog(TraditionNews.this);
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

			startActivity(new Intent(TraditionNews.this, SplashActivity.class));
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
								startActivity(new Intent(TraditionNews.this,
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
