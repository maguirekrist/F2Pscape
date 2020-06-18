package org.skidperfect;

import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.BankLocation;
import org.rspeer.runetek.api.commons.Pair;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BotTask {

    public int currentLvl;
    public int currentState;

    public enum ToolMat {
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


    public int run() {
        //Abstract Run shared amongst all BotTasksr
        return 300;
    }

    public void update(int lvl) {

    }

   public int BankHandler(boolean isDeposit, Pair<Integer, String>... items) {
       // TODO
       return 300;
   }


}
