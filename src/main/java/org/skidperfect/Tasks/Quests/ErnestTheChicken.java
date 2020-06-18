package org.skidperfect.Tasks.Quests;

import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.Pickable;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.Varps;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.scene.Pickables;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.ui.Log;
import sun.security.x509.AVA;

public class ErnestTheChicken extends IQuest {
    private final int VARP_ID = 32;

    private final Area MASION_ENTRANCE = Area.rectangular(3107, 3332, 3113, 3327);
    private final Area POISON_ROOM = Area.rectangular(3097, 3366, 3101, 3364);
    private final Area FISH_ROOM = Area.rectangular(3107, 3361, 3110, 3354, 1);
    private final Area PROFESSOR_ROOM = Area.rectangular(3108, 3370, 3112, 3362, 2);
    private final Area PUZZEL_ROOM = Area.rectangular(3089, 9767, 3118, 9745);
    private final Area FOUNTAIN = Area.rectangular(3084, 3338, 3091, 3331);
    private final Area SHELF_ROOM = Area.rectangular(3097, 3363, 3104, 3354);
    private final Area AVA_ROOM = Area.rectangular(3091, 3363, 3096, 3354);
    private final Area STORAGE_ROOM = Area.rectangular(3120, 3360, 3126, 3354);
    private final Area HEAP = Area.rectangular(3087, 3358, 3084, 3365);
    private final Area TUBE_ROOM = Area.rectangular(3108, 3368, 3112, 3366);

    @Override
    public int run() {
        Player local = Players.getLocal();
        if(Dialog.isOpen() && Dialog.canContinue()) {
            Dialog.processContinue();
            return 1500;
        }
        switch(getQValue()) {
            case 0:
                if(MASION_ENTRANCE.contains(local.getPosition())) {
                    processDialog("Veronica", 0);
                    return 800;
                } else {
                    Movement.getDaxWalker().walkTo(MASION_ENTRANCE.getCenter());
                    return 500;
                }
            case 1:
                if(Inventory.contains("Poisoned fish food") && !Inventory.contains("Oil can")) {
                    if (PUZZEL_ROOM.contains(local.getPosition())) {
                        Log.fine("Solving puzzle...");
                        SceneObject leverA = SceneObjects.getNearest("Lever A");
                        SceneObject leverB = SceneObjects.getNearest("Lever B");
                        SceneObject leverC = SceneObjects.getNearest("Lever C");
                        SceneObject leverD = SceneObjects.getNearest("Lever D");
                        SceneObject leverE = SceneObjects.getNearest("Lever E");
                        SceneObject leverF = SceneObjects.getNearest("Lever F");

                        SceneObject door1 = SceneObjects.getNearest(144);
                        SceneObject door2 = SceneObjects.getNearest(137);
                        SceneObject door3 = SceneObjects.getNearest(142);
                        SceneObject door4 = SceneObjects.getNearest(139);
                        SceneObject door5 = SceneObjects.getNearest(145);
                        SceneObject door6 = SceneObjects.getNearest(140);
                        SceneObject door7 = SceneObjects.getNearest(143);
                        SceneObject door8 = SceneObjects.getNearest(138);
                        SceneObject finalDoor = SceneObjects.getNearest(141);

                        if(leverA.interact("Pull")) {
                            Time.sleepUntil(()-> Varps.get(668) == 132, 10000);
                        }
                        Time.sleep(1200, 1500);
                        if(leverB.interact("Pull")) {
                            Time.sleepUntil(()-> Varps.get(33) == 6, 10000);
                        }
                        Time.sleep(1800, 2000);
                        if(door1.interact("Open")) {
                            Time.sleepUntil(() -> !Movement.isDestinationSet(), 60000);
                            Time.sleep(2000, 2500);
                        }
                        if(leverD.interact("Pull")) {
                            Time.sleepUntil(()-> Varps.get(668) == 396, 10000);
                        }
                        Time.sleep(1800, 2600);
                        if(door1.interact("Open")) {
                            Time.sleepUntil(() -> !Movement.isDestinationSet(), 60000);
                            Time.sleep(2000, 2500);
                        }
                        Time.sleep(1200, 1500);
                        if(leverA.interact("Pull")) {
                            Time.sleepUntil(()-> Varps.get(668) == 264, 10000);
                        }
                        Time.sleep(1200, 1500);
                        if(leverB.interact("Pull")) {
                            Time.sleepUntil(()-> Varps.get(33) == 16, 10000);
                        }
                        Time.sleep(1200, 1500);
                        if(door5.interact("Open")) {
                            Time.sleepUntil(() -> !Movement.isDestinationSet(), 60000);
                        }
                        Time.sleep(2000, 2500);
                        if(door6.interact("Open")) {
                            Time.sleepUntil(() -> !Movement.isDestinationSet(), 60000);
                        }
                        Time.sleep(2000, 2500);
                        if(door7.interact("Open")) {
                            Time.sleepUntil(() -> !Movement.isDestinationSet(), 60000);
                        }
                        Time.sleep(2000, 2500);
                        if(leverF.interact("Pull")) {
                            Time.sleepUntil(()-> Varps.get(33) == 80, 10000);
                        }
                        Time.sleep(1800, 2000);
                        if(leverE.interact("Pull")) {
                            Time.sleepUntil(()-> Varps.get(33) == 112, 10000);
                        }
                        Time.sleep(2000, 2500);
                        if(door8.interact("Open")) {
                            Time.sleepUntil(() -> !Movement.isDestinationSet(), 60000);
                        }
                        Time.sleep(2000, 2500);
                        if(door2.interact("Open")) {
                            Time.sleepUntil(() -> !Movement.isDestinationSet(), 60000);
                        }
                        if(leverC.interact("Pull")) {
                            Time.sleepUntil(()-> Varps.get(33) == 120, 10000);
                        }
                        Time.sleep(1800, 2000);
                        if(door2.interact("Open")) {
                            Time.sleepUntil(() -> !Movement.isDestinationSet(), 60000);
                        }
                        Time.sleep(2000, 2500);
                        if(door8.interact("Open")) {
                            Time.sleepUntil(() -> !Movement.isDestinationSet(), 60000);
                        }
                        Time.sleep(1800, 2000);
                        if(leverE.interact("Pull")) {
                            Time.sleepUntil(()-> Varps.get(33) == 88, 10000);
                        }
                        Time.sleep(2000, 2500);
                        if(door8.interact("Open")) {
                            Time.sleepUntil(() -> !Movement.isDestinationSet(), 60000);
                        }
                        Time.sleep(2000, 2500);
                        if(door3.interact("Open")) {
                            Time.sleepUntil(() -> !Movement.isDestinationSet(), 60000);
                        }
                        Time.sleep(2000, 2500);
                        if(door5.interact("Open")) {
                            Time.sleepUntil(() -> !Movement.isDestinationSet(), 60000);
                        }
                        Time.sleep(2000, 2500);
                        if(finalDoor.interact("Open")) {
                            Time.sleepUntil(() -> !Movement.isDestinationSet(), 60000);
                        }
                        Time.sleep(2000, 2500);
                        Pickable oilCan = Pickables.getNearest("Oil can");
                        if(oilCan.interact("Take")) {
                            Time.sleepUntil(() -> Inventory.contains("Oil can"), 100000);
                        }
                        Time.sleep(2000, 2500);
                        if(finalDoor.interact("Open")) {
                            Time.sleepUntil(() -> !Movement.isDestinationSet(), 60000);
                        }
                        return 500;
                    } else {
                        if(SHELF_ROOM.contains(local.getPosition())) {
                            SceneObject bookCase = SceneObjects.newQuery().names("Bookcase").actions("Search").results().nearest();
                            if(bookCase.interact("Search")) {
                                Time.sleepUntil(()->AVA_ROOM.contains(local.getPosition()), 10000);
                                return 600;
                            }
                        } else if(AVA_ROOM.contains(local.getPosition())) {
                            SceneObject ladder = SceneObjects.newQuery().names("Ladder").actions("Climb-down").results().nearest();
                            if(ladder.interact("Climb-down")) {
                                Time.sleepUntil(()->PUZZEL_ROOM.contains(local.getPosition()), 20000);
                                return 600;
                            }
                        } else {
                            Movement.walkTo(SHELF_ROOM.getCenter());
                            return 300;
                        }
                        return 600;
                    }
                } else if(Inventory.contains("Oil can") && Inventory.contains("Poisoned fish food") && !Inventory.contains("Pressure gauge")) {
                    if(PUZZEL_ROOM.contains(local.getPosition())) {
                        Movement.getDaxWalker().walkTo(AVA_ROOM.getCenter());
                        return 800;
                    } else if(AVA_ROOM.contains(local.getPosition())) {
                        SceneObject lvr = SceneObjects.getNearest("Lever");
                        if(lvr.interact("Pull")) {
                            Time.sleepUntil(()->SHELF_ROOM.contains(local.getPosition()), 100000);
                        }
                    } else {
                        if(Inventory.contains("Key")) {
                            if(FOUNTAIN.contains(local.getPosition())) {
                                Item pff = Inventory.getFirst("Poisoned fish food");
                                SceneObject fnt = SceneObjects.getNearest("Fountain");
                                if(pff.interact("Use")) {
                                    Time.sleep(1000, 2000);
                                    if(fnt.interact("Use")) {
                                        Time.sleep(7000, 8000);
                                    }
                                }
                                if(!Inventory.contains("Poisoned fish food")) {
                                    if(fnt.interact("Search")) {
                                        while(!Inventory.contains("Pressure gauge")) {
                                            if(Dialog.isOpen() && Dialog.canContinue()) {
                                                Dialog.processContinue();
                                            }
                                        }
                                        return 500;
                                    }
                                }
                            } else {
                                Movement.getDaxWalker().walkTo(FOUNTAIN.getCenter());
                                return 500;
                            }
                        } else {
                            if(Inventory.contains("Spade")) {
                                if(HEAP.contains(local.getPosition())) {
                                    SceneObject heap = SceneObjects.getNearest("Compost heap");
                                    if(heap.interact("Search")) {
                                        Time.sleepUntil(()->Inventory.contains("Key"), 10000);
                                        return 400;
                                    }
                                } else {
                                    Movement.getDaxWalker().walkTo(HEAP.getCenter());
                                    return 500;
                                }
                            } else {
                                if(STORAGE_ROOM.contains(local.getPosition())) {
                                    Pickable spade = Pickables.getNearest("Spade");
                                    if(spade.interact("Take")) {
                                        Time.sleepUntil(()->Inventory.contains("Spade"), 20000);
                                        return 400;
                                    }
                                } else {
                                    Movement.getDaxWalker().walkTo(STORAGE_ROOM.getCenter());
                                    return 800;
                                }
                            }
                        }
                    }
                } else if(Inventory.contains("Pressure gauge") && !Inventory.contains("Poisoned fish food") && Inventory.contains("Oil can") && !Inventory.contains("Rubber tube")) {
                    if(TUBE_ROOM.contains(local.getPosition())) {
                        Pickable tube = Pickables.getNearest("Rubber tube");
                        if(tube.interact("Take")) {
                            Time.sleepUntil(()->Inventory.contains("Rubber tube"), 5000);
                            return 400;
                        }
                    } else {
                        Movement.getDaxWalker().walkTo(TUBE_ROOM.getCenter());
                        return 300;
                    }
                } else if(Inventory.contains("Pressure gauge") && Inventory.contains("Oil can") && Inventory.contains("Rubber tube")) {
                    if(PROFESSOR_ROOM.contains(local.getPosition())) {
                        processDialog("Professor Oddenstein", 0, 1);
                        Time.sleep(2000, 3000);
                        processDialog("Professor Oddenstein");
                        return 500;
                    } else {
                        Movement.getDaxWalker().walkTo(PROFESSOR_ROOM.getCenter());
                        return 650;
                    }
                } else {
                    if(Inventory.contains("Poison")) {
                        if(Inventory.contains("Fish food")) {
                            Item ffood = Inventory.getFirst("Fish food");
                            Item poison = Inventory.getFirst("Poison");
                            if(poison.interact("Use")) {
                                Time.sleep(1500, 3000);
                                ffood.interact("Use");
                                Time.sleepUntil(()->Inventory.contains("Poisoned fish food"), 700000);
                                return 800;
                            }
                        } else {
                            if(FISH_ROOM.contains(local.getPosition())) {
                                Pickable ffood = Pickables.getNearest("Fish food");
                                if(ffood.interact("Take")) {
                                    Time.sleepUntil(()->Inventory.contains("Fish food"), 4000);
                                    return 800;
                                }
                            } else {
                                Movement.getDaxWalker().walkTo(FISH_ROOM.getCenter());
                                return 500;
                            }
                        }
                    } else {
                        if(POISON_ROOM.contains(local.getPosition())) {
                            Pickable poison = Pickables.getNearest("Poison");
                            if(poison.interact("Take")) {
                                Time.sleepUntil(()->Inventory.contains("Poison"), 4000);
                                return 900;
                            }
                        } else {
                            Movement.getDaxWalker().walkTo(POISON_ROOM.getCenter());
                            return 500;
                        }
                    }
                }
            case 3:
                Log.fine("Ernest the Chicken completed!!");
                if(Interfaces.isOpen(277)) {
                    Interfaces.getComponent(277, 16).interact("Close");
                    return 600;
                }
                return 500;
        }
        return 300;
    }

    @Override
    public int getQValue() {
        return Varps.get(VARP_ID);
    }
}
