package com.nodomain.manyface.ui.customviews;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.nodomain.manyface.R;


public class RoundedImageView extends ImageView {

    private final Paint paint = new Paint();
    private final Xfermode xfermodeSrcIn = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    private final Paint strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private boolean withStroke = false;

    public RoundedImageView(Context context) {
        super(context);
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RoundedImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            return;
        }

        Bitmap bitmap = drawableToBitmap(drawable);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int smallerSide = width > height ? height : width;
        int radius = smallerSide / 2;

        Bitmap bitmapCopy = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap croppedBitmap = cropBitmapByCircle(bitmapCopy, width, height, radius);
        canvas.drawBitmap(croppedBitmap, 0, 0, null);

        if (withStroke) {
            canvas.drawCircle(width / 2, height / 2, radius, strokePaint);
        }
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RoundedImageView);

        int strokeColor = a.getColor(R.styleable.RoundedImageView_strokeColor, 0);
        float strokeWidth = a.getFloat(R.styleable.RoundedImageView_strokeWidth, 0);

        a.recycle();

        if (strokeWidth > 0) {
            withStroke = true;
            strokePaint.setStyle(Paint.Style.STROKE);
            strokePaint.setColor(strokeColor);
            strokePaint.setStrokeWidth(strokeWidth);
        }
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;

            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private Bitmap cropBitmapByCircle(Bitmap bitmap, int width, int height, int radius) {
        Bitmap outputBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(outputBitmap);

        paint.reset();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);

        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(width / 2, height / 2, radius, paint);

        paint.setXfermode(xfermodeSrcIn);

        canvas.drawBitmap(bitmap, 0, 0, paint);

        return outputBitmap;
    }
}
