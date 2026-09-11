package dev.abidux.nogoalplugin.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import dev.abidux.nogoalplugin.NoGoalPlugin;
import dev.abidux.nogoalplugin.bot.DiscordBot;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class PlayerJoinQuit implements Listener {
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().getName().toLowerCase().endsWith("_bot")) {
            return;
        }

        DiscordBot bot = NoGoalPlugin.getBot();
        Long textChannelId = NoGoalPlugin.getInstance().getConfig().getLong("text_channel_id");
        Long joinNotificationRoleId = NoGoalPlugin.getInstance().getConfig().getLong("join_notification_role_id");

        TextChannel textChannel = bot.getInstance().getTextChannelById(textChannelId);
        textChannel.sendMessage(":arrow_up: " + event.getPlayer().getName() + " entrou no servidor. <@&" + joinNotificationRoleId + ">").queue();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (event.getPlayer().getName().toLowerCase().endsWith("_bot")) {
            return;
        }

        DiscordBot bot = NoGoalPlugin.getBot(); 
        Long textChannelId = NoGoalPlugin.getInstance().getConfig().getLong("text_channel_id");
        Long joinNotificationRoleId = NoGoalPlugin.getInstance().getConfig().getLong("join_notification_role_id");
        
        TextChannel textChannel = bot.getInstance().getTextChannelById(textChannelId);
        textChannel.sendMessage(":arrow_down: " + event.getPlayer().getName() + " saiu do servidor. <@&" + joinNotificationRoleId + ">").queue();
    }
}
