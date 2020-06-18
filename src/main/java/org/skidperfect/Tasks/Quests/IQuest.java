package org.skidperfect.Tasks.Quests;

import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.api.Game;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.ui.Log;

public abstract class IQuest {

    public int run() {
        return 300;
    }

    public int getQValue() {
        return 0;
    }

    public void processDialog(String npcName, int... options) {
        Npc duke = Npcs.getNearest(npcName);
        if(Players.getLocal().getAnimation() == -1 && !Movement.isDestinationSet() && !Game.isInCutscene() && !Dialog.isOpen()) {
            if(duke.interact("Talk-to")) {
                if(Time.sleepUntil(()-> Dialog.isOpen() && Dialog.canContinue(), Random.nextInt(7000, 8000))) {
                    quitDialog:  while(Dialog.isOpen()) {
                        for (int option: options) {
                            boolean canEnterOption = Dialog.isViewingChatOptions();
                            while(!canEnterOption) {
                                if(Dialog.canContinue() && !Dialog.isProcessing()) {
                                    Dialog.processContinue();
                                    Time.sleep(100, 1200);
                                    Time.sleepUntil(()-> !Dialog.isProcessing(), Random.nextInt(800, 1700));
                                } else if(Dialog.isProcessing()) {
                                    Time.sleep(700, 1000);
                                } else if(Dialog.isViewingChatOptions()) {
                                    canEnterOption = true;
                                    break;
                                } else if(!Dialog.isOpen()) {
                                    break quitDialog;
                                }
                            }
                            if(canEnterOption) {
                                Dialog.process(option);
                                Time.sleep(600, 1000);
                                Time.sleepUntil(()-> !Dialog.isProcessing(), Random.nextInt(700, 1200));
                            }
                        }
                        if(Dialog.canContinue() && !Dialog.isProcessing()) {
                            Dialog.processContinue();
                            Time.sleep(1000, 3000);
                            Time.sleepUntil(()-> !Dialog.isProcessing(), Random.nextInt(1000, 2200));
                        } else if(Dialog.isProcessing()) {
                            Time.sleep(600, 800);
                        } else {
                            break;
                        }
                    }
                }
            }
        }
    }
    public void processDialog(String npcName) {
        Npc duke = Npcs.getNearest(npcName);
        if(Players.getLocal().getAnimation() == -1 && !Movement.isDestinationSet() && !Game.isInCutscene() && !Dialog.isOpen()) {
            if(duke.interact("Talk-to")) {
                if(Time.sleepUntil(()-> Dialog.isOpen() && Dialog.canContinue(), Random.nextInt(7000, 8000))) {
                    while(Dialog.isOpen()) {
                        if(Dialog.canContinue() && !Dialog.isProcessing()) {
                            Dialog.processContinue();
                            Time.sleep(1500, 2000);
                            Time.sleepUntil(()-> !Dialog.isProcessing(), Random.nextInt(800, 1000));
                        } else if(Dialog.isProcessing()) {
                            Time.sleep(600, 1000);
                        } else {
                            break;
                        }
                    }
                }
            }
        }
    }
}
