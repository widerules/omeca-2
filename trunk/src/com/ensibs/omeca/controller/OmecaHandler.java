package com.ensibs.omeca.controller;

import com.ensibs.omeca.GameActivity;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

public class OmecaHandler extends Handler{
	
	public static final int CONNEXION = 0;
	public static final int DECONNEXION = 1;
	
	public OmecaHandler(Looper looper){
		super(looper);
	}
	
	@Override
	public void handleMessage(Message msg) {
		if(msg.what == DECONNEXION){
			Toast.makeText(GameActivity.getActivity(), "Deconnexion", Toast.LENGTH_LONG).show();
		}
	}
}
