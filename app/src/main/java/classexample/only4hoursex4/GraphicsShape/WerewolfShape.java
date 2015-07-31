package classexample.only4hoursex4.GraphicsShape;

import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * Created by andying on 7/31/15.
 */
public class WerewolfShape extends BitmapShape {
    static public final int kInsideWorldBound = 0x0;
    static public final int kOutsideWorldBound = 0x10;
    static public final int kCollideLeft = 0x1;
    static public final int kCollideRight = 0x2;
    static public final int kCollideTop = 0x4;
    static public final int kCollideBottom = 0x8;

    public WerewolfShape(Bitmap b, int atX, int atY, int w, int h) {
        super(b, atX, atY, w, h);
    }

    public void responseToWorldCollision(Rect worldBound) {

        int status = collideWithWorld(worldBound);

        if (status == kInsideWorldBound)
            return;

        switch (status) {
            case kCollideBottom:
                // move ourself back up to the top of the world bound
                moveTo(getX(), worldBound.bottom-getHeight()/2);
                flipVY();
                break;

            case kCollideTop:
                moveTo(getX(), getHeight()/2);
                flipVY();
                break;

            case kCollideLeft:
                moveTo(getWidth()/2, getY());
                flipVX();
                break;

            case kCollideRight:
                moveTo(worldBound.right-getWidth()/2, getY());
                flipVX();
                break;

            case kOutsideWorldBound:
                break;
        }
    }

    public boolean collideWith(Shape s) {
        if (intersects(s)) {
            return true;
        }
        return false;
    }

    private void flipVX() {
        float vx = -getVelocityX();
        setVelocityX(vx);
    }

    private void flipVY() {
        float vy = -getVelocityY();
        setVelocityY(vy);
    }

    protected int collideWithWorld(Rect worldBound) {
        Rect r = getBound();

        if (worldBound.contains(r))
            return kInsideWorldBound;

        Rect intersect = new Rect();
        int returnStatus = kOutsideWorldBound;

        if (intersect.setIntersect(worldBound, r)) { // returns the intersection of worldBound and r
            returnStatus = kInsideWorldBound;
            if (intersect.left == 0) {// this is the left bound
                returnStatus |= kCollideLeft;	// what is the difference between these two?
                returnStatus = kCollideLeft;
            }

            if (intersect.right == worldBound.right) {
                returnStatus |= kCollideRight;
                returnStatus = kCollideRight;
            }

            if (intersect.top == worldBound.top) {
                returnStatus |= kCollideTop;
                returnStatus = kCollideTop;
            }

            if (intersect.bottom == worldBound.bottom) {
                returnStatus |= kCollideBottom;
                returnStatus = kCollideBottom;
            }
        }
        return returnStatus;
    }
}
