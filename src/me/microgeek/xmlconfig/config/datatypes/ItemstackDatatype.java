package me.microgeek.xmlconfig.config.datatypes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.microgeek.xmlconfig.config.XMLConfig;

public class ItemstackDatatype implements DataType {

    @Override
    public Object parsed(String path, XMLConfig config) {
        Map<String, Object> itemMap = new HashMap<String, Object>();
        Map<String, Object> rawMap = config.getKeysAndValues(path, true);
        Map<String, Object> enchantmentMap = config.getKeysAndValues(path + ".enchantments", false);
        List<String> lore = config.getStringList(path + ".lore");

        new ItemStackValueVerifier("type", config.getString(path + ".type")).addToItemMap(itemMap);
        new ItemStackValueVerifier("amount", config.getInt(path + ".amount")).addToItemMap(itemMap);
        new ItemStackValueVerifier("damage", config.getShort(path + ".damage")).addToItemMap(itemMap);
        new ItemStackValueVerifier("enchantments", enchantmentMap).addToItemMap(itemMap);
        
        System.out.println(itemMap);
        ItemStack item = ItemStack.deserialize(itemMap);
        ItemMeta meta = item.getItemMeta();

        if(rawMap.containsKey("name")) {
            meta.setDisplayName(rawMap.get("name").toString());
        }
        for(Entry<String, Object> e : enchantmentMap.entrySet()) {
            meta.addEnchant(Enchantment.getByName(e.getKey()), Integer.parseInt(e.getValue().toString()), false);
        }
        if(lore != null) {
            meta.setLore(Arrays.asList(config.getValues(path + ".lore", false).toArray(new String[0])));
        }

        item.setItemMeta(meta);
        return item;
    }

    private class ItemStackValueVerifier {

        private Object object;
        private String path;
        
        public ItemStackValueVerifier(String path, Object object) {
            this.object = object;
            this.path = path;
        }
        
        public void addToItemMap(Map<String, Object> map) {
            if(object != null) {
                map.put(path, object);
            }
        }

    }

}
