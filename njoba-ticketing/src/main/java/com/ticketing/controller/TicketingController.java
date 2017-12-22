package com.ticketing.controller;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

import com.ticketing.entity.Ticket;
import com.ticketing.entity.Ticket.TicketStatus;
import com.ticketing.entity.USSDSession;
import com.ticketing.entity.USSDSession.USSDStatus;
import com.ticketing.entity.USSDSession.USSDSubStatus;
import com.ticketing.entity.ussd.InboundUSSDMessageRequest;
import com.ticketing.entity.ussd.OutboundUSSDMessageRequest;
import com.ticketing.entity.ussd.ResponseRequest;
import com.ticketing.entity.ussd.USSDRequest;
import com.ticketing.entity.ussd.USSDResponse;
import com.ticketing.service.ITicketingService;
import com.ticketing.utils.RestServiceUtil;


@Controller
@RequestMapping({"ticketing"})
public class TicketingController
{
  @Autowired
  private ITicketingService ticketingService;
  @Autowired
  private RestServiceUtil restUtil;

  public static final String MO_INIT = "moinit";
  public static final String MO_CONT = "mocont";
  public static final String MO_FIN = "mofin";
  public static final String MT_FIN = "mtfin";
  public static final int retry_count = 3;
  
  @Value("${ticketPrice}")
  public BigDecimal ticketPrice;
  
  private final Logger LOGGER = LoggerFactory.getLogger(getClass());
  

  @PostMapping({"ussd"})
  public ResponseEntity<USSDResponse> processUssdRequest(@RequestBody USSDRequest ussdMessage, UriComponentsBuilder builder)
  {
    LOGGER.debug(ussdMessage.toString());
    
    InboundUSSDMessageRequest inboundUssd = ussdMessage.getInboundUSSDMessageRequest();
    
    USSDResponse ussdResponse = new USSDResponse();
    OutboundUSSDMessageRequest outboundRequest = new OutboundUSSDMessageRequest();
    
    LOGGER.info(" New request from:" + inboundUssd.getAddress() + "- ussdSession:" + inboundUssd.getSessionID());
    outboundRequest.setAddress(inboundUssd.getAddress());
    outboundRequest.setClientCorrelator(inboundUssd.getClientCorrelator());
    outboundRequest.setKeyword(inboundUssd.getKeyword());
    outboundRequest.setShortCode(inboundUssd.getShortCode());
    outboundRequest.setUssdAction(MO_CONT);
    outboundRequest.setSessionID(inboundUssd.getSessionID());
    ResponseRequest responseRequest = new ResponseRequest();
    responseRequest.setNotifyURL("http://18.218.13.239:8080/ticketing/ussdmenu");
    

    USSDSession ussdSession = new USSDSession(inboundUssd.getSessionID(), inboundUssd.getClientCorrelator(), inboundUssd.getAddress());
    

    outboundRequest.setOutboundUSSDMessage("Welcome to Swarnamela 2017!\n1. Ticket Purchasing\n2. Payment Confirmation\n3. Event Information");
    
    ussdSession.setState(USSDSession.USSDStatus.WEL.toString());
    ussdSession.setSubStatus(USSDSubStatus.INI.toString());
    ticketingService.initiateUSSDSession(ussdSession);
    outboundRequest.setResponseRequest(responseRequest);
    ussdResponse.setOutboundUSSDMessageRequest(outboundRequest);
    
    LOGGER.debug(ussdResponse.toString());
    
    return new ResponseEntity<USSDResponse>(ussdResponse, HttpStatus.OK);
  }
  


  @PostMapping({"ussdmenu"})
  public ResponseEntity<USSDResponse> processUssdMenuRequest(@RequestBody USSDRequest ussdMessage, UriComponentsBuilder builder)
  {
    LOGGER.debug(ussdMessage.toString());
    
    InboundUSSDMessageRequest inboundUssd = ussdMessage.getInboundUSSDMessageRequest();
    
    USSDSession ussdSession = ticketingService.getUSSDSession(inboundUssd.getSessionID());
    
    USSDResponse ussdResponse = new USSDResponse();
    OutboundUSSDMessageRequest outboundRequest = new OutboundUSSDMessageRequest();
    outboundRequest.setAddress(inboundUssd.getAddress());
    outboundRequest.setClientCorrelator(inboundUssd.getClientCorrelator());
    outboundRequest.setKeyword(inboundUssd.getKeyword());
    outboundRequest.setShortCode(inboundUssd.getShortCode());
    outboundRequest.setSessionID(inboundUssd.getSessionID());
    
    ResponseRequest responseRequest = new ResponseRequest();
    
    String logInfo = "MSISDN: " + inboundUssd.getAddress() + "- USSD Session:" + inboundUssd.getSessionID();
    switch (USSDStatus.valueOf(ussdSession.getState()))
    {
    case WEL: 
      if ("1".equals(inboundUssd.getInboundUSSDMessage())) {
        outboundRequest.setOutboundUSSDMessage("Please confirm the purchasing of a ticket for Rs. 150 (service charges and taxes apply).\n1. Yes\n2. Cancel");
        
        outboundRequest.setUssdAction(MO_CONT);
        responseRequest.setNotifyURL("http://18.218.13.239:8080/ticketing/ussdmenu");
        responseRequest.setCallbackData("");
        ussdSession.setState(USSDSession.USSDStatus.TKT.toString());
        ussdSession.setSubStatus(USSDSubStatus.TCK.toString());
        LOGGER.info(logInfo + "- SubStatus: " + ussdSession.getSubStatus());
      }
      else if ("2".equals(inboundUssd.getInboundUSSDMessage()))
      {

        USSDSession lastUssdSession = ticketingService.getLastPaymentConfirmationPendingRequest(inboundUssd.getAddress());
        
        if ((lastUssdSession != null) && (lastUssdSession.getServerReference() != null)) {
          outboundRequest.setOutboundUSSDMessage("Enter the 6- digit payment confirmation PIN");
          outboundRequest.setUssdAction(MO_CONT);
          responseRequest.setNotifyURL("http://18.218.13.239:8080/ticketing/ussdmenu");
          responseRequest.setCallbackData("");
          ussdSession.setState(USSDSession.USSDStatus.PIN.toString());
          ussdSession.setSubStatus(USSDSubStatus.PIN.toString());
          LOGGER.info(logInfo + "- SubStatus: " + ussdSession.getSubStatus());
        } else {
          outboundRequest.setOutboundUSSDMessage("There is no pending payment for this mobile number\n1. Previous Menu");
          
          outboundRequest.setUssdAction(MO_CONT);
          responseRequest.setNotifyURL("http://18.218.13.239:8080/ticketing/ussdmenu");
          responseRequest.setCallbackData("");
          ussdSession.setState(USSDSession.USSDStatus.INI.toString());
          ussdSession.setSubStatus(USSDSubStatus.NO_PAY.toString());
          LOGGER.info(logInfo + "- SubStatus: " + ussdSession.getSubStatus());
        }
      }
      else if ("3".equals(inboundUssd.getInboundUSSDMessage()))
      {
        outboundRequest.setOutboundUSSDMessage("Welcome to Swarnamela 2017!\n  December 21-24 @ BOI Grounds, Katunayaka");
        
        outboundRequest.setUssdAction(MT_FIN);
        responseRequest.setNotifyURL("http://18.218.13.239:8080/ticketing/ussdmenu");
        responseRequest.setCallbackData("");
        ussdSession.setState(USSDSession.USSDStatus.INF.toString());
        ussdSession.setSubStatus(USSDSubStatus.INF.toString());
        LOGGER.info(logInfo + "- SubStatus: " + ussdSession.getSubStatus());
      }
      else {
        Integer retryCount = ussdSession.getRetryCount();
        
        Integer availableRetries = null;
        if (retryCount != null) {
          availableRetries = retryCount;
        } else {
          availableRetries = retry_count;
        }
        
        availableRetries = availableRetries - 1;
        
        if (availableRetries > 0) {
          outboundRequest.setOutboundUSSDMessage("Invalid Input.\n1. Ticket Purchasing\n2. Payment Confirmation\n3. Event Information");
          
          outboundRequest.setUssdAction(MO_CONT);
          responseRequest.setNotifyURL("http://18.218.13.239:8080/ticketing/ussdmenu");
          ussdSession.setRetryCount(availableRetries);
          ussdSession.setSubStatus(USSDSubStatus.RET.toString());
          LOGGER.info(logInfo + "- SubStatus: " + ussdSession.getSubStatus());
        }
        else {
          outboundRequest.setOutboundUSSDMessage("Exceeded Maximum Retries.");
          outboundRequest.setUssdAction(MT_FIN);
          ussdSession.setRetryCount(0);
          ussdSession.setSubStatus(USSDSubStatus.EXC.toString());
          LOGGER.info(logInfo + "- SubStatus: " + ussdSession.getSubStatus());
        }
      }
      break;
    case TKT: 
      if ("1".equals(inboundUssd.getInboundUSSDMessage()))
      {
        BigDecimal accountBalance = restUtil.getBalance(inboundUssd.getAddress());
        
        if (ticketPrice.compareTo(accountBalance) > 0)
        {
          outboundRequest.setOutboundUSSDMessage("Insufficient balance or credit limit to do ticket purchase");
          ussdSession.setSubStatus(USSDSubStatus.BAL.toString());
          LOGGER.info(logInfo + "- SubStatus: " + ussdSession.getSubStatus());

        }
        else
        {
          Ticket ticket = ticketingService.issueTicket(inboundUssd.getAddress());
          String serverReference = restUtil.doPayment(inboundUssd.getAddress(), ticketPrice, inboundUssd
            .getSessionID(), "Your ticket voucher code is " + ticket.getTicketId() + " Please show this SMS at the entrance");
          

          if ((serverReference != null) && (!serverReference.isEmpty())) {
            ussdSession.setServerReference(serverReference);
            ticket.setServerReference(serverReference);
            ticketingService.updateTicket(ticket);
            
            outboundRequest.setOutboundUSSDMessage("PIN sent. Please dial #7522*411# and select '2. Payment Confirmation' to get your ticket confirmed for Swarnamela 2017!");
            
            ussdSession.setSubStatus(USSDSubStatus.PEN.toString());
            LOGGER.info(logInfo + "- SubStatus: " + ussdSession.getSubStatus());
          } else {
            outboundRequest.setOutboundUSSDMessage("Cannot initiate payment. Please try again later.");
            ussdSession.setSubStatus(USSDSubStatus.PAY.toString());
            LOGGER.info(logInfo + "- SubStatus: " + ussdSession.getSubStatus());
          }
        }
        
        outboundRequest.setUssdAction(MT_FIN);
        responseRequest.setNotifyURL("http://18.218.13.239:8080/ticketing/ussdmenu");
        ussdSession.setRetryCount(0);
      }
      else if ("2".equals(inboundUssd.getInboundUSSDMessage())) {
        outboundRequest.setUssdAction(MO_FIN);
        responseRequest.setNotifyURL("http://18.218.13.239:8080/ticketing/ussdmenu");
        ussdSession.setRetryCount(0);
        ussdSession.setSubStatus(USSDSubStatus.SKI.toString());
        LOGGER.info(logInfo + "- SubStatus: " + ussdSession.getSubStatus());
      }
      else {
        Integer retryCount = ussdSession.getRetryCount();
        
        Integer availableRetries = null;
        if (retryCount != null) {
          availableRetries = retryCount;
        } else {
          availableRetries = retry_count;
        }
        
        availableRetries = availableRetries - 1;
        
        if (availableRetries > 0) {
          outboundRequest.setOutboundUSSDMessage("Invalid Input.\nPlease confirm the purchasing of a ticket for Rs. 150.\n1. Yes\n2. Cancel");
          
          ussdSession.setRetryCount(availableRetries);
          outboundRequest.setUssdAction(MO_CONT);
          ussdSession.setSubStatus(USSDSubStatus.RET.toString());
          LOGGER.info(logInfo + "- SubStatus: " + ussdSession.getSubStatus());
        } else {
          outboundRequest.setOutboundUSSDMessage("Exceeded Maximum Retries");
          ussdSession.setRetryCount(0);
          ussdSession.setSubStatus(USSDSubStatus.EXC.toString());
          outboundRequest.setUssdAction(MT_FIN);
          LOGGER.info(logInfo + "- SubStatus: " + ussdSession.getSubStatus());
        }
        
        responseRequest.setNotifyURL("http://18.218.13.239:8080/ticketing/ussdmenu");
      }
      break;
    case PIN: 
      String pin = inboundUssd.getInboundUSSDMessage();
      
      USSDSession lastUssdSession = ticketingService.getLastPaymentConfirmationPendingRequest(inboundUssd.getAddress());
      
      if ((lastUssdSession != null) && (lastUssdSession.getServerReference() != null))
      {
        String pinSuccess = restUtil.verifyPIN(pin, lastUssdSession.getServerReference());
        
        if ("SUCCESS".equals(pinSuccess))
        {
          Ticket ticket = ticketingService.confirmTicket(lastUssdSession.getServerReference());
         
          outboundRequest.setOutboundUSSDMessage("Your ticket voucher code is " + ticket.getTicketId() + "\n Please show the SMS you recieve for payment confirmation at the entrance");
          
          ussdSession.setSubStatus(USSDSubStatus.CNF.toString());
          outboundRequest.setUssdAction(MT_FIN);
          LOGGER.info(logInfo + "- SubStatus: " + ussdSession.getSubStatus());
          
          lastUssdSession.setSubStatus(USSDSubStatus.CNF.toString());
          ticketingService.updateUSSDSession(lastUssdSession);


        }
        else if ("FAILED".equals(pinSuccess)) {
          outboundRequest.setOutboundUSSDMessage("PIN verification failed. Try with correct pin again");
          ussdSession.setSubStatus(USSDSubStatus.FAIL.toString());
          outboundRequest.setUssdAction(MO_CONT);
          responseRequest.setNotifyURL("http://18.218.13.239:8080/ticketing/ussdmenu");
          LOGGER.info(logInfo + "- SubStatus: " + ussdSession.getSubStatus());
        } else if ("MAX_RETRY".equals(pinSuccess))
        {
          outboundRequest.setOutboundUSSDMessage("PIN verification maximum retry attempts used. Please do new ticket purchase request");
          
          ussdSession.setSubStatus(USSDSubStatus.TERM.toString());
          outboundRequest.setUssdAction(MT_FIN);
          
          lastUssdSession.setSubStatus(USSDSubStatus.REJ.toString());
          ticketingService.updateUSSDSession(lastUssdSession);
          ticketingService.rejectTempTicket(lastUssdSession.getServerReference());
          LOGGER.info(logInfo + "- SubStatus: " + ussdSession.getSubStatus());
        }
      }
      else
      {
        outboundRequest.setOutboundUSSDMessage("No payment verification pending");
        ussdSession.setSubStatus(USSDSubStatus.NO.toString());
        outboundRequest.setUssdAction(MT_FIN);
        LOGGER.info(logInfo + "- SubStatus: " + ussdSession.getSubStatus());
      }
      
      break;
    case INI: 
      if ("1".equals(inboundUssd.getInboundUSSDMessage())) {
        outboundRequest.setOutboundUSSDMessage("Welcome to Swarnamela 2017!\n1. Ticket Purchasing\n2. Payment Confirmation\n3. Event Information");
        
        outboundRequest.setUssdAction(MO_CONT);
        ussdSession.setState(USSDSession.USSDStatus.WEL.toString());
        ussdSession.setSubStatus(USSDSubStatus.INI.toString());
        responseRequest.setNotifyURL("http://18.218.13.239:8080/ticketing/ussdmenu");
        LOGGER.info(logInfo + "- SubStatus: " + ussdSession.getSubStatus());
      } else {
        Integer retryCount = ussdSession.getRetryCount();
        
        Integer availableRetries = null;
        if (retryCount != null) {
          availableRetries = retryCount;
        } else {
        	 availableRetries = retry_count;
        }
        
        availableRetries = availableRetries - 1;
        
        if (availableRetries > 0) {
          outboundRequest.setOutboundUSSDMessage("Invalid Input.\n1. Previous Menu");
          outboundRequest.setUssdAction(MO_CONT);
          responseRequest.setNotifyURL("http://18.218.13.239:8080/ticketing/ussdmenu");
          ussdSession.setRetryCount(availableRetries);
          ussdSession.setSubStatus(USSDSubStatus.RET.toString());
          LOGGER.info(logInfo + "- SubStatus: " + ussdSession.getSubStatus());
        }
        else {
          outboundRequest.setOutboundUSSDMessage("Exceeded Maximum Retries");
          outboundRequest.setUssdAction(MT_FIN);
          ussdSession.setRetryCount(0);
          ussdSession.setSubStatus(USSDSubStatus.EXC.toString());
          LOGGER.info(logInfo + "- SubStatus: " + ussdSession.getSubStatus());
        }
      }
      
      break;
    }
    
    
    ticketingService.updateUSSDSession(ussdSession);
    outboundRequest.setResponseRequest(responseRequest);
    ussdResponse.setOutboundUSSDMessageRequest(outboundRequest);
    
    LOGGER.debug(ussdResponse.toString());
    
    return new ResponseEntity<USSDResponse>(ussdResponse, HttpStatus.OK);
  }
  

  @CrossOrigin
  @GetMapping({"ticket/{id}"})
  public ResponseEntity<Ticket> showTicketStatus(@PathVariable("id") String ticketId)
  {
    if ((ticketId != null) && (!ticketId.isEmpty())) {
      Ticket ticket = ticketingService.getTicketById(ticketId.toUpperCase());
      LOGGER.info("View ticket request for: " + ticketId);
      if ((ticket != null) && ((ticket.getStatus().equals(Ticket.TicketStatus.CNF.toString())) || 
        (ticket.getStatus().equals(Ticket.TicketStatus.USE.toString())))) {
        LOGGER.info("Ticket Info: " + ticket.toString());
        return new ResponseEntity<Ticket>(ticket, HttpStatus.OK);
      }
      LOGGER.info("No ticket found: " + ticketId);
      return new ResponseEntity<Ticket>(new Ticket(), HttpStatus.BAD_REQUEST);
    }
    
    LOGGER.info("Empty or null ticket id :");
    return new ResponseEntity<Ticket>(new Ticket(), HttpStatus.BAD_REQUEST);
  }
  

  @CrossOrigin
  @RequestMapping({"use/{id}"})
  public ResponseEntity<TicketStatus> useTicket(@PathVariable("id") String ticketId)
  {
    LOGGER.info("Use ticket request for: " + ticketId);
    
    if ((ticketId != null) && (!ticketId.isEmpty())) {
      Ticket.TicketStatus ticketStatus = ticketingService.useTicket(ticketId.toUpperCase());
      LOGGER.info("Use ticket response for: " + ticketId + ":" + ticketStatus);
      return new ResponseEntity<TicketStatus>(ticketStatus, HttpStatus.OK);
    }
    
    LOGGER.info("Empty or null ticket id sent for use");
    return new ResponseEntity<TicketStatus>(Ticket.TicketStatus.NO, HttpStatus.OK);
  }
}