package com.ensibs.omeca.view;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.R;
import com.ensibs.omeca.controller.ActionController;
import com.ensibs.omeca.model.actions.MoveCardAction;
import com.ensibs.omeca.utils.SliderbarCardGallery;
import com.ensibs.omeca.wifidirect.event.WifiDirectEvent;
import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;

import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnDragListener;
import android.widget.Gallery;
import android.widget.TextView;

public class PileDragListener implements OnDragListener {
	private ViewGroup pile;

	
	public PileDragListener(ViewGroup pile){
		this.pile = pile;
	}
	
	@Override
	public boolean onDrag(View v, DragEvent event) {
		View view = (View) event.getLocalState();
		ViewGroup owner = (ViewGroup) view.getParent();
		
		switch (event.getAction()) {
		case DragEvent.ACTION_DRAG_STARTED:
			break;
		case DragEvent.ACTION_DRAG_ENTERED:
			break;
		case DragEvent.ACTION_DRAG_EXITED:
			
			break;
		case DragEvent.ACTION_DROP:
			if(view instanceof CardView){				
				owner.removeViewInLayout(view);
				Log.w("Type", owner.getClass().toString());
				pile.addView(view);
				view.setOnDragListener(null);
				view.setVisibility(View.VISIBLE);
				if(v instanceof DiscardPileView){
					DiscardPileView pile = (DiscardPileView)v;
					TextView text = (TextView)((View)v.getParent()).findViewById(R.id.nbDiscardPileCards);
					text.setText(""+pile.getDiscardPile().getNumberOfCards());
					if(owner instanceof DrawPileView){
						GameActivity.getActivity().getWifiDirectManager().sendEvent(new WifiDirectEventImpl(WifiDirectEvent.EVENT, new MoveCardAction("DrawPileView", "DiscardPileView",((CardView)view).getCard())));
					}else if(owner instanceof BoardView){
						GameActivity.getActivity().getWifiDirectManager().sendEvent(new WifiDirectEventImpl(WifiDirectEvent.EVENT, new MoveCardAction("BoardView", "DiscardPileView",((CardView)view).getCard())));
					}else if(owner instanceof HandView){
						GameActivity.getActivity().getWifiDirectManager().sendEvent(new WifiDirectEventImpl(WifiDirectEvent.EVENT, new MoveCardAction("Player", ActionController.user.getId(), ((CardView)view).getCard(), "DiscardPileView")));
						Gallery g = (Gallery) GameActivity
								.getActivity()
								.findViewById(
										R.id.playerview_slider_board_cardgallery);
						if (g != null) {
							SliderbarCardGallery a = (SliderbarCardGallery) g
									.getAdapter();
							a.notifyDataSetChanged();
						}
					}
					// multi drag
					BoardView boardView = (BoardView) (GameActivity.getActivity().findViewById(R.id.view_board));
					boardView.getCardsGroup().moveToPile(pile);
				}
				else if(v instanceof DrawPileView){
					DrawPileView pile = (DrawPileView)v;
					TextView text = (TextView)((View)v.getParent()).findViewById(R.id.nbDrawPileCards);
					text.setText(""+pile.getDrawpile().getNumberOfCards());
					if(owner instanceof DiscardPileView){
						GameActivity.getActivity().getWifiDirectManager().sendEvent(new WifiDirectEventImpl(WifiDirectEvent.EVENT, new MoveCardAction("DiscardPileView","DrawPileView" ,((CardView)view).getCard())));
					}else if(owner instanceof BoardView){
						GameActivity.getActivity().getWifiDirectManager().sendEvent(new WifiDirectEventImpl(WifiDirectEvent.EVENT, new MoveCardAction("BoardView", "DrawPileView",((CardView)view).getCard())));
					}else if(owner instanceof HandView){
						GameActivity.getActivity().getWifiDirectManager().sendEvent(new WifiDirectEventImpl(WifiDirectEvent.EVENT, new MoveCardAction("Player", ActionController.user.getId(), ((CardView)view).getCard(), "DrawPileView")));
						Gallery g = (Gallery) GameActivity
								.getActivity()
								.findViewById(
										R.id.playerview_slider_board_cardgallery);
						if (g != null) {
							SliderbarCardGallery a = (SliderbarCardGallery) g
									.getAdapter();
							a.notifyDataSetChanged();
						}
					}
				}
			}
			else
				view.setVisibility(View.VISIBLE);
			break;
		case DragEvent.ACTION_DRAG_ENDED:
			if(view instanceof CardView){
				if(v instanceof DiscardPileView){
					DiscardPileView pile = (DiscardPileView)v;
					TextView text = (TextView)((View)v.getParent()).findViewById(R.id.nbDiscardPileCards);
					text.setText(""+pile.getDiscardPile().getNumberOfCards());
				}
				else if(v instanceof DrawPileView){
					DrawPileView pile = (DrawPileView)v;
					TextView text = (TextView)((View)v.getParent()).findViewById(R.id.nbDrawPileCards);
					text.setText(""+pile.getDrawpile().getNumberOfCards());
				}
			}
			break;
		default:
			break;
		}
		return true;
	}

}
