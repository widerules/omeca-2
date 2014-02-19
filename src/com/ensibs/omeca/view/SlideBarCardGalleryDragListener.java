package com.ensibs.omeca.view;

import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.widget.Gallery;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.R;
import com.ensibs.omeca.controller.GA;
import com.ensibs.omeca.model.actions.MoveCardAction;
import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.model.entities.Player;
import com.ensibs.omeca.utils.NotificationTools;
import com.ensibs.omeca.utils.SliderbarCardGallery;
import com.ensibs.omeca.wifidirect.event.WifiDirectEvent;
import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;

/**
 * Drag listener for the SlidebarCardGallery to add CardViews to it
 * 
 * @author OMECA 2.0 Team (Rapha�l GICQUIAUX - Nicolas HALLOUIN - Sylvain RIO -
 *         Lindsay ROZIER)
 * 
 */
public class SlideBarCardGalleryDragListener implements OnDragListener {

	/**
	 * OnDrag event actions
	 */
	@Override
	public boolean onDrag(View v, DragEvent event) {
		switch (event.getAction()) {
		case DragEvent.ACTION_DRAG_STARTED:
			break;
		case DragEvent.ACTION_DRAG_ENTERED:
			v.setBackgroundResource(R.drawable.gallery_background_hover);
			break;
		case DragEvent.ACTION_DRAG_EXITED:
			v.setBackgroundResource(R.drawable.gallery_background);
			break;
		case DragEvent.ACTION_DROP:
			View vTmp = (View) event.getLocalState();

			if (vTmp instanceof CardView) {
				CardView view = (CardView) vTmp;
				ViewGroup parent = (ViewGroup) (view.getParent());
				Card c = view.getCard();
				Player p = GA.user;
				// multi drag
				BoardView boardView = (BoardView) (GameActivity.getActivity()
						.findViewById(R.id.view_board));
				boardView.getCardsGroup().moveToPlayer(p);

				p.addCard(c);
				parent.removeViewInLayout(view);
				Gallery g = (Gallery) v;
				SliderbarCardGallery l = (SliderbarCardGallery) g.getAdapter();
				l.notifyDataSetChanged();
				g.setSelection(GA.user.getNumberOfCards() - 1);
				HandView hv = ((HandView) ((View) v.getParent().getParent()
						.getParent()).findViewById(R.id.handview));
				hv.updateView(true);
				// Send event to the other
				if (parent instanceof BoardView) {
					GameActivity
							.getActivity()
							.getWifiDirectManager()
							.sendEvent(
									new WifiDirectEventImpl(
											WifiDirectEvent.EVENT,
											new MoveCardAction("BoardView",
													view.getCard(), "Player",
													GA.user.getId())));
				} else if (parent instanceof DrawPileView) {
					GameActivity
							.getActivity()
							.getWifiDirectManager()
							.sendEvent(
									new WifiDirectEventImpl(
											WifiDirectEvent.EVENT,
											new MoveCardAction("DrawPileView",
													view.getCard(), "Player",
													GA.user.getId())));
				} else if (parent instanceof DiscardPileView) {
					GameActivity
							.getActivity()
							.getWifiDirectManager()
							.sendEvent(
									new WifiDirectEventImpl(
											WifiDirectEvent.EVENT,
											new MoveCardAction(
													"DiscardPileView", view
															.getCard(),
													"Player", GA.user.getId())));
				}
				if (GA.isSoundToggled())
					NotificationTools.createSoundNotification(v.getContext(),
							R.drawable.flipcard);
			} else {
				vTmp.setVisibility(View.VISIBLE);
			}
			break;
		case DragEvent.ACTION_DRAG_ENDED:
			v.setBackgroundResource(R.drawable.gallery_background);
			break;
		default:
			break;
		}

		return true;
	}
}
