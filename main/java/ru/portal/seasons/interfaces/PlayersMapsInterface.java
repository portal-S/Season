package ru.portal.seasons.interfaces;

import java.util.List;
import java.util.Map;

public interface PlayersMapsInterface {

    public Map<String, Integer> getPlayersPlayTime();

    public void setPlayersPlayTime(Map<String, Integer> playersPlayTime);

    public Map<String, Integer> getPlayersJoinTime();

    public void setPlayersJoinTime(Map<String, Integer> playersJoinTime);

    public Map<String, Integer> getPlayersDayInfo();

    public void setPlayersDayInfo(Map<String, Integer> playersDayInfo);

    public Map<String, Long> getPlayersLastPlay();

    public Map<String, List<String>> getPlayersDaysList();

    }
