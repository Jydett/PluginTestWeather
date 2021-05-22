package fr.jydet.api.utils;

import org.bukkit.Bukkit;

/**
 * Utils methods related to Bukkit
 * @author Jydet
 */
public final class BukkitUtils {

    /**
     * Get the MNS version
     */
    public static final String MAPPING_VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1);

}