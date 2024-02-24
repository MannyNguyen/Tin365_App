/*
 * Copyright (C) 2012 YIXIA.COM
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skyward101.tin365;

import java.util.ArrayList;

import utility.SharePreferance;

import com.flurry.android.FlurryAgent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.skyward101.tin365.R;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import android.Vietnalyze;
import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class VideoViewActivity extends Activity implements OnClickListener {
	boolean checkedSession = false;
	boolean is3G = false;
	boolean isWifi = false;
	boolean connection = false;
	private String path = "";
	private String title = "";
	private VideoView mVideoView;
	private TextView tvTitle;
	LinearLayout layoutBrightness;
	LinearLayout layoutVolume;
	LinearLayout layoutTime;
	RelativeLayout layoutTopBar, layout_next_pre;
	ImageButton btn_favorite;
	boolean checkVideoExisted;
	/** The view to show the ad. */
	private AdView adView;
	SharePreferance share;
	int pos;
	Context context = this;
	ImageView next, pre, img_Guide;
	ArrayList<String> array_url;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.videoview);
		// Create an ad.
		adView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice("INSERT_YOUR_HASHED_DEVICE_ID_HERE").build();

		// Start loading the ad in the background.
		adView.loadAd(adRequest);
		share = new SharePreferance(context);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this))
			return;
		Bundle bundle = getIntent().getExtras();
		pos = bundle.getInt("pos");
		array_url = bundle.getStringArrayList("array");
		path = array_url.get(pos);
		// title = bundle.getString("title");

		tvTitle = (TextView) findViewById(R.id.tv_title_video);
		tvTitle.setText(title);
		// init next & previous
		next = (ImageView) findViewById(R.id.next);
		pre = (ImageView) findViewById(R.id.previous);
		img_Guide = (ImageView) findViewById(R.id.imgGuide);
		img_Guide.setOnClickListener(this);
		next.setOnClickListener(this);
		pre.setOnClickListener(this);
		layoutTopBar = (RelativeLayout) findViewById(R.id.layout_topbar_videoview);
		layoutBrightness = (LinearLayout) findViewById(R.id.layout_video_info_brightness);
		layoutVolume = (LinearLayout) findViewById(R.id.layout_video_info_volume);
		layoutTime = (LinearLayout) findViewById(R.id.layout_video_info_time);
		layout_next_pre = (RelativeLayout) findViewById(R.id.layout_next_pre);
		findViewById(R.id.imb_back).setOnClickListener(this);

		mVideoView = (VideoView) findViewById(R.id.surface_view);
		mVideoView.setLayoutInfo(layoutBrightness, layoutVolume, layoutTime);
		mVideoView.setVideoPath(path);
		mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);
		mVideoView.setMediaController(new MediaController(this), layoutTopBar,
				layout_next_pre);
		mVideoView.requestFocus();
		mVideoView.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				if (pos < array_url.size() - 1) {
					NextVideo();
				} else
					finish();
			}
		});
		startPlayingVideo();

	}

	private void NextVideo() {
		pos++;
		reInitVideo();
	}

	private void PreVideo() {
		pos--;
		reInitVideo();
	}

	@Override
	public void onResume() {
		System.out.println("onResume");
		super.onResume();
	}

	@Override
	protected void onPause() {
		System.out.println("onPause");
		mVideoView.pause();
		super.onPause();
	}

	@Override
	protected void onStop() {
		System.out.println("onStop");
		super.onStop();
		Log.i("Flurry", "onStop");
		FlurryAgent.onEndSession(this);
		// your code

		Vietnalyze.onEndSession(this);
	}

	@Override
	public void onStart() {
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
	protected void onDestroy() {
		System.out.println("onDestroy");
		if (Vitamio.isInitialized(VideoViewActivity.this))
			mVideoView.stopPlayback();
		super.onDestroy();
	}

	private void startPlayingVideo() {
		if (hasConnection(getApplicationContext())) {
			mVideoView.start();
			if(!share.getStatusfirstVideo())
				img_Guide.setVisibility(View.VISIBLE);

		} else {
			showDialog(VideoViewActivity.this, "Network Error",
					"Please check network connection", "OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							finish();
						}
					});
		}
	}// end startPlayingVideo

	// @Override
	// public boolean onTouchEvent(MotionEvent event) {
	// return super.onTouchEvent(event);
	// }

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if (mVideoView != null) {
			mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
			mVideoView.setScreenResolution(VideoViewActivity.this);
		}
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgGuide:
			share.setStatusfirstVideo(true);
			img_Guide.setVisibility(View.GONE);
			break;
		case R.id.imb_back:
			finish();
			break;
		case R.id.next:
			if (pos < array_url.size() - 1)
				NextVideo();
			break;
		case R.id.previous:
			if (pos > 0)
				PreVideo();
			break;

		default:
			break;
		}
	}

	public static boolean hasConnection(Context ctx) {
		ConnectivityManager manager = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// For 3G check
		boolean is3g = false;
		try {
			is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
					.isConnectedOrConnecting();
		} catch (Exception e) {
			// TODO: handle exception
		}
		// For WiFi Check
		boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.isConnectedOrConnecting();

		if (is3g || isWifi)
			return true;
		else
			return false;
	}

	public static void showDialog(Context ctx, String title, String msg,
			String mPosText, DialogInterface.OnClickListener mPoslistener) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
		alertDialog.setTitle(title);
		alertDialog.setMessage(msg);
		alertDialog.setPositiveButton(mPosText, mPoslistener);
		alertDialog.create().show();
	}

	private void reInitVideo() {

		path = array_url.get(pos);
		setContentView(R.layout.videoview);
		// Create an ad.
		adView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice("INSERT_YOUR_HASHED_DEVICE_ID_HERE").build();

		// Start loading the ad in the background.
		adView.loadAd(adRequest);
		// init next & previous
		next = (ImageView) findViewById(R.id.next);
		pre = (ImageView) findViewById(R.id.previous);
		next.setOnClickListener(this);
		pre.setOnClickListener(this);
		layoutTopBar = (RelativeLayout) findViewById(R.id.layout_topbar_videoview);
		layoutBrightness = (LinearLayout) findViewById(R.id.layout_video_info_brightness);
		layoutVolume = (LinearLayout) findViewById(R.id.layout_video_info_volume);
		layoutTime = (LinearLayout) findViewById(R.id.layout_video_info_time);
		layout_next_pre = (RelativeLayout) findViewById(R.id.layout_next_pre);
		findViewById(R.id.imb_back).setOnClickListener(this);
		mVideoView = (VideoView) findViewById(R.id.surface_view);
		mVideoView.setLayoutInfo(layoutBrightness, layoutVolume, layoutTime);
		mVideoView.setVideoPath(path);
		mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);
		mVideoView.setMediaController(new MediaController(this), layoutTopBar,
				layout_next_pre);
		mVideoView.requestFocus();
		mVideoView.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				if (pos < array_url.size() - 1) {
					NextVideo();
				} else
					finish();
			}
		});
	}
}
