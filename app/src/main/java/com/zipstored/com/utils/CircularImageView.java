package com.zipstored.com.utils;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;


public class CircularImageView extends androidx.appcompat.widget.AppCompatImageView {

    private float xRadius = 10;
    private float yRadius = 10;
    private Paint paint = new Paint();

    public CircularImageView(Context context) {
        super(context);
    }

    public CircularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public float getxRadius() {
        return xRadius;
    }

    public void setxRadius(float xRadius) {
        this.xRadius = xRadius;
    }

    public float getyRadius() {
        return yRadius;
    }

    public void setyRadius(float yRadius) {
        this.yRadius = yRadius;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onDraw(Canvas canvas) {
        // java.lang.ClassCastException: android.graphics.drawable.TransitionDrawable cannot be cast
        // to android.graphics.drawable.BitmapDrawable
        BitmapShader shader;
        if (getDrawable() instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) getDrawable();
            // clip
            shader = new BitmapShader(bitmapDrawable.getBitmap(), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            RectF rect = new RectF(0.0f, 0.0f, getWidth(), getHeight());
            int width = bitmapDrawable.getBitmap().getWidth();
            int height = bitmapDrawable.getBitmap().getHeight();
            RectF src = null;
            if (((float) width) / height > 1) {
                src = new RectF(0.0f, 0.0f, height, height);
            } else {
                src = new RectF(0.0f, 0.0f, width, width);
            }
            Matrix matrix = canvas.getMatrix();
            matrix.setRectToRect(src, rect, Matrix.ScaleToFit.CENTER);
            shader.setLocalMatrix(matrix);

            paint.setAntiAlias(true);
            paint.setShader(shader);
            // draw round circle for HeadImage or other
            canvas.drawRoundRect(rect, this.getWidth() / 2, this.getHeight() / 2, paint);
            // canvas.drawRoundRect(rect, xRadius, yRadius / 2, paint);
        }
    }
}
