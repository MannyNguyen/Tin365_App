package com.skyward101.tin365;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import object.HotDealsCategory;
import object.HotDealsCity;
import object.HotDealsProduct;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import adapter.HotDealsGridViewAdapter;
import android.Vietnalyze;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.flurry.android.FlurryAgent;
import com.skyward101.tin365.R;
import com.slidingmenu.lib.SlidingMenu;

public class HotDealsActivity extends SherlockFragmentActivity implements
		OnClickListener {

	public String url_categories = "http://news.ctyprosoft.com:8083/API/GetCity/GetCityList.aspx?city=";

	private ActionBar actionBar;
	private SlidingMenu menu;
	private GridView gridView;
	private HotDealsGridViewAdapter hotDealsGridViewAdapter;

	private SharedPreferences sharedpreferences;
	private static final String SP_NAME = "MY_sharedpreferences";
	public static String mActionbar_color;

	private boolean isActivityDestroy = false;

	private ArrayList<HotDealsCity> cities;
	private ArrayList<HotDealsCategory> categories;
	private ArrayList<HotDealsProduct> products;
	private boolean isFirst = false;
	private int id_of_city;

	private Dialog dialog;

	private ProgressBar progressBarHotDeals;

	private ProgressBar progressBarHotDealsLoadMore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hot_deals);
		progressBarHotDealsLoadMore = (ProgressBar) findViewById(R.id.progressBarHotDealsLoadMore);
		progressBarHotDeals = (ProgressBar) findViewById(R.id.progressBarHotDeals);
		gridView = (GridView) findViewById(R.id.gridViewProducts);

		init();

		initActionBar();

	}

	@Override
	protected void onDestroy() {
		isActivityDestroy = true;
		super.onDestroy();
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
	public void onStop() {
		super.onStop();
		Log.i("Flurry", "onStop");
		FlurryAgent.onEndSession(this);
		// your code

		Vietnalyze.onEndSession(this);
	}

	private void init() {

		// sharedpreferences
		sharedpreferences = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		mActionbar_color = sharedpreferences.getString("actionbar_color",
				constant.Constants.COLOR_ACTION_BAR);
		isFirst = sharedpreferences.getBoolean("hot_deals_is_first", true);

		cities = new ArrayList<HotDealsCity>();
		categories = new ArrayList<HotDealsCategory>();
		products = new ArrayList<HotDealsProduct>();

		hotDealsGridViewAdapter = new HotDealsGridViewAdapter(
				HotDealsActivity.this, R.layout.hot_deals_grid_view_item,
				products);
		gridView.setAdapter(hotDealsGridViewAdapter);

		new asyTaskGetCityList()
				.execute("http://news.ctyprosoft.com:8083/API/GetCity/GetCityList.aspx");
	}

	/**
	 * Setting for first time focus
	 */
	private void loadFirstTime() {

		progressBarHotDeals.setVisibility(View.GONE);
		dialog = new Dialog(this);
		dialog.setContentView(R.layout.dialog_choose_city);
		dialog.setTitle(getResources().getString(R.string.chooseYourCity));
		dialog.setCanceledOnTouchOutside(false);

		ListView lvChooseCity = (ListView) dialog
				.findViewById(R.id.lvChooseCity);

		String[] array = new String[cities.size()];
		for (int i = 0; i < cities.size(); i++)
			array[i] = cities.get(i).getName();

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
				HotDealsActivity.this,
				R.layout.hot_deals_sliding_menu_listview_item, array);

		lvChooseCity.setAdapter(arrayAdapter);
		lvChooseCity.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int pos,
					long mId) {
				v.setSelected(true);
				progressBarHotDeals.setVisibility(View.VISIBLE);
				dialog.dismiss();
				if (menu != null && menu.isMenuShowing())
					menu.toggle();

				SharedPreferences.Editor edit = sharedpreferences.edit();
				edit.putInt("id_of_city", cities.get(pos).getId());
				edit.putBoolean("hot_deals_is_first", false);
				edit.commit();

				// TODO
				// show contents
				id_of_city = cities.get(pos).getId();

				categories = new ArrayList<HotDealsCategory>();
				products = new ArrayList<HotDealsProduct>();

				hotDealsGridViewAdapter = new HotDealsGridViewAdapter(
						HotDealsActivity.this,
						R.layout.hot_deals_grid_view_item, products);
				gridView.setAdapter(hotDealsGridViewAdapter);

				new asyTaskGetCategories(0).execute(url_categories
						+ cities.get(pos).getId());
			}
		});

		dialog.show();
	}

	private void initSlidingMenu() {
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.RIGHT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menu.setShadowWidthRes(R.dimen.sliding_shadow_width);
		menu.setShadowDrawable(R.drawable.sliding_menu_shadow_right);
		menu.setBehindOffsetRes(R.dimen.hot_deals_sliding_behind_of_set);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu.setMenu(R.layout.sliding_menu_hot_deals);
		menu.setSlidingEnabled(true);

		ListView lvChonDanhMuc = (ListView) findViewById(R.id.lvChonDanhMuc);

		String[] array = new String[categories.size()];
		for (int i = 0; i < categories.size(); i++)
			array[i] = categories.get(i).getName();

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
				HotDealsActivity.this,
				R.layout.hot_deals_sliding_menu_listview_item, array);

		lvChonDanhMuc.setAdapter(arrayAdapter);
		lvChonDanhMuc.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos,
					long mId) {
				page = 1;
				progressBarHotDeals.setVisibility(View.VISIBLE);
				menu.toggle();
				products = new ArrayList<HotDealsProduct>();

				hotDealsGridViewAdapter = new HotDealsGridViewAdapter(
						HotDealsActivity.this,
						R.layout.hot_deals_grid_view_item, products);
				gridView.setAdapter(hotDealsGridViewAdapter);

				id = categories.get(pos).getIdcategories();
				new asyTaskGetProducts()
						.execute("http://news.ctyprosoft.com:8083/API/GetProduct/GetProduct.aspx?city="
								+ id_of_city
								+ "&categories="
								+ categories.get(pos).getIdcategories()
								+ "&page=1");
			}
		});
	}

	private void initActionBar() {
		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);

		LayoutInflater inflator = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View v = inflator.inflate(R.layout.actionbar_hotdeals, null);

		com.actionbarsherlock.app.ActionBar.LayoutParams params = new com.actionbarsherlock.app.ActionBar.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		actionBar.setCustomView(v, params);
		actionBar.setDisplayShowCustomEnabled(true);

		LinearLayout mActionBar_LinearLayout = (LinearLayout) v
				.findViewById(R.id.llActionBar);
		mActionBar_LinearLayout.setBackgroundColor(Color
				.parseColor(mActionbar_color));

		v.findViewById(R.id.img_location).setOnClickListener(this);
		v.findViewById(R.id.img_cat).setOnClickListener(this);
		v.findViewById(R.id.llIcon).setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.img_cat:
			menu.toggle();

			break;
		case R.id.llIcon:
			finish();
			break;
		case R.id.img_location:

			if (dialog != null)
				dialog.show();
			else
				loadFirstTime();
			break;
		default:
			break;
		}

	}

	/**
	 * AsyTask to get City List from server
	 * 
	 * @author My PC
	 * 
	 */
	class asyTaskGetCityList extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... uri) {
			String responseString = doBackGround(uri[0]);
			if (isActivityDestroy)
				this.cancel(true);
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				JSONArray jsonArray = new JSONArray(result);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					int id = jsonObject.getInt("id");
					String name = jsonObject.getString("name");

					HotDealsCity hotDealsCity = new HotDealsCity(id, name);
					//HotDealsCity hotDealsCity = new HotDealsCity(id, constant.Constants.cities[i]);
					cities.add(hotDealsCity);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			id_of_city = sharedpreferences.getInt("id_of_city", cities.get(0)
					.getId());

			// TODO
			if (isFirst)
				loadFirstTime();
			else {
				new asyTaskGetCategories(0)
						.execute(url_categories + id_of_city);
			}
		}
	}

	private int id = 0;

	/**
	 * AsyTask to get Categories List from server
	 * 
	 * @author My PC
	 * 
	 */
	class asyTaskGetCategories extends AsyncTask<String, String, String> {

		public asyTaskGetCategories(int mId) {
			id = mId;
		}

		@Override
		protected String doInBackground(String... uri) {
			String responseString = doBackGround(uri[0]);
			if (isActivityDestroy)
				this.cancel(true);
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				JSONArray jsonArray = new JSONArray(result);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					int idcity = jsonObject.getInt("idcity");
					String namecity = jsonObject.getString("namecity");
					int idcategories = jsonObject.getInt("idcategories");
					String name = jsonObject.getString("name");

					HotDealsCategory hotDealsCategory = new HotDealsCategory(
							idcity, namecity, idcategories, name);
//					HotDealsCategory hotDealsCategory = new HotDealsCategory(
//							idcity, namecity, idcategories, constant.Constants.hotdeals_cat[i]);
					categories.add(hotDealsCategory);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			initSlidingMenu();

			if (categories.size() > 0) {
				if (id == 0)
					id = categories.get(0).getIdcategories();
				new asyTaskGetProducts()
						.execute("http://news.ctyprosoft.com:8083/API/GetProduct/GetProduct.aspx?city="
								+ id_of_city + "&categories=" + id + "&page=1");
			}

		}
	}

	/**
	 * AsyTask to get Categories List from server
	 * 
	 * @author My PC
	 * 
	 */
	class asyTaskGetProducts extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... uri) {
			String responseString = doBackGround(uri[0]);
			if (isActivityDestroy)
				this.cancel(true);
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				JSONObject json_result = new JSONObject(result);
				JSONArray jsonArray = json_result.getJSONArray("data");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					int idproduct = jsonObject.getInt("idproduct");
					int idcity = jsonObject.getInt("idcity");
					String namecity = jsonObject.getString("namecity");
					int idcategories = jsonObject.getInt("idcategories");
					String namecategories = jsonObject
							.getString("namecategories");
					String title = jsonObject.getString("title");
					String imageurl = jsonObject.getString("imageurl");
					String price = jsonObject.getString("price");
					String pricecompare = jsonObject.getString("pricecompare");
					String linkdetail = jsonObject.getString("linkdetail");
					String createdate = jsonObject.getString("createdate");

					HotDealsProduct dealsProduct = new HotDealsProduct(
							idproduct, idcity, namecity, idcategories,
							namecategories, title, imageurl, price,
							pricecompare, linkdetail, createdate);
					products.add(dealsProduct);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// TODO
			// gridView.setAdapter(new HotDealsGridViewAdapter(
			// HotDealsActivity.this, R.layout.hot_deals_grid_view_item,
			// products));

			hotDealsGridViewAdapter.notifyDataSetChanged();
			if (progressBarHotDeals != null)
				progressBarHotDeals.setVisibility(View.GONE);
			if (progressBarHotDealsLoadMore != null)
				progressBarHotDealsLoadMore.setVisibility(View.GONE);

			gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View v, int pos,
						long arg3) {
					Intent i = new Intent(HotDealsActivity.this,
							HotDealsDetailsActivity.class);
					i.putExtra("url", products.get(pos).getLinkdetail());
					startActivity(i);

				}
			});

			gridView.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view,
						int scrollState) {

					// 0 la luc scroll dung
					mScrollState = scrollState;
				}

				int mScrollState;

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					int lastInScreen = firstVisibleItem + visibleItemCount;

					// constant.Constants.log_d("TAG", "totalItemCount: " +
					// totalItemCount);
					// constant.Constants.log_d("TAG", "firstVisibleItem: " +
					// firstVisibleItem);
					// constant.Constants.log_d("TAG", "visibleItemCount: " +
					// visibleItemCount);
					// constant.Constants.log_d("TAG", "mScrollState: " +
					// mScrollState);

					if (totalItemCount != 0 && (lastInScreen == totalItemCount)
							&& !loadingMore
							&& totalItemCount != totalItemCountOld) {
						totalItemCountOld = totalItemCount;
						constant.Constants.log_d("TAG", "Load More = true");
						HotDealsGridViewAdapter.imageLoader.clearMemoryCache();
						loadingMore = true;
						progressBarHotDealsLoadMore.setVisibility(View.VISIBLE);
						page += 1;
						new asyTaskGetProducts()
								.execute("http://news.ctyprosoft.com:8083/API/GetProduct/GetProduct.aspx?city="
										+ id_of_city
										+ "&categories="
										+ id
										+ "&page=" + page);
					}
				}
			});

			constant.Constants.log_d("TAG", "Load More = false");
			loadingMore = false;
		}
	}

	int totalItemCountOld = 0;
	private boolean loadingMore = false;
	int page = 1;

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

	@Override
	public void onBackPressed() {

		if (menu != null && menu.isMenuShowing())
			menu.toggle();
		else
			super.onBackPressed();
	}
}
