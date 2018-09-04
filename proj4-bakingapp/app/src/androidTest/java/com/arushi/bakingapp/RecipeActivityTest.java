/*
 *
 *  *
 *  *  This project was submitted by Arushi Pant as part of the Android Developer Nanodegree at Udacity.
 *  *
 *  *  As part of Udacity Honor code, your submissions must be your own work, hence
 *  *  submitting this project as yours will cause you to break the Udacity Honor Code
 *  *  and the suspension of your account.
 *  *
 *  *  I, the author of the project, allow you to check the code as a reference, but if
 *  *  you submit it, it's your own responsibility if you get expelled.
 *  *
 *  *  Besides the above notice, the MIT license applies and this license notice
 *  *  must be included in all works derived from this project
 *  *
 *  *  Copyright (c) 2018 Arushi Pant
 *  *
 *
 */

package com.arushi.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.arushi.bakingapp.recipe.RecipeActivity;
import com.arushi.bakingapp.utils.PhoneTest;
import com.arushi.bakingapp.utils.TabletTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;

@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest extends BaseTest {
    private static final int DESSERT_ID = 1;
    private static final String DESSERT_NAME = "Nutella Pie";
    private static final int DESSERT_DEFAULT_IMG = R.drawable.oven;

    @Rule
    public ActivityTestRule<RecipeActivity> mActivityTestRule =
            new ActivityTestRule<RecipeActivity>(RecipeActivity.class){
                @Override
                protected Intent getActivityIntent() {
                    // To pass Intent Extras while starting Activity
                    Context targetContext = InstrumentationRegistry
                                                .getInstrumentation()
                                                .getTargetContext();

                    Intent intent = new Intent(targetContext, RecipeActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putInt(RecipeActivity.KEY_RECIPE_ID, DESSERT_ID);
                    bundle.putString(RecipeActivity.KEY_RECIPE_NAME, DESSERT_NAME);
                    bundle.putInt(RecipeActivity.KEY_RECIPE_DEFAULT_IMG, DESSERT_DEFAULT_IMG);
                    intent.putExtra(RecipeActivity.KEY_RECIPE_DATA, bundle);
                    return intent;
                }
            };

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
        super.setUp();
    }

    @Test
    public void checkIngredientsLabelDisplayed(){
        onView(withId(R.id.lbl_ingredients))
                .check(matches(isDisplayed()))
                .check(matches(withText("Ingredients")));
    }

    @Test
    public void checkIngredientsListDisplayed(){
        // At least 1 ingredient displayed
        onView(withId(R.id.rv_ingredients))
                .check(matches(isDisplayed()))
                .check(matches(hasMinimumChildCount(1)));
    }

    @Test
    public void checkServingsLabelDisplayed(){
        onView(withId(R.id.lbl_servings))
                .check(matches(isDisplayed()))
                .check(matches(withText(startsWith("Servings:"))));
    }

    @Test
    public void checkServingsDisplayed(){
        onView(withId(R.id.tv_servings))
                .check(matches(isDisplayed()));
    }

    @Test
    public void checkStepsLabelDisplayed(){
        onView(withId(R.id.lbl_steps))
                .check(matches(isDisplayed()))
                .check(matches(withText("Steps")));
    }

    @Test
    public void checkStepListDisplayed(){
        // At least 1 step displayed
        onView(withId(R.id.rv_steps))
                .check(matches(isDisplayed()))
                .check(matches(hasMinimumChildCount(1)));
    }

    @PhoneTest
    @Test
    public void phone_checkStepNotDisplayed(){
        // Step description should not be displayed
        onView(withId(R.id.tv_description))
                .check(doesNotExist());
    }

    @TabletTest
    @Test
    public void tablet_checkFirstStepDefaultDisplay(){
        // Step description should not be empty
        onView(withId(R.id.tv_description))
                .check(matches(isDisplayed()))
                .check(matches(not(withText(""))));
    }

}
