package com.newconex.conex;


public class MensagemAlerta {

	private String title;
	private String body;
	private String subTitle;
	
	public MensagemAlerta(String title, String body,String subTitle) {	
		this.title = title;
		this.body = body;
		this.subTitle = subTitle;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getBody() {
		return body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public String getSubTitle() {
		return subTitle;
	}
	
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

}
