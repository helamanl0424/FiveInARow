package edu.byuh.cis.cs203.grid.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;

import edu.byuh.cis.cs203.grid.R;

public class GridButton {
    private Bitmap buttonImage, releasedButtonImage;
    public RectF bounds;
    public char label;
    private boolean pressed;

    /**
     *
     * @param res  the images/resources
     * @param size the size of images/resources
     * @param x startX of images/resources
     * @param y startY of images/resources
     * @param l label of images/resources
     */
    public GridButton(Resources res, float size, float x, float y, char l) {
        label = l;
        buttonImage = BitmapFactory.decodeResource(res, R.drawable.slightly_smiling_face_emoji);
        buttonImage = Bitmap.createScaledBitmap((buttonImage), (int)size, (int)size, true);
        releasedButtonImage = BitmapFactory.decodeResource(res, R.drawable.heart_eyes_emoji3);
        releasedButtonImage = Bitmap.createScaledBitmap((releasedButtonImage), (int)size, (int)size, true);
        bounds = new RectF(x, y, x+size, y+size);
        pressed = false;

    }

    /**
     * when someone press
     */
    public void pressed() {
        pressed = true;
    }

    /**
     * when someone unpress
     */
    public void released() {
        pressed = false;
    }

    /**
     *
     * @param c draw the bitmap
     */
    public void draw(Canvas c) {
        if (pressed) {
            c.drawBitmap(releasedButtonImage, bounds.left, bounds.top, null);
        }else {
            c.drawBitmap(buttonImage, bounds.left, bounds.top, null);
        }
    }

    /**
     * Where I get the x,y of the images
     * @param x
     * @param y
     * @return
     */
    public boolean contains(float x, float y) {
        return bounds.contains(x,y);
    }
}