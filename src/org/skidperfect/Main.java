package org.skidperfect;

import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.runetek.event.listeners.RenderListener;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.awt.*;


@ScriptMeta(name = "F2Pscape",  desc = "Automatically plays noob f2p.", developer = "SkidPerfect", category = ScriptCategory.OTHER)
public class Main extends Script implements RenderListener {
    //Meta data
    public static long startTime;
    public static long runTime;

    //Graphics
    private final Font font1 = new Font("Consolas", 0, 12);

    @Override
    public int loop() {
        BotTaskManager.update();
        BotTaskManager.getCurrentTask().execute();

        return Random.nextInt(300, 400);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onStart() {
        Log.fine("Welcome to Free to Play Scape! -- Designed and Written by SkidPerfect");
        BotTaskManager.initializeManager();
        startTime = System.currentTimeMillis();
    }

    @Override
    public void notify(RenderEvent renderEvent) {
        Graphics render = renderEvent.getSource();
        runTime = System.currentTimeMillis() - startTime;

        render.drawString("Runtime: " + formatTime(runTime), 20, 20);
        render.setFont(font1);
    }

    public static final String formatTime(final long ms) {
        long s = ms / 1000, m = s / 60, h = m / 60;
        s %= 60;
        m %= 60;
        h %= 24;
        return String.format("%02d:%02d:%02d", h, m, s);
    }
}
