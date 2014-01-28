package com.ensibs.omeca.view;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.DragShadowBuilder;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ensibs.omeca.R;
import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.model.entities.Player;
import com.ensibs.omeca.utils.AvatarsList;

public class PlayerView extends RelativeLayout{

	private Player player;
	private Context context;
	private TextView name;
	private TextView cards;
	private ImageView avatar;
	private ImageView cardsImages;

	public static final int SIZE = 7;

	public PlayerView(Context context) {
		super(context);
		init(context);
	}

	public PlayerView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		init(context);
	}

	public PlayerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		this.name = new TextView(context);
		this.avatar = new ImageView(context);
		this.cardsImages = new ImageView(context);
		this.cards = new TextView(context);
		DisplayMetrics metrics = context.getApplicationContext().getResources().getDisplayMetrics();
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(metrics.heightPixels/(SIZE), metrics.heightPixels/SIZE);

		setLayoutParams(params);
		setBackgroundResource(R.drawable.player_empty);
		setOnDragListener(new PlayerDragListener());
		setOnTouchListener(new PlayerTouchListener());
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player, boolean isMe){
		this.player = player;
		removeAllViews();
		if (player == null) { 
			setBackgroundResource(R.drawable.player_empty);

		} else { 
			setBackgroundColor(Color.TRANSPARENT);

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params.addRule(ALIGN_PARENT_TOP, TRUE);

			name.setText(player.getName());
			name.setGravity(Gravity.CENTER_HORIZONTAL);
			name.setTextColor(Color.WHITE);
			name.setTypeface(null, Typeface.BOLD);
			name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
			addView(name, params);

			if (!isMe) {			
				params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				params.addRule(CENTER_VERTICAL, TRUE);

				cards.setGravity(Gravity.RIGHT);
				cards.setTextColor(Color.WHITE);
				cards.setTypeface(null, Typeface.BOLD);
				cards.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);

				addView(cards, params);

				params = new RelativeLayout.LayoutParams((int)(getLayoutParams().width*0.85), (int)(getLayoutParams().height*0.85));
				params.addRule(ALIGN_PARENT_BOTTOM, TRUE);
				params.addRule(CENTER_HORIZONTAL);

				avatar.setImageResource(AvatarsList.get(player.getAvatar()));
				addView(avatar, params);

				params = new RelativeLayout.LayoutParams((int)(getLayoutParams().width*0.4), (int)(getLayoutParams().height*0.4));
				params.addRule(ALIGN_PARENT_BOTTOM, TRUE);
				params.addRule(ALIGN_PARENT_RIGHT, TRUE);

				addView(cardsImages, params);
				
				switch(player.getNumberOfCards()){
				case 0:	
					cardsImages.setBackgroundColor(Color.TRANSPARENT);
					cards.setText("");
					break;
				case 1:
					cardsImages.setBackgroundResource(R.drawable.cardsx1);
					break;
				case 2:
					cardsImages.setBackgroundResource(R.drawable.cardsx2);
					break;
				default:
					cardsImages.setBackgroundResource(R.drawable.cardsx3);
					cards.setText("" + player.getCards().size());
					break;
				}

			} else {
				params = new RelativeLayout.LayoutParams((int)(getLayoutParams().width*0.80), (int)(getLayoutParams().height*0.80));
				params.addRule(ALIGN_PARENT_BOTTOM, TRUE);
				params.addRule(CENTER_HORIZONTAL);

				avatar.setImageResource(AvatarsList.get(player.getAvatar()));
				addView(avatar, params);

				setOnDragListener(null);

				name.setTextColor(Color.DKGRAY);
				name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
				name.setGravity(Gravity.LEFT);
			}
		}
	}

	private class PlayerDragListener implements OnDragListener{

		@Override
		public boolean onDrag(View v, DragEvent event) {
			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				setBackgroundResource(R.drawable.player);
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				if(player != null)
					setBackgroundColor(Color.TRANSPARENT);
				else
					setBackgroundResource(R.drawable.player_empty);
				break;
			case DragEvent.ACTION_DROP:
				if (event.getLocalState() instanceof PlayerView) {
					PlayerView pv = (PlayerView) event.getLocalState();

					Player pvtmp = player;
					setPlayer(pv.getPlayer(), false);
					pv.setPlayer(pvtmp, false);
					pv.setVisibility(View.VISIBLE);

				} else {
					CardView view = (CardView) event.getLocalState();
					ViewGroup parent = (ViewGroup)(view.getParent());
					Card c = view.getCard();
					player.addCard(c);
					switch(player.getNumberOfCards()){
					case 1:
						cardsImages.setBackgroundResource(R.drawable.cardsx1);
						break;
					case 2:
						cardsImages.setBackgroundResource(R.drawable.cardsx2);
						break;
					default:
						cardsImages.setBackgroundResource(R.drawable.cardsx3);
						cards.setText("" + player.getCards().size());
						break;
					}
					parent.removeViewInLayout(view);
				}

				setBackgroundColor(Color.TRANSPARENT);
				break;
			case DragEvent.ACTION_DRAG_ENDED:
				break;
			default:
				break;
			}
			return true;
		}
	}

	class PlayerTouchListener implements OnTouchListener{
		private float x;
		private float y;
		private final float SCROLL_THRESHOLD = 10;
		private boolean isOnClick;

		@Override
		public boolean onTouch(View view, MotionEvent mE) {
			switch (mE.getAction()){
			case MotionEvent.ACTION_DOWN:
				x = mE.getX();
				y = mE.getY();
				isOnClick = true;
				break;
			case MotionEvent.ACTION_UP:
				break;
			case MotionEvent.ACTION_MOVE:
				if(isOnClick && 
						Math.sqrt((x-mE.getX())*(x-mE.getX()) + (y-mE.getY())*(y-mE.getY())) > SCROLL_THRESHOLD){
					view.setVisibility(View.INVISIBLE);
					ClipData data = ClipData.newPlainText("", "");
					DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
					view.startDrag(data, shadowBuilder, view, 0);
					isOnClick = false;
				}
				break;
			default:
				break;
			}	
			return true;
		}

	}
}
