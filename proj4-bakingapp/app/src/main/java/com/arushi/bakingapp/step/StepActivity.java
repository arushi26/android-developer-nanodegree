package com.arushi.bakingapp.step;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.arushi.bakingapp.R;
import com.arushi.bakingapp.data.local.entity.StepEntity;

import java.util.ArrayList;
import java.util.List;
import timber.log.Timber;

public class StepActivity extends AppCompatActivity
    implements StepDetailFragment.StepListener {

    private List<StepEntity> mStepsList;
    private int mInitialPosition;

    private ViewPager mPager;

    public static final String KEY_DATA = "Data";
    public static final String KEY_STEPS = "Steps";
    public static final String KEY_POSITION = "Position";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        Intent intentThatStartedActivity = getIntent();


        if(savedInstanceState!=null){
            Bundle bundle = savedInstanceState.getBundle(KEY_DATA);
            mStepsList = bundle.getParcelableArrayList(KEY_STEPS);
            mInitialPosition = bundle.getInt(KEY_POSITION);
        } else if (intentThatStartedActivity != null
                     && intentThatStartedActivity.hasExtra(KEY_STEPS)) {
            mStepsList = intentThatStartedActivity.getParcelableArrayListExtra(KEY_STEPS);
            try{
                mInitialPosition = intentThatStartedActivity.getIntExtra(KEY_POSITION, 0);
            }catch (Exception e){
                mInitialPosition = 0;
            }
        }

        if(mStepsList==null || mStepsList.size()==0) {
            Toast.makeText(this, getString(R.string.error_no_data), Toast.LENGTH_LONG).show();
            Timber.e( "Steps data not available");
            finish();
        }

        Timber.d("Steps: %s", mStepsList.toString());

        bindPager();
    }

    private void bindPager(){
        mPager = (ViewPager) findViewById(R.id.pager_steps);
        PagerAdapter pagerAdapter = new StepsPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);
        // Show step selected by user while opening activity
        mPager.setCurrentItem(mInitialPosition);
    }

    @Override
    public void onBackPressed() {
        int currentPosition = mPager.getCurrentItem();

        if (currentPosition == 0
                || currentPosition == mInitialPosition ) {
            // Back button, when on 1st  or initial step, calls finish() on this activity
            // and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    public boolean isCurrentFocus(int id) {
        int currentPosition = mPager.getCurrentItem();
        int currentId = mStepsList.get(currentPosition).getId();

        return currentId == id;
    }

    // Pager adapter for Recipe steps
    private class StepsPagerAdapter extends FragmentStatePagerAdapter {
        public StepsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setCurrentStep(mStepsList.get(position));
            return stepDetailFragment;
        }

        @Override
        public int getCount() {
            return mStepsList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return getString(R.string.text_pager_step) + " " + String.valueOf(position);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_POSITION, mInitialPosition);
        bundle.putParcelableArrayList(KEY_STEPS, (ArrayList<? extends Parcelable>) mStepsList);
        outState.putBundle(KEY_DATA, bundle);
        super.onSaveInstanceState(outState);
    }
}
