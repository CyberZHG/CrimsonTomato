package zhaohg.crimson.goal;

import java.util.ArrayList;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import zhaohg.crimson.R;

public class GoalsActivity extends AppCompatActivity {

    private ArrayList<Fragment> fragments;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.action_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(GoalsActivity.this, NewGoalActivity.class);
                startActivity(intent);
            }
        });

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.slidingTab);
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.goal_show_type_unfinished)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.goal_show_type_finished)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.goal_show_type_all)));
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        fragments = new ArrayList<>();
        fragments.add(GoalFragment.newInstance(GoalFragment.SHOW_UNFINISHED));
        fragments.add(GoalFragment.newInstance(GoalFragment.SHOW_FINISHED));
        fragments.add(GoalFragment.newInstance(GoalFragment.SHOW_ALL));
        PagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }
        };
        viewPager.setOffscreenPageLimit(fragments.size());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

}
