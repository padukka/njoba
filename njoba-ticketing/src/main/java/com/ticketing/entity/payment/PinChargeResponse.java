package com.ticketing.entity.payment;

public class PinChargeResponse {

	private String statusCode;
	
	private String message;
	
	private PaymentData data;

	public String getStatusCode() {
		return statusCode;
	}

	public String getMessage() {
		return message;
	}

	public PaymentData getData() {
		return data;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setData(PaymentData data) {
		this.data = data;
	}
	
	
}
