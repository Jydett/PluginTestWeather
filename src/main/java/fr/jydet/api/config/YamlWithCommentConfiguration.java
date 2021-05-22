package fr.jydet.api.config;

import fr.jydet.api.annotation.NotNull;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A subclass of {@link YamlConfiguration} which support comments
 * <i>This <b>doesn't</b> support comment on the same line as the value !</i>
 */
public class YamlWithCommentConfiguration extends YamlConfiguration {

    private final Map<String, List<String>> comments;

    public YamlWithCommentConfiguration() {
        comments = new HashMap<>();
    }

    /**
     * Add a comment to the configuration, it will be printed above the
     * given #key
     * @param key the key of the comment
     * @param newComments the comments to add
     */
    public void addComment(@NotNull String key, @NotNull List<String> newComments) {
        List<String> strings = comments.get(key);
        if (strings != null) {
            strings.addAll(newComments);
        } else {
            comments.put(key, new ArrayList<>(newComments));
        }
    }

    @Override
    public String saveToString() {
        String contents = super.saveToString();

        List<String> list = new ArrayList<>();
        Collections.addAll(list, contents.split("\n"));

        int currentLayer = 0;
        StringBuilder currentPath = new StringBuilder();

        StringBuilder sb = new StringBuilder();

        int lineNumber = 0;
        for(Iterator<String> iterator = list.iterator(); iterator.hasNext(); lineNumber++) {
            String line = iterator.next();

            if(!line.isEmpty()) {
                if(line.contains(":")) {

                    int layerFromLine = getLayerFromLine(line, lineNumber);

                    if(layerFromLine < currentLayer) {
                        currentPath = new StringBuilder(regressPathBy(currentLayer - layerFromLine, currentPath.toString()));
                    }

                    String key = getKeyFromLine(line);

                    if(currentLayer == 0) {
                        currentPath = new StringBuilder(key);
                    }
                    else {
                        currentPath.append(".").append(key);
                    }

                    String path = currentPath.toString();
                    if(comments.containsKey(path)) {
                        comments.get(path).forEach(string -> {
                            sb.append('#');
                            sb.append(string);
                            sb.append('\n');
                        });
                    }
                }
            }


            sb.append(line);
            sb.append('\n');
        }

        return sb.toString();
    }


    @Override
    public void loadFromString(String contents) throws InvalidConfigurationException {
        super.loadFromString(contents);

        List<String> list = new ArrayList<>();
        Collections.addAll(list, contents.split("\n"));

        int currentLayer = 0;
        String currentPath = "";

        int lineNumber = 0;
        for(Iterator<String> iterator = list.iterator(); iterator.hasNext(); lineNumber++) {
            String line = iterator.next();

            String trimmed = line.trim();
            if(trimmed.startsWith("#") || trimmed.isEmpty()) {
                addCommentLine(currentPath, line);
                continue;
            }

            if(!line.isEmpty()) {
                if(line.contains(":")) {

                    int layerFromLine = getLayerFromLine(line, lineNumber);

                    if(layerFromLine < currentLayer) {
                        currentPath = regressPathBy(currentLayer - layerFromLine, currentPath);
                    }

                    String key = getKeyFromLine(line);

                    if(currentLayer == 0) {
                        currentPath = key;
                    }
                    else {
                        currentPath += "." + key;
                    }
                }
            }
        }
    }

    private void addCommentLine(String currentPath, String line) {
        List<String> list = comments.get(currentPath);
        if(list == null) {
            list = new ArrayList<>();
        }
        list.add(line);

        comments.put(currentPath, list);
    }

    private String getKeyFromLine(String line) {
        String key = null;

        for(int i = 0; i < line.length(); i++) {
            if(line.charAt(i) == ':') {
                key = line.substring(0, i);
                break;
            }
        }

        return key == null ? null : key.trim();
    }

    private String regressPathBy(int i, String currentPath) {
        if(i <= 0) {
            return currentPath;
        }
        String[] split = currentPath.split("\\.");

        StringBuilder rebuild = new StringBuilder();
        for(int j = 0; j < split.length - i; j++) {
            rebuild.append(split[j]);
            if(j <= (split.length - j)) {
                rebuild.append(".");
            }
        }

        return rebuild.toString();
    }

    private int getLayerFromLine(String line, int lineNumber) {
        double d = 0;
        for(int i = 0; i < line.length(); i++) {
            if(line.charAt(i) == ' ') {
                d += 0.5;
            }
            else {
                break;
            }
        }

        return (int) d;
    }
}
