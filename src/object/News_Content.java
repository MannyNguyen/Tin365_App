package object;

import java.io.Serializable;

@SuppressWarnings("serial")
public class News_Content implements Serializable {
	private int ID;
	private int categoryID;
	private int mainCatID;
	private int newestImageHeight;
	private int newestImageWidth;

	private String title;
	private String subtitle;
	private String url;
	private String datePost;
	private String excerpt;
	private String featureImage;
	private String newestImage;
	private String websiteName;
	private String websiteURL;
	private String websiteIcon;
	private String websiteLogo;
	private int numberComment;
	public News_Content(int iD, int categoryID, int mainCatID,
			int newestImageHeight, int newestImageWidth, String title,
			String url, String datePost, String excerpt, String featureImage,
			String newestImage, String websiteName, String websiteURL,
			String websiteIcon, String websiteLogo,int numberComment) {
		super();
		ID = iD;
		this.categoryID = categoryID;
		this.mainCatID = mainCatID;
		this.newestImageHeight = newestImageHeight;
		this.newestImageWidth = newestImageWidth;
		this.title = title;
		this.url = url;
		this.datePost = datePost;
		this.excerpt = excerpt;
		this.featureImage = featureImage;
		this.newestImage = newestImage;
		this.websiteName = websiteName;
		this.websiteURL = websiteURL;
		this.websiteIcon = websiteIcon;
		this.websiteLogo = websiteLogo;
		this.numberComment = numberComment;
	}
	public News_Content() {
		super();
	}
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}

	public int getMainCatID() {
		return mainCatID;
	}

	public void setMainCatID(int mainCatID) {
		this.mainCatID = mainCatID;
	}

	public int getNewestImageHeight() {
		return newestImageHeight;
	}

	public void setNewestImageHeight(int newestImageHeight) {
		this.newestImageHeight = newestImageHeight;
	}

	public int getNewestImageWidth() {
		return newestImageWidth;
	}

	public void setNewestImageWidth(int newestImageWidth) {
		this.newestImageWidth = newestImageWidth;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDatePost() {
		return datePost;
	}

	public void setDatePost(String datePost) {
		this.datePost = datePost;
	}

	public String getExcerpt() {
		return excerpt;
	}

	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
	}

	public String getFeatureImage() {
		return featureImage;
	}

	public void setFeatureImage(String featureImage) {
		this.featureImage = featureImage;
	}

	public String getNewestImage() {
		return newestImage;
	}

	public void setNewestImage(String newestImage) {
		this.newestImage = newestImage;
	}

	public String getWebsiteName() {
		return websiteName;
	}

	public void setWebsiteName(String websiteName) {
		this.websiteName = websiteName;
	}

	public String getWebsiteURL() {
		return websiteURL;
	}

	public void setWebsiteURL(String websiteURL) {
		this.websiteURL = websiteURL;
	}

	public String getWebsiteIcon() {
		return websiteIcon;
	}

	public void setWebsiteIcon(String websiteIcon) {
		this.websiteIcon = websiteIcon;
	}

	public String getWebsiteLogo() {
		return websiteLogo;
	}

	public void setWebsiteLogo(String websiteLogo) {
		this.websiteLogo = websiteLogo;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	public int getNumberComment() {
		return numberComment;
	}
	public void setNumberComment(int numberComment) {
		this.numberComment = numberComment;
	}

	
	
	
}