package org.skidperfect;

import org.rspeer.RSPeer;
import org.rspeer.runetek.api.Game;
import org.rspeer.runetek.api.Login;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.event.listeners.ItemTableListener;
import org.rspeer.runetek.event.listeners.RenderListener;
import org.rspeer.runetek.event.types.ItemTableEvent;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.awt.*;


@ScriptMeta(name = "F2Pscape",  desc = "Automatically plays noob f2p.", developer = "SkidPerfect", category = ScriptCategory.OTHER)
public class Main extends Script implements RenderListener, ItemTableListener {
    //Meta data
    public static long startTime;
    public static long runTime;

    public static int TOTAL_EARNINGS;

    private UpdatePrices updatePrices = new UpdatePrices();

    //Graphics
    private final Font font1 = new Font("Dialog", Font.PLAIN, 14);

    @Override
    public int loop() {
        BotTaskManager.update();
        BotTaskManager.getCurrentTask().execute();

        return Random.nextInt(300, 400);
    }

    @Override
    public void onStop() {
        updatePrices.setRunning(false);
        super.onStop();
    }

    @Override
    public void onStart() {
        Log.fine("Welcome to Free to Play Scape! -- Designed and Written by SkidPerfect");
        BotTaskManager.initializeManager();
        updatePrices.start();
        updatePrices.setRunning(true);
        startTime = System.currentTimeMillis();
    }

    @Override
    public void notify(RenderEvent renderEvent) {
        Graphics render = renderEvent.getSource();
        runTime = System.currentTimeMillis() - startTime;

        render.setFont(font1);
        render.setColor(Color.MAGENTA);
        render.drawString("Runtime: " + formatTime(runTime), 20, 20);
        render.drawString("Total Gold Collected: " + TOTAL_EARNINGS, 20, 100);
        render.drawString("Time till Next Task: " + formatTime(BotTaskManager.getTimeTilNxtTask()), 200, 20);
        render.drawString("Current State: " + BotTaskManager.getCurrentTask().toString(), 20, 50);
        render.drawString(BotTaskManager.getCurrentTask().toString() + " Level: " + BotTaskManager.getCurrentTask().getBotTask().currentLvl, 20, 70);
    }

    public static final String formatTime(final long ms) {
        long s = ms / 1000, m = s / 60, h = m / 60;
        s %= 60;
        m %= 60;
        h %= 24;
        return String.format("%02d:%02d:%02d", h, m, s);
    }

    @Override
    public void notify(ItemTableEvent itemTableEvent) {
        if(itemTableEvent.getChangeType() == ItemTableEvent.ChangeType.ITEM_ADDED) {
            updatePrices.update(itemTableEvent.getId());
        }
    }
}
