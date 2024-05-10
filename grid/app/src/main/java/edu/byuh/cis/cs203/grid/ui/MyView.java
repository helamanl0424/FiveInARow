package edu.byuh.cis.cs203.grid.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import edu.byuh.cis.cs203.grid.R;
import edu.byuh.cis.cs203.grid.logic.GameBoard;
import edu.byuh.cis.cs203.grid.logic.GameMode;
import edu.byuh.cis.cs203.grid.logic.Player;

import static android.graphics.Color.*;

@SuppressWarnings("ALL")
public class MyView extends ImageView implements TickListener {

    private Paint p1;
    private float startX, startY, endX;
    private float lineWidth;
    private float w, h;
    private int grid;
    private int line;
    private float gridLength;
    private GridButton gridButton;
    private int buttonSize;
    private ArrayList<GridButton> buttonLabel = new ArrayList<>();
    private boolean mathDone;
    private ArrayList<GuiTokens> tok = new ArrayList<>();
    private GameBoard engine;
    private GameHandler handler;
    private ArrayList<GuiTokens> invisibleToken =new ArrayList<>();
    private Paint textPaint;
    public int scoreX;
    public int scoreO;
    private GameMode gameMode;
    private GuiTokens guiTokens;
    private MediaPlayer soundtrack,press,slide;
    private int image;
    private String playerMode;



    /**
     * constructor
     *
     * @param c
     */
    public MyView(Context c, String player) {
        super(c);
        //setGameModeDialog();
        playerMode = player;
        if (player.equals("ONE_PLAYER")) {
            gameMode = GameMode.ONE_PLAYER;
        } else {
            gameMode = GameMode.TWO_PLAYER;
        }
        handler = GameHandler.timerFactory();
        p1 = new Paint();
        p1.setColor(BLACK);
        grid = 5; //number of grids
        line = grid + 1; // number of lines
        mathDone = false;
        engine = new GameBoard();
        handler.regist(this);
        textPaint = new Paint();
        image = R.drawable.back1;
        setImageResource(image);
        setScaleType(ScaleType.FIT_XY);
        if (Prefs.soundOn(c)) {
            soundtrack = MediaPlayer.create(c, R.raw.audio);
            soundtrack.start();
            soundtrack.setLooping(true);
        }
        if (Prefs.soundEffectOn(c)) {
            slide = MediaPlayer.create(c, R.raw.slide);
            slide.setLooping(false);
            press = MediaPlayer.create(c, R.raw.press);
            press.setLooping(false);
        }
    }

    /**
     *get move details from TickListener to re-draw and announce winner if there is one
     */
    @Override
    public void onTick() {
        if (GuiTokens.motionCheck() == false) {
            Player winner = engine.checkForWin();
            if(winner != Player.BLANK){
                handler.paused = true;
                alertDialog(winner);
            }else {
                if (gameMode == GameMode.ONE_PLAYER && engine.getCurrentPlayer() == Player.O) {
                    post(() -> {
                        Random random = new Random();
                        gridButton = buttonLabel.get(random.nextInt(buttonLabel.size()));
                        guiTokens = makeToken(gridButton);
                    });
//                    engine.checkForHorizontalWinSoon();
//                    post(() -> {
//                        if (engine.checkForHorizontalWinSoon) {
//                            if (engine.getRow() == 0) {
//                                gridButton = buttonLabel.get(5);
//                            }
//                            if (engine.getRow() == 1) {
//                                gridButton = buttonLabel.get(6);
//                            }
//                            if (engine.getRow() == 2) {
//                                gridButton = buttonLabel.get(7);
//                            }
//                            if (engine.getRow() == 3) {
//                                gridButton = buttonLabel.get(8);
//                            }
//                            if (engine.getRow() == 4) {
//                                gridButton = buttonLabel.get(9);
//                            }
//                            guiTokens = makeToken(gridButton);
//                            engine.checkForHorizontalWinSoon = false;
//                        }else {
//                            Random random = new Random();
//                            gridButton = buttonLabel.get(random.nextInt(buttonLabel.size()));
//                            guiTokens = makeToken(gridButton);
//                        }
//                    });
                }
            }
        }
        invalidate();
    }

    /**
     * pause the music
     */
    private void pauseMusic() {
        if (soundtrack != null) {
            soundtrack.pause();
        }
    }

    /**
     * restart music
     */
    private void restartMusic() {
        if (soundtrack != null) {
            soundtrack.start();
        }
    }

    /**
     * pause music when backgrounded
     */
    public void justGotBackgrounded() {
        pauseMusic();
    }

    /**
     * restart music when go back in
     */
    public void justGotForegrounded() {
        restartMusic();
    }

    /**
     * release memory when quit app
     */
    public void cleanupBeforeShutdown() {
        if (soundtrack != null) {
            soundtrack.release();
        }
    }

    /**
     * background each round
     */
    private void setNextBackground(){
        if(image == R.drawable.back1){
            image = R.drawable.back2;
        }else if (image == R.drawable.back2) {
            image = R.drawable.back3;
        }else if (image == R.drawable.back3) {
            image = R.drawable.back4;
        }else if (image == R.drawable.back4){
            image = R.drawable.back1;
        }
    }
    /**
     * config alertDialog
     * @param p
     */
    public void alertDialog(Player p) {
        AlertDialog.Builder ab = new AlertDialog.Builder(getContext())
        .setMessage(R.string.endgame_message)
        .setPositiveButton(R.string.yes, (d,i) -> resetAll())
        .setNegativeButton(R.string.no,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int i) {
                ((MainActivity) getContext()).finish();
            }
        });
        if (p == Player.TIE) {
            ab.setTitle(R.string.endgame_title1);
            scoreX += 1;
            scoreO += 1;
        }
        if (p == Player.X) {
            ab.setTitle(R.string.endgame_title2);
            scoreX += 1;
        }
        if (p == Player.O) {
            ab.setTitle(R.string.endgame_title3);
            scoreO += 1;
        }
        AlertDialog box = ab.create();
        box.show();
    }

    /**
     * Alert dialog for game mode
     */
    public void setGameModeDialog() {
        AlertDialog gm = new AlertDialog.Builder(getContext())
                .setTitle(R.string.gameMode_title)
                .setMessage(R.string.gameMode_message)
                .setNegativeButton(R.string.onePlayer, (dialog, which) -> gameMode = GameMode.ONE_PLAYER)
                .setPositiveButton(R.string.twoPlayer, (dialog, which) -> gameMode = GameMode.TWO_PLAYER)
                .create();
        gm.show();
    }

    /**
     * reset the game
     */
    public void resetAll() {
        engine.resetGameBoard();
        tok.clear();
        GuiTokens.stopMoving();
        handler.deregistAll();
        handler.regist(this);
        setNextBackground();
        setImageResource(image);
    }

    /**
     * it is where I draw the buttons, lines, and grids in my view
     *
     * @param c
     */
    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        w = c.getWidth();
        h = c.getHeight();
        if (mathDone == false) {
            textPaint.setTextSize(w * h * 0.00004f);
            textPaint.setColor(BLACK);
            lineWidth = w * 0.01f;
            p1.setStrokeWidth(lineWidth);
            //c.drawColor(WHITE);
            gridLength = w / (grid + 2);
            buttonSize = (int) (gridLength * 0.99);
            startX = gridLength;
            endX = gridLength * line;
            startY = (h - gridLength * grid) / 2;
            for (int j = 0; j < grid; j++) { //insert 5 buttons horizontally
                char label = (char) ('1' + j);
                gridButton = new GridButton(getResources(), buttonSize, startX + w * 0.002f, startY - gridLength, label);
                buttonLabel.add(gridButton);
                startX += gridLength;
            }
            for (int i = 0; i < grid; i++) { //insert 5 buttons vertically
                char label = (char) ('A' + i);
                gridButton = new GridButton(getResources(), buttonSize, 0, startY, label);
                buttonLabel.add(gridButton);
                startY += gridLength;
            }
            mathDone = true;
        }
        startX = gridLength;
        startY = (h - gridLength * grid) / 2;
        for (int i = 0; i < line; i++) { //draw horizontal lines
            c.drawLine(startX, startY, endX, startY, p1);
            startY += gridLength;
        }
        startY = (h - gridLength * grid) / 2;
        for (int j = 0; j < line; j++) { //draw vertical lines
            c.drawLine(startX, startY, startX, startY + gridLength * 5, p1);
            startX += gridLength;
        }
        for (GuiTokens g : tok) {
            if(g.isInvisible(h)) {
                invisibleToken.add(g);
                handler.deregist(g);
            }else {
                g.draw(c);
            }
        }
        tok.removeAll(invisibleToken);
        for (GridButton b : buttonLabel) {
            b.draw(c);
        }
        c.drawText(getResources().getString(R.string.playerX) + " " + scoreX, gridLength/3, (h-gridLength*grid)/7, textPaint);
        c.drawText(getResources().getString(R.string.playerO) + " " + scoreO, gridLength*4, (h-gridLength*grid)/7, textPaint);
        c.drawText(getResources().getString(R.string.playerTurn1) + " "
                + engine.getCurrentPlayer()
                + getResources().getString(R.string.playerTurn2)
                , gridLength*2,(h-gridLength*grid)/4, textPaint);
    }

    /**
     * @param m where I manage the press and unpress
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent m) {
            float x = m.getX();
            float y = m.getY();
            if (m.getAction() == MotionEvent.ACTION_DOWN) {
                if (GuiTokens.motionCheck() == true) {
                    return true;
                }
                boolean pressButton = false;
                for (GridButton b : buttonLabel) {
                    if (b.contains(x, y)) {
                        if (press != null) {
                            press.start();
                        }
                        pressButton = true;
                        b.pressed();
                        makeToken(b);
                    }
                }
                if (pressButton == false) {
                    Toast t = Toast.makeText(getContext(),
                            R.string.toast_message, Toast.LENGTH_SHORT);
                    t.show();
                }
            }
            if (m.getAction() == MotionEvent.ACTION_UP) {
                for (GridButton b : buttonLabel) {
                    b.released();
                }
            }
            invalidate();
            return true;
    }

    /**
     * A simple factory for token
     * @param b
     * @return
     */
    private GuiTokens makeToken(GridButton b){
        int id;
        if(engine.getCurrentPlayer() == Player.X){
            id = R.drawable.siumai;
        } else {
            id = R.drawable.hargao;
        }
        GuiTokens tok2 = new GuiTokens(getResources(), gridLength, b.bounds.left, b.bounds.top, b, id, this);
        tok.add(tok2);
        engine.submitMove(b.label);
        handler.regist(tok2); //subscribe
        slides(b.label);
        return tok2;
    }



    /**
     * it is where I make the tokens slide
     * @param label
     */
    private void slides(char label) {
        char first;
        if (label >= '1' && label <= '5') { //slide vertically
            first = 'A';
            for (int i=0; i<5; i++) {
                boolean hasToken = false;
                for (GuiTokens g : tok) {
                    if (g.gridPos.row == first && g.gridPos.column == label
                            && g.velocity.x == 0 && g.velocity.y == 0) {
                            g.setColGoal();
                            g.gridPos.row++;
                            hasToken = true;
                            break;
                    }
                }
                first++;
                if (hasToken == false) {
                    break;
                }
            }
        } else {
            first = '1';
            for (int i=0; i<5; i++) { //slide horizontally
                boolean hasToken = false;
                for (GuiTokens g : tok) {
                    if (g.gridPos.row == label && g.gridPos.column == first
                            && g.velocity.x == 0 && g.velocity.y == 0) {
                        g.setRowGoal();
                        g.gridPos.column++;
                        hasToken = true;
                        break;
                    }
                }
                first++;
                if (hasToken == false) {
                    break;
                }
            }
        }
        if (slide != null) {
            slide.start();
        }
    }
}
