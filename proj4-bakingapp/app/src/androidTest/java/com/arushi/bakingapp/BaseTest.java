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
