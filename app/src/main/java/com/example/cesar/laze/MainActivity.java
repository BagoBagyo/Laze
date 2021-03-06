package com.example.cesar.laze;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayDeque;
import java.util.Observable;
import java.util.Observer;


public class MainActivity extends ActionBarActivity implements Observer {
    final static String tag = "LAZE";
    final static int blockGridWidth = 7;//7,5,5 //6,4,4 //5,3,3
    final static int blockGridHeight = 10;//10,8,6 //9,7,5 //7,5,3
    LazeGame myLazeGame;
    ArrayDeque<Ray> mySources;
    ArrayDeque<Target> myTargets;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mp = MediaPlayer.create(getApplicationContext(), R.raw.click);
        mp.setVolume((float) .1, (float) .1);

        mySources = new ArrayDeque<>();
        mySources.add(new Ray(10, 5, Ray.Type.RED, 225));
        mySources.add(new Ray(4, 9, Ray.Type.RED, 135));

        myTargets = new ArrayDeque<>();
        myTargets.add(new Target(3, 8, false));
        myTargets.add(new Target(8, 5, false));
        myTargets.add(new Target(10, 9, false));

        Log.d(tag, "onCreate(): mySources: " + mySources);
        Log.d(tag, "onCreate(): myTargets: " + myTargets);

        myLazeGame = new LazeGame(blockGridWidth, blockGridHeight, mySources, myTargets);

        DisplayMetrics d = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(d);
        ((LazeView) this.findViewById(R.id.view)).initLazeView(myLazeGame.getBlockGrid(), mySources, myTargets);
        ((LazeView) this.findViewById(R.id.view)).blockDroppedObservable.addObserver(this);

        updateLaze();

        Log.d(tag, "onCreate(): Finished onCreate");
    }

    public void updateLaze() {
        myLazeGame.update();
        mp.start();
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

    @Override
    public void update(Observable observable, Object data) {
        Log.d(tag, "MainActivity.update");
        updateLaze();
    }
}
