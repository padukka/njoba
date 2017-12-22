package com.ticketing.entity.payment;

import java.math.BigDecimal;

public class PaymentAmount {
	
	private ChargingInformation chargingInformation;
	
	private ChargingMetaData chargingMetaData;
	
	private BigDecimal totalAmountCharged;

	public ChargingInformation getChargingInformation() {
		return chargingInformation;
	}

	public void setChargingInformation(ChargingInformation chargingInformation) {
		this.chargingInformation = chargingInformation;
	}

	public ChargingMetaData getChargingMetaData() {
		return chargingMetaData;
	}

	public void setChargingMetaData(ChargingMetaData chargingMetaData) {
		this.chargingMetaData = chargingMetaData;
	}

	public BigDecimal getTotalAmountCharged() {
		return totalAmountCharged;
	}

	public void setTotalAmountCharged(BigDecimal totalAmountCharged) {
		this.totalAmountCharged = totalAmountCharged;
	}
	
}
