package classexample.only4hoursex4.GrapicsSupport;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.SensorEventListener;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

import classexample.only4hoursex4.R;

//试试看改下代码再push 顺便把mainactivity的extends ActionBarActivity 改成了extends Activity
//上面的bar不见了 应该没事吧
public class MainActivity extends Activity implements SimpleFragment.OnFragmentInteractionListener{

    SimpleFragment mSimpleFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // without this check, we would create a new fragment at each orientation change!
        if (null == savedInstanceState)
            createFragment();

    }

    private void createFragment()
    {
        FragmentManager fManager = getFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();

        mSimpleFragment = new SimpleFragment();

        String wwName = "werewolf.jpg";
        String vgName = "villager.jpg";
        String htName = "hunter.jpg";

        mSimpleFragment.setBitMaps(loadImage(wwName), loadImage(vgName), loadImage(htName));

        // Adding the new fragment
        fTransaction.add(R.id.mainContainer, mSimpleFragment);
        fTransaction.commit();
    }

    private Bitmap loadImage(String fileName)
    {
        //get the name of the picture the user inputed
        String pName = fileName;
        Bitmap bit = null;
        //set up an inputStream
        InputStream bitmapStream=null;
        try {
            //open the file from the assets folder with the given name
            bitmapStream=getAssets().open(pName);
            // getAssets: is the utility for accessing the Asset folder!

            //decode the stream as a bitmap
            bit = BitmapFactory.decodeStream(bitmapStream);

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
        return bit;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
