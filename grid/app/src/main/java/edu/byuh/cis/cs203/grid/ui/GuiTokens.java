package edu.byuh.cis.cs203.grid.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;
import android.media.MediaPlayer;

import edu.byuh.cis.cs203.grid.R;


public class GuiTokens implements TickListener {
    private float length;
    private Bitmap token;
    private RectF bounds;
    public PointF velocity,origin;
    public gridPosition gridPos;
    public static int movers = 0;
    public boolean moving;
    public float speed = 30;
    private MediaPlayer drop;
    private MyView myView;

    /**
     * create char for positions
     */
    public class gridPosition {
        public char row;
        public char column;
    }
    /**
     *
     * @param res the images/resources
     * @param size the size of images/resources
     * @param x startX of images/resources
     * @param y startY of images/resources
     * @param b get data from GridButton ArrayList
     * @param id id to determine which image to draw
     */
    public GuiTokens(Resources res, float size, float x, float y, GridButton b, int id, MyView v) {
        this.myView = v;
        length = size;
        token = BitmapFactory.decodeResource(res, id);
        token = Bitmap.createScaledBitmap((token), (int)size, (int)size, true);
        bounds = new RectF(x, y, x+size, y+size);
        gridPos = new gridPosition();
        if(b.label >= '1' && b.label <= '5'){ // when pressing horizontal button
            velocity = new PointF(0, speed);
            origin = new PointF(x,y+size);
            gridPos.column = b.label;
            gridPos.row = 'A';
        } else { // when pressing vertical button
            velocity = new PointF(speed,0);
            origin = new PointF(x+size,y);
            gridPos.row = b.label;
            gridPos.column = '1';
        }
        movers++;
        moving = true;
        drop = MediaPlayer.create(v.getContext(), R.raw.drop);
        drop.setLooping(false);
    }

    public void setColGoal() {
        origin.y += length;
        velocity.y += speed;
        movers++;
        moving = true;
    }

    public void setRowGoal() {
        origin.x += length;
        velocity.x += speed;
        movers++;
        moving = true;
    }

    /**
     * move the token
     */
    public void move() {
        bounds.offset(velocity.x,velocity.y);
        float x1 = origin.x;
        float y1 = origin.y;
        float x2 = bounds.left;
        float y2 = bounds.top;
        float distance = (float)Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2, 2));
        if(distance < speed){
            velocity.set(0,0);
            if (gridPos.row > 'E' || gridPos.column > '5'){
                velocity.y = speed*2;
                if (Prefs.soundEffectOn(myView.getContext())) {
                    drop.start();
                }
            }else{
                bounds.offsetTo(origin.x, origin.y);
            }
            if(moving) {
                movers--;
                moving = false;
            }
        }
    }

    /**
     *
     * @param c draw the token
     */
    public void draw(Canvas c) {

        c.drawBitmap(token, bounds.left, bounds.top, null);
    }

    /**
     * call the move on TickListener
     */
    @Override
    public void onTick() {
        move();
    }

    /**
     * it is where I check if tokens are moving
     * @return
     */
    public static boolean motionCheck(){
        if(movers > 0) {
            return true;
        }
        return false;
    }
    /**
     * check if any token is invisible
     */
    public boolean isInvisible(float screenHeight){
        if(bounds.top > screenHeight){
            return true;
        }
        return false;
    }

    /**
     * reset movers
     */
    static void stopMoving() {
        movers = 0;
    }

}
