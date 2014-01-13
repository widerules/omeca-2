package com.ensibs.omeca.view;

import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.widget.Gallery;

import com.ensibs.omeca.ControllerView;
import com.ensibs.omeca.R;
import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.model.entities.Player;
import com.ensibs.omeca.utils.SliderbarCardGallery;
import com.ensibs.omeca.view.HandView.HandCardsAdapter;

public class SlideBarCardGalleryDragListener implements OnDragListener{

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
				CardView view = (CardView) event.getLocalState();
				ViewGroup parent = (ViewGroup)(view.getParent());
				Card c = view.getCard();
				Player p = ControllerView.user;
				p.addCard(c);
		        parent.removeView(view);
		        Gallery g = (Gallery)v;
		        SliderbarCardGallery l = (SliderbarCardGallery)g.getAdapter();
		        l.notifyDataSetChanged();
		        	g.setSelection(ControllerView.user.getNumberOfCards()-1);
		        Gallery g2 = ((Gallery)((View)v.getParent().getParent()).findViewById(R.id.view_hand_slidebar));
		        HandCardsAdapter a2 = (HandCardsAdapter) g2.getAdapter();
				a2.notifyDataSetChanged();
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
