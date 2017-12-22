package com.ticketing.entity.ussd;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class USSDResponse {

	private OutboundUSSDMessageRequest outboundUSSDMessageRequest;

	public OutboundUSSDMessageRequest getOutboundUSSDMessageRequest() {
		return outboundUSSDMessageRequest;
	}

	public void setOutboundUSSDMessageRequest(OutboundUSSDMessageRequest outboundUSSDMessageRequest) {
		this.outboundUSSDMessageRequest = outboundUSSDMessageRequest;
	}
	
	 @Override
	    public String toString() {
	    	ObjectMapper mapper = new ObjectMapper();
	    	
	    	String jsonString = "";
			try {
				mapper.enable(SerializationFeature.INDENT_OUTPUT);
				jsonString = mapper.writeValueAsString(this);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			
	    	return jsonString;
	    }

}
