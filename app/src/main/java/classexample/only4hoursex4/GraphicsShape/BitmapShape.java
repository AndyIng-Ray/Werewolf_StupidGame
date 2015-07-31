package classexample.only4hoursex4.GraphicsShape;

/**
 * Created by andying on 7/31/15.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class BitmapShape extends Shape {

    protected Bitmap mBitmap;
    protected float mScaleX, mScaleY;  // easier to implement
    protected Rect mSrcRect, mDstRect; // for drawing the bitmap

    /**
     * Instantiates the bitmap shape
     *
     * @param context
     * @param drawableResource	resource id
     * @param atX 				create at this location-x (center of bitmap)
     * @param atY				create at this location-y (center of bitmap)
     * @param w					width/height of bitmap (in pixels)
     * @param h
     */
    public BitmapShape(Context context, int drawableResource, int atX, int atY, int w, int h) {
        mPaint = null; // override super. do not need this.
        mBitmap = BitmapFactory.decodeResource(context.getResources(), drawableResource);
        initializeBitmap(atX, atY, w, h);
    }

    public BitmapShape(Context context, String fileInAssets, int atX, int atY, int w, int h) {
        mPaint = null;
        mBitmap = Shape.loadBitmap(context, fileInAssets);
        initializeBitmap(atX, atY, w, h);
    }

    public BitmapShape(Bitmap b, int atX, int atY, int w, int h) {
        mPaint = null;
        mBitmap = b;
        initializeBitmap(atX, atY, w, h);
    }

    private void initializeBitmap(int x, int y, int w, int h) {
        mBitmap = Bitmap.createScaledBitmap(mBitmap, w, h, false);
        mSrcRect = new Rect(0, 0, w, h);
        mDstRect = new Rect();
        mScaleX = mScaleY = 1.0f;
        moveTo(x, y);
    }

    // geometry
    @Override
    public void moveTo(float x, float y) {
        mX = x;
        mY = y;
        computeDstRect();
    }

    public void moveBy(float x, float y) {
        mX += x;
        mY += y;
        computeDstRect();
    }

    @Override
    public void setScale(float scale)
    {
        mScaleX = mScaleY = scale;
        computeDstRect();
    }

    @Override
    public void setSize(int w, int h) {
        mScaleX = (float)(w) / (float)(mBitmap.getWidth());
        mScaleY = (float) (h) / (float)(mBitmap.getHeight());
        computeDstRect();
    }


    @Override
    public Rect getBound() { return mDstRect; }

    public float getWidth() { return mSrcRect.right; }
    public float getHeight() { return mSrcRect.bottom; }

    // drawing
    @Override
    public void draw(Canvas c) {
        c.drawBitmap(mBitmap, mSrcRect, mDstRect, null);
    }

    private void computeDstRect() {
        // where we want to draw:
        //      center is located at (x, y)
        int w = (int) (mScaleX * mBitmap.getWidth());
        int h = (int) (mScaleY * mBitmap.getHeight());
        int left = (int) (mX - (w/2));
        int top = (int) (mY - (h/2));
        mDstRect.set(left, top, left+w, top+h);
    }
}
