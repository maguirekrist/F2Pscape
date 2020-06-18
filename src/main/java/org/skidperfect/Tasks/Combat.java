package org.skidperfect.Tasks;

import org.rspeer.networking.dax.walker.models.RSBank;
import org.rspeer.runetek.api.component.tab.*;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.ui.Log;
import org.skidperfect.BotTask;

public class Combat extends BotTask {

    private enum TaskState {
        CHICKENS(Area.rectangular(3169, 3303, 3186, 3289), RSBank.LUMBRIDGE_TOP, 0, null, "Chicken", "Goblin"),
        //COWS(Area.rectangular(3240, 3298, 3265, 3276), RSBank.LUMBRIDGE_TOP, 10, new String[]{"Cowhide"},"Cow", "Cow calf"),
        RATS(Area.rectangular(3188, 3197, 3236, 3162), RSBank.LUMBRIDGE_TOP, 10, null, "Giant rat"),
        FROGS(Area.rectangular(3188, 3197, 3236, 3162), RSBank.LUMBRIDGE_TOP, 20, null, "Giant frog");

        private final Area area;
        private final RSBank bank;
        private final int requiredCbtLvl;
        private final String[] mobNames;
        private final String[] pickDrops;

        TaskState(Area area, RSBank bank, int requiredLvl, String[] drops, String... mobNames) {
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

    private static TaskState currentState;

    @Override
    public int run() {
        if(currentLvl != Skills.getCurrentLevel(Skill.STRENGTH)) {
            currentLvl = Skills.getCurrentLevel(Skill.STRENGTH);
            Log.info("Current Combat Level" + Players.getLocal().getCombatLevel());
            update(currentLvl);
            return 300;
        }


        return 300;
    }

    @Override
    public void update(int lvl) {
        for (TaskState state: TaskState.values()) {
            if(lvl >= state.getRequiredCbtLvl()) {
                currentState = state;
            }
        }
        super.update(lvl);
    }
}
