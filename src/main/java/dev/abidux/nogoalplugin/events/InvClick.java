package dev.abidux.nogoalplugin.events;

import dev.abidux.nogoalplugin.model.PlayerData;
import dev.abidux.nogoalplugin.utils.ItemCreator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.List;

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

        if (!event.getView().title().equals(Component.text("Quality of Life", NamedTextColor.DARK_GRAY))) {
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
        
        ItemCreator keepInventory = ItemCreator.builder()
            .material(Material.SKELETON_SKULL)
            .name(Component.text("Manter inventário", data.isKeepInventoryActive() ? NamedTextColor.GREEN : NamedTextColor.RED))
            .lore(List.of(
                Component.text("Clique para ", NamedTextColor.GRAY)
                    .append(
                        Component.text(
                            data.isKeepInventoryActive() ? "desativar" : "ativar",
                            data.isKeepInventoryActive()
                                ? NamedTextColor.RED
                                : NamedTextColor.GREEN
                        )
                    )
                    .append(Component.text(" o manter inventário.", NamedTextColor.GRAY))
            ))
            .build();
        event.getInventory().setItem(13, keepInventory.toStack());
    }
}