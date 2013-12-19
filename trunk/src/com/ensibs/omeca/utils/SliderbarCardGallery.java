package com.ensibs.omeca.utils;

import com.ensibs.omeca.ControllerView;
import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.R;
import com.ensibs.omeca.view.CardView;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * @author S�bastien Bat�zat <sebastien.batezat@gmail.com>
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

        CardView cv = new CardView(mContext, ControllerView.user.getCards().get(position));
        View tmp = GameActivity.getActivity().findViewById(R.id.playerview_slidebar_board);
        Log.i("height", ""+(tmp==null));
	        int height = (int)(tmp.getHeight()*0.9);
	        Log.i("height", ""+height);
	        cv.setLayoutParams(new Gallery.LayoutParams((int)(height/CardView.RATIO), height));
	        cv.setOnTouchListener(null);
	        cv.turnCard();
	        return cv; 
    
    }
    
}