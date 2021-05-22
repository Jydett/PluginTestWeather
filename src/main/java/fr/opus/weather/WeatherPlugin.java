package fr.opus.weather;

import co.aikar.commands.PaperCommandManager;
import fr.minuskube.inv.SmartInvsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public final class WeatherPlugin extends JavaPlugin implements Listener {

    private final NamespacedKey weatherKey;

    private WeatherConfig config;
    private WeatherGUI weatherGUI;

    public WeatherPlugin() {
        weatherKey = new NamespacedKey(this, "fr/opus/weather");
    }

    @Override
    public void onEnable() {
        try {
            config = new WeatherConfig(this);
        } catch (InvalidConfigurationException e) {
            Bukkit.getLogger().severe("Invalid config file ! Disabling WeatherGUI : " + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        weatherGUI = new WeatherGUI(this);

        getServer().getPluginManager().registerEvents(this, this);

        PaperCommandManager commandManager = new PaperCommandManager(this);
        // enable brigadier integration for paper servers
        commandManager.enableUnstableAPI("brigadier");
        // optional: enable unstable api to use help
        commandManager.enableUnstableAPI("help");

        commandManager.getCommandReplacements().addReplacement("sellregionAdminPerm", WeatherConfig.WEATHERGUI_COMMAND_PERMISSION);

        commandManager.registerCommand(new WeatherGiveCommand());

        SmartInvsPlugin.setPlugin(this);
    }

    @EventHandler
    public void onInventoryClick(PlayerInteractEvent event) {
        final ItemStack item = event.getItem();
        if (item != null) {
            final ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta != null && itemMeta.getPersistentDataContainer().has(weatherKey, PersistentDataType.BYTE)) {
                final Player player = event.getPlayer();
                if (player.hasPermission(WeatherConfig.WEATHERGUI_OPEN_PERMISSION)) {
                    weatherGUI.openWeatherGui(player);
                } else {
                    player.sendMessage(WeatherConfig.NO_COMMAND_PERMISSION);
                }
            }
        }
    }

    public NamespacedKey getWeatherKey() {
        return weatherKey;
    }
}