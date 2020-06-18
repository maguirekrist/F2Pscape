package org.skidperfect;

import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.ui.Log;
import org.skidperfect.Tasks.*;

import java.util.List;

public enum BotTasks {

    WOOD_CUTTING(52, new WoodCutting()),
    FIREMAKING(1, new Firemaking()),
    INTERMISSION(100, new Intermission()),
    QUESTING(1, new Questing()),
    FISHING(51, new Fishing()),
    COMBAT(50, new Combat());

    private int priority;
    private BotTask botTask;

    BotTasks(int priority, BotTask task) {
       this.priority = priority;
       this.botTask = task;
    }

    public void execute() {
        botTask.run();
    }

    public BotTask getBotTask() { return this.botTask; }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int p) { this.priority = p; }

}
