package com.ensibs.omeca.view;

import com.ensibs.omeca.model.entities.Card;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class CardView extends ImageView{
	Card card;
	private static final float ratio = 1.452f;

	public CardView(Context context, Card card) {
		super(context);
		this.card = card;
		setImageDrawable(
				context.getResources().getDrawable(
						context.getResources().getIdentifier(
								card.getColor()+card.getValue(),
								"drawable",
								context.getApplicationContext().getPackageName()
						)
				)
		);
		DisplayMetrics metrics = context.getApplicationContext().getResources().getDisplayMetrics();
		int height = metrics.heightPixels/4;
		int width = (int) (height/ratio);
		setLayoutParams(new LayoutParams(width, height));
				
	}
	

}
