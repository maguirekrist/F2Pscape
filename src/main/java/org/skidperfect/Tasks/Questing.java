package org.skidperfect.Tasks;

import org.rspeer.networking.dax.walker.models.RSBank;
import org.rspeer.runetek.api.Varps;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.providers.RSVarps;
import org.rspeer.ui.Log;
import org.skidperfect.BotTask;
import org.skidperfect.Tasks.Quests.*;

import java.util.HashMap;
import java.util.Map;

public class Questing extends BotTask {

    private enum TaskData {
        RUNE_MYSTERIES(63, 6, null, new RuneMysteries(), (String) null),
        SHEEP_SHEERER(179, 21 ,null, new SheepSheerer(), (String) null),
        RESTLESS_GHOST(107, 5,null, new RestlessGhost(), (String) null),
        ROMEO_JULIET(144, 100, null, new RomeoNJuliet(), (String) null),
        ERNEST_THE_CHICKEN(32, 3, null, new ErnestTheChicken(), (String) null);

        private int varpId;
        private int completedValue;
        private Map skillRequirements;
        private IQuest quest;
        private String[] requiredItems;

        TaskData(int varpId, int completedValue, Map<Skill, Integer> skillReq, IQuest quest, String... requiredItems) {
            this.varpId = varpId;
            this.completedValue = completedValue;
            this.skillRequirements = skillReq;
            this.quest = quest;
            this.requiredItems = requiredItems;
        }


        public Map getSkillRequirements() {
            return skillRequirements;
        }

        public IQuest getQuest() {
            return quest;
        }

        public String[] getRequiredItems() {
            return requiredItems;
        }

        public int getVarpId() {
            return varpId;
        }

        public int getVarpValue() {
            return Varps.get(varpId);
        }

        public boolean isCompleted() {
            return Varps.get(this.getVarpId()) == this.completedValue;
        }
    }

    public static TaskData currentQuest;

    @Override
    public int run() {
        currentQuest = getQuestTodo();
        if(currentQuest != null) {
            Log.fine(currentQuest.toString());
            currentQuest.getQuest().run();
            return 500;
        } else {
            Log.fine("No more quest to do! ");
            return 301;
        }
    }

    public TaskData getQuestTodo() {
        for (TaskData quest: TaskData.values()) {
            if(!quest.isCompleted()) {
                return quest;
            }
        }
        return null;
    }

    public static boolean isAllQuestDone() {
        for (TaskData quest: TaskData.values()) {
            if(!quest.isCompleted()) {
               return false;
            }
        }
        return true;
    }
}
