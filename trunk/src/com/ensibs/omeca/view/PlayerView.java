package com.ensibs.omeca.view;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnDragListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ensibs.omeca.R;
import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.model.entities.Player;
import com.ensibs.omeca.utils.AvatarsList;

public class PlayerView extends RelativeLayout{
	
	private Player player;
	private Context context;
	
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
		DisplayMetrics metrics = context.getApplicationContext().getResources().getDisplayMetrics();
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(metrics.heightPixels/(SIZE), metrics.heightPixels/SIZE);
		
		setLayoutParams(params);
		setOnDragListener(new PlayerDragListener());
	}
	
	public void setPlayer(Player player){
		this.player = player;
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int)(getLayoutParams().width*0.9), (int)(getLayoutParams().height*0.9));
		params.addRule(ALIGN_PARENT_BOTTOM, TRUE);
		params.addRule(CENTER_HORIZONTAL);
		ImageView image = new ImageView(context);
		image.setImageResource(AvatarsList.get(player.getAvatar()));
		addView(image, params);
		
		params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.addRule(ALIGN_PARENT_TOP, TRUE);
		
		TextView textView = new TextView(context);
		textView.setText(player.getName());
		textView.setGravity(Gravity.CENTER_HORIZONTAL);
		textView.setTextColor(Color.DKGRAY);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
		addView(textView, params);
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
					setBackgroundColor(Color.TRANSPARENT);
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
}
