package org.skidperfect;

import org.rspeer.runetek.api.commons.math.Random;

import java.util.Comparator;

import static org.rspeer.runetek.api.commons.math.Random.nextBoolean;

public class SortByPriority implements Comparator<BotTasks> {
    @Override
    public int compare(BotTasks o1, BotTasks o2) {
        int value =  o1.getPriority() - o2.getPriority();
        boolean rand = Random.nextBoolean();
        if(value == 0) {
            if(rand) {
                o1.setPriority(o1.getPriority() + 1);
            } else {
                o2.setPriority(o2.getPriority() + 1);
            }
        }
        value =  o1.getPriority() - o2.getPriority();
        return value;
    }
}
