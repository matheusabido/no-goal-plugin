package dev.abidux.nogoalplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import dev.abidux.nogoalplugin.NoGoalPlugin;
import dev.abidux.nogoalplugin.utils.MineUtils;

public class MinecartLoadChunksCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cCommand only available for players");
            return true;
        }

        Entity targetedEntity = player.getTargetEntity(6);
        if (targetedEntity == null) {
            player.sendMessage("§cYou must be looking at a minecart.");
            return true;
        }

        if (!MineUtils.isMinecart(targetedEntity.getType())) {
            player.sendMessage("§cEntity is not a minecart");
            return true;
        }

        if (targetedEntity.getPersistentDataContainer().get(NoGoalPlugin.KEY_MLC, PersistentDataType.BOOLEAN) == null) {
            targetedEntity.getPersistentDataContainer().set(NoGoalPlugin.KEY_MLC, PersistentDataType.BOOLEAN, true);
            player.sendMessage("§aThis minecart now loads chunks.");
        } else {
            targetedEntity.getPersistentDataContainer().remove(NoGoalPlugin.KEY_MLC);
            player.sendMessage("§cThis minecart now does not load chunks.");
        }
        return false;
    }
}
