package com.zetta.pedaja;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;


@SuppressLint("DrawAllocation")
public class PizzaDuasMetades extends View {
	
	private Bitmap overlayBitmap;
	private int overlayWidth;
	public String CorMetade01 = "#CCCCCC";
	public String CorMetade02 = "#AAAAAA";
	
	public PizzaDuasMetades(Context context) {
		super(context);
		overlayBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.piechart_shade, null);		
		this.overlayWidth  = overlayBitmap.getWidth();
		setLayoutParams(new LayoutParams(overlayWidth, overlayWidth));	
	}

	public PizzaDuasMetades(Context context, AttributeSet attrs) {
        super(context, attrs);
	}
	
	
	@SuppressLint("DrawAllocation")
	@Override 
    protected void onDraw(Canvas canvas) {
    	super.onDraw(canvas);   
    	Paint mBgPaints = new Paint();
    	mBgPaints.setAntiAlias(true);
    	mBgPaints.setStyle(Paint.Style.FILL);
    	mBgPaints.setStrokeWidth(0.5f);
    	
    	Paint tPaint = new Paint();
    	tPaint.setAlpha(0);
    	canvas.drawColor(tPaint.getColor());
    	
    	RectF mOvals = new RectF( 0, 0, overlayWidth, overlayWidth); 	 
    	mBgPaints.setColor(Color.parseColor(CorMetade02));
    	canvas.drawArc(mOvals, 270, 180, true, mBgPaints);
    	
    	mBgPaints.setColor(Color.parseColor(CorMetade01));
    	canvas.drawArc(mOvals, 90, 180, true, mBgPaints);     	
    	
    	
    	/* 
    	mBgPaints.setColor(Color.BLUE);
    	canvas.drawArc(mOvals, 270, 180, true, mBgPaints);
    	mBgPaints.setColor(Color.YELLOW);
    	canvas.drawArc(mOvals, 90, 180, true, mBgPaints); *//*

    	mBgPaints.setColor(Color.BLUE);
    	canvas.drawArc(mOvals, 270, 120, true, mBgPaints);
    	mBgPaints.setColor(Color.YELLOW);
    	canvas.drawArc(mOvals, 30, 120, true, mBgPaints);
    	mBgPaints.setColor(Color.RED);
    	canvas.drawArc(mOvals, 150, 120, true, mBgPaints);
	    	*/
    	   	
    	canvas.drawBitmap(overlayBitmap, 0.0f, 0.0f, null); 
    }
	
	
}
