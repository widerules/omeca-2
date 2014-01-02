package com.ensibs.omeca.wifidirect;

import java.util.Observable;
import java.util.Observer;

import com.ensibs.omeca.wifidirect.event.ConnectionWifiDirectEvent;
import com.ensibs.omeca.wifidirect.event.WifiDirectEvent;
import com.ensibs.omeca.wifidirect.exchange.WifiDirectClientExchange;
import com.ensibs.omeca.wifidirect.exchange.WifiDirectIExchange;
import com.ensibs.omeca.wifidirect.exchange.WifiDirectServerExchange;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;

//TODO : meilleur mode, deconnexion, continuer event, mettre en place echange, correction bug + stabilite

public class WifiDirectManager extends Observable implements Observer {
	
	//Wifi direct system service
	private WifiP2pManager wifiP2pManager; 
	private Channel wifiDirectChannel;
	//Activity context
	private static Context applicationContext;
	//Different listener
	private WifiDirectChannelListener channelListener;
	private WifiDirectManagerActionListener actionListener;
	private WifiDirectManagerStatusReceiver statusReceiver;
	private WifiDirectDiscoveryReceiver discoveryReceiver;
	private WifiDirectConnectionListener connectionListener;
	private WifiDirectDeviceChanged deviceChanged;
	//Intent filter for broadcast receiver
	IntentFilter peerfilter;
	IntentFilter connectionfilter;
	IntentFilter p2pEnabled;
	IntentFilter ownDeviceFilter;
	//Attribut for host or client
	private boolean isHost;
	private boolean connected = false;
	//Exchange class
	WifiDirectIExchange exchangeClass;
	
	public WifiDirectManager(Context ctx){
		//set context of the application for get system service
		applicationContext = ctx;
		//init different listener
		channelListener = new WifiDirectChannelListener(this);
		actionListener = new WifiDirectManagerActionListener();
		statusReceiver = new WifiDirectManagerStatusReceiver();
		discoveryReceiver = new WifiDirectDiscoveryReceiver(this);
		connectionListener = new WifiDirectConnectionListener(this);
		deviceChanged = new WifiDirectDeviceChanged();
		//create intent filter
		peerfilter = new IntentFilter(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
	    connectionfilter = new IntentFilter(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
	    p2pEnabled = new IntentFilter(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
	    ownDeviceFilter = new IntentFilter(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
		//enable wifi on the device
		this.enableWifi();
		this.initializeWiFiDirect();
	}
	
	public void initializeWiFiDirect() {
		//Get system service wifi direct by the constant 
		wifiP2pManager = (WifiP2pManager)applicationContext.getSystemService(Context.WIFI_P2P_SERVICE);
		//Create channel with context, context main loop and channellistener
		wifiDirectChannel = wifiP2pManager.initialize(applicationContext, applicationContext.getMainLooper(), channelListener); 
		//Enabled wifi direct intent
		this.registred();
	}
	
	public void removeWifiDirect(){
		//this.disableWifi();
		this.unregistred();
	}
	
	//Active wifi on device
	private void enableWifi(){
		WifiManager wifiManager = (WifiManager) applicationContext.getSystemService(Context.WIFI_SERVICE);
		if(!wifiManager.isWifiEnabled()){
			wifiManager.setWifiEnabled(true);
		}
	}
	
	//Disable wifi on device
	private void disableWifi(){
		WifiManager wifiManager = (WifiManager) applicationContext.getSystemService(Context.WIFI_SERVICE);
		if(wifiManager.isWifiEnabled()){
			wifiManager.setWifiEnabled(false);
		}
	}
	
	public void discoverPeers() {
		wifiP2pManager.discoverPeers(wifiDirectChannel, actionListener);
	}
	
	//TODO : unregistred service better
	@SuppressLint("NewApi")
	public void stopDiscoverPeers(){
		//((WifiP2pManager) wifiP2pManager).stopPeerDiscovery(wifiDirectChannel, actionListener);
	}
	
	public synchronized void connectTo(WifiP2pDevice device) {
		WifiP2pConfig config = new WifiP2pConfig();
		config.deviceAddress = device.deviceAddress;
		config.groupOwnerIntent = 0;
		wifiP2pManager.connect(wifiDirectChannel, config, actionListener);
		this.connected = true;
	}
	
	public synchronized void discoveringDialog(WifiP2pDeviceList peers){
		if(!connected){
			WifiDirectDiscoverySelectDialog dialog = new WifiDirectDiscoverySelectDialog(applicationContext, this, peers);
			dialog.show();
		}
	}
	
	public void unregistred(){
		applicationContext.unregisterReceiver(discoveryReceiver);
		applicationContext.unregisterReceiver(connectionListener);
		applicationContext.unregisterReceiver(statusReceiver);
		applicationContext.unregisterReceiver(deviceChanged);
	}
	
	public void registred(){
		applicationContext.registerReceiver(discoveryReceiver, peerfilter);
		applicationContext.registerReceiver(connectionListener, connectionfilter);
		applicationContext.registerReceiver(statusReceiver, p2pEnabled);
		applicationContext.registerReceiver(deviceChanged, ownDeviceFilter);
	}
	
	public void createExchange(String ip){
		if(isHost){
			exchangeClass = new WifiDirectServerExchange();
		}else{
			exchangeClass = new WifiDirectClientExchange(ip);
		}
		exchangeClass.addObserver(this);
		exchangeClass.startExchange();
	}

	public WifiP2pManager getWifiP2pManager() {
		return wifiP2pManager;
	}

	public Channel getWifiDirectChannel() {
		return wifiDirectChannel;
	}

	public static Context getApplicationContext() {
		return applicationContext;
	}	
	
	public void setRole(boolean isHost){
		this.isHost = isHost;
	}

	public boolean isHost() {
		return isHost;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public boolean isConnected() {
		return connected;
	}
	
	public void notifyConnection(){
		  setChanged();
	      notifyObservers(new ConnectionWifiDirectEvent());
	}
	
	public void notifyEvent(WifiDirectEvent event){
		  setChanged();
	      notifyObservers(event);
	}
	
	public void sendEvent(WifiDirectEvent event){
		exchangeClass.sendEvent(event);
	}

	@Override
	public void update(Observable observable, Object data) {
		notifyEvent((WifiDirectEvent) data);
	}
}
