package hrishikesh.com.smartparking.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hrishikesh.com.smartparking.Feed_Fragment;
import hrishikesh.com.smartparking.GeoFenceFragment;
import hrishikesh.com.smartparking.ParkingFragment;
import hrishikesh.com.smartparking.ProfileActivity;
import hrishikesh.com.smartparking.R;
import hrishikesh.com.smartparking.Utils.SharedPreferenceMethods;


public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (SharedPreferenceMethods.getString(MainActivity.this, SharedPreferenceMethods.IS_LOGGED_IN).equals("yes")) {
            init();
        }
        else if (SharedPreferenceMethods.getString(MainActivity.this, SharedPreferenceMethods.IS_LOGGED_IN).equals("no")) {
            startActivity(new Intent(MainActivity.this, LoginRegisterChoose.class));
            finish();
        }
        else {
            Toast.makeText(MainActivity.this, "Login Error. Report!", Toast.LENGTH_SHORT).show();
        }
    }

    void init() {
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feeds, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }
        if (id == R.id.action_logout) {
            SharedPreferenceMethods.setString(MainActivity.this, SharedPreferenceMethods.IS_LOGGED_IN, "no");
            startActivity(new Intent(MainActivity.this, LoginRegisterChoose.class));
            finish();
            return true;
        }
        /*
        else if (id == R.id.action_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        }
        */

        return super.onOptionsItemSelected(item);
    }

    // Setting fragments in the top bar
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GeoFenceFragment(), "Pedestrian");
        adapter.addFragment(new Feed_Fragment(), "Firebase feed");
        adapter.addFragment(new ParkingFragment(), "Parking - iot");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5 && keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(MainActivity.this, "back pressed", Toast.LENGTH_SHORT).show();
    }
}
