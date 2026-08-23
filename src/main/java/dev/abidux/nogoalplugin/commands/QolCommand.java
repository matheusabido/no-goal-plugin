package dev.abidux.nogoalplugin.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import dev.abidux.nogoalplugin.model.PlayerData;
import dev.abidux.nogoalplugin.utils.ItemCreator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class QolCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        PlayerData data = PlayerData.get(player);
        Inventory inventory = Bukkit.createInventory(null, 3*9, Component.text("Quality of Life", NamedTextColor.DARK_GRAY));

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

        inventory.setItem(13, keepInventory.toStack());
        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1L, 1L);
        return false;
    }
}