package classexample.only4hoursex4.GrapicsSupport;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.hardware.SensorEventListener;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import classexample.only4hoursex4.R;

//试试看改下代码再push 顺便把mainactivity的extends ActionBarActivity 改成了extends Activity
//上面的bar不见了 应该没事吧
public class MainActivity extends Activity {

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

        // Adding the new fragment
        fTransaction.add(R.id.mainContainer, mSimpleFragment);
        fTransaction.commit();
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
