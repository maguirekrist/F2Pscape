package org.skidperfect.Tasks.Quests;

import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.Game;
import org.rspeer.runetek.api.Varps;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.ui.Log;

public class RomeoNJuliet extends IQuest {
    private final int VARP_ID = 144;

    private final Area VARROCK_SQUARE = Area.rectangular(3206, 3435, 3222, 3421);
    private final Area VARROCK_BALCON = Area.rectangular(3155, 3427, 3161, 3425, 1);
    private final Area VARROCK_CHRUCH = Area.rectangular(3252, 3488, 3259, 3471);
    private final Area CADAVA_BUSH = Area.rectangular(3262, 3374, 3278, 3361);
    private final Area VARROCK_APOTHOCARY = Area.rectangular(3192, 3406, 3198, 3402);

    @Override
    public int run() {
        Player local = Players.getLocal();
        if(Dialog.isOpen() && Dialog.canContinue()) {
            Dialog.processContinue();
            return 1500;
        }
        switch (getQValue()) {
            case 0:
                if(VARROCK_SQUARE.contains(local.getPosition())) {
                    processDialog("Romeo", 0, 0);
                    return 800;
                } else {
                    Movement.getDaxWalker().walkTo(VARROCK_SQUARE.getCenter());
                    return 500;
                }
            case 10:
                if(VARROCK_BALCON.contains(local.getPosition())) {
                    processDialog("Juliet", 0, 0);
                } else {
                    Movement.getDaxWalker().walkTo(VARROCK_BALCON.getCenter());
                    return 500;
                }
            case 20:
                if(VARROCK_SQUARE.contains(local.getPosition())) {
                    processDialog("Romeo", 3);
                    return 800;
                } else {
                    Movement.getDaxWalker().walkTo(VARROCK_SQUARE.getCenter());
                    return 500;
                }
            case 30:
                if(VARROCK_CHRUCH.contains(local.getPosition())) {
                    processDialog("Father Lawrence");
                    return 800;
                } else {
                    Movement.getDaxWalker().walkTo(VARROCK_CHRUCH.getCenter());
                    return 500;
                }
            case 40:
                if(Inventory.contains("Cadava berries")) {
                    if(VARROCK_APOTHOCARY.contains(local.getPosition())) {
                        processDialog("Apothecary", 1, 0);
                        return 800;
                    } else {
                        Movement.getDaxWalker().walkTo(VARROCK_APOTHOCARY.getCenter());
                        return 500;
                    }
                } else {
                    if(CADAVA_BUSH.contains(local.getPosition())) {
                        SceneObject bush = SceneObjects.getNearest("Cadava bush");
                        if(bush.interact("Pick-from")) {
                            Time.sleepUntil(()->Inventory.contains("Cadava berries"), Random.nextInt(3000, 5000));
                            return 600;
                        }
                        return 800;
                    } else {
                        Movement.getDaxWalker().walkTo(CADAVA_BUSH.getCenter());
                        return 500;
                    }
                }
            case 50:
                if(VARROCK_BALCON.contains(local.getPosition())) {
                    processDialog("Juliet");
                } else if(!Game.isInCutscene()) {
                    Movement.getDaxWalker().walkTo(VARROCK_BALCON.getCenter());
                    return 500;
                }
            case 60:
                if(VARROCK_SQUARE.contains(local.getPosition())) {
                    processDialog("Romeo");
                    return 800;
                } else if(!Game.isInCutscene()){
                    Movement.getDaxWalker().walkTo(VARROCK_SQUARE.getCenter());
                    return 500;
                }
            case 100:
                Log.fine("Romeo and Juliet completed!");
                if(Interfaces.isOpen(277)) {
                    Interfaces.getComponent(277, 16).interact("Close");
                    return 600;
                }
        }
        return 300;
    }

    @Override
    public int getQValue() {
        return Varps.get(144);
    }
}
