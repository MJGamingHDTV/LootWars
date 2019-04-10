package net.piraty.mj.lootwars.listeners;

import net.piraty.mj.lootwars.LootWars;
import net.piraty.mj.lootwars.utils.Data;
import net.piraty.mj.lootwars.utils.GameState;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinQuitListener implements Listener {

    private final LootWars lootWars;

    public PlayerJoinQuitListener(@NotNull LootWars lootWars) {
        this.lootWars = lootWars;
        lootWars.registerListeners(this);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent joinEvent) {
        Player player = joinEvent.getPlayer();
        player.getInventory().clear();
        if (lootWars.getGameManager().getGameState().equals(GameState.LOBBY) || lootWars.getGameManager().getGameState().equals(GameState.LOBBY_END)) {
            player.setGameMode(GameMode.ADVENTURE);
            player.setFoodLevel(20);
            addItems(player);
            if (lootWars.getLocationManager().getLobby() != null)
                player.teleport(lootWars.getLocationManager().getLobby());
            else
                player.sendMessage(Data.PREFIX + "§cEs wurde noch keine Lobby-Position festgelegt!");
            if (Bukkit.getOnlinePlayers().size() >= Data.MIN_PLAYERS) {
                if (lootWars.getGameManager().getBukkitTask() == null)
                    lootWars.getGameManager().gameScheduler();
            }
        } else {
            player.setGameMode(GameMode.SPECTATOR);
            player.teleport(lootWars.getLocationManager().getSpectator());
        }
    }

    private void addItems(@NotNull Player player) {
        ItemStack kitMenue = new ItemStack(Material.CHEST);
        ItemMeta kitMeta = kitMenue.getItemMeta();
        kitMeta.setDisplayName("Kits");
        kitMenue.setItemMeta(kitMeta);

        ItemStack teamMenue = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta teamSkullMeta = (SkullMeta) teamMenue.getItemMeta();
        teamSkullMeta.setOwner(player.getName());
        teamSkullMeta.setDisplayName("Teams");
        teamMenue.setItemMeta(teamSkullMeta);

        player.getInventory().setItem(0, kitMenue);
        player.getInventory().setItem(8, teamMenue);
    }
}
