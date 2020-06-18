package org.skidperfect.Tasks.Quests;

import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.Varps;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.InterfaceAddress;
import org.rspeer.runetek.api.component.InterfaceOptions;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.runetek.api.component.tab.Equipment;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.component.tab.Spell;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.ui.Log;

public class RestlessGhost extends IQuest {
    private final int VARP_ID = 107;

    private final Area LUMBRIDGE_CHURCH = Area.rectangular(3240, 3215, 3247, 3204);
    private final Area LUMSWAMP_HOUSE = Area.rectangular(3143, 3178, 3152, 3172);
    private final Area LUMBRIDGE_GRAVE=  Area.rectangular(3247, 3195, 3252, 3190);
    private final Area WIZARDTOWER_ALTAR = Area.rectangular(3111, 9569, 3121, 9564);

    @Override
    public int run() {
        Player local = Players.getLocal();
        if(Dialog.isOpen() && Dialog.canContinue()) {
            Dialog.processContinue();
            return 1000;
        }
        if(Interfaces.isOpen(277)) {
            Interfaces.getComponent(277, 16).interact("Close");
            return 600;
        }
        switch (getQValue()) {
            case 0:
                if(LUMBRIDGE_CHURCH.contains(local.getPosition())) {
                    processDialog("Father Aereck", 2, 0);
                    return 800;
                } else {
                    Movement.getDaxWalker().walkTo(LUMBRIDGE_CHURCH.getCenter());
                    return 500;
                }
            case 1:
                if(LUMSWAMP_HOUSE.contains(local.getPosition())) {
                    processDialog("Father Urhney", 1, 0);
                    return 800;
                } else {
                    Movement.getDaxWalker().walkTo(LUMSWAMP_HOUSE.getCenter());
                    return 500;
                }
            case 2:
                if(LUMBRIDGE_GRAVE.contains(local.getPosition())) {
                    SceneObject coffin = SceneObjects.getNearest("Coffin");
                    Npc ghost = Npcs.newQuery().names("Restless ghost").results().nearest();
                    if(ghost != null && coffin.containsAction("Close")) {
                        if(Equipment.contains("Ghostspeak amulet")) {
                            processDialog("Restless ghost", 0);
                            return 800;
                        } else {
                            Item ghostSpeak = Inventory.newQuery().names("Ghostspeak amulet").actions("Wear").results().first();
                            if(ghostSpeak.interact("Wear")) {
                                Time.sleepUntil(()->Equipment.contains("Ghostspeak amulet"), Random.nextInt(2000, 3000));
                                return 500;
                            }
                        }
                    } else {
                        if(coffin.interact("Open")) {
                            Time.sleepUntil(() -> coffin.containsAction("Close") && ghost != null, Random.nextInt(1000, 2000));
                            return Random.nextInt(1500, 2500);
                        }
                    }
                } else {
                    Movement.getDaxWalker().walkTo(LUMBRIDGE_GRAVE.getCenter());
                    return 500;
                }
            case 3:
                if(WIZARDTOWER_ALTAR.contains(local.getPosition())) {
                    SceneObject altar = SceneObjects.newQuery().names("Altar").actions("Search").results().nearest();
                    if(altar.interact("Search")) {
                        Time.sleepUntil(()->Inventory.contains("Ghost's skull"), Random.nextInt(600, 1200));
                        return 300;
                    }
                } else {
                    Movement.getDaxWalker().walkTo(WIZARDTOWER_ALTAR.getCenter());
                    return 500;
                }
            case 4:
                if(LUMBRIDGE_GRAVE.contains(local.getPosition())) {
                    SceneObject coffin = SceneObjects.getNearest("Coffin");
                    Npc ghost = Npcs.newQuery().names("Restless ghost").results().nearest();
                    Item skull = Inventory.newQuery().names("Ghost's skull").results().first();
                    if(ghost != null && coffin.containsAction("Close")) {
                        if(skull.interact("Use")) {
                            Time.sleep(1000, 2000);
                            coffin.click();
                            Time.sleepUntil(()->getQValue() == 5, Random.nextInt(8000, 10000));
                            return 1000;
                        }
                    } else {
                        if(coffin.interact("Open")) {
                            Time.sleepUntil(()-> ghost != null, Random.nextInt(5000, 6000));
                            return 900;
                        }
                    }
                } else {
                    Movement.getDaxWalker().walkTo(LUMBRIDGE_GRAVE.getCenter());
                    return 500;
                }
            case 5:
                Log.fine("Restless Ghost Completed!");
                return 301;
        }
        return 300;
    }

    @Override
    public int getQValue() {
        return Varps.get(VARP_ID);
    }
}
