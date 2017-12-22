package com.ticketing.entity.payment;

import java.math.BigDecimal;

public class PinChargeRequest {
	
	private String msisdn;
	
	private String description;
	
	private Boolean taxable = Boolean.TRUE;
	
	private String callbackURL;
	
	private String txnRef;
	
	private BigDecimal amount;

	public String getMsisdn() {
		return msisdn;
	}

	public String getDescription() {
		return description;
	}

	public Boolean getTaxable() {
		return taxable;
	}

	public String getCallbackURL() {
		return callbackURL;
	}

	public String getTxnRef() {
		return txnRef;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setTaxable(Boolean taxable) {
		this.taxable = taxable;
	}

	public void setCallbackURL(String callbackURL) {
		this.callbackURL = callbackURL;
	}

	public void setTxnRef(String txnRef) {
		this.txnRef = txnRef;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
