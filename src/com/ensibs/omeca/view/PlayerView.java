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
		name = new TextView(context);
		cards = new TextView(context);
		DisplayMetrics metrics = context.getApplicationContext().getResources().getDisplayMetrics();
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(metrics.heightPixels/(SIZE), metrics.heightPixels/SIZE);
		
		setLayoutParams(params);
		setOnDragListener(new PlayerDragListener());
	}
	
	public void setPlayer(Player player, boolean isMe){
		if(isMe)
			setOnDragListener(null);
		this.player = player;
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int)(getLayoutParams().width*0.9), (int)(getLayoutParams().height*0.9));
		params.addRule(ALIGN_PARENT_BOTTOM, TRUE);
		params.addRule(CENTER_HORIZONTAL);
		ImageView image = new ImageView(context);
		image.setImageResource(AvatarsList.get(player.getAvatar()));
		addView(image, params);
		
		params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.addRule(ALIGN_PARENT_TOP, TRUE);
		
		name.setText(player.getName());
		name.setGravity(Gravity.CENTER_HORIZONTAL);
		name.setTextColor(Color.LTGRAY);
		name.setTypeface(null, Typeface.BOLD);
		name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
		
		addView(name, params);
		
		if (!isMe) {			
			params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params.addRule(ALIGN_PARENT_BOTTOM, TRUE);
			
			cards.setText("x" + player.getCards().size());
			cards.setGravity(Gravity.CENTER_HORIZONTAL);
			cards.setTextColor(Color.WHITE);
			cards.setTypeface(null, Typeface.BOLD);
			cards.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
			
			addView(cards, params);
		} else {
			name.setTextColor(Color.DKGRAY);
			name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
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
					name.setTextColor(Color.WHITE);
					break;
				case DragEvent.ACTION_DRAG_EXITED:
					setBackgroundColor(Color.TRANSPARENT);
					name.setTextColor(Color.DKGRAY);
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
					name.setTextColor(Color.DKGRAY);
					break;
				default:
					break;
			}
			return true;
	    }
	}
}
