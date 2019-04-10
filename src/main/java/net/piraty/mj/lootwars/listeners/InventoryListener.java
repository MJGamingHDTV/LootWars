package net.piraty.mj.lootwars.listeners;

import net.piraty.mj.lootwars.LootWars;
import net.piraty.mj.lootwars.managers.InventoryManager;
import net.piraty.mj.lootwars.utils.Data;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InventoryListener implements Listener {

    private final LootWars lootWars;
    private Map<Player, Set<ItemStack>> playerKit = new HashMap<>();

    public InventoryListener(@NotNull LootWars lootWars) {
        this.lootWars = lootWars;
        lootWars.registerListeners(this);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent interactEvent) {
        if (interactEvent.getItem() == null) return;
        if (interactEvent.getItem().getType() == null) return;
        if (interactEvent.getItem().getType().equals(Material.AIR)) return;
        if (!(interactEvent.getAction().equals(Action.RIGHT_CLICK_AIR) || interactEvent.getAction().equals(Action.RIGHT_CLICK_BLOCK) || interactEvent.getAction().equals(Action.LEFT_CLICK_AIR) || interactEvent.getAction().equals(Action.LEFT_CLICK_BLOCK)))
            return;
        Player player = interactEvent.getPlayer();
        if (interactEvent.getMaterial().equals(Material.CHEST)) {
            player.openInventory(InventoryManager.getKitInventory());
        }
        if (interactEvent.getMaterial().equals(Material.SKULL_ITEM)) {
            player.openInventory(InventoryManager.getTeamInventory());
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent clickEvent) {
        if (clickEvent.getClickedInventory() == null) return;
        if (clickEvent.getClickedInventory().getType() == null) return;
        if (clickEvent.getCurrentItem() == null) return;
        if (clickEvent.getCurrentItem().getType() == null) return;
        if (clickEvent.getCurrentItem().getType().equals(Material.AIR)) return;
        Player player = (Player) clickEvent.getWhoClicked();
        if (clickEvent.getClickedInventory().getTitle().equalsIgnoreCase("§6§lKits")) {
            for (String nameAndItem : lootWars.getKitManager().getKits().keySet()) {
                String[] spliced = nameAndItem.split("#");
                String name = spliced[0];
                if (clickEvent.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(name)) {
                    player.sendMessage(Data.PREFIX + "§aDu hast das Kit " + name + " §a ausgewählt!");
                    Set<ItemStack> items = new HashSet<>(lootWars.getKitManager().getKits().get(nameAndItem));
                    if (!playerKit.containsKey(player))
                        playerKit.put(player, items);
                    else
                        playerKit.replace(player, items);
                }
            }
            player.closeInventory();
        }
        if (clickEvent.getClickedInventory().getTitle().equalsIgnoreCase("§6§lTeams")) {
            ItemStack itemStack = clickEvent.getCurrentItem().clone();
            String displayName = itemStack.getItemMeta().getDisplayName();
            int teamID = Integer.parseInt(displayName.split("-")[1]);
            if (!lootWars.getTeamManager().isPlayerInTeam(teamID, player)) {
                lootWars.getTeamManager().addPlayerToTeam(teamID, player);
                player.sendMessage(Data.PREFIX + "§6Du bist nun im Team " + teamID);
            } else
                player.sendMessage(Data.PREFIX + "§cDu bist bereits in diesem Team!");
            player.closeInventory();
        }
    }

    public Map<Player, Set<ItemStack>> getPlayerKit() {
        return playerKit;
    }
}

