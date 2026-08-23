package dev.abidux.nogoalplugin.utils;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import lombok.Builder;
import net.kyori.adventure.text.Component;

@Builder
public class ItemCreator {
    private Material material;
    private Component name;
    private List<Component> lore;

    public ItemStack toStack() {
        ItemStack item = new ItemStack(material);

        item.editMeta(meta -> {
            if (this.name != null) {
                meta.customName(this.name);
            }

            if (this.lore != null) {
                meta.lore(this.lore);
            }
        });

        return item;
    }
}