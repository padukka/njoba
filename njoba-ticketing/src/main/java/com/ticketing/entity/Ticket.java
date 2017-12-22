package com.ticketing.entity;

import java.util.Date;

public class Ticket {
	
	public enum TicketStatus {
		CNF,
		ISU,
		USE,
		NO,
		REJ,
		ALREADY_USED
	}
	
	private String ticketId;
	
	private String msisdn;
	
	private Date issuedDate;
	
	private Date confirmedDate;
	
	private Date usedDate;
	
	private String status = TicketStatus.ISU.toString();
	
	private String serverReference;

	
	public Ticket(){
		
	}
	public Ticket(String ticketId, String msisdn) {
		this.ticketId = ticketId;
		this.msisdn = msisdn;
	}
	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public Date getIssuedDate() {
		return issuedDate;
	}

	public void setIssuedDate(Date issuedDate) {
		this.issuedDate = issuedDate;
	}

	public Date getUsedDate() {
		return usedDate;
	}

	public void setUsedDate(Date usedDate) {
		this.usedDate = usedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	public Date getConfirmedDate() {
		return confirmedDate;
	}
	public String getServerReference() {
		return serverReference;
	}
	public void setConfirmedDate(Date confirmedDate) {
		this.confirmedDate = confirmedDate;
	}
	public void setServerReference(String serverReference) {
		this.serverReference = serverReference;
	}
	@Override
	public String toString() {
		return "Ticket [ticketId=" + ticketId + ", msisdn=" + msisdn + ", status=" + status + "]";
	}
	
}
