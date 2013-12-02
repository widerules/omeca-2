package com.ensibs.omeca.utils;

import com.ensibs.omeca.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

/**
 * @author Sébastien Batézat <sebastien.batezat@gmail.com>
 */
public class AvatarGallery extends BaseAdapter {
	
    private Context mContext;

    public AvatarGallery(Context c) {
    	
    	this.mContext = c;
    
    }

    public int getCount() {
    	
        return AvatarsList.getLength();
        
    }

    public Object getItem(int position) {
    	
        return position;
    
    }

    public long getItemId(int position) {
    	
        return position;
        
    }
    

    public View getView(int position, View convertView, ViewGroup parent) {
    	
        ImageView imageView = new ImageView(this.mContext);
        imageView.setImageResource(AvatarsList.get(position));
        imageView.setLayoutParams(new Gallery.LayoutParams(
    		this.mContext.getResources().getDimensionPixelSize(R.dimen.avatar_width),
    		this.mContext.getResources().getDimensionPixelSize(R.dimen.avatar_height))
        );
        //imageView.setBackgroundResource(R.drawable.avatar);
        
        return imageView;
    
    }
    
}