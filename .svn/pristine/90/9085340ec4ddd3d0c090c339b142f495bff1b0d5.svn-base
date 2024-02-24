package com.skyward101.tin365;

import com.actionbarsherlock.app.SherlockActivity;
import com.flurry.android.FlurryAgent;

import android.Vietnalyze;
import android.app.Activity;
import android.util.Log;

public class VinalyzeFlurry extends SherlockActivity {
	@Override
	 public void onStart()
	    {
	       super.onStart();
	       Log.i("Flurry", "onStart second");
	       FlurryAgent.onStartSession(this, "YD9XG88S3BBSHG42P5HD");
	       FlurryAgent.setLogEnabled(true);
	       FlurryAgent.setLogEvents(true);
	       FlurryAgent.setLogLevel(Log.VERBOSE);
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
