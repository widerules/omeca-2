package com.ensibs.omeca.wifidirect;

import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.util.Log;

import com.ensibs.omeca.wifidirect.event.WifiDirectEvent;
import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;
import com.ensibs.omeca.wifidirect.exchange.WifiDirectExchangeClient;
import com.ensibs.omeca.wifidirect.exchange.WifiDirectExchangeServer;
import com.ensibs.omeca.wifidirect.exchange.WifiDirectIExchange;
import com.ensibs.omeca.wifidirect.mod.WifiDirectMod;
import com.ensibs.omeca.wifidirect.property.WifiDirectProperty;
import com.ensibs.omeca.wifidirect.status.WifiDirectStatus;

/**
 * 
 *
 */
public class WifiDirectManager extends Observable implements Observer{
	//TODO methode deco client
	//Method send msg if host if client
	private WifiDirectMod mod = null;
	private WifiDirectNotificationCenter notificationCenter = null;
	private WifiP2pManager wifiP2pManager = null;
	private Channel wifiP2PChannel = null;
	private static Context applicationContext = null;
	private P2PActionListener actionListener = null;
	private P2PChannelListener channelListener = null;
	private P2PConnectionListener connectionListener = null;
	private P2PDiscoveryReceiver discoveryReceiver = null;
	private P2PStatusReceiver statusReceiver = null;
	private WifiDirectStatus status;
	private boolean isOnConnection = false;
	private WifiDirectIExchange wifiDirectIExchange = null;
	private boolean isDiscoveryRegistred = false;

	/**
	 * 
	 * @param applicationContext
	 */
	public WifiDirectManager(Context applicationContext){
		WifiDirectManager.applicationContext  = applicationContext;
		this.status = WifiDirectStatus.DISCONNECTED;
		this.initNotificator();
		this.initP2P();
	}
	
	/**
	 * 
	 * @param applicationContext
	 */
	public static void setApplicationContext(Context applicationContext) {
		WifiDirectManager.applicationContext = applicationContext;
	}

	/**
	 * 
	 */
	private void initNotificator(){
		this.notificationCenter = new WifiDirectNotificationCenter();
		this.notificationCenter.addObserver(this);
	}

	/**
	 * 
	 */
	private void initP2P(){
		this.wifiP2pManager = (WifiP2pManager)applicationContext.getSystemService(Context.WIFI_P2P_SERVICE);
		this.actionListener = new P2PActionListener(getNotificationCenter());
		this.channelListener = new P2PChannelListener(getNotificationCenter());
		this.connectionListener = new P2PConnectionListener(getNotificationCenter());
		this.discoveryReceiver = new P2PDiscoveryReceiver(getNotificationCenter());
		this.statusReceiver = new P2PStatusReceiver(getNotificationCenter());
		this.wifiP2PChannel = this.wifiP2pManager.initialize(applicationContext, applicationContext.getMainLooper(), channelListener); 
		this.registred();
	}

	/**
	 * 
	 */
	private void registred(){
		WifiDirectManager.applicationContext.registerReceiver(statusReceiver, new IntentFilter(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION));
		WifiDirectManager.applicationContext.registerReceiver(connectionListener, new IntentFilter(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION));
	}

	/**
	 * 
	 */
	private void unregistred(){
		WifiDirectManager.applicationContext.unregisterReceiver(statusReceiver);
		WifiDirectManager.applicationContext.unregisterReceiver(connectionListener);
	}
	
	/**
	 * 
	 */
	public void startVisible(){
		this.wifiP2pManager.discoverPeers(wifiP2PChannel, actionListener);
	}

	/**
	 * 
	 */
	public void startDiscoverPeers(){
		WifiDirectManager.applicationContext.registerReceiver(discoveryReceiver, new IntentFilter(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION));
		this.isDiscoveryRegistred = true;
	}

	/**
	 * 
	 */
	public void stopDiscoverPeers(){
		WifiDirectManager.applicationContext.unregisterReceiver(discoveryReceiver);
		this.isDiscoveryRegistred = false;
	} 

	/**
	 * 
	 * @param peers
	 */
	private void showConnectionDialog(WifiP2pDeviceList peers){
		WifiDirectPickPeersDialog dialog = new WifiDirectPickPeersDialog(applicationContext, this, peers);
		dialog.show();
	}

	/**
	 * 
	 */
	public void cancelConnection(){
		this.isOnConnection = false;
	}
	
	/**
	 * 
	 * @param device
	 */
	public void connectTo(WifiP2pDevice device) {
		WifiP2pConfig config = new WifiP2pConfig();
		config.deviceAddress = device.deviceAddress;
		config.groupOwnerIntent = WifiDirectProperty.CLIENT_GO;
		wifiP2pManager.connect(this.wifiP2PChannel, config, actionListener);
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				Log.i("P2PTest", "Timer start");
				if(status == WifiDirectStatus.DISCONNECTED){
					disconnect();
					Log.i("P2PTest", "Cancel connection");
				}
			}
		}, new Date((new Date()).getTime()+15000));
	}
	
	public void disconnect(){
		this.wifiP2pManager.cancelConnect(wifiP2PChannel, actionListener);
		this.status = WifiDirectStatus.DISCONNECTED;
		this.cancelConnection();
	}

	/**
	 * 
	 * @return
	 */
	public WifiDirectNotificationCenter getNotificationCenter() {
		return this.notificationCenter;
	}

	/**
	 * 
	 * @param mod
	 */
	public void setMode(WifiDirectMod mod){
		this.mod = mod;
	}

	/**
	 * 
	 * @return
	 */
	public WifiDirectMod getMod(){
		return this.mod;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isDiscoveryRegistred() {
		return isDiscoveryRegistred;
	}

	/**
	 * 
	 * @return
	 */
	public static Context getApplicationContext(){
		return WifiDirectManager.applicationContext;
	}

	/**
	 * 
	 * @return
	 */
	public WifiDirectStatus getStatus() {
		return this.status;
	}
	
	/**
	 * 
	 * @param event
	 */
	public void sendEvent(WifiDirectEventImpl event){
		this.wifiDirectIExchange.sendEvent(event);
	}

	/**
	 * 
	 */
	@Override
	public synchronized void update(Observable source, Object event) {
		WifiDirectEventImpl p2pEvent = (WifiDirectEventImpl) event;
		if(p2pEvent.getEvent() == WifiDirectEvent.BUSY){
			//TODO No wifi !!!! ou framework en attente
			setChanged();
			notifyObservers(p2pEvent);
		}else if(p2pEvent.getEvent() == WifiDirectEvent.CHANNEL_LOST){
			//TODO Later
		}else if(p2pEvent.getEvent() == WifiDirectEvent.CONNECTED){
			cancelConnection();
			this.status = WifiDirectStatus.CONNECTED;
			//Request info
			if(this.mod == WifiDirectMod.HOST){
				this.wifiDirectIExchange = new WifiDirectExchangeServer(notificationCenter);
				this.wifiDirectIExchange.startExchange();
			}else{
				this.wifiP2pManager.requestConnectionInfo(wifiP2PChannel, new ConnectionInfoListener() {
						public void onConnectionInfoAvailable(WifiP2pInfo info) {
							// If the connection is established
							if (info.groupFormed) {
								wifiDirectIExchange = new WifiDirectExchangeClient(notificationCenter,info.groupOwnerAddress.getHostAddress());
								wifiDirectIExchange.startExchange();
							}
						}
					});
				setChanged();
				notifyObservers(p2pEvent);
			}
		}else if(p2pEvent.getEvent() == WifiDirectEvent.CONNECTION){
			//this.stopDiscoverPeers();
			this.connectTo((WifiP2pDevice) p2pEvent.getData());
		}else if(p2pEvent.getEvent() == WifiDirectEvent.DISCONNECTED){
			//TODO close socket
			this.status = WifiDirectStatus.DISCONNECTED;
		}else if(p2pEvent.getEvent() == WifiDirectEvent.ENABLED){
			//TODO Nothing later
		}else if(p2pEvent.getEvent() == WifiDirectEvent.ERROR){
			//Resends event or not ? Close game ?
		}else if(p2pEvent.getEvent() == WifiDirectEvent.EVENT){
			setChanged();
			notifyObservers(p2pEvent);
			//Resend if host
			if(this.mod == WifiDirectMod.HOST){
				
			}
		}else if(p2pEvent.getEvent() == WifiDirectEvent.RECEIVED_P2P_LIST){
			this.wifiP2pManager.requestPeers(wifiP2PChannel, new PeerListListener() {
				public void onPeersAvailable(WifiP2pDeviceList peers) {
					if(mod == WifiDirectMod.CLIENT && status == WifiDirectStatus.DISCONNECTED && !isOnConnection){
						isOnConnection = true;
						showConnectionDialog(peers);
					}
				}
			});
			this.stopDiscoverPeers();
		}else if(p2pEvent.getEvent() == WifiDirectEvent.UNSUPPORTED){
			//TODO P2P not supported
			setChanged();
			notifyObservers(p2pEvent);
		}
	}
}
