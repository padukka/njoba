package com.ticketing.entity;

import java.util.Date;

public class USSDSession {
	
	public enum USSDStatus {
		WEL,
		TKT,
		INF,
		PIN,
		CNF,
		INI
	}
	
	public enum USSDSubStatus {
		INI,
		TCK,
		INF,
		PIN,
		PEN,
		NO_PAY,
		RET,
		EXC,
		BAL,
		CNF,
		PAY,
		SKI,
		ISU,
		REJ,
		NO,
		FAIL,
		TERM
	}
		
	private String sessionId;
	
	private String clientCorrelator;
	
	private String state;
	
	private String msisdn;
	
	private Date initiated;
	
	private Date lastUpdated;
	
	private String serverReference;
	
	private Integer retryCount;
	
	private String subStatus;
	
	
	public USSDSession(){
	
	}
	
	public USSDSession(String sessionId, String clientCorrelator, String msisdn){
		this.sessionId = sessionId;
		this.clientCorrelator = clientCorrelator;
		this.msisdn = msisdn;
	}

	public String getSessionId() {
		return sessionId;
	}

	public String getState() {
		return state;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public Date getInitiated() {
		return initiated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setSessionId(String sessionID) {
		this.sessionId = sessionID;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public void setInitiated(Date initiated) {
		this.initiated = initiated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getServerReference() {
		return serverReference;
	}

	public void setServerReference(String serverReference) {
		this.serverReference = serverReference;
	}

	public String getClientCorrelator() {
		return clientCorrelator;
	}

	public void setClientCorrelator(String clientCorrelator) {
		this.clientCorrelator = clientCorrelator;
	}

	public Integer getRetryCount() {
		return retryCount;
	}

	public String getSubStatus() {
		return subStatus;
	}

	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}

	public void setSubStatus(String subStatus) {
		this.subStatus = subStatus;
	}
}
