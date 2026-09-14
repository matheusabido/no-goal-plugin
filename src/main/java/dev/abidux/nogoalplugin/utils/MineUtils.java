package dev.abidux.nogoalplugin.utils;

import java.util.Arrays;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import dev.abidux.nogoalplugin.NoGoalPlugin;

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

    public static boolean hasLoaderMinecart(@NotNull Chunk chunk, @Nullable Entity ignoreEntity) {
        return Arrays.stream(chunk.getEntities()).anyMatch(e -> {
            if (ignoreEntity == null) {
                return MineUtils.isMinecart(e.getType()) && isMLC(e);
            }
            return !e.getUniqueId().equals(ignoreEntity.getUniqueId()) && MineUtils.isMinecart(e.getType()) && isMLC(e);
        });
    }

    public static boolean isMLC(Entity entity) {
        Boolean result = entity.getPersistentDataContainer().get(NoGoalPlugin.KEY_MLC, PersistentDataType.BOOLEAN);
        return result != null && result;
    }
}