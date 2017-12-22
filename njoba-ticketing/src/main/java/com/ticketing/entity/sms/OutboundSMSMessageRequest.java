package com.ticketing.entity.sms;

import java.util.List;

public class OutboundSMSMessageRequest {
	
	private List<String> address;
	
	private String senderAddress;
	
	private OutboundSMSTextMessage outboundSMSTextMessage;
	
	private String clientCorrelator;
	
	private ReceiptRequest receiptRequest;
	
	private String senderName;
	
	private String serverReferenceCode;
	
	private DeliveryInfoList deliveryInfoList;
	

	public List<String> getAddress() {
		return address;
	}

	public void setAddress(List<String> address) {
		this.address = address;
	}

	public String getSenderAddress() {
		return senderAddress;
	}

	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}

	public OutboundSMSTextMessage getOutboundSMSTextMessage() {
		return outboundSMSTextMessage;
	}

	public void setOutboundSMSTextMessage(OutboundSMSTextMessage outboundSMSTextMessage) {
		this.outboundSMSTextMessage = outboundSMSTextMessage;
	}

	public String getClientCorrelator() {
		return clientCorrelator;
	}

	public void setClientCorrelator(String clientCorrelator) {
		this.clientCorrelator = clientCorrelator;
	}

	public ReceiptRequest getReceiptRequest() {
		return receiptRequest;
	}

	public void setReceiptRequest(ReceiptRequest receiptRequest) {
		this.receiptRequest = receiptRequest;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getServerReferenceCode() {
		return serverReferenceCode;
	}

	public void setServerReferenceCode(String serverReferenceCode) {
		this.serverReferenceCode = serverReferenceCode;
	}

	public DeliveryInfoList getDeliveryInfoList() {
		return deliveryInfoList;
	}

	public void setDeliveryInfoList(DeliveryInfoList deliveryInfoList) {
		this.deliveryInfoList = deliveryInfoList;
	}
	
}
