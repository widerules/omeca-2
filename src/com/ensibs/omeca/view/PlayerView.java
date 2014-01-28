package com.ensibs.omeca.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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
		this.cards = new TextView(context);
		this.avatar = new ImageView(context);
		DisplayMetrics metrics = context.getApplicationContext().getResources().getDisplayMetrics();
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(metrics.heightPixels/(SIZE), metrics.heightPixels/SIZE);
		
		setLayoutParams(params);
		setOnDragListener(new PlayerDragListener());
	}
	
	public void setPlayer(Player player, boolean isMe){
		this.player = player;
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
			params.addRule(ALIGN_PARENT_BOTTOM, TRUE);
			
			cards.setText("x" + player.getCards().size());
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
					setBackgroundColor(Color.TRANSPARENT);
					break;
				case DragEvent.ACTION_DROP:
					CardView view = (CardView) event.getLocalState();
					ViewGroup parent = (ViewGroup)(view.getParent());
					Card c = view.getCard();
					player.addCard(c);
					cards.setText("x" + player.getCards().size());
			        parent.removeViewInLayout(view);
					break;
				case DragEvent.ACTION_DRAG_ENDED:
					setBackgroundColor(Color.TRANSPARENT);
					break;
				default:
					break;
			}
			return true;
	    }
	}
}
