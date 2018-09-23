package com.udacity.gradle.builditbigger;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.pratibimb.jokelib.JokeProviderClass;
import com.udacity.gradle.builditbigger.utils.EndpointsAsyncTask;
import com.udacity.gradle.builditbigger.utils.GCEIdlingResource;


public class MainActivity extends AppCompatActivity
    implements MainActivityFragment.MainFragmentListener {

    //Idling resource. Will be null in production
    @Nullable
    private GCEIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void tellJoke() {
        EndpointsAsyncTask jokeTask = new EndpointsAsyncTask();
        jokeTask.execute(this, mIdlingResource);
    }

    // Only called from test, creates and returns a new GCEIdlingResource
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new GCEIdlingResource();
        }
        return mIdlingResource;
    }
}
