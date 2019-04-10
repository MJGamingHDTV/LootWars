package net.piraty.mj.lootwars.managers;

import net.piraty.mj.lootwars.LootWars;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

public class KitManager {

    private File kitFile;
    private YamlConfiguration yamlConfiguration;
    private Map<String, Set<ItemStack>> kits = new HashMap<>();

    public KitManager(@NotNull LootWars lootWars) {
        kitFile = new File(lootWars.getDataFolder().getAbsolutePath(), "kits.yml");
        yamlConfiguration = YamlConfiguration.loadConfiguration(kitFile);
        addKits();
    }

    private void addKits() {
        for (String name : yamlConfiguration.getConfigurationSection("kits").getKeys(false)) {
            Set<ItemStack> itemStacks = new HashSet<>();
            List<String> list = yamlConfiguration.getStringList("kits." + name);
            for (String items : list) {
                String[] spliced = items.split(";");
                String item = spliced[0].split("#")[0];
                String enchanting1 = null;
                String enchanting2 = null;
                String enchanting3 = null;
                if (spliced.length == 2)
                    enchanting1 = spliced[1];
                if (spliced.length == 3)
                    enchanting2 = spliced[2];
                if (spliced.length == 4)
                    enchanting3 = spliced[3];
                short type = Short.parseShort(spliced[0].split("#")[1]);
                int amount = Integer.parseInt(spliced[0].split("#")[2]);
                ItemStack itemStack = new ItemStack(Material.valueOf(item.toUpperCase()), amount, type);
                if (enchanting1 != null) {
                    Enchantment enchantment = Enchantment.getByName(spliced[1].split("#")[0].toUpperCase());
                    int level = Integer.parseInt(spliced[1].split("#")[1]);
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.addEnchant(enchantment, level, true);
                    itemStack.setItemMeta(itemMeta);
                }
                if (enchanting2 != null) {
                    Enchantment enchantment = Enchantment.getByName(spliced[2].split("#")[0].toUpperCase());
                    int level = Integer.parseInt(spliced[2].split("#")[1]);
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.addEnchant(enchantment, level, true);
                    itemStack.setItemMeta(itemMeta);
                }
                if (enchanting3 != null) {
                    Enchantment enchantment = Enchantment.getByName(spliced[3].split("#")[0].toUpperCase());
                    int level = Integer.parseInt(spliced[3].split("#")[1]);
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.addEnchant(enchantment, level, true);
                    itemStack.setItemMeta(itemMeta);
                }
                itemStacks.add(itemStack);
            }
            kits.put(name, itemStacks);
        }
    }

    public Map<String, Set<ItemStack>> getKits() {
        return kits;
    }
}
