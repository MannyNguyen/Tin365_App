package object;

public class WebSource {
	private int id;
	private String name;
	private String urlIcon;
	public WebSource(int id, String name, String urlIcon) {
		super();
		this.id = id;
		this.name = name;
		this.urlIcon = urlIcon;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrlIcon() {
		return urlIcon;
	}
	public void setUrlIcon(String urlIcon) {
		this.urlIcon = urlIcon;
	}
	
	
}
