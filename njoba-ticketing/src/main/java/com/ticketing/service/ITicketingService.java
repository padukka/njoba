package com.ticketing.service;

import com.ticketing.entity.Ticket;
import com.ticketing.entity.Ticket.TicketStatus;
import com.ticketing.entity.USSDSession;

public interface ITicketingService {
	
	public Ticket getTicketById(String ticketId);

	public Ticket issueTicket(String msisdn);

	public TicketStatus useTicket(String ticketId);
	
	public Ticket confirmTicket(String serverReference);
	
	public void updateTicket(Ticket ticket);
	
	public USSDSession getLastPaymentConfirmationPendingRequest(String msisdn);
	
	public void initiateUSSDSession(USSDSession session);
	
	public USSDSession getUSSDSession(String sessionId);
	
	public void updateUSSDSession(USSDSession session);
	
	public void rejectTempTicket(String serverReference);

}
