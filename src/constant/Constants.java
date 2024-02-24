package constant;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

public class Constants {
	// ///////////////////////////////////////
	// Default values
	// //////////////////////////////////////
	public static final boolean MODERN_LAYOUT = true;

	public static final String COLOR_ACTION_BAR = "#CC0000";
	public static final String COLOR_BACKGROUND = "#F2F2F2";
	public static final String COLOR_DETAILS_BACKGROUND = "#FFFFFF";
	public static final String COLOR_DETAILS_FONT = "#000000";
	public static final int FONT_SIZE = 16;

	// ///////////////////////////////////////
	// Static values
	// ///////////////////////////////////////
	public static int DEVICE_WIDTH;
	public static int DEVICE_HEIGHT;

	public static boolean isVietnamese = true;
	
	public static boolean isAddSource = false;

	public static String[] ext = { ".jpg", ".jpeg", ".gif", ".png", ".GIF",
			".PNG", ".JPG", ".JPEG" };

	public static String editImageLink(String img_url1) {
		int pos;
		String img_url = img_url1.replace(" ", "%20");
		// constant.Constants.log_d("TAG", "img_url: " + img_url);
		String new_url;
		for (int i = 0; i < ext.length; i++) {
			pos = img_url.lastIndexOf(ext[i]);
			if (pos > 0) {
				new_url = img_url.substring(0, pos) + ext[i];
				return new_url;
			}
		}

		return img_url;

	}

	public static boolean debug = true;

	public static void log_d(String tag, String mess) {
		if (debug)
			Log.d(tag, mess);
	}

	public static boolean isPhone(Context context) {
		try {
			TelephonyManager manager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			if (manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			return true;
		}
	}

	/**
	 * To check Internet status
	 * 
	 * @return true if available or false
	 */
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

//	public static String[] categories = { "Hot News", "Politics � Society",
//			"Life", "World", "Economics", "Culture � Entertainment", "Sport",
//			"Law", "Traveling", "Science � Technology", "Vehicles",
//			"Community", "Sharing � Advice", "Relaxation", "Health",
//			"Environment", "Documentary � Feature", "Education", "Daily tips",
//			"Youth", "Culture � Art", "Union" };
//
//	public static String[] hotdeals_cat = { "Entertainment",
//			"Restaurant", "Household", "Travel & Leisure", "Shopping",
//			"Mom & Baby", "Technology", "Spa & Beauty", "Fashion" };
//
//	public static String[] cities = { "Dallas", "New York", "San Diego",
//			"Other cities"
//
//	};

}
