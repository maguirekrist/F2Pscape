package org.skidperfect.Tasks;

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

public class Combat extends BotTask {

    private final String[] combatSwords = {"Training sword", "Bronze sword"};
    private final String[] combatShields = {"Training shield", "Wood shield"};


    private enum CBStates {
        CHICKENS(Area.rectangular(3182, 3282, 3193, 3275), RSBank.LUMBRIDGE_TOP, 3, new String[]{"Feather"}, "Chicken"),
        COWS(Area.rectangular(3240, 3298, 3265, 3276), RSBank.LUMBRIDGE_TOP, 10, new String[]{"Cowhide"},"Cow", "Cow calf");

        private final Area area;
        private final RSBank bank;
        private final int requiredCbtLvl;
        private final String[] mobNames;
        private final String[] pickDrops;

        CBStates(Area area, RSBank bank, int requiredLvl, String[] drops, String... mobNames) {
            this.area = area;
            this.bank = bank;
            this.requiredCbtLvl = requiredLvl;
            this.pickDrops = drops;
            this.mobNames = mobNames;
        }


        public Area getArea() {
            return area;
        }

        public RSBank getBank() {
            return bank;
        }

        public int getRequiredCbtLvl() {
            return requiredCbtLvl;
        }

        public String[] getMobNames() {
            return mobNames;
        }

        public String[] getPickDrops() {
            return pickDrops;
        }
    }

    private static CBStates currentCBState;

    @Override
    public void run() {
        if(currentLvl != Players.getLocal().getCombatLevel()) {
            currentLvl = Players.getLocal().getCombatLevel();
            Log.info("Current Combat Level" + currentLvl);
            updateCbState();
        }
        if(Equipment.contains(combatSwords) && Equipment.contains(combatShields)) {
            if(Inventory.isFull()) {
                Movement.getDaxWalker().walkToBank(currentCBState.getBank());
                if(bankAtBank() && Bank.isOpen()) {
                    if(!Inventory.isEmpty()) {
                        if(Bank.depositAll(currentCBState.getPickDrops())) {
                            Time.sleepUntil(() -> Inventory.isEmpty(), Random.nextInt(4000, 6000));
                        }
                    }
                    if(Bank.close()) {
                        Time.sleepUntil(() -> Bank.isClosed(), Random.nextInt(800, 1600));
                    }

                }
            } else if(!Inventory.isFull() && currentCBState.getArea().contains(Players.getLocal())) {
                Log.info("Running ");
                if (Players.getLocal().getAnimation() == -1 && !Movement.isDestinationSet() && Players.getLocal().getTarget() == null) {
                    int arrayLength = currentCBState.getMobNames().length - 1;
                    Npc ent = Npcs.getNearest(npc -> npc.isPositionInteractable() && npc.getTarget() == null && npc.getName().equals(currentCBState.getMobNames()[Random.nextInt(0, arrayLength)]));
                    if(ent.getTarget() == null && ent.getHealthBar() == null) {
                        if (ent.interact("Attack")) {
                            Log.info("Trying to interacting with SceneObject: " + ent.getId());
                            if (Time.sleepUntil(() -> Players.getLocal().getTarget() != null, Random.high(8000, 11500))) {
                                Time.sleepUntil(() -> Players.getLocal().getTarget() == null, Random.high(8000, 11500));
                            } else {
                                Log.severe("Timeout exceeded while trying to interact with SceneObject: " + ent.getId());
                            }
                        }
                    }
                }
            } else if(!Inventory.isFull() && !currentCBState.getArea().contains(Players.getLocal())) {
                Movement.getDaxWalker().walkTo(currentCBState.getArea().getTiles().get(Random.nextInt(0, currentCBState.getArea().getTiles().toArray().length)));
            }
        } else {
            Movement.getDaxWalker().walkToBank(currentCBState.getBank());


        }
    }

    private void updateCbState() {
        for (CBStates state: CBStates.values()) {
            if(currentLvl >= state.getRequiredCbtLvl()) {
                currentCBState = state;
                Log.severe("ERROR : " + currentCBState.toString());
            }
        }
    }


}
