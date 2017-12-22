package com.ticketing.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TicketRowMapper implements RowMapper<Ticket> {

	@Override
	public Ticket mapRow(ResultSet row, int rowNum) throws SQLException {
		Ticket ticket = new Ticket();
		ticket.setTicketId(row.getString("ticket_id"));
		ticket.setMsisdn(row.getString("msisdn"));
		ticket.setIssuedDate(row.getDate("issued_date"));
		ticket.setConfirmedDate(row.getDate("confirmed_date"));
		ticket.setUsedDate(row.getDate("issued_date"));
		ticket.setStatus(row.getString("status"));
		ticket.setServerReference("server_reference");
		
		return ticket;
	}

}
