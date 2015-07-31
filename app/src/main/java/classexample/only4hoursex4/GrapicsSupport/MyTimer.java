package classexample.only4hoursex4.GrapicsSupport;

/**
 * Created by andying on 7/31/15.
 */

import android.os.Handler;

public class MyTimer {

    private OnMyTimerAlarmListener mCallbackObject = null;

    // Default is 25 mSec update period
    static public final int kRealTimeUpdatePeriod = 25;
    private int mUpdatePeriod;
    private boolean mEnabled = false;
    private int mCountSinceStarted = 0;

    // define a custom Runnable object
    private Handler mFutureTask = new Handler();
    private Runnable mTimerTask = new Runnable() {
        public void run() {
            mCountSinceStarted++;
            if (null != mCallbackObject)
                mCallbackObject.onMyTimerAlarm();

            if (mEnabled)
                mFutureTask.postDelayed(this, mUpdatePeriod);
        }
    };

    public MyTimer(OnMyTimerAlarmListener listener)
    {
        mCallbackObject = listener;
    }


    public void startTimer() {
        mEnabled = true;
        mFutureTask.postDelayed(mTimerTask, mUpdatePeriod);
        mCountSinceStarted = 0;
    }

    public int getTimerCount() { return mCountSinceStarted; }

    public boolean isTimerEnabled() { return mEnabled; }

    public void stopTimer() {
        mEnabled = false;
        mFutureTask.removeCallbacks(mTimerTask);
        mCountSinceStarted = 0;
    }

    public void setTimerPeriod(int milliSecond) {
        mUpdatePeriod = milliSecond;
    }

    public void setOnMyTimerAlarmListener(OnMyTimerAlarmListener l) { mCallbackObject = l; }

}
