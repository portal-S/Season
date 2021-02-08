package ru.portal.seasons.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import ru.portal.seasons.SeasonPlugin;
import ru.portal.seasons.gui.GuiHandler;
import ru.portal.seasons.gui.GuiItems;
import ru.portal.seasons.interfaces.PlayersMapsInterface;
import ru.portal.seasons.utils.Handlers;

import java.util.ArrayList;

public class PlayerListener implements Listener {
    @EventHandler
    public void join(PlayerJoinEvent e){
        PlayersMapsInterface pl = new PlayerInfo();
        String nick = e.getPlayer().getName();
        pl.getPlayersJoinTime().put(nick, 0);
        if(!pl.getPlayersPlayTime().containsKey(nick)) pl.getPlayersPlayTime().put(nick, 1);
        if(!pl.getPlayersDayInfo().containsKey(nick)) pl.getPlayersDayInfo().put(nick,0);
        if(!pl.getPlayersDaysList().containsKey(nick)) pl.getPlayersDaysList().put(nick,new ArrayList<String>());
        if(!pl.getPlayersLastPlay().containsKey(nick)) pl.getPlayersLastPlay().put(nick, (long) 1);
    }


    @EventHandler
    public void interact(InventoryClickEvent e){
        if(e.getInventory().getTitle().equals(SeasonPlugin.replace(SeasonPlugin.getSeason().getSeasonName()))){
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
            if(e.getCurrentItem() != null && e.getClick().isLeftClick()){
                ItemStack itemStack = e.getCurrentItem();
                int days = GuiHandler.getDay(e.getInventory().getItem(10));
                if(GuiHandler.hasItem(itemStack, 2)){
                    e.getWhoClicked().openInventory(GuiHandler.newGui((Player) e.getWhoClicked(), 2, e.getInventory()));}
                else if (GuiHandler.hasItem(itemStack, 3)){
                    if(days > 9){
                        e.getWhoClicked().openInventory(GuiHandler.newGui((Player) e.getWhoClicked(), 4, e.getInventory()));
                    } else e.getWhoClicked().openInventory(GuiHandler.newGui((Player) e.getWhoClicked(), 1, e.getInventory()));
                }
                else if (GuiHandler.hasItem(itemStack, 1)) {            //если забрал подарок
                    int day = GuiHandler.getDay(itemStack);
                    GuiHandler.removeDay((Player) e.getWhoClicked(), day);
                    Handlers.useCMDs(day, (Player) e.getWhoClicked());
                    if(day > 7) e.getWhoClicked().openInventory(GuiHandler.newGui((Player) e.getWhoClicked(), 3, e.getInventory()));
                    else e.getWhoClicked().openInventory(GuiHandler.newGui((Player) e.getWhoClicked(), 1, e.getInventory()));
                    /*ItemStack item = e.getInventory().getItem(25);
                    if(GuiHandler.hasItem(item , 2)) e.getWhoClicked().openInventory(GuiHandler.newGui((Player) e.getWhoClicked(), 1, e.getInventory()));
                    else if(GuiHandler.hasItem(item , 3)) e.getWhoClicked().openInventory(GuiHandler.newGui((Player) e.getWhoClicked(), 2, e.getInventory()));
                    else  e.getWhoClicked().openInventory(GuiHandler.newGui((Player) e.getWhoClicked(), 1, e.getInventory()));*/
                }
            }
            ((Player) e.getWhoClicked()).updateInventory();
        }
    }
}
