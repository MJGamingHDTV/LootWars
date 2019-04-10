package net.piraty.mj.lootwars.utils;

import net.piraty.mj.lootwars.LootWars;

public class Data {

    public static final String PREFIX = LootWars.getInstance().getConfig().getString("prefix");
    public static final Integer MIN_PLAYERS = LootWars.getInstance().getConfig().getInt("min_players");
    public static final Integer TEAM_SIZE = LootWars.getInstance().getConfig().getInt("team_size");
    public static final Integer MAX_PLAYERS_PER_TEAM = LootWars.getInstance().getConfig().getInt("max_players_per_team");

}
