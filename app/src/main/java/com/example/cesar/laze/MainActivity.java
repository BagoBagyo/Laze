package com.example.cesar.laze;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayDeque;


public class MainActivity extends ActionBarActivity {
    final static String tag = "LAZE";
    final static int blockGridWidth = 6;
    final static int blockGridHeight = 6;
    LazeGame myLazeGame;
    ArrayDeque<Ray> mySources;
    ArrayDeque<Target> myTargets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //LazeView lazeView = new LazeView(this);
        //lazeView.setVisibility(View.VISIBLE);
        setContentView(R.layout.activity_main);

        //LazeView lazeView = new LazeView(this);
        //setContentView(lazeView);
        //lazeView.invalidate();


        mySources = new ArrayDeque<>();
        mySources.add(new Ray(12, 1, Ray.Type.RED, 225));
        mySources.add(new Ray(2, 1, Ray.Type.RED, 135));

        myTargets = new ArrayDeque<>();
        myTargets.add(new Target(3, 12, false));
        myTargets.add(new Target(8, 5, false));
        myTargets.add(new Target(11, 10, false));

        Log.d(tag, "onCreate(): mySources: " + mySources);
        Log.d(tag, "onCreate(): myTargets: " + myTargets);

        myLazeGame = new LazeGame(blockGridWidth, blockGridHeight, mySources, myTargets);

        DisplayMetrics d = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(d);

        ((LazeView) this.findViewById(R.id.view)).initLazeView(myLazeGame.getBlockGrid(), mySources, myTargets);

        updateLaze();

        Log.d(tag, "onCreate(): Finished onCreate");
    }

    public void updateLaze() {
        myLazeGame.update();
        if (myLazeGame.allTargetsHit()) {
            Toast.makeText(this, "YOU WON!", Toast.LENGTH_LONG).show();
            Log.d(tag, "onCreate(): You won! All targets were hit! myTargets: " + myTargets);
        } else {
            Toast.makeText(this, "NOT ALL TARGETS WERE HIT.", Toast.LENGTH_LONG).show();
            Log.d(tag, "onCreate(): You lost! Not all targets were hit. myTargets: " + myTargets);

        }

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
            updateLaze();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
