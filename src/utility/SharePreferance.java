package utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Gabriele Porcelli
 * 
 *         Example. 
 *         FirstTimePreference prefFirstTime = new FirstTimePreference(getApplicationContext()); 
 *         if (prefFirstTime.runTheFirstNTimes("myKey" , 3)) {
 *         Toast.makeText(this,"Test myKey & coutdown: "+ prefFirstTime.getCountDown("myKey"),Toast.LENGTH_LONG).show(); }
 */

public class SharePreferance {

	public static final String FIRST_TIME_PREFERENCES_KEY = "FirstKeyPreferences";
	
	public static final String login = "login";
	public static final String firstVideo = "firstVideo";
	public static final String langu = "language";
	private static final String user_name = "user";
	private static final String email = "email";
	private static final String fbID = "fbID";
	private static final String accessToken = "accessToken";
	private static final String clickLoginFB = "click";
	private static final String cardNo = "cardNo";
	private static final String imgAvarta = "imgAvarta";
	private static final String phone = "phone";
	private static final String gender = "gender";
	private static final String birth = "birth";
	private static final String first_brand = "first_brand";
	private static final String first_city = "first_city";
	private static final String first_coupon_guide = "first_brand";
	private static final String first_game_guide = "first_city";
	private static final String first_invite_guide = "first_brand";
	private static final String crateIconHome = "crateIconHome";
	
	private static final String url_FB_photo = "crateIconHome";
	
	private final SharedPreferences firstTimePreferences;
	

	public SharePreferance(Context context) {
		firstTimePreferences = context.getSharedPreferences(
				FIRST_TIME_PREFERENCES_KEY, Context.MODE_PRIVATE);
	}

	public void setStatusLogged(Boolean status)
	{
		SharedPreferences.Editor editor = firstTimePreferences.edit();
		editor.putBoolean(login, status);
		editor.commit();
	}
	public boolean getStatusLogged()
	{
		return firstTimePreferences.getBoolean(login, false);
	}
	
	public void setStatusCreateIconHome(Boolean status)
	{
		SharedPreferences.Editor editor = firstTimePreferences.edit();
		editor.putBoolean(crateIconHome, status);
		editor.commit();
	}
	public boolean getStatusCreateIconHome()
	{
		return firstTimePreferences.getBoolean(crateIconHome, false);
	}
	
	
	public void setStatusfirstBrand(Boolean status)
	{
		SharedPreferences.Editor editor = firstTimePreferences.edit();
		editor.putBoolean(first_brand, status);
		editor.commit();
	}
	public boolean getStatusfirstBrand()
	{
		return firstTimePreferences.getBoolean(first_brand, false);
	}
	
	public void setStatusfirstVideo(Boolean status)
	{
		SharedPreferences.Editor editor = firstTimePreferences.edit();
		editor.putBoolean(firstVideo, status);
		editor.commit();
	}
	public boolean getStatusfirstVideo()
	{
		return firstTimePreferences.getBoolean(firstVideo, false);
	}
	
	public void setStatusfirstCity(Boolean status)
	{
		SharedPreferences.Editor editor = firstTimePreferences.edit();
		editor.putBoolean(first_city, status);
		editor.commit();
	}
	public boolean getStatusfirstBrandCity()
	{
		return firstTimePreferences.getBoolean(first_city, false);
	}
	
	public void setStatusLanguage(String value)
	{
		SharedPreferences.Editor editor = firstTimePreferences.edit();
		editor.putString(langu, value);
		editor.commit();
	}
	public boolean getclickLoginFB()
	{
		return firstTimePreferences.getBoolean(clickLoginFB, false);
	}
	
	public void setclickLoginFB(Boolean click)
	{
		SharedPreferences.Editor editor = firstTimePreferences.edit();
		editor.putBoolean(clickLoginFB, click);
		editor.commit();
	}
	public String getValueLanguage()
	{
		return firstTimePreferences.getString(langu, "");
	}
	public void setUserName(String value)
	{
		SharedPreferences.Editor editor = firstTimePreferences.edit();
		editor.putString(user_name, value);
		editor.commit();
	}
	public String getEmail() {
		return firstTimePreferences.getString(email, "");
	}
	public void setEmail(String value)
	{
		SharedPreferences.Editor editor = firstTimePreferences.edit();
		editor.putString(email, value);
		editor.commit();
	}
	public String getUserName() {
		return firstTimePreferences.getString(user_name, "");
	}
	
	public void setCardNo(String value)
	{
		SharedPreferences.Editor editor = firstTimePreferences.edit();
		editor.putString(cardNo, value);
		editor.commit();
	}
	public String getCardNo() {
		return firstTimePreferences.getString(cardNo, "");
	}
	
	public void setimgAvarta(String value)
	{
		SharedPreferences.Editor editor = firstTimePreferences.edit();
		editor.putString(imgAvarta, value);
		editor.commit();
	}
	public String getimgAvarta() {
		return firstTimePreferences.getString(imgAvarta, "");
	}
	
	public void setphone(String value)
	{
		SharedPreferences.Editor editor = firstTimePreferences.edit();
		editor.putString(phone, value);
		editor.commit();
	}
	public String getphone() {
		return firstTimePreferences.getString(phone, "");
	}
	
	public void setgender(String value)
	{
		SharedPreferences.Editor editor = firstTimePreferences.edit();
		editor.putString(gender, value);
		editor.commit();
	}
	public String getgender() {
		return firstTimePreferences.getString(gender, "");
	}
	public void setfbID(String value)
	{
		SharedPreferences.Editor editor = firstTimePreferences.edit();
		editor.putString(fbID, value);
		editor.commit();
	}
	public String getAccessToken() {
		return firstTimePreferences.getString(accessToken, "");
	}
	public void setAccessToken(String value)
	{
		SharedPreferences.Editor editor = firstTimePreferences.edit();
		editor.putString(accessToken, value);
		editor.commit();
	}
	public String getfbID() {
		return firstTimePreferences.getString(fbID, "");
	}
	public void setbirth(String value)
	{
		SharedPreferences.Editor editor = firstTimePreferences.edit();
		editor.putString(birth, value);
		editor.commit();
	}
	public String getbirth() {
		return firstTimePreferences.getString(birth, "");
	}

	public boolean getFirstCouponGuide()
	{
		return firstTimePreferences.getBoolean(first_coupon_guide, true);
	}
	
	public void setFirstCouponGuide(Boolean click)
	{
		SharedPreferences.Editor editor = firstTimePreferences.edit();
		editor.putBoolean(first_coupon_guide, click);
		editor.commit();
	}
	
	public boolean getFirstGameGuide()
	{
		return firstTimePreferences.getBoolean(first_game_guide, true);
	}
	
	public void setFirstGameGuide(Boolean click)
	{
		SharedPreferences.Editor editor = firstTimePreferences.edit();
		editor.putBoolean(first_game_guide, click);
		editor.commit();
	}
	
	public boolean getFirstInviteGuide()
	{
		return firstTimePreferences.getBoolean(first_invite_guide, true);
	}
	
	public void setFirstInviteGuide(Boolean click)
	{
		SharedPreferences.Editor editor = firstTimePreferences.edit();
		editor.putBoolean(first_invite_guide, click);
		editor.commit();
	}
	
	
	public void setFbPhoto(String value)
	{
		SharedPreferences.Editor editor = firstTimePreferences.edit();
		editor.putString(url_FB_photo, value);
		editor.commit();
	}
	public String getFbPhoto() {
		return firstTimePreferences.getString(url_FB_photo, "");
	}

	



}
