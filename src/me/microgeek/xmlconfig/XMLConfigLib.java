package me.microgeek.xmlconfig;

import me.microgeek.xmlconfig.config.XMLConfig;
import me.microgeek.xmlconfig.config.XMLUtilities;

import org.bukkit.plugin.java.JavaPlugin;

public class XMLConfigLib extends JavaPlugin {

  /*
   * (non-Javadoc)
   * @see org.bukkit.plugin.java.JavaPlugin#onDisable()
   */
  @Override
  public void onDisable() {
      XMLUtilities.saveAllConfigs();
  }

  /*
   * (non-Javadoc)
   * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
   */
  @Override
  public void onEnable() {
      XMLConfig xc = XMLUtilities.loadConfig("test", this);
      System.out.println(xc.getItemStack("asdf.itemstack_1"));
  }
}
