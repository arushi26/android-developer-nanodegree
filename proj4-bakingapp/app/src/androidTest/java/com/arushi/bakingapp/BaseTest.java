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

import android.app.Activity;
import com.arushi.bakingapp.utils.PhoneTest;
import com.arushi.bakingapp.utils.TabletTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import java.lang.reflect.Method;

import static org.junit.Assume.assumeTrue;


public class BaseTest {
    public Activity mActivity;

    @Rule
    public TestName testName = new TestName();

    @Before
    public void setUp() throws Exception {
        /* IMP: requires setting mActivity in any class that extends this */
        assertDeviceOrSkip();
    }

    private void assertDeviceOrSkip() {
        try {
            Method m = getClass().getMethod(testName.getMethodName());
            if (m.isAnnotationPresent(TabletTest.class)) {
                assumeTrue(isTablet());
            } else if (m.isAnnotationPresent(PhoneTest.class)) {
                assumeTrue(isPhone());
            }
        } catch (NoSuchMethodException e) {
            // Do nothing
        }
    }

    private boolean isPhone() {
        return !isTablet();
    }

    private boolean isTablet() {
        return mActivity.getResources().getBoolean(R.bool.isTablet);
    }
}
