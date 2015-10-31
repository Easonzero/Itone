package com.wangyi.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class ImageUtil {
	public static Bitmap toRoundBitmap(Bitmap photo){
		Bitmap output = Bitmap.createBitmap(photo.getWidth(),photo.getHeight(),Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0,0,photo.getWidth(),photo.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = photo.getWidth()/2;
		
		paint.setAntiAlias(true);
		canvas.drawARGB(0,0,0,0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF,roundPx,roundPx,paint);
		
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(photo,rect,rect,paint);
		return output;
	}
}
