package classexample.only4hoursex4.GrapicsSupport;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import classexample.only4hoursex4.R;
import classexample.only4hoursex4.Werewolf.Model;
import classexample.only4hoursex4.Werewolf.OnModelChangeListenr;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SimpleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SimpleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SimpleFragment extends Fragment implements
        OnMyTimerAlarmListener, OnModelChangeListenr, SensorEventListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    TextView mCount = null;
    TextView mMode = null;
    Button mCreateButt = null;
    Button mDeleteButt = null;
    Button mPlayButt = null;
    ImageButton mVGButt = null;
    ImageButton mHTButt = null;
    DrawArea mDrawArea = null;

    Model mGame;
    float mAX, mAY;

    final int kUpdatePeriod = 25;
    MyTimer mTimer;

    Bitmap mWWBitmap;
    Bitmap mVGBitmap;
    Bitmap mHTBitmap;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SimpleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SimpleFragment newInstance(String param1, String param2) {
        SimpleFragment fragment = new SimpleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SimpleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_simple, container, false);

        linkGUI2Var(rootView);

        mGame = new Model(mWWBitmap, mVGBitmap, mHTBitmap);
        mGame.setOnModelChangeListener(this); // we want to know when Model count change

        // initialize
        mDrawArea.setModel(mGame);

        // timer ...
        mTimer = new MyTimer(this);
        mTimer.startTimer();

        // setup Accelerometer
        setupAccelerometer();

        setUpGUI();

        return rootView;
    }

    private void linkGUI2Var(View rootView) {
        mCount = (TextView) rootView.findViewById(R.id.count);
        mMode = (TextView) rootView.findViewById(R.id.mode);
        mCreateButt = (Button) rootView.findViewById(R.id.createButton);
        mDeleteButt = (Button) rootView.findViewById(R.id.deleteButton);
        mPlayButt = (Button) rootView.findViewById(R.id.playButton);
        mVGButt = (ImageButton) rootView.findViewById(R.id.villagerImage);
        mHTButt = (ImageButton) rootView.findViewById(R.id.hunterImage);
        mDrawArea = (DrawArea) rootView.findViewById(R.id.DrawArea);
    }

    private void setUpGUI() {
        //mVGButt.setImageBitmap(mVGBitmap);
        //mHTButt.setImageBitmap(mHTBitmap);

        mVGButt.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mGame.setVG();
                    }
                }
        );

        mHTButt.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mGame.setHT();
                    }
                }
        );

        mCreateButt.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mGame.setCreate();
                        mMode.setText("Mode: Create");
                    }
                }
        );

        mDeleteButt.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mGame.setDelete();
                        mMode.setText("Mode: Delete");
                    }
                }
        );
    }

    private void setupAccelerometer() {
        mAX = 0f;
        mAY = 0f;

        mPlayButt.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mGame.CloseEyes();
                        enableAccelerometer(mGame.getIsNight());
                    }
                }
        );
    }

    public void setBitMaps(Bitmap wwBit, Bitmap vgBit, Bitmap htBit) {
        mWWBitmap = wwBit;
        mVGBitmap = vgBit;
        mHTBitmap = htBit;
    }

    private void enableAccelerometer(boolean enabled) {
        SensorManager accelManager = (SensorManager) getActivity().getSystemService(android.content.Context.SENSOR_SERVICE);
        mAX = mAY = 0f;
        if (enabled) {
            //get a sensor manager for accelerometer
            accelManager.registerListener(this,
                    accelManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            accelManager.unregisterListener(this);
        }
    }

    @Override
    public void onModelChange(int count) {
        mCount.setText("count=" + count);
    }

    @Override
    public void onMyTimerAlarm() {
        mGame.updateMode(-mAX, mAY);

        // world probably changed, let's redraw
        mDrawArea.invalidate();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            //accelerometer readings: http://developer.android.com/reference/android/hardware/SensorEvent.html#values
            mAX = event.values[0];
            mAY = event.values[1];
        } else {
            mAX = mAY = 0f;
        }
        mGame.setWWVelocity(-mAX, mAY);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}
