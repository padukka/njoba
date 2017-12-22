package com.ticketing.entity.payment;

public class AmountTransaction {
	
	private String clientCorrelator;
	
	private String endUserId;
	
	private PaymentAmount paymentAmount;
	
	private String referenceCode;
	
	private String transactionOperationStatus;
	
	private String serverReferenceCode;
	
	private String resourceURL;

	public String getClientCorrelator() {
		return clientCorrelator;
	}

	public void setClientCorrelator(String clientCorrelator) {
		this.clientCorrelator = clientCorrelator;
	}

	public String getEndUserId() {
		return endUserId;
	}

	public void setEndUserId(String endUserId) {
		this.endUserId = endUserId;
	}

	public PaymentAmount getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(PaymentAmount paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getReferenceCode() {
		return referenceCode;
	}

	public void setReferenceCode(String referenceCode) {
		this.referenceCode = referenceCode;
	}

	public String getTransactionOperationStatus() {
		return transactionOperationStatus;
	}

	public void setTransactionOperationStatus(String transactionOperationStatus) {
		this.transactionOperationStatus = transactionOperationStatus;
	}

	public String getServerReferenceCode() {
		return serverReferenceCode;
	}

	public void setServerReferenceCode(String serverReferenceCode) {
		this.serverReferenceCode = serverReferenceCode;
	}

	public String getResourceURL() {
		return resourceURL;
	}

	public void setResourceURL(String resourceURL) {
		this.resourceURL = resourceURL;
	}

}
