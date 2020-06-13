package org.skidperfect;

import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.ui.Log;

import java.util.ArrayList;
import java.util.List;

public class BotTaskManager {

    private static BotTasks currentTask;
    private static BotTasks nextTask;
   // private static boolean switchingTask = false;
    private static long timeTilNxtTask;
    private static List<BotTasks> taskList = new ArrayList<BotTasks>();

    public static void update() {
        currentTask = taskList.get(0);
        nextTask = taskList.get(1);
        Log.fine(currentTask + " " + currentTask.getPriority());
        if(Main.runTime >= timeTilNxtTask) {
            timeTilNxtTask = Main.runTime + Random.nextLong(40*60000, 80*60000);
            currentTask.setPriority(taskList.toArray().length - 1);
            nextTask.setPriority(1);
            taskList.sort(new SortByPriority());
        }
    }

    public static void initializeManager() {
        taskList.add(BotTasks.FISHING);
        taskList.add(BotTasks.WOOD_CUTTING);
        //taskList.add(BotTasks.FIREMAKING);
        taskList.add(BotTasks.COMBAT);
        taskList.sort(new SortByPriority());
        timeTilNxtTask = Random.nextLong(40*60000, 80*60000);
    }

    public static BotTasks getCurrentTask() {
        return currentTask;
    }

    public static BotTasks getNextTask() { return nextTask; }

    public static long getTimeTilNxtTask() { return timeTilNxtTask; }
}
