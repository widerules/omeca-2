package com.ensibs.omeca.wifidirect;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;

public class WifiDirectDiscoverySelectDialog extends AlertDialog.Builder{
	
	private WifiP2pDeviceList peers;
	private WifiDirectManager manager;

	public WifiDirectDiscoverySelectDialog(Context context, WifiDirectManager manager, WifiP2pDeviceList peers) {
		super(context);
		this.create();
		this.peers = peers;
		this.manager = manager;
		this.setTitle("Choisir l'hebergeur");
		this.setList();
	}
	
	private void setList(){
		this.setItems(peersToCharSequence(), new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
		    	manager.connectTo((WifiP2pDevice) peers.getDeviceList().toArray()[which]);
		    }});
	}
	
	private CharSequence[] peersToCharSequence(){
		CharSequence[] ret = new CharSequence[peers.getDeviceList().size()];
		int i = 0;
		for(WifiP2pDevice device :peers.getDeviceList() ){
			ret[i] = device.deviceName+" : "+device.deviceAddress;
			i++;
		}
		return ret;
	}
}
