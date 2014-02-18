package com.ensibs.omeca.utils;

import com.ensibs.omeca.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

/**
 * This class deals with the AvatarGallery functionality
 * @author OMECA 2.0 Team (Raphaël GICQUIAUX - Nicolas HALLOUIN - Sylvain RIO - Lindsay ROZIER)
 * 
 */
public class AvatarGallery extends BaseAdapter {
	
	/**
	 * The context
	 */
    private Context mContext;

    /**
     * Constructor of the AvatarGallery
     * @param c the context
     */
    public AvatarGallery(Context c) {    	
    	this.mContext = c;
    
    }

    /**
     * Returns the number of available avatars
     */
    public int getCount() {    	
        return AvatarsList.getLength();
        
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
     * Returns a view, given position, convertView and parent
     * @param position the position
     * @param convertView the convertView
     * @param parent the parent
     */
    public View getView(int position, View convertView, ViewGroup parent) {
    	ImageView imageView = (ImageView)convertView;
    	if(convertView == null){
    		imageView = new ImageView(this.mContext);
    		imageView.setImageResource(AvatarsList.get(position));
    		imageView.setLayoutParams(new Gallery.LayoutParams(
    	    		this.mContext.getResources().getDimensionPixelSize(R.dimen.avatar_width),
    	    		this.mContext.getResources().getDimensionPixelSize(R.dimen.avatar_height))
    	        );
    		imageView.setBackgroundResource(R.drawable.avatar);
    	}

        
        return imageView;
    
    }
    
}