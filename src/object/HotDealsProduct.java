package object;

public class HotDealsProduct {
	
	private int idproduct;
	private int idcity;
	private String namecity;
	private int idcategories;
	private String namecategories;
	private String title;
	private String imageurl;
	private String price;
	private String pricecompare;
	private String linkdetail;
	private String createdate;
	public HotDealsProduct(int idproduct, int idcity, String namecity,
			int idcategories, String namecategories, String title,
			String imageurl, String price, String pricecompare,
			String linkdetail, String createdate) {
		super();
		this.idproduct = idproduct;
		this.idcity = idcity;
		this.namecity = namecity;
		this.idcategories = idcategories;
		this.namecategories = namecategories;
		this.title = title;
		this.imageurl = imageurl;
		this.price = price;
		this.pricecompare = pricecompare;
		this.linkdetail = linkdetail;
		this.createdate = createdate;
	}
	public int getIdproduct() {
		return idproduct;
	}
	public void setIdproduct(int idproduct) {
		this.idproduct = idproduct;
	}
	public int getIdcity() {
		return idcity;
	}
	public void setIdcity(int idcity) {
		this.idcity = idcity;
	}
	public String getNamecity() {
		return namecity;
	}
	public void setNamecity(String namecity) {
		this.namecity = namecity;
	}
	public int getIdcategories() {
		return idcategories;
	}
	public void setIdcategories(int idcategories) {
		this.idcategories = idcategories;
	}
	public String getNamecategories() {
		return namecategories;
	}
	public void setNamecategories(String namecategories) {
		this.namecategories = namecategories;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getPricecompare() {
		return pricecompare;
	}
	public void setPricecompare(String pricecompare) {
		this.pricecompare = pricecompare;
	}
	public String getLinkdetail() {
		return linkdetail;
	}
	public void setLinkdetail(String linkdetail) {
		this.linkdetail = linkdetail;
	}
	public String getCreatedate() {
		return createdate;
	}
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}
	
}
