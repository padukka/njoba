package com.ticketing.entity.balancecheck;

import java.math.BigDecimal;

public class AccountInfo {
	
	private String accountType;
	
	private String accountStatus;
	
	private BigDecimal creditLimit = BigDecimal.ZERO;
	
	private BigDecimal balance = BigDecimal.ZERO;

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public BigDecimal getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(BigDecimal creditLimit) {
		this.creditLimit = creditLimit;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
}
