package dev.abidux.nogoalplugin.events;

import dev.abidux.nogoalplugin.model.PlayerData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InvClick implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        if (!event.getView().title().equals(Component.text("Quality of Life", NamedTextColor.GRAY))) {
            return;
        }

        event.setCancelled(true);

        if (event.getRawSlot() != 13) {
            return;
        }

        if (event.getCurrentItem() == null ||
            event.getCurrentItem().getType() != Material.SKELETON_SKULL) {
            return;
        }

        PlayerData data = PlayerData.get(player);
        data.setKeepInventoryActive(!data.isKeepInventoryActive());
        data.save(player);
    }
}