package bongda365;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

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

import object.News_Content;

import adapter.Bongda24hAdapter;
import android.Vietnalyze;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import bongda365.BongDa365Activity.asyTaskGetItem;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.flurry.android.FlurryAgent;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.skyward101.tin365.R;

public class ThuVienVideo extends SherlockActivity implements
		OnItemClickListener {

	private int totalItemCountOld = 0;
	private boolean loadingMore = false;
	private int page = 1;

	String video_url = "http://news.ctyprosoft.com:8081/service.php?action=getArticleMainCategory&categoryID=31&numPerPage=15&pageNumber=###&type=video";

	ProgressBar progressBarWaiting;
	ListView lvFragmetTitle;
	ProgressBar progressBarLoadMore;

	public static ImageLoader imageLoader_ThuVienVideo;

	private ArrayList<News_Content> mListVideos;
	private Bongda24hAdapter bongda24hAdapter;

	public static String mActionbar_color;
	// share
	private SharedPreferences sharedpreferences;
	private static final String SP_NAME = "MY_sharedpreferences";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_thu_vien_video);

		// ///////////////
		progressBarLoadMore = (ProgressBar) findViewById(R.id.progressBarLoadMore);
		progressBarWaiting = (ProgressBar) findViewById(R.id.progressBarWaiting);
		lvFragmetTitle = (ListView) findViewById(R.id.lvFragmetTitle);

		// ///////////////

		init();
		initActionBar();

	}

	private void init() {
		sharedpreferences = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		mActionbar_color = sharedpreferences.getString("actionbar_color",
				constant.Constants.COLOR_ACTION_BAR);

		// //////////////////
		// ListView
		mListVideos = new ArrayList<News_Content>();
		bongda24hAdapter = new Bongda24hAdapter(this, mListVideos);
		lvFragmetTitle.setAdapter(bongda24hAdapter);
		lvFragmetTitle.setOnItemClickListener(this);

		// ///////////////////
		// GetData
		new asyTaskGetItem().execute(video_url.replace("###", "" + page));
	}

	@SuppressLint("NewApi")
	private void initActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);

		LayoutInflater inflator = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View v = inflator.inflate(R.layout.actionbar_bongda365, null);

		com.actionbarsherlock.app.ActionBar.LayoutParams params = new com.actionbarsherlock.app.ActionBar.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		actionBar.setCustomView(v, params);
		actionBar.setDisplayShowCustomEnabled(true);

		((TextView) v.findViewById(R.id.tvSpinner)).setText(getResources()
				.getString(R.string.bongda365_thuvienVideo));

		v.findViewById(R.id.img_cat).setVisibility(View.GONE);
		v.findViewById(R.id.llIcon).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

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
		super.onStop();
		Log.i("Flurry", "onStop");
		FlurryAgent.onEndSession(this);
		// your code

		Vietnalyze.onEndSession(this);
	}

	@Override
	protected void onDestroy() {
		isActivityDestroy = true;
		super.onDestroy();
	}

	private boolean isActivityDestroy = false;
	private String _day = "";

	/**
	 * AsyTask to get Categories List from server
	 * 
	 * @author My PC
	 * 
	 */
	class asyTaskGetItem extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... uri) {
			String responseString = doBackGround(uri[0]);
			if (isActivityDestroy)
				this.cancel(true);
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			initBongda365ArrayList(result);

			constant.Constants.log_d("TAG", "size: " + mListVideos.size());
			constant.Constants.log_d("TAG", "page1: " + page);

			bongda24hAdapter.notifyDataSetChanged();

			if (progressBarWaiting != null)
				progressBarWaiting.setVisibility(View.GONE);
			if (progressBarLoadMore != null)
				progressBarLoadMore.setVisibility(View.GONE);

			// /**
			// * Set on item click of Grid view
			// */
			// gridView.setOnItemClickListener(new OnItemClickListener() {
			// @Override
			// public void onItemClick(AdapterView<?> arg0, View v, int pos,
			// long id) {
			// Intent i = new Intent(BongDa365Activity.this,
			// BongDaDetailsActivity.class);
			// i.putExtra("articleID", bongda365GridItems.get(pos).getID());
			// startActivity(i);
			// }
			// });

			// bongda365GridViewAdapter = new
			// Bongda365GridViewAdapter(BongDa365Activity.this,
			// bongda365GridItems, R.layout.bongda365_item_grid);
			// gridView.setAdapter(bongda365GridViewAdapter);

			lvFragmetTitle.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view,
						int scrollState) {
				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {

					int lastInScreen = firstVisibleItem + visibleItemCount;

					if (totalItemCount != 0 && (lastInScreen == totalItemCount)
							&& !loadingMore
							&& totalItemCount != totalItemCountOld) {

						totalItemCountOld = totalItemCount;

						constant.Constants.log_d("TAG", "Load More = true");
						loadingMore = true;

						// Hide Load More progress bar
						progressBarLoadMore.setVisibility(View.VISIBLE);
						page += 1;
						constant.Constants.log_d("TAG", "page2: " + page);
						constant.Constants.log_d("TAG", "video_url: "
								+ video_url.replace("###", "" + page));

						new asyTaskGetItem().execute(video_url.replace("###",
								"" + page));
					}
				}
			});
			loadingMore = false;

		}

		/**
		 * This is method to make Array list of bongda365
		 * 
		 * @param result
		 */
		private void initBongda365ArrayList(String result) {
			makeContents(result);
		}

		private void makeContents(String result) {
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
					// constant.Constants.log_d("TAG", "newestImageSize: " +
					// newestImageSize);
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

					mListVideos.add(content);

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		/**
		 * This is background method to get data from api
		 * 
		 * @param url
		 * @return
		 */
		private String doBackGround(String url) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try {
				response = httpclient.execute(new HttpGet(url));
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

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
		Intent i = new Intent(ThuVienVideo.this, BongDaDetailsActivity.class);
		i.putExtra("articleID", mListVideos.get(pos).getID());
		startActivity(i);
	}

}
