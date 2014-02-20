package com.ensibs.omeca.wifidirect;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;

import com.ensibs.omeca.R;
import com.ensibs.omeca.wifidirect.event.WifiDirectEvent;
import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;

/**
 * Popup in which we can choose a wifi direct device for connection
 */
public class WifiDirectPickPeersDialog extends AlertDialog.Builder{
	
	/**
	 * Interface of wifi direct api
	 */
	private WifiDirectManager manager = null;
	/**
	 * List of wifi direct device
	 */
	private WifiP2pDeviceList peers = null;

	/**
	 * Constructor
	 * @param context activity context
	 * @param manager wifi direct api manager
	 * @param peers list of device available
	 */
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

	/**
	 * Return tab of char device name from the list
	 * @param peers device list
	 * @return tab of device name
	 */
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
