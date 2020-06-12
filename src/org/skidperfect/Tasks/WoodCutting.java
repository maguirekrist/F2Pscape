package org.skidperfect.Tasks;

import com.sun.tools.javac.jvm.Items;
import org.rspeer.networking.dax.walker.models.RSBank;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.tab.Equipment;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.ui.Log;
import org.skidperfect.BotTask;

import java.util.zip.Adler32;

public class WoodCutting extends BotTask {

    //Axe Data
    private final String BRONZE_AXE = "Bronze axe";
    private final String IRON_AXE = "Iron axe";
    private final String MITH_AXE = "Mithril axe";
    private final String ADAM_AXE = "Adamant axe";
    private final String RUNE_AXE = "Rune axe";

    private final String[] allAxes = { BRONZE_AXE, IRON_AXE, MITH_AXE, ADAM_AXE, RUNE_AXE };

    //Lvl
    public static int currentWoodCutLvl;

    public static WCStates currentState;

    private enum WCStates {
        TREES(RSBank.DRAYNOR, Area.rectangular(3074, 3275, 3086, 3263), 0, "Tree", "Logs"),
        OAKS(RSBank.VARROCK_WEST, Area.rectangular(3155, 3421, 3172, 3398), 0, "Oak", "Oak Logs"),
        WILLOWS(RSBank.DRAYNOR, Area.rectangular(3080, 3239, 3094, 3224), 0, "Willow", "Willow Logs");

        private final RSBank rsBank;
        private final Area chopArea;
        private final int requiredLvl;
        private final String treeName;
        private final String logName;

        WCStates(final RSBank rsBank, final Area chopArea, final int requiredLvl, final String treeName, final String logName) {
            this.rsBank = rsBank;
            this.chopArea = chopArea;
            this.requiredLvl = requiredLvl;
            this.treeName = treeName;
            this.logName = logName;
        }

        public RSBank getRsBank() {
            return rsBank;
        }

        public Area getChopArea() {
            return chopArea;
        }

        public int getRequiredLvl() {
            return requiredLvl;
        }

        public String getTreeName() {
            return treeName;
        }

        public String getLogName() {
            return logName;
        }
    }


    @Override
    public void run() {
        //LEARN how to setup seperate thread for checking LVL update
        if(currentWoodCutLvl != Skills.getCurrentLevel(Skill.WOODCUTTING)) {
            currentWoodCutLvl = Skills.getCurrentLevel(Skill.WOODCUTTING);
            Log.info("Current WoodCutting Level" + currentWoodCutLvl);
            updateWCState(currentWoodCutLvl);
        }


        if(Equipment.contains(allAxes) || Inventory.contains(allAxes)) {
            if(Inventory.isFull()) {
                Movement.getDaxWalker().walkToBank(currentState.getRsBank());
                if(bankAtBank() && Bank.isOpen()) {
                    if(!Inventory.isEmpty()) {
                        if(Bank.depositAll(currentState.getLogName())) {
                            Time.sleepUntil(() -> Inventory.isEmpty(), Random.nextInt(4000, 6000));
                        }
                    }
                    if(Bank.close()) {
                        Time.sleepUntil(() -> Bank.isClosed(), Random.nextInt(800, 1600));
                    }

                }
            } else if(!Inventory.isFull() && currentState.getChopArea().contains(Players.getLocal())) {
                if (Players.getLocal().getAnimation() == -1 && !Movement.isDestinationSet()) {
                    SceneObject tree = SceneObjects.getNearest(currentState.getTreeName());
                    if (tree.interact("Chop down")) {
                        Log.info("Trying to interacting with SceneObject: " + tree.getId());
                        if (Time.sleepUntil(() -> Players.getLocal().getAnimation() == 879, Random.high(3000, 4500))) {
                            Time.sleepUntil(() -> Players.getLocal().getAnimation() == -1, Random.high(4000, 6500));
                        } else {
                            Log.severe("Timeout exceeded while trying to interact with SceneObject: " + tree.getId());
                        }
                    } else {
                        Log.severe("Failed to interact with the SceneObject: " + tree.getId());
                    }
                }
            } else if(!Inventory.isFull() && !currentState.getChopArea().contains(Players.getLocal())) {
                Movement.getDaxWalker().walkTo(currentState.getChopArea().getTiles().get(Random.nextInt(0, currentState.getChopArea().getTiles().toArray().length)));
            }
        } else {
            Movement.getDaxWalker().walkToBank(currentState.getRsBank());
            if(bankAtBank() && Bank.isOpen()) {
                if(!Inventory.isEmpty()) {
                    if(Bank.depositInventory()) {
                        Time.sleepUntil(() -> Inventory.isEmpty(), Random.nextInt(4000, 6000));
                    }
                }
                Item[] items = getBestItemInBank(currentWoodCutLvl, "axe");
                if(Bank.close()) {
                    Time.sleepUntil(() -> Bank.isClosed(), Random.nextInt(800, 1600));
                }
                for (Item item: items) {
                    if(item.containsAction("Wield")) {
                        if(item.interact("Wield")) {
                            Time.sleepUntil(() -> Equipment.contains(item.getName()), Random.nextInt(4000, 6000));
                        }
                    }
                    if(item.containsAction("Equip")) {
                        if(item.interact("Equip")) {
                            Time.sleepUntil(() -> Equipment.contains(item.getName()), Random.nextInt(4000, 6000));
                        }
                    }
                }
            }
        }

    }

    private void updateWCState(int lvl) {
        if(lvl < 15) {
           currentState = WCStates.TREES;
        } else if(lvl < 30) {
            currentState = WCStates.OAKS;
        } else if (lvl < 60){
            currentState = WCStates.WILLOWS;
        }
    }


}
