package net.piraty.mj.lootwars.managers;

import net.piraty.mj.lootwars.LootWars;
import net.piraty.mj.lootwars.utils.Data;
import net.piraty.mj.lootwars.utils.GameState;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class GameManager {

    private final LootWars lootWars;
    private GameState gameState;
    private int lobbyTimer = 60;
    private int lobbyEndTimer = 10;
    private int gameTimer = 7210;
    private int endTimer = 10;
    private BukkitTask bukkitTask = null;

    public GameManager(LootWars lootWars) {
        this.lootWars = lootWars;
        gameState = GameState.LOBBY;
    }

    public void changeGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    private void resetTimingsAndStopScheduler() {
        bukkitTask.cancel();
        bukkitTask = null;
        lobbyTimer = 60;
        gameTimer = 7210;
        endTimer = 10;
        gameState = GameState.LOBBY;
    }

    public void gameScheduler() {
        bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (gameState.equals(GameState.LOBBY) && lobbyTimer >= 10) {
                    lobbyTimer--;
                    Bukkit.getOnlinePlayers().forEach(player -> player.setLevel(lobbyTimer));
                    if (lobbyTimer == 10)
                        gameState = GameState.LOBBY_END;
                    if (Bukkit.getOnlinePlayers().size() < Data.MIN_PLAYERS)
                        resetTimingsAndStopScheduler();
                }
                if (gameState.equals(GameState.LOBBY_END) && lobbyEndTimer >= 0) {
                    lobbyEndTimer--;
                    Bukkit.getOnlinePlayers().forEach(player -> player.setLevel(lobbyEndTimer));
                    if (lobbyEndTimer == 0) {
                        lootWars.getChestManager().detectChests("world");
                        gameState = GameState.PREPARE;
                        Bukkit.getOnlinePlayers().forEach(player -> {
                            player.setGameMode(GameMode.SURVIVAL);
                            player.getInventory().clear();
                        });
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            int i = 0;
                            for (ItemStack itemStack : lootWars.getInventoryListener().getPlayerKit().get(player)) {
                                player.getInventory().setItem(i, itemStack);
                                i++;
                            }
                        }
                    }
                    if (Bukkit.getOnlinePlayers().size() < Data.MIN_PLAYERS)
                        resetTimingsAndStopScheduler();
                }
                if (gameState.equals(GameState.PREPARE) && gameTimer >= 0) {
                    gameTimer--;
                    if (gameTimer == 7200) {
                        gameState = GameState.GAME;
                        for (int i : TeamManager.teams.keySet()) {
                            Location island = lootWars.getLocationManager().getIsland(String.valueOf(i));
                            TeamManager.teams.get(i).getPlayers().forEach(player -> player.teleport(island));
                            System.out.println("Team teleported to Island-Location");
                        }
                    }
                    if (gameTimer == 0) {
                        gameState = GameState.END;
                    }
                }
                if (gameState.equals(GameState.END) && endTimer >= 0) {
                    endTimer--;
                    Bukkit.getOnlinePlayers().forEach(player -> player.setLevel(endTimer));
                    if (endTimer == 0) {
                        Bukkit.shutdown();
                    }
                }
            }
        }.runTaskTimer(lootWars, 0L, 20L);
    }

    public BukkitTask getBukkitTask() {
        return bukkitTask;
    }
}
