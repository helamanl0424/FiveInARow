package edu.byuh.cis.cs203.grid.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import edu.byuh.cis.cs203.grid.R;

public class SplashActivity extends Activity {

    private ImageView iv;
    public static final String PLAYER_MODE = "PLAYER_MODE";
    public static final String ONE_PLAYER = "ONE_PLAYER";
    public static final String TWO_PLAYER = "TWO_PLAYER";


    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        iv = new ImageView(this);
        iv.setImageResource(R.drawable.splash);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        setContentView(iv);
    }

    @Override
    public boolean onTouchEvent(MotionEvent m) {
        if (m.getAction() == MotionEvent.ACTION_DOWN) {
            float x = m.getX();
            float y = m.getY();
            float w = iv.getWidth();
            float h = iv.getHeight();
            //When pressing one player button
            if (x > 0.1*w && x < 0.4*w && y > 0.65*h && y < 0.85*h) {
                Intent i = new Intent(this, MainActivity.class);
                i.putExtra(PLAYER_MODE, ONE_PLAYER);
                startActivity(i);
                finish();
            //When pressing two player button
            } else if (x > 0.57*w && x < 0.98*w && y > 0.65*h && y < 0.85*h) {
                Intent i = new Intent(this, MainActivity.class);
                i.putExtra(PLAYER_MODE, TWO_PLAYER);
                startActivity(i);
                finish();
            //When pressing the information button
            } else if (x < 0.19*w && y > 0.93*h) {
                alertDialog();
            //When pressing the preference button
            } else if (x > 0.81*w && y > 0.93*h) {
                Intent i = new Intent(this, Prefs.class);
                startActivity(i);
            }
        }
        return true;
    }

    /**
     * the informative alertDialog
     */
    public void alertDialog() {
        AlertDialog ab = new AlertDialog.Builder(iv.getContext())
            .setTitle(R.string.info_title)
            .setMessage(R.string.info_msg1 +"\n"+
                    R.string.info_msg2 +"\n"+
                    R.string.info_msg3 +"\n"+
                    R.string.info_msg4 +"\n"+
                    R.string.info_msg5)
            .create();
        ab.show();
    }
}
