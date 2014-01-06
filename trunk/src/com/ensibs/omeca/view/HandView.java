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
	
	public void updateView(){
		this.init();
	}
	private void init(){        
        // configurations for the carousel.
        this.setClickable(false);
        this.setSpacing(1);
   
        // set images for the carousel.
        adapter = new ImageAdapter(c);
        this.setAdapter(adapter);
        this.setUnselectedAlpha((float) 1);
        this.setSelection(ControllerView.user.getNumberOfCards()/2);
        this.setOnDragListener(new HandViewCardGalleryDragListener());
        this.setOnTouchListener(new OnTouchListener() {

        	  @Override
        	  public boolean onTouch(View v, MotionEvent event) {

        	     return true;
        	  }
        	 });

	}
	

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return super.onScroll(e1, e2, 0, 0);
	}
	public HandView(Context context, AttributeSet attrs) {
		super(context, attrs);
		c= context;
		init();
		liste = new ArrayList<CardView>();
	}
	
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
			} */
			public View getView(int position, View convertView, ViewGroup parent) {
		    	if(parent.getHeight() == 0)
		    		notifyDataSetChanged();
		    	CardView cv = (CardView)convertView;
		    	if(cv == null){
		    		DisplayMetrics metrics = mContext.getApplicationContext().getResources().getDisplayMetrics();
		    		cv = new CardView(mContext, ControllerView.user.getCards().get(position));
		    		int width = (int)(metrics.widthPixels/(this.getCount()*1.1));
		    		int height = (int)(width*CardView.RATIO);
		    		cv.setLayoutParams(new Gallery.LayoutParams( width, height));
		    		cv.setOnTouchListener(null);
		    		if(!cv.getCard().isFaceUp())
		    			cv.turnCard();
		    		cv.onHand(true);
		    		return cv; 
		    	}
		    	return cv;
		    }
	    }
	
}
