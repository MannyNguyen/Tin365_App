package object;

import java.util.ArrayList;

public class News_Detail {
	private int mId;
	private String mTitle;
	private String mHeader;
	private String mDate;
	private String mSourceName;
	private String mSourceIcon;
	private String mSourceUrl;
	private ArrayList<String> mContent;
	private ArrayList<String> mImgUrls;
	private ArrayList<String> mImgHints;
	private ArrayList<EachVideoBongDa365> mVideoUrls;
	private int numberComment;

	public News_Detail(int mId, String mTitle, String mHeader, String mDate,
			String mSourceName, String mSourceIcon, String mSourceUrl,
			ArrayList<String> mContent, ArrayList<String> mImgUrls,
			ArrayList<String> mImgHints,
			ArrayList<EachVideoBongDa365> mVideoUrls, int numberComment) {
		super();
		this.mId = mId;
		this.mTitle = mTitle;
		this.mHeader = mHeader;
		this.mDate = mDate;
		this.mSourceName = mSourceName;
		this.mSourceIcon = mSourceIcon;
		this.mSourceUrl = mSourceUrl;
		this.mContent = mContent;
		this.mImgUrls = mImgUrls;
		this.mImgHints = mImgHints;
		this.mVideoUrls = mVideoUrls;

		this.numberComment = numberComment;
	}

	public News_Detail() {
		super();
	}

	public News_Detail(int mId, String mTitle, String mHeader, String mDate,
			String mSourceName, String mSourceIcon, String mSourceUrl,
			ArrayList<String> mContent, ArrayList<String> mImgUrls,
			ArrayList<String> mImgHints, int numberComment) {
		super();
		this.mId = mId;
		this.mTitle = mTitle;
		this.mHeader = mHeader;
		this.mDate = mDate;
		this.mSourceName = mSourceName;
		this.mSourceIcon = mSourceIcon;
		this.mSourceUrl = mSourceUrl;
		this.mContent = mContent;
		this.mImgUrls = mImgUrls;
		this.mImgHints = mImgHints;
		this.numberComment = numberComment;
	}

	public int getmId() {
		return mId;
	}

	public void setmId(int mId) {
		this.mId = mId;
	}

	public String getmTitle() {
		return mTitle;
	}

	public int getNumberComment() {
		return numberComment;
	}

	public void setNumberComment(int numberComment) {
		this.numberComment = numberComment;
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public String getmHeader() {
		return mHeader;
	}

	public void setmHeader(String mHeader) {
		this.mHeader = mHeader;
	}

	public String getmDate() {
		return mDate;
	}

	public void setmDate(String mDate) {
		this.mDate = mDate;
	}

	public String getmSourceName() {
		return mSourceName;
	}

	public void setmSourceName(String mSourceName) {
		this.mSourceName = mSourceName;
	}

	public String getmSourceIcon() {
		return mSourceIcon;
	}

	public void setmSourceIcon(String mSourceIcon) {
		this.mSourceIcon = mSourceIcon;
	}

	public String getmSourceUrl() {
		return mSourceUrl;
	}

	public void setmSourceUrl(String mSourceUrl) {
		this.mSourceUrl = mSourceUrl;
	}

	public ArrayList<String> getmContent() {
		return mContent;
	}

	public void setmContent(ArrayList<String> mContent) {
		this.mContent = mContent;
	}

	public ArrayList<String> getmImgUrls() {
		return mImgUrls;
	}

	public void setmImgUrls(ArrayList<String> mImgUrls) {
		this.mImgUrls = mImgUrls;
	}

	public ArrayList<String> getmImgHints() {
		return mImgHints;
	}

	public void setmImgHints(ArrayList<String> mImgHints) {
		this.mImgHints = mImgHints;
	}

	public ArrayList<EachVideoBongDa365> getmVideoUrls() {
		return mVideoUrls;
	}

	public void setmVideoUrls(ArrayList<EachVideoBongDa365> mVideoUrls) {
		this.mVideoUrls = mVideoUrls;
	}

}
