package com.ensibs.omeca.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ensibs.omeca.R;
import com.ensibs.omeca.model.entities.Player;

public class PlayerView extends RelativeLayout{
	
	private Player player;
	private Context context;
	
	public static final int SIZE = 8;

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
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(metrics.heightPixels/SIZE, metrics.heightPixels/SIZE);
		setLayoutParams(params);
		setBackgroundResource(R.drawable.player);
		ImageView image = new ImageView(context);
		image.setImageResource(R.drawable.avatar_alien);
		addView(image);
		TextView textView = new TextView(context);
		textView.setText("Nom de joueur");
		addView(textView);
	}

}
