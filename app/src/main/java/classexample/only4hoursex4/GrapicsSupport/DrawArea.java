package classexample.only4hoursex4.GrapicsSupport;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import classexample.only4hoursex4.Werewolf.Model;

/**
 * Created by andying on 7/31/15.
 */
public class DrawArea extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    SurfaceHolder mDrawSurface = null;
    Context mResources = null;

    int mWidth, mHeight; // Width and Height of the drawing area

    Model mGame;

    public DrawArea(Context context) {
        super(context);
        initSurface(context);
    }

    public DrawArea(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initSurface(context);
    }

    private void initSurface(Context context)
    {
        mDrawSurface = getHolder();
        // enable the calling of SurfaceHolder.Callback functions!
        mDrawSurface.addCallback(this);
        setOnTouchListener(this);
    }

    void setModel(Model m) { mGame = m; }

    @Override
    protected void onDraw(Canvas c) {
        if (this.isInEditMode())
            return;

        mGame.draw(c);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setWillNotDraw(false);
        setBackgroundColor(Color.GRAY);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mWidth = width;
        mHeight = height;
        mGame.setWorldDimension(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float touchX, touchY;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchX = event.getX();
                touchY = event.getY();

                Log.d("MyTouch", touchX + " " + touchY);
                mGame.touchHere(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        invalidate();  // forces redraw!
        return true; // event handled

    }
}
