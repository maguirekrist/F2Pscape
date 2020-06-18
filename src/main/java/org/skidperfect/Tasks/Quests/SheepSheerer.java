package org.skidperfect.Tasks.Quests;

import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.Varps;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.Production;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.Scene;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.ui.Log;

public class SheepSheerer extends IQuest {
    private final int VARP_ID = 179;
    private final String[] REQ_ITEMS = null;

    private final Area LUMBRIDGE_FARM = Area.rectangular(3184, 3275, 3192, 3270);
    private final Area LUMBRIDGE_SHEEPS = Area.rectangular(3193, 3276, 3212, 3257);
    private final Area LUMBRIDGE_SPIN = Area.rectangular(3208, 3216, 3213, 3212, 1);

    @Override
    public int run() {
        Player local = Players.getLocal();
        if (Movement.getRunEnergy() > 25 && !Movement.isRunEnabled()) {
            Movement.toggleRun(!Movement.isRunEnabled());
        }
        if(getQValue() == 0) {
            Log.severe("Starting Quest!");
            if(LUMBRIDGE_FARM.contains(local.getPosition())) {
                processDialog("Fred the Farmer", 0, 0, 0);
                return 400;
            } else {
                Movement.getDaxWalker().walkTo(LUMBRIDGE_FARM.getCenter());
                return 500;
            }
        } else if(getQValue() == 1) {
            if(Inventory.getCount("Wool") + Inventory.getCount("Ball of Wool") < 20) {
                if(LUMBRIDGE_SHEEPS.contains(local.getPosition())) {
                    Npc sheep = Npcs.getNearest(n -> n.getName().equals("Sheep") && n.isPositionInteractable() && n.containsAction("Shear"));
                    int woolCount = Inventory.getCount("Wool");
                    if(sheep != null) {
                        Time.sleepUntil(()->sheep.interact("Shear"), Random.nextInt(3000, 5000));
                        Time.sleepUntil(()->Inventory.getCount("Wool") > woolCount, Random.nextInt(1000, 3000));
                        return 300;
                    }
                } else {
                    Movement.getDaxWalker().walkTo(LUMBRIDGE_SHEEPS.getCenter());
                    return 400;
                }
            } else if(Inventory.getCount("Ball of Wool") + Inventory.getCount("Wool") >= 20 && Inventory.contains("Wool")) {
                if(LUMBRIDGE_SPIN.contains(local.getPosition())) {
                    SceneObject spinningWheel = SceneObjects.getNearest("Spinning wheel");
                    if(spinningWheel != null) {
                        Time.sleepUntil(()->spinningWheel.interact("Spin"), Random.nextInt(2000, 4000));
                        Time.sleepUntil(()->Production.isOpen(), Random.nextInt(5000, 6500));
                        if(Production.isOpen()) {
                            if(Production.initiate("Ball of Wool")) {
                                Time.sleepUntil(()->Inventory.getCount("Ball of Wool") >= 20, Random.nextInt(20000, 25000));
                                return 300;
                            }
                        }
                    }
                } else {
                    Movement.getDaxWalker().walkTo(LUMBRIDGE_SPIN.getCenter());
                    return 500;
                }
            } else if(Inventory.getCount("Ball of Wool") >= 20 && !Inventory.contains("Wool")) {
                if(LUMBRIDGE_FARM.contains(local.getPosition())) {
                    processDialog("Fred the Farmer", 0);
                    return 400;
                } else {
                    Movement.getDaxWalker().walkTo(LUMBRIDGE_FARM.getCenter());
                    return 500;
                }
            }
        } else if(getQValue() == 21) {
            Log.fine("Sheep Sheerer Completed!");
            return 301;
        }
        return 300;
    }

    @Override
    public int getQValue() {
        return Varps.get(VARP_ID);
    }
}
