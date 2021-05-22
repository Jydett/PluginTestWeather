package fr.opus.weather;

import fr.jydet.api.config.PaperConfig;
import fr.jydet.api.config.YamlWithCommentConfiguration;
import fr.jydet.api.utils.ItemStackUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WeatherConfig {
    public static final String ERROR_INVALID_TIME_VALUE = "Heure invalide : '%val%' n'est pas un nombre";
    public static final String ERROR_INVALID_TIME_VALUE_2 = "Heure invalide : '%val%' doit être entre 0 et 23999";
    public static final String MESSAGE_TIME_CHANGED = "Heure mise à %val%";
    public static String GUI_TIME_TITLE = "Quelle heure ? (heure * 1000)";
    public static String GUI_TITLE = "La météo";
    public static String ERROR_MESSAGE_INVENTORY_FULL = ChatColor.RED + "Ton inventaire est plein";

    public static int CHANGE_DURATION_TICK = 300 * 20;

    public static ItemStack OPEN_GUI_ITEM = ItemStackUtils.setAppearance(new ItemStack(Material.WHITE_DYE, 1), ChatColor.GOLD + "Ouvrir le menu météo");

    public static ItemStack GUI_ITEM_CLEAR = ItemStackUtils.setAppearance(new ItemStack(Material.BARRIER, 1), ChatColor.WHITE + "Faire venir le beau temps");
    public static int CLEAR_ITEM_POSITION = 1;
    public static String MESSAGE_CLEAR_EXECUTED = ChatColor.GREEN + "Le beau temps est de retour !";

    public static ItemStack GUI_ITEM_RAIN = ItemStackUtils.setAppearance(new ItemStack(Material.LIGHT_BLUE_DYE, 1), ChatColor.AQUA + "Faire venir la pluie");
    public static int RAIN_ITEM_POSITION = 2;
    public static String MESSAGE_RAIN_EXECUTED = ChatColor.GREEN + "La pluie est de retour !";

    public static ItemStack GUI_ITEM_STORM = ItemStackUtils.setAppearance(new ItemStack(Material.YELLOW_DYE, 1), ChatColor.GOLD + "Faire venir l'orage");
    public static int STORM_ITEM_POSITION = 3;
    public static String MESSAGE_STORM_EXECUTED = ChatColor.GREEN + "La tonnerre est de retour !";

    public static ItemStack GUI_ITEM_CLOCK = ItemStackUtils.setAppearance(new ItemStack(Material.CLOCK, 1), ChatColor.GREEN + "Regler l'heure", Arrays.asList(ChatColor.GRAY + "La valeur à donnée", ChatColor.GRAY + "est un nombre entre", ChatColor.GRAY + "0 et 23999 et ", ChatColor.GRAY + "correspond à " + ChatColor.ITALIC + ChatColor.WHITE + "heure * 1000"));
    public static int CLOCK_ITEM_POSITION = 4;

    public static String WEATHERGUI_COMMAND_PERMISSION = "opus.weathergui.give";
    public static String WEATHERGUI_OPEN_PERMISSION = "opus.weathergui.open";
    public static String NO_COMMAND_PERMISSION = ChatColor.RED + "Tu n'a pas la permission d'utiliser cet objet";


    private final PaperConfig paperConfig;

    public WeatherConfig(WeatherPlugin plugin) throws InvalidConfigurationException {
        paperConfig = new PaperConfig(plugin, "config.yml", false);

        loadConfig(paperConfig);
        checkConfig();

        paperConfig.saveConfig();

        final ItemMeta itemMeta = OPEN_GUI_ITEM.getItemMeta();
        itemMeta.getPersistentDataContainer().set(plugin.getWeatherKey(), PersistentDataType.BYTE, (byte) 0);
        OPEN_GUI_ITEM.setItemMeta(itemMeta);
    }

    private void loadConfig(PaperConfig paperConfig) throws InvalidConfigurationException {
        YamlWithCommentConfiguration config;

        if (paperConfig.configFileExists()) {
            paperConfig.reloadConfig();
            config = paperConfig.getConfig();
        } else {
            config = paperConfig.newConfig();
        }

        loadFromConfigOrSaveDefault(config, "change_tick_duration", CHANGE_DURATION_TICK, o -> CHANGE_DURATION_TICK = o);
        loadFromConfigOrSaveDefault(config, "gui_title", GUI_TITLE, o -> GUI_TITLE = o);
        loadFromConfigOrSaveDefault(config, "weathergui_command_permission", WEATHERGUI_COMMAND_PERMISSION,
            o -> WEATHERGUI_COMMAND_PERMISSION = o);
        loadFromConfigOrSaveDefault(config, "weathergui_open_permission", WEATHERGUI_OPEN_PERMISSION,
            o -> WEATHERGUI_OPEN_PERMISSION = o);
        loadFromConfigOrSaveDefault(config, "gui_title", GUI_TITLE, o -> GUI_TITLE = o);
        loadFromConfigOrSaveDefault(config, "error_message_inventory_full", ERROR_MESSAGE_INVENTORY_FULL,
            o -> ERROR_MESSAGE_INVENTORY_FULL = o);
        loadFromConfigOrSaveDefault(config, "open_gui_item", OPEN_GUI_ITEM, o -> OPEN_GUI_ITEM = o);
        loadFromConfigOrSaveDefault(config, "gui_item_clear", GUI_ITEM_CLEAR, o -> GUI_ITEM_CLEAR = o);
        loadFromConfigOrSaveDefault(config, "gui_item_rain", GUI_ITEM_RAIN, o -> GUI_ITEM_RAIN = o);
        loadFromConfigOrSaveDefault(config, "gui_item_thunder", GUI_ITEM_STORM, o -> GUI_ITEM_STORM = o);
        loadFromConfigOrSaveDefault(config, "rain_item_position", RAIN_ITEM_POSITION, o -> RAIN_ITEM_POSITION = o);
        loadFromConfigOrSaveDefault(config, "clear_item_position", CLEAR_ITEM_POSITION, o -> CLEAR_ITEM_POSITION = o);
        loadFromConfigOrSaveDefault(config, "storm_item_position", STORM_ITEM_POSITION, o -> STORM_ITEM_POSITION = o);
        loadFromConfigOrSaveDefault(config, "clock_item_position", CLOCK_ITEM_POSITION, o -> CLOCK_ITEM_POSITION = o);
    }

    private void checkConfig() throws InvalidConfigurationException {
        if (CHANGE_DURATION_TICK < 0) {
            throw new InvalidConfigurationException("the 'change_tick_duration' should be positive");
        }

        if (CLEAR_ITEM_POSITION < -1 || CLEAR_ITEM_POSITION > 26) {
            throw new InvalidConfigurationException("the 'clear_item_position' should be between -1 and 26 (inclusive)");
        }

        if (RAIN_ITEM_POSITION < -1 || RAIN_ITEM_POSITION > 26) {
            throw new InvalidConfigurationException("the 'rain_item_position' should be between -1 and 26 (inclusive)");
        }

        if (STORM_ITEM_POSITION < -1 || STORM_ITEM_POSITION > 26) {
            throw new InvalidConfigurationException("the 'storm_item_position' should be between -1 and 26 (inclusive)");
        }

        if (CLOCK_ITEM_POSITION < -1 || CLOCK_ITEM_POSITION > 26) {
            throw new InvalidConfigurationException("the 'clock_item_position' should be between -1 and 26 (inclusive)");
        }

        //On check que les items n'utilisent pas la même position
        final Map<Integer, Long> positionByOccurrences = Stream.of(CLEAR_ITEM_POSITION, RAIN_ITEM_POSITION, STORM_ITEM_POSITION, CLOCK_ITEM_POSITION)
            .filter(i -> i != -1)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        for (Map.Entry<Integer, Long> entry : positionByOccurrences.entrySet()) {
            if (entry.getValue() > 1) {
                throw new InvalidConfigurationException("Position " + entry.getKey() + " is used by more than one item !");
            }
        }
    }

    private static <T> void loadFromConfigOrSaveDefault(YamlWithCommentConfiguration config, String key, T obj, Consumer<T> setter) throws InvalidConfigurationException {
        if (config.contains(key)) {
            try {
                final T setted = (T) config.get(key, obj);
                setter.accept(setted);
            } catch (ClassCastException e) {
                throw new InvalidConfigurationException("The configuration for the key '" + key + "' is not of a valid type.");
            }
        } else {
            config.set(key, obj);
        }
    }
}
