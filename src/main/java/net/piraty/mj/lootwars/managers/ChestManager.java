package net.piraty.mj.lootwars.managers;

import net.piraty.mj.lootwars.LootWars;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ChestManager {

    private YamlConfiguration yamlConfiguration;

    public ChestManager(@NotNull LootWars lootWars) {
        File chestFile = new File(lootWars.getDataFolder().getAbsolutePath(), "chests.yml");
        yamlConfiguration = YamlConfiguration.loadConfiguration(chestFile);
        List<String> defaults = new ArrayList<>();
        yamlConfiguration.options().copyDefaults(true);
        defaults.add("stone-0,stone-1;50%");
        defaults.add("diamond_sword-0,iron_sword-0;100%;FIRE_ASPECT-2#100%");
        yamlConfiguration.addDefault("Items", defaults);
    }

    private List<ItemStack> getItems() {
        List<ItemStack> itemsList = new ArrayList<>();
        //stone-1,stone-2;100%;Enchanting-Level#20%;Enchanting-Level#25%
        List<String> itemList = yamlConfiguration.getStringList("Items");
        for (String items : itemList) {
            String[] split = items.split(";");
            String itemsAndTypes = split[0];
            String percentage = split[1].replace("%", "");
            String enchanting1 = null;
            String enchanting2 = null;
            String enchanting3 = null;
            if (split.length == 3)
                enchanting1 = split[2];
            if (split.length == 4)
                enchanting2 = split[3];
            if (split.length == 5)
                enchanting3 = split[4];

            String[] itemWithType = itemsAndTypes.split(",");
            List<String> item = new ArrayList<>();
            List<Short> types = new ArrayList<>();
            for (int i = itemWithType.length - 1; i >= 0; i--) {
                String[] itemAndType = itemWithType[i].split("-");
                item.add(itemAndType[0]);
                types.add(Short.parseShort(itemAndType[1]));
            }
            int itemsSize = item.size();
            int typeSize = types.size();
            Random random = new Random();
            int randomItem = random.nextInt(itemsSize);
            int randomType = random.nextInt(typeSize);
            ItemStack itemStack = new ItemStack(Material.valueOf(item.get(randomItem).toUpperCase()));
            itemStack.setDurability(types.get(randomType));
            int amount = random.nextInt(itemStack.getMaxStackSize() + 1);
            if (amount == 0)
                amount = 1;
            itemStack.setAmount(amount);

            if (enchanting1 != null) {
                String[] enchantingPlusLevelAndPercentage = enchanting1.split("#");
                String[] enchantingAndLevel = enchantingPlusLevelAndPercentage[0].split("-");
                String percent = enchantingPlusLevelAndPercentage[1];
                String enchanting = enchantingAndLevel[0];
                int level = Integer.valueOf(enchantingAndLevel[1]);
                Enchantment enchantment = Enchantment.getByName(enchanting.toUpperCase());
                float enchantmentPercent = Float.valueOf(percent.replace("%", "")) / 100;
                float enchantmentChance = random.nextFloat();
                if (enchantmentChance <= enchantmentPercent) {
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.addEnchant(enchantment, level, true);
                    itemStack.setItemMeta(itemMeta);
                }
            }

            if (enchanting2 != null) {
                String[] enchantingPlusLevelAndPercentage = enchanting2.split("#");
                String[] enchantingAndLevel = enchantingPlusLevelAndPercentage[0].split("-");
                String percent = enchantingPlusLevelAndPercentage[1];
                String enchanting = enchantingAndLevel[0];
                int level = Integer.valueOf(enchantingAndLevel[1]);
                Enchantment enchantment = Enchantment.getByName(enchanting.toUpperCase());
                float enchantmentPercent = Float.valueOf(percent.replace("%", "")) / 100;
                float enchantmentChance = random.nextFloat();
                if (enchantmentChance <= enchantmentPercent) {
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.addEnchant(enchantment, level, true);
                    itemStack.setItemMeta(itemMeta);
                }
            }

            if (enchanting3 != null) {
                String[] enchantingPlusLevelAndPercentage = enchanting3.split("#");
                String[] enchantingAndLevel = enchantingPlusLevelAndPercentage[0].split("-");
                String percent = enchantingPlusLevelAndPercentage[1];
                String enchanting = enchantingAndLevel[0];
                int level = Integer.valueOf(enchantingAndLevel[1]);
                Enchantment enchantment = Enchantment.getByName(enchanting.toUpperCase());
                float enchantmentPercent = Float.valueOf(percent.replace("%", "")) / 100;
                float enchantmentChance = random.nextFloat();
                if (enchantmentChance <= enchantmentPercent) {
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.addEnchant(enchantment, level, true);
                    itemStack.setItemMeta(itemMeta);
                }
            }

            float percent = Float.valueOf(percentage) / 100;
            float chance = random.nextFloat();
            if (chance <= percent) {
                itemsList.add(itemStack);
            }
        }
        return itemsList;
    }

    void detectChests(String worldName) {
        List<Chunk> chunks = new ArrayList<>(Arrays.asList(Bukkit.getWorld(worldName).getLoadedChunks()));
        for (Chunk chunk : chunks) {
            for (BlockState blockState : chunk.getTileEntities()) {
                if (blockState instanceof Chest) {
                    fillChest((Chest) blockState);
                }
            }
        }
    }

    private void fillChest(@NotNull Chest chest) {
        chest.getBlockInventory().clear();
        for (ItemStack itemStack : getItems()) {
            try {
                chest.getBlockInventory().addItem(itemStack);
            } catch (NullPointerException ignored) {}
        }
    }
}
