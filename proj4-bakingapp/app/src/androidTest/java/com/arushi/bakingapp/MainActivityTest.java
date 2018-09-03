package com.arushi.bakingapp;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;

import com.arushi.bakingapp.CustomMatchers.CollapsibleToolbarMatcher;
import com.arushi.bakingapp.CustomMatchers.ToolbarMatcher;
import com.arushi.bakingapp.main.MainActivity;
import com.arushi.bakingapp.utils.PhoneTest;
import com.arushi.bakingapp.utils.TabletTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest extends BaseTest {
    private static final String DESSERT_NAME = "Yellow Cake";
    private static final int DESSERT_POSITION = 1;

    // IMPORTANT: Turn Animations OFF in device settings before running this test

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
        super.setUp();
    }

    @Test
    public void checkDessertItemPresent(){
        onView(withId(R.id.rv_desserts))
                .check(matches(hasDescendant(withText(DESSERT_NAME))));
    }

    @PhoneTest
    @Test
    public void phone_ClickDessertItem_OpenRecipeActivity(){
        // Click Dessert Item in list
        onView(withId(R.id.rv_desserts))
               .perform(RecyclerViewActions.actionOnItemAtPosition(DESSERT_POSITION, click()));

        // Check correct recipe opened
        onView(isAssignableFrom(CollapsingToolbarLayout.class))
                .check(matches(CollapsibleToolbarMatcher.withTitle(is(DESSERT_NAME))));
    }

    @TabletTest
    @Test
    public void tablet_ClickDessertItem_OpenRecipeActivity(){
        // Click Dessert Item in list
        onView(withId(R.id.rv_desserts))
                .perform(RecyclerViewActions.actionOnItemAtPosition(DESSERT_POSITION, click()));

        // Check correct recipe opened
        onView(isAssignableFrom(Toolbar.class))
                .check(matches(ToolbarMatcher.withTitle(is(DESSERT_NAME))));
    }

}
