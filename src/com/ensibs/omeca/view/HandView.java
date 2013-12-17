package com.ensibs.omeca.view;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TableLayout;
import android.widget.TableRow;

public class HandView extends TableLayout {

	ArrayList<CardView> liste;
	TableRow tr1, tr2, tr3;
	
	public HandView(Context context) {
		super(context);	
		tr1 = new TableRow(context);
		liste = new ArrayList<CardView>();
	
	}

	public HandView(Context context, AttributeSet attrs) {
		super(context, attrs);
		tr1 = new TableRow(context);
		liste = new ArrayList<CardView>();
	}
	
	public void addCardToHand(CardView cv){
		liste.add(cv);
		tr1.addView(cv);
		this.addView(tr1);
		//this.addView(cv);
	}
	

}
