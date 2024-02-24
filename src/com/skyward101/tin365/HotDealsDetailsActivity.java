package com.skyward101.tin365;

import io.vov.vitamio.utils.Log;
import android.Vietnalyze;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.skyward101.tin365.R;

public class HotDealsDetailsActivity extends SherlockFragmentActivity implements
		OnClickListener {

	private SharedPreferences sharedpreferences;
	private ActionBar actionBar;
	private String url;
	boolean _admode = false;
	private static final String SP_NAME = "MY_sharedpreferences";
	public static String mActionbar_color;
	/** The view to show the ad. */
	private AdView adView;
	//
	Dialog dialog;
	WebView myWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_hot_deals_details);

		init();
		initActionBar();
		
		
	}

	private void init() {

		// sharedpreferences
		sharedpreferences = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		mActionbar_color = sharedpreferences.getString("actionbar_color",
				constant.Constants.COLOR_ACTION_BAR);
		
		Intent intent = getIntent();
		url = intent.getStringExtra("url");
		try {
			_admode = intent.getBooleanExtra("admod", false);
		} catch (Exception e) {
			// TODO: handle exception
		}
		myWebView = (WebView) findViewById(R.id.webView1);
		myWebView.setWebViewClient(new myWebClient());
		myWebView.getSettings().setJavaScriptEnabled(true);
		myWebView.getSettings().setUseWideViewPort(true);
		myWebView.getSettings().setBuiltInZoomControls(true);
		myWebView.loadUrl(url);
		if(!_admode)
		{
			 // Create an ad.
		    adView = (AdView)findViewById(R.id.adView);

		    // Create an ad request. Check logcat output for the hashed device ID to
		    // get test ads on a physical device.
		    AdRequest adRequest = new AdRequest.Builder()
		        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
		        .addTestDevice("INSERT_YOUR_HASHED_DEVICE_ID_HERE")
		        .build();

		    // Start loading the ad in the background.
		    adView.loadAd(adRequest);
		}
		else
		{
			 adView = (AdView)findViewById(R.id.adView);
			 adView.setVisibility(View.GONE);
		}

	}

	private void initActionBar() {
		actionBar = getSupportActionBar();
		actionBar.hide();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {

		case R.id.llIcon:
			finish();
			break;

		default:
			break;
		}
	}
	public class myWebClient extends WebViewClient
	{
	    @Override
	    public void onPageStarted(WebView view, String url, Bitmap favicon) {
	        // TODO Auto-generated method stub
	        super.onPageStarted(view, url, favicon);
	        view.clearView();
	        if (dialog == null) {
				dialog = new Dialog(HotDealsDetailsActivity.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.waitingdialog);
				dialog.show();
			}
	    }

	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        // TODO Auto-generated method stub

	        view.loadUrl(url);
	        return true;

	    }
	    @Override
	    public void onPageFinished(WebView view, String url) {
	    	// TODO Auto-generated method stub
	    	super.onPageFinished(view, url);
	    	dialog.dismiss();

	    }
	}
	@Override
	 public void onStart()
	    {
	       super.onStart();
	       Log.i("Flurry", "onStart second");
	       FlurryAgent.onStartSession(this, "YD9XG88S3BBSHG42P5HD");
	       FlurryAgent.setLogEnabled(true);
	       FlurryAgent.setLogEvents(true);
	       // your code
	       
	       //Vietnalyze
	       Vietnalyze.onStartSession(this, "ZW9SHTZ4PYX5UI7P4FVG");
	    }
	@Override
	    public void onStop()
	    {
	       super.onStop();
	       Log.i("Flurry", "onStop");
	       FlurryAgent.onEndSession(this);
	       // your code
	       
	       Vietnalyze.onEndSession(this);
	    }

}
