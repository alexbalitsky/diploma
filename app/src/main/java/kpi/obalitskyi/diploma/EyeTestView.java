package kpi.obalitskyi.diploma;

/**
 * Created by obalitskyi on 5/30/17.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class EyeTestView extends View {
    private Bitmap  mBitmap = null;
    private Canvas mCanvas = null;
    private Paint mBitmapPaint, mPaint;
    private int resource_id = 0;

    private float mOldX;
    private float mOldY;

    // constructor
    public EyeTestView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        mBitmapPaint = new Paint(Paint.DITHER_FLAG);

        mPaint = new Paint(Paint.DITHER_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        // mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(context.getSharedPreferences(PrefsActivity.default_preferences_name,
                Context.MODE_PRIVATE).getInt("stroke_width", 10));
    }

    public void setBitmapResource(int resource_id)
    {
        this.resource_id = resource_id;
        if((getWidth() > 0) && (getHeight() > 0))
            setBitmap(getWidth(), getHeight());

        invalidate();
    }

    // result bitmap
    public Bitmap getBitmap()
    {
        return mBitmap;
    }

    private void setBitmap(int w, int h)
    {
        // make scaled bitmap from resource

        // get bitmap from resources and scale it for prevent out-of-memory errors
        Bitmap bitmap_decoded = decodeBitmap(resource_id, w, h);

        // make bitmap with exace size for to fit allowed space
        Bitmap bitmap_scaled = Bitmap.createScaledBitmap(bitmap_decoded, w, h, true);
        // free memory
        bitmap_decoded.recycle();

        // make mutable bitmap, so we can change it
        mBitmap = bitmap_scaled.copy(Bitmap.Config.ARGB_8888, true);
        // free memory
        bitmap_scaled.recycle();

        // set draw canvas
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if(resource_id != 0)
            setBitmap(w, h);

        invalidate();
    }

    // draw the view
    @Override
    protected void onDraw(Canvas canvas) {
        if((mBitmap != null) || (resource_id != 0)) {
            if(mBitmap == null)
                setBitmap(getWidth(), getHeight());
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if(mCanvas == null)
            return false;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mCanvas.drawPoint(x, y, mPaint);
                mOldX = x;
                mOldY = y;
                // repaint bitmap
                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                if((mOldY != -1) && (mOldY != -1) && ((x != mOldX) || (y != mOldY)))
                    mCanvas.drawLine(mOldX, mOldY, x, y, mPaint);
                else mCanvas.drawPoint(x, y, mPaint);
                mOldX = x;
                mOldY = y;

                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                mOldX = -1;
                mOldX = -1;
                break;
        }
        return true;
    }

    // for prevent out-of-memory errors we need to lower bitmap size based on screen size
    public Bitmap decodeBitmap(int resId, int reqWidth, int reqHeight)
    {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(getResources(), resId, options); // crashes here
    }

    // got from http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
