package com.ticketing.entity.payment;

import java.math.BigDecimal;

public class ChargingMetaData {
	
	private String onBehalfOf;
	
	private String purchaseCategoryCode;
	
	private String channel;
	
	private BigDecimal taxAmount = BigDecimal.ZERO;
	
	private String serviceID;

	public String getOnBehalfOf() {
		return onBehalfOf;
	}

	public void setOnBehalfOf(String onBehalfOf) {
		this.onBehalfOf = onBehalfOf;
	}

	public String getPurchaseCategoryCode() {
		return purchaseCategoryCode;
	}

	public void setPurchaseCategoryCode(String purchaseCategoryCode) {
		this.purchaseCategoryCode = purchaseCategoryCode;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getServiceID() {
		return serviceID;
	}

	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
	}
	
}
