package net.piraty.mj.lootwars.commands;

import net.piraty.mj.lootwars.LootWars;
import net.piraty.mj.lootwars.utils.Data;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LootWarsCommand implements CommandExecutor {

    private final LootWars lootWars;

    public LootWarsCommand(@NotNull LootWars lootWars) {
        this.lootWars = lootWars;
        lootWars.registerCommands("lootWars", this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("lootWars.admin"))
            return false;
        if (args[0].equalsIgnoreCase("debug")) {
            sender.sendMessage(Data.PREFIX + "§eDebug - Team:");
            lootWars.getTeamManager().getTeams().forEach((integer, iTeam) -> {
                sender.sendMessage(integer.toString());
                iTeam.getPlayers().forEach(player -> sender.sendMessage(player.getName()));
            });
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(Data.PREFIX + "§cDu musst ein Spieler sein, um diesen Befehl nutzen zu können!");
            return false;
        }
        Player player = (Player) sender;
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("setLobby")) {
                lootWars.getLocationManager().addLobby(player.getLocation());
                sender.sendMessage(Data.PREFIX + "Du hast die Lobby-Positon erfolgreich gesetzt!");
            }
            if (args[0].equalsIgnoreCase("setSpectator")) {
                lootWars.getLocationManager().addSpectator(player.getLocation());
                sender.sendMessage(Data.PREFIX + "Du hast die Spectator-Position erfolgreich gesetzt!");
            }
        }
        if (args.length == 2) {
            // /sw setIsland 1
            if (!isInteger(args[1])) {
                sender.sendMessage(Data.PREFIX + "Bitte benutze /sw setIsland <1, 2, 3, ...>");
                return false;
            }
            if (args[0].equalsIgnoreCase("setIsland")) {
                lootWars.getLocationManager().addIslands(args[1], player.getLocation());
                sender.sendMessage(Data.PREFIX + "Du hast die Insel-Postion " + args[1] + " erfolgreich gesetzt!");
            }
        }
        return false;
    }

    private boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
        } catch(NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }
}
