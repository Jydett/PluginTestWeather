package fr.opus.weather;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandAlias("weathergui")
@CommandPermission("%weatherGuiPerm")
public class WeatherGiveCommand extends BaseCommand {

    @Default
    public void give(Player player) {
        final ItemStack openGuiItem = WeatherConfig.OPEN_GUI_ITEM.clone();
        if (! player.getInventory().addItem(openGuiItem).isEmpty()) {
            player.sendMessage(WeatherConfig.ERROR_MESSAGE_INVENTORY_FULL);
        }
    }
}