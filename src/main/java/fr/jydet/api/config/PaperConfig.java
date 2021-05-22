package fr.jydet.api.config;

import com.google.common.base.Charsets;
import fr.jydet.api.annotation.NotNull;
import fr.jydet.api.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.logging.Level;

/**
 * A utility class to handle paper config files with ease
 * @author Jydet
 */
public class PaperConfig {
    private final String fileName;
    private final Plugin plugin;
    private final URL configInJarUrl;

    private final File externalConfigFile;
    private YamlWithCommentConfiguration fileConfiguration;
    private YamlWithCommentConfiguration defaults;

    /**
     * Create a new PaperConfig linked to the provided path
     * @param plugin your paper plugin
     * @param fileName the name of your config file (relative path from your ressource folder WITH extension)
     */
    public PaperConfig(@NotNull Plugin plugin, @NotNull String fileName) {
        this(plugin, fileName, true);
    }

    /**
     * Create a new PaperConfig linked to the provided path
     * @param plugin your paper plugin
     * @param fileName the name of your config file (relative path from your ressource folder WITH extension)
     * @param saveDefault whether the default config will be save immediately
     */
    public PaperConfig(@NotNull Plugin plugin, @NotNull String fileName, boolean saveDefault) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.configInJarUrl = plugin.getClass().getClassLoader().getResource(fileName);
        this.externalConfigFile = new File(plugin.getDataFolder(), fileName);
        this.defaults = getDefaults();
        if (saveDefault) {
            saveDefaultConfig();
            reloadConfig();
        }
    }

    /**
     * Reload the config file
     */
    public void reloadConfig() {
        if (externalConfigFile != null) {
            try {
                fileConfiguration = loadConfigurationWithComment(new FileInputStream(externalConfigFile), Charsets.UTF_8);
                if (fileConfiguration != null && defaults != null) {
                    fileConfiguration.setDefaults(defaults);
                }
            } catch (FileNotFoundException e) {
                Bukkit.getLogger().severe("FileNotFoundException on reload config : " + e.getMessage());
            }
        }
    }

    /**
     * Get the FileConfiguration object
     * @return the configuration red from a file or the default configuration if it is null
     */
    @NotNull
    public YamlWithCommentConfiguration getConfig() {
        if (fileConfiguration == null) {
            if (defaults != null) {
                return defaults;
            } else {
                reloadConfig();
            }
        }
        return fileConfiguration;
    }

    /**
     * Generate a new config file and returns it
     * @return the new config file
     */
    public YamlWithCommentConfiguration newConfig() {
        fileConfiguration = new YamlWithCommentConfiguration();
        return fileConfiguration;
    }

    /**
     * Get the default configuration aka the file of the same name at the root of the jar
     * @return the default configuration if it exists, otherwise null
     */
    @Nullable
    private YamlWithCommentConfiguration getDefaults() {
        if (defaults == null) {
            // Look for defaults in the jar
            InputStream defConfigStream = plugin.getResource(fileName);
            if (defConfigStream != null) {
                return loadConfigurationWithComment(defConfigStream, null);
            }
        }
        return defaults;
    }

    /**
     * Instanciate a new {@link YamlWithCommentConfiguration} from the given #defConfigStream and an optional #charSet
     * @param defConfigStream inputStream containing the data of the configFile
     * @param charSet required if you are reading from a file
     * @return a loaded configuration with comment support or null if an error occurred.
     */
    @Nullable
    private YamlWithCommentConfiguration loadConfigurationWithComment(@NotNull InputStream defConfigStream, @Nullable Charset charSet) {
        YamlWithCommentConfiguration config = new YamlWithCommentConfiguration();

        try {
            InputStreamReader reader;
            if (charSet == null) {
                reader = new InputStreamReader(defConfigStream);
            } else {
                reader = new InputStreamReader(defConfigStream, charSet);
            }
            config.load(reader);
        } catch (IOException | InvalidConfigurationException var3) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load configuration from stream", var3);
            return null;
        }

        return config;
    }

    /**
     * Save the configuration in the plugin folder
     */
    public void saveConfig() {
        if (fileConfiguration != null && externalConfigFile != null) {
            try {
                getConfig().save(externalConfigFile);
            } catch (IOException ex) {
                plugin.getLogger().log(Level.SEVERE, "Could not save config to " + externalConfigFile, ex);
            }
        }
    }

    /**
     * Save the default configuration in the plugin folder if
     * the plugin folder doesn't exist
     */
    public void saveDefaultConfig() {
        if (! externalConfigFile.exists()) {
            if (internalConfigFileExists()) {
                try {
                    this.plugin.saveResource(fileName, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (defaults != null) {
                try {
                    defaults.save(externalConfigFile);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * @return wether the config file exists in the plugin folder
     */
    public boolean configFileExists() {
        return externalConfigFile.exists();
    }

    public File getExternalConfigFile() {
        return externalConfigFile;
    }

    /**
     * @return whether the config file exists in the plugin jar
     */
    public boolean internalConfigFileExists() {
        return configInJarUrl != null;
    }

    /**
     * Sets the default config for this configuration file
     * @param defaults a map of the defaults key, values
     */
    public void setDefault(Map<String, Object> defaults) {
        YamlWithCommentConfiguration res = new YamlWithCommentConfiguration();
        defaults.forEach(res::set);
        this.defaults = res;
    }
}