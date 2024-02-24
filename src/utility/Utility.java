package utility;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Window;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.skyward101.tin365.R;

import object.News_Detail;

@SuppressLint("SimpleDateFormat")
public class Utility {
	public static String url_photo_FB = "";
	public static String _str_table_Bookmark = DataBaseHelper.TABLE_Bookmark;
	public static String user_name = "";
	public static String fbID = "";

	public static String getHourAgo(String time) {
		String hour = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date past = null;
		try {
			past = format.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date now = new Date();

		// System.out.println(TimeUnit.MILLISECONDS.toMillis(now.getTime() -
		// past.getTime()) + " milliseconds ago");
		// System.out.println(TimeUnit.MILLISECONDS.toMinutes(now.getTime() -
		// past.getTime()) + " minutes ago");
		System.out.println(TimeUnit.MILLISECONDS.toHours(now.getTime()
				- past.getTime())
				+ " hours ago");
		// System.out.println(TimeUnit.MILLISECONDS.toDays(now.getTime() -
		// past.getTime()) + " days ago");
		hour = String.valueOf(TimeUnit.MILLISECONDS.toHours(now.getTime()
				- past.getTime()));
		return hour;
	}

	public static String getDayAgo(String time) {
		String day = "";

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date past = null;
		try {
			past = format.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date now = new Date();

		// System.out.println(TimeUnit.MILLISECONDS.toMillis(now.getTime() -
		// past.getTime()) + " milliseconds ago");
		// System.out.println(TimeUnit.MILLISECONDS.toMinutes(now.getTime() -
		// past.getTime()) + " minutes ago");
		// System.out.println(TimeUnit.MILLISECONDS.toHours(now.getTime() -
		// past.getTime()) + " hours ago");
		System.out.println(TimeUnit.MILLISECONDS.toDays(now.getTime()
				- past.getTime())
				+ " days ago");
		day = String.valueOf(TimeUnit.MILLISECONDS.toDays(now.getTime()
				- past.getTime()));
		return day;
	}

	public static String getMinuteAgo(String time) {
		String minute = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date past = null;
		try {
			past = format.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date now = new Date();

		// System.out.println(TimeUnit.MILLISECONDS.toMillis(now.getTime() -
		// past.getTime()) + " milliseconds ago");
		System.out.println(TimeUnit.MILLISECONDS.toMinutes(now.getTime()
				- past.getTime())
				+ " minutes ago");
		// System.out.println(TimeUnit.MILLISECONDS.toHours(now.getTime() -
		// past.getTime()) + " hours ago");
		// System.out.println(TimeUnit.MILLISECONDS.toDays(now.getTime() -
		// past.getTime()) + " days ago");
		minute = String.valueOf(TimeUnit.MILLISECONDS.toMinutes(now.getTime()
				- past.getTime()));
		return minute;
	}

	public static void showDialog(Context context, String sms) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage(sms);

		builder.setPositiveButton("OK", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});
		Dialog dialog = builder.create();
		// TextView messageText =
		// (TextView)dialog.findViewById(android.R.id.message);
		// messageText.setGravity(Gravity.CENTER);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.show();
	}

	public static String getTime(String time,Context context){
		String _day="";
		int day = Integer.parseInt(getDayAgo(time));
		try {
			if (getDayAgo(time).equals("0")) {
				if (getHourAgo(time).equals("0")) {
					String d1 = getMinuteAgo(time);
					String ago1 = context.getResources().getString(
							R.string.minuteago);
					if (!d1.equals("1"))
						ago1 = ago1.replace("minute", "minutes");
					_day = d1 + ago1;
				} else {
					String d2 = getHourAgo(time);
					String ago2 = context.getResources()
							.getString(R.string.hourago);
					if (!d2.equals("1"))
						ago2 = ago2.replace("hour", "hours");
					_day = d2 + ago2;
					constant.Constants.log_d("TAG", "d2: " + d2);
				}
			} else if (day < 31) {
				String d3 = getDayAgo(time);
				String ago3 = context.getResources().getString(R.string.dayago);
				if (!d3.equals("1"))
					ago3 = ago3.replace("day", "days");
				_day = d3 + ago3;

			} else
				_day = time;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return _day;
	}
	
	public static boolean isInternet(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mMobile = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if ((mWifi != null && mWifi.isConnected())
				|| (mMobile != null && mMobile.isConnected()))
			return true;
		return false;

	}
}
