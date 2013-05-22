package me.microgeek.xmlconfig.config;

import java.io.File;
import java.util.Collection;

import org.bukkit.plugin.java.JavaPlugin;

public class XMLUtilities {

	/**
	 * Needs a lot of work
	 */

	public static void saveAllConfigs() {
		for(XMLConfig x : getAllConfigs()) {
			if(x.isSave()) {
				x.saveConfig();
			}
		}
	}

	public static XMLConfig loadConfig(String name, JavaPlugin plugin) {
	    File path = new File(plugin.getDataFolder(), name + ".xml");
		return new XMLConfig(path, plugin);
	}

	public static XMLConfig newConfig(String name, JavaPlugin plugin, boolean saveOnExit) {
		return null;
	}

	public static Collection<XMLConfig> getAllConfigs() {
		return XMLWrapper.XML_CONFIGS.values();
	}
}
