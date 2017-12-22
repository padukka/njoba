package com.ticketing.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ticketing.dao.ITicketingDAO;
import com.ticketing.entity.Ticket;
import com.ticketing.entity.Ticket.TicketStatus;
import com.ticketing.entity.USSDSession;
import com.ticketing.utils.TicketingUtils;

@Service
public class TicketingService implements ITicketingService {

	@Autowired
	private ITicketingDAO ticketingDAO;

	@Override
	public Ticket getTicketById(String ticketId) {

		return ticketingDAO.getTicket(ticketId);
	}

	@Override
	public Ticket issueTicket(String msisdn) {

		Ticket ticket = null;
		String ticketNumber = TicketingUtils.newTicketNumber();

		try {
			if (alreadyIssuedTicket(msisdn)) {
				ticketNumber = TicketingUtils.newTicketNumber();
			}

			ticket = new Ticket(ticketNumber, msisdn);
			ticketingDAO.saveTicket(ticket);
		} catch (Exception ex) {
			if (alreadyIssuedTicket(msisdn)) {
				ticketNumber = TicketingUtils.newTicketNumber();
			}

			ticket = new Ticket(ticketNumber, msisdn);
			ticketingDAO.saveTicket(ticket);
		}

		return ticket;
	}

	@Override
	public TicketStatus useTicket(String ticketId) {

		TicketStatus returnStatus = TicketStatus.NO;
		Ticket ticket = ticketingDAO.getTicket(ticketId);

		if (ticket == null) {
			returnStatus = TicketStatus.NO;
		} else {
			if (ticket.getStatus().equals(TicketStatus.CNF.toString())) {
				ticket.setStatus(TicketStatus.USE.toString());
				ticketingDAO.updateTicket(ticket);
				returnStatus = TicketStatus.USE;
			} else if(ticket.getStatus().equals(TicketStatus.USE.toString())){
				returnStatus = TicketStatus.ALREADY_USED;
			}
		}

		return returnStatus;
	}

	@Override
	public USSDSession getLastPaymentConfirmationPendingRequest(String msisdn) {
		
		return ticketingDAO.getLastUssdSession(msisdn);
		
		
	}
	

	@Override
	public void initiateUSSDSession(USSDSession session) {
		ticketingDAO.saveUssdSession(session);

	}

	@Override
	public USSDSession getUSSDSession(String sessionId) {
		return ticketingDAO.getUssdSession(sessionId);
	}

	@Override
	public void updateUSSDSession(USSDSession session) {
		ticketingDAO.updateUssdSession(session);

	}

	private boolean alreadyIssuedTicket(String ticketId) {
		
		return ticketingDAO.getTicket(ticketId) != null;
	}

	@Override
	public Ticket confirmTicket(String serverReference) {
		
		Ticket ticket = ticketingDAO.getIssuedTicketByServerReference(serverReference);
		ticket.setConfirmedDate(new Date());
		ticket.setStatus(TicketStatus.CNF.toString());
		ticketingDAO.updateTicket(ticket);
		
		return ticket;
	}
	
	public void rejectTempTicket(String serverReference) {
		ticketingDAO.deleteTicket(serverReference);		
	}

	@Override
	public void updateTicket(Ticket ticket) {
		ticketingDAO.updateTicket(ticket);
		
	}
	
	

}
