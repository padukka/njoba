package com.ticketing.entity.sms;

public class DeliveryInfo {
	
	private String address;
	
	private String deliveryStatus;
	
	private String messageReferenceCode;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	public String getMessageReferenceCode() {
		return messageReferenceCode;
	}

	public void setMessageReferenceCode(String messageReferenceCode) {
		this.messageReferenceCode = messageReferenceCode;
	}

}
