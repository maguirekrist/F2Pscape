package org.skidperfect;

import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.ui.Log;
import org.skidperfect.Tasks.Combat;
import org.skidperfect.Tasks.Firemaking;
import org.skidperfect.Tasks.Fishing;
import org.skidperfect.Tasks.WoodCutting;

import java.util.List;

public enum BotTasks {

    WOOD_CUTTING(2, new WoodCutting()),
    FISHING(1, new Fishing()),
    FIREMAKING(3, new Firemaking()),
    COMBAT(4, new Combat());

    private int priority;
    private BotTask botTask;

    BotTasks(int priority, BotTask task) {
       this.priority = priority;
       this.botTask = task;
    }

    public void execute() {
        botTask.run();
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int p) { this.priority = p; }

}
