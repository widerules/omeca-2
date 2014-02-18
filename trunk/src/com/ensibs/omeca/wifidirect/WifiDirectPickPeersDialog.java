package com.ensibs.omeca.wifidirect;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;

import com.ensibs.omeca.R;
import com.ensibs.omeca.wifidirect.event.WifiDirectEvent;
import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;

public class WifiDirectPickPeersDialog extends AlertDialog.Builder{
	
	private WifiDirectManager manager = null;
	private WifiP2pDeviceList peers = null;

	public WifiDirectPickPeersDialog(Context context, WifiDirectManager manager, WifiP2pDeviceList peers){
		super(context);
		this.manager = manager;
		this.peers = peers;
		this.create();
		this.setTitle(context.getString(R.string.choose_host));
		this.setCancelable(false);
		this.setNegativeButton(context.getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				WifiDirectPickPeersDialog.this.manager.cancelConnection();
				dialog.cancel();
			}
		});
		this.setItems(peersToCharSequence(peers), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				WifiDirectPickPeersDialog.this.manager.getNotificationCenter().notifyManager(new WifiDirectEventImpl(WifiDirectEvent.CONNECTION,(WifiP2pDevice)(WifiDirectPickPeersDialog.this.peers.getDeviceList().toArray()[which])));
			}});
	}

	private CharSequence[] peersToCharSequence(WifiP2pDeviceList peers){
		CharSequence[] ret = new CharSequence[peers.getDeviceList().size()];
		int i = 0;
		for(WifiP2pDevice device :peers.getDeviceList() ){
			ret[i] = device.deviceName+" : "+device.deviceAddress;
			i++;
		}
		return ret;
	}
}
