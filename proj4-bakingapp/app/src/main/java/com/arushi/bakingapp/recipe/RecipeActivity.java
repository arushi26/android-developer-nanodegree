package com.arushi.bakingapp.recipe;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arushi.bakingapp.BApplication;
import com.arushi.bakingapp.R;
import com.arushi.bakingapp.data.local.entity.DessertEntity;
import com.arushi.bakingapp.data.local.entity.IngredientEntity;
import com.arushi.bakingapp.data.local.entity.StepEntity;
import com.arushi.bakingapp.step.StepActivity;
import com.arushi.bakingapp.step.StepDetailFragment;
import com.arushi.bakingapp.utils.GlideApp;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class RecipeActivity extends AppCompatActivity
    implements RecipeFragment.RecipeListener,
               StepDetailFragment.StepListener{
    private int mId;
    private String mName;
    private int mDefaultImgResource;
    private boolean mTwoPane;
    private boolean isRecreated = false;
    private StepEntity mStep;

    public static final String KEY_RECIPE_DATA = "Data";
    public static final String KEY_RECIPE_ID = "Id";
    public static final String KEY_RECIPE_NAME = "Name";
    public static final String KEY_RECIPE_DEFAULT_IMG = "DefaultImg";
    // TODO Accessibility
    // TODO Localization

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent intentThatStartActivity = getIntent();

        if(savedInstanceState!=null){
            try {
                isRecreated = true; // Activity recreated
                Bundle bundle = savedInstanceState.getBundle(KEY_RECIPE_DATA);
                mId = bundle.getInt(KEY_RECIPE_ID);
                mName = bundle.getString(KEY_RECIPE_NAME);
                mDefaultImgResource = bundle.getInt(KEY_RECIPE_DEFAULT_IMG);
            } catch (Exception e) {
                Timber.e("Exception reading savedInstanceState");
            }
        } else if(intentThatStartActivity!=null
                && intentThatStartActivity.hasExtra(KEY_RECIPE_DATA)) {
            Bundle bundle = intentThatStartActivity.getBundleExtra(KEY_RECIPE_DATA);
            mId = bundle.getInt(KEY_RECIPE_ID);
            mName = bundle.getString(KEY_RECIPE_NAME);
            mDefaultImgResource = bundle.getInt(KEY_RECIPE_DEFAULT_IMG);
        } else {
            Toast.makeText(this, getString(R.string.error_no_data), Toast.LENGTH_SHORT)
                    .show();
            finish();
        }

        initViews();
    }

    private void initViews(){
        if (findViewById(R.id.step_container) != null) {
            // This FrameLayout will only initially exist in the two-pane tablet case
            mTwoPane = true;
            this.setTitle(mName);
            if(!isRecreated) {
                showRecipeFragment();
                showStepsFragment();
            }
        } else {
            mTwoPane = false;
            // Postpone the shared element enter transition.
            supportPostponeEnterTransition();
            // Show recipe
            showRecipeFragment();
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
        // Keep false for two-pane layout
        // As no view pager used
        return false;
    }
}
