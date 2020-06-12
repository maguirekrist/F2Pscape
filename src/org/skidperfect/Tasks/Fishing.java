package org.skidperfect.Tasks;

import org.rspeer.networking.dax.walker.models.RSBank;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.adapter.scene.SceneObject;
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
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.ui.Log;
import org.skidperfect.BotTask;

public class Fishing extends BotTask  {

    public enum TaskData {
        SHRIMPS(RSBank.DRAYNOR, Area.rectangular(2984, 3178, 2999, 3154), 0, "Small Net", new String[]{"Small fishing net"}, new String[]{"Raw shrimps"}, true, false),
        TROUT(RSBank.VARROCK_WEST, Area.rectangular(3099, 3437, 3110, 3422), 20,"Lure", new String[]{"Feather", "Fly fishing rod"}, new String[]{"Raw trout", "Raw Salmon"}, false, false),
        LOBSTER(RSBank.DRAYNOR, Area.rectangular(2924, 3180, 2925, 3176), 40, "Lure", new String[]{"Lobster pot"}, new String[]{"Raw lobster"}, false, true),
        SWORDFISH(RSBank.DRAYNOR, Area.rectangular(2924, 3180, 2925, 3176), 50, "Harpoon", new String[]{"Harpoon"}, new String[]{"Raw swordfish", "Raw tuna"}, false, true);

        private RSBank rsBank;
        private Area area;
        private int requiredLvl;
        private String action;
        private String[] tools;
        private String[] fish;
        private boolean doDrop;
        private boolean portMethod;

        TaskData(RSBank bank, Area area, int requiredLvl, String action,  String[] tools, String[] fish, boolean doDrop, boolean portMethod) {
            this.rsBank = bank;
            this.area = area;
            this.requiredLvl = requiredLvl;
            this.tools = tools;
            this.fish = fish;
            this.doDrop = doDrop;
            this.portMethod = portMethod;
        }

        public RSBank getRsBank() {
            return rsBank;
        }

        public Area getArea() {
            return area;
        }

        public int getRequiredLvl() {
            return requiredLvl;
        }

        public String[] getTools() {
            return tools;
        }

        public String[] getFish() {
            return fish;
        }

        public boolean isDoDrop() {
            return doDrop;
        }

        public boolean isPortMethod() {
            return portMethod;
        }

        public String getAction() {
            return action;
        }
    }

    public static TaskData currentTask;

    @Override
    public void run() {
        if(currentLvl != Skills.getCurrentLevel(Skill.FISHING)) {
            currentLvl = Skills.getCurrentLevel(Skill.FISHING);
            Log.info("Current " + Skill.FISHING.toString() + " Level" + currentLvl);
            update(currentLvl);
        }

        if(Equipment.contains(currentTask.getTools()) || Inventory.contains(currentTask.getTools())) {
            if(!Inventory.isFull()) {
                if(currentTask.getArea().contains(Players.getLocal().getPosition())) {
                    Npc pool = Npcs.getNearest("Fishing spot");
                    if(pool == null) {
                        Movement.walkToRandomized(currentTask.getArea().getCenter());
                    }
                    if(Players.getLocal().getAnimation() == -1 && pool != null) {
                        if(pool.interact("Small Net")) {
                            if (Time.sleepUntil(() -> Players.getLocal().getAnimation() == 621, Random.high(3000, 4500))) {
                                Log.severe("Interacting with " + pool.getId());
                                Time.sleepUntil(() -> Players.getLocal().getAnimation() == -1 || Inventory.isFull() || Dialog.isOpen(), Random.nextInt(8*60000, 10*60000));
                            }
                        }
                    }
                } else if(currentTask.isPortMethod() == true) {
                    //todo
                } else {
                    Movement.getDaxWalker().walkTo(currentTask.getArea().getCenter());
                }
            } else {
                if(currentTask.isDoDrop()) {
                    for (String in: currentTask.getFish()) {
                        for (Item item : Inventory.getItems(i -> i.getName().equalsIgnoreCase(in))) {
                                if (item.interact("Drop")) Time.sleep(Random.high(750, 1250));
                        }
                    }
                } else if(currentTask.isPortMethod() == false) {
                    Movement.getDaxWalker().walkToBank(currentTask.getRsBank());
                    if(bankAtBank() && Bank.isOpen()) {
                        if(Bank.depositAllExcept(currentTask.getTools())) {
                            Time.sleepUntil(() -> Inventory.containsOnly(currentTask.getTools()), Random.nextInt(4000, 6000));
                        }
                        if(Bank.close()) {
                            Time.sleepUntil(() -> Bank.isClosed(), Random.nextInt(800, 1600));
                        }
                    }
                } else if(currentTask.isPortMethod() == true) {

                }
            }
        } else {
            Movement.getDaxWalker().walkToBank(currentTask.getRsBank());
            if(bankAtBank() && Bank.isOpen()) {
                if(!Inventory.isEmpty()) {
                    if(Bank.depositInventory()) {
                        Time.sleepUntil(() -> Inventory.isEmpty(), Random.nextInt(4000, 6000));
                    }
                }
                for (String item: currentTask.getTools()) {
                    if(Bank.withdrawAll(item)) Time.sleepUntil(() -> Inventory.contains(currentTask.getTools()), Random.nextInt(4000, 8000));
                }
                if(Bank.close()) {
                    Time.sleepUntil(() -> Bank.isClosed(), Random.nextInt(800, 1600));
                }
            }
        }

    }

    @Override
    public void update(int lvl) {
        if(lvl < 20) {
            currentTask = TaskData.SHRIMPS;
        } else if(lvl < 40) {
            currentTask = TaskData.TROUT;
        } else if(lvl < 50) {
            currentTask = TaskData.LOBSTER;
        } else if (lvl < 99) {
            currentTask = TaskData.SWORDFISH;
        }
    }
}
