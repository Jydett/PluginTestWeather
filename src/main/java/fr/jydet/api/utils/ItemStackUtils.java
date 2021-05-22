package fr.jydet.api.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * Utilities to quickly build ItemStacks
 * @author Jydet
 */
public class ItemStackUtils {

    /**
     * Set the name and the lore of an ItemStack, if the given item
     * has no ItemMeta, the unmodified ItemStack is returned.
     * @param item the item to decorate
     * @param name the new name of the item
     * @param lores the new lore of the item
     * @return the decorated item
     */
    public static ItemStack setAppearance(ItemStack item, String name, List<String> lores) {
        ItemMeta i = item.getItemMeta();
        if (i != null) {
            i.setDisplayName(name);
            i.setLore(lores);
            item.setItemMeta(i);
        }
        return item;
    }

    /**
     * Set the name of an ItemStack, if the given item
     * has no ItemMeta, the unmodified ItemStack is returned.
     * @param item the item to decorate
     * @param name the new name of the item
     * @return the decorated item
     */
    public static ItemStack setAppearance(ItemStack item, String name) {
        ItemMeta i = item.getItemMeta();
        if (i != null) {
            i.setDisplayName(name);
            item.setItemMeta(i);
        }
        return item;
    }

    /**
     * Set the name and the modelData of an ItemStack, if the given item
     * has no ItemMeta, the unmodified ItemStack is returned.
     * @param item the item to decorate
     * @param name the new name of the item
     * @param modelData the modelData of the item is used to apply the matching model
     *                  of the item (the one with the matching 'custom_model_data' tag)
     * @return the decorated item
     */
    public static ItemStack setAppearance(ItemStack item, String name, int modelData) {
        ItemMeta i = item.getItemMeta();
        if (i != null) {
            i.setDisplayName(name);
            i.setCustomModelData(modelData);
            item.setItemMeta(i);
        }
        return item;
    }

    /**
     * Set the name, the modelData and the lore's lines of an ItemStack, if the given item
     * has no ItemMeta, the unmodified ItemStack is returned.
     * @param item the item to decorate
     * @param name the new name of the item
     * @param modelData the modelData of the item is used to apply the matching model
     *                  of the item (the one with the matching 'custom_model_data' tag)
     * @param lores the new lore of the item
     * @return the decorated item
     */
    public static ItemStack setAppearance(ItemStack item, String name, int modelData, List<String> lores) {
        ItemMeta i = item.getItemMeta();
        if (i != null) {
            i.setDisplayName(name);
            i.setLore(lores);
            i.setCustomModelData(modelData);
            item.setItemMeta(i);
        }
        return item;
    }
}
