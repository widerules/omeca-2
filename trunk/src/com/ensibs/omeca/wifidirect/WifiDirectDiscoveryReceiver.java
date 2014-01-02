package com.ensibs.omeca.wifidirect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.widget.Toast;

public class WifiDirectDiscoveryReceiver extends BroadcastReceiver{
	
	private WifiDirectManager manager;
	
	public WifiDirectDiscoveryReceiver(WifiDirectManager manager){
		this.manager = manager;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("Peer discovery");
		manager.getWifiP2pManager().requestPeers(manager.getWifiDirectChannel(), 
		        new PeerListListener() {
		          public void onPeersAvailable(WifiP2pDeviceList peers) {
		        	  //System.out.println("Nb devices for P2P : "+peers.getDeviceList().size());
		        	  //Toast.makeText(manager.getApplicationContext(), "Peers trouve !!!", Toast.LENGTH_LONG).show();
		        	  if(!manager.isHost() & !manager.isConnected())
		        		  manager.discoveringDialog(peers);
		          }
		        });
	}
}
