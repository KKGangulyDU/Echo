package edu.univdhaka.apps.echo.domain;

public class Tweet {
	String post;
	String issues;
	String selectedDate;
	byte[] convertedImage;
	private String latitude;
	private String longitude;

	public String getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(String selectedDate) {
		this.selectedDate = selectedDate;
	}

	public String getPost() {
		return post;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public byte[] getConvertedImage() {
		return convertedImage;
	}

	public void setConvertedImage(byte[] convertedImage) {
		this.convertedImage = convertedImage;
	}

	public String getIssues() {
		return issues;
	}

	public void setIssues(String issues) {
		this.issues = issues;
	}

}
