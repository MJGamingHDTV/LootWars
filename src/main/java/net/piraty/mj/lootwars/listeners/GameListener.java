package net.piraty.mj.lootwars.listeners;

import net.piraty.mj.lootwars.LootWars;
import net.piraty.mj.lootwars.managers.GameManager;
import net.piraty.mj.lootwars.utils.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.jetbrains.annotations.NotNull;

public class GameListener implements Listener {

    private final GameManager gameManager;

    public GameListener(@NotNull LootWars lootwars) {
        lootwars.registerListeners(this);
        this.gameManager = lootwars.getGameManager();
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent damageByEntityEvent) {
        if (gameManager.getGameState().equals(GameState.LOBBY) || gameManager.getGameState().equals(GameState.LOBBY_END) || gameManager.getGameState().equals(GameState.PREPARE))
            damageByEntityEvent.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent entityDamageEvent) {
        if (gameManager.getGameState().equals(GameState.LOBBY) || gameManager.getGameState().equals(GameState.LOBBY_END))
            entityDamageEvent.setCancelled(true);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent levelChangeEvent) {
        if (gameManager.getGameState().equals(GameState.LOBBY) || gameManager.getGameState().equals(GameState.LOBBY_END)) {
            levelChangeEvent.setFoodLevel(20);
            levelChangeEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent dropItemEvent) {
        if (gameManager.getGameState().equals(GameState.LOBBY) || gameManager.getGameState().equals(GameState.LOBBY_END)) {
            dropItemEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent pickupItemEvent) {
        if (gameManager.getGameState().equals(GameState.LOBBY) || gameManager.getGameState().equals(GameState.LOBBY_END)) {
            pickupItemEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemMove(InventoryClickEvent clickEvent) {
        if (gameManager.getGameState().equals(GameState.LOBBY) || gameManager.getGameState().equals(GameState.LOBBY_END)) {
            clickEvent.setCancelled(true);
        }
    }
}
