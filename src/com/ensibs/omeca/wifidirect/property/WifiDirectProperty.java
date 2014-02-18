package com.ensibs.omeca.wifidirect.property;

/**
 * Class with all properties related to the wifi direct
 */
public class WifiDirectProperty {

	//Tag for log
	public final static String TAG = "WifiDirect-OMECA2";
	//Port of the application
	public final static int PORT = 8888;
	//Config value for host group owner
	public final static int HOST_GO = 15;
	//Config value for client group owner
	public final static int CLIENT_GO = 0;	
	//Time for reset framework after connection try
	public final static int TIMER = 15000;
}
