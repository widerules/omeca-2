package com.ensibs.omeca.view;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.R;
import com.ensibs.omeca.controller.ActionController;
import com.ensibs.omeca.model.actions.ReturnCardAction;
import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.wifidirect.event.WifiDirectEvent;
import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;
import com.ensibs.omeca.wifidirect.property.WifiDirectProperty;

@SuppressLint("ViewConstructor")
public class CardView extends ImageView{
	private Card card;
	private Context context;

	public static final float RATIO = 1.452f;
	public static final int SIZE = 5;

	public CardView(Context context, Card card) {
		super(context);
		this.context = context;
		this.card = card;
		if(card.isFaceUp())
			setFaceUpBackground();
		else
			setBackBackground();
		DisplayMetrics metrics = context.getApplicationContext().getResources().getDisplayMetrics();
		int height = metrics.heightPixels/SIZE;
		int width = (int) (height/RATIO);
		setLayoutParams(new RelativeLayout.LayoutParams(width, height));
		setOnTouchListener(new CardTouchListener());
		setOnLongClickListener(new CardLongTouchListener());
		setOnDragListener(null);

	}

	public void turnCard(){
		if(card.isFaceUp())
			setBackBackground();
		else
			setFaceUpBackground();

		card.setFaceUp(!card.isFaceUp());
	}

	private void setBackBackground(){
		setImageDrawable(
				context.getResources().getDrawable(
						context.getResources().getIdentifier(
								Card.CARDBACK,
								"drawable",
								context.getApplicationContext().getPackageName()
								)
						)
				);
	}

	private void setFaceUpBackground(){
		setImageDrawable(
				context.getResources().getDrawable(
						context.getResources().getIdentifier(
								card.getColor()+card.getValue(),
								"drawable",
								context.getApplicationContext().getPackageName()
								)
						)
				);
	}

	class CardTouchListener implements OnTouchListener{
		private float x;
		private float y;
		private final float SCROLL_THRESHOLD = 10;
		private boolean isOnClick;

		@Override
		public boolean onTouch(View view, MotionEvent mE) {
			BoardView boardView = (BoardView) (GameActivity.getActivity().findViewById(R.id.view_board));
			switch (mE.getAction()){
			case MotionEvent.ACTION_DOWN:
				x = mE.getX();
				y = mE.getY();
				isOnClick = true;
				break;
			case MotionEvent.ACTION_UP:
				if(isOnClick){
					int index = ActionController.user.getCards().indexOf(card);
					if(index != -1){
						ActionController.user.getCards().remove(index);
						turnCard();
						ActionController.user.getCards().add(index, card);
					}
					else
						turnCard();

					view.setVisibility(View.VISIBLE);
					String src = "";
					if(view.getParent() instanceof DrawPileView)
						src = "DrawPileView";
					else if(view.getParent() instanceof BoardView)
						src = "BoardView";
					else if(view.getParent() instanceof DiscardPileView)
						src = "DiscardPileView";
					if(src.equals("DrawPileView") || src.equals("BoardView") || src.equals("DiscardPileView"))
						GameActivity.getActivity().getWifiDirectManager().sendEvent(new WifiDirectEventImpl(WifiDirectEvent.EVENT, new ReturnCardAction(src, getCard())));
					boardView.cg.leave();
				}
				boardView.cg.getTuching().clear();
				break;
			case MotionEvent.ACTION_MOVE:
				if(isOnClick && 
						Math.sqrt((x-mE.getX())*(x-mE.getX()) + (y-mE.getY())*(y-mE.getY())) > SCROLL_THRESHOLD){
					view.setVisibility(View.INVISIBLE);
					ClipData data = ClipData.newPlainText("", "");
					DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
					view.startDrag(data, shadowBuilder, view, 0);
					isOnClick = false; 
					if(boardView.cg.getTuching().size()>1){
						Log.i(WifiDirectProperty.TAG, "x: "+mE.getX());
						boardView.cg.startMove(view.getX()+mE.getX(), view.getY()+ mE.getY());
					}
				}
				break;
			default:
				break;
			}	
			return false;
		}

	}

	class CardLongTouchListener implements OnLongClickListener{

		@Override
		public boolean onLongClick(View v) {
				BoardView boardView = (BoardView) (GameActivity.getActivity().findViewById(R.id.view_board));
				ArrayList<View> cards = new ArrayList<View>();
				for( int i = 0; i <boardView.getChildCount(); i++){
					View inspect = boardView.getChildAt(i);
					if( inspect instanceof CardView){
						cards.add(inspect);
						//boardView.cg.add(v,inspect);
					}
				}
				boardView.cg.startVerify(cards, v);
			
			return true;
		}		
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

}
