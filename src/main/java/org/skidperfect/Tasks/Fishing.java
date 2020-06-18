package org.skidperfect.Tasks;

import org.rspeer.RSPeer;
import org.rspeer.networking.dax.walker.models.RSBank;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.DepositBox;
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

    private final Area portSarimDepost = Area.rectangular(3040, 3238, 3053, 3232);
    private final Area posStuck1 = Area.rectangular(2952, 3145, 2963, 3137, 1);
    private final Area posStuck2 = Area.rectangular(3029, 3222, 3040, 3211, 1);

    public enum TaskData {
        SHRIMPS(RSBank.DRAYNOR, Area.rectangular(2984, 3178, 2999, 3154), 0, "Small Net", new String[]{"Small fishing net"}, new String[]{"Raw shrimps", "Raw anchovies"}, "Fishing spot", true, false),
        TROUT(RSBank.VARROCK_WEST, Area.rectangular(3099, 3437, 3110, 3422), 20,"Lure", new String[]{"Feather", "Fly fishing rod"}, new String[]{"Raw trout", "Raw Salmon"},"Rod Fishing spot", true, false),
        LOBSTER(RSBank.DRAYNOR, Area.rectangular(2922, 3181, 2927, 3173), 40, "Cage", new String[]{"Lobster pot", "Coins"}, new String[]{"Raw lobster"}, "Fishing spot", false, true),
        SWORDFISH(RSBank.DRAYNOR, Area.rectangular(2922, 3181, 2927, 3173), 50, "Harpoon", new String[]{"Harpoon", "Coins"}, new String[]{"Raw swordfish", "Raw tuna"}, "Fishing spot",false, true);

        private RSBank rsBank;
        private Area area;
        private int requiredLvl;
        private String action;
        private String[] tools;
        private String[] fish;
        private String spotName;
        private boolean doDrop;
        private boolean portMethod;

        TaskData(RSBank bank, Area area, int requiredLvl, String action,  String[] tools, String[] fish, String spotName, boolean doDrop, boolean portMethod) {
            this.rsBank = bank;
            this.area = area;
            this.requiredLvl = requiredLvl;
            this.tools = tools;
            this.fish = fish;
            this.doDrop = doDrop;
            this.portMethod = portMethod;
            this.action = action;
            this.spotName = spotName;
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

        public String getSpotName() {
            return spotName;
        }
    }

    public static TaskData currentTask;

    @Override
    public int run() {
        if(currentLvl != Skills.getCurrentLevel(Skill.FISHING)) {
            currentLvl = Skills.getCurrentLevel(Skill.FISHING);
            Log.info("Current " + Skill.FISHING.toString() + " Level" + currentLvl);
            update(currentLvl);
            return 300;
        }

        if(posStuck1.contains(Players.getLocal().getPosition()) || posStuck2.contains(Players.getLocal().getPosition())) {
            SceneObject plank = SceneObjects.getNearest("Gangplank");
            Log.severe("STUCK: FIXING! ");
            if(plank.interact("Cross")) Time.sleepUntil(() -> !posStuck1.contains(Players.getLocal().getPosition()) || !posStuck2.contains(Players.getLocal().getPosition()) , 10000);
            return 300;
        }

        if(Equipment.contains(currentTask.getTools()) || Inventory.contains(currentTask.getTools())) {
            if(!Inventory.isFull()) {
                if(currentTask.getArea().contains(Players.getLocal().getPosition())) {
                    Npc pool = Npcs.getNearest(n -> n.getName().equalsIgnoreCase(currentTask.spotName) && n.containsAction(currentTask.getAction()));
                    if(pool == null) {
                        Movement.walkToRandomized(currentTask.getArea().getCenter());
                        return 300;
                    }
                    if(Players.getLocal().getAnimation() == -1 && pool != null) {
                        if(pool.interact(currentTask.getAction())) {
                            if (Time.sleepUntil(() -> Players.getLocal().getAnimation() != -1, Random.high(3000, 4500))) {
                                Log.severe("Interacting with " + pool.getId());
                                Time.sleepUntil(() -> Players.getLocal().getAnimation() == -1 || Inventory.isFull() || Dialog.isOpen(), Random.nextInt(8*60000, 10*60000));
                                return 300;
                            }
                        } else {
                            return 300;
                        }
                    }
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
                    return 300;
                } else if(currentTask.isPortMethod() == false) {
                    Movement.getDaxWalker().walkToBank(currentTask.getRsBank());
                    return 300;
                } else if(currentTask.isPortMethod() == true) {
                    Movement.getDaxWalker().walkTo(portSarimDepost.getCenter());
                    if(portSarimDepost.contains(Players.getLocal().getPosition())) {
                        return 300;
                    }
                }
            }
        } else {
           return 300;
        }
        return 300;
    }

    @Override
    public void update(int lvl) {

    }
}
