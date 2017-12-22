package com.ticketing.entity.payment;

import java.math.BigDecimal;

public class ChargingInformation {
	
	private BigDecimal amount = BigDecimal.ZERO;
	
	private String currency;
	
	private String description;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
