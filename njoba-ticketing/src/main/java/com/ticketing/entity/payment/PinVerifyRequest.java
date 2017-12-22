package com.ticketing.entity.payment;

public class PinVerifyRequest {
	
	private String pin;
	
	private String serverRef;

	public String getPin() {
		return pin;
	}

	public String getServerRef() {
		return serverRef;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public void setServerRef(String serverRef) {
		this.serverRef = serverRef;
	}

}
