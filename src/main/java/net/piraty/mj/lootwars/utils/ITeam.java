package net.piraty.mj.lootwars.utils;

import org.bukkit.entity.Player;

import java.util.Set;

public interface ITeam {

    Integer getTeamID();

    Set<Player> getPlayers();

    void addPlayer(Player player);
}
