package com.example.cesar.laze;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayDeque;


public class MainActivity extends ActionBarActivity {
    final static String tag = "LAZE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayDeque<Ray> mySources = new ArrayDeque<>();
        mySources.add(new Ray(4, 1, Ray.Type.RED, 225));

        ArrayDeque<Target> myTargets = new ArrayDeque<>();
        myTargets.add(new Target(1, 0, false));

        Log.d(tag, "onCreate(): mySources: " + mySources);
        Log.d(tag, "onCreate(): myTargets: " + myTargets);

        LazeGame myLazeGame = new LazeGame(6, 6, mySources, myTargets);

        myLazeGame.update();
        //Log.d(tag, "myLazeGame: " + myLazeGame.toString());
        if (myLazeGame.allTargetsHit()) {
            Log.d(tag, "onCreate(): All targets were hit! You won! myTargets: " + myTargets);
        } else {
            Log.d(tag, "onCreate(): All targets were not hit! myTargets: " + myTargets);

        }

        Log.d(tag, "onCreate(): Finished onCreate");
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
}
