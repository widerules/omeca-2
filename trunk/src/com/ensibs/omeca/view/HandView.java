package com.ensibs.omeca.view;

import java.util.ArrayList;

import android.content.ClipData;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.DragShadowBuilder;
import android.view.View.OnTouchListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;

import com.digitalaria.gama.carousel.Carousel;
import com.ensibs.omeca.ControllerView;


public class HandView extends Gallery{
	 
	ArrayList<CardView> liste;
	private ImageAdapter adapter;
	Context c;
		
	public HandView(Context context) {
		super(context);	
		c= context;
		init();
		liste = new ArrayList<CardView>();
	}
	private void init()
    {        
        // configurations for the carousel.
        this.setClickable(false);
        this.setSpacing(-30);
   
        // set images for the carousel.
        adapter = new ImageAdapter(c);
        this.setAdapter(adapter);
               
    }

	public HandView(Context context, AttributeSet attrs) {
		super(context, attrs);
		c= context;
		init();
		liste = new ArrayList<CardView>();
	}
	/*public void addCardToHand(CardView cv){
		liste.add(cv);
		this.addView(cv);
	}*/
	
	   public class ImageAdapter extends BaseAdapter {
	    	private Context mContext;
	    	public ImageAdapter(Context c){
	    		mContext = c;
	    	}
	    	@Override
	    	public int getCount() {
	    		  return ControllerView.user.getCards().size();
	    	}
			@Override
			public Object getItem(int position) {
				return null;
			}
			@Override
			public long getItemId(int position) {
				return position;
			}
			
			@Override
			/*public View getView(int position, View convertView, ViewGroup parent) {
				DisplayMetrics metrics = mContext.getApplicationContext().getResources().getDisplayMetrics();
				int width = (int) (metrics.widthPixels/(this.getCount()*0.85));
				int height = (int) (width*CardView.RATIO);
				View view = convertView;
				if (view == null) {
					CardView cv = new CardView(mContext , ControllerView.user.getCards().get(position));
					cv.onHand(true);
					if(!cv.getCard().isFaceUp())
						cv.turnCard();
					view = cv;			
					view.setLayoutParams(new Carousel.LayoutParams(width, height));
				}				
				return view;
			}*/
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
	
}
