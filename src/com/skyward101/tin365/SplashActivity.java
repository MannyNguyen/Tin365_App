package com.skyward101.tin365;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import object.News_Cat;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import utility.JSONParser;
import utility.SharePreferance;
import utility.Utility;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Window;
import com.google.android.gcm.GCMRegistrar;

public class SplashActivity extends VinalyzeFlurry {

	public static ArrayList<News_Cat> cats;
	public static int SPLASH_TIME = 10;

	private SharedPreferences sharedpreferences;
	private static final String SP_NAME = "MY_sharedpreferences";
	private boolean isModern;

	private boolean isActivityDestroy = false;

	SharePreferance share;
	public static String mRegId = "";

	private static final int TIME_OUT = 45000;
	private boolean isHandler = false;

	@Override
	protected void onDestroy() {
		isActivityDestroy = true;
		super.onDestroy();
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		Display display = getWindowManager().getDefaultDisplay();
		constant.Constants.DEVICE_WIDTH = display.getWidth(); // deprecated
		constant.Constants.DEVICE_HEIGHT = display.getHeight(); // deprecated
		constant.Constants.log_d("TAG", "DEVICE_WIDTH: " + display.getWidth());
		constant.Constants
				.log_d("TAG", "DEVICE_HEIGHT: " + display.getHeight());

		try {
			ActionBar acbar = getActionBar();
			acbar.hide();
		} catch (Exception e) {

		}

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				if (!isHandler)
					showConfirmDialog();
			}
		}, TIME_OUT);

		reset();

		sharedpreferences = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		boolean isFirstLaunch = sharedpreferences.getBoolean("isFirstLaunch",
				true);
		if (isFirstLaunch) {
			// if (!isFirstLaunch) {
			SharedPreferences.Editor edit = sharedpreferences.edit();
			edit.putBoolean("isFirstLaunch", false);
			edit.commit();

			showDialogChonNgonNgu();
		} else
			init();
	}

	private void init() {
		if (constant.Constants.isInternet(this)) {
			constant.Constants.log_d("TAG", "isInternet");
			loadLanguage();
			// getKeyHash();

			/**
			 * To get RegId for GCM
			 */
			getRegIdNotification();
			// TODO Register account to server
			constant.Constants.log_d("TAG", "RegID Ok chua: " + mRegId);

			share = new SharePreferance(this);
			Utility.url_photo_FB = share.getFbPhoto();
			// TODO
			// Check shared preference to choose Modern layout or traditional
			// layout

			boolean isFirstRegisterGCM = sharedpreferences.getBoolean(
					"isFirstRegisterGCM", true);

			if (!mRegId.equals("") && isFirstRegisterGCM) {
				new asyTaskRegiterServer().execute();
			} else {

				String link = "http://news.ctyprosoft.com:8081/service.php?action=getListMainCategory";
				if (!constant.Constants.isVietnamese)
					link = link + "&lang=en";
				new asyTaskGetCats().execute(link);

			}
		}

		else {
			showConfirmDialog();
		}

	}

	private void loadLanguage() {
		String lg = "vi";
		boolean isVietnamese = sharedpreferences.getBoolean("isVietnamese",
				true);
		if (!isVietnamese) {
			lg = "en";
			constant.Constants.isVietnamese = false;
		} else
			constant.Constants.isVietnamese = true;

		Resources res = this.getResources();
		// Change locale settings in the app.
		DisplayMetrics dm = res.getDisplayMetrics();
		android.content.res.Configuration conf = res.getConfiguration();
		conf.locale = new Locale(lg);
		res.updateConfiguration(conf, dm);
	}

	// private void getKeyHash() {
	// PackageInfo info;
	// try {
	// info = getPackageManager().getPackageInfo("com.skyward101.tin365",
	// PackageManager.GET_SIGNATURES);
	// for (Signature signature : info.signatures) {
	// MessageDigest md;
	// md = MessageDigest.getInstance("SHA");
	// md.update(signature.toByteArray());
	// String something = new String(Base64.encode(md.digest(), 0));
	// // String something = new
	// // String(Base64.encodeBytes(md.digest()));
	// Log.e("hash key", something);
	// }
	// } catch (NameNotFoundException e1) {
	// Log.e("name not found", e1.toString());
	// } catch (NoSuchAlgorithmException e) {
	// Log.e("no such an algorithm", e.toString());
	// } catch (Exception e) {
	// Log.e("exception", e.toString());
	// }
	// }

	private void showConfirmDialog() {
		// TODO

		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature((int) Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_check_internet);
		dialog.setCancelable(false);

		// set the custom dialog components - text, image and button
		TextView tvThuLai = (TextView) dialog.findViewById(R.id.tvThuLai);
		TextView tvThoat = (TextView) dialog.findViewById(R.id.tvThoat);

		tvThoat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tvThuLai.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				resetActivity();
			}
		});

		dialog.show();

	}

	private void reset() {
		// TODO Auto-generated method stub
		TraditionNews.isMainCategory = true;
	}

	private void resetActivity() {
		Intent intent = getIntent();
		overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		overridePendingTransition(0, 0);
		startActivity(intent);
	}

	private void checkUi() {
		if (android.os.Build.VERSION.SDK_INT < 11) { // Go to tradition layout
			startActivity(new Intent(SplashActivity.this, TraditionNews.class));
		} else {
			sharedpreferences = getSharedPreferences(SP_NAME,
					Context.MODE_PRIVATE);
			isModern = sharedpreferences.getBoolean("isModern", true);
			if (isModern)
				startActivity(new Intent(SplashActivity.this, ModernNews.class));
			else
				startActivity(new Intent(SplashActivity.this,
						TraditionNews.class));
		}

	}

	/**
	 * To get Cats from api
	 */
	class asyTaskGetCats extends AsyncTask<String, String, String> {

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
			cats = new ArrayList<News_Cat>();
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

					cats.add(cat);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			constant.Constants.log_d("TAG",
					"cats-size: " + SplashActivity.cats.size());

			// // TODO set Cat object here
			// // cats = new ArrayList<News_Cat>();
			// cats = constant.Modern.news_Cats;

			// startActivity(new
			// Intent(SplashActivity.this,ModernNews.class));
			if (cats.size() == 0)
				Toast.makeText(
						getApplicationContext(),
						getResources().getString(
								R.string.coloixayrakhongthetaiduocdulieu),
						Toast.LENGTH_LONG).show();
			else {
				isHandler = true;
				checkUi();
				SplashActivity.this.finish();
			}
			super.onPostExecute(result);
		}
	}

	/**
	 * To get mRegId for gcm
	 */
	private void getRegIdNotification() {
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);

		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);

		// Get GCM registration id
		mRegId = GCMRegistrar.getRegistrationId(this);
		// Check if regid already presents
		if (mRegId.equals("")) {

			GCMRegistrar.register(this, GCMIntentService.SENDER_ID);
		}
		Log.d("TAG", "mRegId: " + mRegId);

	}

	/**
	 * To get infor as TokenDevice , GCM RegID, status to register to server
	 */
	private String getInfoToRegister() {
		String android_id = Secure.getString(this.getContentResolver(),
				Secure.ANDROID_ID);
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("os", "android");
			jsonObject.put("status", 1);
			jsonObject.put("deviceToken", mRegId);
			jsonObject.put("deviceID", android_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		constant.Constants.log_d("TAG", "datasend: " + jsonObject.toString());
		return jsonObject.toString();

	}

	/**
	 * Create params before register to server
	 */
	private JSONObject createParam() {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("data", getInfoToRegister()));
		params.add(new BasicNameValuePair("action", "registerPush"));

		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.makeHttpRequest(
				"http://news.ctyprosoft.com:8081/registerPush.php", "POST",
				params);
		constant.Constants.log_d("TAG", "success json: " + json.toString());
		// success = json.getInt(Constant.TAG_SUCCESS);
		return json;
	}

	/**
	 * AsyncTask Register
	 * 
	 * @author My PC
	 * 
	 */
	class asyTaskRegiterServer extends AsyncTask<Void, Void, Void> {
		private JSONObject mRespone;
		private int success;

		@Override
		protected Void doInBackground(Void... params) {
			mRespone = createParam();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			try {
				success = mRespone.getInt("success");
				if (success == 1) {
					SharedPreferences.Editor edit = sharedpreferences.edit();
					edit.putBoolean("isFirstRegisterGCM", false);
					edit.commit();

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			constant.Constants.log_d("TAG", "asyTaskRegiterServer");
			String link = "http://news.ctyprosoft.com:8081/service.php?action=getListMainCategory";
			if (!constant.Constants.isVietnamese)
				link = link + "&lang=en";
			new asyTaskGetCats().execute(link);
			super.onPostExecute(result);
		}
	}

	/**
	 * To show dialog get hot news
	 */
	private void showDialogChonNgonNgu() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this)
				.setTitle("Notice")
				.setMessage(
						("Vui lòng chọn ngôn ngữ\n(Please choose your language)"))
				.setCancelable(false)
				.setPositiveButton("Tiếng Việt",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								SharedPreferences.Editor edit = sharedpreferences
										.edit();
								edit.putBoolean("isVietnamese", true);
								edit.commit();
							}
						})
				.setNegativeButton("English",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								SharedPreferences.Editor edit = sharedpreferences
										.edit();
								edit.putBoolean("isVietnamese", false);
								edit.commit();
							}
						}).setIcon(android.R.drawable.ic_dialog_alert);
		AlertDialog alertDialog2 = alertDialog.create();
		alertDialog2.show();
		alertDialog2.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface arg0) {
				constant.Constants.log_d("TAG",
						"Dialog Chon ngon ngu onDismiss");
				init();
			}
		});
	}

}
