package net.piraty.mj.lootwars.managers;

import net.piraty.mj.lootwars.LootWars;
import net.piraty.mj.lootwars.utils.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LocationManager {

    private Location lobby;
    private Location spectator;
    private Map<String, Location> islands = new HashMap<>();
    private File locFile;
    private YamlConfiguration yamlConfiguration;

    public LocationManager(@NotNull LootWars lootWars) {
        this.locFile = new File(lootWars.getDataFolder().getAbsolutePath(), "locations.yml");
        this.yamlConfiguration = YamlConfiguration.loadConfiguration(locFile);
        yamlConfiguration.options().header("Do not change this values!!!");
        try {
            loadLocations();
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(Data.PREFIX + "§cEs wurden noch keine Locations gesetzt!");
        }
    }

    public void addLobby(Location location) {
        yamlConfiguration.set("Lobby.World", location.getWorld().getName());
        yamlConfiguration.set("Lobby.X", location.getX());
        yamlConfiguration.set("Lobby.Y", location.getY());
        yamlConfiguration.set("Lobby.Z", location.getZ());
        yamlConfiguration.set("Lobby.Yaw", location.getYaw());
        yamlConfiguration.set("Lobby.Pitch", location.getPitch());
        try {
            yamlConfiguration.save(locFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addSpectator(Location location) {
        yamlConfiguration.set("Spectator.World", location.getWorld().getName());
        yamlConfiguration.set("Spectator.X", location.getX());
        yamlConfiguration.set("Spectator.Y", location.getY());
        yamlConfiguration.set("Spectator.Z", location.getZ());
        yamlConfiguration.set("Spectator.Yaw", location.getYaw());
        yamlConfiguration.set("Spectator.Pitch", location.getPitch());
        try {
            yamlConfiguration.save(locFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addIslands(String number, Location location) {
        String path = "Islands." + number;
        yamlConfiguration.set(path + ".World", location.getWorld().getName());
        yamlConfiguration.set(path + ".X", location.getX());
        yamlConfiguration.set(path + ".Y", location.getY());
        yamlConfiguration.set(path + ".Z", location.getZ());
        yamlConfiguration.set(path + ".Yaw", location.getYaw());
        yamlConfiguration.set(path + ".Pitch", location.getPitch());
        try {
            yamlConfiguration.save(locFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadLocations() {
        for (String number : yamlConfiguration.getConfigurationSection("Islands").getKeys(false)) {
            String path = "Islands." + number;
            World world = Bukkit.getWorld(yamlConfiguration.getString(path + ".World"));
            double x = (double) yamlConfiguration.get(path + ".X");
            double y = (double) yamlConfiguration.get(path + ".Y");
            double z = (double) yamlConfiguration.get(path + ".Z");
            float yaw =  Float.parseFloat(yamlConfiguration.getString(path + ".Yaw"));
            float pitch = Float.parseFloat(yamlConfiguration.getString(path + ".Pitch"));
            Location location = new Location(world, x, y, z, yaw, pitch);
            islands.put(number, location);
        }

        lobby = new Location(
                Bukkit.getWorld(yamlConfiguration.getString("Lobby.World")),
                (double) yamlConfiguration.get("Lobby.X"),
                (double) yamlConfiguration.get("Lobby.Y"),
                (double) yamlConfiguration.get("Lobby.Z"),
                Float.parseFloat(yamlConfiguration.getString("Lobby.Yaw")),
                Float.parseFloat(yamlConfiguration.getString("Lobby.Pitch")));

        spectator = new Location(
                Bukkit.getWorld(yamlConfiguration.getString("Spectator.World")),
                (double) yamlConfiguration.get("Spectator.X"),
                (double) yamlConfiguration.get("Spectator.Y"),
                (double) yamlConfiguration.get("Spectator.Z"),
                Float.parseFloat(yamlConfiguration.getString("Spectator.Yaw")),
                Float.parseFloat(yamlConfiguration.getString("Spectator.Pitch")));
    }

    public Location getIsland(String number) {
        return islands.get(number);
    }

    public Location getLobby() {
        return lobby;
    }

    public Location getSpectator() {
        return spectator;
    }
}
