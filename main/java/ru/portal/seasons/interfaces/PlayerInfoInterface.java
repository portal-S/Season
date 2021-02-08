package ru.portal.seasons.interfaces;

public interface PlayerInfoInterface {

    public int getPlayDays(String name);

    public int getPlayTime(String name);

    public int[] getMinHour(String name);
}
