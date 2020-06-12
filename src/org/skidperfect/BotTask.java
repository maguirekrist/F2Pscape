package org.skidperfect;

import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.ui.Log;
import sun.awt.windows.ThemeReader;

import java.util.List;

public class BotTask {

    public int currentLvl;

    private enum ToolMat {
        RUNE(41, "Rune"),
        ADAMANT(31, "Adamant"),
        MITHRIL(21, "Mithril"),
        STEEL(11, "Steel"),
        IRON(0, "Iron"),
        BRONZE(0, "Bronze");


        private final int requiredLvl;
        private final String name;

        ToolMat(int requiredLvl, String name) {
            this.requiredLvl = requiredLvl;
            this.name = name;
        }

        public int getRequiredLvl() {
            return requiredLvl;
        }

        public String getName() {
            return name;
        }
    }

    private final ToolMat[] ALL_MATS = {ToolMat.RUNE, ToolMat.ADAMANT, ToolMat.MITHRIL, ToolMat.STEEL, ToolMat.IRON, ToolMat.BRONZE };

    public void run() {
        //Abstract Run shared amongst all BotTasks
    }

    public void update(int lvl) {

    }

    public boolean bankAtBank() {
        if(Random.nextBoolean()) {
            SceneObject bankBooth = SceneObjects.getNearest("Bank booth");
            if(bankBooth != null) {
                if(bankBooth.interact("Bank")) {
                    Time.sleepUntil(() -> Bank.isOpen(), Random.high(8000, 11500));
                    return true;
                }
            } else {
                return false;
            }
        } else {
            Npc banker = Npcs.getNearest("Banker");
            if(banker != null) {
                if(banker.interact("Bank")) {
                    Time.sleepUntil(() -> Bank.isOpen(), Random.high(8000, 11500));
                    return true;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    public Item[] getBestItemInBank(int lvl, String... itemType) {
        if(Bank.isOpen()) {
            for (String item: itemType) {
                breakSpot:
                for (ToolMat mat : ALL_MATS) {
                    String itemName = mat.getName() + " " + item;
                    if(lvl >= mat.requiredLvl) {
                        if(Bank.contains(itemName)) {
                            if(Bank.withdraw(itemName, 1)) {
                                Time.sleepUntil(() -> Inventory.contains(itemName), Random.nextInt(3000, 5000));
                                break breakSpot;
                            } else {
                                Log.severe("Error withdrawing from Bank!");
                            }
                        } else {
                            continue;
                        }
                    }
                }
            }
        } else {
            Log.severe("Bank is Not Open! Error using getBestItemInBank()");
        }
        return Inventory.getItems();
    }


}
