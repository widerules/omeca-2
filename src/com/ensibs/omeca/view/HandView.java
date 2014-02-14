package com.ensibs.omeca.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.ensibs.omeca.R;
import com.ensibs.omeca.controller.ActionController;
import com.ensibs.omeca.model.entities.Card;

public class HandView extends Gallery {
	public static final int CARDS_TO_DISPLAY_ORIGIN = 5;
	public static int CARDS_TO_DISPLAY = 5;
	
	ArrayList<Card> list;
	private HandCardsAdapter adapter;
	Context c;
	HandViewCardGalleryDragListener hvcgdl = new HandViewCardGalleryDragListener();

	public HandView(Context context) {
		super(context);
		c = context;
		init();
	}
	
	public void addCard(CardView cv){
		ActionController.user.addCard(cv.getCard());
		updateView(true);
	}

	public void totalOrderCard() {
		list = ActionController.user.getCards();
		Card[] lOfspades = new Card[Card.VALUES.length];
		Card[] lOfhearts = new Card[Card.VALUES.length];
		Card[] lOfclubs = new Card[Card.VALUES.length];
		Card[] lOfdiamonds = new Card[Card.VALUES.length];
		Card[] jokers = new Card[Card.JOKERS.length];
		for (int i = 0; ActionController.user.getCards().size() > i; i++) {
			if (list.get(i).getColor().equals(Card.COLORS[0]))
				lOfdiamonds[list.get(i).getValue() - 1] = list.get(i);
			else if (list.get(i).getColor().equals(Card.COLORS[1]))
				lOfspades[list.get(i).getValue() - 1] = list.get(i);
			else if (list.get(i).getColor().equals(Card.COLORS[2]))
				lOfhearts[list.get(i).getValue() - 1] = list.get(i);
			else if (list.get(i).getColor().equals(Card.COLORS[3]))
				lOfclubs[list.get(i).getValue() - 1] = list.get(i);
			else if (list.get(i).getColor().equals(Card.JOKERS[0])) {
				jokers[0] = list.get(i);
			} else
				jokers[1] = list.get(i);

		}
		list.clear();
		for (int i = 0; i < Card.JOKERS.length; i++)
			if (jokers[i] != null)
				list.add(jokers[i]);
		for (int i = 0; i < Card.VALUES.length; i++)
			if (lOfdiamonds[i] != null)
				list.add(lOfdiamonds[i]);
		for (int i = 0; i < Card.VALUES.length; i++)
			if (lOfspades[i] != null)
				list.add(lOfspades[i]);
		for (int i = 0; i < Card.VALUES.length; i++)
			if (lOfhearts[i] != null)
				list.add(lOfhearts[i]);
		for (int i = 0; i < Card.VALUES.length; i++)
			if (lOfclubs[i] != null)
				list.add(lOfclubs[i]);
		ActionController.user.setCards(list);
		updateView(true);
	}

	public void colorOrderCard() {
		list = ActionController.user.getCards();
		ArrayList<Card> lOfspades = new ArrayList<Card>();
		ArrayList<Card> lOfhearts = new ArrayList<Card>();
		ArrayList<Card> lOfclubs = new ArrayList<Card>();
		ArrayList<Card> lOfdiamonds = new ArrayList<Card>();
		ArrayList<Card> jokers = new ArrayList<Card>();
		for (int i = 0; ActionController.user.getCards().size() > i; i++) {
			if (list.get(i).getColor().equals(Card.COLORS[0]))
				lOfdiamonds.add(list.get(i));
			else if (list.get(i).getColor().equals(Card.COLORS[1]))
				lOfspades.add(list.get(i));
			else if (list.get(i).getColor().equals(Card.COLORS[2]))
				lOfhearts.add(list.get(i));
			else if (list.get(i).getColor().equals(Card.COLORS[3]))
				lOfclubs.add(list.get(i));
			else
				jokers.add(list.get(i));
		}
		list.clear();
		list.addAll(jokers);
		list.addAll(lOfdiamonds);
		list.addAll(lOfspades);
		list.addAll(lOfhearts);
		list.addAll(lOfclubs);
		ActionController.user.setCards(list);
		updateView(true);
	}

	public void valueOrderCard() {
		list = ActionController.user.getCards();
		ArrayList<Card> finalOrder = new ArrayList<Card>();
		Card min = null;
		while (!list.isEmpty()) {
			min = list.get(0);
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getValue() < min.getValue())
					min = list.get(i);

			}
			finalOrder.add(min);
			ActionController.user.removeCard(min);
		}
		ActionController.user.setCards(finalOrder);
		this.updateView(true);
	}

	public void updateView(boolean backToTheMiddle) {
		adapter.notifyDataSetChanged();
		DisplayMetrics metrics = c.getApplicationContext().getResources()
				.getDisplayMetrics();
		int cardWidth = (int)(metrics.heightPixels / CARDS_TO_DISPLAY);
		int nbUserCards = ActionController.user.getNumberOfCards();
		int spacing = (nbUserCards > CARDS_TO_DISPLAY) ? (int)((-2*cardWidth*(nbUserCards-CARDS_TO_DISPLAY)) / (nbUserCards+1)) : 1;
		setSpacing(spacing);
		if (backToTheMiddle)
			this.setSelection(ActionController.user.getNumberOfCards() / 2);
	}

	private void init() {
		// configurations for the carousel.
		this.setClickable(false);
		this.setSpacing(1);

		// set images for the carousel.
		adapter = new HandCardsAdapter(c);
		this.setAdapter(adapter);
		this.setUnselectedAlpha((float) 1);
		this.setSelection(ActionController.user.getNumberOfCards() / 2);
		this.setOnDragListener(hvcgdl);
		list = new ArrayList<Card>();
	}

	public HandView(Context context, AttributeSet attrs) {
		super(context, attrs);
		c = context;
		init();
	}

	public class HandCardsAdapter extends BaseAdapter {
		private Context mContext;

		public HandCardsAdapter(Context c) {
			mContext = c;
		}

		@Override
		public int getCount() {
			return ActionController.user.getNumberOfCards();
		}

		@Override
		public Object getItem(int position) {
			return new CardView(c, ActionController.user.getCards()
					.get(position));
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (parent.getHeight() == 0)
				notifyDataSetChanged();
			CardView cv = (CardView) convertView;
			if (cv == null) {
				DisplayMetrics metrics = mContext.getApplicationContext()
						.getResources().getDisplayMetrics();
				cv = new CardView(mContext, ActionController.user.getCards()
						.get(position));
				int width = (int) (metrics.widthPixels / (CARDS_TO_DISPLAY));
				int height = (int) (width * CardView.RATIO);
				cv.setLayoutParams(new Gallery.LayoutParams(width, height));
				cv.setOnDragListener(new OnDragListenerHand());
			}
			return cv;
		}
	}

	class OnDragListenerHand implements OnDragListener {
		@Override
		public boolean onDrag(View v, DragEvent event) {
			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_ENTERED:
				CardView cv = (CardView) v;
				cv.setRotationY(-10);
				cv.setBackgroundResource(R.drawable.sorting_card_background);
				cv.setPadding(5, 0 ,0 ,0);
				break;
			case DragEvent.ACTION_DROP:
				CardView newPositionCard = (CardView) v;
				int i = ActionController.user.getCards().indexOf(
						newPositionCard.getCard());
				HandView hv1 = (HandView) v.getParent();
				CardView cardToMove1 = (CardView) event.getLocalState();
				int exPosition = ActionController.user.getCards().indexOf(
						cardToMove1.getCard());
				ActionController.user.getCards().remove(cardToMove1.getCard());
				if (i < exPosition)
					ActionController.user.getCards().add(i,
							cardToMove1.getCard());
				else
					ActionController.user.getCards().add(i - 1,
							cardToMove1.getCard());
				hv1.updateView(false);
				break;

			case DragEvent.ACTION_DRAG_EXITED:
				CardView cv2 = (CardView) v;
				cv2.setRotationY(0);
				cv2.setPadding(0, 0 ,0 ,0);
				cv2.setBackgroundColor(Color.TRANSPARENT);
				break;
			default:
				break;
			}
			return true;
		}
	}

	/**
	 * No need for the moment
	class CardTouchListenerHand implements OnTouchListener {
		private boolean isOnClick;

		@Override
		public boolean onTouch(View view, MotionEvent mE) {
			switch (mE.getAction()) {
			case MotionEvent.ACTION_DOWN:
				isOnClick = true;
				break;
			case MotionEvent.ACTION_MOVE:
				view.setVisibility(View.INVISIBLE);
				ClipData data = ClipData.newPlainText("", "");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
						view);
				view.startDrag(data, shadowBuilder, view, 0);
				isOnClick = false;
				break;
			case MotionEvent.ACTION_UP:
				if (!isOnClick) {
					view.setVisibility(View.VISIBLE);
				}
				break;
			default:
				break;
			}
			return true;
		}
	}
	*/

	@Override
	public void removeViewInLayout(View view) {
		if (view instanceof CardView) {
			super.removeViewInLayout(view);
			ActionController.user.removeCard(((CardView) view).getCard());
			updateView(true);
		}
	}
	
	
	public class CardsZoomSeekbarChangeListener implements OnSeekBarChangeListener{

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			CARDS_TO_DISPLAY = (int)(((1.0+(20-progress))/10)*CARDS_TO_DISPLAY_ORIGIN)+CARDS_TO_DISPLAY_ORIGIN;
			updateView(false);
			
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		}
		
	}

	public class OrderByValueTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent mE) {
			switch (mE.getAction()) {
			case MotionEvent.ACTION_DOWN:
				v.setBackgroundResource(R.drawable.action_down);
				break;
			case MotionEvent.ACTION_UP:
				valueOrderCard();
				v.setBackgroundResource(R.drawable.action_background);
				break;
			default:
				break;
			}
			return true;
		}

	}

	public class OrderByColorTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent mE) {
			switch (mE.getAction()) {
			case MotionEvent.ACTION_DOWN:
				v.setBackgroundResource(R.drawable.action_down);
				break;
			case MotionEvent.ACTION_UP:
				colorOrderCard();
				v.setBackgroundResource(R.drawable.action_background);
				break;
			default:
				break;
			}
			return true;
		}

	}

	public class TotalOrderTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent mE) {
			switch (mE.getAction()) {
			case MotionEvent.ACTION_DOWN:
				v.setBackgroundResource(R.drawable.action_down);
				break;
			case MotionEvent.ACTION_UP:
				totalOrderCard();
				v.setBackgroundResource(R.drawable.action_background);
				break;
			default:
				break;
			}
			return true;
		}

	}

}
