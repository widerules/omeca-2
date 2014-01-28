package com.ensibs.omeca.view;

import java.util.ArrayList;

import android.content.ClipData;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Toast;

import com.ensibs.omeca.controller.ActionController;
import com.ensibs.omeca.model.entities.Card;


public class HandView extends Gallery{

	ArrayList<Card> liste;
	private HandCardsAdapter adapter;
	Context c;
	HandViewCardGalleryDragListener hvcgdl = new HandViewCardGalleryDragListener();

	public HandView(Context context) {
		super(context);	
		c= context;
		liste = new ArrayList<Card>();
		init();
	}
	public void orderCard(){
		liste = ActionController.user.getCards();
		ArrayList<Card> lOfdiamonds = new ArrayList<Card>() ;
		ArrayList<Card> lOfspades = new ArrayList<Card>();
		ArrayList<Card> lOfhearts = new ArrayList<Card>();
		ArrayList<Card> lOfclubs = new ArrayList<Card>();
		for( int i = 0; ActionController.user.getCards().size()>i ; i++ ){
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
		liste.addAll(lOfhearts);
		liste.addAll(lOfdiamonds);
		liste.addAll(lOfclubs);
		liste.addAll(lOfspades);
		ActionController.user.setCards(liste);
		init();
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
		this.setSelection(ActionController.user.getNumberOfCards()/2);
		this.setOnDragListener(hvcgdl);

		/*block le slider
        this.setOnTouchListener(new OnTouchListener() {
        	  @Override
        	  public boolean onTouch(View v, MotionEvent event) {
        	     return true;
        	  }
        	 });*/

	}

	public HandView(Context context, AttributeSet attrs) {
		super(context, attrs);
		c= context;
		init();
		liste = new ArrayList<Card>();
	}

	public class HandCardsAdapter extends BaseAdapter {
		private Context mContext;
		private ArrayList<CardView> liste = new ArrayList<CardView>();
		public HandCardsAdapter(Context c){
			mContext = c;
		}
		@Override
		public int getCount() {
			return ActionController.user.getCards().size();
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
				cv = new CardView(mContext, ActionController.user.getCards().get(position));
				int width = (int)(metrics.widthPixels/
						(this.getCount()<5 ? 5 : this.getCount() *1.1));
				int height = (int)(width*CardView.RATIO);
				cv.setLayoutParams(new Gallery.LayoutParams( width, height));
				cv.setOnTouchListener(null);
				if(!cv.getCard().isFaceUp())
					cv.turnCard();
				cv.setOnDragListener(new OnDragListenerHand() );
				cv.setOnTouchListener(new CardTouchListenerHand());
				//liste.add(position, cv);
				return cv; 
			}
			return cv;
		}
	}

	private class OnDragListenerHand implements OnDragListener{
		@Override
		public boolean onDrag(View v, DragEvent event) {
			switch (event.getAction()){
			case DragEvent.ACTION_DRAG_ENTERED:
				CardView cv = (CardView) v;	
				cv.setRotationY(-20);
				/*Rotater la carte de gauche
				 HandView hv = (HandView) v.getParent();
				 int j = ActionController.user.getCards().indexOf(cv.getCard());
				Toast.makeText(c ,"j: "+j, Toast.LENGTH_SHORT).show();
				if(j>0){
					cv = (CardView) hv.getItemAtPosition(j);
					Toast.makeText(c ,"j-1 :" + (j-1)+"    "+ "valeur"+cv.getCard().getValue(), Toast.LENGTH_SHORT).show();
					cv.setRotationY(20);
				}*/
				break;
			case DragEvent.ACTION_DROP:				
				CardView newPositionCard = (CardView) v;
				int i = ActionController.user.getCards().indexOf(newPositionCard.getCard());
				HandView hv1 = (HandView) v.getParent();
				CardView cardToMove1 =(CardView) event.getLocalState();
				int exPosition = ActionController.user.getCards().indexOf(cardToMove1.getCard());
				ActionController.user.getCards().remove(cardToMove1.getCard());
				if(i<exPosition)
					ActionController.user.getCards().add(i,cardToMove1.getCard());
				else 	ActionController.user.getCards().add(i-1,cardToMove1.getCard());
				hv1.updateView();
				break;

			case DragEvent.ACTION_DRAG_EXITED :
				CardView cv2 = (CardView) v;	
				cv2.setRotationY(0);
				/*HandView hv2 = (HandView) v.getParent();
				int k = ActionController.user.getCards().indexOf(cv2.getCard());
				cv2 = (CardView) hv2.getItemAtPosition(k);
				cv2.setRotationY(0);*/
				break;
			default:
				break;
			}
			return true;
		}
	}
	public class CardTouchListenerHand implements OnTouchListener{
		private boolean isOnClick;

		@Override
		public boolean onTouch(View view, MotionEvent mE) {
			switch (mE.getAction()){
			case MotionEvent.ACTION_DOWN:
				isOnClick = true;
				break;
			case MotionEvent.ACTION_MOVE:
				view.setVisibility(View.INVISIBLE);
				ClipData data = ClipData.newPlainText("", "");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
				view.startDrag(data, shadowBuilder, view, 0);
				isOnClick = false;
				break;
			case MotionEvent.ACTION_UP:
				if(!isOnClick){
					view.setVisibility(View.VISIBLE);
				}
				break;
			default:
				break;
			}	
			return true;
		}
	}

}
