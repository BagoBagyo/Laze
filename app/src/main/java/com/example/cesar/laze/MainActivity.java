package com.example.cesar.laze;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayDeque;


public class MainActivity extends ActionBarActivity {
    final static String tag = "LAZE";
    final static int blockGridWidth = 6;
    final static int blockGridHeight = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        LazeView lazeView = new LazeView(this);
        setContentView(lazeView);

        ArrayDeque<Ray> mySources = new ArrayDeque<>();
        mySources.add(new Ray(12, 1, Ray.Type.RED, 225));
        mySources.add(new Ray(1, 4, Ray.Type.RED, 135));

        ArrayDeque<Target> myTargets = new ArrayDeque<>();
        myTargets.add(new Target(1, 0, false));
        myTargets.add(new Target(8, 5, false));
        myTargets.add(new Target(7, 12, false));

        Log.d(tag, "onCreate(): mySources: " + mySources);
        Log.d(tag, "onCreate(): myTargets: " + myTargets);

        LazeGame myLazeGame = new LazeGame(blockGridWidth, blockGridHeight, mySources, myTargets);
        DisplayMetrics d = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(d);

        lazeView.setLazeViewParams(myLazeGame.getBlockGrid(), mySources, myTargets);
        lazeView.invalidate();
        myLazeGame.update();
        //Log.d(tag, "myLazeGame: " + myLazeGame.toString());
        if (myLazeGame.allTargetsHit()) {
            Log.d(tag, "onCreate(): You won! All targets were hit! myTargets: " + myTargets);
        } else {
            Log.d(tag, "onCreate(): You lost! Not all targets were hit. myTargets: " + myTargets);

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
