package org.skidperfect.Tasks;

import org.rspeer.networking.dax.walker.models.RSBank;
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
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.ui.Log;
import org.skidperfect.BotTask;

import java.util.ArrayList;
import java.util.List;

public class Mining extends BotTask {
    //Axe Data
    private final String BRONZE_AXE = "Bronze pickaxe";
    private final String IRON_AXE = "Iron pickaxe";
    private final String MITH_AXE = "Mithril pickaxe";
    private final String ADAM_AXE = "Adamant pickaxe";
    private final String STEEL_AXE = "Steel pickaxe";
    private final String RUNE_AXE = "Rune pickaxe";

    private final String[] allAxes = { BRONZE_AXE, IRON_AXE, MITH_AXE, ADAM_AXE, RUNE_AXE, STEEL_AXE };
    private enum TaskState {
        COPPERTIN(Area.rectangular(3221, 3151, 3232, 3142), RSBank.LUMBRIDGE_TOP, BankLocation.LUMBRIDGE_TOP_DB, true,0, new String[]{"Tin ore", "Copper ore"},10943, 11161, 11360, 11361),
        IRON(Area.rectangular(3172, 3379, 3185, 3364), RSBank.VARROCK_WEST, BankLocation.VARROCK_WEST ,false,15, new String[]{"Iron ore"}, 0);
       // CLAY();

        private Area location;
        private RSBank bank;
        private BankLocation bankLocation;
        private boolean drop;
        private int requiredLvl;
        private String[] rockNames;
        private int[] rockIds;

        TaskState(Area location, RSBank bank, BankLocation bankLocation, boolean drop,  int requiredLvl, String[] rockNames,int... rockIds) {
            this.location = location;
            this.bank = bank;
            this.bankLocation = bankLocation;
            this.drop = drop;
            this.requiredLvl = requiredLvl;
            this.rockNames = rockNames;
            this.rockIds = rockIds;
        }

        public Area getLocation() {
            return location;
        }

        public RSBank getBank() {
            return bank;
        }

        public int getRequiredLvl() {
            return requiredLvl;
        }

        public int[] getRockIds() {
            return rockIds;
        }

        public BankLocation getBankLocation() {
            return bankLocation;
        }

        public boolean isDrop() {
            return drop;
        }

        public String[] getRockNames() {
            return rockNames;
        }
    }

    private TaskState currentState;
    @Override
    public int run() {
        if (currentLvl != Skills.getCurrentLevel(Skill.MINING)) {
            currentLvl = Skills.getCurrentLevel(Skill.MINING);
            update(currentLvl);
        }

        if(Dialog.isOpen() && Dialog.canContinue()) Dialog.processContinue();

        if (Equipment.contains(allAxes) || Inventory.contains(allAxes)) {
            if (Inventory.isFull() && Players.getLocal().distance(currentState.getBankLocation().getPosition()) >= 20) {
                Movement.getDaxWalker().walkToBank(currentState.getBank());
                return 300;
            } else if (Inventory.isFull() && Players.getLocal().distance(currentState.getBankLocation().getPosition()) <= 20) {
                if (Bank.isOpen() && Inventory.contains(currentState.getRockNames())) {
                    Time.sleep(1200, 1500);
                    if (Bank.depositAllExcept(allAxes)) {
                        Time.sleepUntil(() -> Inventory.containsOnly(allAxes), Random.nextInt(1000, 2000));
                    }
                    return Random.nextInt(900, 14400);
                } else if (Bank.isOpen() && !Inventory.contains(currentState.getRockNames())) {
                    Bank.close();
                    return 600;
                } else {
                    Bank.open(BankLocation.getNearest());
                    return 300;
                }
            } else if (!Inventory.isFull() && currentState.getLocation().contains(Players.getLocal())) {
                SceneObject ore = SceneObjects.newQuery().ids(currentState.getRockIds()).reachable().results().nearest();
                if (ore != null && !Movement.isDestinationSet() && !Dialog.isOpen()) {
                    if (ore.interact("Mine")) {
                        if (Time.sleepUntil(() -> Players.getLocal().getAnimation() != -1 || Dialog.isOpen(), Random.nextInt(6000, 6400))) {
                            Log.severe("Success");
                            if (Dialog.isOpen() && Dialog.canContinue()) {
                                Dialog.processContinue();
                                Time.sleepUntil(() -> !Dialog.isOpen(), 5000);
                            }
                            Time.sleepUntil(() -> ore == null || Dialog.isOpen() || Players.getLocal().getAnimation() == -1, 20000);
                            return Random.nextInt(600, 900);
                        }
                    }
                    return 200;
                }
            } else if (!Inventory.isFull() && !currentState.getLocation().contains(Players.getLocal())) {
                Movement.getDaxWalker().walkTo(currentState.getLocation().getTiles().get(Random.nextInt(0, currentState.getLocation().getTiles().toArray().length)));
                return 300;
            }
        } else {
            if (Players.getLocal().distance(currentState.getBankLocation().getPosition()) <= 20) {
                //Bank Code
                if (Bank.isOpen() && !Inventory.contains(allAxes)) {
                    getBestAxe();
                    return 500;
                } else if (Bank.isOpen() && Inventory.contains(allAxes)) {
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
        for (TaskState state: TaskState.values()) {
            if(lvl >= state.getRequiredLvl()) {
                currentState = state;
            }
        }
    }

    private void getBestAxe() {
        List<String> possibleItemName = new ArrayList<String>();
        for (ToolMat mat : ToolMat.values()) {
            String itemName = mat.getName() + " " + "pickaxe";
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
