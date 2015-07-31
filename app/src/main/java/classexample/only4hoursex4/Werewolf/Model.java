package classexample.only4hoursex4.Werewolf;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.List;
import java.util.Vector;

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

    boolean isCreate = false;
    boolean isVillage = true;

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

}
