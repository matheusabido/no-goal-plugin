package dev.abidux.nogoalplugin.model;

import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import lombok.Data;

@Data
public class PlayerData {
    public static final HashMap<String, PlayerData> PLAYER_DATA = new HashMap<>();

    private boolean keepInventoryActive;

    public static PlayerData get(String playerName) {
        return PLAYER_DATA.getOrDefault(playerName.toLowerCase(), new PlayerData());
    }

    public static PlayerData get(Player player) {
        return get(player.getName());
    }

    public void save(String playerName) {
        PLAYER_DATA.put(playerName.toLowerCase(), this);
    }

    public void save(Player player) {
        save(player.getName());
    }

    public static PlayerData fromConfig(FileConfiguration config, String baseKey) {
        PlayerData data = new PlayerData();
        data.setKeepInventoryActive(config.getBoolean(baseKey + ".keep_inventory_active"));
        return data;
    }

    public void saveTo(FileConfiguration config, String baseKey) {
        config.set(baseKey + ".keep_inventory_active", this.keepInventoryActive);
    }
}