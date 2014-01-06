package com.ensibs.omeca.view;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.digitalaria.gama.carousel.Carousel;
import com.ensibs.omeca.ControllerView;


public class HandView extends Carousel{
	 
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
        this.setType(Carousel.TYPE_COVERFLOW);
        this.setOverScrollBounceEnabled(true);
        this.setInfiniteScrollEnabled(false);
        this.setItemRearrangeEnabled(false);
        this.setClickable(false);
        this.setSpacing(-30);

        
        // set images for the carousel.
        adapter = new ImageAdapter(c);
        this.setAdapter(adapter);
        
        // change the first selected position.
        this.setCenterPosition(ControllerView.user.getNumberOfCards()/2 +1);
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
			public View getView(int position, View convertView, ViewGroup parent) {
				DisplayMetrics metrics = mContext.getApplicationContext().getResources().getDisplayMetrics();
				int width = (int) (metrics.widthPixels/(this.getCount()*0.85));
				int height = (int) (width*CardView.RATIO);
				View view = convertView;
				if (position< 0)
					position = 0;
				if (view == null) {
					view = new CardView(mContext , ControllerView.user.getCards().get(position));				
					view.setLayoutParams(new Carousel.LayoutParams(width, height));
				}				
				return view;
			}
	    }
	
}
