package com.ticketing.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class USSDSessionRowMapper implements RowMapper<USSDSession> {

	@Override
	public USSDSession mapRow(ResultSet row, int rowNum) throws SQLException {
		
		USSDSession ussdSession = new USSDSession();
		ussdSession.setSessionId(row.getString("session_id"));
		ussdSession.setClientCorrelator(row.getString("client_correlator"));
		ussdSession.setMsisdn(row.getString("msisdn"));
		ussdSession.setState(row.getString("state"));
		ussdSession.setInitiated(row.getDate("initiated"));
		ussdSession.setLastUpdated(row.getDate("last_updated"));
		ussdSession.setServerReference(row.getString("server_reference"));
		ussdSession.setSubStatus(row.getString("sub_status"));
		ussdSession.setRetryCount(row.getInt("retry_count"));
				
		return ussdSession;
	}

}
