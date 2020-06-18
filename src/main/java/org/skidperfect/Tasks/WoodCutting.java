package org.skidperfect.Tasks;

import org.rspeer.networking.dax.walker.models.RSBank;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.BankLocation;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.tab.Equipment;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.query.ItemQueryBuilder;
import org.rspeer.runetek.api.query.results.ItemQueryResults;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.ui.Log;
import org.skidperfect.BotTask;

import java.util.ArrayList;
import java.util.List;

public class WoodCutting extends BotTask {

    //Axe Data
    private final String BRONZE_AXE = "Bronze axe";
    private final String IRON_AXE = "Iron axe";
    private final String MITH_AXE = "Mithril axe";
    private final String ADAM_AXE = "Adamant axe";
    private final String STEEL_AXE = "Steel axe";
    private final String RUNE_AXE = "Rune axe";

    private final String[] allAxes = { BRONZE_AXE, IRON_AXE, MITH_AXE, ADAM_AXE, RUNE_AXE, STEEL_AXE };

    //Lvl

    public static TaskState currentState;

    private enum TaskState {
        TREES(RSBank.DRAYNOR, Area.rectangular(3089, 3246, 3097, 3240), Area.rectangular(3074, 3275, 3086, 3263), 0, "Tree", "Logs", false),
        OAKS(RSBank.DRAYNOR, Area.rectangular(3089, 3246, 3097, 3240), Area.rectangular(3073, 3309, 3087, 3293), 15, "Oak", "Oak Logs", false);
       // WILLOWS(RSBank.DRAYNOR, Area.rectangular(3089, 3246, 3097, 3240), Area.rectangular(3080, 3239, 3094, 3224), 30, "Willow", "Willow Logs", false);

        private final RSBank rsBank;
        private final Area bankArea;
        private final Area chopArea;
        private final int requiredLvl;
        private final String treeName;
        private final String logName;
        private boolean dropLogs;

        TaskState(final RSBank rsBank, Area bankArea, final Area chopArea, final int requiredLvl, final String treeName, final String logName, boolean dropLogs) {
            this.rsBank = rsBank;
            this.bankArea = bankArea;
            this.chopArea = chopArea;
            this.requiredLvl = requiredLvl;
            this.treeName = treeName;
            this.logName = logName;
        }

        public RSBank getRsBank() {
            return rsBank;
        }

        public Area getBankArea() { return bankArea; }

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

        public boolean isDropLogs() {
            return dropLogs;
        }
    }


    @Override
    public int run() {
        //LEARN how to setup seperate thread for checking LVL update
        if (currentLvl != Skills.getCurrentLevel(Skill.WOODCUTTING)) {
            currentLvl = Skills.getCurrentLevel(Skill.WOODCUTTING);
            Log.info("Current WoodCutting Level" + currentLvl);
            update(currentLvl);
            return 300;
        }

        if(Dialog.isOpen() && Dialog.canContinue()) Dialog.processContinue();

        //Main Loop
        if (Equipment.contains(allAxes) || Inventory.contains(allAxes)) {
            if (Inventory.isFull() && !currentState.getBankArea().contains(Players.getLocal().getPosition())) {
                Movement.getDaxWalker().walkToBank(currentState.getRsBank());
                return 300;
            } else if (Inventory.isFull() && currentState.getBankArea().contains(Players.getLocal().getPosition())) {
                // Bank Code
                if(Bank.isOpen() && Inventory.contains(currentState.getLogName())) {
                    Time.sleep(1200, 1500);
                    if(Bank.depositAllExcept(allAxes)) {
                        Time.sleepUntil(()->Inventory.containsOnly(allAxes), Random.nextInt(1000, 2000));
                    }
                    return Random.nextInt(900, 14400);
                } else if(Bank.isOpen() && !Inventory.contains(currentState.getLogName())) {
                    Bank.close();
                    return 600;
                } else {
                    Bank.open(BankLocation.getNearest());
                    return 300;
                }
            } else if (!Inventory.isFull() && currentState.getChopArea().contains(Players.getLocal())) {
                SceneObject tree = SceneObjects.newQuery().names(currentState.getTreeName()).reachable().results().nearest();
                if(tree != null && !Movement.isDestinationSet() && !Dialog.isOpen()) {
                    if(tree.interact("Chop down")) {
                        if(Time.sleepUntil(()->Players.getLocal().getAnimation() != -1 || Dialog.isOpen(), Random.nextInt(6000, 6400))) {
                            Log.severe("Success");
                            if(Dialog.isOpen() && Dialog.canContinue()) {
                                Dialog.processContinue();
                                Time.sleepUntil(()->!Dialog.isOpen(), 5000);
                            }
                            Time.sleepUntil(()->tree == null || Dialog.isOpen() || Players.getLocal().getAnimation() == -1, 20000);
                            return Random.nextInt(600, 900);
                        }
                    }
                    return 200;
                }
            } else if (!Inventory.isFull() && !currentState.getChopArea().contains(Players.getLocal())) {
                Movement.getDaxWalker().walkTo(currentState.getChopArea().getTiles().get(Random.nextInt(0, currentState.getChopArea().getTiles().toArray().length)));
                return 300;
            }
        } else {
            if (currentState.getBankArea().contains(Players.getLocal().getPosition())) {
                //Bank Code
                if(Bank.isOpen() && !Inventory.contains(allAxes)) {
                    getBestAxe();
                    return 500;
                } else if(Bank.isOpen() && Inventory.contains(allAxes)) {
                    Bank.close();
                    return 600;
                } else {
                    Bank.open(BankLocation.getNearest());
                    return 300;
                }
            } else {
                Movement.getDaxWalker().walkToBank(currentState.getRsBank());
                return 300;
            }

        }
        return 300;
    }

    @Override
    public void update(int lvl) {
        for (TaskState state: TaskState.values()) {
            if(lvl >= state.getRequiredLvl()) {
                currentState = state;

            }
        }
    }

    private void getBestAxe() {
        List<String> possibleItemName = new ArrayList<String>();
            for (ToolMat mat : ToolMat.values()) {
                String itemName = mat.getName() + " " + "axe";
                if(currentLvl >= mat.getRequiredLvl()) {
                    if (Bank.contains(itemName)) {
                        possibleItemName.add(itemName);
                    } else {
                        continue;
                    }
                }
            }

       if(Bank.withdraw(possibleItemName.get(0), 1)) {
           Time.sleepUntil(()->Inventory.contains(possibleItemName.get(0)), 5000);
           Time.sleep(800, 1200);
       }

    }
}
