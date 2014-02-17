package com.ensibs.omeca.view;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.R;
import com.ensibs.omeca.model.actions.MoveCardAction;
import com.ensibs.omeca.model.entities.Player;
import com.ensibs.omeca.wifidirect.event.WifiDirectEvent;
import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;

public class CardGroup {
	private ArrayList<View> tuching;
	private float x, y; 
	
	public CardGroup() {
		tuching = new ArrayList<View>();
	}
	
	/**
	 * return the list of grouped cards based on the long click card
	 * @return list of grouped cards
	 */
	public ArrayList<View> getTuching() {
		return tuching;
	}
	
	/**
	 * add inspect to tuching if it's tuching v
	 * @param v long click card
	 * @param inspect card to eventualy add to tuching
	 */
	private void add(View v, View inspect){
		if(inspect != v){ // touche la principale 
			for(int j= tuching.size()-1; j>-1; j--){
				if(isTuching(tuching.get(j),inspect)){
					inspect.setAlpha( 0.5f);	
					tuching.add(inspect);
					j=-1;
				}
			} 
		}
	}
	
	/**
	 * verify which card is tuching the groupe 
	 * @param cards all cards on the board
	 * @param v long click view
	 */
	public void startVerify( ArrayList<View> cards, View v) {
		int oldSize = 0;
		tuching.add(v);
		while (oldSize != tuching.size()){
			oldSize= tuching.size();
			for(int i =cards.size()-1; i >=0; i--){
				this.add(v, cards.get(i));
				if(tuching.contains(cards.get(i)))
					cards.remove(i);
			}
		}	
	}
	
	/**
	 * move all card by a distance of (x,y) in the board
	 * if some cards leave the board it stay at the border
	 * @param x distance in x 
	 * @param y distance in y 
	 * @param maxX lateral size of the board
	 * @param maxY vertical size of the board
	 */
	public void move(Float x, Float y, int maxX, int maxY){
		int left=0 , top =0, right, bottom ;
		for(int i = tuching.size()-1; i>0; i--){
			if(tuching.get(i).getX()+ (x-this.x)>0)
				if(tuching.get(i).getX()+ (x-this.x)<(maxX- tuching.get(i).getWidth()))
					left = (int) (tuching.get(i).getX()+ (x-this.x));
					//tuching.get(i).setX(tuching.get(i).getX()+ (x-this.x));
				else  left = maxX- tuching.get(i).getWidth();
					//tuching.get(i).setX(maxX);
			//else tuching.get(i).setX(0);
			
			if(tuching.get(i).getY()+ (y-this.y)>0)
				if(tuching.get(i).getY()+ (y-this.y)< maxY- tuching.get(i).getHeight())
					top = (int) (tuching.get(i).getY()+ (y-this.y));
					//tuching.get(i).setY(tuching.get(i).getY()+ (y-this.y));
				else  top = maxY - tuching.get(i).getHeight();//tuching.get(i).setY(maxY);
			//else tuching.get(i).setY(0);
			//tuching.get(i).setY(tuching.get(i).getY()+ (y-this.y));
			tuching.get(i).setAlpha(1);
			tuching.get(i).setVisibility(View.VISIBLE);
			
			MarginLayoutParams marginParams = new MarginLayoutParams(
					tuching.get(i).getLayoutParams());
			right = (int) (maxX
					- left + tuching.get(i).getWidth());
			bottom = (int) (maxY
					- top + tuching.get(i).getHeight());
			marginParams.setMargins(left, top, right, bottom);
			tuching.get(i).setLayoutParams(new RelativeLayout.LayoutParams(
					marginParams));
		}
		tuching.get(0).setVisibility(View.VISIBLE);
		tuching.clear();
	}
	
	/**
	 * prepar the groupe to be move
	 * this function have to be called before drag
	 * @param x2
	 * @param y2
	 */
	public void startMove( float x2, float y2){
		this.x= x2;
		this.y= y2;
		for(int i= 1; i<tuching.size(); i++){
			tuching.get(i).setVisibility(View.INVISIBLE);}
	}	
	public Boolean isTuching(View c1, View c2){
		float xUp1, xUp2, yUp1, yUp2;
		xUp1= c1.getX();
		yUp1= c1.getY();
		xUp2= c2.getX();
		yUp2= c2.getY();
		if(Math.abs(xUp1 - xUp2)<= c1.getWidth() && Math.abs(yUp1-yUp2)<= c1.getHeight())
			return true;
		else return false;		
	}
	public void leave() {
		//multiDrag=false;
		for(int i= 1; i<tuching.size(); i++){
			tuching.get(i).setAlpha(1);
			tuching.get(i).setVisibility(View.VISIBLE);
		}
		tuching.clear();
	}
	public void moveToPile(FrameLayout parent) {
		for(int i = tuching.size()-1; i>0; i--){
			BoardView boardView = (BoardView) (GameActivity.getActivity().findViewById(R.id.view_board));
			boardView.removeView(tuching.get(i));
			tuching.get(i).setAlpha(1);
			tuching.get(i).setVisibility(View.VISIBLE);
			parent.addView(tuching.get(i));
			if(parent instanceof DrawPileView){
				GameActivity.getActivity().getWifiDirectManager().sendEvent(new WifiDirectEventImpl(WifiDirectEvent.EVENT, new MoveCardAction("BoardView", "DrawPileView",((CardView)tuching.get(i)).getCard())));
			}else if(parent instanceof DiscardPileView){
				GameActivity.getActivity().getWifiDirectManager().sendEvent(new WifiDirectEventImpl(WifiDirectEvent.EVENT, new MoveCardAction("BoardView", "DiscardPileView",((CardView)tuching.get(i)).getCard())));
			}
		}
		tuching.clear();
	}

	public void moveToPlayer(Player p) {
		for(int i = tuching.size()-1; i>0; i--){
			BoardView boardView = (BoardView) (GameActivity.getActivity().findViewById(R.id.view_board));
			CardView cv = (CardView) tuching.get(i);
			boardView.removeView(cv);
			cv.setAlpha(1);
			cv.setVisibility(View.VISIBLE);
			p.addCard(cv.getCard());
			GameActivity.getActivity().getWifiDirectManager().sendEvent(new WifiDirectEventImpl(WifiDirectEvent.EVENT, new MoveCardAction("BoardView",cv.getCard(),"Player",p.getId())));
		}
		tuching.clear();
	}
}
