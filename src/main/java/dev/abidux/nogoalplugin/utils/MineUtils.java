package dev.abidux.nogoalplugin.utils;

import org.bukkit.entity.EntityType;

public class MineUtils {
    public static boolean isMinecart(EntityType entityType) {
        switch (entityType) {
            case MINECART:
            case TNT_MINECART:
            case CHEST_MINECART:
            case HOPPER_MINECART:
            case FURNACE_MINECART:
            case SPAWNER_MINECART:
            case COMMAND_BLOCK_MINECART:
                return true;
            default:
                return false;
        }
    }
}