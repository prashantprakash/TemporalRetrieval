package com.src.ebola;

public class Result {
	private String docId;
	private String text;
	private String url;
	private double score;

	public Result(String docId, String text, double score) {
		this.docId = docId;
		this.text = text;
		this.score = score;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

}
