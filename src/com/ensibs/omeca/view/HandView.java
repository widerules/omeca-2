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
import com.ensibs.omeca.model.entities.Card;


public class HandView extends Gallery{
	 
	ArrayList<Card> liste;
	private HandCardsAdapter adapter;
	Context c;
		
	public HandView(Context context) {
		super(context);	
		c= context;
		init();
		liste = new ArrayList<Card>();
		this.orderCard();
		init();
	}
	public void orderCard(){
		liste = ControllerView.user.getCards();
		ArrayList<Card> lOfdiamonds = new ArrayList<Card>() ;
		ArrayList<Card> lOfspades = new ArrayList<Card>();
		ArrayList<Card> lOfhearts = new ArrayList<Card>();
		ArrayList<Card> lOfclubs = new ArrayList<Card>();
		for( int i = 0; ControllerView.user.getCards().size()>i ; i++ ){
			if(liste.get(i).getColor().equals("ofdiamonds"))
				lOfdiamonds.add(liste.get(i).getValue(), liste.get(i));
			else if(liste.get(i).getColor().equals("ofspades"))
				lOfspades.add(liste.get(i).getValue(), liste.get(i));
			else if(liste.get(i).getColor().equals("ofhearts"))
				lOfhearts.add(liste.get(i).getValue(), liste.get(i));
			else 
				lOfclubs.add(liste.get(i).getValue(), liste.get(i));
		}
		liste.clear();
		lOfdiamonds.removeAll(null);
		lOfspades.removeAll(null);
		lOfhearts.removeAll(null);
		lOfclubs.removeAll(null);
		liste.addAll(lOfdiamonds);
		liste.addAll(lOfclubs);
		liste.addAll(lOfhearts);
		liste.addAll(lOfspades);
		ControllerView.user.setCards(liste);
		
	}
	
	public void updateView(){
		this.init();
	}
	private void init(){        
        // configurations for the carousel.
        this.setClickable(false);
        this.setSpacing(1);
   
        // set images for the carousel.
        adapter = new HandCardsAdapter(c);
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
		liste = new ArrayList<Card>();
	}
	
	   public class HandCardsAdapter extends BaseAdapter {
	    	private Context mContext;
	    	public HandCardsAdapter(Context c){
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
			
			public View getView(int position, View convertView, ViewGroup parent) {
		    	if(parent.getHeight() == 0)
		    		notifyDataSetChanged();
		    	CardView cv = (CardView)convertView;
		    	if(cv == null){
		    		DisplayMetrics metrics = mContext.getApplicationContext().getResources().getDisplayMetrics();
		    		cv = new CardView(mContext, ControllerView.user.getCards().get(position));
		    		int width = (int)(metrics.widthPixels/
		    				(this.getCount()<3 ? 3 : this.getCount() *1.1));
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
