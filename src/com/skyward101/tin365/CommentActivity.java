package com.skyward101.tin365;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import object.Bookmark;
import object.Comment;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utility.GetJsonFromAPI;
import utility.SharePreferance;
import utility.Utility;

import adapter.CommentAdapter;
import android.Vietnalyze;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.facebook.Session;
import com.flurry.android.FlurryAgent;
import com.skyward101.tin365.R;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebook.OnLoginListener;

public class CommentActivity extends SherlockFragmentActivity implements
		OnScrollListener, OnItemClickListener {
	Context context = this;
	// variable listview
	ListView lv_cmt;
	public static String mActionbar_color;
	Bookmark object_cmt;
	CommentAdapter adapter_cmt;
	ArrayList<Comment> array_cmt;
	// parse page
	private int currentPage = 1;
	private int positionListView = 0;
	private static int totalPage = 0;
	// dialog
	Dialog dialog;
	// action bar
	ActionBar actionBar;
	// edit
	static EditText et_content;
	String content = "";
	// share
	private SharedPreferences sharedpreferences;
	private static final String SP_NAME = "MY_sharedpreferences";
	private int mFontSize;
	SimpleFacebook mSimpleFacebook;
	String articalID;
	int _cmt_number = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cmt);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		initListview();
		init();
		initActionBar();

		initInfoFb();
		new bg_getCmt().execute();
		
	}

	private void init() {
		sharedpreferences = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		mActionbar_color = sharedpreferences.getString("actionbar_color",
				constant.Constants.COLOR_ACTION_BAR);

		mFontSize = sharedpreferences.getInt("font_size",
				constant.Constants.FONT_SIZE);
		Intent intent = getIntent();
		articalID = intent.getStringExtra("articleID");
	}

	private void initInfoFb() {
		SharePreferance share = new SharePreferance(context);
		Utility.url_photo_FB = share.getFbPhoto();
		Utility.user_name = share.getUserName();
		Utility.fbID = share.getfbID();
		et_content = (EditText) findViewById(R.id.edit_content);
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
				.getString(R.string.title_activity_conment));
		v.findViewById(R.id.llIcon).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResultActivity();
			}
		});

	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		setResultActivity();
		super.onBackPressed();
	}
	private void setResultActivity()
	{
		Intent intent=new Intent();  
        intent.putExtra("MESSAGE",String.valueOf(_cmt_number));  
        setResult(1,intent);  
		finish();
	}
	private void initListview() {
		lv_cmt = (ListView) findViewById(R.id.lv_cmt);
		lv_cmt.setOnScrollListener(this);

		// dialog
		dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.waitingdialog);
		dialog.setCancelable(false);
		array_cmt = new ArrayList<Comment>();
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
			new bg_getCmt().execute();
			positionListView = totalItem - visibleItem;
		}
	}
	
	class bg_getCmt extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog.show();
			array_cmt.removeAll(array_cmt);
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			array_cmt = GetJsonFromAPI
					.getCmtList("http://news.ctyprosoft.com:8081/"
							+ "service.php?action=getComment&articleID="+articalID+"&order=desc");
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			adapter_cmt = new CommentAdapter(context, array_cmt);
			_cmt_number = array_cmt.size();
			lv_cmt.setAdapter(adapter_cmt);
			lv_cmt.setOnItemClickListener(CommentActivity.this);
			lv_cmt.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					lv_cmt.setSelection(positionListView);
					lv_cmt.requestFocus();
				}
			});

			// cancel Dialog
			dialog.cancel();
		}

	}

	@SuppressLint("SimpleDateFormat")
	public void sendSms(View view) {
		mSimpleFacebook = SimpleFacebook.getInstance(CommentActivity.this);
		if (!mSimpleFacebook.isLogin())
			mSimpleFacebook.login(mOnLoginListener);
		else
		{
			postcmt();
		}
		

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
//				getProfileExample();
				postcmt();

			}

			@Override
			public void onNotAcceptingPermissions() {
				// toast("You didn't accept read permissions");
			}
		};
		private void postcmt()
		{
			_cmt_number = _cmt_number + 1;
			String timenow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			//disappear
			InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			mgr.hideSoftInputFromWindow(et_content.getWindowToken(), 0);
			content = et_content.getText().toString();
			et_content.setText("");
			//add item in array
			Comment cmt = new Comment("27351", Utility.url_photo_FB,Utility.user_name, timenow , content);
			array_cmt.add(0, cmt);
			adapter_cmt.notifyDataSetChanged();
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://news.ctyprosoft.com:8081/getComment.php");
//			MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,null,Charset.forName("UTF-8"));
			// create json object
			JSONObject cmt_object = new JSONObject();
			ArrayList<String> arr = null;
			JSONObject comment_oject = null;
			try {

			/*	cmt_object.put("faceID", URLEncoder.encode(Utility.fbID, "UTF-8"));
				cmt_object.put("avatar",URLEncoder.encode(Utility.url_photo_FB, "UTF-8"));
				cmt_object.put("content", URLEncoder.encode(et_content.getText().toString(), "UTF-8"));
				cmt_object.put("articleID", URLEncoder.encode("12", "UTF-8"));
				cmt_object.put("faceName",URLEncoder.encode( Utility.user_name, "UTF-8"));*/

				cmt_object.put("faceID", Utility.fbID);
				cmt_object.put("avatar", Utility.url_photo_FB);
				// cmt_object.put("commentTime", timenow);
				cmt_object.put("faceName", Utility.user_name);
//				cmt_object.put("faceName", "Tam");
				cmt_object.put("content",content);
				cmt_object.put("articleID", articalID);
				// create json array
				JSONArray cmt_array = new JSONArray();
				cmt_array.put(cmt_object);

				comment_oject = new JSONObject();
				comment_oject.put("data", cmt_object);

				arr = new ArrayList<String>();
				Iterator iter = comment_oject.keys();
				while (iter.hasNext()) {
					String key = (String) iter.next();
					arr.add(comment_oject.getString(key));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
//				entity.addPart("data", new StringBody(arr.toString()));
				// Add your data
				System.out.println("data=" + arr.toString());
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("data", arr.toString()));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				String responseString = EntityUtils.toString(response.getEntity());
				System.out.println("screen=" + responseString.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
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