package com.mycompany.api;

import java.util.Date;

public class Response {
	private String message;
	private String time;

	public Response(String message) {
		this.time = new Date().toString();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
