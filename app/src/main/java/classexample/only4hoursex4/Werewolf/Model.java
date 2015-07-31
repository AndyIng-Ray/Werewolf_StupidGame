package classexample.only4hoursex4.Werewolf;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.List;
import java.util.Vector;

import classexample.only4hoursex4.GraphicsShape.BitmapShape;
import classexample.only4hoursex4.GraphicsShape.HunterShape;
import classexample.only4hoursex4.GraphicsShape.VillagerShape;
import classexample.only4hoursex4.GraphicsShape.WerewolfShape;

/**
 * Created by andying on 7/31/15.
 */
public class Model {
    final int kShapeSize = 100;
    final int kDefaultWorldSize = 200;

    Rect mWorldBound;

    // Region: Model state
    List<VillagerShape> mAllVillagers;
    List<HunterShape> mAllHunters;
    WerewolfShape mWerewolf = null;

    Bitmap mWWmap = null;
    Bitmap mVGmap = null;
    Bitmap mHTmap = null;

    boolean isCreate = true;
    boolean isVillage = true;
    boolean isNight = false;

    OnModelChangeListenr mModelChangeCallback = null;
    public void setOnModelChangeListener(OnModelChangeListenr listener) {
        mModelChangeCallback = listener;
    }
    private void updateListener() {
        if (null != mModelChangeCallback)
            mModelChangeCallback.onModelChange();
    }

    public Model(Bitmap wereWolf, Bitmap villager, Bitmap hunter) {
        mAllVillagers = new Vector<VillagerShape>();
        mAllHunters = new Vector<HunterShape>();

        mWWmap = wereWolf;
        mVGmap = villager;
        mHTmap = hunter;

        mWerewolf = null;
        mWorldBound = new Rect(0, 0, kDefaultWorldSize, kDefaultWorldSize);
    }

    public void touchHere(float x, float y) {
        if (isCreate == true) {
            if (isVillage == true) {
                mAllVillagers.add(new VillagerShape());
            }
            else {
                mAllHunters.add(new HunterShape());
            }
        }
        else {
            int i = 0;
            List tempList;
            if (isVillage == true)
                tempList = mAllVillagers;
            else
                tempList = mAllHunters;

            while (i < tempList.size()) {
                BitmapShape tempShape = (BitmapShape) tempList.get(i);
                if (tempShape.contains((int)x, (int)y) == true) {
                    tempList.remove(i);
                    break;
                }
                i++;
            }
        }
    }

    public void setWWVelocity(float vx, float vy) {
        if (null == mWerewolf)
            return;

        float size = (float) Math.sqrt(vx*vx + vy*vy);

        float factor = size / (40 * 10);

        if (size < Float.MIN_VALUE) {
            vx = 0f;
            vy = 0f;
        } else {
            vx = factor * vx / size;
            vy = factor * vy / size;
        }

        mWerewolf.setVelocity(vx, vy);
    }

    public void draw(Canvas c) {
        if (isNight == true)
            mWerewolf.draw(c);

        for (int i = 0; i < mAllVillagers.size(); i++)
            mAllVillagers.get(i).draw(c);
        for (int i = 0; i < mAllHunters.size(); i++)
            mAllHunters.get(i).draw(c);
    }

    public void updateMode(float ax, float ay) {
        if (null != mWerewolf) {
            mWerewolf.updateVelocity(ax, ay);
            mWerewolf.updateShapeWithVelocity();
            mWerewolf.responseToWorldCollision(mWorldBound);

            for (int i = 0; i < mAllVillagers.size(); i++) {
                VillagerShape villager = mAllVillagers.get(i);
                if (mWerewolf.collideWith(villager) == true) {
                    mAllVillagers.remove(i);
                    break;
                }
            }

            for (int i = 0; i < mAllHunters.size(); i++) {
                HunterShape hunter = mAllHunters.get(i);
                if (mWerewolf.collideWith(hunter) == true) {
                    mAllHunters.remove(i);
                    break;
                }
            }
        }

        updateListener(); //  with number of ducks on screen
    }

    public void setWorldDimension(int w, int h) {
        mWorldBound = new Rect(0, 0, w, h);
    }
}
