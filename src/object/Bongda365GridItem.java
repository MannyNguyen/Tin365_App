package object;

public class Bongda365GridItem {
	private int id;
	private String title;
	private String date;
	private String url_image;
	public Bongda365GridItem(int id, String title, String date, String url_image) {
		super();
		this.id = id;
		this.title = title;
		this.date = date;
		this.url_image = url_image;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getUrl_image() {
		return url_image;
	}
	public void setUrl_image(String url_image) {
		this.url_image = url_image;
	}

	
}
