package dev.abidux.nogoalplugin;

import java.util.Set;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import dev.abidux.nogoalplugin.bot.DiscordBot;
import dev.abidux.nogoalplugin.commands.QolCommand;
import dev.abidux.nogoalplugin.events.InvClick;
import dev.abidux.nogoalplugin.events.PlayerDeath;
import dev.abidux.nogoalplugin.events.PlayerJoinQuit;
import dev.abidux.nogoalplugin.model.PlayerData;

public class NoGoalPlugin extends JavaPlugin {

    private static NoGoalPlugin instance;
    private static DiscordBot discordBotInstance;

    @Override
    public void onEnable() {
        NoGoalPlugin.instance = this;
        this.saveDefaultConfig();

        this.readPlayerData();
        
        String token = getConfig().getString("bot_token");
        if (token.equals("placeholder")) {
            Bukkit.getConsoleSender().sendMessage("§cNoGoalPlugin não foi configurado. Desativando.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        NoGoalPlugin.discordBotInstance = new DiscordBot(token);
        Bukkit.getScheduler().runTask(this, discordBotInstance::start);
        Bukkit.getScheduler().runTaskTimer(this, this::savePlayerData, 10 * 60 * 20, 10 * 60 * 20);
        
        Bukkit.getPluginManager().registerEvents(new PlayerJoinQuit(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeath(), this);
        Bukkit.getPluginManager().registerEvents(new InvClick(), this);

        getCommand("qol").setExecutor(new QolCommand());
        
        Bukkit.getConsoleSender().sendMessage("§aNoGoalPlugin iniciado.");
    }

    private void savePlayerData() {
        for (Entry<String, PlayerData> entry : PlayerData.PLAYER_DATA.entrySet()) {
            entry.getValue().saveTo(getConfig(), "player_data." + entry.getKey());
        }
        saveConfig();
    }

    private void readPlayerData() {
        if (!getConfig().isSet("player_data")) return;
        
        Set<String> playerNames = getConfig().getConfigurationSection("player_data").getKeys(false);
        for (String playerName : playerNames) {
            PlayerData data = PlayerData.fromConfig(getConfig(), "player_data." + playerName);
            data.save(playerName);
        }
    }

    @Override
    public void onDisable() {
        this.savePlayerData();
        Bukkit.getConsoleSender().sendMessage("§cNoGoalPlugin finalizado.");
    }

    public static NoGoalPlugin getInstance() {
        return instance;
    }

    public static DiscordBot getBot() {
        return discordBotInstance;
    }
}