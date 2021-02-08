package ru.portal.seasons.gui;

import net.minecraft.server.v1_6_R3.StripColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.portal.seasons.SeasonPlugin;
import ru.portal.seasons.interfaces.PlayersMapsInterface;
import ru.portal.seasons.player.PlayerInfo;
import ru.portal.seasons.seasons.Season;
import ru.portal.seasons.utils.ItemsMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GuiHandler {

    private static PlayersMapsInterface pl = new PlayerInfo();
    private static Season season = SeasonPlugin.getSeason();
    private static Inventory inv = Bukkit.createInventory(null , 54, SeasonPlugin.replace(season.getSeasonName()));

    public static Inventory newGui(Player player, int invId, Inventory inventory){
        Season season = SeasonPlugin.getSeason();
        List<Integer> slots = Arrays.asList(0, 1, 2, 3, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26);
        inv = Bukkit.createInventory(null , 27, SeasonPlugin.replace(season.getSeasonName()));
        inv.setItem(4, GuiItems.getPlayerItem(player.getName()));
        ItemsMenu itemsMenu = new ItemsMenu();
        for (int i = 0 ; i < slots.size(); i++){ inv.setItem(slots.get(i), itemsMenu.getBackGround()); }
        switch (invId){
            case 1:
                openFirstGui(inv, player);
                break;
            case 2:
                openNextGui(inv, player, inventory);
                break;
            case 3:
                openThisGui(inv, player, inventory);
                break;
            case 4:
                openPrevGui(inv, player, inventory);
                break;
        }
        return inv;

    }

    private static void openFirstGui(Inventory inv, Player player){
        for (int i = 0; i < season.getDaysCount(); i++){
            if(i > 6) break;
            int f = i;
            while (inv.getItem(f) != null) f++;
            if(!(pl.getPlayersDayInfo().get(player.getName()) < i + 1)) {
                if(pl.getPlayersDaysList().get(player.getName()).contains(String.valueOf(i + 1))){
                    inv.setItem(f, GuiItems.getItemGui(1, i));
                } else inv.setItem(f, GuiItems.getItemGui(3, i));
            } else inv.setItem(f, GuiItems.getItemGui(2, i));
        }
        if(season.getDaysCount() > 7) inv.setItem(25, GuiItems.getItemGui(4, 0));
    }

    private static void openNextGui(Inventory inv, Player player, Inventory lastInv){
        int day = getDay(lastInv.getItem(16));
        int s = 10;
        for (int i = day; i < season.getDaysCount(); i++){
            if(i > day + 6) break;
                if(!(pl.getPlayersDayInfo().get(player.getName()) < i + 1)) {
                    if(pl.getPlayersDaysList().get(player.getName()).contains(String.valueOf(i + 1))){
                        inv.setItem(s, GuiItems.getItemGui(1, i));
                    } else inv.setItem(s, GuiItems.getItemGui(3, i));
                } else inv.setItem(s, GuiItems.getItemGui(2, i));
            s++;
        }

        if(!(inv.getItem(16) == null || day >= season.getDaysCount())){
            inv.setItem(25, GuiItems.getItemGui(4, 0));
        }
        inv.setItem(19, GuiItems.getItemGui(5, 0));
    }

    private static void openPrevGui(Inventory inv, Player player, Inventory lastInv){
        int day = getDay(lastInv.getItem(10));
        int s = 10;
        for (int i = day - 8; i < season.getDaysCount(); i++){
            if(i > day - 2) break;
            if(!(pl.getPlayersDayInfo().get(player.getName()) < i + 1)) {
                if(pl.getPlayersDaysList().get(player.getName()).contains(String.valueOf(i + 1))){
                    inv.setItem(s, GuiItems.getItemGui(1, i));
                } else inv.setItem(s, GuiItems.getItemGui(3, i));
            } else inv.setItem(s, GuiItems.getItemGui(2, i));
            s++;
        }
        inv.setItem(25, GuiItems.getItemGui(4, 0));
        inv.setItem(19, GuiItems.getItemGui(5, 0));
    }

    private static void openThisGui(Inventory inv, Player player, Inventory lastInv){
        int day = getDay(lastInv.getItem(10));
        int s = 10;
        for (int i = day - 1; i < season.getDaysCount(); i++){
            if(i > day + 5) break;
            if(!(pl.getPlayersDayInfo().get(player.getName()) < i + 1)) {
                if(pl.getPlayersDaysList().get(player.getName()).contains(String.valueOf(i + 1))){
                    inv.setItem(s, GuiItems.getItemGui(1, i));
                } else inv.setItem(s, GuiItems.getItemGui(3, i));
            } else inv.setItem(s, GuiItems.getItemGui(2, i));
            s++;
        }
        if(season.getDaysCount() > day) {
            if(!(inv.getItem(16) == null || getDay(lastInv.getItem(16)) >= season.getDaysCount())){
                inv.setItem(25, GuiItems.getItemGui(4, 0));
            }
        }
        if(day> 7) inv.setItem(19, GuiItems.getItemGui(5, 0));
    }

    public static Integer getDay(ItemStack itemStack){
        String name = ChatColor.stripColor(itemStack.getItemMeta().getDisplayName());
        return Integer.valueOf(name.replace("День ", ""));
    }


    /*private static void openSecondGui(Inventory inv, Player player){
        for (int i = 21; i < season.getDaysCount(); i++){
            int f = i - 2;
            while (inv.getItem(f) != null) f++;
            if(!(pl.getPlayersDayInfo().get(player.getName()) < i + 1)) {
                if(pl.getPlayersDaysList().get(player.getName()).contains(String.valueOf(i + 1))){
                    inv.setItem(f, GuiItems.getItemGui(1, i));
                } else inv.setItem(f, GuiItems.getItemGui(3, i));
            } else inv.setItem(f, GuiItems.getItemGui(2, i));
        }
        inv.setItem(49, GuiItems.getItemGui(5, 0));
    }*/

    public static void removeDay(Player player, int day){
        pl.getPlayersDaysList().get(player.getName()).remove(String.valueOf(day));
    }

    public static boolean hasItem(ItemStack itemStack, int id) {
        ItemsMenu menu = new ItemsMenu();
        if (itemStack.hasItemMeta()) {
            switch (id) {
                case (1):
                    if (itemStack.getItemMeta().getLore().equals(menu.getYes().getItemMeta().getLore())) {
                        return true;
                    } else return false;
                case (2):
                    if (itemStack.getItemMeta().getDisplayName().equals(GuiItems.replace("&3Следующая страница"))){
                        return true;
                    } else return false;
                case (3):
                    if (itemStack.getItemMeta().getDisplayName().equals(GuiItems.replace("&3Предыдущая страница"))){
                        return true;
                    } else return false;

                default:
                    return false;
            }
        } else {

            return false;
        }
    }



    
}
