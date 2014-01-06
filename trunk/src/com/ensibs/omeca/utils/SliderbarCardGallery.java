package com.ensibs.omeca.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnDragListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;

import com.ensibs.omeca.ControllerView;
import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.R;
import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.view.CardView;

/**
 * @author Raphael GICQUIAUX
 */
public class SliderbarCardGallery extends BaseAdapter {
	
    private Context mContext;

    public SliderbarCardGallery(Context c) {
    	this.mContext = c;
    }

    public int getCount() {
        return ControllerView.user.getCards().size();        
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;    
    }
    

    public View getView(int position, View convertView, ViewGroup parent) {
    	if(parent.getHeight() == 0)
    		notifyDataSetChanged();
    	CardView cv = (CardView)convertView;
    	if(cv == null){
    		cv = new CardView(mContext, ControllerView.user.getCards().get(position));
    		int height = (int)(parent.getHeight()*0.9);
    		cv.setLayoutParams(new Gallery.LayoutParams((int)(height/CardView.RATIO), height));
    		cv.setOnTouchListener(null);
    		if(!cv.getCard().isFaceUp())
    			cv.turnCard();
    		return cv; 
    	}
    	return cv;
    }
    
}