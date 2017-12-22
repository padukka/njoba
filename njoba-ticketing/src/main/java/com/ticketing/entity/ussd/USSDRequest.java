package com.ticketing.entity.ussd;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class USSDRequest {

	private InboundUSSDMessageRequest inboundUSSDMessageRequest;

	public InboundUSSDMessageRequest getInboundUSSDMessageRequest() {
		return inboundUSSDMessageRequest;
	}

	public void setInboundUSSDMessageRequest(InboundUSSDMessageRequest inboundUSSDMessageRequest) {
		this.inboundUSSDMessageRequest = inboundUSSDMessageRequest;
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
