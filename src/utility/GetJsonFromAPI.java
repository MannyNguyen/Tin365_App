package utility;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import object.*;
import utility.RestClient.RequestMethod;



public class GetJsonFromAPI {
	public static JSONArray json_array;
	public static JSONObject json;
	static String[] values;
	static String[] values_china;
	public static HashMap<String, String> hashmap, hashmap_digital;

	public static String postRequest(String Url) {
		System.out.println("url =" + Url);
		RestClient client = new RestClient(Url);
		String response = new String();
		try {
			client.Execute(RequestMethod.POST);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response = client.getResponse();
		System.out.println("response =" + response);
		try {
			json_array = new JSONArray();
			json_array = new JSONArray(response);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
		}

		return response;
	}

	public static String callregister(String Url) {
		System.out.println("url =" + Url);
		RestClient client = new RestClient(Url);
		String response = new String();
		try {
			client.Execute(RequestMethod.POST);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String status = "";
		response = client.getResponse();
		try {
			JSONObject json = new JSONObject(response);
			status = json.getString("status");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("response =" + response);

		return status;
	}
	public static String getCountCmt(String Url) {
		System.out.println("url =" + Url);
		RestClient client = new RestClient(Url);
		String response = new String();
		try {
			client.Execute(RequestMethod.POST);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String status = "";
		response = client.getResponse();
		try {
			JSONObject json = new JSONObject(response);
			status = json.getString("numberComment");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("response =" + response);

		return status;
	}
	public static ArrayList<Comment> getCmtList(String Url) {
		ArrayList<Comment> array = new ArrayList<Comment>();

		System.out.println("url_list =" + Url);
		RestClient client = new RestClient(Url);
		String response = new String();
		try {
			client.Execute(RequestMethod.POST);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String status = "";
		response = client.getResponse();

		try {
			JSONArray jsonArray = new JSONArray(response);
			for (int i = 0; i < jsonArray.length(); i++) {
				Comment cmt = new Comment();
				cmt.setAvartaFB(jsonArray.getJSONObject(i).getString("avatar"));
				cmt.setTitle(jsonArray.getJSONObject(i).getString("faceName"));
				cmt.setContent(jsonArray.getJSONObject(i)
						.getString("content"));
				cmt.setCreatDate(jsonArray.getJSONObject(i).getString("commentTime"));
				array.add(cmt);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("response =" + response);

		return array;
	}
	public static ArrayList<News_Content> getfootballList(String Url) {
		ArrayList<News_Content> array = new ArrayList<News_Content>();

		System.out.println("url_list =" + Url);
		RestClient client = new RestClient(Url);
		String response = new String();
		try {
			client.Execute(RequestMethod.POST);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String status = "";
		response = client.getResponse();

		try {
			JSONArray jsonArray = new JSONArray(response);
			for (int i = 0; i < jsonArray.length(); i++) {
				News_Content cmt = new News_Content();
				cmt.setID(Integer.valueOf(jsonArray.getJSONObject(i).getString("ID")));
				cmt.setTitle(jsonArray.getJSONObject(i).getString("title"));
				cmt.setWebsiteURL(jsonArray.getJSONObject(i)
						.getString("url"));
				cmt.setSubtitle(jsonArray.getJSONObject(i).getString("excerpt"));
				cmt.setFeatureImage(jsonArray.getJSONObject(i).getString("featureImage"));
				cmt.setNewestImage(jsonArray.getJSONObject(i).getString("newestImage"));
				cmt.setWebsiteLogo(jsonArray.getJSONObject(i).getString("websiteLogo"));
				cmt.setWebsiteIcon(jsonArray.getJSONObject(i).getString("websiteIcon"));
				cmt.setDatePost(jsonArray.getJSONObject(i).getString("datePost"));
				array.add(cmt);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("response =" + response);

		return array;
	}
	public static ArrayList<News_Content> getListVideodetail(String Url) {
		ArrayList<News_Content> array = new ArrayList<News_Content>();

		System.out.println("url_list =" + Url);
		RestClient client = new RestClient(Url);
		String response = new String();
		try {
			client.Execute(RequestMethod.POST);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String status = "";
		response = client.getResponse();

		try {
			JSONObject jsonArray = new JSONObject(response);
				News_Content cmt = new News_Content();
				cmt.setID(Integer.valueOf(jsonArray.getString("ID")));
				cmt.setTitle(jsonArray.getString("title"));
				cmt.setWebsiteURL(jsonArray
						.getString("url"));
				cmt.setSubtitle(jsonArray.getString("excerpt"));
				cmt.setFeatureImage(jsonArray.getString("featureImage"));
				cmt.setNewestImage(jsonArray.getString("newestImage"));
				cmt.setWebsiteLogo(jsonArray.getString("websiteLogo"));
				cmt.setWebsiteIcon(jsonArray.getString("websiteIcon"));
				cmt.setWebsiteName(jsonArray.getString("websiteName"));
				cmt.setDatePost(jsonArray.getString("datePost"));
				cmt.setUrl(jsonArray.getString("videoURL"));
				
				array.add(cmt);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("response =" + response);

		return array;
	}
	public static ArrayList<String> getListVideo(String Url) {
		ArrayList<String> array = new ArrayList<String>();

		System.out.println("url_list =" + Url);
		RestClient client = new RestClient(Url);
		String response = new String();
		try {
			client.Execute(RequestMethod.POST);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String status = "";
		response = client.getResponse();

		try {
			JSONObject jsonArray = new JSONObject(response);
				String[] video = jsonArray.getString("videoURL").split("<1><1>###<1>");
				for(int j = 0;j < video.length ; j++ )
				{
					
					if(j==0)
						status = video[j].replace("###<1>", "");
					else if( j == video.length -1 )
						status = video[j].replace("<1><1>###", "");
					array.add(status);
				}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("response =" + response);

		return array;
	}
/*
	public static boolean checkLogin(String Url) {
		boolean login = false;
		System.out.println("url =" + Url);
		RestClient client = new RestClient(Url);
		String response = new String();
		try {
			client.Execute(RequestMethod.POST);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String status = "";
		response = client.getResponse();
		try {
			JSONObject json = new JSONObject(response);
			status = json.getString("status");
			if (status.equals("false"))
				login = false;
			else
				login = true;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				login = true;
				JSONArray jsonArray = new JSONArray(response);
//				Coupon cp = new Coupon();
				for (int i = 0; i < jsonArray.length(); i++) {
					LoginMenuActivity.cp_getInfoUserFb.setCp_type(jsonArray.getJSONObject(i).getString("Name"));
					LoginMenuActivity.cp_getInfoUserFb.setCpNo(jsonArray.getJSONObject(i).getString("CardNo"));
					LoginMenuActivity.cp_getInfoUserFb.setImg_path(jsonArray.getJSONObject(i).getString("Avatar"));
					LoginMenuActivity.cp_getInfoUserFb.setCp_phone(jsonArray.getJSONObject(i).getString("Phone"));
					LoginMenuActivity.cp_getInfoUserFb.setCp_birthday(jsonArray.getJSONObject(i).getString(
							"Birthday"));
					LoginMenuActivity.cp_getInfoUserFb.setCp_gender(jsonArray.getJSONObject(i).getString("Gender"));
					LoginMenuActivity.cp_getInfoUserFb.setCp_Email(jsonArray.getJSONObject(i).getString("Email"));
				}
			} catch (JSONException es) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		
		return login;
	}

	public static String getImageAvartaFacebook(String Url) {
		System.out.println("url =" + Url);
		RestClient client = new RestClient(Url);
		String response = new String();
		try {
			client.Execute(RequestMethod.POST);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String status = "";
		response = client.getResponse();
		try {
			JSONObject oject = new JSONObject(response);
			json = new JSONObject(oject.getString("data"));
			json_array = json.getJSONArray("movie_link");
			Url = json_array.getJSONObject(0).getString("url");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("response =" + response);

		return Url;
	}

	public static Game getGameInfo(String Url) {
		Game game = new Game();
		System.out.println("url =" + Url);
		RestClient client = new RestClient(Url);
		String response = new String();
		try {
			client.Execute(RequestMethod.POST);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String status = "";
		response = client.getResponse();

		try {
			json = new JSONObject(response);
			game.setGameID(json.getString("GameID"));
			game.setID(json.getString("ID"));
			game.setWin(json.getString("Win"));
			game.setCpValue(json.getString("CpValue"));
			game.setCredits(json.getString("Credits"));
			game.setTotal_wins(json.getString("Total_wins"));
			game.setThumbImg(json.getString("ThumbImg"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("response =" + response);

		return game;
	}

	public static ArrayList<Coupon> getCpList(String Url) {
		ArrayList<Coupon> array = new ArrayList<Coupon>();

		System.out.println("url_list =" + Url);
		RestClient client = new RestClient(Url);
		String response = new String();
		try {
			client.Execute(RequestMethod.POST);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String status = "";
		response = client.getResponse();

		try {
			JSONObject oject = new JSONObject(response);
			CouponActivity.totalPage = oject.getInt("total_page");
			JSONArray jsonArray = oject.getJSONArray("data");
			for (int i = 0; i < jsonArray.length(); i++) {
				Coupon cp = new Coupon();
				cp.setCp_type(jsonArray.getJSONObject(i).getString("Name"));
				cp.setCp_value(jsonArray.getJSONObject(i).getString("CPValue"));
				cp.setCp_expired(jsonArray.getJSONObject(i)
						.getString("EndDate"));
				cp.setImg_path(jsonArray.getJSONObject(i).getString("ThumbImg"));
				cp.setId(jsonArray.getJSONObject(i).getString("ID"));
				array.add(cp);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("response =" + response);

		return array;
	}

	public static ArrayList<InviteFB> getFBFriend(
			ArrayList<InviteFB> arrayTemp, String Url) {
		ArrayList<InviteFB> array = new ArrayList<InviteFB>();

		System.out.println("url =" + Url);
		RestClient client = new RestClient(Url);
		String response = new String();
		try {
			client.Execute(RequestMethod.POST);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String status = "";
		response = client.getResponse();

		try {
			JSONObject oject = new JSONObject(response);
			InviteFBActivity.totalPage = oject.getInt("total_page");
			JSONArray jsonArray = oject.getJSONArray("data");
			for (int i = 0; i < jsonArray.length(); i++) {
				InviteFB cp = new InviteFB();
				cp.setFb_id(jsonArray.getJSONObject(i).getString("FBID"));
				cp.setFb_name(jsonArray.getJSONObject(i).getString("Name"));
				cp.setImg_avarta(jsonArray.getJSONObject(i).getString("Avatar"));
				if(InviteFBActivity.flag_checkAll == 1)
					cp.setCheck(true);
				else if(InviteFBActivity.flag_checkAll == 0)
					cp.setCheck(false);
				elsec
				{
					try {
						cp.setCheck(arrayTemp.get(i).isCheck());
					} catch (Exception e) {
						// TODO: handle exception
						cp.setCheck(false);
					}
//				}
				
				array.add(cp);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		System.out.println("response =" + response);

		return array;
	}

	public static ArrayList<Purchage> getPCList(String Url) {
		ArrayList<Purchage> array = new ArrayList<Purchage>();

		System.out.println("url =" + Url);
		RestClient client = new RestClient(Url);
		String response = new String();
		try {
			client.Execute(RequestMethod.POST);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String status = "";
		response = client.getResponse();

		try {
			JSONObject oject = new JSONObject(response);
			PurchaseActivity.totalPage = oject.getInt("total_page");
			JSONArray jsonArray = oject.getJSONArray("data");
			for (int i = 0; i < jsonArray.length(); i++) {
				Purchage cp = new Purchage();
				cp.setId(jsonArray.getJSONObject(i).getString("ID"));
				String[] day = jsonArray.getJSONObject(i)
						.getString("CreateDate").split(" ");
				cp.setReceiptNo(jsonArray.getJSONObject(i).getString(
						"ReceiptNo"));
				cp.setDay(day[0]);
				cp.setAttr(jsonArray.getJSONObject(i).getString("Attr"));

				cp.setDesc(jsonArray.getJSONObject(i).getString("Desc"));
				cp.setPrice(jsonArray.getJSONObject(i).getString("PriceWT"));
				cp.setStoreName(jsonArray.getJSONObject(i).getString(
						"StoreName"));
				cp.setSize(jsonArray.getJSONObject(i).getString("Size"));
				cp.setUPC(jsonArray.getJSONObject(i).getString("UPC"));
				cp.setQty(jsonArray.getJSONObject(i).getString("Qty"));

				array.add(cp);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("response =" + response);

		return array;
	}

	public static ArrayList<Notification> getNotifiList(String Url) {
		ArrayList<Notification> array = new ArrayList<Notification>();

		System.out.println("url =" + Url);
		RestClient client = new RestClient(Url);
		String response = new String();
		try {
			client.Execute(RequestMethod.POST);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String status = "";
		response = client.getResponse();

		try {
			JSONObject oject = new JSONObject(response);
			NotificationActivity.totalPage = oject.getInt("total_page");
			JSONArray jsonArray = oject.getJSONArray("data");
			for (int i = 0; i < jsonArray.length(); i++) {
				Notification cp = new Notification();
				cp.setMessage(jsonArray.getJSONObject(i).getString("Message"));
				String[] day = jsonArray.getJSONObject(i)
						.getString("CreateDate").split(" ");
				cp.setCreateDate(jsonArray.getJSONObject(i).getString(
						"CreateDate"));
				cp.setClicked(jsonArray.getJSONObject(i).getString("clicked"));
				cp.setEmail(jsonArray.getJSONObject(i).getString("Email"));
				cp.setID(jsonArray.getJSONObject(i).getString("ID"));

				array.add(cp);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("response =" + response);

		return array;
	}

	public static int getTotalPage(String link) {
		System.out.println(link);
		RestClient client = new RestClient(link);
		String response = new String();
		try {
			client.Execute(RequestMethod.POST);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response = client.getResponse();
		int totalPage = 0;
		if (response != null) {
			JSONObject json = new JSONObject();
			try {
				json = new JSONObject(response);
				totalPage = json.getInt("total_page");
				return totalPage;
			} catch (JSONException e) {
				System.out.println(e.toString());
			}
		}
		return totalPage;
	}

	public static ArrayList<News> getNewList(String Url) {
		ArrayList<News> array = new ArrayList<News>();

		System.out.println("url =" + Url);
		RestClient client = new RestClient(Url);
		String response = new String();
		try {
			client.Execute(RequestMethod.POST);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String status = "";
		response = client.getResponse();

		try {
			JSONObject oject = new JSONObject(response);
			JSONArray jsonArray = oject.getJSONArray("data");
			for (int i = 0; i < jsonArray.length(); i++) {
				News news = new News();
				news.setNews_id(jsonArray.getJSONObject(i).getString("ID"));
				news.setNews_title(jsonArray.getJSONObject(i)
						.getString("Title"));
				news.setNews_subtitle(jsonArray.getJSONObject(i).getString(
						"SubTitle"));
				news.setNews_image_url(jsonArray.getJSONObject(i).getString(
						"TitleImage"));
				news.setNews_contents(jsonArray.getJSONObject(i).getString(
						"Content"));
				array.add(news);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("response =" + response);

		return array;
	}

	public static ArrayList<BrandList> getBrandList(String Url) {
		ArrayList<BrandList> array = new ArrayList<BrandList>();
		System.out.println("url =" + Url);
		RestClient client = new RestClient(Url);
		String response = new String();
		try {
			client.Execute(RequestMethod.POST);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String status = "";
		response = client.getResponse();

		try {
			JSONArray jsonArray = new JSONArray(response);
			for (int i = 0; i < jsonArray.length(); i++) {
				BrandList oject = new BrandList();
				oject.setFBPage(jsonArray.getJSONObject(i).getString("FBPage"));
				oject.setLogo(jsonArray.getJSONObject(i).getString("Logo"));
				array.add(oject);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return array;

	}

	public static Coupon getCpDetail(String Url) {
		Coupon cp = new Coupon();

		System.out.println("url =" + Url);
		RestClient client = new RestClient(Url);
		String response = new String();
		try {
			client.Execute(RequestMethod.POST);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String status = "";
		response = client.getResponse();
		JSONObject oject = new JSONObject();
		try {
			JSONArray jsonArray = new JSONArray(response);
			for (int i = 0; i < jsonArray.length(); i++) {
				cp.setCp_type(jsonArray.getJSONObject(i).getString("Name"));
				cp.setCp_value(jsonArray.getJSONObject(i).getString("CPValue"));
				cp.setCpNo(jsonArray.getJSONObject(i).getString("CPNo"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("response =" + response);

		return cp;
	}

	public static Coupon getMemberCard(String Url) {
		Coupon cp = new Coupon();

		System.out.println("url =" + Url);
		RestClient client = new RestClient(Url);
		String response = new String();
		try {
			client.Execute(RequestMethod.POST);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String status = "";
		response = client.getResponse();
		JSONObject oject = new JSONObject();
		try {
			JSONArray jsonArray = new JSONArray(response);
			for (int i = 0; i < jsonArray.length(); i++) {
				cp.setCp_type(jsonArray.getJSONObject(i).getString("Name"));
				cp.setCpNo(jsonArray.getJSONObject(i).getString("CardNo"));
				cp.setImg_path(jsonArray.getJSONObject(i).getString("Avatar"));
				cp.setCp_phone(jsonArray.getJSONObject(i).getString("Phone"));
				cp.setCp_birthday(jsonArray.getJSONObject(i).getString(
						"Birthday"));
				cp.setCp_gender(jsonArray.getJSONObject(i).getString("Gender"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("response =" + response);

		return cp;
	}

	public static void getLocation(String Url) {
		System.out.println("url =" + Url);
		RestClient client = new RestClient(Url);
		String response = new String();
		try {
			client.Execute(RequestMethod.POST);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response = client.getResponse();
		// System.out.println("response =" + response);
		try {
			json = new JSONObject(response);
			json_array = json.getJSONArray("results");
			// System.out.println("json_array = "+json_array);
			JSONObject location = json_array.getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("location");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
		}

	}
*/
}
