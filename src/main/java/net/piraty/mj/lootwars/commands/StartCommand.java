package net.piraty.mj.lootwars.commands;

import net.piraty.mj.lootwars.LootWars;
import net.piraty.mj.lootwars.utils.GameState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class StartCommand implements CommandExecutor {

    private final LootWars lootWars;

    public StartCommand(@NotNull LootWars lootWars) {
        this.lootWars = lootWars;
        lootWars.registerCommands("start", this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("skywars.start")) {
            if (lootWars.getGameManager().getGameState().equals(GameState.LOBBY)) {
                lootWars.getGameManager().changeGameState(GameState.LOBBY_END);
            }
        }
        return false;
    }
}
