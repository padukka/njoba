package com.ticketing.dao;
import com.ticketing.entity.Ticket;
import com.ticketing.entity.USSDSession;
public interface ITicketingDAO {
	
	public USSDSession getUssdSession(String sessionId);
	
	public void saveUssdSession(USSDSession ussdSession); 
	
	public void updateUssdSession (USSDSession ussdSession);
	
	public USSDSession getLastUssdSession(String msisdn);
	
	public void saveTicket(Ticket ticket);
	
	public Ticket getTicket(String ticketId);
	
	public void updateTicket(Ticket ticket);
	
	public Ticket getIssuedTicketByServerReference(String serverReference);
	
	public void deleteTicket(String serverReference);
	
}
 