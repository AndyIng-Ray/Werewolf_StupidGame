package classexample.only4hoursex4.Werewolf;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.widget.Toast;

import java.text.DecimalFormat;
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
    final int kShapeSize = 75;
    final int kDefaultWorldSize = 200;

    Rect mWorldBound;

    Paint paint = null;

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

    int mTime = 0;

    OnModelChangeListener mModelChangeCallback = null;
    public void setOnModelChangeListener(OnModelChangeListener listener) {
        mModelChangeCallback = listener;
    }
    private void updateListener() {
        if (null != mModelChangeCallback)
            mModelChangeCallback.onModelChange(mAllHunters.size() + mAllVillagers.size());
    }

    public Model(Bitmap wereWolf, Bitmap villager, Bitmap hunter) {
        mAllVillagers = new Vector<VillagerShape>();
        mAllHunters = new Vector<HunterShape>();

        mWWmap = wereWolf;
        mVGmap = villager;
        mHTmap = hunter;

        mWerewolf = null;
        mWorldBound = new Rect(5, 5, kDefaultWorldSize - 5, kDefaultWorldSize - 5);

        paint = new Paint();
        paint.setColor(0xff504031);
        paint.setTextSize(35);
    }

    public void CloseEyes() {
        if (isNight == true) {
            isNight = false;
        }
        else {
            isNight = true;
            mTime = 0;
            if (mWerewolf != null) {
                mWerewolf = null;
            }
            mWerewolf = new WerewolfShape(mWWmap, 325, 350, kShapeSize, kShapeSize);
        }
    }

    public void touchHere(float x, float y) {
        if (isCreate == true) {
            if (isVillage == true) {
                mAllVillagers.add(new VillagerShape(mVGmap, (int)x, (int)y, kShapeSize, kShapeSize));
            }
            else {
                mAllHunters.add(new HunterShape(mHTmap, (int)x, (int)y, kShapeSize, kShapeSize));
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

//    public void setWWVelocity(float vx, float vy) {
//        if (null == mWerewolf || isNight == false)
//            return;
//
//        float size = (float) Math.sqrt(vx*vx + vy*vy);
//
//        float factor = size / ( 1);
//
//        if (size < Float.MIN_VALUE) {
//            vx = 0f;
//            vy = 0f;
//        } else {
//            vx = factor * vx / size;
//            vy = factor * vy / size;
//        }
//
//        mWerewolf.setVelocity(vx, vy);
//    }

    public void draw(Canvas c) {
        if (null != mWerewolf)
            mWerewolf.draw(c);

        for (int i = 0; i < mAllVillagers.size(); i++)
            mAllVillagers.get(i).draw(c);
        for (int i = 0; i < mAllHunters.size(); i++)
            mAllHunters.get(i).draw(c);

        if (isNight == true) {
            DecimalFormat decimalFormat = new DecimalFormat(".0");//构造方法的字符格式这里如果小数不足2位,会以0补足.
            String p = decimalFormat.format(mTime / 1000.0);
            String textTime = "Timer: " + p + "s";
            c.drawText(textTime, 13, 750, paint);
        }
    }

    final float kAccelScale = 0.05f;

    public void updateMode(float ax, float ay) {
        if (null != mWerewolf && isNight == true) {
            mWerewolf.updateVelocity(kAccelScale * ax, kAccelScale * ay);
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
        if (isNight == true)
            mTime += 25;

        updateListener(); //  with number of ducks on screen
    }

    public void setWorldDimension(int w, int h) {
        mWorldBound = new Rect(0, 0, w, h);
    }

    public boolean getIsNight() {
        return isNight;
    }

    public int getTime() {
        return mTime;
    }

    public void setCreate() {
        isCreate = true;
    }

    public void setDelete() {
        isCreate = false;
    }

    public void setVG() {
        isVillage = true;
    }

    public void setHT() {
        isVillage = false;
    }

    public void clearModel() {
        mWerewolf = null;
        mAllVillagers.clear();
        mAllHunters.clear();
        isNight = false;
    }
}
