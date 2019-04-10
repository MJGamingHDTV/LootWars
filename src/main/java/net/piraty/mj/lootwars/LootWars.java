package net.piraty.mj.lootwars;

import net.piraty.mj.lootwars.commands.LootWarsCommand;
import net.piraty.mj.lootwars.commands.StartCommand;
import net.piraty.mj.lootwars.listeners.GameListener;
import net.piraty.mj.lootwars.listeners.InventoryListener;
import net.piraty.mj.lootwars.listeners.PlayerJoinQuitListener;
import net.piraty.mj.lootwars.managers.*;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;

public final class LootWars extends JavaPlugin {

    private GameManager gameManager;
    private LocationManager locationManager;
    private ChestManager chestManager;
    private KitManager kitManager;
    private InventoryListener inventoryListener;
    private TeamManager teamManager;
    private static LootWars instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        this.saveDefaultConfig();
        this.gameManager = new GameManager(this);
        new LootWarsCommand(this);
        new StartCommand(this);
        new GameListener(this);
        new PlayerJoinQuitListener(this);
        inventoryListener = new InventoryListener(this);
        this.locationManager = new LocationManager(this);
        chestManager = new ChestManager(this);
        kitManager = new KitManager(this);
        teamManager = new TeamManager();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }

    public void registerListeners(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
    }

    public void registerCommands(String command, CommandExecutor commandExecutor) {
        this.getCommand(command).setExecutor(commandExecutor);
    }

    @Contract(pure = true)
    public GameManager getGameManager() {
        return gameManager;
    }

    @Contract(pure = true)
    public LocationManager getLocationManager() {
        return locationManager;
    }

    @Contract(pure = true)
    public ChestManager getChestManager() {
        return chestManager;
    }

    @Contract(pure = true)
    public KitManager getKitManager() {
        return kitManager;
    }

    @Contract(pure = true)
    public InventoryListener getInventoryListener() {
        return inventoryListener;
    }

    @Contract(pure = true)
    public TeamManager getTeamManager() {
        return teamManager;
    }

    @Contract(pure = true)
    public static LootWars getInstance() {
        return instance;
    }
}
