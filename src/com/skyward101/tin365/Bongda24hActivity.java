package com.skyward101.tin365;

import java.util.ArrayList;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.flurry.android.FlurryAgent;
import com.skyward101.tin365.R;

import object.Bookmark;
import object.News_Content;
import object.News_Detail;
import utility.DataBaseHelper;
import utility.GetJsonFromAPI;
import utility.Utility;
import adapter.Bongda24hAdapter;
import adapter.BookmarkAdapter;
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
import android.widget.Toast;

public class Bongda24hActivity extends SherlockFragmentActivity implements OnScrollListener,
		OnItemClickListener {
	Context context = this;
	// variable listview
	ListView lv_bookmark;
	public static String mActionbar_color;
	Bookmark object_bookmark;
	Bongda24hAdapter adapter_bookmark;
	public static ArrayList<News_Content> array_bookmark;
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
	int _size_array = 0;
	int flag = 0;
	// share
	private SharedPreferences sharedpreferences;
	private static final String SP_NAME = "MY_sharedpreferences";
	private int mFontSize;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bookmark);

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
	}

	private void initListview() {
		lv_bookmark = (ListView) findViewById(R.id.lv_bookmark);
		lv_bookmark.setOnScrollListener(this);

		// dialog
		dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.waitingdialog);
		dialog.setCancelable(false);
		// databse
		db = new DataBaseHelper(context);
		array_bookmark = new ArrayList<News_Content>();
		adapter_bookmark = new Bongda24hAdapter(context, array_bookmark);
		lv_bookmark.setAdapter(adapter_bookmark);
		lv_bookmark.setOnItemClickListener(Bongda24hActivity.this);
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
		if(firstVisibleItem + visibleItemCount == 20*currentPage)
		{
//			Toast.makeText(context, "load more", 500).show();
//			currentPage = currentPage  + 1;
//			new bg_getBookmark().execute();
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int scrollState) {
		// TODO Auto-generated method stub
//		boolean flag = (currentPage == totalPage + 1);
		System.out.println(currentPage + " " + totalPage + "  " + flag);
		if (firstVisiblePosition == (totalItem - visibleItem)
				&& scrollState == 0 && flag != 10) {
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
			totalPage = db.GetTotalBookMark(Utility._str_table_Bookmark);
//			array_bookmark.removeAll(array_bookmark);
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			array_bookmark.addAll(GetJsonFromAPI.getfootballList("http://news.ctyprosoft.com:8081/service.php?action=getArticleCategory&categoryID=174&numPerPage=20&pageNumber="+currentPage));
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(flag == 0 )
			{
				_size_array = array_bookmark.size();
				flag = 1;
			}
			else
			{
				if(_size_array == array_bookmark.size())
					flag = 10;//cause for dont load more
				else
					_size_array = array_bookmark.size();
			}
				
			adapter_bookmark.notifyDataSetChanged();
			/*lv_bookmark.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					lv_bookmark.setSelection(positionListView);
					lv_bookmark.requestFocus();
				}
			});*/

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
		if(Utility.isInternet(context))
		{
			String ArticalId = String.valueOf(array_bookmark.get(arg2).getID());
			Intent it = new Intent(context,Bongda24hDetailActivity.class);
			it.putExtra("artical", ArticalId);
			startActivity(it);
		}
		else
			Utility.showDialog(context, getResources().getString(R.string.internetConnection));
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