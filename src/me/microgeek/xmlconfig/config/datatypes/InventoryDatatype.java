package me.microgeek.xmlconfig.config.datatypes;


import java.util.Arrays;
import java.util.Map.Entry;

import org.bukkit.inventory.ItemStack;

import me.microgeek.xmlconfig.config.XMLConfig;

public class InventoryDatatype implements Datatype {

    @Override
    public Object parsed(String path, XMLConfig config) {
        ItemStack[] items = new ItemStack[36];
        for(Entry<String, Object> e : config.getKeysAndValues(path, false).entrySet()) {
            items[Integer.parseInt(e.getKey())] = config.getItemStack(path + "." + e.getKey());
        }
        System.out.println(Arrays.toString(items));
        return items;
    }

}
