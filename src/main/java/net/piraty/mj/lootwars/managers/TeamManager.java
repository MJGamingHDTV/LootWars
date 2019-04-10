package net.piraty.mj.lootwars.managers;

import net.piraty.mj.lootwars.utils.Data;
import net.piraty.mj.lootwars.utils.ITeam;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TeamManager {

     static Map<Integer, ITeam> teams = new HashMap<>();
    private static Map<Integer, Set<Player>> IDPlayer = new HashMap<>();

    public TeamManager() {
        createTeams();
    }

    private void createTeams() {
        int TEAM_MAX_ID = Data.TEAM_SIZE;
        while (TEAM_MAX_ID > 0) {
            final int teamID = TEAM_MAX_ID;
            teams.put(teamID, new ITeam() {
                @Override
                public Integer getTeamID() {
                    return teamID;
                }

                @Override
                public Set<Player> getPlayers() {
                    return IDPlayer.get(teamID);
                }

                @Override
                public void addPlayer(Player player) {
                    IDPlayer.get(teamID).add(player);
                }
            });
            IDPlayer.put(teamID, new HashSet<>());
            TEAM_MAX_ID--;
        }
    }

    public void addPlayerToTeam(Integer teamID, Player player) {
        for (Integer teamIDs : teams.keySet()) {
            ITeam team = teams.get(teamIDs);
            team.getPlayers().remove(player);
            teams.replace(teamIDs, team);
        }
        teams.get(teamID).addPlayer(player);
    }

    public boolean isPlayerInTeam(Integer teamID, Player player) {
        return teams.get(teamID).getPlayers().contains(player);
    }

    @Contract(pure = true)
    public Map<Integer, ITeam> getTeams() {
        return teams;
    }

    @Contract(pure = true)
    public Map<Integer, Set<Player>> getIDPlayer() {
        return IDPlayer;
    }
}
