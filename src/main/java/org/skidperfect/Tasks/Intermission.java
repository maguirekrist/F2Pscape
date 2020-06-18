package org.skidperfect.Tasks;

import org.rspeer.networking.dax.walker.models.RSBank;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.api.commons.BankLocation;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.tab.Equipment;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.ui.Log;
import org.skidperfect.BotTask;

public class Intermission extends BotTask {

    private final Area GRAND_EXCHANGE = Area.rectangular(3161, 3493, 3168, 3485);

    @Override
    public int run() {
        Player local = Players.getLocal();
        if(GRAND_EXCHANGE.contains(local)) {
            Log.severe("Intermission!");
            BankLocation grandExchange = BankLocation.GRAND_EXCHANGE;
            if(Bank.isOpen()) {
                if(Inventory.isEmpty()) {
                    if(Equipment.getItems().length != 0) {
                        if(Bank.depositEquipment()) {
                            Time.sleepUntil(()->Equipment.getItems().length  == 0, Random.nextInt(5000, 6000));
                            return 600;
                        }
                    }
                } else {
                    if(Bank.depositInventory()) {
                        Time.sleepUntil(()->Inventory.isEmpty(), Random.nextInt(5000, 6000));
                        return 500;
                    }
                }
            } else if(Bank.open(grandExchange)) {
                Time.sleepUntil(()->Bank.isOpen(), 5000);
                return 400;
            }
        } else {
            Movement.getDaxWalker().walkToBank(RSBank.GRAND_EXCHANGE);
        }
        return 300;
    }
}
