package dev.abidux.nogoalplugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import dev.abidux.nogoalplugin.bot.DiscordBot;
import dev.abidux.nogoalplugin.events.PlayerJoinQuit;

public class NoGoalPlugin extends JavaPlugin {

    private static NoGoalPlugin instance;
    private static DiscordBot discordBotInstance;

    @Override
    public void onEnable() {
        NoGoalPlugin.instance = this;
        this.saveDefaultConfig();
        
        String token = getConfig().getString("bot_token");
        if (token.equals("placeholder")) {
            Bukkit.getConsoleSender().sendMessage("§cNoGoalPlugin não foi configurado. Desativando.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        NoGoalPlugin.discordBotInstance = new DiscordBot(token);
        Bukkit.getScheduler().runTask(this, discordBotInstance::start);
        
        Bukkit.getPluginManager().registerEvents(new PlayerJoinQuit(), this);
        
        Bukkit.getConsoleSender().sendMessage("§aNoGoalPlugin iniciado.");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§cNoGoalPlugin finalizado.");
    }

    public static NoGoalPlugin getInstance() {
        return instance;
    }

    public static DiscordBot getBot() {
        return discordBotInstance;
    }
}