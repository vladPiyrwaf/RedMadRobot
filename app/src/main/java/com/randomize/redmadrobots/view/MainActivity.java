package com.randomize.redmadrobots.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.randomize.redmadrobots.R;
import com.randomize.redmadrobots.adapters.FragmentPageAdapter;
import com.randomize.redmadrobots.models.Photo;

public class MainActivity extends AppCompatActivity{

    private ViewPager viewPager;
    private Toolbar mToolbar;
    private FragmentPagerAdapter fragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentAdapter = new FragmentPageAdapter(getSupportFragmentManager(), 3);
        viewPager = findViewById(R.id.view_pager);
        mToolbar = findViewById(R.id.toolbar);

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
            startActivity(new Intent(MainActivity.this, SearchPhotoActivity.class));
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
