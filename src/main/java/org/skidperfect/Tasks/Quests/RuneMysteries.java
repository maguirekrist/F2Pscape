package org.skidperfect.Tasks.Quests;

import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.api.Varps;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.ui.Log;
import org.skidperfect.Tasks.Questing;

public class RuneMysteries extends IQuest {

    private final int VARP_ID = 63;
    private final String[] REQ_ITEMS = null;

    private final Area STARTING_AREA = Area.rectangular(3208, 3225, 3213, 3218, 1);
    private final Area WIZARD_BASEMENT = Area.rectangular(3095, 9574, 3107, 9566);
    private final Area VARROCK_RUNESHOP = Area.rectangular(3250, 3404, 3255, 3399);

    @Override
    public int run() {
        Player localPlayer = Players.getLocal();
        if(getQValue() == 0) {
            if(STARTING_AREA.contains(localPlayer.getPosition())) {
                processDialog("Duke Horacio", new int[]{0 , 0});
                return 500;
            } else {
                Movement.getDaxWalker().walkTo(STARTING_AREA.getCenter());
                return 500;
            }
        } else if(getQValue() == 1) {
            if(WIZARD_BASEMENT.contains(localPlayer.getPosition())) {
                processDialog("Sedridor", 2, 0, 0);
                return 500;
            } else {
                Movement.getDaxWalker().walkTo(WIZARD_BASEMENT.getCenter());
                return 600;
            }
        } else if(getQValue() == 3) {
            if(VARROCK_RUNESHOP.contains(localPlayer.getPosition())) {
                processDialog("Aubury", 2);
                return 500;
            } else {
                Movement.getDaxWalker().walkTo(VARROCK_RUNESHOP.getCenter());
                return 600;
            }
        } else if(getQValue() == 4) {
            if(VARROCK_RUNESHOP.contains(localPlayer.getPosition())) {
                processDialog("Aubury");
                return 500;
            } else {
                Movement.getDaxWalker().walkTo(VARROCK_RUNESHOP.getCenter());
                return 600;
            }
        } else if(getQValue() == 5) {
            if(WIZARD_BASEMENT.contains(localPlayer.getPosition())) {
                processDialog("Sedridor");
                return 500;
            } else {
                Movement.getDaxWalker().walkTo(WIZARD_BASEMENT.getCenter());
                return 600;
            }
        } else if(getQValue() == 6) {
            Log.fine("Rune Mysteries Completed!");
            return 301;
        }
        return 300;
    }

    @Override
    public int getQValue() {
        return Varps.get(VARP_ID);
    }

}
