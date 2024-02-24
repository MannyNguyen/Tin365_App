package fragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

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

import utility.Utility;

import adapter.TitleListView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.skyward101.tin365.R;
import com.skyward101.tin365.SplashActivity;
import com.skyward101.tin365.TraditionDetailsActivity;
import com.skyward101.tin365.TraditionNews;

@SuppressLint("ValidFragment")
public class TraditionFragmentTitle extends SherlockFragment {

	private int pos;
	private View v;
	private ListView lvTitle;

	private ArrayList<News_Content> contents;
	private ArrayList<News_Content> mNews;
	private ProgressBar progressBar;

	private boolean isActivityDestroy = false;

	private String url;
	String _day = "";

	private final int NUM_PER_PAGE = 15;

	public TraditionFragmentTitle(int pos) {
		super();

		this.pos = pos;
		contents = new ArrayList<News_Content>();
		mNews = new ArrayList<News_Content>();
		if (TraditionNews.isMainCategory)
			url = "http://news.ctyprosoft.com:8081/service.php?action=getArticleMainCategory&categoryID="
					+ SplashActivity.cats.get(pos).getmId()
					+ "&numPerPage="
					+ NUM_PER_PAGE + "&pageNumber=";
		else
			url = "http://news.ctyprosoft.com:8081/service.php?action=getArticleCategory&categoryID="
					+ SplashActivity.cats.get(pos).getmId()
					+ "&numPerPage="
					+ NUM_PER_PAGE + "&pageNumber=";

		constant.Constants.log_d("TAG", "URL : " + url);
	}

	@Override
	public void onDestroy() {
		isActivityDestroy = true;
		super.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		v = inflater.inflate(R.layout.fragment_title, container, false);
		progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
		lvTitle = (ListView) v.findViewById(R.id.lvFragmetTitle);

		lvTitle.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);

		try {
			if (!constant.Constants.isVietnamese)
				new RequestTask().execute(url + "1&lang=en");
			else
				new RequestTask().execute(url + "1");
		} catch (Exception e) {
			// TODO: handle exception
		}

		return v;

	}

	int page = 1;
	private boolean mLoading = false;
	TitleListView titleListView;

	// private News_Content contentsAtZero;

	class RequestTask extends AsyncTask<String, String, String> implements
			OnScrollListener {

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
			makeContents(responseString);
			if (isActivityDestroy)
				this.cancel(true);

			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {

			if (contents.size() > 0) {

				// /////////////////////////////////////////
				// add Header

				LayoutInflater inflater = (LayoutInflater) getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = inflater.inflate(R.layout.title_item_header, null);
				TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
				TextView tvDate = (TextView) v.findViewById(R.id.tvDate);
				ImageView icon = (ImageView) v.findViewById(R.id.imgIcon);

				// TODO error here
				tvTitle.setText(contents.get(0).getTitle());

				// init day
				_day = Utility.getTime(contents.get(0).getDatePost(),
						getActivity());
				// init day
				tvDate.setText(_day);
				// tvDate.setText("test");

				// Set image loader
				// image
				ImageLoader imageLoader = ImageLoader.getInstance();
				// Initialize ImageLoader with configuration. Do it once.
				imageLoader.init(ImageLoaderConfiguration
						.createDefault(getActivity()));
				// Load and display image asynchronously
				DisplayImageOptions doption = new DisplayImageOptions.Builder()
						.showImageForEmptyUri(R.drawable.ic)
						.showImageOnFail(R.drawable.ic).cacheInMemory(true)
						.cacheOnDisc(true).build();
				String newestImage = contents.get(0).getNewestImage();
				if (!newestImage.equals("null"))
					imageLoader.displayImage(newestImage, icon, doption);
				else {
					imageLoader.displayImage(contents.get(0).getFeatureImage(),
							icon, doption);
					constant.Constants.log_d("TAG", "Liink : "
							+ contents.get(0).getFeatureImage());
				}
				icon.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(getActivity(),
								TraditionDetailsActivity.class);
						i.putExtra("news", mNews);
						i.putExtra("pos", 0);
						startActivity(i);

					}
				});
				lvTitle.addHeaderView(v);

				// End add Header
				// //////////////////////////////////////////////

				// for (int i = 0; i < contents.size(); i++)
				// mNews.add(contents.get(i));

				// contentsAtZero = contents.get(0);
				contents.remove(0);

				titleListView = new TitleListView(getActivity(),
						R.layout.title_item, contents);
				lvTitle.setAdapter(titleListView);
				lvTitle.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						Intent i = new Intent(getActivity(),
								TraditionDetailsActivity.class);
						// i.putExtra("cat", TraditionNews.cat);
						i.putExtra("news", mNews);
						i.putExtra("pos", position);

						startActivity(i);
					}

				});
				lvTitle.setOnScrollListener(this);

			}// ### end of if(contents.size()>0)
			progressBar.setVisibility(View.GONE);
			lvTitle.setVisibility(View.VISIBLE);
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			if (view.getLastVisiblePosition() >= (totalItemCount - 1)
					&& totalItemCount != totalItemCountOld) {
				totalItemCountOld = totalItemCount;
				checkLoadMore();
			}

		}

		@Override
		public void onScrollStateChanged(AbsListView arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		private void checkLoadMore() {
			if (!mLoading && contents.size() > (NUM_PER_PAGE - 2)) {
				TitleListView.imageLoader.clearMemoryCache();
				constant.Constants.log_d("TAG",
						"checkLoadMore - clearMemoryCache");

				progressBar.setVisibility(View.VISIBLE);
				mLoading = true;
				page += 1;
				if (!constant.Constants.isVietnamese)
					new LoadMore().execute(url + page+ "&lang=en");
				else
					new LoadMore().execute(url + page);
					
				constant.Constants.log_d("TAG","URL _Loadmore: "+ url + page);
			}
		}

	}

	int totalItemCountOld = 0;

	class LoadMore extends AsyncTask<String, String, String> {

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
			makeContents(responseString);
			if (isActivityDestroy)
				this.cancel(true);
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			// if (isFirst) {
			// isFirst = false;
			// contents.add(0, contentsAtZero);
			// }

			titleListView.notifyDataSetChanged();
			// flipAdapter.notifyDataSetChanged();
			progressBar.setVisibility(View.GONE);
			mLoading = false;
			lvTitle.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					Intent it = new Intent(getActivity(),
							TraditionDetailsActivity.class);

					it.putExtra("news", mNews);
					it.putExtra("pos", position);
					it.putExtra("page", page);
					constant.Constants.log_d("TAG",
							"TraditionFragmentTitle page: " + page);

					startActivity(it);
				}
			});

			super.onPostExecute(result);
		}

	}

	boolean isOk = false; // return true if the first item has newestImage

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

				Log.d("TAG", "title" + title);
				Log.d("TAG", "url" + url);
				Log.d("TAG", "featureImage" + featureImage);
				Log.d("TAG", "excerpt" + excerpt);

				News_Content content = new News_Content(id, categoryID,
						mainCatID, newestImageHeight, newestImageWidth, title,
						url, datePost, excerpt, featureImage, newestImage,
						websiteName, websiteURL, websiteIcon, websiteLogo,
						numberComment);

				// contents.add(content);
				// mNews.add(content);

				if (!isOk
						&& (!newestImage.equals("null") || !featureImage
								.equals("null"))) {
					isOk = true;
					contents.add(content);
					mNews.add(content);
				} else {
					if (isOk) {
						contents.add(content);
						mNews.add(content);
					}
				}
				Log.d("TAG", "contents size:" + contents.size());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
