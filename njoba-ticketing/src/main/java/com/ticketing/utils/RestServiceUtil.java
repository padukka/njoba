package com.ticketing.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ticketing.entity.balancecheck.BalanceCheckResponse;
import com.ticketing.entity.payment.PinChargeRequest;
import com.ticketing.entity.payment.PinChargeResponse;
import com.ticketing.entity.payment.PinVerifyRequest;
import com.ticketing.entity.sms.OutboundSMSMessageRequest;
import com.ticketing.entity.sms.OutboundSMSTextMessage;
import com.ticketing.entity.sms.SMSRequest;
import com.ticketing.entity.sms.SMSResponse;
import com.ticketing.entity.token.TokenResponse;

@Service
public class RestServiceUtil {
	
	
	@Value("${username}")
	private String username;
	
	@Value("${password}")
	private String password;
	
	@Value("${consumerKey}")
	private String consumerKey;
	
	@Value("${consumerSecret}")
	private String consumerSecret;
	
	 private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	 public BigDecimal getBalance(String msisdn) {

		 	msisdn = msisdn.replace("tel:+", "");
		 	BigDecimal balance = BigDecimal.ZERO;
	    	String balanceUrl = "https://ideabiz.lk/apicall/balancecheck/v3/"+msisdn+"/transactions/amount/balance";
	    	String token = getToken();
	    	HttpHeaders headers = new HttpHeaders();
	    	headers.setContentType(MediaType.APPLICATION_JSON);
	    	headers.set("Authorization", "Bearer "+token);
	        RestTemplate restTemplate = new RestTemplate();
		    String url = balanceUrl;
	        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
	        
	        try {
	        	ResponseEntity<BalanceCheckResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, BalanceCheckResponse.class);
	        	BalanceCheckResponse balaceResponse = responseEntity.getBody();
	        	balance = balaceResponse.getAccountInfo().getBalance(); 
	        } catch (Exception ex) {
	        	LOGGER.error("Error in retrieving balance", ex);
	        }
	        
	        return balance;
	    }
	 
	 public String getToken() {
		 			 	
		 	String plainTextKeySecretPair = consumerKey+":"+consumerSecret;
		 	String token = null;
		 	String url = "https://ideabiz.lk/apicall/token?grant_type=password&username="+username+"&password="+password+"&scope=PRODUCTION";
	    	
	    	
	    	String authHeader =  Base64.getEncoder().encodeToString(plainTextKeySecretPair.getBytes());
	    	
	    	HttpHeaders headers = new HttpHeaders();
	    	headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	    	headers.set("Authorization", "Basic "+ authHeader);
	        RestTemplate restTemplate = new RestTemplate();
	
	        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
	        
	        try {
	        	ResponseEntity<TokenResponse> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, TokenResponse.class);
	        	TokenResponse tokenResponse = responseEntity.getBody();
	        	token = tokenResponse.getAccess_token();
	        } catch (Exception ex) {
	        	LOGGER.error("Error in retrieving token", ex);
	        }
	        
	        return token;
	    }
	 
	 public String doPayment (String msisdn, BigDecimal amount, String txnRef, String description) {
		 
		 	String serverRef = null;
	    	String token = getToken();
	    	HttpHeaders headers = new HttpHeaders();
	    	headers.setContentType(MediaType.APPLICATION_JSON);
	    	headers.set("Authorization", "Bearer "+token);
	        RestTemplate restTemplate = new RestTemplate();
		    String url = "https://ideabiz.lk/apicall/pin/payment/v1/charge";
		    
		    PinChargeRequest chargeRequest = new PinChargeRequest();
		    chargeRequest.setAmount(amount);
		    chargeRequest.setMsisdn(msisdn);
		    chargeRequest.setTxnRef(txnRef);
		    chargeRequest.setDescription(description);
		    
		    HttpEntity<PinChargeRequest> requestEntity = new HttpEntity<PinChargeRequest>(chargeRequest, headers);
	        
	        try {
	        	ResponseEntity<PinChargeResponse> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, PinChargeResponse.class);
	        	PinChargeResponse pinChargeResponse = responseEntity.getBody();
	        	
	        	if("SUCCESS".equals(pinChargeResponse.getStatusCode())) {
	        		
	        		if(pinChargeResponse.getData() != null && "PENDING_AUTH".equals(pinChargeResponse.getData().getStatus()) && pinChargeResponse.getData().getServerRef() !=  null){
	        			serverRef = pinChargeResponse.getData().getServerRef();
	        		}
	        	}

	        } catch (Exception ex) {
	        	LOGGER.error("Error in initiating payment", ex);
	        }
	        
	        return serverRef;
		
	 }
	 
	 public String verifyPIN (String pin, String serverReference) {
		 
			String status = "FAILED";
	    	String token = getToken();
	    	HttpHeaders headers = new HttpHeaders();
	    	headers.setContentType(MediaType.APPLICATION_JSON);
	    	headers.set("Authorization", "Bearer "+token);
	        RestTemplate restTemplate = new RestTemplate();
		    String url = "https://ideabiz.lk/apicall/pin/payment/v1/submitPin";
		    
		    
		    PinVerifyRequest pinVerifyRequest = new PinVerifyRequest();
		    pinVerifyRequest.setPin(pin);
		    pinVerifyRequest.setServerRef(serverReference);
		    
;
		    
		    HttpEntity<PinVerifyRequest> requestEntity = new HttpEntity<PinVerifyRequest>(pinVerifyRequest, headers);
	        
	        try {
	        	ResponseEntity<PinChargeResponse> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, PinChargeResponse.class);
	        	PinChargeResponse pinChargeResponse = responseEntity.getBody();
	        	
	        	if("SUCCESS".equals(pinChargeResponse.getStatusCode()) && "Transaction Success".equals(pinChargeResponse.getMessage()) && "SUCCESS".equals(pinChargeResponse.getData().getStatus())) {
	        		status = "SUCCESS";
	        	} else if("ERROR".equals(pinChargeResponse.getStatusCode())){
	        		
	        		if("Wrong Pin".equals(pinChargeResponse.getMessage())) {
	        			status = "FAILED";
	        		} else if("Max attempt exceeded".equals(pinChargeResponse.getMessage())){
	        			status = "MAX_RETRY";
	        		}
	        		if("Max attempt exceeded".equals(pinChargeResponse.getMessage())) {
	        			status = "MAX_RETRY";
	        		}
	        		
	        	}

	        } catch (Exception ex) {
	        	LOGGER.error("Error in pin verification", ex);
	        }
	        
	        return status;
		 
	 }
	 
	 public boolean sendSMS (String msisdn, String message, String clientCorrelator) {
		 
		    boolean success = false;
	    	String token = getToken();
	    	HttpHeaders headers = new HttpHeaders();
	    	headers.setContentType(MediaType.APPLICATION_JSON);
	    	headers.set("Authorization", "Bearer "+token);
	        RestTemplate restTemplate = new RestTemplate();
		    String url = "https://ideabiz.lk/apicall/smsmessaging/v3/outbound/87711/requests";
		    
		    
		    SMSRequest smsRequest = new SMSRequest();
		    OutboundSMSMessageRequest outbound = new OutboundSMSMessageRequest();
		    
		    List<String> address = new ArrayList();
		    address.add(msisdn);
		    
		    OutboundSMSTextMessage txtMesssage = new OutboundSMSTextMessage();
		    txtMesssage.setMessage(message);
		    outbound.setAddress(address);
		    outbound.setClientCorrelator(clientCorrelator);
		    outbound.setSenderAddress("tel:87711");
		    outbound.setSenderName("NJOBA");
		    txtMesssage.setMessage(message);
		    outbound.setOutboundSMSTextMessage(txtMesssage);
		    smsRequest.setOutboundSMSMessageRequest(outbound);
		    
		    		    
		    HttpEntity<SMSRequest> requestEntity = new HttpEntity<SMSRequest>(smsRequest, headers);
	        
	        try {
	        	ResponseEntity<SMSResponse> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, SMSResponse.class);
	        	SMSResponse smsResponse = responseEntity.getBody();
	        	
	        	if(smsResponse.getOutboundSMSMessageRequest().getDeliveryInfoList() != null) {
	        		success = true;
	        	}
	        	
	        	
	        } catch (Exception ex) {
	        	ex.printStackTrace();
	        }
		 
		 return success;
		 
	 }

}
