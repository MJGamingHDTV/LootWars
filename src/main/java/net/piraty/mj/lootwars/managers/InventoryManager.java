package net.piraty.mj.lootwars.managers;

import net.piraty.mj.lootwars.LootWars;
import net.piraty.mj.lootwars.utils.ITeam;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InventoryManager {

    public static Inventory getKitInventory() {
        Inventory kitInventory = Bukkit.createInventory(null, 18, "§6§lKits");
        Map<String, Set<ItemStack>> kits = LootWars.getInstance().getKitManager().getKits();
        for (String nameAndItem : kits.keySet()) {
            String[] spliced = nameAndItem.split("#");
            ItemStack itemStack = new ItemStack(Material.getMaterial(spliced[1]));
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(spliced[0]);

            List<String> lore = new ArrayList<>();
            for (ItemStack item : kits.get(nameAndItem)) {
                lore.add("§7" + item.getAmount() + "x " + item.getType().toString().toLowerCase());
                ItemMeta enchantingMeta = item.getItemMeta();
                try {
                    if (!enchantingMeta.getEnchants().isEmpty()) {
                        for (Enchantment enchantment : enchantingMeta.getEnchants().keySet()) {
                            lore.add(" - §7" + enchantment.getName().toLowerCase() + " " + enchantingMeta.getEnchantLevel(enchantment));
                        }
                    }
                } catch (NullPointerException ignore) {}
            }
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            kitInventory.addItem(itemStack);
        }
        return kitInventory;
    }

    public static Inventory getTeamInventory() {
        Inventory teamInventory = Bukkit.createInventory(null, 18, "§6§lTeams");
        for (Integer teamId : TeamManager.teams.keySet()) {
            ItemStack teamStack = new ItemStack(Material.NETHER_STAR);
            ItemMeta teamMeta = teamStack.getItemMeta();
            ITeam ITeam = TeamManager.teams.get(teamId);
            List<String> lore = new ArrayList<>();
            for (Player player : ITeam.getPlayers()) {
                lore.add(player.getDisplayName());
                teamMeta.setLore(lore);
            }
            teamMeta.setDisplayName("Team-" + teamId);
            teamStack.setItemMeta(teamMeta);
            teamInventory.addItem(teamStack);
        }
        return teamInventory;
    }
}
