package com.ensibs.omeca.view;

import com.ensibs.omeca.ControllerView;
import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.model.entities.Player;

import android.graphics.Color;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnDragListener;
import android.widget.Toast;

public class SlideBarCardGalleryDragListener implements OnDragListener{

	@Override
    public boolean onDrag(View v, DragEvent event) {
		switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				Toast.makeText(GameActivity.getActivity(), "Poueeeeeeet",
						   Toast.LENGTH_SHORT).show();
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				Toast.makeText(GameActivity.getActivity(), "Patamoueeeeeette",
						   Toast.LENGTH_SHORT).show();
				break;
			case DragEvent.ACTION_DROP:
				CardView view = (CardView) event.getLocalState();
				ViewGroup parent = (ViewGroup)(view.getParent());
				Card c = view.getCard();
				Player p = ControllerView.user;
				p.addCard(c);
		        parent.removeView(view);
				break;
			case DragEvent.ACTION_DRAG_ENDED:
				break;
			default:
				break;
		}
		
		return true;
    }
}
