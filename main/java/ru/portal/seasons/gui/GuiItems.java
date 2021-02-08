package ru.portal.seasons.gui;

import com.culleystudios.spigot.lib.compatibility.Compatibility_v1_7_R4;
import com.culleystudios.spigot.lib.compatibility.item.ItemCompatibility;
import com.culleystudios.spigot.lib.compatibility.item.ItemCompatibility_v1;
import com.culleystudios.spigot.lib.item.Item;
import com.culleystudios.spigot.lib.item.PlayerHead;
import com.culleystudios.spigot.lib.item.items.ItemWrapper;
import com.culleystudios.spigot.lib.item.items.PlayerHeadItem;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import portal.authBot.bot.config.BotConfig;
import ru.portal.seasons.SeasonPlugin;
import ru.portal.seasons.interfaces.PlayerInfoInterface;
import ru.portal.seasons.player.PlayerInfo;
import ru.portal.seasons.utils.ItemsMenu;

import java.util.ArrayList;
import java.util.List;

public class GuiItems {
    public static ItemStack getItemGui(int i, int day){
        day++;
        ItemStack itemStack = new ItemStack(1);
        ItemMeta meta = itemStack.getItemMeta();
        ItemsMenu itemsMenu = new ItemsMenu();
        switch (i){
            case (1):
                itemStack = itemsMenu.getYes();
                meta.setDisplayName(itemStack.getItemMeta().getDisplayName() + day);
                meta.setLore(itemStack.getItemMeta().getLore());
                itemStack.setItemMeta(meta);
                return itemStack;
            case (2):
                itemStack = itemsMenu.getNo();
                meta.setDisplayName(itemStack.getItemMeta().getDisplayName() + day);
                meta.setLore(itemStack.getItemMeta().getLore());
                itemStack.setItemMeta(meta);
                return itemStack;
            case (3):
                itemStack = itemsMenu.getNone();
                meta.setDisplayName(itemStack.getItemMeta().getDisplayName() + day);
                meta.setLore(itemStack.getItemMeta().getLore());
                itemStack.setItemMeta(meta);
                return itemStack;
            case (4):
                itemStack = itemsMenu.getToNextPage();
                meta.setDisplayName(replace("&3Следующая страница"));
                itemStack.setItemMeta(meta);
                return itemStack;
            case (5):
                itemStack = itemsMenu.getOnPrevPage();
                meta.setDisplayName(replace("&3Предыдущая страница"));
                itemStack.setItemMeta(meta);
                return itemStack;
            default: return itemStack;
        }
    }

    /*public static ItemStack getPlayerItem(String nick){
        PlayerInfoInterface playerInfo = new PlayerInfo();
        ItemStack itemStack = new ItemStack(Material.SKULL, 1);
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        meta.setOwner(nick);
        List lores = new ArrayList();
        itemStack.setDurability((short) 3);
        meta.setDisplayName(replace("&6Игрок ") + nick);
        //lores.add(replace("&7День: ") + playerInfo.getPlayDays(nick));
        lores.add(replace("&bСтрик: &9") + playerInfo.getPlayDays(nick));
        lores.add(replace("&7Времени сыграно: ") + playerInfo.getPlayTime(nick) + " минут(ы)");
        if(BotConfig.getGuardList().contains(nick)) {
            lores.add(replace("&7Discord: &aпривязан"));
        } else lores.add(replace("&7Discord: &cне привязан"));
        lores.add(replace("&7До следующего дня: " + playerInfo.getMinHour(nick)[0] + " мин. " + playerInfo.getMinHour(nick)[1] + " ч."));

        *//*if(SeasonPlugin.getSeason().isDsVerify() && !(BotConfig.getGuardList().contains(nick))){
            lores.add(replace("&7Для участия в сезоне необходимо привязать Discord аккаунт"));
        }*//*
        meta.setLore(lores);
        itemStack.setItemMeta(meta);
        return itemStack;
    }*/

    public static ItemStack getPlayerItem(String nick){
        PlayerInfoInterface playerInfo = new PlayerInfo();
        Material m = Material.getMaterial("PLAYER_HEAD");  //чужой говнокод, лучше не трогать
        if (m == null) {
            m = Material.getMaterial("SKULL_ITEM");
        }

        ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        SkullMeta meta = (SkullMeta)itemStack.getItemMeta();
        meta.setOwner(nick);
       // ItemStack itemStack = new ItemStack(m, 1);
        itemStack.setDurability((short)3);
        //SkullMeta meta = (SkullMeta)itemStack.getItemMeta();
        meta.setOwner(nick);
        List lores = new ArrayList();
        //itemStack.setDurability((short) 3);
        meta.setDisplayName(replace("&6Игрок ") + nick);
        lores.add(replace("&bСтрик: &9") + playerInfo.getPlayDays(nick));
        lores.add(replace("&7Времени сыграно: ") + playerInfo.getPlayTime(nick) + " минут(ы)");
        if(BotConfig.getGuardList().contains(nick)) {
            lores.add(replace("&7Discord: &aпривязан"));
        } else lores.add(replace("&7Discord: &cне привязан"));
        lores.add(replace("&7До следующего бонусного дня: " + playerInfo.getMinHour(nick)[1] + " ч. " + playerInfo.getMinHour(nick)[0] + " мин."));
        meta.setLore(lores);
        meta.setOwner(nick);
        itemStack.setItemMeta((ItemMeta)meta);
        return itemStack;

    }






    public static String replace(String s){
        return s.replace("&" , "§");
    }
}
