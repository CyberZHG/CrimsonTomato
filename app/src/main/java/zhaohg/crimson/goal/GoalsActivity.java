package zhaohg.crimson.goal;

import java.util.ArrayList;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import zhaohg.crimson.R;
import zhaohg.crimson.sliding.SlidingFragment;
import zhaohg.crimson.sliding.SlidingPagerAdapter;
import zhaohg.crimson.sliding.SlidingTabLayout;

public class GoalsActivity extends AppCompatActivity {

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final SlidingTabLayout slidingTab = (SlidingTabLayout) findViewById(R.id.slidingTab);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ArrayList<SlidingFragment> fragments = new ArrayList<>();
        fragments.add(GoalFragment.newInstance(GoalFragment.SHOW_UNFINISHED));
        fragments.add(GoalFragment.newInstance(GoalFragment.SHOW_FINISHED));
        fragments.add(GoalFragment.newInstance(GoalFragment.SHOW_ALL));
        SlidingPagerAdapter pagerAdapter = new SlidingPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setOffscreenPageLimit(fragments.size());
        viewPager.setAdapter(pagerAdapter);
        slidingTab.setViewPager(viewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_goals, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.menu_item_new_goal: {
                    Intent intent = new Intent();
                    intent.setClass(GoalsActivity.this, NewGoalActivity.class);
                    startActivity(intent);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
