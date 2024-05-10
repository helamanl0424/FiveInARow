package edu.byuh.cis.cs203.grid.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class MainActivity extends Activity {

    private MyView mv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        String player = i.getStringExtra(SplashActivity.PLAYER_MODE);
        mv = new MyView(this, player);
        setContentView(mv);
    }

    /**
     * pause the music
     */
    @Override
    public void onPause() {

        super.onPause();
        mv.justGotBackgrounded();
    }

    /**
     * resume the music
     */
    @Override
    public void onResume() {

        super.onResume();
        mv.justGotForegrounded();
    }

    /**
     * clean up the memory used by the application
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mv.cleanupBeforeShutdown();
    }
}