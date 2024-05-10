package edu.byuh.cis.cs203.grid.ui;

import android.os.Handler;
import android.os.Message;
import java.util.ArrayList;
import java.util.Timer;

public class GameHandler extends Handler {
    private ArrayList<TickListener> tic = new ArrayList<>();
    public boolean paused = false;
    private static GameHandler gameHandler;
    private int delay;

    /**
     * constructor
     */
    private GameHandler() {

            sendMessageDelayed(obtainMessage(), 1000);
        }

    /**
     * send messages to TickListener
     * @param
     */
    @Override
    public void handleMessage(Message msg) {
        notifyTickListener();
        sendMessageDelayed(obtainMessage(), delay);
    }

    /**
     * method for notify TickListener
     */
    public void notifyTickListener() {
        if (!paused) {
            for(TickListener t: tic){
                t.onTick();
            }
        }
        if (tic.size() > 0) {
            delay = Prefs.getSpeed(((MyView)tic.get(0)).getContext());
        } else {
            delay = 100;
        }
    }
    /**
     * registering
     * @param
     */
    public void regist(TickListener t){

        tic.add(t);
    }

    /**
     * de-registering
     * @param
     */
    public void deregist(TickListener d){

        tic.remove(d);
    }

    /**
     * deregister all observer
     */
    public void deregistAll() {
        tic.clear();
        paused = false;
    }


    public static GameHandler timerFactory(){
        if(gameHandler == null) {
            gameHandler = new GameHandler();
        }
        return gameHandler;
    }
}

