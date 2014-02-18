package com.ensibs.omeca.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;

import com.ensibs.omeca.controller.ActionController;
import com.ensibs.omeca.view.CardView;

/**
 * This class creates and manages the slidebar's mini card gallery
 * @author OMECA 2.0 Team (Raphaël GICQUIAUX - Nicolas HALLOUIN - Sylvain RIO - Lindsay ROZIER)
 * 
 */
public class SliderbarCardGallery extends BaseAdapter {
	
	/**
	 * The context
	 */
    private Context mContext;

    /**
     * Constructor
     * @param c the context
     */
    public SliderbarCardGallery(Context c) {
    	this.mContext = c;
    }

    /**
     * Returns the user's number of cards
     */
    public int getCount() {
        return ActionController.user.getCards().size();        
    }

    /**
     * Returns an item, given position
     * @param position the position
     */
    public Object getItem(int position) {
        return position;
    }

    /**
     * Returns an item id, given position
     * @param position the position
     */
    public long getItemId(int position) {
        return position;    
    }
    

    /**
     * Returns a view, given position, convertview and parent
     * @param position the position
     * @param convertView the convertView
     * @param parent the parent
     */
    public View getView(int position, View convertView, ViewGroup parent) {
    	if(parent.getHeight() == 0)
    		notifyDataSetChanged();
    	CardView cv = (CardView)convertView;
    	if(cv == null){
    		cv = new CardView(mContext, ActionController.user.getCards().get(position));
    		int height = (int)(parent.getHeight()*0.9);
    		cv.setLayoutParams(new Gallery.LayoutParams((int)(height/CardView.RATIO), height));
    		cv.setOnTouchListener(null);
    		
    		return cv; 
    	}
    	return cv;
    }
    
}