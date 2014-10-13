/*******************************************************************************
 * Copyright (c) 2013-2014 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Vijay Subramanian
 * Raghuram Sudhaakar
 * Peter Tran
 ******************************************************************************/
package dmo.client.demo;

import java.util.logging.Logger;
import java.io.IOException;
import java.net.URISyntaxException;

import dmo.client.api.API;

/** 
 *	@file getrule.java
 *	@brief This is a sample standalone getrule demonstration application to illustrate
 * 	the DMo RESTful API. The demo will first create a new rule called "getrule",
 *	register an event (e.g. GetPayload=5), filter on TCP network packets for certain 
 *	HTTP headers (e.g. Content-Type=application/jscon) and then perform a HTTP WebDAV 
 *	PROPFIND operation/method to query for all the properties for the existing "getrule" 
 *	created previously using the default user context "DMRootCtx" (e.g. login username 
 *	and password) using JSON format payload and transmitted the encapsulated request over 
 *	standard HTTPS secure web protocol within the network to register an event action and rule.
 * 	If successful, the PROPFIND request will return a HTTP "207 Multi-Status" response status
 *	code along with XML body response on the property names and associated values. It will
 *	then perform a HTTP WebDAV DELETE operation/method to remove the rule called "getrule"
 *	that was created previously.
 *
 * 	@version 1.0
 *	@bug No known bugs.
 * 	@support Questions? Send email to: dmo-eng@external.cisco.com
 *	@copyright Cisco Systems, Inc. 2013-2014
 */
public class getrule {

	private final Logger logger; 						/* Logger instance */
	private static final int MAX_CMD_LINE_ARGS = 7;		/* Constant for maximum command line arguments */
	private int enableSSL = 1;							/* Enables SSL connection */
	
	/**
	 *	@brief getrule constructor initializes the default parameters.
	 */
	public getrule() throws IOException, URISyntaxException {
		
		String srcIPAddr = "192.168.1.1";
		String srcPort = "443";
		String destIPAddr = "192.168.1.3";
		String destPort = "3000";
		String contextUser = "iox_dmo";
		String contextPasswd = "iox_dmo";
		String javaKeyStore = "/Users/apple/dmokeystore.jks"; 
		
		/* Initialize handle reference */
		int h = 0;
		
		/* Retrieves instance of Logger */
		logger = Logger.getLogger(this.getClass().getName());
		
		/* Gets instance of the DMo client API library */
		API api = API.getInstance();
		
		/* Initializes the DMo client API library */
		api.initAPI();

		/* Create a new rule and get a reference to it */
		h = api.createRequest();
		
		/* This dictates where this rule is to be sent for registration. */
		api.setRegisterParams(h, srcIPAddr, srcPort);

		/* Checks whether the request handle reference is valid or not */
		if (h == 0) {
			logger.severe("Failed to create request\n");
			System.exit(-1);
		}
		
		/* Gives login credentials for particular user context (e.g. username and password) */
		api.setLogin(h, contextUser, contextPasswd);

		/* Sets the cache size to 1024 bytes and timer value to 3 secs. or 3000 millsecs. */
		/* api.setMeta(h, 3000, 1024); */

		/* 
	 	 * 	Specifies whether we want to operate in server or client mode. In server
	 	 * 	mode, we specify an endpoint (e.g. URI destination) in the response block 
	 	 *	where the results are to be sent. In client mode, we use GET operations to
	 	 *	get the results at a time of our choosing.
	 	 * 
	 	 * 	In this example, define an event action where "GetPayload=5".
	 	 */
		api.setAction(h, "event", "GetPayload", "2000");

		/* 
	 	 * 	Specifies the target endpoint with destination IP address and port. 
	 	 *	Only either HTTP or FIFO protocol is currently supported.
	 	 */
		
		
		
		api.setActionEndpoint(h, "http", destIPAddr, destPort, "/");
		
		 /* 
	 	  *	This dictates which traffic to inspect. In this example, filter on tcp
	 	  *	packets and we do not match the destination address, destination port, 
	 	  *	source address and source port are ignored by default. 
     	  */
//		api.setNetFilter(h, "tcp", "", srcPort, "", "");
	
	    /*	
     	 * 	This dictates that we are interested only in HTTP traffic with the 
     	 * 	following content type, content length, host parameters, etc.
     	 * 	Need to set the application protocol (e.g. http) first. 
     	 */
		api.setApplicationProtocol(h, "http");
		api.addApplicationFilter(h, "Content-Type", "application/json");
		//api.addApplicationFilter(h, "Host:", "sensor-device.com");
		
		/* Sets the Java SSL keystore */
		api.setSSLKeyStore(javaKeyStore);
		
		api.findContent(h, "ANALOG.B < " + Integer.toString(celsiusToAnalog(20)));
		
		/*
	 	 *	Adds a new rule called "getrule".
	 	 */
		addRule(api, h);
		
		/* Sleeps for 1 sec. */
//		try { 
//			Thread.sleep(1000L);
//		} catch (Exception e) {
//			logger.severe("Main thread can't sleep.\n");
//			e.printStackTrace();
//		}
		
		/*
	 	 *	Performs WebDAV PROPFIND method to query for "getrule" rule.
	 	 */
//		findRule(api, h);
//
//		/* Sleeps for 1 sec. */
//		try { 
//			Thread.sleep(1000L);
//		} catch (Exception e) {
//			logger.severe("Main thread can't sleep.\n");
//			e.printStackTrace();
//		}

		/*
         *	Deletes an existing rule called "getrule".
         */		
//		deleteRule(api, h);
		
		
	}

	/**
	 *	@brief The method will perform a HTTP WebDAV DELETE operation on the rule name
	 * 	identified as "sensordemo" to delete the rule from the server. 
	 * 	The HTTP server response code should be "204 No Content".
	 *
	 *	@param api The {@link API} instance object to set the WebDAV method operation, 
	 *	rule name identifier and enable SSL connection.
	 *	@param h The handle reference given during the client API initialization.
	 *
	 *	@return Always succeeds and returns true in this case.
	 */
	private boolean deleteRule(API api, int h) throws IOException, URISyntaxException {
		api.setOperation(h, "DELRULE");
		api.setRuleId(h,"getrule");
		api.registerRequest(h, enableSSL);
		return true;
	}

	/**
	 *	@brief The method will perform a HTTP WebDAV PUT operation on the rule name
	 * 	identified as "sensordemo" to add a new rule onto the server. 
	 *	The HTTP server response code should be "201 Created".
	 *
	 *	@param api The {@link API} instance object to set the WebDAV method operation, 
	 *	rule name identifier and enable SSL connection.
	 *	@param h The handle reference given during the client API initialization.
	 *
	 *	@return Always succeeds and returns true in this case.
	 */
	private boolean addRule(API api, int h) throws IOException, URISyntaxException {
		api.setOperation(h, "ADDRULE");
		api.setRuleId(h,"getrule");
		api.registerRequest(h, enableSSL);
		return true;

	}
	
	private int celsiusToAnalog(int temp) {
		return (int)Math.round((temp + 61.111)/.2222);
	}
	
	/**
	 *	@brief The method will perform a HTTP WebDAV PROPFIND operation on the rule name
	 * 	identified as "sensordemo" to query to all the rule properties. 
	 *	The HTTP server response code should be "207 Multi-Status" with XML response content
	 * 	to list the properties.
	 *
	 *	@param api The {@link API} instance object to set the WebDAV method operation, 
	 *	rule name identifier and enable SSL connection.
	 *	@param h The handle reference given during the client API initialization.
	 *
	 *	@return Always succeeds and returns true in this case.
	 */
	private boolean findRule(API api, int h) throws IOException, URISyntaxException {
		api.setOperation(h, "PROPFIND");
		api.setRuleId(h,"getrule");
		api.registerRequest(h, enableSSL);
		return true;
	}
	
	/**
 	 *	@brief The main function to execute the test rule demo application.
 	 *
 	 *	@return Returns and exits 0 when successful and nonzero when unsuccessful.
 	 */
	public static void main(String[] args) {
		try {
      		new getrule();
      	} catch (Exception ex) {
			ex.printStackTrace();
      	}	
	}
}
