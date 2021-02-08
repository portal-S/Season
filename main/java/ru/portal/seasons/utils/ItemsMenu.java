package ru.portal.seasons.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.portal.seasons.SeasonPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemsMenu {
    private ItemStack yes;
    private ItemStack no;
    private ItemStack none;
    private ItemStack backGround;
    private ItemStack toNextPage;
    private ItemStack onPrevPage;

    public ItemsMenu(){
        ConfigurationSection section = SeasonPlugin.getConfiguration().getConfigurationSection("itemsMenu");
        yes = new ItemStack(section.getInt("yesI.id"));
        yes.setDurability((short) section.getInt("yesI.data"));
        ItemMeta yesMeta = yes.getItemMeta();
        yesMeta.setLore(Handlers.replaceList(section.getStringList("yesI.lore")));
        yesMeta.setDisplayName(SeasonPlugin.replace(section.getString("yesI.name")));
        yes.setItemMeta(yesMeta);

        no = new ItemStack(section.getInt("noI.id"));
        no.setDurability((short) section.getInt("noI.data"));
        ItemMeta noMeta = no.getItemMeta();
        noMeta.setLore(Handlers.replaceList(section.getStringList("noI.lore")));
        noMeta.setDisplayName(SeasonPlugin.replace(section.getString("noI.name")));
        no.setItemMeta(noMeta);

        none = new ItemStack(section.getInt("none.id"));
        none.setDurability((short) section.getInt("none.data"));
        ItemMeta noneMeta = none.getItemMeta();
        noneMeta.setLore(Handlers.replaceList(section.getStringList("none.lore")));
        noneMeta.setDisplayName(SeasonPlugin.replace(section.getString("none.name")));
        none.setItemMeta(noneMeta);

        backGround = new ItemStack(section.getInt("backGround.id"));
        backGround.setDurability((short) section.getInt("backGround.data"));

        toNextPage = new ItemStack(section.getInt("toNextPage.id"));
        toNextPage.setDurability((short) section.getInt("toNextPage.data"));

        onPrevPage = new ItemStack(section.getInt("onPrevPage.id"));
        onPrevPage.setDurability((short) section.getInt("onPrevPage.data"));
    }

    public ItemStack getYes() {
        return yes;
    }

    public ItemStack getNone() {
        return none;
    }

    public ItemStack getNo() {
        return no;
    }

    public ItemStack getBackGround() {
        return backGround;
    }

    public ItemStack getToNextPage() {
        return toNextPage;
    }

    public ItemStack getOnPrevPage() {
        return onPrevPage;
    }
}
