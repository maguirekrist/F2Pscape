package org.skidperfect;

import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.ui.Log;
import org.skidperfect.Tasks.Intermission;
import org.skidperfect.Tasks.Questing;

import java.util.ArrayList;
import java.util.List;

public class BotTaskManager {

    private static BotTasks currentTask;
    private static long timeTilNxtTask;
    private static boolean intermission;
    private static int prioChecker;
    private static List<BotTasks> taskList = new ArrayList<BotTasks>();

    public static void update() {
        if(Main.runTime >= timeTilNxtTask) {
            if(currentTask != BotTasks.INTERMISSION) {
                currentTask = BotTasks.INTERMISSION;
                timeTilNxtTask = Main.runTime + Random.nextLong(10*60000, 26*60000);
            } else {
                timeTilNxtTask = Main.runTime + Random.nextLong(40*60000, 80*60000);
                prioChecker += 1;
                if(taskList.get(prioChecker) == BotTasks.QUESTING) {
                    if(Questing.isAllQuestDone()) {
                        prioChecker += 1;
                        currentTask = taskList.get(prioChecker);
                    } else {
                        currentTask = taskList.get(prioChecker);
                    }
                } else {
                    currentTask = taskList.get(prioChecker);
                }
            }

            Log.fine(currentTask + " " + currentTask.getPriority());
        }
    }

    public static void initializeManager() {
        intermission = false;
        prioChecker = 0;
        taskList.add(BotTasks.FISHING);
        taskList.add(BotTasks.WOOD_CUTTING);
        taskList.add(BotTasks.COMBAT);
        taskList.add(BotTasks.FIREMAKING);
        if(!Questing.isAllQuestDone()) {
            taskList.add(BotTasks.QUESTING);
        }
        taskList.sort(new SortByPriority());
        timeTilNxtTask = Random.nextLong(30*60000, 60*60000);
        currentTask = taskList.get(prioChecker);
    }

    public static BotTasks getCurrentTask() {
        return currentTask;
    }

    public static long getTimeTilNxtTask() { return timeTilNxtTask; }

    public static boolean isIntermission() { return intermission; }

    public static void setIntermission(boolean val) { intermission = val; }

}
