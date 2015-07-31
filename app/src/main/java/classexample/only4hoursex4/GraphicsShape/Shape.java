package classexample.only4hoursex4.GraphicsShape;

/**
 * Created by andying on 7/31/15.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.io.IOException;
import java.io.InputStream;


abstract public class Shape {
    protected Paint mPaint;	// for drawing this Shape;
    protected float mX, mY; // position of the shape

    protected float mVx, mVy; // speed in the x and y direction

    public Shape() {
        mPaint = new Paint();
        mX = 0f;
        mY = 0f;
    }

    // Region Velocity support
    /**
     *
     */
    public void updateShapeWithVelocity() {
        mX += mVx;
        mY += mVy;
        moveTo(mX, mY);
    }
    public void setVelocity(float vx, float vy) {
        mVx = vx;
        mVy = vy;
    }
    public void updateVelocity(float dvx, float dvy) {
        mVx += dvx;
        mVy += dvy;
    }
    public float getVelocityX() { return mVx; }
    public float getVelocityY() { return mVy; }
    public void setVelocityX(float vx) { mVx = vx; }
    public void setVelocityY(float vy) { mVy = vy; }
    // EndRegion

    // Region: geometry
    public float getX() { return mX; }
    public float getY() { return mY; }

    /**
     * Moves the shape to location (x,y)
     *
     * @param x the x coordinate of location to be moved to
     * @param y the y coordinate of the location to be moved to
     */
    public void moveTo(float x, float y) { mX = x; mY = y; }

    /**
     * Sets the size of the Shape to w x h
     *
     * @param w new width of shape
     * @param h new height of shape
     */
    abstract public void setSize(int w, int h);

    /**
     * Sets the scale factor of current shape
     *
     * @param scale new scale factor
     */
    abstract public void setScale(float scale);

    /**
     * Gets the bound (axis-aligned) for the shape
     *
     * @return the rectangle that bounds the shape
     */
    abstract public Rect getBound();
    // EndRegion

    // Region: drawing
    /**
     * Draws the shape onto the Canvas
     *
     * @param c the canvas to be drawn to
     */
    abstract public void draw(Canvas c);

    /**
     * Sets the color of the shape
     *
     * @param c the color
     */
    public void setColor(int c) { mPaint.setColor(c); }

    /**
     * Sets the width of the stroke when drawing the shape
     *
     * @param w width of the stroke
     */
    public void setStrokeWidth(int w) { mPaint.setStrokeWidth(w); }
    // EndRegion

    // Region simple behavior
    /**
     * Attempts to select shape at (x,y), defualt behavior is that
     * if (x,y) is inside bound, then selected.
     * @param x
     * @param y
     * @return true if (x,y) is inside the bound
     */
    public boolean selectShapeAt(float x, float y)
    {
        return getBound().contains((int)x, (int)y);
    }

    /**
     * Intersects two shapes
     *
     * @param s another shape to be intersects with current shape
     * @return true if the bounds of the two shapes touch
     */
    public boolean intersects(Shape s) {
        Rect yourBound = s.getBound();
        Rect myBound = getBound();
        return (Rect.intersects(yourBound, myBound));
    }

    public boolean contains(int x, int y) {
        Rect b = getBound();
        return b.contains(x, y);
    }
    // EndRegion

    static public Bitmap loadBitmap(Context context, String fileInAssets) {
        InputStream bitmapStream=null;
        Bitmap b = null;
        try {
            //open the file from the assets folder with the given name
            bitmapStream=context.getAssets().open(fileInAssets);

            //decode the stream as a bitmap
            b = BitmapFactory.decodeStream(bitmapStream);
            //set up an inputStream
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(bitmapStream!=null)
                try {
                    //close the inputstream if it was loaded successfully
                    bitmapStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return b;
    }
}