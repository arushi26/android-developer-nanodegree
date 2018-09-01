package com.arushi.bakingapp.CustomMatchers;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.Toolbar;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class ToolbarMatcher {

    public static Matcher<Object> withTitle(final Matcher<String> textMatcher) {
        return new BoundedMatcher<Object, Toolbar>(Toolbar.class) {

            @Override
            protected boolean matchesSafely(Toolbar toolbarLayout) {
                return textMatcher.matches(toolbarLayout.getTitle());
            }

            @Override public void describeTo(Description description) {
                description.appendText("with toolbar title: ");
                textMatcher.describeTo(description);
            }

        };
    }

}
