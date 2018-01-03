package com.github.healpot.plugin.enhancement.me.visual;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import com.github.healpot.plugin.enhancement.me.lore.Data;
import com.github.healpot.plugin.enhancement.me.main.Main;

public class MenuHandler implements Listener {
	private Main m;
	private ItemStack enhancingItem;
	Data data = new Data();

	public MenuHandler(Main m) {
		this.m = m;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (!e.getInventory().getName().equalsIgnoreCase(m.menu.getScreen().getName())) {
			List<String> loreList = new ArrayList<String>();
			if ((e.getInventory().getType() != InventoryType.CRAFTING)
					&& (e.getInventory().getType() != InventoryType.PLAYER)) {
				if ((e.getClick().equals(ClickType.NUMBER_KEY))
						&& (e.getWhoClicked().getInventory().getItem(e.getHotbarButton()) != null)) {
					ItemStack itemMoved = e.getWhoClicked().getInventory().getItem(e.getHotbarButton());
					if ((itemMoved.hasItemMeta()) && (itemMoved.getItemMeta().hasLore())) {
						int loreSize = itemMoved.getItemMeta().getLore().size();
						for (int i = 0; i < loreSize; i++) {
							loreList.add((String) itemMoved.getItemMeta().getLore().get(i));
						}
						if (loreList.contains(ChatColor.translateAlternateColorCodes('&',
								m.getConfig().getString("Lore.UntradeableLore")))) {
							e.setCancelled(true);
							Data.sendMessage(m.getConfig().getString("Messages.NoStorage"), e.getWhoClicked());
						}
					}
				}
				if (e.getCurrentItem() != null) {
					if ((e.getCurrentItem().hasItemMeta()) && (e.getCurrentItem().getItemMeta().hasLore())) {
						int loreSize = e.getCurrentItem().getItemMeta().getLore().size();
						for (int i = 0; i < loreSize; i++) {
							loreList.add((String) e.getCurrentItem().getItemMeta().getLore().get(i));
						}
						if (loreList.contains(ChatColor.translateAlternateColorCodes('&',
								m.getConfig().getString("Lore.UntradeableLore")))) {
							e.setCancelled(true);
							Data.sendMessage(m.getConfig().getString("Messages.NoStorage"), e.getWhoClicked());
						}
					}
				}
			}
		}
		if (!(e.getWhoClicked() instanceof Player))

		{
			return;
		}

		if (!e.getCurrentItem().hasItemMeta()) {
			return;
		}
		Player player = (Player) e.getWhoClicked();

		if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Enhance")) {
			e.setCancelled(true);
			return;
		}

		if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Stats")) {
			e.setCancelled(true);
			return;
		}

	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		if (!e.getInventory().getName().equalsIgnoreCase(
				((Main) Bukkit.getPluginManager().getPlugin("EnchantmentsEnhance")).menu.getScreen().getName())) {
			return;
		}
	}

	public static int getSlot(int x, int y) {
		return (y * 9) - (9 - x) - 1;
	}

}