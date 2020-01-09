package com.randomize.redmadrobots.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.randomize.redmadrobots.collections_screen.CollectionsFragment;
import com.randomize.redmadrobots.home_screen.HomeFragment;
import com.randomize.redmadrobots.R;
import com.randomize.redmadrobots.adapters.FragmentPageAdapter;
import com.randomize.redmadrobots.search_screen.SearchPhotosActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity{

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.view_pager) ViewPager viewPager;

    private FragmentPagerAdapter fragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        new Thread(() -> {
            SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

            boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

            if (isFirstStart) {

                Intent i = new Intent(MainActivity.this, IntroActivity.class);
                startActivity(i);

                SharedPreferences.Editor e = getPrefs.edit();
                e.putBoolean("firstStart", false);
                e.apply();
            }
        }).start();

        fragmentAdapter = new FragmentPageAdapter(getSupportFragmentManager(), 3);

        setSupportActionBar(mToolbar);
        setTitle(R.string.app_name);

        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search){
            startActivity(new Intent(MainActivity.this, SearchPhotosActivity.class));
            return true;
        } else { return super.onOptionsItemSelected(item);}
    }

    private void setupViewPager(ViewPager viewPager){
        FragmentPageAdapter fragmentPagerAdapter =
                new FragmentPageAdapter(getSupportFragmentManager() ,3);
        fragmentPagerAdapter.addFragment(new HomeFragment(), "Home");
        fragmentPagerAdapter.addFragment(new CollectionsFragment(), "Collections");

        viewPager.setAdapter(fragmentPagerAdapter);
    }

}
