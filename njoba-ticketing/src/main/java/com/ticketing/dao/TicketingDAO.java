package com.ticketing.dao;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ticketing.entity.Ticket;
import com.ticketing.entity.USSDSession;

@Transactional
@Repository
public class TicketingDAO implements ITicketingDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public USSDSession getUssdSession(String sessionId) {
		String sql = "SELECT * FROM ussd_session WHERE session_id = ?";
		RowMapper<USSDSession> rowMapper = new BeanPropertyRowMapper<USSDSession>(USSDSession.class);
		USSDSession ussdSession = null;
		try {
			ussdSession = jdbcTemplate.queryForObject(sql, rowMapper, sessionId);
		} catch (EmptyResultDataAccessException e) {

		}
		return ussdSession;
	}

	@Override
	public void saveUssdSession(USSDSession ussdSession) {
		String sql = "INSERT INTO ussd_session (session_id, client_correlator, state, msisdn, initiated, last_updated, server_reference, sub_status) values (?, ?, ?, ?, ?, ?, ?, ?)";

		Date currentDate = new Date();
		jdbcTemplate.update(sql, ussdSession.getSessionId(), ussdSession.getClientCorrelator(),
				ussdSession.getState(), ussdSession.getMsisdn(), currentDate, currentDate,
				ussdSession.getServerReference(), ussdSession.getSubStatus());
	}

	@Override
	public void updateUssdSession(USSDSession ussdSession) {
		String sql = "UPDATE ussd_session SET state=?,sub_status=?, last_updated=?, server_reference=?, retry_count= ? WHERE session_id=?";
		jdbcTemplate.update(sql, ussdSession.getState(),ussdSession.getSubStatus(), new Date(), ussdSession.getServerReference(),ussdSession.getRetryCount(),
				ussdSession.getSessionId());
	}

	@Override
	public USSDSession getLastUssdSession(String msisdn) {

		String sql = "SELECT * FROM ussd_session WHERE msisdn = ? AND sub_status = 'PEN' and state = 'TKT' order by last_updated DESC limit 1  ";
		RowMapper<USSDSession> rowMapper = new BeanPropertyRowMapper<USSDSession>(USSDSession.class);
		USSDSession ussdSession = null;
		try {
			ussdSession = jdbcTemplate.queryForObject(sql, rowMapper, msisdn);
		} catch (EmptyResultDataAccessException e) {

		}

		return ussdSession;

	}

	@Override
	public void saveTicket(Ticket ticket) {
		String sql = "INSERT INTO ticket (ticket_id, msisdn, issued_date, status) values (?, ?, ?, ?)";
		jdbcTemplate.update(sql, ticket.getTicketId(), ticket.getMsisdn(), new Date(), ticket.getStatus());

	}

	@Override
	public Ticket getTicket(String ticketId) {
		String sql = "SELECT * FROM ticket WHERE ticket_id = ?";
		RowMapper<Ticket> rowMapper = new BeanPropertyRowMapper<Ticket>(Ticket.class);

		Ticket ticket = null;
		try {
			ticket = jdbcTemplate.queryForObject(sql, rowMapper, ticketId);
		} catch (EmptyResultDataAccessException e) {

		}
		return ticket;
	}

	@Override
	public void updateTicket(Ticket ticket) {
		String sql = "UPDATE ticket SET status=?, confirmed_date = ? , used_date=?, server_reference = ? WHERE ticket_id=?";
		jdbcTemplate.update(sql, ticket.getStatus(),
				(ticket.getConfirmedDate() != null ? ticket.getConfirmedDate() : null),
				(ticket.getUsedDate() != null ? ticket.getUsedDate() : null),
				(ticket.getServerReference() != null ? ticket.getServerReference() : null), ticket.getTicketId());

	}

	@Override
	public Ticket getIssuedTicketByServerReference(String serverReference) {
		String sql = "SELECT * FROM ticket WHERE server_reference = ? AND status = 'ISU'";
		RowMapper<Ticket> rowMapper = new BeanPropertyRowMapper<Ticket>(Ticket.class);

		Ticket ticket = null;
		try {
			ticket = jdbcTemplate.queryForObject(sql, rowMapper, serverReference);
		} catch (EmptyResultDataAccessException e) {

		}
		return ticket;
	}

	@Override
	public void deleteTicket(String serverReference) {
		
		String sql = "DELETE FROM ticket WHERE server_reference=?";
		jdbcTemplate.update(sql, serverReference);
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}
