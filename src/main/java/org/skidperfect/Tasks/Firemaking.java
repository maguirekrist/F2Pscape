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
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.task.Task;
import org.rspeer.ui.Log;
import org.skidperfect.BotTask;

import java.util.ArrayList;
import java.util.List;

public class Firemaking extends BotTask {

    //Axe Data
    private final String BRONZE_AXE = "Bronze axe";
    private final String IRON_AXE = "Iron axe";
    private final String MITH_AXE = "Mithril axe";
    private final String ADAM_AXE = "Adamant axe";
    private final String STEEL_AXE = "Steel axe";
    private final String RUNE_AXE = "Rune axe";

    private final String[] allAxes = { BRONZE_AXE, IRON_AXE, MITH_AXE, ADAM_AXE, RUNE_AXE, STEEL_AXE };

    private enum TaskData {
        NORMAL(Area.rectangular(3070, 3283, 3096, 3263), new Position(3096, 3281, 0), RSBank.DRAYNOR, BankLocation.DRAYNOR, 0, "Logs", "Tree"),
        OAK(Area.rectangular(3073, 3309, 3087, 3293), new Position(3096, 3281, 0), RSBank.DRAYNOR, BankLocation.DRAYNOR, 15, "Oak logs", "Oak"),
        WILLOW(Area.rectangular(3080, 3239, 3091, 3225), null,RSBank.DRAYNOR, BankLocation.DRAYNOR, 30, "Willow logs", "Willow");

        private Area area;
        private Position lightSpot;
        private RSBank bank;
        private BankLocation bankLocation;
        private int requiredLvl;
        private String logName;
        private String treeName;

        TaskData(Area area, Position lightSpot, RSBank bank, BankLocation bankLocation, int requiredLvl, String logName, String treeName) {
            this.area = area;
            this.bank = bank;
            this.lightSpot = lightSpot;
            this.bankLocation = bankLocation;
            this.requiredLvl = requiredLvl;
            this.logName = logName;
            this.treeName = treeName;
        }

        public Area getArea() {
            return area;
        }

        public RSBank getBank() {
            return bank;
        }

        public int getRequiredLvl() {
            return requiredLvl;
        }

        public String getLogName() {
            return logName;
        }

        public String getTreeName() {
            return treeName;
        }

        public BankLocation getBankLocation() {
            return bankLocation;
        }

        public Position getLightSpot() {
            return lightSpot;
        }
    }

    private TaskData currentState;

    @Override
    public int run() {
        if(currentLvl != Skills.getCurrentLevel(Skill.FIREMAKING)) {
            currentLvl = Skills.getCurrentLevel(Skill.FIREMAKING);
            update(currentLvl);
        }

        if(Dialog.isOpen() && Dialog.canContinue()) Dialog.processContinue();

        if ((Equipment.contains(allAxes) || Inventory.contains(allAxes)) && Inventory.contains("Tinderbox")) {
            if (Inventory.isFull()) {
                //FireMaking activity
                if(Players.getLocal().getPosition().distance(currentState.getLightSpot()) <= 2) {
                    Item tinderBox = Inventory.getFirst("Tinderbox");
                    Item[] logs = Inventory.getItems(i->i.getName().equals(currentState.getLogName()));
                    for (Item log: logs) {
                        if(!Players.getLocal().isAnimating() && !Dialog.isOpen() && !Movement.isDestinationSet()) {
                            if(tinderBox.interact("Use")) {
                                Time.sleep(600, 1200);
                                if(log.interact("Use")) {
                                    if(Time.sleepUntil(()->Players.getLocal().getAnimation() != -1 || Dialog.isOpen(), Random.nextInt(3000, 5000))) {
                                        if(Dialog.isOpen()) {
                                            while(Dialog.isOpen()) {
                                                Dialog.processContinue();
                                                Time.sleep(500, 800);
                                            }
                                        }
                                        Time.sleepUntil(()->Players.getLocal().getAnimation() == -1, Random.nextInt(20000, 25000));
                                    }
                                }
                            }
                        } else if(Dialog.isOpen()) {
                            while(Dialog.isOpen()) {
                                Dialog.processContinue();
                                Time.sleep(500, 800);
                            }
                        }
                    }
                } else {
                    if(!Movement.isDestinationSet())  Movement.setWalkFlag(currentState.getLightSpot());
                    return 600;
                }
                return Random.nextInt(1200, 1600);
            } else if (!Inventory.isFull() && currentState.getArea().contains(Players.getLocal())) {
                SceneObject tree = SceneObjects.newQuery().names(currentState.getTreeName()).reachable().results().nearest();
                if (tree != null && !Movement.isDestinationSet() && !Dialog.isOpen()) {
                    if (tree.interact("Chop down")) {
                        if (Time.sleepUntil(() -> Players.getLocal().getAnimation() != -1 || Dialog.isOpen(), Random.nextInt(6000, 6400))) {
                            Log.severe("Success");
                            if (Dialog.isOpen() && Dialog.canContinue()) {
                                Dialog.processContinue();
                                Time.sleepUntil(() -> !Dialog.isOpen(), 5000);
                            }
                            Time.sleepUntil(() -> tree == null || Dialog.isOpen() || Players.getLocal().getAnimation() == -1, 20000);
                            return Random.nextInt(600, 900);
                        }
                    }
                    return 200;
                }
            } else if (!Inventory.isFull() && !currentState.getArea().contains(Players.getLocal())) {
                Movement.getDaxWalker().walkTo(currentState.getArea().getTiles().get(Random.nextInt(0, currentState.getArea().getTiles().toArray().length)));
                return 300;
            }
        } else {
            if (Players.getLocal().distance(currentState.getBankLocation().getPosition()) <= 20) {
                //Bank Code
                if (Bank.isOpen() && !Inventory.contains(allAxes)) {
                    getBestAxe();
                    return 500;
                } else if(Bank.isOpen() && Inventory.contains(allAxes) && !Inventory.contains("Tinderbox")) {
                    if(Bank.withdraw("Tinderbox", 1)) {
                        Time.sleepUntil(()->Inventory.contains("Tinderbox"), 100000);
                        return 600;
                    }
                } else if (Bank.isOpen() && Inventory.contains(allAxes) && Inventory.contains("Tinderbox")) {
                    Bank.close();
                    return 600;
                } else {
                    Bank.open(BankLocation.getNearest());
                    return 300;
                }
            } else {
                Movement.getDaxWalker().walkToBank(currentState.getBank());
                return 300;
            }
        }
        return 500;
    }

    @Override
    public void update(int lvl) {
        for (TaskData state: TaskData.values()) {
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
            Time.sleepUntil(()-> Inventory.contains(possibleItemName.get(0)), 5000);
            Time.sleep(800, 1200);
        }

    }
}
