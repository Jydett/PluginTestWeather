package fr.opus.weather;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import static fr.opus.weather.WeatherConfig.*;

public class WeatherGUI {

    private final WeatherPlugin plugin;

    public WeatherGUI(WeatherPlugin plugin) {
        this.plugin = plugin;
    }

    public void openWeatherGui(Player player) {
        SmartInventory.builder()
            .title(GUI_TITLE)
            .size(3, 9)
            .type(InventoryType.CHEST)
            .noUpdateProvider((p, inv) -> {
                if (CLEAR_ITEM_POSITION != -1) {
                    inv.set(CLEAR_ITEM_POSITION, ClickableItem.from(GUI_ITEM_CLEAR,
                        ignored -> executeWeatherCommand(WeatherArg.CLEAR, p, CHANGE_DURATION_TICK)));
                }

                if (RAIN_ITEM_POSITION != -1) {
                    inv.set(RAIN_ITEM_POSITION, ClickableItem.from(GUI_ITEM_RAIN,
                        ignored -> executeWeatherCommand(WeatherArg.RAIN, p, CHANGE_DURATION_TICK)));
                }

                if (STORM_ITEM_POSITION != -1) {
                    inv.set(STORM_ITEM_POSITION, ClickableItem.from(GUI_ITEM_STORM,
                        ignored -> executeWeatherCommand(WeatherArg.STORM, p, CHANGE_DURATION_TICK)));
                }

                if (CLOCK_ITEM_POSITION != -1) {
                    inv.set(CLOCK_ITEM_POSITION, ClickableItem.from(GUI_ITEM_CLOCK,
                        ignored -> SmartInventory.anvilPrompt(plugin, GUI_TIME_TITLE, (res, pl) -> {
                            if (res != null) {
                                int value;
                                try {
                                    value = Integer.parseInt(res);
                                } catch (NumberFormatException e) {
                                    pl.sendMessage(ERROR_INVALID_TIME_VALUE.replace("%val%", res));
                                    return false;
                                }

                                if (value < 0 || value > 23999) {
                                    pl.sendMessage(ERROR_INVALID_TIME_VALUE_2.replace("%val%", res));
                                    return false;
                                }
                                pl.getWorld().setTime(value);
                                pl.sendMessage(MESSAGE_TIME_CHANGED.replace("%val%", res));
                            }
                            return false;
                        }).open(player)
                    ));
                }
            }).build()
            .open(player);
    }

    public void executeWeatherCommand(WeatherArg weatherArg, Player player, int duration) {
        World world = player.getWorld();
        world.setWeatherDuration(duration);
        world.setThunderDuration(duration);
        switch (weatherArg) {
            case CLEAR:
                world.setStorm(false);
                world.setThundering(false);
                player.sendMessage(MESSAGE_CLEAR_EXECUTED);
                break;
            case RAIN:
                world.setStorm(true);
                world.setThundering(false);
                player.sendMessage(MESSAGE_RAIN_EXECUTED);
                break;
            case STORM:
                world.setStorm(true);
                world.setThundering(true);
                player.sendMessage(MESSAGE_STORM_EXECUTED);
                break;
        }
    }

    private enum WeatherArg {
        CLEAR, RAIN, STORM
    }
}
