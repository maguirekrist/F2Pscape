package org.skidperfect;

import org.rspeer.runetek.api.Game;
import org.rspeer.runetek.event.listeners.ItemTableListener;
import org.rspeer.runetek.event.types.ItemTableEvent;
import org.rspeer.ui.Log;

import java.io.IOException;

public class UpdatePrices extends Thread {
    private static int id = 0;
    private static boolean running;
    private static boolean revieved;

    public static boolean isRunning() {
        return running;
    }

    public static void setRunning(boolean running) {
        UpdatePrices.running = running;
    }

    public static boolean isRevieved() {
        return revieved;
    }

    public static void setRevieved(boolean revieved) {
        UpdatePrices.revieved = revieved;
    }

    @Override
    public void run() {
        while(isRunning()) {
            if(isRevieved()) {
                Log.severe("Updating gold!");
                try {
                    Main.TOTAL_EARNINGS += PriceHelper.getRSPrice(id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                setRevieved(false);
            }
        }
    }

    public synchronized void update(int id) {
        this.id = id;
        setRevieved(true);
    }
}
