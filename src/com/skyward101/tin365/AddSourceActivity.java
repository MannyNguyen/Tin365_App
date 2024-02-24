package com.skyward101.tin365;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import object.WebSource;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.Vietnalyze;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.flurry.android.FlurryAgent;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.skyward101.tin365.R;

public class AddSourceActivity extends SherlockActivity {

	private ArrayList<WebSource> AllWeb;
	private ProgressBar progressBar;
	private String mActionbar_color;
	private SharedPreferences sharedpreferences;
	private static final String SP_NAME = "MY_sharedpreferences";

	private ArrayList<Integer> CheckedPosition;
	private String add_source;

	@Override
	protected void onStop() {
		String s = "";
		constant.Constants.log_d("TAG", "CheckedPosition.size() " + CheckedPosition.size());
		for (int i = 0; i < CheckedPosition.size(); i++) {

			WebSource ws = AllWeb.get(CheckedPosition.get(i));

			s = s + ws.getId() + "@@@" + ws.getName() + "@@@" + ws.getUrlIcon()
					+ "###";
		}
		SharedPreferences.Editor edit = sharedpreferences.edit();
		edit.putString("add_source", s);
		edit.commit();

		constant.Constants.log_d("TAG", "s: " + s);
		
		Log.i("Flurry", "onStop");
		FlurryAgent.onEndSession(this);
		// your code

		Vietnalyze.onEndSession(this);
		super.onStop();
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

	ArrayList<Integer> SourceId;
	boolean[] Checked;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_source);
		initActionBar();

		lvAddSource = (ListView) findViewById(R.id.listView1);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		CheckedPosition = new ArrayList<Integer>();
		
		constant.Constants.isAddSource = true;

		
		String url= "http://news.ctyprosoft.com:8081/service.php?action=getWeb";
		if (!constant.Constants.isVietnamese) url = url + "&lang=en";
		new asyTaskGetAllWeb()
				.execute(url);
	}

	private void setCheckedSource() {

		SourceId = new ArrayList<Integer>();
		if (add_source.equals(""))
			return;
		String[] eachSource = add_source.split("###");
		for (int i = 0; i < eachSource.length; i++) {
			String[] es = eachSource[i].split("@@@");
			int id = Integer.parseInt(es[0]);
			constant.Constants.log_d("TAG", "id: " + id);
			SourceId.add(id);
		}

	}

	private void initActionBar() {
		// sharedpreferences
		sharedpreferences = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		mActionbar_color = sharedpreferences.getString("actionbar_color",
				constant.Constants.COLOR_ACTION_BAR);

		add_source = sharedpreferences.getString("add_source", "");
		constant.Constants.log_d("TAG", "add Source: " + add_source);

		setCheckedSource();

		// configure action bar
		// action bar
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setIcon(R.drawable.ic_launcher);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);

		// actionBar.setTitle(ModernContents.cat);
		actionBar.setTitle(Html.fromHtml("<font color=\"white\">"
				+ getResources().getString(R.string.themnguon) + "</font>"));
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor(mActionbar_color)));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * To get Web from server
	 */
	class asyTaskGetAllWeb extends AsyncTask<String, String, String> {

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
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			// constant.Constants.log_d("TAG", "result: " + result);

			AllWeb = new ArrayList<WebSource>();
			try {
				JSONArray jsonArray = new JSONArray(result);
				Checked = new boolean[jsonArray.length()];
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					int id = jsonObject.getInt("ID");
					String name = jsonObject.getString("name");
					String urlIcon = jsonObject.getString("urlIcon");

					WebSource web = new WebSource(id, name, urlIcon);
					// WebSource web = new WebSource(id, name,
					// "http://huthamcauvn.net/images/stories/tin-tuc/girl-xinh/girl-xinh3.jpg");

					AllWeb.add(web);
					if (checkElementInList(SourceId, id)) {
						Checked[i] = true;
						CheckedPosition.add(i);
					} else
						Checked[i] = false;

				}
			} catch (Exception e) {
				constant.Constants.log_d("TAG", "get All WEb error");
			}

			constant.Constants.log_d("TAG", " Size of AllWEb : " + AllWeb.size());

			setListView();

			super.onPostExecute(result);
		}

		/**
		 * Setting for list view
		 */
		private void setListView() {

			addSourceAdapter adapter = new addSourceAdapter(
					AddSourceActivity.this, R.layout.item_add_source, AllWeb);
			lvAddSource.setAdapter(adapter);
			progressBar.setVisibility(View.GONE);

			lvAddSource.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View v, int pos,
						long arg3) {
					CheckBox t = (CheckBox) v.findViewById(R.id.checkbok);
					if (t.isChecked()) {
						t.setChecked(false);
						Checked[pos] = false;
						try {
							CheckedPosition.remove(CheckedPosition.indexOf(pos));
						} catch (Exception e) {
						}

					} else {
						Vietnalyze.logEvent(getResources().getString(R.string.eventaddSpecialSources));
						FlurryAgent.logEvent(getResources().getString(R.string.eventaddSpecialSources));
						t.setChecked(true);
						Checked[pos] = true;
						if (!checkElementInList(CheckedPosition, pos))
							CheckedPosition.add(pos);
					}
				}
			});
		}
	}

	private ListView lvAddSource;

	class addSourceAdapter extends ArrayAdapter<WebSource> {
		private Context mContext;
		private int res;
		private ArrayList<WebSource> webs;

		private ImageLoader imageLoader;
		DisplayImageOptions doption = null;

		public addSourceAdapter(Context context, int resource,
				ArrayList<WebSource> webs) {
			super(context, resource);
			this.mContext = context;
			this.res = resource;
			this.webs = webs;

			// image
			imageLoader = ImageLoader.getInstance();
			// Initialize ImageLoader with configuration. Do it once.
			imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
			// Load and display image asynchronously
			doption = new DisplayImageOptions.Builder().cacheInMemory(true)
					.cacheOnDisc(true).build();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return this.webs.size();
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			constant.Constants.log_d("TAG", "getView: " + position);
			View vi = convertView;
			if (vi == null) {
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				vi = inflater.inflate(this.res, null);

			}

			ImageView imgIcon = (ImageView) vi.findViewById(R.id.imgIcon);
			TextView tvName = (TextView) vi.findViewById(R.id.tvSourceName);

			CheckBox cb = (CheckBox) vi.findViewById(R.id.checkbok);

			tvName.setText(webs.get(position).getName());

			imageLoader.displayImage(webs.get(position).getUrlIcon(), imgIcon,
					doption);

			cb.setEnabled(false);

			cb.setChecked(Checked[position]);

			// // TODO xu ly nhung noi dung da check
			// if (webs.get(position).getId() == 3){
			// cb.setChecked(true);
			// }
			//
			// if (cb.isChecked()) {
			// if (!checkElementInList(CheckedPosition, position))
			// CheckedPosition.add(position);
			// }

			// cb.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// if (cb.isChecked()) {
			// cb.setChecked(false);
			// try {
			// CheckedPosition.remove(CheckedPosition
			// .indexOf(position));
			// } catch (Exception e) {
			//
			// }
			// } else {
			// cb.setChecked(true);
			// if (!checkElementInList(CheckedPosition, position))
			// CheckedPosition.add(position);
			// }
			// }
			// });

			return vi;
		}

	}

	private boolean checkElementInList(ArrayList<Integer> list, int value) {
		if (list == null || list.size() == 0)
			return false;

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) == value)
				return true;
		}
		return false;
	}
}
