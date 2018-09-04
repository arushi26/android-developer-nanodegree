package com.arushi.bakingapp.recipe;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.arushi.bakingapp.R;
import com.arushi.bakingapp.data.local.entity.StepEntity;
import com.arushi.bakingapp.step.StepActivity;
import com.arushi.bakingapp.step.StepDetailFragment;
import java.util.ArrayList;
import timber.log.Timber;

public class RecipeActivity extends AppCompatActivity
    implements RecipeFragment.RecipeListener,
               StepDetailFragment.StepListener{
    private int mId;
    private String mName;
    private int mDefaultImgResource;
    private boolean mTwoPane;
    private boolean mIsRecreated = false;
    private StepEntity mStep;

    public static final String KEY_RECIPE_DATA = "Data";
    public static final String KEY_RECIPE_ID = "Id";
    public static final String KEY_RECIPE_NAME = "Name";
    public static final String KEY_RECIPE_DEFAULT_IMG = "DefaultImg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Postpone the shared element enter transition.
        supportPostponeEnterTransition();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent intentThatStartActivity = getIntent();

        if(savedInstanceState!=null){
            // Activity recreated
            try {
                mIsRecreated = true;
                Bundle bundle = savedInstanceState.getBundle(KEY_RECIPE_DATA);
                mId = bundle.getInt(KEY_RECIPE_ID);
                mName = bundle.getString(KEY_RECIPE_NAME);
                mDefaultImgResource = bundle.getInt(KEY_RECIPE_DEFAULT_IMG);
            } catch (Exception e) {
                Timber.e("Exception reading savedInstanceState");
            }
        } else if(intentThatStartActivity!=null
                && intentThatStartActivity.hasExtra(KEY_RECIPE_DATA)) {
            // Get data from intent that started activity
            Bundle bundle = intentThatStartActivity.getBundleExtra(KEY_RECIPE_DATA);
            mId = bundle.getInt(KEY_RECIPE_ID);
            mName = bundle.getString(KEY_RECIPE_NAME);
            mDefaultImgResource = bundle.getInt(KEY_RECIPE_DEFAULT_IMG);
        }

        if(mName==null) {
            Toast.makeText(this, getString(R.string.error_no_data), Toast.LENGTH_SHORT)
                    .show();
            finish();
        }

        initViews();
    }

    private void initViews(){
        /* Display according to device */
        if (findViewById(R.id.step_container) != null) {
            // No transition animation
            supportStartPostponedEnterTransition();
            // This FrameLayout will only initially exist in the two-pane tablet case
            mTwoPane = true;
            this.setTitle(mName);
            if(!mIsRecreated) {
                showRecipeFragment();
                showStepsFragment();
            }
        } else {
            mTwoPane = false;
            // Show recipe
            if(!mIsRecreated) {
                showRecipeFragment();
            } else {
                // No transition animation
                supportStartPostponedEnterTransition();
            }
        }
    }

    private void showRecipeFragment() {
        RecipeFragment recipeFragment = new RecipeFragment();
        recipeFragment.setRecipeData(mId, mDefaultImgResource);
        recipeFragment.setTwoPane(mTwoPane);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if(fragmentManager.findFragmentById(R.id.recipe_container)!=null){
            fragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.recipe_container, recipeFragment)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.recipe_container, recipeFragment)
                    .commit();
        }
    }

    @Override
    public void showStepDetail(ArrayList<StepEntity> stepEntities, int position) {
        if(mTwoPane){
            mStep = stepEntities.get(position);
            showStepsFragment();
        } else {
            Intent intent = new Intent(this, StepActivity.class);
            intent.putExtra(StepActivity.KEY_DESSERT_NAME, mName);
            intent.putParcelableArrayListExtra(StepActivity.KEY_STEPS, stepEntities);
            intent.putExtra(StepActivity.KEY_POSITION, position);
            startActivity(intent);
        }
    }

    private void showStepsFragment() {
        if(mStep==null) return;
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setCurrentStep(mStep);
        stepDetailFragment.setTwoPane(mTwoPane);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if(fragmentManager.findFragmentById(R.id.step_container)!=null){
            fragmentManager.beginTransaction()
                    .replace(R.id.step_container, stepDetailFragment)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .add(R.id.step_container, stepDetailFragment)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_RECIPE_ID, mId);
        bundle.putString(KEY_RECIPE_NAME, mName);
        bundle.putInt(KEY_RECIPE_DEFAULT_IMG, mDefaultImgResource);

        outState.putBundle(KEY_RECIPE_DATA, bundle);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean isCurrentFocus(int id) {
        // Method required to be over-ridden for StepDetailFragment listener
        // Keep false for two-pane layout as no view pager is used
        return false;
    }
}
